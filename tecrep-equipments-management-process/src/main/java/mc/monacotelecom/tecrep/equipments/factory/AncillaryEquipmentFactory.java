package mc.monacotelecom.tecrep.equipments.factory;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.importer.ImportParameters;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentModel;
import mc.monacotelecom.tecrep.equipments.entity.Warehouse;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory;
import mc.monacotelecom.tecrep.equipments.importer.implementations.context.EquipmentImporterContext;
import mc.monacotelecom.tecrep.equipments.mapper.AncillaryMapper;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentModelRepository;
import mc.monacotelecom.tecrep.equipments.repository.WarehouseRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AncillaryEquipmentFactory {
    private final AncillaryMapper ancillaryMapper;
    private final WarehouseRepository warehouseRepository;
    private final EquipmentModelRepository equipmentModelRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    
    public AncillaryEquipment createFromCsvRecord(
        EquipmentModel model,
        Warehouse warehouse,
        String serial,
        String mac
    ) {
        // Create import parameters for the context
        ImportParameters importParameters = new ImportParameters();
        importParameters.addParameter(EquipmentImporterContext.MODELNAME_KEY, new String[]{model.getName()});
        importParameters.addParameter(EquipmentImporterContext.WAREHOUSE_ID_KEY, new String[]{warehouse.getWarehouseId().toString()});
        
        // Use mapper with standardized context
        EquipmentImporterContext ctx = new EquipmentImporterContext(
            warehouseRepository,
            equipmentModelRepository,
            importParameters,
            localizedMessageBuilder,
            EquipmentModelCategory.ANCILLARY
        );
        
        AncillaryEquipment equipment = ancillaryMapper.toNode(ctx, model.getEquipmentName());
        equipment.setSerialNumber(serial);
        equipment.setMacAddress(mac);
        return equipment;
    }
}

