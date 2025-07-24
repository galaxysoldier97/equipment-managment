package mc.monacotelecom.tecrep.equipments.importer.pipeline.stage;

import lombok.EqualsAndHashCode;
import mc.monacotelecom.inventory.common.importer.process.pipeline.PersistStage;
import mc.monacotelecom.tecrep.equipments.entity.InventoryPool;
import mc.monacotelecom.tecrep.equipments.entity.Plmn;
import mc.monacotelecom.tecrep.equipments.repository.InventoryPoolRepository;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Component
public class InventoryPoolPersistStage extends PersistStage<InventoryPool> {

    final InventoryPoolRepository inventoryPoolRepository;

    public InventoryPoolPersistStage(InventoryPoolRepository inventoryPoolRepository) {
        super();
        addAction(PersistActionType.SAVE, new PersistStageSaveAction((t, p) -> saveOrUpdate(t)));
        addAction(PersistActionType.UPDATE, new PersistStageUpdateAction<Plmn>((t, p) -> saveOrUpdate(t)));
        addAction(PersistActionType.DELETE, new PersistStageDeleteAction<Plmn>((t, p) -> delete(t)));
        this.inventoryPoolRepository = inventoryPoolRepository;
    }

    private InventoryPool delete(InventoryPool t) {
        inventoryPoolRepository.delete(t);
        return t;
    }

    private InventoryPool saveOrUpdate(InventoryPool t) {
        return inventoryPoolRepository.save(t);
    }
}
