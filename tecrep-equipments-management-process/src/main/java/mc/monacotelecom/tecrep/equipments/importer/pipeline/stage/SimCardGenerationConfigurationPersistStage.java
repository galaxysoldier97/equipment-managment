package mc.monacotelecom.tecrep.equipments.importer.pipeline.stage;

import lombok.EqualsAndHashCode;
import mc.monacotelecom.inventory.common.importer.process.pipeline.PersistStage;
import mc.monacotelecom.tecrep.equipments.entity.Plmn;
import mc.monacotelecom.tecrep.equipments.entity.SimCardGenerationConfiguration;
import mc.monacotelecom.tecrep.equipments.repository.SimCardGenerationConfigurationRepository;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Component
public class SimCardGenerationConfigurationPersistStage extends PersistStage<SimCardGenerationConfiguration> {

    final SimCardGenerationConfigurationRepository simCardGenerationConfigurationRepository;

    public SimCardGenerationConfigurationPersistStage(SimCardGenerationConfigurationRepository simCardGenerationConfigurationRepository) {
        super();
        addAction(PersistActionType.SAVE, new PersistStageSaveAction((t, p) -> saveOrUpdate(t)));
        addAction(PersistActionType.UPDATE, new PersistStageUpdateAction<Plmn>((t, p) -> saveOrUpdate(t)));
        addAction(PersistActionType.DELETE, new PersistStageDeleteAction<Plmn>((t, p) -> delete(t)));
        this.simCardGenerationConfigurationRepository = simCardGenerationConfigurationRepository;
    }

    private SimCardGenerationConfiguration delete(SimCardGenerationConfiguration t) {
        simCardGenerationConfigurationRepository.delete(t);
        return t;
    }

    private SimCardGenerationConfiguration saveOrUpdate(SimCardGenerationConfiguration t) {
        return simCardGenerationConfigurationRepository.save(t);
    }
}
