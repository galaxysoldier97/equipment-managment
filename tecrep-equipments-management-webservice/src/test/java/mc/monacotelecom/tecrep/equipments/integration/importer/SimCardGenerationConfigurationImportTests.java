package mc.monacotelecom.tecrep.equipments.integration.importer;

import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sql/clean.sql", "/sql/simcard_generation_data.sql"})
class SimCardGenerationConfigurationImportTests extends BaseIntegrationTest {

    @Test
    void import_simCardGenerationConfigurationCRUD_success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "import_OK.xlsx", "text/plain",
                new ClassPathResource("data/simCardGenerationConfiguration/import_OK.xlsx").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/SimCardGenerationConfiguration/start?sync=true")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"importStatus\": \"COMPLETED\",\"errors\": []}"));

        var scgcOpt = simCardGenerationConfigurationRepository.findByName("test");
        assertTrue(scgcOpt.isPresent());

        var scgc = scgcOpt.get();
        assertAll(
                () -> assertEquals("test", scgc.getName()),
                () -> assertEquals("default_export", scgc.getExportFileConfiguration().getName()),
                () -> assertEquals("default_import", scgc.getImportFileConfiguration().getName()),
                () -> assertEquals("01", scgc.getTransportKey()),
                () -> assertEquals((Integer) 2, scgc.getAlgorithmVersion()),
                () -> assertEquals("27203", scgc.getPlmn().getCode()),
                () -> assertEquals("Technicolor", scgc.getProvider().getName()),
                () -> assertEquals("DEFAULT1", scgc.getMsinSequence()),
                () -> assertEquals("DEFAULT2", scgc.getIccidSequence()),
                () -> assertEquals("EIR_SME_REP", scgc.getArtwork()),
                () -> assertEquals("MET_PP001_LTE", scgc.getSimReference()),
                () -> assertEquals("MULTISIM", scgc.getType()),
                () -> assertEquals("8935303", scgc.getFixedPrefix()),
                () -> assertEquals("0524", scgc.getSequencePrefix())
        );

        assertTrue(allImportsCompleted());
    }

    @Test
    void import_simCardGenerationConfigurationCRUD_missingProvider() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "import_KO_provider.xlsx", "text/plain",
                new ClassPathResource("data/simCardGenerationConfiguration/import_KO_provider.xlsx").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/SimCardGenerationConfiguration/start?sync=true")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"importStatus\": \"COMPLETED\",\"errors\":[{\"line\":1,\"error\":\"java.lang.RuntimeException Mapper SimCardGenerationConfigurationMapperImpl invocation error: Provider with name 'Missing' could not be found\"}]}"));

        assertTrue(allImportsCompleted());
    }

    @Test
    void import_simCardGenerationConfigurationCRUD_missingPlmn() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "import_KO_plmn.xlsx", "text/plain",
                new ClassPathResource("data/simCardGenerationConfiguration/import_KO_plmn.xlsx").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/SimCardGenerationConfiguration/start?sync=true")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"importStatus\": \"COMPLETED\",\"errors\":[{\"line\":1,\"error\":\"java.lang.RuntimeException Mapper SimCardGenerationConfigurationMapperImpl invocation error: PLMN with code '11111' could not be found\"}]}"));

        assertTrue(allImportsCompleted());
    }

    @Test
    void import_simCardGenerationConfigurationCRUD_missingExportFileConfiguration() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "import_KO_export.xlsx", "text/plain",
                new ClassPathResource("data/simCardGenerationConfiguration/import_KO_export.xlsx").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/SimCardGenerationConfiguration/start?sync=true")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"importStatus\": \"COMPLETED\",\"errors\":[{\"line\":1,\"error\":\"java.lang.RuntimeException Mapper SimCardGenerationConfigurationMapperImpl invocation error: File configuration with name 'missing' could not be found\"}]}"));

        assertTrue(allImportsCompleted());
    }

    @Test
    void import_simCardGenerationConfigurationCRUD_missingImportFileConfiguration() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "import_KO_import.xlsx", "text/plain",
                new ClassPathResource("data/simCardGenerationConfiguration/import_KO_import.xlsx").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/SimCardGenerationConfiguration/start?sync=true")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"importStatus\": \"COMPLETED\",\"errors\":[{\"line\":1,\"error\":\"java.lang.RuntimeException Mapper SimCardGenerationConfigurationMapperImpl invocation error: File configuration with name 'missing' could not be found\"}]}"));

        assertTrue(allImportsCompleted());
    }

    @Test
    void import_simCardGenerationConfigurationCRUD_updateExisting() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "import_KO_name.xlsx", "text/plain",
                new ClassPathResource("data/simCardGenerationConfiguration/import_KO_name.xlsx").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/SimCardGenerationConfiguration/start?sync=true")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"importStatus\": \"COMPLETED\",\"errors\": []}"));

        assertTrue(allImportsCompleted());
    }

    @Test
    void import_failureWithoutFile() throws Exception {
        mockMvc.perform(multipart("/private/auth/import/SimCardGenerationConfiguration/start?sync=true"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\": \"File is mandatory for import 'SimCardGenerationConfigurationImporter'\"}"));

        assertTrue(allImportsCompleted());
    }
}
