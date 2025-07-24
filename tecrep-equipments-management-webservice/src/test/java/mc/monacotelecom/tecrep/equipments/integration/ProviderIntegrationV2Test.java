package mc.monacotelecom.tecrep.equipments.integration;

import mc.monacotelecom.tecrep.equipments.dto.v2.ProviderDTOV2;
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
class ProviderIntegrationV2Test extends BaseIntegrationTest {

    final String baseUrl = "/api/v2/private/auth/providers";

    @Test
    void getById_success() throws Exception {
        final long id = 1L;
        mockMvc.perform(get(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Technicolor"))
                .andExpect(jsonPath("$.accessType").value("DOCSIS"));
    }

    @Test
    void getById_notFound() throws Exception {
        final long id = 10L;
        mockMvc.perform(get(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"Provider with ID '10' not found\"}"));
    }

    @Test
    void deleteById_success() throws Exception {
        final long id = 5L;
        mockMvc.perform(delete(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
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

        map.add("name", "Technicolor");

        mockMvc.perform(get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(map))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Technicolor"))
                .andExpect(jsonPath("$.content[0].accessType").value("DOCSIS"));
    }

    @Test
    void update_success() throws Exception {
        final long id = 1L;
        var dto = new ProviderDTOV2();
        dto.setName("lorem");
        dto.setAccessType(AccessType.FTTH);
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void update_notFound() throws Exception {
        final long id = 10L;
        mockMvc.perform(get(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"Provider with ID '10' not found\"}"));
    }

    @Test
    void add_success() throws Exception {

        var dto = new ProviderDTOV2();
        dto.setName("lorem");
        dto.setAccessType(AccessType.FTTH);
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":6,\"name\":\"lorem\",\"accessType\":\"FTTH\"}"));
    }

    @Test
    void add_alreadyExisting() throws Exception {

        var dto = new ProviderDTOV2();
        dto.setName("Technicolor");
        dto.setAccessType(AccessType.FTTH);
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Provider named 'Technicolor' already exists\"}"));
    }
}
