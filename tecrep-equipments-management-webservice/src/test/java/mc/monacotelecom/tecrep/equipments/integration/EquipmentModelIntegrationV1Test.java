package mc.monacotelecom.tecrep.equipments.integration;

import mc.monacotelecom.tecrep.equipments.dto.request.EquipmentModelCreateDTO;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sql/clean.sql", "/sql/equipmentModel_data.sql"})
class EquipmentModelIntegrationV1Test extends BaseIntegrationTest {

    final String baseUrl = "/private/auth/equipmentModels";

    @Test
    void getAll_success() throws Exception {
        mockMvc.perform(get(baseUrl + "?page=0&size=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[{\"id\":1,\"name\":\"name-1\",\"currentFirmware\":\" test-1\",\"provider\":{\"providerId\":1,\"name\":\"Toto\",\"accessType\":\"DOCSIS\"},\"accessType\":\"DOCSIS\",\"category\":\"CPE\"}],\"pageable\":{\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"pageSize\":1,\"pageNumber\":0,\"offset\":0,\"paged\":true,\"unpaged\":false},\"totalPages\":4,\"totalElements\":4,\"last\":false,\"number\":0,\"size\":1,\"numberOfElements\":1,\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"first\":true,\"empty\":false}"));
    }

    @Test
    void getById_success() throws Exception {
        final long id = 1L;
        mockMvc.perform(get(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"name-1\",\"currentFirmware\":\" test-1\",\"provider\":{\"providerId\":1,\"name\":\"Toto\",\"accessType\":\"DOCSIS\"},\"accessType\":\"DOCSIS\",\"category\":\"CPE\"}"));
    }

    @Test
    void getById_notFound() throws Exception {
        final long id = 100L;
        mockMvc.perform(get(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EqmNotFoundException))
                .andExpect(result -> assertEquals("Could not find an Equipment Model with id '100'", result.getResolvedException().getMessage()));
    }

    @Test
    void put_success() throws Exception {
        final long id = 1L;
        EquipmentModelCreateDTO equipmentModelCreateDTO = new EquipmentModelCreateDTO();
        equipmentModelCreateDTO.setCurrentFirmware("test-2");
        equipmentModelCreateDTO.setCategory(EquipmentModelCategory.CPE);
        equipmentModelCreateDTO.setProviderId(1L);
        equipmentModelCreateDTO.setAccessType(AccessType.DOCSIS);
        equipmentModelCreateDTO.setName("created-model");

        final String requestJson = objectMapper.writeValueAsString(equipmentModelCreateDTO);

        mockMvc.perform(put(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"created-model\",\"currentFirmware\":\"test-2\",\"provider\":{\"providerId\":1,\"name\":\"Toto\",\"accessType\":\"DOCSIS\"},\"accessType\":\"DOCSIS\",\"category\":\"CPE\"}"));
    }

    @Test
    void put_notFound() throws Exception {
        final long id = 100L;
        EquipmentModelCreateDTO equipmentModelCreateDTO = new EquipmentModelCreateDTO();
        equipmentModelCreateDTO.setCurrentFirmware("test-2");

        final String requestJson = objectMapper.writeValueAsString(equipmentModelCreateDTO);

        mockMvc.perform(put(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EqmNotFoundException))
                .andExpect(result -> assertEquals("Could not find an Equipment Model with id '100'", result.getResolvedException().getMessage()));
    }

    @Test
    void delete_success() throws Exception {
        final long id = 1L;
        mockMvc.perform(delete(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_notFound() throws Exception {
        final long id = 100L;
        mockMvc.perform(delete(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EqmNotFoundException))
                .andExpect(result -> assertEquals("Could not find an Equipment Model with id '100'", result.getResolvedException().getMessage()));
    }

    @Test
    void post_success() throws Exception {
        final EquipmentModelCreateDTO equipmentModelDTO = new EquipmentModelCreateDTO();
        equipmentModelDTO.setAccessType(AccessType.DOCSIS);
        equipmentModelDTO.setCategory(EquipmentModelCategory.CPE);
        equipmentModelDTO.setName("test");
        equipmentModelDTO.setProviderId(1L);

        final String requestJson = objectMapper.writeValueAsString(equipmentModelDTO);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":5,\"name\":\"test\",\"currentFirmware\":null,\"provider\":{\"providerId\":1,\"name\":\"Toto\",\"accessType\":\"DOCSIS\"},\"accessType\":\"DOCSIS\",\"category\":\"CPE\"}\n"));
    }
}
