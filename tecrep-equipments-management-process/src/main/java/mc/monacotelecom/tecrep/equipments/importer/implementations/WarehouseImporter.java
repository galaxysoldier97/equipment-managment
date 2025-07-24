package mc.monacotelecom.tecrep.equipments.importer.implementations;

import mc.monacotelecom.importer.ImportMapper;
import mc.monacotelecom.inventory.common.importer.process.NamedAbstractImporter;
import mc.monacotelecom.tecrep.equipments.entity.Warehouse;
import mc.monacotelecom.tecrep.equipments.importer.tags.Tag;
import mc.monacotelecom.tecrep.equipments.repository.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WarehouseImporter extends NamedAbstractImporter<Warehouse, ImportMapper.MappedLine<Warehouse>> {
    private final WarehouseRepository warehouseRepository;

    protected WarehouseImporter(WarehouseRepository warehouseRepository) {
        super(Tag.EQUIPMENTSADMIN);
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public void onParseLine(ImportMapper.MappedLine<Warehouse> warehouseMappedLine) {
        warehouseMappedLine.getNodes().forEach(warehouse -> {
            Optional<Warehouse> optionalWarehouse = warehouseRepository.findByName(warehouse.getName());
            optionalWarehouse.ifPresent(existing -> warehouse.setWarehouseId(existing.getWarehouseId()));
            validate(warehouse, warehouseMappedLine.getSaveDepth().get());
        });
    }
}
