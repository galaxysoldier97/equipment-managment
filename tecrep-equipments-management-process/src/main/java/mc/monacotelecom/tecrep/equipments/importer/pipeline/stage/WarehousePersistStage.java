package mc.monacotelecom.tecrep.equipments.importer.pipeline.stage;

import lombok.EqualsAndHashCode;
import mc.monacotelecom.inventory.common.importer.process.pipeline.PersistStage;
import mc.monacotelecom.tecrep.equipments.entity.Warehouse;
import mc.monacotelecom.tecrep.equipments.repository.WarehouseRepository;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Component
public class WarehousePersistStage extends PersistStage<Warehouse> {
    private final WarehouseRepository repository;

    public WarehousePersistStage(WarehouseRepository repository) {
        super();
        addAction(PersistActionType.SAVE, new PersistStageSaveAction((t, p) -> saveOrUpdate(t)));
        addAction(PersistActionType.UPDATE, new PersistStageUpdateAction<Warehouse>((t, p) -> saveOrUpdate(t)));
        addAction(PersistActionType.DELETE, new PersistStageDeleteAction<Warehouse>((t, p) -> delete(t)));

        this.repository = repository;
    }

    Warehouse saveOrUpdate(Warehouse s) {
        return repository.save(s);
    }

    Warehouse delete(Warehouse s) {
        repository.delete(s);
        return s;
    }
}
