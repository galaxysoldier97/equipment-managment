package mc.monacotelecom.tecrep.equipments.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sql/clean.sql", "/sql/batch_data.sql"})
class StatControllerIntegrationTest extends BaseIntegrationTest {

    final String baseUrl = "/private/auth/equipmentsDashboard";

    @Test
    void resourcesDashboard_success() throws Exception {
        mockMvc.perform(get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"statusOfEquipments\":{\"INSTORE\":6}}"));
    }
}
