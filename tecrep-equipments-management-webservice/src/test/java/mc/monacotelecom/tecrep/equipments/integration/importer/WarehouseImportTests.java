package mc.monacotelecom.tecrep.equipments.integration.importer;

import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sql/clean.sql", "/sql/warehouse_data.sql"})
class WarehouseImportTests extends BaseIntegrationTest {

    @Test
    void import_success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "importWarehouse_OK.xlsx", "text/plain",
                new ClassPathResource("data/warehouse/importWarehouse_OK.xlsx").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/Warehouse/start?sync=true")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"importStatus\": \"COMPLETED\",\"errors\": []}"));

        var warehouseOpt = warehouseRepository.findByName("Test Warehouse");
        assertTrue(warehouseOpt.isPresent());

        var warehouse = warehouseOpt.get();
        assertEquals("TW", warehouse.getResellerCode());
        assertEquals("Test Warehouse", warehouse.getName());

        assertTrue(allImportsCompleted());
    }

    @Test
    void import_failureWithoutFile() throws Exception {
        mockMvc.perform(multipart("/private/auth/import/Warehouse/start?sync=true"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\": \"File is mandatory for import 'WarehouseImporter'\"}"));

        assertTrue(allImportsCompleted());
    }
}
