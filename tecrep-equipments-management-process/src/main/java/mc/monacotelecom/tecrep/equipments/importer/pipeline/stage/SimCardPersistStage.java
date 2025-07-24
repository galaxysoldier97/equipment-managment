package mc.monacotelecom.tecrep.equipments.importer.pipeline.stage;

import lombok.EqualsAndHashCode;
import mc.monacotelecom.inventory.common.importer.process.pipeline.PersistStage;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.repository.SimCardRepository;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Component
public class SimCardPersistStage extends PersistStage<SimCard> {
    final SimCardRepository repository;

    public SimCardPersistStage(SimCardRepository repository) {
        super();
        addAction(PersistActionType.SAVE, new PersistStageSaveAction((t, p) -> saveOrUpdate(t)));
        addAction(PersistActionType.UPDATE, new PersistStageUpdateAction<SimCard>((t, p) -> saveOrUpdate(t)));
        addAction(PersistActionType.DELETE, new PersistStageDeleteAction<SimCard>((t, p) -> delete(t)));
        this.repository = repository;
    }

    private SimCard delete(SimCard t) {
        repository.delete(t);
        return t;
    }

    private SimCard saveOrUpdate(SimCard t) {
        return repository.save(t);
    }
}
