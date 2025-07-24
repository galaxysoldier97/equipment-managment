package mc.monacotelecom.tecrep.equipments.integration.specification;

import mc.monacotelecom.tecrep.equipments.dto.SimCardGenerationConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchSimCardGenerationConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql({"/sql/clean.sql", "/sql/simcard_generation_configuration_data.sql"})
class SimCardGenerationConfigurationSpecificationTests extends BaseIntegrationTest {

    @Test
    void search_withNameFound() {
        SearchSimCardGenerationConfigurationDTO searchSimCardGenerationConfigurationDTO = new SearchSimCardGenerationConfigurationDTO();
        searchSimCardGenerationConfigurationDTO.setName("EIRCOM_FCS_MOB_LANDLINE_UNPAIR");

        Collection<SimCardGenerationConfigurationDTO> result = simCardGenerationConfigurationProcess.searchV1(searchSimCardGenerationConfigurationDTO, pageable).getContent();

        assertEquals(1, result.size());
        final var simCardGenerationConfigurationDTO = result.stream().findFirst().get();
        assertEquals("EIRCOM_FCS_MOB_LANDLINE_UNPAIR", simCardGenerationConfigurationDTO.getName());
    }

    @Test
    void search_withNameNotFound() {
        SearchSimCardGenerationConfigurationDTO searchSimCardGenerationConfigurationDTO = new SearchSimCardGenerationConfigurationDTO();
        searchSimCardGenerationConfigurationDTO.setName("UNKNOWN");

        Collection<SimCardGenerationConfigurationDTO> result = simCardGenerationConfigurationProcess.searchV1(searchSimCardGenerationConfigurationDTO, pageable).getContent();

        assertEquals(0, result.size());
    }

    @Test
    void search_withNameNotValued() {
        SearchSimCardGenerationConfigurationDTO searchSimCardGenerationConfigurationDTO = new SearchSimCardGenerationConfigurationDTO();
        searchSimCardGenerationConfigurationDTO.setName("");

        Collection<SimCardGenerationConfigurationDTO> result = simCardGenerationConfigurationProcess.searchV1(searchSimCardGenerationConfigurationDTO, pageable).getContent();

        assertEquals(2, result.size());
    }

    @Test
    void search_withTransportKeyFound() {
        SearchSimCardGenerationConfigurationDTO searchSimCardGenerationConfigurationDTO = new SearchSimCardGenerationConfigurationDTO();
        searchSimCardGenerationConfigurationDTO.setTransportKey(6);

        Collection<SimCardGenerationConfigurationDTO> result = simCardGenerationConfigurationProcess.searchV1(searchSimCardGenerationConfigurationDTO, pageable).getContent();

        assertEquals(1, result.size());
        final var simCardGenerationConfigurationDTO = result.stream().findFirst().get();
        assertEquals("EIRCOM_FCS_MOB_LANDLINE_UNPAIR", simCardGenerationConfigurationDTO.getName());
    }

    @Test
    void search_withTransportKeyNotFound() {
        SearchSimCardGenerationConfigurationDTO searchSimCardGenerationConfigurationDTO = new SearchSimCardGenerationConfigurationDTO();
        searchSimCardGenerationConfigurationDTO.setTransportKey(99);

        Collection<SimCardGenerationConfigurationDTO> result = simCardGenerationConfigurationProcess.searchV1(searchSimCardGenerationConfigurationDTO, pageable).getContent();

        assertEquals(0, result.size());
    }
}
