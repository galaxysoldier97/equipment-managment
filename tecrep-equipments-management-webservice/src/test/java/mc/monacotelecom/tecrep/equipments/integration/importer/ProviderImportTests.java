package mc.monacotelecom.tecrep.equipments.integration.importer;

import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;

import static mc.monacotelecom.tecrep.equipments.enums.AccessType.MOBILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sql/clean.sql", "/sql/provider_data.sql"})
class ProviderImportTests extends BaseIntegrationTest {

    @Test
    void import_success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "importProvider_OK.xlsx", "text/plain",
                new ClassPathResource("data/provider/importProvider_OK.xlsx").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/Provider/start?sync=true")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"importStatus\": \"COMPLETED\",\"errors\": []}"));

        var providerOpt = providerRepository.findByName("IDEMIA");
        assertTrue(providerOpt.isPresent());

        var provider = providerOpt.get();
        assertEquals(MOBILE, provider.getAccessType());
        assertEquals("IDEMIA", provider.getName());

        assertTrue(allImportsCompleted());
    }

    @Test
    void import_failureWithoutFile() throws Exception {
        mockMvc.perform(multipart("/private/auth/import/Provider/start?sync=true"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\": \"File is mandatory for import 'ProviderImporter'\"}"));

        assertTrue(allImportsCompleted());
    }
}
