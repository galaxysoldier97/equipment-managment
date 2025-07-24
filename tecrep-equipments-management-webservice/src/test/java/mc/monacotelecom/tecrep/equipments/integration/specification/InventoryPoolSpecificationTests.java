package mc.monacotelecom.tecrep.equipments.integration.specification;

import mc.monacotelecom.tecrep.equipments.dto.InventoryPoolDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchInventoryPoolDTO;
import mc.monacotelecom.tecrep.equipments.enums.SimCardSimProfile;
import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql({"/sql/clean.sql", "/sql/inventory_pool_data.sql"})
class InventoryPoolSpecificationTests extends BaseIntegrationTest {

    @Test
    void search_withCodeFound() {
        SearchInventoryPoolDTO searchInventoryPoolDTO = new SearchInventoryPoolDTO();
        searchInventoryPoolDTO.setCode("METEOR_PAIRED_CUSTOMER_POOL");

        Collection<InventoryPoolDTOV2> result = inventoryPoolProcess.searchV2(searchInventoryPoolDTO, pageable).getContent();

        assertEquals(1, result.size());
        final var inventoryPoolDTO = result.stream().findFirst().get();
        assertEquals("METEOR_PAIRED_CUSTOMER_POOL", inventoryPoolDTO.getCode());
    }

    @Test
    void search_withCodeNotFound() {
        SearchInventoryPoolDTO searchInventoryPoolDTO = new SearchInventoryPoolDTO();
        searchInventoryPoolDTO.setCode("MISSING");

        Collection<InventoryPoolDTOV2> result = inventoryPoolProcess.searchV2(searchInventoryPoolDTO, pageable).getContent();

        assertEquals(0, result.size());
    }

    @Test
    void search_withCodeNotValued() {
        SearchInventoryPoolDTO searchInventoryPoolDTO = new SearchInventoryPoolDTO();
        searchInventoryPoolDTO.setCode("");

        Collection<InventoryPoolDTOV2> result = inventoryPoolProcess.searchV2(searchInventoryPoolDTO, pageable).getContent();

        assertEquals(4, result.size());
    }

    @Test
    void search_withMvnoFound() {
        SearchInventoryPoolDTO searchInventoryPoolDTO = new SearchInventoryPoolDTO();
        searchInventoryPoolDTO.setMvno(0);

        Collection<InventoryPoolDTOV2> result = inventoryPoolProcess.searchV2(searchInventoryPoolDTO, pageable).getContent();

        assertEquals(1, result.size());
        final var inventoryPoolDTO = result.stream().findFirst().get();
        assertEquals("METEOR_PAIRED_CUSTOMER_POOL", inventoryPoolDTO.getCode());
    }

    @Test
    void search_withMvnoNotFound() {
        SearchInventoryPoolDTO searchInventoryPoolDTO = new SearchInventoryPoolDTO();
        searchInventoryPoolDTO.setMvno(9);

        Collection<InventoryPoolDTOV2> result = inventoryPoolProcess.searchV2(searchInventoryPoolDTO, pageable).getContent();

        assertEquals(0, result.size());
    }

    @Test
    void search_withSimProfileFound() {
        SearchInventoryPoolDTO searchInventoryPoolDTO = new SearchInventoryPoolDTO();
        searchInventoryPoolDTO.setSimProfile(SimCardSimProfile.REPLACEMENT);

        Collection<InventoryPoolDTOV2> result = inventoryPoolProcess.searchV2(searchInventoryPoolDTO, pageable).getContent();

        assertEquals(1, result.size());
        final var inventoryPoolDTO = result.stream().findFirst().get();
        assertEquals("ATTACHED_TO_BATCH", inventoryPoolDTO.getCode());
    }
}
