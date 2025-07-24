package mc.monacotelecom.tecrep.equipments.integration.specification;

import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchEquipmentModelDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.EquipmentModelDTOV2;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory;
import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql({"/sql/clean.sql", "/sql/equipmentModel_data.sql"})
class EquipmentModelSpecificationTests extends BaseIntegrationTest {

    @Test
    void search_withCategoryAncillary() {
        SearchEquipmentModelDTO dto = new SearchEquipmentModelDTO();
        dto.setCategory(EquipmentModelCategory.ANCILLARY);

        Collection<EquipmentModelDTOV2> result = equipmentModelProcess.search(dto, pageable).getContent();

        assertEquals(0, result.size());
    }

    @Test
    void search_withCategoryCPE() {
        SearchEquipmentModelDTO dto = new SearchEquipmentModelDTO();
        dto.setCategory(EquipmentModelCategory.CPE);

        Collection<EquipmentModelDTOV2> result = equipmentModelProcess.search(dto, pageable).getContent();

        assertEquals(4, result.size());
    }

    @Test
    void search_withCategoryNotValued() {
        SearchEquipmentModelDTO dto = new SearchEquipmentModelDTO();
        dto.setCategory(null);

        Collection<EquipmentModelDTOV2> result = equipmentModelProcess.search(dto, pageable).getContent();

        assertEquals(4, result.size());
    }
}
