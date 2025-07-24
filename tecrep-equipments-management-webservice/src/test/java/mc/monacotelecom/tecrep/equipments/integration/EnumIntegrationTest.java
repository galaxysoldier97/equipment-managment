package mc.monacotelecom.tecrep.equipments.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EnumIntegrationTest extends BaseIntegrationTest {

    final String baseUrl = "/public/enums";

    @Test
    void list_accessType() throws Exception {
        mockMvc.perform(get(baseUrl + "/accesstype")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[\"DOCSIS\",\"FTTH\",\"DISE\",\"FREEDHOME\",\"ZATTOO\",\"TRUNKSIP\",\"BBHB\",\"MOBILE\"]"));
    }

    @Test
    void list_activity() throws Exception {
        mockMvc.perform(get(baseUrl + "/activity")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[\"MOBILE\", \"INTERNET\", \"TELEPHONY\", \"TV\", \"NDD\", \"OX\", \"MEVO\"]"));
    }

    @Test
    void list_allotmentType() throws Exception {
        mockMvc.perform(get(baseUrl + "/allotmenttype")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[\"PREPAID\", \"POSTPAID\", \"B2B\", \"B2C\", \"REPLACEMENT_SIM_CARD\"]"));
    }
}
