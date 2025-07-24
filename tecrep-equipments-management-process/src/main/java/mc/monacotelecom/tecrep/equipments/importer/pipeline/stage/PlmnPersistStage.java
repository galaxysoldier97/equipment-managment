package mc.monacotelecom.tecrep.equipments.importer.pipeline.stage;

import lombok.EqualsAndHashCode;
import mc.monacotelecom.inventory.common.importer.process.pipeline.PersistStage;
import mc.monacotelecom.tecrep.equipments.entity.Plmn;
import mc.monacotelecom.tecrep.equipments.repository.PlmnRepository;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Component
public class PlmnPersistStage extends PersistStage<Plmn> {

    final PlmnRepository plmnRepository;

    public PlmnPersistStage(PlmnRepository plmnRepository) {
        super();
        addAction(PersistActionType.SAVE, new PersistStageSaveAction((t, p) -> saveOrUpdate(t)));
        addAction(PersistActionType.UPDATE, new PersistStageUpdateAction<Plmn>((t, p) -> saveOrUpdate(t)));
        addAction(PersistActionType.DELETE, new PersistStageDeleteAction<Plmn>((t, p) -> delete(t)));
        this.plmnRepository = plmnRepository;
    }

    private Plmn delete(Plmn t) {
        plmnRepository.delete(t);
        return t;
    }

    private Plmn saveOrUpdate(Plmn t) {
        return plmnRepository.save(t);
    }
}
