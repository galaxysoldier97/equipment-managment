package mc.monacotelecom.tecrep.equipments.importer.implementations;

import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.importer.process.NamedAbstractImporter;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.entity.Equipment;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.importer.entities.ChangeWarehouseOrStatus;
import mc.monacotelecom.tecrep.equipments.importer.strategy.ChangeWarehouseOrStatusLine;
import mc.monacotelecom.tecrep.equipments.importer.tags.Tag;
import mc.monacotelecom.tecrep.equipments.process.EquipmentStateMachineWithoutRecyclingService;
import mc.monacotelecom.tecrep.equipments.repository.AncillaryRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.ANCILLARY_LIFECYCLE_ERROR;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.IMPORT_INDEPENDENT_MODE;

@Slf4j
@Service
public class ChangeWarehouseOrStatusImporter extends NamedAbstractImporter<ChangeWarehouseOrStatus, ChangeWarehouseOrStatusLine> {

    private final EquipmentStateMachineWithoutRecyclingService equipmentStateMachineService;
    private final AncillaryRepository ancillaryRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    protected ChangeWarehouseOrStatusImporter(final EquipmentStateMachineWithoutRecyclingService equipmentStateMachineService,
                                              final AncillaryRepository ancillaryRepository,
                                              final LocalizedMessageBuilder localizedMessageBuilder) {
        super(Tag.CHANGEWAREHOUSEORSTATUS);
        this.equipmentStateMachineService = equipmentStateMachineService;
        this.ancillaryRepository = ancillaryRepository;
        this.localizedMessageBuilder = localizedMessageBuilder;
    }

    @Override
    public Set<String> getNames() {
        return Set.of("ChangeWarehouseOrStatus");
    }

    @Override
    public void onParseLine(ChangeWarehouseOrStatusLine parsedLine) {
        parsedLine.getNodes().forEach(changeWarehouseOrStatus -> {
            Equipment equipment = changeWarehouseOrStatus.getEquipment();
            if (EquipmentCategory.ANCILLARY.equals(equipment.getCategory())) {
                Optional<AncillaryEquipment> optionalAncillaryEquipment = ancillaryRepository.findByCategoryAndSerialNumber(changeWarehouseOrStatus.getEquipment().getCategory(), changeWarehouseOrStatus.getEquipment().getSerialNumber());
                optionalAncillaryEquipment.ifPresentOrElse(
                        value -> {
                            if (value.getIndependent() != null && !value.getIndependent()) {
                                throw new EqmValidationException(localizedMessageBuilder, IMPORT_INDEPENDENT_MODE, equipment.getSerialNumber());
                            }
                        },
                        () -> log.warn(String.format("Could not find ancillary equipment with serial number %s", changeWarehouseOrStatus.getEquipment().getSerialNumber()))
                );
            }

            if (Objects.nonNull(changeWarehouseOrStatus.getEvent())) {
                try {
                    equipment.setStatus(equipmentStateMachineService.updateStatus(String.valueOf(equipment.getEquipmentId()), equipment.getStatus(), changeWarehouseOrStatus.getEvent()));
                } catch (EqmValidationException e) {
                    throw new EqmValidationException(localizedMessageBuilder, ANCILLARY_LIFECYCLE_ERROR, changeWarehouseOrStatus.getEvent(), equipment.getStatus(), equipment.getSerialNumber());
                }
            }

            if (Objects.nonNull(changeWarehouseOrStatus.getWarehouse())) {
                equipment.setWarehouse(changeWarehouseOrStatus.getWarehouse());
            }
        });
    }
}