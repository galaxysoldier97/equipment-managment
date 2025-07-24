package mc.monacotelecom.tecrep.equipments.importer.implementations;

import mc.monacotelecom.importer.ImportMapper;
import mc.monacotelecom.inventory.common.importer.process.NamedAbstractImporter;
import mc.monacotelecom.tecrep.equipments.entity.FileConfiguration;
import mc.monacotelecom.tecrep.equipments.importer.tags.Tag;
import mc.monacotelecom.tecrep.equipments.repository.FileConfigurationRepository;
import org.springframework.stereotype.Service;

@Service
public class FileConfigurationImporter extends NamedAbstractImporter<FileConfiguration, ImportMapper.MappedLine<FileConfiguration>> {
    private final FileConfigurationRepository fileConfigurationRepository;

    protected FileConfigurationImporter(FileConfigurationRepository fileConfigurationRepository) {
        super(Tag.EQUIPMENTSADMIN);
        this.fileConfigurationRepository = fileConfigurationRepository;
    }

    @Override
    public void onParseLine(ImportMapper.MappedLine<FileConfiguration> parsedLine) {
        parsedLine.getNodes().forEach(fileConfiguration -> {
            fileConfigurationRepository.findByName(fileConfiguration.getName()).ifPresent(
                    dbFileConfiguration -> fileConfiguration.setId(dbFileConfiguration.getId()));
            validate(fileConfiguration, parsedLine.getSaveDepth().get());
        });
    }
}
