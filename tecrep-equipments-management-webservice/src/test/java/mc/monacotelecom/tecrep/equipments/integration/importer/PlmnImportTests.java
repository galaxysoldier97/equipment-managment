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

@Sql({"/sql/clean.sql", "/sql/plmn_data.sql"})
class PlmnImportTests extends BaseIntegrationTest {

    @Test
    void import_success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "importPlmn_OK.xlsx", "text/plain",
                new ClassPathResource("data/plmn/importPlmn_OK.xlsx").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/Plmn/start?sync=true")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"importStatus\": \"COMPLETED\",\"errors\": []}"));

        var plmnOpt = plmnRepository.findByCode("27203");
        assertTrue(plmnOpt.isPresent());

        var plmn = plmnOpt.get();
        assertEquals("Meteor Mobile Telecommunications Limited", plmn.getNetworkName());
        assertEquals("27203", plmn.getCode());
        assertEquals("IRLME", plmn.getTadigCode());
        assertEquals("IRL", plmn.getCountryIsoCode());
        assertEquals("Ireland", plmn.getCountryName());
        assertEquals("3368900890401", plmn.getRangesPrefix());

        assertTrue(allImportsCompleted());
    }

    @Test
    void import_failureWithoutFile() throws Exception {
        mockMvc.perform(multipart("/private/auth/import/Plmn/start?sync=true"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\": \"File is mandatory for import 'PlmnImporter'\"}"));

        assertTrue(allImportsCompleted());
    }
}
