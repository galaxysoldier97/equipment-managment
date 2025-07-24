package mc.monacotelecom.tecrep.equipments.importer.implementations;

import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.importer.domain.entity.IEntity;
import mc.monacotelecom.inventory.common.importer.process.NamedAbstractImporter;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;
import mc.monacotelecom.tecrep.equipments.importer.data.GenericEquipementCsvLines;
import mc.monacotelecom.tecrep.equipments.importer.data.GenericEquipmentImportMappers;
import mc.monacotelecom.tecrep.equipments.importer.tags.Tag;
import mc.monacotelecom.tecrep.equipments.repository.SimCardRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.SIMCARD_NOT_FOUND_ICCID;

@Slf4j
@Service
public class SimCardDeliveryFileImporter extends NamedAbstractImporter<IEntity, GenericEquipementCsvLines.SimCardDeliveryFileLine> {

    private final SimCardRepository simCardRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final GenericEquipmentImportMappers.SimCardDeliveryFileMapper simCardDeliveryFileMapper;

    protected SimCardDeliveryFileImporter(final SimCardRepository simCardRepository,
                                          @Qualifier("simCardDeliveryFileMapper") final GenericEquipmentImportMappers.SimCardDeliveryFileMapper simCardDeliveryFileMapper,
                                          final LocalizedMessageBuilder localizedMessageBuilder) {
        super(Tag.SIMCARD);
        this.simCardRepository = simCardRepository;
        this.simCardDeliveryFileMapper = simCardDeliveryFileMapper;
        this.localizedMessageBuilder = localizedMessageBuilder;
    }

    @Override
    public void onParseLine(final GenericEquipementCsvLines.SimCardDeliveryFileLine simCardMappedLine) {
        final SimCard simCard = simCardDeliveryFileMapper.toNode(simCardMappedLine);
        final String serialNumber = simCard.getSerialNumber();

        if (!StringUtils.containsOnly(serialNumber, "0")) {

            SimCard simCardToUpdate = simCardRepository.findBySerialNumberAndCategory(serialNumber, EquipmentCategory.SIMCARD)
                    .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, SIMCARD_NOT_FOUND_ICCID, serialNumber));

            simCardToUpdate.setOrderId(simCard.getOrderId());
            simCardToUpdate.setPackId(simCard.getPackId());

            simCardMappedLine.setNodes(Collections.singletonList(simCardToUpdate));
        }
    }

    @Override
    public Set<String> getNames() {
        return Set.of("DeliveryFile");
    }
}
