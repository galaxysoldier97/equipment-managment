package mc.monacotelecom.tecrep.equipments.importer.implementations.context;

import lombok.Getter;
import mc.monacotelecom.importer.ImportParameters;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentModelRepository;
import mc.monacotelecom.tecrep.equipments.repository.WarehouseRepository;

@Getter
public class CPEImporterContext extends EquipmentImporterContext {

    private static final String WITH_ANCILLARY_EQUIPMENT = "withAncillary";
    private boolean shouldCreateAncillaryOnTheFly;

    public CPEImporterContext(final WarehouseRepository warehouseRepository,
                              final EquipmentModelRepository equipmentModelRepository,
                              final ImportParameters importParameters,
                              final LocalizedMessageBuilder localizedMessageBuilder) {
        super(warehouseRepository, equipmentModelRepository, importParameters, localizedMessageBuilder, EquipmentModelCategory.CPE);
        this.shouldCreateAncillaryOnTheFly();
    }

    public void shouldCreateAncillaryOnTheFly() {
        this.shouldCreateAncillaryOnTheFly = this.importParameters.getParameter(WITH_ANCILLARY_EQUIPMENT)
                .map(Boolean::parseBoolean)
                .orElse(true);
    }
}
