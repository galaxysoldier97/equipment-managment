package mc.monacotelecom.tecrep.equipments.integration;

import mc.monacotelecom.tecrep.equipments.dto.v2.WarehouseDTOV2;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sql/clean.sql", "/sql/warehouse_data.sql"})
class WarehouseIntegrationV2Test extends BaseIntegrationTest {

    final String baseUrl = "/api/v2/private/auth/warehouses";

    @Test
    void getById_success() throws Exception {
        final long id = 1L;
        mockMvc.perform(get(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Monaco Telecom Entreprise"))
                .andExpect(jsonPath("$.resellerCode").value("MTENT"));
    }

    @Test
    void getById_notFound() throws Exception {
        final long id = 10L;
        mockMvc.perform(get(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"Warehouse with ID '10' not found\"}"));
    }

    @Test
    void deleteById_success() throws Exception {
        final long id = 4L;
        mockMvc.perform(delete(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteById_notFound() throws Exception {
        mockMvc.perform(delete(baseUrl + "/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"Warehouse with ID '99' not found\"}"));
    }

    @Test
    void search_success() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("name", "Mccom");

        mockMvc.perform(get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(map))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(4))
                .andExpect(jsonPath("$.content[0].name").value("Mccom"))
                .andExpect(jsonPath("$.content[0].resellerCode").value("MCCOM"));
    }

    @Test
    void update_success() throws Exception {
        final long id = 1L;
        var dto = new WarehouseDTOV2();
        dto.setName("Méditerranée Electro");
        dto.setResellerCode("MED");
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Méditerranée Electro"))
                .andExpect(jsonPath("$.resellerCode").value("MED"));
    }

    @Test
    void update_notFound() throws Exception {
        final long id = 10L;
        mockMvc.perform(get(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"Warehouse with ID '10' not found\"}"));
    }

    @Test
    void add_success() throws Exception {

        var dto = new WarehouseDTOV2();
        dto.setName("Méditerranée Electro");
        dto.setResellerCode("MED");
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("Méditerranée Electro"))
                .andExpect(jsonPath("$.resellerCode").value("MED"));
    }

    @Test
    void add_alreadyExisting() throws Exception {

        var dto = new WarehouseDTOV2();
        dto.setName("Monaco Telecom Entreprise");
        dto.setResellerCode("MTENT");
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"A warehouse with resellerCode 'MTENT' already exist\"}"));
    }
}
