package mc.monacotelecom.tecrep.equipments.process;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.repository.FileConfigurationRepository;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConfigurationProcess {
    private final FileConfigurationRepository fileConfigurationRepository;

    /**
     * Globbing pattern for multiple files: {MMC*.out,TST*.out}
     */
    public String getImportFilePattern() {
        var patterns = fileConfigurationRepository.findImportFileConfigurations()
                .stream()
                .map(pattern -> pattern.getPrefix() + "*" + pattern.getSuffix())
                .collect(Collectors.joining(","));
        return "{" + patterns + "}";
    }
}
