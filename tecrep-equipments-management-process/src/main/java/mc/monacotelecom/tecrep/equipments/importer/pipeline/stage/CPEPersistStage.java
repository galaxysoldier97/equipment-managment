package mc.monacotelecom.tecrep.equipments.importer.pipeline.stage;

import lombok.EqualsAndHashCode;
import mc.monacotelecom.inventory.common.importer.process.pipeline.PersistStage;
import mc.monacotelecom.tecrep.equipments.entity.CPE;
import mc.monacotelecom.tecrep.equipments.entity.Plmn;
import mc.monacotelecom.tecrep.equipments.repository.AncillaryRepository;
import mc.monacotelecom.tecrep.equipments.repository.CPERepository;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Component
public class CPEPersistStage extends PersistStage<CPE> {
    final CPERepository cpeRepository;
    final AncillaryRepository ancillaryRepository;

    public CPEPersistStage(CPERepository cpeRepository, AncillaryRepository ancillaryRepository) {
        super();
        addAction(PersistActionType.SAVE, new PersistStageSaveAction((t, p) -> saveOrUpdate(t)));
        addAction(PersistActionType.UPDATE, new PersistStageUpdateAction<Plmn>((t, p) -> saveOrUpdate(t)));
        addAction(PersistActionType.DELETE, new PersistStageDeleteAction<Plmn>((t, p) -> delete(t)));
        this.cpeRepository = cpeRepository;
        this.ancillaryRepository = ancillaryRepository;
    }

    private CPE delete(CPE t) {
        cpeRepository.delete(t);
        return t;
    }

    private CPE saveOrUpdate(CPE t) {
        return cpeRepository.save(t);
    }
}
