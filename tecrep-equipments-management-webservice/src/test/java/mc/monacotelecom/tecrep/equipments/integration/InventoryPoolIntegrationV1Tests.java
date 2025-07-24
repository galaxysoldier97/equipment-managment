package mc.monacotelecom.tecrep.equipments.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sql/clean.sql", "/sql/inventory_pool_data.sql"})
class InventoryPoolIntegrationV1Tests extends BaseIntegrationTest {

    final String baseUrl = "/private/auth/inventorypools";

    @Test
    void search_success() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("code", "METEOR_PAIRED_CUSTOMER_POOL");
        mockMvc.perform(get(baseUrl + "?size=1&page=0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(map))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[{\"inventoryPoolId\":1,\"code\":\"METEOR_PAIRED_CUSTOMER_POOL\",\"description\":\"Meteor Paired Customer Pool\",\"mvno\":0,\"simProfile\":\"DEFAULT\"}],\"pageable\":{\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"pageSize\":1,\"pageNumber\":0,\"offset\":0,\"paged\":true,\"unpaged\":false},\"totalPages\":1,\"totalElements\":1,\"last\":true,\"number\":0,\"size\":1,\"numberOfElements\":1,\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"first\":true,\"empty\":false}"));
    }

    @Test
    void getByCode_success() throws Exception {
        mockMvc.perform(get(baseUrl + "/METEOR_PAIRED_CUSTOMER_POOL"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"inventoryPoolId\":1,\"code\":\"METEOR_PAIRED_CUSTOMER_POOL\",\"description\":\"Meteor Paired Customer Pool\",\"mvno\":0,\"simProfile\":\"DEFAULT\"}"));
    }

    @Test
    void getByCode_missing() throws Exception {
        mockMvc.perform(get(baseUrl + "/METEOR_PAIRED"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"Inventory Pool with code 'METEOR_PAIRED' not found\"}"));
    }

    @Test
    void delete_success() throws Exception {
        mockMvc.perform(delete(baseUrl + "/METEOR_PAIRED_CUSTOMER_POOL"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_missing() throws Exception {
        mockMvc.perform(delete(baseUrl + "/METEOR_PAIRED"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"Inventory Pool with code 'METEOR_PAIRED' not found\"}"));
    }

    @Test
    void delete_attachedToBatch() throws Exception {
        mockMvc.perform(delete(baseUrl + "/ATTACHED_TO_BATCH"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Inventory Pool with code 'ATTACHED_TO_BATCH' cannot be deleted because it is referenced by batch '61'\"}"));
    }

    @Test
    void delete_attachedToSimCard() throws Exception {
        mockMvc.perform(delete(baseUrl + "/ATTACHED_TO_SIM"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Inventory Pool with code 'ATTACHED_TO_SIM' cannot be deleted because it is referenced by SIM card with ICCID '893771033000000007'\"}"));
    }

    @Test
    void delete_attachedToAllotment() throws Exception {
        mockMvc.perform(delete(baseUrl + "/ATTACHED_TO_ALLOTMENT"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Inventory Pool with code 'ATTACHED_TO_ALLOTMENT' cannot be deleted because it is referenced by allotment '6'\"}"));
    }

    @Test
    void create_success() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"code\":\"ANOTHER\",\"description\":\"Meteor Paired Customer Pool\",\"mvno\":0,\"simProfile\":\"DEFAULT\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"inventoryPoolId\":5,\"code\":\"ANOTHER\",\"description\":\"Meteor Paired Customer Pool\",\"mvno\":0,\"simProfile\":\"DEFAULT\"}"));
    }

    @Test
    void create_codeAlreadyExists() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"code\":\"METEOR_PAIRED_CUSTOMER_POOL\",\"description\":\"Meteor Paired Customer Pool\",\"mvno\":0,\"simProfile\":\"DEFAULT\"}"))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().json("{\"errorMessage\":\"Inventory Pool with code 'METEOR_PAIRED_CUSTOMER_POOL' already exists\"}"));
    }

    @Test
    void update_success() throws Exception {
        mockMvc.perform(patch(baseUrl + "/METEOR_PAIRED_CUSTOMER_POOL")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"code\":\"UPDATED\",\"description\":\"Something else\",\"mvno\":4,\"simProfile\":\"REPLACEMENT\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"inventoryPoolId\":1,\"code\":\"UPDATED\",\"description\":\"Something else\",\"mvno\":4,\"simProfile\":\"REPLACEMENT\"}"));
    }

    @Test
    void update_missing() throws Exception {
        mockMvc.perform(patch(baseUrl + "/MISSING")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"code\":\"UPDATED\",\"description\":\"Something else\",\"mvno\":4,\"simProfile\":\"REPLACEMENT\"}"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"Inventory Pool with code 'MISSING' not found\"}"));
    }

    @Test
    void update_codeAlreadyExists() throws Exception {
        mockMvc.perform(patch(baseUrl + "/METEOR_PAIRED_CUSTOMER_POOL")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"code\":\"ATTACHED_TO_SIM\",\"description\":\"Something else\",\"mvno\":4,\"simProfile\":\"REPLACEMENT\"}"))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().json("{\"errorMessage\":\"Inventory Pool with code 'ATTACHED_TO_SIM' already exists\"}"));
    }

    @Test
    void update_sameCode() throws Exception {
        mockMvc.perform(patch(baseUrl + "/METEOR_PAIRED_CUSTOMER_POOL")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"code\":\"METEOR_PAIRED_CUSTOMER_POOL\",\"description\":\"Something else\",\"mvno\":4,\"simProfile\":\"REPLACEMENT\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"inventoryPoolId\":1,\"code\":\"METEOR_PAIRED_CUSTOMER_POOL\",\"description\":\"Something else\",\"mvno\":4,\"simProfile\":\"REPLACEMENT\"}"));
    }
}
