package mc.monacotelecom.tecrep.equipments.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.CommonFunctions;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.configuration.DefaultSimCardGenerationProperties;
import mc.monacotelecom.tecrep.equipments.dto.BatchDTO;
import mc.monacotelecom.tecrep.equipments.dto.SimCardPartialDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.CreateBatchRequestDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.GenerateSimCardsRequestDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddSimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchBatchDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.BatchDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.Batch;
import mc.monacotelecom.tecrep.equipments.entity.SequenceBatchNumber;
import mc.monacotelecom.tecrep.equipments.entity.SequenceICCID;
import mc.monacotelecom.tecrep.equipments.entity.SequenceMSIN;
import mc.monacotelecom.tecrep.equipments.entity.SimCardGenerationConfiguration;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.exceptions.DeleteFileException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.exceptions.UploadFileException;
import mc.monacotelecom.tecrep.equipments.mapper.BatchMapper;
import mc.monacotelecom.tecrep.equipments.mapper.SimCardMapper;
import mc.monacotelecom.tecrep.equipments.process.simcard.SimCardCreator;
import mc.monacotelecom.tecrep.equipments.process.simcardgenerator.SimCardIdentifiersGenerator;
import mc.monacotelecom.tecrep.equipments.repository.BatchRepository;
import mc.monacotelecom.tecrep.equipments.repository.InventoryPoolRepository;
import mc.monacotelecom.tecrep.equipments.repository.SequenceBatchNumberRepository;
import mc.monacotelecom.tecrep.equipments.repository.SequenceICCIDRepository;
import mc.monacotelecom.tecrep.equipments.repository.SequenceMSINRepository;
import mc.monacotelecom.tecrep.equipments.repository.SimCardGenerationConfigurationRepository;
import mc.monacotelecom.tecrep.equipments.repository.SimCardRepository;
import mc.monacotelecom.tecrep.equipments.repository.specification.BatchSpecifications;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.BATCH_DELETE_IMPORT_FAILURE;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.BATCH_DOWNLOAD_IMPORT_NOT_FOUND;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.BATCH_NOT_DELETABLE_ALLOTED;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.BATCH_NOT_DELETABLE_PROCESSED;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.BATCH_NOT_FOUND;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.BATCH_NOT_FOUND_FOR_IMPORT_FILE_NAME;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.BATCH_UPLOAD_IMPORT_FAILURE;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.ICCID_SEQUENCE_FINISHED;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.INVENTORY_POOL_NOT_FOUND_CODE;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.MSIN_SEQUENCE_FINISHED;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.SIMCARD_GENERATION_CONFIGURATION_NAME_NOT_FOUND;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.SIMCARD_GENERATION_MISSING_BATCH_SEQUENCE;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.SIMCARD_GENERATION_MISSING_ICCID_SEQUENCE;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.SIMCARD_GENERATION_MISSING_MSIN_SEQUENCE;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchProcess {

    private final BatchRepository batchRepository;
    private final SimCardRepository simCardRepository;
    private final BatchMapper batchMapper;
    private final Clock clock;
    private final InventoryPoolRepository inventoryPoolRepository;
    private final SimCardGenerationConfigurationRepository simCardGenerationConfigurationRepository;
    private final SequenceMSINRepository sequenceMSINRepository;
    private final SequenceICCIDRepository sequenceICCIDRepository;
    private final SequenceBatchNumberRepository sequenceBatchNumberRepository;
    private final SimCardCreator simCardCreator;
    private final SimCardIdentifiersGenerator simCardIdentifiersGeneration;
    private final DefaultSimCardGenerationProperties simCardGenerationProperties;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final SimCardMapper simcardMapper;

    @Value("${incoming.files.processed-directory:/tmp}")
    private String processedDirectory;

    @Value("${incoming.files.queue-directory:/tmp}")
    private String queueDirectory;


    @Deprecated(since = "2.21.0", forRemoval = true)
    public Page<BatchDTO> getAllV1(final Pageable pageable) {
        return batchRepository.findAll(pageable).map(batchMapper::toDto);
    }

    public Page<BatchDTOV2> getAll(final Pageable pageable) {
        return batchRepository.findAll(pageable).map(batchMapper::toDtoV2);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public Page<BatchDTO> searchV1(final SearchBatchDTO searchBatchDTO, final Pageable pageable) {
        final Specification<Batch> specification = this.prepareSpecifications(searchBatchDTO);

        return batchRepository.findAll(specification, pageable).map(batchMapper::toDto);
    }

    public Page<BatchDTOV2> search(final SearchBatchDTO searchBatchDTO, final Pageable pageable) {
        final Specification<Batch> specification = this.prepareSpecifications(searchBatchDTO);

        return batchRepository.findAll(specification, pageable).map(batchMapper::toDtoV2);
    }

    private Specification<Batch> prepareSpecifications(final SearchBatchDTO searchBatchDTO) {
        Specification<Batch> specification;

        specification = searchBatchDTO.isProcessable() ? Specification.where(BatchSpecifications.hasNotProcessedDate()).and(BatchSpecifications.hasReturnedDate()) : null;
        specification = StringUtils.isNotBlank(searchBatchDTO.getImportFileName()) ? CommonFunctions.addSpecification(specification, BatchSpecifications.hasImportFileNameLike(searchBatchDTO.getImportFileName())) : specification;
        specification = StringUtils.isNotBlank(searchBatchDTO.getExportFileName()) ? CommonFunctions.addSpecification(specification, BatchSpecifications.hasExportFileNameLike(searchBatchDTO.getExportFileName())) : specification;
        specification = StringUtils.isNotBlank(searchBatchDTO.getInventoryPoolCode()) ? CommonFunctions.addSpecification(specification, BatchSpecifications.hasInventoryPoolCode(searchBatchDTO.getInventoryPoolCode())) : specification;
        specification = StringUtils.isNotBlank(searchBatchDTO.getConfigurationName()) ? CommonFunctions.addSpecification(specification, BatchSpecifications.hasConfigurationName(searchBatchDTO.getConfigurationName())) : specification;

        return specification;
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public BatchDTO setReturnedDateV1(final Long batchNumber) {
        final var batch = batchRepository.findById(batchNumber).orElseThrow(() ->
                new EqmNotFoundException(localizedMessageBuilder, BATCH_NOT_FOUND, batchNumber));
        return setReturnedDateV1(batch);
    }

    public BatchDTOV2 setReturnedDate(final Long batchNumber) {
        final var batch = batchRepository.findById(batchNumber).orElseThrow(() ->
                new EqmNotFoundException(localizedMessageBuilder, BATCH_NOT_FOUND, batchNumber));
        return setReturnedDate(batch);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public BatchDTO setReturnedDateV1(final String importFileName) {
        final var batch = batchRepository.findByImportFileName(importFileName).orElseThrow(() ->
                new EqmNotFoundException(localizedMessageBuilder, BATCH_NOT_FOUND_FOR_IMPORT_FILE_NAME, importFileName));
        return setReturnedDateV1(batch);
    }

    public BatchDTOV2 setReturnedDate(final String importFileName) {
        final var batch = batchRepository.findByImportFileName(importFileName).orElseThrow(() ->
                new EqmNotFoundException(localizedMessageBuilder, BATCH_NOT_FOUND_FOR_IMPORT_FILE_NAME, importFileName));
        return setReturnedDate(batch);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public BatchDTO setReturnedDateV1(final Batch batch) {
        log.info(String.format("Setting returned date on batch '%s'", batch.getBatchNumber()));
        batch.setReturnedDate(LocalDateTime.now(clock));
        batchRepository.save(batch);
        return batchMapper.toDto(batch);
    }

    public BatchDTOV2 setReturnedDate(final Batch batch) {
        log.info(String.format("Setting returned date on batch '%s'", batch.getBatchNumber()));
        batch.setReturnedDate(LocalDateTime.now(clock));
        batchRepository.save(batch);
        return batchMapper.toDtoV2(batch);
    }

    public BatchDTOV2 clearReturnedDate(final Batch batch) {
        log.info("Clearing returned date on batch '{}'", batch.getBatchNumber());
        batch.setReturnedDate(null);
        batchRepository.save(batch);
        return batchMapper.toDtoV2(batch);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public BatchDTO setProcessedDateV1(final Long batchNumber) {
        final var batch = batchRepository.findById(batchNumber).orElseThrow(() ->
                new EqmNotFoundException(localizedMessageBuilder, BATCH_NOT_FOUND, batchNumber));
        log.info(String.format("Setting processed date on batch '%s'", batch.getBatchNumber()));
        batch.setProcessedDate(LocalDateTime.now(clock));
        batchRepository.save(batch);
        return batchMapper.toDto(batch);
    }

    public BatchDTOV2 setProcessedDate(final Long batchNumber) {
        final var batch = batchRepository.findById(batchNumber).orElseThrow(() ->
                new EqmNotFoundException(localizedMessageBuilder, BATCH_NOT_FOUND, batchNumber));
        log.info(String.format("Setting processed date on batch '%s'", batch.getBatchNumber()));
        batch.setProcessedDate(LocalDateTime.now(clock));
        batchRepository.save(batch);
        return batchMapper.toDtoV2(batch);
    }

    /**
     * Create a new Batch using Inventory Pool and SIM Card Generation Configuration
     *
     * @param createBatchRequestDTO DTO with IP and SCGC
     * @return Batch DTO
     */
    @Deprecated(since = "2.21.0", forRemoval = true)
    public BatchDTO createBatchV1(final CreateBatchRequestDTO createBatchRequestDTO) {
        final var inventoryPool = inventoryPoolRepository.findByCode(createBatchRequestDTO.getInventoryPoolCode())
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, INVENTORY_POOL_NOT_FOUND_CODE, createBatchRequestDTO.getInventoryPoolCode()));
        final var configuration = simCardGenerationConfigurationRepository.findByName(createBatchRequestDTO.getConfigurationName())
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, SIMCARD_GENERATION_CONFIGURATION_NAME_NOT_FOUND, createBatchRequestDTO.getConfigurationName()));

        final var creationDate = LocalDateTime.now(clock);

        // Calculate next Batch Number manually (database auto-increment cannot be trusted)
        final AtomicReference<Long> nextBatchNumberComputed = new AtomicReference<>();
        sequenceBatchNumberRepository.findFirstByOrderByValueDesc().ifPresentOrElse(
                x -> nextBatchNumberComputed.set(x.getValue() + 1),
                () -> {
                    throw new EqmValidationException(localizedMessageBuilder, SIMCARD_GENERATION_MISSING_BATCH_SEQUENCE);
                }
        );

        final SequenceBatchNumber nextBatchNumber = sequenceBatchNumberRepository.save(new SequenceBatchNumber(nextBatchNumberComputed.get()));

        var batch = new Batch();
        batch.setBatchNumber(nextBatchNumber.getValue());
        batch.setInventoryPool(inventoryPool);
        batch.setSimCardGenerationConfiguration(configuration);
        batch.setCreationDate(creationDate);
        batch.setExportFileName(configuration.getExportFileConfiguration().getPrefix() + nextBatchNumber.getFormattedValue() + configuration.getExportFileConfiguration().getSuffix());
        batch.setImportFileName(configuration.getImportFileConfiguration().getPrefix() + nextBatchNumber.getFormattedValue() + configuration.getImportFileConfiguration().getSuffix());

        return batchMapper.toDto(batchRepository.saveAndFlush(batch));
    }

    /**
     * Create a new Batch using Inventory Pool and SIM Card Generation Configuration
     *
     * @param createBatchRequestDTO DTO with IP and SCGC
     * @return Batch DTO
     */
    public BatchDTOV2 createBatch(final CreateBatchRequestDTO createBatchRequestDTO) {
        final var inventoryPool = inventoryPoolRepository.findByCode(createBatchRequestDTO.getInventoryPoolCode())
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, INVENTORY_POOL_NOT_FOUND_CODE, createBatchRequestDTO.getInventoryPoolCode()));
        final var configuration = simCardGenerationConfigurationRepository.findByName(createBatchRequestDTO.getConfigurationName())
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, SIMCARD_GENERATION_CONFIGURATION_NAME_NOT_FOUND, createBatchRequestDTO.getConfigurationName()));

        final var creationDate = LocalDateTime.now(clock);

        // Calculate next Batch Number manually (database auto-increment cannot be trusted)
        final AtomicReference<Long> nextBatchNumberComputed = new AtomicReference<>();
        sequenceBatchNumberRepository.findFirstByOrderByValueDesc().ifPresentOrElse(
                x -> nextBatchNumberComputed.set(x.getValue() + 1),
                () -> {
                    throw new EqmValidationException(localizedMessageBuilder, SIMCARD_GENERATION_MISSING_BATCH_SEQUENCE);
                }
        );

        final SequenceBatchNumber nextBatchNumber = sequenceBatchNumberRepository.save(new SequenceBatchNumber(nextBatchNumberComputed.get()));

        var batch = new Batch();
        batch.setBatchNumber(nextBatchNumber.getValue());
        batch.setInventoryPool(inventoryPool);
        batch.setSimCardGenerationConfiguration(configuration);
        batch.setCreationDate(creationDate);
        batch.setExportFileName(configuration.getExportFileConfiguration().getPrefix() + nextBatchNumber.getFormattedValue() + configuration.getExportFileConfiguration().getSuffix());
        batch.setImportFileName(configuration.getImportFileConfiguration().getPrefix() + nextBatchNumber.getFormattedValue() + configuration.getImportFileConfiguration().getSuffix());

        return batchMapper.toDtoV2(batchRepository.saveAndFlush(batch));
    }

    /**
     * Increment the current SequenceICCID in database according to the quantity passed
     * Returns the original starting SequenceICCID value
     */
    @Transactional
    public Long updateSequenceIccid(int quantity, String name) {
        var currentIccid = sequenceICCIDRepository.findFirstByNameOrderByValueDesc(name)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, SIMCARD_GENERATION_MISSING_ICCID_SEQUENCE, name));
        final var originalIccidValue = currentIccid.getValue();
        log.info(String.format("Incrementing ICCID sequence '%s' from '%s' with quantity '%s'", name, currentIccid.getValue(), quantity));

        // Check that the sequence won't overlap another one
        sequenceICCIDRepository.findFirstByValueBetween(originalIccidValue + 1, originalIccidValue + quantity).ifPresent(overlappingIccid -> {
            throw new EqmValidationException(localizedMessageBuilder, ICCID_SEQUENCE_FINISHED,
                    name,
                    String.valueOf(overlappingIccid.getValue()),
                    overlappingIccid.getName());
        });

        final List<SequenceICCID> newIccids = new ArrayList<>();
        for (var index = 1; index <= quantity; index++) {
            final long nextValue = currentIccid.getValue() + 1;

            final var newIccid = new SequenceICCID(nextValue, name);
            newIccids.add(newIccid);
            currentIccid = newIccid;
        }
        sequenceICCIDRepository.saveAll(newIccids);

        log.info(String.format("New ICCID sequence value is now '%s'", currentIccid.getValue()));
        return originalIccidValue;
    }

    /**
     * Increment the current SequenceMSIN in database according to the quantity passed and sequence name
     * Returns the original starting SequenceMSIN value
     */
    @Transactional
    public Long updateSequenceMsin(int quantity, String name) {
        var currentMsin = sequenceMSINRepository.findFirstByNameOrderByValueDesc(name)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, SIMCARD_GENERATION_MISSING_MSIN_SEQUENCE, name));
        final var originalMsinValue = currentMsin.getValue();
        log.info(String.format("Incrementing MSIN sequence '%s' from '%s' with quantity '%s'", name, originalMsinValue, quantity));

        // Check that the sequence won't overlap another one
        sequenceMSINRepository.findFirstByValueBetween(originalMsinValue + 1, originalMsinValue + quantity).ifPresent(overlappingMsin -> {
            throw new EqmValidationException(localizedMessageBuilder, MSIN_SEQUENCE_FINISHED,
                    name,
                    String.valueOf(overlappingMsin.getValue()),
                    overlappingMsin.getName());
        });

        final List<SequenceMSIN> newMsins = new ArrayList<>();
        for (var index = 1; index <= quantity; index++) {
            var nextValue = currentMsin.getValue() + 1;

            final var newMsin = new SequenceMSIN(nextValue, name);
            newMsins.add(newMsin);
            currentMsin = newMsin;
        }
        sequenceMSINRepository.saveAll(newMsins);

        log.info(String.format("New MSIN sequence '%s' value is now '%s'", name, currentMsin.getValue()));
        return originalMsinValue;
    }

    /**
     * Careful, single transaction per simcard is the wanted behavior, as with too many items in the transaction,
     * dead lock issues appear in a regular environment.
     *
     * @param batch         {@link Batch} containing the SIM Card
     * @param configuration {@link SimCardGenerationConfiguration} related to the generation process
     * @param number        msisdn to associate with the simcard, if available
     * @return {@link SimCardPartialDTO} representing the new SIM Card
     */
    @Transactional
    public SimCardPartialDTO generateSimCard(final Batch batch,
                                             final SimCardGenerationConfiguration configuration,
                                             final GenerateSimCardsRequestDTO generateSimCardsRequestDTO,
                                             final String number,
                                             final Long iccidSequence,
                                             final Long msinSequence) {

        final String formattedSequenceICCID = String.format(simCardGenerationProperties.getFormatICCID(), iccidSequence);
        final String formattedSequenceMSIN = String.format(simCardGenerationProperties.getFormatMSIN(), msinSequence);

        final String serialNumber = simCardIdentifiersGeneration.generateSerialNumber(batch, formattedSequenceICCID, formattedSequenceMSIN, generateSimCardsRequestDTO, configuration);
        final String imsiNumber = simCardIdentifiersGeneration.generateImsiNumber(batch, formattedSequenceICCID, formattedSequenceMSIN, generateSimCardsRequestDTO, configuration);

        final var newSIM = new AddSimCardDTOV2();
        newSIM.setImsiNumber(imsiNumber);
        newSIM.setSerialNumber(serialNumber);
        newSIM.setProviderName(configuration.getProvider().getName());
        newSIM.setPlmnCode(configuration.getPlmn().getCode());
        newSIM.setAccessType(AccessType.MOBILE);
        newSIM.setInventoryPoolCode(batch.getInventoryPool().getCode());
        newSIM.setTransportKey(configuration.getTransportKey());
        newSIM.setAlgorithmVersion(configuration.getAlgorithmVersion());
        newSIM.setBatchNumber(String.valueOf(batch.getBatchNumber()));
        newSIM.setEsim(generateSimCardsRequestDTO.isEsim());
        if (StringUtils.isNotBlank(number)) {
            newSIM.setNumber(number);
        }

        return simcardMapper.toPartialDto(simCardCreator.create(newSIM));
    }

    /**
     * Saves an incoming file as the expected import file for a given batch
     *
     * @param batchNumber Related batch, to retrieve the expected name of the file
     * @param inputFile   Import file returned by the provider, to be saved on the local filesystem
     * @throws EqmNotFoundException if batch is not found
     * @throws UploadFileException  if a failure happened during the save process
     */
    public void uploadImportFile(final Long batchNumber, final MultipartFile inputFile) {
        final var batch = batchRepository.findById(batchNumber).orElseThrow(() ->
                new EqmNotFoundException(localizedMessageBuilder, BATCH_NOT_FOUND, batchNumber));
        final var localFile = new File(new File(queueDirectory), batch.getImportFileName());
        try {
            inputFile.transferTo(localFile);
        } catch (IOException e) {
            throw new UploadFileException(localizedMessageBuilder, BATCH_UPLOAD_IMPORT_FAILURE);
        }
        setReturnedDate(batch);
    }

    /**
     * Deletes the batch import file, provided that the import operation was not
     * yet executed
     *
     * @param batchNumber Related batch
     * @throws EqmNotFoundException if batch is not found
     * @throws DeleteFileException if a failure happened during the deletion
     */
    public void deleteImportFile(final Long batchNumber) {
        final var batch = batchRepository.findById(batchNumber)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, BATCH_NOT_FOUND, batchNumber));

        // Only process this request if the import was not processed yet.
        if (batch.getProcessedDate() == null) {

            // No need to care if the file does not exist, as we're clearing the
            // returned date anyway and the user can start a new upload.
            try {
                Files.deleteIfExists(Path.of(queueDirectory, batch.getImportFileName()));
            } catch (IOException e) {
                throw new DeleteFileException(localizedMessageBuilder, BATCH_DELETE_IMPORT_FAILURE);
            }

            // Reset the return date, so that we may start an upload again
            clearReturnedDate(batch);
        }
    }
    /**
     * Return an import file from the dedicated directory, if available
     *
     * @param fileName Name of the import file to return
     * @return InputStream of the file, if found on the server
     * @throws EqmNotFoundException if file is not found on the server
     */
    public InputStream getImportFile(final String fileName) {
        try {
            return Files.newInputStream(Paths.get(processedDirectory + FileSystems.getDefault().getSeparator() + fileName));
        } catch (IOException e) {
            throw new EqmNotFoundException(localizedMessageBuilder, BATCH_DOWNLOAD_IMPORT_NOT_FOUND, fileName);
        }
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public BatchDTO getByBatchNumberV1(final Long batchNumber) {
        return batchMapper.toDto(getEntityByBatchNumber(batchNumber));
    }

    public BatchDTOV2 getByBatchNumber(final Long batchNumber) {
        return batchMapper.toDtoV2(getEntityByBatchNumber(batchNumber));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Batch getEntityByBatchNumber(final Long batchNumber) {
        return batchRepository.findById(batchNumber)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, BATCH_NOT_FOUND, batchNumber));
    }

    public void deleteByBatchNumber(final Long batchNumber) {
        final var batch = getEntityByBatchNumber(batchNumber);

        if (!batch.getAllotmentSummaries().isEmpty()) {
            throw new EqmValidationException(localizedMessageBuilder, BATCH_NOT_DELETABLE_ALLOTED, batch.getBatchNumber());
        }

        if (Objects.nonNull(batch.getProcessedDate())) {
            throw new EqmValidationException(localizedMessageBuilder, BATCH_NOT_DELETABLE_PROCESSED, batch.getBatchNumber());
        }

        simCardRepository.deleteByBatchNumber(String.valueOf(batch.getBatchNumber()));

        batchRepository.delete(batch);
    }
}
