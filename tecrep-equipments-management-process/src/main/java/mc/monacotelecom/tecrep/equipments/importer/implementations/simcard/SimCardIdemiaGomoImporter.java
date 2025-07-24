package mc.monacotelecom.tecrep.equipments.importer.implementations.simcard;

import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.importer.process.NamedAbstractImporter;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.importer.data.GenericEquipementCsvLines;
import mc.monacotelecom.tecrep.equipments.importer.implementations.context.SimCardDefaultImporterContext;
import mc.monacotelecom.tecrep.equipments.importer.tags.Tag;
import mc.monacotelecom.tecrep.equipments.mapper.SimCardMapper;
import mc.monacotelecom.tecrep.equipments.repository.SimCardRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Slf4j
@Service
public class SimCardIdemiaGomoImporter extends NamedAbstractImporter<SimCard, GenericEquipementCsvLines.SimCardGomoLine> {

    private final SimCardImporterHelper helper;
    private final SimCardImportValidator validator;
    private final SimCardMapper simCardMapper;
    private final SimCardRepository simCardRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    protected SimCardIdemiaGomoImporter(final SimCardImporterHelper helper,
                                        final SimCardMapper simCardMapper,
                                        final SimCardImportValidator validator,
                                        final SimCardRepository simCardRepository,
                                        final LocalizedMessageBuilder localizedMessageBuilder) {
        super(Tag.SIMCARD);
        this.helper = helper;
        this.simCardMapper = simCardMapper;
        this.validator = validator;
        this.simCardRepository = simCardRepository;
        this.localizedMessageBuilder = localizedMessageBuilder;
    }


    @Override
    public void onParseLine(GenericEquipementCsvLines.SimCardGomoLine simCardMappedLine) {
        var simCardDefaultImporterContext = new SimCardDefaultImporterContext(this.strategy.getImportParameters(), localizedMessageBuilder);
        var headers = this.strategy.getHeaders();

        validator.validateSerialNumber(simCardMappedLine.getSerialNumber());
        simCardRepository.findBySerialNumber(simCardMappedLine.getSerialNumber().substring(0, 18)).ifPresentOrElse(existingSimCard -> {
                    simCardMapper.updateGomo(existingSimCard, simCardMappedLine);
                    helper.setFromHeader(headers, existingSimCard);
                    simCardMappedLine.setNodes(Collections.singletonList(existingSimCard));
                },
                () -> {
                    var simCard = simCardMapper.toSimCardGomo(simCardMappedLine);
                    helper.fillSimCard(simCardDefaultImporterContext, simCard);
                    helper.setFromHeader(headers, simCard);
                    simCardMappedLine.setNodes(Collections.singletonList(simCard));
                });
    }

    @Override
    public Set<String> getNames() {
        return Set.of("IDEMIA_GOMO", "IDEMIA_EPIC");
    }
}
