package mc.monacotelecom.tecrep.equipments.integration;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.apache.http.HttpHeaders.ACCEPT_LANGUAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sql/clean.sql"})
class LocaleResolverTest extends BaseIntegrationTest {

    @Test
    void englishErrorByDefault() throws Exception {
        mockMvc.perform(get("/private/auth/simcards/13"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\": \"Simcard with ID '13' could not be found\"}"));
    }

    @Test
    void frenchErrorIfFrHeader() throws Exception {
        mockMvc.perform(get("/private/auth/simcards/13")
                        .header(ACCEPT_LANGUAGE, "fr-FR"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\": \"La carte SIM d'ID '13' n'existe pas\"}"));
    }

    @Test
    void englishErrorIfEnHeader() throws Exception {
        mockMvc.perform(get("/private/auth/simcards/13")
                        .header(ACCEPT_LANGUAGE, "en-US"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\": \"Simcard with ID '13' could not be found\"}"));
    }

    @Test
    void englishErrorIfUnknownHeader() throws Exception {
        mockMvc.perform(get("/private/auth/simcards/13")
                        .header(ACCEPT_LANGUAGE, "ko"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\": \"Simcard with ID '13' could not be found\"}"));
    }
}
