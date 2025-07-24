package mc.monacotelecom.tecrep.equipments.importer.pipeline.stage;

import lombok.EqualsAndHashCode;
import mc.monacotelecom.inventory.common.importer.process.pipeline.PersistStage;
import mc.monacotelecom.tecrep.equipments.entity.Equipment;
import mc.monacotelecom.tecrep.equipments.entity.Provider;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentRepository;
import org.springframework.stereotype.Component;

@Component
@EqualsAndHashCode(callSuper = true)
public class ChangeWarehouseOrStatusPersistStage<T extends Equipment> extends PersistStage<T> {

    private final EquipmentRepository<T> repository;

    public ChangeWarehouseOrStatusPersistStage(EquipmentRepository<T> repository) {
        super();
        addAction(PersistActionType.SAVE, new PersistStageSaveAction((t, p) -> saveOrUpdate(t)));
        addAction(PersistActionType.UPDATE, new PersistStageUpdateAction<Provider>((t, p) -> saveOrUpdate(t)));
        addAction(PersistActionType.DELETE, new PersistStageDeleteAction<Provider>((t, p) -> delete(t)));

        this.repository = repository;
    }

    T saveOrUpdate(T s) {
        return repository.save(s);
    }

    T delete(T e) {
        repository.deleteById(e.getEquipmentId());
        return e;
    }

}
