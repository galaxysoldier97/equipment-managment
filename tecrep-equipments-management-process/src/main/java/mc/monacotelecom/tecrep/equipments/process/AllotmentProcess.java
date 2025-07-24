package mc.monacotelecom.tecrep.equipments.process;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.configuration.AllotmentFileNameProperties;
import mc.monacotelecom.tecrep.equipments.dto.AllotmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.AllotmentProvisionedDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.AllotmentExportRequestDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.AllotmentRequestDTO;
import mc.monacotelecom.tecrep.equipments.entity.*;
import mc.monacotelecom.tecrep.equipments.enums.AllotmentType;
import mc.monacotelecom.tecrep.equipments.enums.SimCardSimProfile;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.exceptions.TemplateProcessingException;
import mc.monacotelecom.tecrep.equipments.mapper.AllotmentMapper;
import mc.monacotelecom.tecrep.equipments.operator.OperatorService;
import mc.monacotelecom.tecrep.equipments.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AllotmentProcess {

    @Value("${prepay.max-credit:999}")
    private String maxCredit;

    private final AllotmentSummaryRepository allotmentSummaryRepository;
    private final InventoryPoolRepository inventoryPoolRepository;
    private final SimCardRepository simCardRepository;
    private final AllotmentMapper allotmentMapper;
    private final BatchRepository batchRepository;
    private final FileConfigurationRepository fileConfigurationRepository;
    private final Configuration freemarkerConfiguration;
    private final Clock clock;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final OperatorService operatorService;
    private final AllotmentFileNameProperties fileNameProperties;

    public Page<AllotmentDTO> getAll(Pageable pageable) {
        return allotmentSummaryRepository.findAll(pageable).map(allotmentMapper::toDto);
    }

    public AllotmentDTO getById(Long id) {
        return allotmentMapper.toDto(findById(id));
    }

    private AllotmentSummary findById(Long id) {
        return allotmentSummaryRepository.findById(id).orElseThrow(() ->
                new EqmNotFoundException(localizedMessageBuilder, ALLOTMENT_SUMMARY_NOT_FOUND_ID, id));
    }

    public Page<AllotmentDTO> getAllByBatchNumber(Long batchNumber, Pageable pageable) {
        return allotmentSummaryRepository.findAllByBatchBatchNumber(batchNumber, pageable).map(allotmentMapper::toDto);
    }


    public AllotmentDTO add(AllotmentRequestDTO allotmentRequestDTO) {
        if (operatorService.isEir() && AllotmentType.PREPAID.equals(allotmentRequestDTO.getAllotmentType())) {
            // The "Price Plan" and "Initial Credit" parameters are mandatory when the Type is set to "Prepaid".
            validateParametersForPrepaid(allotmentRequestDTO);
        }

        Batch batch = batchRepository.findById(allotmentRequestDTO.getBatchNumber()).orElseThrow(() ->
                new EqmValidationException(localizedMessageBuilder, ALLOTMENT_BATCH_NOT_VALID, allotmentRequestDTO.getBatchNumber()));
        InventoryPool actualInventoryPool = batch.getInventoryPool();

        // Note that InventoryPool is not a necessary request parameter since it can be determined from
        // the list of remaining non-allotted SIM cards in the batch.
        // However, if it is provided it must be validated.
        if (allotmentRequestDTO.getInventoryPoolCode() != null) {
            InventoryPool requestInventoryPool = inventoryPoolRepository.findByCode(allotmentRequestDTO.getInventoryPoolCode()).orElseThrow(() ->
                    new EqmValidationException(localizedMessageBuilder, INVENTORY_POOL_NOT_FOUND_CODE, allotmentRequestDTO.getInventoryPoolCode()));
            if (!actualInventoryPool.getInventoryPoolId().equals(requestInventoryPool.getInventoryPoolId())
                    && !actualInventoryPool.getCode().equals(requestInventoryPool.getCode())
            ) {
                throw new EqmValidationException(localizedMessageBuilder, ALLOTMENT_INVENTORY_POOL_BATCH, allotmentRequestDTO.getBatchNumber());
            }
        }

        if (operatorService.isEir() && !SimCardSimProfile.REPLACEMENT.equals(actualInventoryPool.getSimProfile()) && allotmentRequestDTO.getPackWithHandset() == null) {
            // PackagedWithHandset parameter is mandatory when the Inventory Pool is NOT REPLACEMENT.
            throw new EqmValidationException(localizedMessageBuilder, ALLOTMENT_PACKAGED_HANDSET_PREREQUISITE);
        }

        // find if the firstSerialNumber exist for SIM cards in the batch
        if (!AllotmentRequestDTO.DEFAULT_FIRST_SIMCARD_NUMBER.equals(allotmentRequestDTO.getFirstSerialNumber())) {
            Integer existSimcard = simCardRepository.countSimCardBySerialNumberAndBatchNumber(allotmentRequestDTO.getFirstSerialNumber(), String.valueOf(allotmentRequestDTO.getBatchNumber()));
            if (existSimcard == 0) {
                throw new EqmValidationException(localizedMessageBuilder, SIMCARD_SN_NOT_FOUND_WITH_BATCH_NUMBER, allotmentRequestDTO.getFirstSerialNumber(), String.valueOf(allotmentRequestDTO.getBatchNumber()));
            }
        }

        // find the remaining non-allotted SIM cards in the batch
        // In case of Prepaid, sim cards must have an associated number
        List<SimCard> simCards = AllotmentType.PREPAID.equals(allotmentRequestDTO.getAllotmentType()) ?
                simCardRepository.findAllotableSimcardWithNumber(allotmentRequestDTO.getFirstSerialNumber(), String.valueOf(allotmentRequestDTO.getBatchNumber())) :
                simCardRepository.findAllotableSimcardWithoutNumber(allotmentRequestDTO.getFirstSerialNumber(), String.valueOf(allotmentRequestDTO.getBatchNumber()));

        if ((allotmentRequestDTO.getQuantity() > simCards.size()) || (simCards.isEmpty())) {
            throw new EqmValidationException(localizedMessageBuilder, ALLOTMENT_QUANTITY_TOO_HIGH, allotmentRequestDTO.getQuantity(), simCards.size(), allotmentRequestDTO.getBatchNumber());
        }

        AllotmentSummary allotmentSummary = allotmentMapper.toEntity(allotmentRequestDTO);

        var lastAllotmentForBatch = allotmentSummaryRepository.findAllByBatchBatchNumberOrderByAllotmentNumberDesc(batch.getBatchNumber())
                .stream()
                .findFirst();

        allotmentSummary.setAllotmentNumber(lastAllotmentForBatch.map(summary -> summary.getAllotmentNumber() + 1).orElse(1));
        allotmentSummary.setBatch(batch);
        allotmentSummary.setPreProvisioning(AllotmentType.PREPAID.equals(allotmentRequestDTO.getAllotmentType()));
        allotmentSummary.setInventoryPool(actualInventoryPool);
        // save the AllotmentSummary entity
        allotmentSummaryRepository.save(allotmentSummary);

        // update SimCard with allotment id
        // Might be optimized in a single request, but we'd need to make the historization ourselves
        for (int i = 0; i < allotmentRequestDTO.getQuantity(); i++) {
            simCards.get(i).setAllotment(allotmentSummary);
            // For Prepaid, SIMs end up BOOKED, for other cases they stay AVAILABLE
            if (AllotmentType.PREPAID.equals(allotmentRequestDTO.getAllotmentType())) {
                simCards.get(i).setStatus(Status.BOOKED);
            }
        }
        simCardRepository.saveAll(simCards);

        return allotmentMapper.toDto(allotmentSummaryRepository.findById(allotmentSummary.getAllotmentId())
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, ALLOTMENT_SUMMARY_NOT_FOUND_ID, allotmentSummary.getAllotmentId())));
    }

    private void validateParametersForPrepaid(AllotmentRequestDTO allotmentRequestDTO) {
        if ((allotmentRequestDTO.getPricePlan() == null)
                || (allotmentRequestDTO.getPricePlan().isBlank())
                || (allotmentRequestDTO.getInitialCredit() == null)
                || (allotmentRequestDTO.getInitialCredit() < 0)) {
            throw new EqmValidationException(localizedMessageBuilder, ALLOTMENT_TYPE_PREPAID_PREREQUISITE, allotmentRequestDTO.getAllotmentType());
        }
        if (allotmentRequestDTO.getInitialCredit() > Integer.parseInt(maxCredit)) {
            throw new EqmValidationException(localizedMessageBuilder, ALLOTMENT_CREDIT_TOO_HIGH, allotmentRequestDTO.getInitialCredit().toString(), maxCredit);
        }
    }

    /**
     * Export for Packaging.
     */
    public ByteArrayInputStream export(AllotmentExportRequestDTO allotmentExportRequestDTO) {

        AllotmentSummary allotment = findById(allotmentExportRequestDTO.getAllotmentId());
        FileConfiguration fileConfiguration = fileConfigurationRepository.findByName(allotmentExportRequestDTO.getFileConfigurationName())
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, FILE_CONFIGURATION_NAME_NOT_FOUND, allotmentExportRequestDTO.getFileConfigurationName()));

        // write the file header first
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        writeFileHeader(output, fileConfiguration, allotment);

        // then write the data records to the file
        List<SimCard> simCards = simCardRepository.findByAllotment(allotment);
        writeDataRecord(output, simCards, fileConfiguration, allotment);

        allotment.setSentToLogisticsDate(LocalDateTime.now(clock));

        return new ByteArrayInputStream(output.toByteArray());
    }

    public String getAllotmentFilename(AllotmentExportRequestDTO allotmentExportRequestDTO) {
        AllotmentSummary allotment = findById(allotmentExportRequestDTO.getAllotmentId());
        FileConfiguration fileConfiguration = fileConfigurationRepository.findByName(allotmentExportRequestDTO.getFileConfigurationName())
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, FILE_CONFIGURATION_NAME_NOT_FOUND, allotmentExportRequestDTO.getFileConfigurationName()));

        return fileConfiguration.getPrefix()
                + String.format(fileNameProperties.getFormatBatchId(), allotment.getBatch().getBatchNumber())
                + String.format(fileNameProperties.getFormatAllotmentNumber(), allotment.getAllotmentNumber())
                + fileConfiguration.getSuffix();
    }


    /**
     * Callback after pre provisioning has been completed for the Allotment.
     *
     * @param allotmentProvisionedDTO AllotmentProvisionedDTO
     * @return AllotmentDTO
     */
    public AllotmentDTO provisioned(AllotmentProvisionedDTO allotmentProvisionedDTO) {

        AllotmentSummary allotment = findById(allotmentProvisionedDTO.getAllotmentId());

        if (!allotment.getQuantity().equals(allotmentProvisionedDTO.getSuccessQuantity() + allotmentProvisionedDTO.getFailures().size()))
            throw new EqmValidationException(localizedMessageBuilder, ALLOTMENT_PROVISIONED_QUANTITY_NOT_VALID, allotmentProvisionedDTO.getSuccessQuantity());

        // unlink the failures from the Allotment
        if (!allotmentProvisionedDTO.getFailures().isEmpty()) {
            for (AllotmentProvisionedDTO.PartialSimCardDTO simCardDTO : allotmentProvisionedDTO.getFailures()) {
                SimCard simCard = simCardRepository.findByImsiNumber(simCardDTO.getImsiNumber())
                        .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, SIMCARD_NOT_FOUND_IMSI, simCardDTO.getImsiNumber()));
                // unlink the SIM card from the Allotment
                simCard.setAllotment(null);
                simCard.setStatus(Status.AVAILABLE);
                simCardRepository.save(simCard);
            }
        }

        // update the state of the Allotment
        allotment.setProvisionedDate(LocalDateTime.now(clock));
        allotment.setQuantity(allotmentProvisionedDTO.getSuccessQuantity());
        allotment.setPreProvisioningFailures(allotmentProvisionedDTO.getFailures().size());

        return allotmentMapper.toDto(allotmentSummaryRepository.save(allotment));
    }

    private void writeFileHeader(ByteArrayOutputStream output, FileConfiguration configuration, AllotmentSummary allotment) {
        String headerFormat = configuration.getHeaderFormat();
        Map<String, String> dataModel = new HashMap<>();
        dataModel.put("QUANTITY", String.valueOf(allotment.getQuantity()));
        dataModel.put("BATCH", String.valueOf(allotment.getBatch().getBatchNumber()));
        dataModel.put("ALLOTMENT_TYPE", String.valueOf(allotment.getAllotmentType()));
        dataModel.put("PACK_WITH_HANDSET", String.valueOf(allotment.getPackWithHandset()));
        dataModel.put("INITIAL_CREDIT", String.valueOf(allotment.getInitialCredit()));
        dataModel.put("PRICE_PLAN", allotment.getPricePlan());
        dataModel.put("ARTWORK", allotment.getBatch().getSimCardGenerationConfiguration().getArtwork());
        dataModel.put("TRANSPORT_KEY", allotment.getBatch().getSimCardGenerationConfiguration().getTransportKey());
        dataModel.put("ALGORITHM_VERSION", String.valueOf(allotment.getBatch().getSimCardGenerationConfiguration().getAlgorithmVersion()));
        dataModel.put("SIM_REFERENCE", allotment.getBatch().getSimCardGenerationConfiguration().getSimReference());
        dataModel.put("TYPE", allotment.getBatch().getSimCardGenerationConfiguration().getType());
        String renderedTemplate = processTemplate(headerFormat, dataModel);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.US_ASCII))) {
            writer.write(renderedTemplate);
        } catch (IOException ioEx) {
            throw new TemplateProcessingException("Failed to write the HEADER of file for batch: " + allotment.getBatch().getBatchNumber(), ioEx);
        }
    }

    private void writeDataRecord(ByteArrayOutputStream output, List<SimCard> simCards, FileConfiguration configuration, AllotmentSummary allotment) {
        String recordFormat = configuration.getRecordFormat();

        simCards.forEach(simCard -> {
            Map<String, String> dataModel = new HashMap<>();
            dataModel.put("BATCH", String.format("%05d", Long.valueOf(simCard.getBatchNumber())));
            dataModel.put("ALLOTMENT_ID", String.format("%02d", allotment.getAllotmentId()));
            dataModel.put("IMSI", simCard.getImsiNumber());
            dataModel.put("SERIAL_NUMBER", simCard.getSerialNumber());
            dataModel.put("CHECK_DIGIT", simCard.getCheckDigit() == null ? null : String.valueOf(simCard.getCheckDigit()));
            dataModel.put("NUMBER", simCard.getNumber());
            dataModel.put("PIN_1_CODE", simCard.getPin1Code());
            dataModel.put("PUK_1_CODE", simCard.getPuk1Code());
            dataModel.put("PIN_2_CODE", simCard.getPin2Code());
            dataModel.put("PUK_2_CODE", simCard.getPuk2Code());
            dataModel.put("TOP_UP", allotment.topUp());

            String renderedTemplate = processTemplate(recordFormat, dataModel);

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.US_ASCII))) {
                writer.write(renderedTemplate);
            } catch (IOException ioEx) {
                throw new TemplateProcessingException("Failed to write a record for SIM: " + simCard, ioEx);
            }
        });
    }

    private String processTemplate(String headerFormat, Map<String, String> dataModel) {
        try {
            Template template = new Template("header", headerFormat, freemarkerConfiguration);
            Writer out = new StringWriter();
            template.process(dataModel, out);
            return out.toString();
        } catch (IOException | TemplateException e) {
            throw new TemplateProcessingException(e.getMessage(), e);
        }
    }
}
