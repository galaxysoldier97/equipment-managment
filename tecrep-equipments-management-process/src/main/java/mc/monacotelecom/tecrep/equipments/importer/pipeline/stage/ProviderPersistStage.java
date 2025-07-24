package mc.monacotelecom.tecrep.equipments.importer.pipeline.stage;

import lombok.EqualsAndHashCode;
import mc.monacotelecom.inventory.common.importer.process.pipeline.PersistStage;
import mc.monacotelecom.tecrep.equipments.entity.Provider;
import mc.monacotelecom.tecrep.equipments.repository.ProviderRepository;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Component
public class ProviderPersistStage extends PersistStage<Provider> {
    private final ProviderRepository repository;

    public ProviderPersistStage(ProviderRepository repository) {
        super();
        addAction(PersistStage.PersistActionType.SAVE, new PersistStageSaveAction((t, p) -> saveOrUpdate(t)));
        addAction(PersistActionType.UPDATE, new PersistStageUpdateAction<Provider>((t, p) -> saveOrUpdate(t)));
        addAction(PersistActionType.DELETE, new PersistStageDeleteAction<Provider>((t, p) -> delete(t)));
        this.repository = repository;
    }

    Provider saveOrUpdate(Provider s) {
        return repository.save(s);
    }

    Provider delete(Provider s) {
        repository.delete(s);
        return s;
    }
}
