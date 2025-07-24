package mc.monacotelecom.tecrep.equipments.integration.specification;

import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchWarehouseDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.WarehouseDTOV2;
import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql({"/sql/clean.sql", "/sql/warehouse_data.sql"})
class WarehouseSpecificationTests extends BaseIntegrationTest {

    @Test
    void search_withNameFound() {
        SearchWarehouseDTO searchWarehouseDTO = new SearchWarehouseDTO();
        searchWarehouseDTO.setName("Monaco");

        Collection<WarehouseDTOV2> result = wareHouseProcess.search(searchWarehouseDTO, pageable).getContent();

        assertEquals(1, result.size());
        final var warehouseDTO = result.stream().findFirst().get();
        assertEquals("Monaco Telecom Entreprise", warehouseDTO.getName());
    }

    @Test
    void search_withNameNotFound() {
        SearchWarehouseDTO searchWarehouseDTO = new SearchWarehouseDTO();
        searchWarehouseDTO.setName("NOTHING");

        Collection<WarehouseDTOV2> result = wareHouseProcess.search(searchWarehouseDTO, pageable).getContent();

        assertEquals(0, result.size());
    }

    @Test
    void search_withNameNotValued() {
        SearchWarehouseDTO searchWarehouseDTO = new SearchWarehouseDTO();
        searchWarehouseDTO.setName("");

        Collection<WarehouseDTOV2> result = wareHouseProcess.search(searchWarehouseDTO, pageable).getContent();

        assertEquals(4, result.size());
    }

    @Test
    void search_withResellerCodeFound() {
        SearchWarehouseDTO searchWarehouseDTO = new SearchWarehouseDTO();
        searchWarehouseDTO.setResellerCode("MTENT");

        Collection<WarehouseDTOV2> result = wareHouseProcess.search(searchWarehouseDTO, pageable).getContent();

        assertEquals(1, result.size());
        final var warehouseDTO = result.stream().findFirst().get();
        assertEquals("Monaco Telecom Entreprise", warehouseDTO.getName());
    }

    @Test
    void search_withResellerCodeNotFound() {
        SearchWarehouseDTO searchWarehouseDTO = new SearchWarehouseDTO();
        searchWarehouseDTO.setResellerCode("NOTHING");

        Collection<WarehouseDTOV2> result = wareHouseProcess.search(searchWarehouseDTO, pageable).getContent();

        assertEquals(0, result.size());
    }

    @Test
    void search_withResellerCodeNotValued() {
        SearchWarehouseDTO searchWarehouseDTO = new SearchWarehouseDTO();
        searchWarehouseDTO.setResellerCode("");

        Collection<WarehouseDTOV2> result = wareHouseProcess.search(searchWarehouseDTO, pageable).getContent();

        assertEquals(4, result.size());
    }
}
