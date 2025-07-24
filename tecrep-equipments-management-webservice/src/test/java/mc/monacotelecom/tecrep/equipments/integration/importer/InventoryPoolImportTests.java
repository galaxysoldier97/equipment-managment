package mc.monacotelecom.tecrep.equipments.integration.importer;

import mc.monacotelecom.tecrep.equipments.enums.SimCardSimProfile;
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

@Sql({"/sql/clean.sql", "/sql/inventory_pool_data.sql"})
class InventoryPoolImportTests extends BaseIntegrationTest {

    @Test
    void import_inventoryPoolCRUD_success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "importIP_OK.xlsx", "text/plain",
                new ClassPathResource("data/inventoryPool/importIP_OK.xlsx").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/InventoryPool/start?sync=true")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"importStatus\": \"COMPLETED\",\"errors\": []}"));

        var ipOpt = inventoryPoolRepository.findByCode("test");
        assertTrue(ipOpt.isPresent());

        var ip = ipOpt.get();
        assertEquals("test", ip.getCode());
        assertEquals("Test", ip.getDescription());
        assertEquals((Integer) 4, ip.getMvno());
        assertEquals(SimCardSimProfile.DEFAULT, ip.getSimProfile());

        assertTrue(allImportsCompleted());
    }

    @Test
    void import_inventoryPoolCRUD_updateExisting() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "importIP_ExistingCode.xlsx", "text/plain",
                new ClassPathResource("data/inventoryPool/importIP_ExistingCode.xlsx").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/InventoryPool/start?sync=true")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"importStatus\": \"COMPLETED\",\"errors\": []}"));

        var ipOpt = inventoryPoolRepository.findByCode("METEOR_PAIRED_CUSTOMER_POOL");
        assertTrue(ipOpt.isPresent());

        var ip = ipOpt.get();
        assertEquals("METEOR_PAIRED_CUSTOMER_POOL", ip.getCode());
        assertEquals("Something else", ip.getDescription());
        assertEquals((Integer) 4, ip.getMvno());
        assertEquals(SimCardSimProfile.DEFAULT, ip.getSimProfile());

        assertTrue(allImportsCompleted());
    }

    @Test
    void import_failureWithoutFile() throws Exception {
        mockMvc.perform(multipart("/private/auth/import/InventoryPool/start?sync=true"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\": \"File is mandatory for import 'InventoryPoolImporter'\"}"));

        assertTrue(allImportsCompleted());
    }
}
