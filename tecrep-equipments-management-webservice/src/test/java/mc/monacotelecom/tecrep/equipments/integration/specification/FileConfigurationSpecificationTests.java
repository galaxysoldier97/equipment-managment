package mc.monacotelecom.tecrep.equipments.integration.specification;

import mc.monacotelecom.tecrep.equipments.dto.FileConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchFileConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql({"/sql/clean.sql", "/sql/file_configuration_data.sql"})
class FileConfigurationSpecificationTests extends BaseIntegrationTest {

    @Test
    void search_withNameFound() {
        SearchFileConfigurationDTO searchFileConfigurationDTO = new SearchFileConfigurationDTO();
        searchFileConfigurationDTO.setName("default_export");

        Collection<FileConfigurationDTO> result = fileConfigurationProcess.search(searchFileConfigurationDTO, pageable).getContent();

        assertEquals(1, result.size());
        final var fileConfigurationDTO = result.stream().findFirst().get();
        assertEquals("default_export", fileConfigurationDTO.getName());
    }

    @Test
    void search_withNameNotFound() {
        SearchFileConfigurationDTO searchFileConfigurationDTO = new SearchFileConfigurationDTO();
        searchFileConfigurationDTO.setName("UNKNOWN");

        Collection<FileConfigurationDTO> result = fileConfigurationProcess.search(searchFileConfigurationDTO, pageable).getContent();

        assertEquals(0, result.size());
    }

    @Test
    void search_withNameNotValued() {
        SearchFileConfigurationDTO searchFileConfigurationDTO = new SearchFileConfigurationDTO();
        searchFileConfigurationDTO.setName("");

        Collection<FileConfigurationDTO> result = fileConfigurationProcess.search(searchFileConfigurationDTO, pageable).getContent();

        assertEquals(4, result.size());
    }
}
