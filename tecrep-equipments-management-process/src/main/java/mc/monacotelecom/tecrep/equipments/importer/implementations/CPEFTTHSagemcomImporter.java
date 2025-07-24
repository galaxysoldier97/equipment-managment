package mc.monacotelecom.tecrep.equipments.importer.implementations;

import mc.monacotelecom.inventory.common.importer.process.NamedAbstractImporter;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.entity.CPE;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentName;
import mc.monacotelecom.tecrep.equipments.importer.data.GenericEquipementCsvLines;
import mc.monacotelecom.tecrep.equipments.importer.helper.CPEImporterHelper;
import mc.monacotelecom.tecrep.equipments.importer.implementations.context.CPEImporterContext;
import mc.monacotelecom.tecrep.equipments.importer.tags.Tag;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentModelRepository;
import mc.monacotelecom.tecrep.equipments.repository.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class CPEFTTHSagemcomImporter extends NamedAbstractImporter<CPE, GenericEquipementCsvLines.CPEFTTHCsvLine> {


    private final CPEImporterHelper cpeImporterHelper;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final WarehouseRepository warehouseRepository;
    private final EquipmentModelRepository equipmentModelRepository;


    public CPEFTTHSagemcomImporter(final CPEImporterHelper cpeImporterHelper,
                                   final LocalizedMessageBuilder localizedMessageBuilder,
                                   final WarehouseRepository warehouseRepository,
                                   final EquipmentModelRepository equipmentModelRepository) {
        super(Tag.CPE);
        this.cpeImporterHelper = cpeImporterHelper;
        this.localizedMessageBuilder = localizedMessageBuilder;
        this.warehouseRepository = warehouseRepository;
        this.equipmentModelRepository = equipmentModelRepository;
    }

    @Override
    public Set<String> getNames() {
        return Set.of("DEFAULT", "FTTH_Sagemcom");
    }

    @Override
    public void onParseLine(final GenericEquipementCsvLines.CPEFTTHCsvLine parsedLine) {
        final var cpeImporterContext = new CPEImporterContext(warehouseRepository, equipmentModelRepository, this.strategy.getImportParameters(), localizedMessageBuilder);
        parsedLine.setNodes(Collections.singletonList(cpeImporterHelper.parseLine(parsedLine, cpeImporterContext, EquipmentName.ONT)));
    }
}