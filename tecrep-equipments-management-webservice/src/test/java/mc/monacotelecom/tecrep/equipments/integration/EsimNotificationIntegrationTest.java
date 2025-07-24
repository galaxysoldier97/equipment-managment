package mc.monacotelecom.tecrep.equipments.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sql/clean.sql", "/sql/simcard_data.sql"})
public class EsimNotificationIntegrationTest extends BaseIntegrationTest {

    final String baseUrl = "/esim/notification";

    @Test
    void retrieve_notification_byIccid() throws Exception {
        mockMvc.perform(get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("iccid","34554323456543"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    void retrieve_notification_byEquipmentId() throws Exception {
        mockMvc.perform(get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("equipmentId","1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    void retrieve_notification_byProfileType() throws Exception {
        mockMvc.perform(get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("profileType","test-2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(2));
    }

    @Test
    void retrieve_notification_not_found() throws Exception {
        mockMvc.perform(get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("profileType","none"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }
}
