package mc.monacotelecom.tecrep.equipments.importer.implementations.simcard;

import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.importer.process.NamedAbstractImporter;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.importer.data.GenericEquipementCsvLines;
import mc.monacotelecom.tecrep.equipments.importer.implementations.context.SimCardDefaultImporterContext;
import mc.monacotelecom.tecrep.equipments.importer.tags.Tag;
import mc.monacotelecom.tecrep.equipments.mapper.SimCardMapper;
import mc.monacotelecom.tecrep.equipments.repository.BatchRepository;
import mc.monacotelecom.tecrep.equipments.repository.SimCardRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.BATCH_NOT_FOUND;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.SIMCARD_SN_NOT_FOUND_WITH_BATCH_NUMBER;

@Service
@Slf4j
public class SimCardIdemiaEirImporter extends NamedAbstractImporter<SimCard, GenericEquipementCsvLines.SimCardIdemiaLine> {

    private final SimCardMapper simcardMapper;

    private final SimCardImporterHelper helper;
    private final SimCardImportValidator validator;
    private final SimCardRepository simCardRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final BatchRepository batchRepository;
    private final Clock clock;


    protected SimCardIdemiaEirImporter(final SimCardMapper simcardMapper,
                                       final SimCardImporterHelper helper,
                                       final SimCardImportValidator validator,
                                       final SimCardRepository simCardRepository,
                                       final LocalizedMessageBuilder localizedMessageBuilder,
                                       final BatchRepository batchRepository,
                                       final Clock clock) {
        super(Tag.SIMCARD);
        this.simcardMapper = simcardMapper;
        this.helper = helper;
        this.validator = validator;
        this.simCardRepository = simCardRepository;
        this.localizedMessageBuilder = localizedMessageBuilder;
        this.batchRepository = batchRepository;
        this.clock = clock;
    }

    @Override
    public void onParseLine(GenericEquipementCsvLines.SimCardIdemiaLine simCardIdemiaLine) {
        var simCardIdemiaImporterContext = new SimCardDefaultImporterContext(this.strategy.getImportParameters(), localizedMessageBuilder);

        // Validate Serial Number integrity (length and checkDigit)
        validator.validateIdemiaSerialNumber(simCardIdemiaLine.getSerialNumber());

        // Find the existing SIM Card that should be updated
        final var serialNumber = simCardIdemiaLine.getSerialNumber().substring(0, 18);
        var existingSimCardOpt = simCardRepository.findBySerialNumberAndCategory(serialNumber, EquipmentCategory.SIMCARD);

        existingSimCardOpt.ifPresentOrElse(existingSimCard -> this.updateExistingSimCard(simCardIdemiaImporterContext, simCardIdemiaLine, existingSimCard),
                () -> {
                    var newSimCard = simcardMapper.toSimCardIdemiaEir(simCardIdemiaLine);
                    helper.fillSimCard(simCardIdemiaImporterContext, newSimCard);
                    simCardIdemiaLine.setNodes(Collections.singletonList(newSimCard));
                });
    }

    private void updateExistingSimCard(SimCardDefaultImporterContext simCardIdemiaImporterContext,
                                       GenericEquipementCsvLines.SimCardIdemiaLine simCardIdemiaLine,
                                       SimCard existingSimCard) {
        simCardIdemiaImporterContext.getBatchNumber().ifPresent(batchNumber -> {
            final var batch = batchRepository.findById(Long.valueOf(batchNumber))
                    .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, BATCH_NOT_FOUND, batchNumber));

            if (!batchNumber.equals(existingSimCard.getBatchNumber())) {
                throw new EqmValidationException(localizedMessageBuilder, SIMCARD_SN_NOT_FOUND_WITH_BATCH_NUMBER, existingSimCard.getSerialNumber(), batchNumber);
            }

            batch.setProcessedDate(LocalDateTime.now(clock));
            batchRepository.save(batch);
        });

        // Update the SIM with the new values from the file
        simcardMapper.update(existingSimCard, simCardIdemiaLine);

        simCardIdemiaLine.setNodes(Collections.singletonList(existingSimCard));
    }

    @Override
    public Set<String> getNames() {
        return Set.of("IDEMIA_EIR", "IDEMIA_MET", "IDEMIA");
    }
}
