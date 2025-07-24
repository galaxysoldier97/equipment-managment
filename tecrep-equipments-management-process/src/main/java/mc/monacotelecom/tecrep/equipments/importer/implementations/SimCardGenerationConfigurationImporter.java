package mc.monacotelecom.tecrep.equipments.importer.implementations;

import mc.monacotelecom.importer.ImportMapper;
import mc.monacotelecom.inventory.common.importer.process.NamedAbstractImporter;
import mc.monacotelecom.tecrep.equipments.entity.SimCardGenerationConfiguration;
import mc.monacotelecom.tecrep.equipments.importer.tags.Tag;
import mc.monacotelecom.tecrep.equipments.repository.SimCardGenerationConfigurationRepository;
import org.springframework.stereotype.Service;

@Service
public class SimCardGenerationConfigurationImporter extends NamedAbstractImporter<SimCardGenerationConfiguration, ImportMapper.MappedLine<SimCardGenerationConfiguration>> {
    private final SimCardGenerationConfigurationRepository simCardGenerationConfigurationRepository;

    protected SimCardGenerationConfigurationImporter(SimCardGenerationConfigurationRepository simCardGenerationConfigurationRepository) {
        super(Tag.EQUIPMENTSADMIN);
        this.simCardGenerationConfigurationRepository = simCardGenerationConfigurationRepository;
    }

    @Override
    public void onParseLine(ImportMapper.MappedLine<SimCardGenerationConfiguration> parsedLine) {
        parsedLine.getNodes().forEach(simcardGenerationConfiguration -> {
            simCardGenerationConfigurationRepository.findByName(simcardGenerationConfiguration.getName()).ifPresent(
                    dbSimcardGenerationConfiguration -> simcardGenerationConfiguration.setSimcardGenerationId(dbSimcardGenerationConfiguration.getSimcardGenerationId()));
            validate(simcardGenerationConfiguration, parsedLine.getSaveDepth().get());
        });
    }
}
