package mc.monacotelecom.tecrep.equipments.integration;

import mc.monacotelecom.tecrep.equipments.dto.request.EquipmentModelCreateDTO;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sql/clean.sql", "/sql/equipmentModel_data.sql"})
class EquipmentModelIntegrationV2Test extends BaseIntegrationTest {

    final String baseUrl = "/api/v2/private/auth/equipmentModels";

    @Test
    void search_success() throws Exception {
        mockMvc.perform(get(baseUrl + "?page=0&size=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("name-1"))
                .andExpect(jsonPath("$.content[0].currentFirmware").value(" test-1"))
                .andExpect(jsonPath("$.content[0].provider.id").value(1))
                .andExpect(jsonPath("$.content[0].provider.name").value("Toto"))
                .andExpect(jsonPath("$.content[0].provider.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.content[0].accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.content[0].category").value("CPE"));
    }

    @Test
    void getById_success() throws Exception {
        final long id = 1L;
        mockMvc.perform(get(baseUrl + "/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name-1"))
                .andExpect(jsonPath("$.currentFirmware").value(" test-1"))
                .andExpect(jsonPath("$.provider.id").value(1))
                .andExpect(jsonPath("$.provider.name").value("Toto"))
                .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.category").value("CPE"));
    }

    @Test
    void getById_notFound() throws Exception {
        final long id = 100L;
        mockMvc.perform(get(baseUrl + "/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"Could not find an Equipment Model with id '100'\"}"));
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

        mockMvc.perform(put(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(equipmentModelCreateDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("created-model"))
                .andExpect(jsonPath("$.currentFirmware").value("test-2"))
                .andExpect(jsonPath("$.provider.id").value(1))
                .andExpect(jsonPath("$.provider.name").value("Toto"))
                .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.category").value("CPE"));
    }

    @Test
    void put_alreadyExists() throws Exception {
        final long id = 1L;
        EquipmentModelCreateDTO equipmentModelCreateDTO = new EquipmentModelCreateDTO();
        equipmentModelCreateDTO.setCurrentFirmware("created-model");
        equipmentModelCreateDTO.setCategory(EquipmentModelCategory.CPE);
        equipmentModelCreateDTO.setProviderId(1L);
        equipmentModelCreateDTO.setAccessType(AccessType.DOCSIS);
        equipmentModelCreateDTO.setName("name-2");

        mockMvc.perform(put(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(equipmentModelCreateDTO)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("Conflict"))
                .andExpect(jsonPath("$.errorMessage").value("An equipment model with name 'name-2' and category 'CPE' already exists"));
    }

    @Test
    void put_alreadyExistsButItself() throws Exception {
        final long id = 1L;
        EquipmentModelCreateDTO equipmentModelCreateDTO = new EquipmentModelCreateDTO();
        equipmentModelCreateDTO.setCurrentFirmware("created-model");
        equipmentModelCreateDTO.setCategory(EquipmentModelCategory.CPE);
        equipmentModelCreateDTO.setProviderId(1L);
        equipmentModelCreateDTO.setAccessType(AccessType.DOCSIS);
        equipmentModelCreateDTO.setName("name-1");

        mockMvc.perform(put(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(equipmentModelCreateDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("name-1"))
                .andExpect(jsonPath("$.currentFirmware").value("created-model"))
                .andExpect(jsonPath("$.provider.id").value(1))
                .andExpect(jsonPath("$.provider.name").value("Toto"))
                .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.category").value("CPE"));
    }

    @Test
    void put_notFound() throws Exception {
        final long id = 100L;
        EquipmentModelCreateDTO equipmentModelCreateDTO = new EquipmentModelCreateDTO();
        equipmentModelCreateDTO.setCurrentFirmware("test-2");

        mockMvc.perform(put(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(equipmentModelCreateDTO)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"Could not find an Equipment Model with id '100'\"}"));
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
    void delete_failureIfUsed() throws Exception {
        final long id = 2L;
        mockMvc.perform(delete(baseUrl + "/{id}", id))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("Conflict"))
                .andExpect(jsonPath("$.errorMessage").value("Equipment model with name 'name-2' and category 'CPE' cannot be deleted as it is still referenced by equipments"));
    }

    @Test
    void delete_notFound() throws Exception {
        final long id = 100L;
        mockMvc.perform(delete(baseUrl + "/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("Not Found"))
                .andExpect(jsonPath("$.errorMessage").value("Could not find an Equipment Model with id '100'"));
    }

    @Test
    void post_success() throws Exception {
        final EquipmentModelCreateDTO equipmentModelDTO = new EquipmentModelCreateDTO();
        equipmentModelDTO.setAccessType(AccessType.DOCSIS);
        equipmentModelDTO.setCategory(EquipmentModelCategory.CPE);
        equipmentModelDTO.setName("test");
        equipmentModelDTO.setProviderId(1L);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(equipmentModelDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.currentFirmware").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.provider.id").value(1))
                .andExpect(jsonPath("$.provider.name").value("Toto"))
                .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.category").value("CPE"));
    }

    @Test
    void post_alreadyExists() throws Exception {
        final EquipmentModelCreateDTO equipmentModelDTO = new EquipmentModelCreateDTO();
        equipmentModelDTO.setAccessType(AccessType.DOCSIS);
        equipmentModelDTO.setCategory(EquipmentModelCategory.CPE);
        equipmentModelDTO.setName("name-1");
        equipmentModelDTO.setProviderId(1L);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(equipmentModelDTO)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("Conflict"))
                .andExpect(jsonPath("$.errorMessage").value("An equipment model with name 'name-1' and category 'CPE' already exists"));
    }

    @Test
    void getNames_success() throws Exception {
        mockMvc.perform(get(baseUrl + "/names")
                        .param("category", "CPE")
                        .param("accessType", "DOCSIS"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("name-1"));
    }
}
