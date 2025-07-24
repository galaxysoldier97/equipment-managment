package mc.monacotelecom.tecrep.equipments.importer.implementations;

import mc.monacotelecom.importer.ImportMapper;
import mc.monacotelecom.inventory.common.importer.process.NamedAbstractImporter;
import mc.monacotelecom.tecrep.equipments.entity.InventoryPool;
import mc.monacotelecom.tecrep.equipments.importer.tags.Tag;
import mc.monacotelecom.tecrep.equipments.repository.InventoryPoolRepository;
import org.springframework.stereotype.Service;

@Service
public class InventoryPoolImporter extends NamedAbstractImporter<InventoryPool, ImportMapper.MappedLine<InventoryPool>> {
    private final InventoryPoolRepository inventoryPoolRepository;

    protected InventoryPoolImporter(InventoryPoolRepository inventoryPoolRepository) {
        super(Tag.EQUIPMENTSADMIN);
        this.inventoryPoolRepository = inventoryPoolRepository;
    }

    @Override
    public void onParseLine(ImportMapper.MappedLine<InventoryPool> parsedLine) {
        parsedLine.getNodes().forEach(inventoryPool -> {
            inventoryPoolRepository.findByCode(inventoryPool.getCode()).ifPresent(
                    dbInventoryPool -> inventoryPool.setInventoryPoolId(dbInventoryPool.getInventoryPoolId()));
            validate(inventoryPool, parsedLine.getSaveDepth().get());
        });
    }
}
