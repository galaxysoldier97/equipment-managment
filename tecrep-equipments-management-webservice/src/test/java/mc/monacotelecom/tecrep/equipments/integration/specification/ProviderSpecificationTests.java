package mc.monacotelecom.tecrep.equipments.integration.specification;

import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchProviderDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.ProviderDTOV2;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql({"/sql/clean.sql", "/sql/provider_data.sql"})
class ProviderSpecificationTests extends BaseIntegrationTest {

    @Test
    void search_withNameFound() {
        SearchProviderDTO searchProviderDTO = new SearchProviderDTO();
        searchProviderDTO.setName("Gemalto");

        Collection<ProviderDTOV2> result = providerProcess.search(searchProviderDTO, pageable).getContent();

        assertEquals(1, result.size());
        final var providerDTO = result.stream().findFirst().get();
        assertEquals("Gemalto", providerDTO.getName());
    }

    @Test
    void search_withNameNotFound() {
        SearchProviderDTO searchProviderDTO = new SearchProviderDTO();
        searchProviderDTO.setName("NOTHING");

        Collection<ProviderDTOV2> result = providerProcess.search(searchProviderDTO, pageable).getContent();

        assertEquals(0, result.size());
    }

    @Test
    void search_withNameNotValued() {
        SearchProviderDTO searchProviderDTO = new SearchProviderDTO();
        searchProviderDTO.setName("");

        Collection<ProviderDTOV2> result = providerProcess.search(searchProviderDTO, pageable).getContent();

        assertEquals(5, result.size());
    }

    @Test
    void search_withAccessTypeFound() {
        SearchProviderDTO searchProviderDTO = new SearchProviderDTO();
        searchProviderDTO.setAccessType(AccessType.STB_ONT);

        Collection<ProviderDTOV2> result = providerProcess.search(searchProviderDTO, pageable).getContent();

        assertEquals(1, result.size());
        final var providerDTO = result.stream().findFirst().get();
        assertEquals("Gemalto", providerDTO.getName());
    }

    @Test
    void search_withAccessTypeNotFound() {
        SearchProviderDTO searchProviderDTO = new SearchProviderDTO();
        searchProviderDTO.setAccessType(AccessType.EXTENSOR_WIFI);

        Collection<ProviderDTOV2> result = providerProcess.search(searchProviderDTO, pageable).getContent();

        assertEquals(0, result.size());
    }
}
