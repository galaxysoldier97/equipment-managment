package mc.monacotelecom.tecrep.equipments.integration;

import mc.monacotelecom.tecrep.equipments.dto.WarehouseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sql/clean.sql", "/sql/warehouse_data.sql"})
class WarehouseIntegrationV1Test extends BaseIntegrationTest {

    final String baseUrl = "/private/auth/warehouses";

    @Test
    void getById_success() throws Exception {
        final long id = 1L;
        mockMvc.perform(get(baseUrl + "/{warehouseId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"warehouseId\":1,\"name\":\"Monaco Telecom Entreprise\",\"resellerCode\":\"MTENT\"}\n"));
    }

    @Test
    void getById_notFound() throws Exception {
        final long id = 10L;
        mockMvc.perform(get(baseUrl + "/{warehouseId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"Warehouse with ID '10' not found\"}"));
    }

    @Test
    void getAll_success() throws Exception {

        mockMvc.perform(get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements").value(4))
                .andExpect(jsonPath("$._embedded.warehouses[0].warehouseId").value(1))
                .andExpect(jsonPath("$._embedded.warehouses[0].name").value("Monaco Telecom Entreprise"))
                .andExpect(jsonPath("$._embedded.warehouses[0].resellerCode").value("MTENT"));
    }

    @Test
    void deleteById_success() throws Exception {
        final long id = 4L;
        mockMvc.perform(delete(baseUrl + "/{warehouseId}", id)
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
                .andExpect(content().json("{\"error\":\"Warehouse with ID '99' not found\"}"));
    }

    @Test
    void search_success() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("name", "SAV Résidentiel");

        mockMvc.perform(get(baseUrl + "/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(map))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void update_success() throws Exception {
        final long id = 1L;
        WarehouseDTO dto = new WarehouseDTO();
        dto.setName("Méditerranée Electro");
        dto.setResellerCode("MED");
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(baseUrl + "/{warehouseId}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"warehouseId\":1,\"name\":\"Méditerranée Electro\",\"resellerCode\":\"MED\"}\n"));
    }

    @Test
    void update_notFound() throws Exception {
        final long id = 10L;
        mockMvc.perform(get(baseUrl + "/{warehouseId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"Warehouse with ID '10' not found\"}"));
    }

    @Test
    void add_success() throws Exception {

        WarehouseDTO dto = new WarehouseDTO();
        dto.setName("Méditerranée Electro");
        dto.setResellerCode("MED");
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"warehouseId\":5,\"name\":\"Méditerranée Electro\",\"resellerCode\":\"MED\"}"));
    }

    @Test
    void add_alreadyExisting() throws Exception {

        WarehouseDTO dto = new WarehouseDTO();
        dto.setName("Monaco Telecom Entreprise");
        dto.setResellerCode("MTENT");
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"A warehouse with resellerCode 'MTENT' already exist\"}"));
    }
}
