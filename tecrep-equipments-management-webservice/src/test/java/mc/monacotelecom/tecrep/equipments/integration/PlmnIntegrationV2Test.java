package mc.monacotelecom.tecrep.equipments.integration;

import mc.monacotelecom.tecrep.equipments.dto.v2.PlmnDTOV2;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sql/clean.sql", "/sql/plmn_data.sql"})
class PlmnIntegrationV2Test extends BaseIntegrationTest {

    final String baseUrl = "/api/v2/private/auth/plmns";

    @Test
    void getById_success() throws Exception {
        final long id = 1L;
        mockMvc.perform(get(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("20201"))
                .andExpect(jsonPath("$.networkName").value("Cosmote Mobile Telecommunications S.A."))
                .andExpect(jsonPath("$.tadigCode").value("GRCCO"))
                .andExpect(jsonPath("$.countryIsoCode").value("GRC"))
                .andExpect(jsonPath("$.countryName").value("Greece"))
                .andExpect(jsonPath("$.rangesPrefix").value("3368900890252"));
    }

    @Test
    void getById_notFound() throws Exception {
        final long id = 10L;
        mockMvc.perform(get(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"PLMN with ID '10' not found\"}"));
    }

    @Test
    void deleteById_success() throws Exception {
        final long id = 5L;
        mockMvc.perform(delete(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteById_notFound() throws Exception {
        final long id = 10L;
        mockMvc.perform(delete(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void search_success() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("prefix", "3368900890252");

        mockMvc.perform(get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(map))
                .andDo(print())

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].code").value(20201))
                .andExpect(jsonPath("$.content[0].networkName").value("Cosmote Mobile Telecommunications S.A."))
                .andExpect(jsonPath("$.content[0].rangesPrefix").value("3368900890252"))
                .andExpect(jsonPath("$.content[0].countryName").value("Greece"))
                .andExpect(jsonPath("$.content[0].countryIsoCode").value("GRC"))
                .andExpect(jsonPath("$.content[0].tadigCode").value("GRCCO"));
    }

    @Test
    void update_success() throws Exception {
        final long id = 1L;
        PlmnDTOV2 dto = new PlmnDTOV2();
        dto.setCode("20209");
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("20209"))
                .andExpect(jsonPath("$.networkName").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.tadigCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.countryIsoCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.countryName").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.rangesPrefix").value(IsNull.nullValue()));
    }

    @Test
    void update_notFound() throws Exception {
        final long id = 10L;
        mockMvc.perform(get(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"PLMN with ID '10' not found\"}"));
    }

    @Test
    void add_success() throws Exception {

        PlmnDTOV2 dto = new PlmnDTOV2();
        dto.setCode("20209");
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(6))
                .andExpect(jsonPath("$.code").value("20209"))
                .andExpect(jsonPath("$.networkName").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.tadigCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.countryIsoCode").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.countryName").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.rangesPrefix").value(IsNull.nullValue()));
    }

    @Test
    void add_alreadyExisting() throws Exception {

        PlmnDTOV2 dto = new PlmnDTOV2();
        dto.setCode("20205");
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Plmn code '20205' already exists\"}"));
    }
}
