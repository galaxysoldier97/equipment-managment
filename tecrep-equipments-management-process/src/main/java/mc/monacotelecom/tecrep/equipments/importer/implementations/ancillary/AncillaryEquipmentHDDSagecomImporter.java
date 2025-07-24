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
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class AncillaryEquipmentHDDSagecomImporter extends NamedAbstractImporter<AncillaryEquipment, GenericEquipementCsvLines.AncillaryEquipmentHDDSagemcomsvLine>
        implements IAncillaryImporter {

    private final WarehouseRepository warehouseRepository;
    private final EquipmentModelRepository equipmentModelRepository;
    private final AncillaryRepository ancillaryRepository;
    private final AncillaryMapper ancillaryMapper;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    public AncillaryEquipmentHDDSagecomImporter(final AncillaryRepository ancillaryRepository,
                                                final EquipmentModelRepository equipmentModelRepository,
                                                final WarehouseRepository warehouseRepository,
                                                final AncillaryMapper ancillaryMapper,
                                                final LocalizedMessageBuilder localizedMessageBuilder) {
        super(Tag.EQUIPMENTSADMIN);
        this.ancillaryRepository = ancillaryRepository;
        this.equipmentModelRepository = equipmentModelRepository;
        this.warehouseRepository = warehouseRepository;
        this.ancillaryMapper = ancillaryMapper;
        this.localizedMessageBuilder = localizedMessageBuilder;
    }

    @Override
    public Set<String> getNames() {
        return Set.of(EquipmentName.HDD.name(), "HDD_Sagemcom");
    }

    @Override
    public EquipmentName getEquipmentName() {
        return EquipmentName.HDD;
    }

    @Override
    public void onParseLine(GenericEquipementCsvLines.AncillaryEquipmentHDDSagemcomsvLine parsedLine) {

        final var context = new EquipmentImporterContext(warehouseRepository, equipmentModelRepository, strategy.getImportParameters(), localizedMessageBuilder, EquipmentModelCategory.ANCILLARY);
        var ancillary = ancillaryMapper.toNode(context, this.getEquipmentName());
        ancillary.setBatchNumber(parsedLine.getPalletNumber());
        ancillary.setSerialNumber(cleanupDecimals(parsedLine.getSn()));
        ancillary.setExternalNumber(parsedLine.getSn2());

        this.checkAncillaryExist(ancillaryRepository, localizedMessageBuilder, ancillary.getSerialNumber());

        parsedLine.setNodes(Collections.singletonList(ancillary));
    }
}
