package mc.monacotelecom.tecrep.equipments.integration.specification;

import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchEsimNotificationDTO;
import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional(readOnly = true)
@Sql({"/sql/clean.sql", "/sql/simcard_data.sql"})
public class EsimNotificationSpecificationTest extends BaseIntegrationTest {

    @Test
    void search_withIccd_found() {
        var searchDto = new SearchEsimNotificationDTO();
        searchDto.setIccid("34554323456543");

        var resultList = esimNotificationProcess.search(searchDto, pageable).getContent();

        assertEquals(1, resultList.size());
        final var result = resultList.stream().findFirst().get();
        assertEquals((Long) 1L, result.getId());
    }

    @Test
    void search_withIccd_notFound() {
        var searchDto = new SearchEsimNotificationDTO();
        searchDto.setIccid("3455432345654344332");

        var resultList = esimNotificationProcess.search(searchDto, pageable).getContent();

        assertEquals(0, resultList.size());
    }

    @Test
    void search_withIccd_notValued() {
        var searchDto = new SearchEsimNotificationDTO();
        searchDto.setIccid("");

        var resultList = esimNotificationProcess.search(searchDto, pageable).getContent();

        assertEquals(2, resultList.size());
    }

    @Test
    void search_withEquipmentId_found() {
        var searchDto = new SearchEsimNotificationDTO();
        searchDto.setEquipmentId(1L);

        var resultList = esimNotificationProcess.search(searchDto, pageable).getContent();

        assertEquals(2, resultList.size());
        final var result = resultList.stream().findFirst().get();
        assertEquals((Long) 1L, result.getId());
    }

    @Test
    void search_withEquipmentId_notFound() {
        var searchDto = new SearchEsimNotificationDTO();
        searchDto.setEquipmentId(3L);

        var resultList = esimNotificationProcess.search(searchDto, pageable).getContent();

        assertEquals(0, resultList.size());
    }

    @Test
    void search_withEquipmentId_notValued() {
        var searchDto = new SearchEsimNotificationDTO();
        searchDto.setEquipmentId(null);

        var resultList = esimNotificationProcess.search(searchDto, pageable).getContent();

        assertEquals(2, resultList.size());
    }

    @Test
    void search_withProfileType_found() {
        var searchDto = new SearchEsimNotificationDTO();
        searchDto.setProfileType("test-2");

        var resultList = esimNotificationProcess.search(searchDto, pageable).getContent();

        assertEquals(1, resultList.size());
        final var result = resultList.stream().findFirst().get();
        assertEquals((Long) 2L, result.getId());
    }

    @Test
    void search_withProfileType_notFound() {
        var searchDto = new SearchEsimNotificationDTO();
        searchDto.setProfileType("test-3");

        var resultList = esimNotificationProcess.search(searchDto, pageable).getContent();

        assertEquals(0, resultList.size());
    }

    @Test
    void search_withProfileType_notValued() {
        var searchDto = new SearchEsimNotificationDTO();
        searchDto.setProfileType("");

        var resultList = esimNotificationProcess.search(searchDto, pageable).getContent();

        assertEquals(2, resultList.size());
    }
}
