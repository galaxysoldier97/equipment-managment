package mc.monacotelecom.tecrep.equipments.integration.importer;

import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sql/clean.sql", "/sql/file_configuration_data.sql"})
class FileConfigurationImportTests extends BaseIntegrationTest {

    @Test
    void import_success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "importFC_OK.xlsx", "text/plain",
                new ClassPathResource("data/fileConfiguration/importFC_OK.xlsx").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/FileConfiguration/start?sync=true")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"importStatus\": \"COMPLETED\",\"errors\": []}"));

        String baseUrl = "/private/auth/fileConfiguration";
        mockMvc.perform(get(baseUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[{\"name\":\"default_export\",\"prefix\":\"MMC\",\"suffix\":\".out\",\"headerFormat\":null,\"recordFormat\":\"<div>${CONTENT2}</div>\"},{\"name\":\"test_export\",\"prefix\":\"MMCC\",\"suffix\":\".out2\",\"headerFormat\":null,\"recordFormat\":\"<div>${CONTENT2}</div>\"},{\"name\":\"test_export2\",\"prefix\":\"MMCC\",\"suffix\":\".out3\",\"headerFormat\":null,\"recordFormat\":\"<div>${CONTENT3}</div>\"},{\"name\":\"test_export3\",\"prefix\":\"MMCC\",\"suffix\":\".out4\",\"headerFormat\":null,\"recordFormat\":\"<div>${CONTENT3}</div>\"},{\"name\":\"test\",\"prefix\":\"aaa\",\"suffix\":\"bbbb\",\"headerFormat\":\"fdml\",\"recordFormat\":\"ldmsdsm\"}],\"pageable\":{\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"pageSize\":20,\"pageNumber\":0,\"offset\":0,\"paged\":true,\"unpaged\":false},\"totalPages\":1,\"totalElements\":5,\"last\":true,\"number\":0,\"size\":20,\"numberOfElements\":5,\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"first\":true,\"empty\":false}"));

        assertTrue(allImportsCompleted());
    }

    @Test
    void import_failureWithoutFile() throws Exception {
        mockMvc.perform(multipart("/private/auth/import/FileConfiguration/start?sync=true"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\": \"File is mandatory for import 'FileConfigurationImporter'\"}"));

        assertTrue(allImportsCompleted());
    }
}
