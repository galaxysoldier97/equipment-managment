package mc.monacotelecom.tecrep.equipments.integration.specification;

import mc.monacotelecom.tecrep.equipments.dto.PlmnDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchPlmnDTO;
import mc.monacotelecom.tecrep.equipments.entity.Plmn;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Sql({"/sql/clean.sql", "/sql/plmn_data.sql"})
class PlmnSpecificationTests extends BaseIntegrationTest {

    @Autowired
    private PagedResourcesAssembler<Plmn> plmnPagedResourcesAssembler;

    @Test
    void search_withCodeFound() {
        SearchPlmnDTO searchPlmnDTO = new SearchPlmnDTO();
        searchPlmnDTO.setCode("20202");

        Collection<PlmnDTO> result = plmnProcess.searchV1(searchPlmnDTO, pageable, plmnPagedResourcesAssembler).getContent();

        assertEquals(1, result.size());
        final var plmnDTO = result.stream().findFirst().get();
        assertEquals("20202", plmnDTO.getCode());
    }

    @Test
    @SuppressWarnings("java:S5778")
        // Message assertion makes the test specific
    void search_withCodeNotFound() {
        SearchPlmnDTO searchPlmnDTO = new SearchPlmnDTO();
        searchPlmnDTO.setCode("NOTHING");

        var e = assertThrows(EqmValidationException.class, () -> plmnProcess.searchV1(searchPlmnDTO, pageable, plmnPagedResourcesAssembler).getContent());
        assertEquals("No PLMNs have been found", e.getMessage());
    }

    @Test
    void search_withCodeNotValued() {
        SearchPlmnDTO searchPlmnDTO = new SearchPlmnDTO();
        searchPlmnDTO.setCode("");

        Collection<PlmnDTO> result = plmnProcess.searchV1(searchPlmnDTO, pageable, plmnPagedResourcesAssembler).getContent();

        assertEquals(5, result.size());
    }

    @Test
    void search_withPrefixFound() {
        SearchPlmnDTO searchPlmnDTO = new SearchPlmnDTO();
        searchPlmnDTO.setPrefix("3368900890254");

        Collection<PlmnDTO> result = plmnProcess.searchV1(searchPlmnDTO, pageable, plmnPagedResourcesAssembler).getContent();

        assertEquals(1, result.size());
        final var plmnDTO = result.stream().findFirst().get();
        assertEquals("20203", plmnDTO.getCode());
    }

    @Test
    @SuppressWarnings("java:S5778")
        // Message assertion makes the test specific
    void search_withPrefixNotFound() {
        SearchPlmnDTO searchPlmnDTO = new SearchPlmnDTO();
        searchPlmnDTO.setPrefix("NOTHING");

        var e = assertThrows(EqmValidationException.class, () -> plmnProcess.searchV1(searchPlmnDTO, pageable, plmnPagedResourcesAssembler).getContent());
        assertEquals("No PLMNs have been found", e.getMessage());
    }

    @Test
    void search_withPrefixNotValued() {
        SearchPlmnDTO searchPlmnDTO = new SearchPlmnDTO();
        searchPlmnDTO.setPrefix("");

        Collection<PlmnDTO> result = plmnProcess.searchV1(searchPlmnDTO, pageable, plmnPagedResourcesAssembler).getContent();

        assertEquals(5, result.size());
    }
}
