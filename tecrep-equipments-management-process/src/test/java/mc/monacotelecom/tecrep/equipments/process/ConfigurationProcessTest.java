package mc.monacotelecom.tecrep.equipments.process;

import mc.monacotelecom.tecrep.equipments.entity.FileConfiguration;
import mc.monacotelecom.tecrep.equipments.repository.FileConfigurationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfigurationProcessTest {

    @Mock
    private FileConfigurationRepository fileConfigurationRepository;

    @InjectMocks
    private ConfigurationProcess configurationProcess;

    @Test
    void getImportFilePattern_empty() {
        when(fileConfigurationRepository.findImportFileConfigurations())
                .thenReturn(Collections.emptyList());

        var pattern = configurationProcess.getImportFilePattern();
        assertEquals("{}", pattern);
    }

    @Test
    void getImportFilePattern_single() {
        var fc = new FileConfiguration();
        fc.setPrefix("MMC");
        fc.setSuffix(".out");
        when(fileConfigurationRepository.findImportFileConfigurations())
                .thenReturn(Collections.singletonList(fc));

        var pattern = configurationProcess.getImportFilePattern();
        assertEquals("{MMC*.out}", pattern);
    }

    @Test
    void getImportFilePattern_multiple() {
        var fc1 = new FileConfiguration();
        fc1.setPrefix("MMC");
        fc1.setSuffix(".out");

        var fc2 = new FileConfiguration();
        fc2.setPrefix("CAT");
        fc2.setSuffix(".tst");
        when(fileConfigurationRepository.findImportFileConfigurations())
                .thenReturn(List.of(fc1, fc2));

        var pattern = configurationProcess.getImportFilePattern();
        assertEquals("{MMC*.out,CAT*.tst}", pattern);
    }
}
