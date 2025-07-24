package mc.monacotelecom.tecrep.equipments.importer.implementations.ancillary;

import mc.monacotelecom.inventory.common.importer.process.NamedAbstractImporter;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentName;
import mc.monacotelecom.tecrep.equipments.importer.data.GenericEquipementCsvLines;
import mc.monacotelecom.tecrep.equipments.importer.implementations.context.EquipmentImporterContext;
import mc.monacotelecom.tecrep.equipments.importer.tags.Tag;
import mc.monacotelecom.tecrep.equipments.mapper.AncillaryMapper;
import mc.monacotelecom.tecrep.equipments.repository.AncillaryRepository;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentModelRepository;
import mc.monacotelecom.tecrep.equipments.repository.WarehouseRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

import static mc.monacotelecom.tecrep.equipments.mapper.CPEMapper.formatMacAddress;

@Service
public class CustomFormatAncillaryImporter
        extends NamedAbstractImporter<AncillaryEquipment, GenericEquipementCsvLines.CustomFormatAncillaryCsvLine>
        implements IAncillaryImporter {

    private static final Logger log = LoggerFactory.getLogger(CustomFormatAncillaryImporter.class);

    private final WarehouseRepository warehouseRepository;
    private final EquipmentModelRepository equipmentModelRepository;
    private final AncillaryRepository ancillaryRepository;
    private final AncillaryMapper ancillaryMapper;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    public CustomFormatAncillaryImporter(AncillaryRepository ancillaryRepository,
                                        EquipmentModelRepository equipmentModelRepository,
                                        WarehouseRepository warehouseRepository,
                                        AncillaryMapper ancillaryMapper,
                                        LocalizedMessageBuilder localizedMessageBuilder) {
        super(Tag.EQUIPMENTSADMIN);
        this.ancillaryRepository = ancillaryRepository;
        this.warehouseRepository = warehouseRepository;
        this.equipmentModelRepository = equipmentModelRepository;
        this.ancillaryMapper = ancillaryMapper;
        this.localizedMessageBuilder = localizedMessageBuilder;
        log.trace("CustomFormatAncillaryImporter initialized with repositories and mapper");
    }

    @Override
    public Set<String> getNames() {
        log.trace("getNames called -->" );
        return Set.of("CUSTOM_FORMAT", "CUSTOM_ANCILLARY", "FORMATO_PERSONALIZADO","SAP_ANCILLARY_TEMP");
    }

    @Override
    public EquipmentName getEquipmentName() {
             return EquipmentName.STB;
    }

    @Override
    public void onParseLine(GenericEquipementCsvLines.CustomFormatAncillaryCsvLine parsedLine) {
        log.trace("onParseLine started: serial={}, line={}", parsedLine.getSerialNumber(), parsedLine.getLineNumber());

        try {
            // 1. Obtener el modelo de equipo por ID
            var equipmentModelId = parsedLine.getEquipmentModelId();
            var equipmentModel = equipmentModelRepository.findById(Long.parseLong(equipmentModelId))
                    .orElseThrow(() -> new RuntimeException("Modelo de equipo no encontrado: " + equipmentModelId));
            log.trace("Equipment model found: {}", equipmentModel.getName());

            // 2. Obtener el almacén por ID
            var warehouseId = parsedLine.getWarehouseId();
            var warehouse = warehouseRepository.findById(Long.parseLong(warehouseId))
                    .orElseThrow(() -> new RuntimeException("Almacén no encontrado: " + warehouseId));
            log.trace("Warehouse found: {}", warehouse.getName());

            // 3. Determinar el tipo de equipo basado en el modelo
            // Get default equipment name from interface
            var equipmentName = getEquipmentName();
            log.trace("Equipment name from interface: {}", equipmentName);

            var context = new EquipmentImporterContext(
                    warehouseRepository,
                    equipmentModelRepository,
                    strategy.getImportParameters(),
                    localizedMessageBuilder,
                    EquipmentModelCategory.ANCILLARY
            );
            log.trace("Context created: {}", context);

            var ancillary = ancillaryMapper.toNode(context, equipmentName);
            log.trace("Ancillary node created: {}", ancillary);

            // Asignar número de serie
            String cleanedSerial = cleanupDecimals(parsedLine.getSerialNumber());
            ancillary.setSerialNumber(cleanedSerial);
            log.trace("Serial number cleaned and set: {}", cleanedSerial);

            // Formatear y asignar MAC address
            ancillary.setMacAddress(formatMacAddress(parsedLine.getMacAddress()));
            log.trace("MAC address set: {}", ancillary.getMacAddress());

            // Asignar modelo y almacén
            ancillary.setModel(equipmentModel);
            ancillary.setWarehouse(warehouse);
            log.trace("Model and warehouse assigned");

            // Asignar valores específicos basados en el modelo
            ancillary.setAccessType(equipmentModel.getAccessType());
            
            // Set the equipment name based on the model
            ancillary.setEquipmentName(equipmentName);
            log.trace("Equipment name set: {}", equipmentName);
            
            // Set recyclable to true by default (required field that can't be null)
            ancillary.setRecyclable(Boolean.TRUE);
            log.trace("Recyclable set to TRUE");
            
            // Verificar si el equipo ancillary ya existe
            checkAncillaryExist(ancillaryRepository, localizedMessageBuilder, ancillary.getSerialNumber());
            log.trace("Ancillary existence check passed");

            parsedLine.setNodes(Collections.singletonList(ancillary));
            log.trace("Ancillary set in parsed line: {}", ancillary.getSerialNumber());

        } catch (Exception e) {
            log.error("-> Error processing line {}: {}", parsedLine.getLineNumber(), e.getMessage(), e);
            // El framework maneja automáticamente los errores por línea
            throw e;
        }
    }
}

