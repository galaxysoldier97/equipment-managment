package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.process.ConfigurationProcess;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ConfigurationService {
    private final ConfigurationProcess configurationProcess;

    @Transactional(readOnly = true)
    public String getImportFilePattern() {
        return configurationProcess.getImportFilePattern();
    }
}
