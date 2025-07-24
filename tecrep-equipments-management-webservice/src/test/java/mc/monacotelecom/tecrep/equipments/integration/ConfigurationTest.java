package mc.monacotelecom.tecrep.equipments.integration;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql({"/sql/clean.sql", "/sql/file_configuration_data.sql"})
class ConfigurationTest extends BaseIntegrationTest {

    @Test
    void getImportFilePattern() {
        var pattern = configurationService.getImportFilePattern();
        assertEquals("{MMC*.out,MMCC*.out2}", pattern);
    }
}
