package mc.monacotelecom.tecrep.equipments.importer.implementations.simcard;

import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.importer.ImportMapper;
import mc.monacotelecom.inventory.common.importer.process.NamedAbstractImporter;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import mc.monacotelecom.tecrep.equipments.importer.implementations.context.SimCardDefaultImporterContext;
import mc.monacotelecom.tecrep.equipments.importer.tags.Tag;
import mc.monacotelecom.tecrep.equipments.repository.SimCardRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class SimCardDefaultImporter extends NamedAbstractImporter<SimCard, ImportMapper.MappedLine<SimCard>> {


    private final SimCardRepository simCardRepository;
    private final SimCardImporterHelper helper;
    private final SimCardImportValidator validator;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    protected SimCardDefaultImporter(final SimCardRepository simCardRepository,
                                     final SimCardImporterHelper helper,
                                     final SimCardImportValidator validator,
                                     final LocalizedMessageBuilder localizedMessageBuilder) {
        super(Tag.SIMCARD);
        this.simCardRepository = simCardRepository;
        this.helper = helper;
        this.validator = validator;
        this.localizedMessageBuilder = localizedMessageBuilder;
    }
    @Override
    public void onParseLine(ImportMapper.MappedLine<SimCard> simCardMappedLine) {
        var simCardDefaultImporterContext = new SimCardDefaultImporterContext(this.strategy.getImportParameters(), localizedMessageBuilder);
        var headers = this.strategy.getHeaders();

        simCardMappedLine.getNodes().forEach(simCard -> {
            // If SIM Card with same Serial Number already exists, we're in an update case
            simCardRepository.findBySerialNumberAndCategory(simCard.getSerialNumber(), EquipmentCategory.SIMCARD)
                    .ifPresent(card -> simCard.setEquipmentId(card.getEquipmentId()));
            validator.prevalidateGemalto(simCard);

            helper.fillSimCard(simCardDefaultImporterContext, simCard);
            helper.setFromHeader(headers, simCard);
        });
    }

    @Override
    public Set<String> getNames() {
        return Set.of("DEFAULT", "GEMALTO", "GEMALTO_MT", "GEMALTO_EPIC");
    }
}
