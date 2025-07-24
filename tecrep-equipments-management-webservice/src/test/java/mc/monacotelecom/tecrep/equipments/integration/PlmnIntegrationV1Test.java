package mc.monacotelecom.tecrep.equipments.integration;

import mc.monacotelecom.tecrep.equipments.dto.PlmnDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sql/clean.sql", "/sql/plmn_data.sql"})
class PlmnIntegrationV1Test extends BaseIntegrationTest {

    final String baseUrl = "/private/auth/plmns";

    @Test
    void getById_success() throws Exception {
        final long id = 1L;
        mockMvc.perform(get(baseUrl + "/{plmnId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"plmnId\":1,\"code\":\"20201\",\"networkName\":\"Cosmote Mobile Telecommunications S.A.\",\"tadigCode\":\"GRCCO\",\"countryIsoCode\":\"GRC\",\"countryName\":\"Greece\",\"rangesPrefix\":\"3368900890252\"}\n"));
    }

    @Test
    void getById_notFound() throws Exception {
        final long id = 10L;
        mockMvc.perform(get(baseUrl + "/{plmnId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"PLMN with ID '10' not found\"}"));
    }

    @Test
    void getAll_success() throws Exception {

        mockMvc.perform(get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements").value(5))
                .andExpect(jsonPath("$._embedded.plmns[0].plmnId").value(1))
                .andExpect(jsonPath("$._embedded.plmns[0].code").value("20201"))
                .andExpect(jsonPath("$._embedded.plmns[0].countryIsoCode").value("GRC"))
                .andExpect(jsonPath("$._embedded.plmns[0].countryName").value("Greece"))
                .andExpect(jsonPath("$._embedded.plmns[0].tadigCode").value("GRCCO"));
    }

    @Test
    void deleteById_success() throws Exception {
        final long id = 5L;
        mockMvc.perform(delete(baseUrl + "/{plmnId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteById_notFound() throws Exception {
        final long id = 10L;
        mockMvc.perform(delete(baseUrl + "/{plmnId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void search_success() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("prefix", "3368900890252");

        mockMvc.perform(get(baseUrl + "/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(map))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"_embedded\":{\"plmns\":[{\"plmnId\":1,\"code\":\"20201\",\"networkName\":\"Cosmote Mobile Telecommunications S.A.\",\"tadigCode\":\"GRCCO\",\"countryIsoCode\":\"GRC\",\"countryName\":\"Greece\",\"rangesPrefix\":\"3368900890252\"}]},\"_links\":{\"self\":{\"href\":\"http://localhost/plmns\"}},\"page\":{\"size\":20,\"totalElements\":1,\"totalPages\":1,\"number\":0}}\n"));
    }

    @Test
    void update_success() throws Exception {
        final long id = 1L;
        PlmnDTO dto = new PlmnDTO();
        dto.setCode("20209");
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(baseUrl + "/{plmnId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"plmnId\":1,\"code\":\"20209\",\"networkName\":null,\"tadigCode\":null,\"countryIsoCode\":null,\"countryName\":null,\"rangesPrefix\":null}\n"));
    }

    @Test
    void update_notFound() throws Exception {
        final long id = 10L;
        mockMvc.perform(get(baseUrl + "/{plmnId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"PLMN with ID '10' not found\"}"));
    }

    @Test
    void add_success() throws Exception {

        PlmnDTO dto = new PlmnDTO();
        dto.setCode("20209");
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"plmnId\":6,\"code\":\"20209\",\"networkName\":null,\"tadigCode\":null,\"countryIsoCode\":null,\"countryName\":null,\"rangesPrefix\":null}\n"));
    }

    @Test
    void add_alreadyExisting() throws Exception {

        PlmnDTO dto = new PlmnDTO();
        dto.setCode("20205");
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"Plmn code '20205' already exists\"}"));
    }
}
