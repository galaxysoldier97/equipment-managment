package mc.monacotelecom.tecrep.equipments.importer.pipeline.stage;

import lombok.EqualsAndHashCode;
import mc.monacotelecom.inventory.common.importer.process.pipeline.PersistStage;
import mc.monacotelecom.tecrep.equipments.entity.FileConfiguration;
import mc.monacotelecom.tecrep.equipments.repository.FileConfigurationRepository;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Component
public class FileConfigurationPersistStage extends PersistStage<FileConfiguration> {

    final FileConfigurationRepository fileConfigurationRepository;

    public FileConfigurationPersistStage(FileConfigurationRepository fileConfigurationRepository) {
        super();
        this.fileConfigurationRepository = fileConfigurationRepository;
        addAction(PersistActionType.SAVE, new PersistStageSaveAction((t, p) -> saveOrUpdate(t)));
        addAction(PersistActionType.UPDATE, new PersistStageUpdateAction<FileConfiguration>((t, p) -> saveOrUpdate(t)));
        addAction(PersistActionType.DELETE, new PersistStageDeleteAction<FileConfiguration>((t, p) -> delete(t)));
    }

    private FileConfiguration delete(FileConfiguration t) {
        fileConfigurationRepository.delete(t);
        return t;
    }

    private FileConfiguration saveOrUpdate(FileConfiguration t) {
        return fileConfigurationRepository.save(t);
    }
}
