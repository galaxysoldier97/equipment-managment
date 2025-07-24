package mc.monacotelecom.tecrep.equipments.integration;

import mc.monacotelecom.tecrep.equipments.dto.ProviderDTO;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sql/clean.sql", "/sql/provider_data.sql"})
class ProviderIntegrationV1Test extends BaseIntegrationTest {

    final String baseUrl = "/private/auth/providers";

    @Test
    void getById_success() throws Exception {
        final long id = 1L;
        mockMvc.perform(get(baseUrl + "/{providerId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"providerId\":1,\"name\":\"Technicolor\",\"accessType\":\"DOCSIS\"}\n"));
    }

    @Test
    void getById_notFound() throws Exception {
        final long id = 10L;
        mockMvc.perform(get(baseUrl + "/{providerId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"Provider with ID '10' not found\"}"));
    }

    @Test
    void getAll_success() throws Exception {

        mockMvc.perform(get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements").value(5))
                .andExpect(jsonPath("$._embedded.providers[0].providerId").value(1))
                .andExpect(jsonPath("$._embedded.providers[0].name").value("Technicolor"))
                .andExpect(jsonPath("$._embedded.providers[0].accessType").value("DOCSIS"));
    }

    @Test
    void deleteById_success() throws Exception {
        final long id = 5L;
        mockMvc.perform(delete(baseUrl + "/{providerId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteById_notFound() throws Exception {
        final long id = 10L;
        mockMvc.perform(delete(baseUrl + "/{providerId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void search_success() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("name", "Technicolor");

        mockMvc.perform(get(baseUrl + "/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(map))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"_embedded\":{\"providers\":[{\"providerId\":1,\"name\":\"Technicolor\",\"accessType\":\"DOCSIS\"}]},\"_links\":{\"self\":{\"href\":\"http://localhost/providers\"}},\"page\":{\"size\":20,\"totalElements\":1,\"totalPages\":1,\"number\":0}}\n"));
    }

    @Test
    void update_success() throws Exception {
        final long id = 1L;
        ProviderDTO dto = new ProviderDTO();
        dto.setName("lorem");
        dto.setAccessType(AccessType.FTTH);
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(baseUrl + "/{providerId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void update_notFound() throws Exception {
        final long id = 10L;
        mockMvc.perform(get(baseUrl + "/{providerId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"Provider with ID '10' not found\"}"));
    }

    @Test
    void add_success() throws Exception {

        ProviderDTO dto = new ProviderDTO();
        dto.setName("lorem");
        dto.setAccessType(AccessType.FTTH);
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"providerId\":6,\"name\":\"lorem\",\"accessType\":\"FTTH\"}\n"));
    }

    @Test
    void add_alreadyExisting() throws Exception {

        ProviderDTO dto = new ProviderDTO();
        dto.setName("Technicolor");
        dto.setAccessType(AccessType.FTTH);
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"Provider named 'Technicolor' already exists\"}"));
    }
}
