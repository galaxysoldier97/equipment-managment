package mc.monacotelecom.tecrep.equipments.importer.pipeline.stage;

import lombok.EqualsAndHashCode;
import mc.monacotelecom.inventory.common.importer.process.pipeline.PersistStage;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.entity.FileConfiguration;
import mc.monacotelecom.tecrep.equipments.repository.AncillaryRepository;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Component
public class AncillaryEquipmentPersistStage extends PersistStage<AncillaryEquipment> {

    final AncillaryRepository ancillaryRepository;

    public AncillaryEquipmentPersistStage(AncillaryRepository ancillaryRepository) {
        super();
        this.ancillaryRepository = ancillaryRepository;
        addAction(PersistActionType.SAVE, new PersistStageSaveAction((t, p) -> saveOrUpdate(t)));
        addAction(PersistActionType.UPDATE, new PersistStageUpdateAction<FileConfiguration>((t, p) -> saveOrUpdate(t)));
        addAction(PersistActionType.DELETE, new PersistStageDeleteAction<FileConfiguration>((t, p) -> delete(t)));
    }

    private AncillaryEquipment delete(AncillaryEquipment t) {
        ancillaryRepository.delete(t);
        return t;
    }

    private AncillaryEquipment saveOrUpdate(AncillaryEquipment t) {
        return ancillaryRepository.save(t);
    }
}
