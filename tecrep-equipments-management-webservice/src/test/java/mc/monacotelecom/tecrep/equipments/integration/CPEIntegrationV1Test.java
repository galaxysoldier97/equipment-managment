package mc.monacotelecom.tecrep.equipments.integration;

import mc.monacotelecom.tecrep.equipments.dto.EquipmentModelDTO;
import mc.monacotelecom.tecrep.equipments.dto.ProviderDTO;
import mc.monacotelecom.tecrep.equipments.dto.WarehouseDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.ChangeStatusDto;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddCPEDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchCpeDto;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateCPEDTO;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sql/clean.sql", "/sql/cpe_data.sql"})
class CPEIntegrationV1Test extends BaseIntegrationTest {

    final String baseUrl = "/private/auth/cpes";

    @Test
    void getById_success() throws Exception {
        final long id = 1L;

        mockMvc.perform(get(baseUrl + "/{cpeId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{    \"equipmentId\" : 1,    \"orderId\" : \"1\",    \"serialNumber\" : \"8937710330000000007\",    \"externalNumber\" : null,    \"accessType\" : \"DOCSIS\",    \"activity\" : null,    \"activationDate\" : null,    \"assignmentDate\" : null,    \"status\" : \"BOOKED\",    \"nature\" : \"MAIN\",    \"recyclable\" : false,    \"preactivated\" : false,    \"batchNumber\" : null,    \"provider\" : {      \"providerId\" : 1,      \"name\" : \"Toto\",      \"accessType\" : \"DOCSIS\"    },    \"warehouse\" : {      \"warehouseId\" : 2,      \"name\" : \"TATA\",      \"resellerCode\" : \"TATA\"    },    \"category\" : \"CPE\",    \"events\" : [ \"free\", \"assign\", \"activate\", \"deactivate\" ],    \"macAddressLan\" : \"00:0A:95:9D:68:20\",    \"macAddressCpe\" : \"00:0A:95:9D:68:14\",    \"macAddressRouter\" : \"00:0A:95:9D:68:20\",    \"macAddressVoip\" : \"00:0A:95:9D:68:34\",    \"macAddress4G\" : \"00:0A:95:9D:68:45\",    \"macAddress5G\" : \"00:0A:95:9D:68:70\",    \"chipsetId\" : null,    \"modelName\" : \"Toti\",    \"hwVersion\" : null,    \"wpaKey\" : null,    \"serviceId\" : null,    \"numberRecycles\" : 0,    \"model\" : {      \"id\" : 2,      \"name\" : \"Toti\",      \"currentFirmware\" : \" Toto-1\",      \"provider\" : {        \"providerId\" : 1,        \"name\" : \"Toto\",        \"accessType\" : \"DOCSIS\"      },      \"accessType\" : \"FTTH\",      \"category\" : \"CPE\"    }  }"));
    }

    @Test
    void getById_notFound() throws Exception {
        final long id = 100L;

        mockMvc.perform(get(baseUrl + "/{cpeId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getEquipmentIdsIn_success() throws Exception {

        List<Long> ids = List.of(1L, 2L, 3L);

        mockMvc.perform(post(baseUrl + "/equipmentIdsIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[{\"equipmentId\":\"1\",\"serialNumber\":\"8937710330000000007\",\"macAddressCpe\":\"00:0A:95:9D:68:14\"},{\"equipmentId\":\"2\",\"serialNumber\":\"8937710330000000008\",\"macAddressCpe\":\"00:0A:95:9D:68:15\"},{\"equipmentId\":\"3\",\"serialNumber\":\"8937710330000000009\",\"macAddressCpe\":\"00:0A:95:9D:68:16\"}],\"pageable\":\"INSTANCE\",\"last\":true,\"totalPages\":1,\"totalElements\":3,\"size\":3,\"number\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"numberOfElements\":3,\"first\":true,\"empty\":false}\n"));
    }

    @Test
    void getEquipmentIdsIn_notFound() throws Exception {

        List<Long> ids = List.of(99L, 100L, 101L);

        mockMvc.perform(post(baseUrl + "/equipmentIdsIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ids.toString()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAll_success() throws Exception {

        mockMvc.perform(get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").value(20))
                .andExpect(jsonPath("$.page.totalElements").value(5))
                .andExpect(jsonPath("$._embedded.cpes[0].equipmentId").value(1))
                .andExpect(jsonPath("$._embedded.cpes[0].serialNumber").value("8937710330000000007"))
                .andExpect(jsonPath("$._embedded.cpes[0].accessType").value("DOCSIS"))
                .andExpect(jsonPath("$._embedded.cpes[0].status").value("BOOKED"))
                .andExpect(jsonPath("$._embedded.cpes[0].macAddressLan").value("00:0A:95:9D:68:20"))
                .andExpect(jsonPath("$._embedded.cpes[0].macAddressCpe").value("00:0A:95:9D:68:14"))
                .andExpect(jsonPath("$._embedded.cpes[0].macAddressRouter").value("00:0A:95:9D:68:20"));
    }

    @Test
    void delete_success() throws Exception {
        final long id = 4L;

        mockMvc.perform(delete(baseUrl + "/{cpeId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_notFound() throws Exception {
        final long id = 400L;

        mockMvc.perform(delete(baseUrl + "/{cpeId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("CPE with ID '400' could not be found"));
    }

    @Test
    void delete_incompatibleStatus() throws Exception {
        final long id = 1L;

        mockMvc.perform(delete(baseUrl + "/{cpeId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EqmValidationException))
                .andExpect(result -> assertEquals("CPE with serial number '8937710330000000007' cannot be deleted because it is 'BOOKED', it should be INSTORE, DEACTIVATED or DEPRECATED", result.getResolvedException().getMessage()));
    }

    @Test
    void search_success() throws Exception {
        SearchCpeDto searchDto = new SearchCpeDto();
        searchDto.setSerialNumber("8937710330000000007");

        final String requestJson = objectMapper.writeValueAsString(searchDto);

        mockMvc.perform(get(baseUrl + "/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").value(20))
                .andExpect(jsonPath("$.page.totalElements").value(5))
                .andExpect(jsonPath("$._embedded.cpes[0].equipmentId").value(1))
                .andExpect(jsonPath("$._embedded.cpes[0].orderId").value(1))
                .andExpect(jsonPath("$._embedded.cpes[0].serialNumber").value("8937710330000000007"))
                .andExpect(jsonPath("$._embedded.cpes[0].status").value("BOOKED"));
    }

    @Test
    void add_success_oldModelName() throws Exception {
        ProviderDTO providerDTO = new ProviderDTO();
        providerDTO.setProviderId(1L);
        WarehouseDTO warehouseDTO = new WarehouseDTO();
        warehouseDTO.setWarehouseId(2L);
        var dto = new AddCPEDTO();
        dto.setSerialNumber("GFAB02600017");
        dto.setProvider(providerDTO);
        dto.setWarehouse(warehouseDTO);
        dto.setModelName("Toti");

        final String requestJson = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{    \"equipmentId\" : 7,    \"orderId\" : null,    \"serialNumber\" : \"GFAB02600017\",    \"externalNumber\" : null,    \"accessType\" : null,    \"activity\" : null,    \"activationDate\" : null,    \"assignmentDate\" : null,    \"status\" : \"INSTORE\",    \"nature\" : \"MAIN\",    \"recyclable\" : true,    \"preactivated\" : null,    \"batchNumber\" : null,    \"provider\" : {      \"providerId\" : 1,      \"name\" : \"Toto\",      \"accessType\" : \"DOCSIS\"    },    \"warehouse\" : {      \"warehouseId\" : 2,      \"name\" : \"TATA\",      \"resellerCode\" : \"TATA\"    },    \"category\" : \"CPE\",    \"events\" : null,    \"macAddressLan\" : null,    \"macAddressCpe\" : null,    \"macAddressRouter\" : null,    \"macAddressVoip\" : null,    \"macAddress4G\" : null,    \"macAddress5G\" : null,    \"chipsetId\" : null,    \"modelName\" : \"Toti\",    \"hwVersion\" : null,    \"wpaKey\" : null,    \"serviceId\" : null,    \"numberRecycles\" : 0,    \"model\" : {      \"id\" : 2,      \"name\" : \"Toti\",      \"currentFirmware\" : \" Toto-1\",      \"provider\" : {        \"providerId\" : 1,        \"name\" : \"Toto\",        \"accessType\" : \"DOCSIS\"      },      \"accessType\" : \"FTTH\",      \"category\" : \"CPE\"    }  }"));
    }

    @Test
    void add_success() throws Exception {
        ProviderDTO providerDTO = new ProviderDTO();
        providerDTO.setProviderId(1L);
        WarehouseDTO warehouseDTO = new WarehouseDTO();
        warehouseDTO.setWarehouseId(2L);
        var dto = new AddCPEDTO();
        dto.setSerialNumber("GFAB02600017");
        dto.setProvider(providerDTO);
        dto.setWarehouse(warehouseDTO);
        var equipmentModelDto = new EquipmentModelDTO();
        equipmentModelDto.setName("Toti");
        dto.setModel(equipmentModelDto);

        final String requestJson = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{    \"equipmentId\" : 7,    \"orderId\" : null,    \"serialNumber\" : \"GFAB02600017\",    \"externalNumber\" : null,    \"accessType\" : null,    \"activity\" : null,    \"activationDate\" : null,    \"assignmentDate\" : null,    \"status\" : \"INSTORE\",    \"nature\" : \"MAIN\",    \"recyclable\" : true,    \"preactivated\" : null,    \"batchNumber\" : null,    \"provider\" : {      \"providerId\" : 1,      \"name\" : \"Toto\",      \"accessType\" : \"DOCSIS\"    },    \"warehouse\" : {      \"warehouseId\" : 2,      \"name\" : \"TATA\",      \"resellerCode\" : \"TATA\"    },    \"category\" : \"CPE\",    \"events\" : null,    \"macAddressLan\" : null,    \"macAddressCpe\" : null,    \"macAddressRouter\" : null,    \"macAddressVoip\" : null,    \"macAddress4G\" : null,    \"macAddress5G\" : null,    \"chipsetId\" : null,    \"modelName\" : \"Toti\",    \"hwVersion\" : null,    \"wpaKey\" : null,    \"serviceId\" : null,    \"numberRecycles\" : 0,    \"model\" : {      \"id\" : 2,      \"name\" : \"Toti\",      \"currentFirmware\" : \" Toto-1\",      \"provider\" : {        \"providerId\" : 1,        \"name\" : \"Toto\",        \"accessType\" : \"DOCSIS\"      },      \"accessType\" : \"FTTH\",      \"category\" : \"CPE\"    }  }"));
    }

    @Test
    void put_success() throws Exception {
        final long id = 3L;

        UpdateCPEDTO dto = new UpdateCPEDTO();
        dto.setMacAddressRouter("00:0A:95:9D:68:18");
        dto.setMacAddressVoip("00:0A:95:9D:68:36");
        dto.setMacAddressCpe("00:0A:95:9D:68:30");

        final String requestJson = objectMapper.writeValueAsString(dto);
        mockMvc.perform(put(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{    \"equipmentId\" : 3,    \"orderId\" : null,    \"serialNumber\" : \"8937710330000000009\",    \"externalNumber\" : null,    \"accessType\" : \"FTTH\",    \"activity\" : null,    \"activationDate\" : null,    \"assignmentDate\" : \"2021-01-18T08:45:58\",    \"status\" : \"ASSIGNED\",    \"nature\" : \"MAIN\",    \"recyclable\" : false,    \"preactivated\" : false,    \"batchNumber\" : null,    \"provider\" : {      \"providerId\" : 1,      \"name\" : \"Toto\",      \"accessType\" : \"DOCSIS\"    },    \"warehouse\" : null,    \"category\" : \"CPE\",    \"events\" : null,    \"macAddressLan\" : null,    \"macAddressCpe\" : \"00:0A:95:9D:68:30\",    \"macAddressRouter\" : \"00:0A:95:9D:68:18\",    \"macAddressVoip\" : \"00:0A:95:9D:68:36\",    \"macAddress4G\" : null,    \"macAddress5G\" : null,    \"chipsetId\" : null,    \"modelName\" : \"Toti\",    \"hwVersion\" : null,    \"wpaKey\" : null,    \"serviceId\" : 2,    \"numberRecycles\" : 0,    \"model\" : {      \"id\" : 2,      \"name\" : \"Toti\",      \"currentFirmware\" : \" Toto-1\",      \"provider\" : {        \"providerId\" : 1,        \"name\" : \"Toto\",        \"accessType\" : \"DOCSIS\"      },      \"accessType\" : \"FTTH\",      \"category\" : \"CPE\"    }  }"));
    }

    @Test
    void put_notFound() throws Exception {
        final long id = 8L;

        UpdateCPEDTO dto = new UpdateCPEDTO();
        dto.setMacAddressRouter("00:0A:95:9D:68:18");
        dto.setMacAddressVoip("00:0A:95:9D:68:36");
        dto.setMacAddressCpe("00:0A:95:9D:68:30");

        final String requestJson = objectMapper.writeValueAsString(dto);
        mockMvc.perform(put(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EqmValidationException))
                .andExpect(result -> assertEquals("CPE with ID '8' could not be found", result.getResolvedException().getMessage()));
    }

    @Test
    void patch_success() throws Exception {
        final long id = 3L;

        UpdateCPEDTO dto = new UpdateCPEDTO();
        dto.setMacAddressRouter("00:0A:95:9D:68:18");
        dto.setMacAddressVoip("00:0A:95:9D:68:36");
        dto.setMacAddressCpe("00:0A:95:9D:68:30");

        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{    \"equipmentId\" : 3,    \"orderId\" : null,    \"serialNumber\" : \"8937710330000000009\",    \"externalNumber\" : \"37799900001\",    \"accessType\" : \"FTTH\",    \"activity\" : null,    \"activationDate\" : null,    \"assignmentDate\" : \"2021-01-18T08:45:58\",    \"status\" : \"ASSIGNED\",    \"nature\" : \"MAIN\",    \"recyclable\" : false,    \"preactivated\" : false,    \"batchNumber\" : \"1\",    \"provider\" : {      \"providerId\" : 1,      \"name\" : \"Toto\",      \"accessType\" : \"DOCSIS\"    },    \"warehouse\" : {      \"warehouseId\" : 1,      \"name\" : \"TOTO\",      \"resellerCode\" : \"TOTO\"    },    \"category\" : \"CPE\",    \"events\" : null,    \"macAddressLan\" : \"00:0A:95:9D:68:22\",    \"macAddressCpe\" : \"00:0A:95:9D:68:30\",    \"macAddressRouter\" : \"00:0A:95:9D:68:18\",    \"macAddressVoip\" : \"00:0A:95:9D:68:36\",    \"macAddress4G\" : \"00:0A:95:9D:68:47\",    \"macAddress5G\" : \"00:0A:95:9D:68:72\",    \"chipsetId\" : null,    \"modelName\" : \"Toti\",    \"hwVersion\" : null,    \"wpaKey\" : null,    \"serviceId\" : 2,    \"numberRecycles\" : 0,    \"model\" : {      \"id\" : 2,      \"name\" : \"Toti\",      \"currentFirmware\" : \" Toto-1\",      \"provider\" : {        \"providerId\" : 1,        \"name\" : \"Toto\",        \"accessType\" : \"DOCSIS\"      },      \"accessType\" : \"FTTH\",      \"category\" : \"CPE\"    }  }"));
    }

    @Test
    void setEventOnCpe_selfEvent_success() throws Exception {
        final long id = 3L;

        ChangeStatusDto dto = new ChangeStatusDto();
        dto.setServiceId(2L);

        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch(baseUrl + "/{cpeId}/{event}", id, Event.assign)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{    \"equipmentId\" : 3,    \"orderId\" : null,    \"serialNumber\" : \"8937710330000000009\",    \"externalNumber\" : \"37799900001\",    \"accessType\" : \"FTTH\",    \"activity\" : null,    \"activationDate\" : null,    \"assignmentDate\" : \"2020-05-31T20:35:24\",    \"status\" : \"ASSIGNED\",    \"nature\" : \"MAIN\",    \"recyclable\" : false,    \"preactivated\" : false,    \"batchNumber\" : \"1\",    \"provider\" : {      \"providerId\" : 1,      \"name\" : \"Toto\",      \"accessType\" : \"DOCSIS\"    },    \"warehouse\" : {      \"warehouseId\" : 1,      \"name\" : \"TOTO\",      \"resellerCode\" : \"TOTO\"    },    \"category\" : \"CPE\",    \"events\" : null,    \"macAddressLan\" : \"00:0A:95:9D:68:22\",    \"macAddressCpe\" : \"00:0A:95:9D:68:16\",    \"macAddressRouter\" : \"00:0A:95:9D:68:22\",    \"macAddressVoip\" : \"00:0A:95:9D:68:36\",    \"macAddress4G\" : \"00:0A:95:9D:68:47\",    \"macAddress5G\" : \"00:0A:95:9D:68:72\",    \"chipsetId\" : null,    \"modelName\" : \"Toti\",    \"hwVersion\" : null,    \"wpaKey\" : null,    \"serviceId\" : 2,    \"numberRecycles\" : 0,    \"model\" : {      \"id\" : 2,      \"name\" : \"Toti\",      \"currentFirmware\" : \" Toto-1\",      \"provider\" : {        \"providerId\" : 1,        \"name\" : \"Toto\",        \"accessType\" : \"DOCSIS\"      },      \"accessType\" : \"FTTH\",      \"category\" : \"CPE\"    }  }"));
    }

    @Test
    void setEventOnCpe_notValid() throws Exception {
        final long id = 4L;

        ChangeStatusDto dto = new ChangeStatusDto();

        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch(baseUrl + "/{cpeId}/{event}", id, Event.assign)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EqmValidationException))
                .andExpect(result -> assertEquals("Cannot apply event 'assign' on CPE with status 'DEPRECATED' and Serial Number '8937710330000000010'", result.getResolvedException().getMessage()));
    }

    @Test
    void seEventOnCpe_numberCycles_increment() throws Exception {
        final long id = 5L;

        ChangeStatusDto dto = new ChangeStatusDto();
        dto.setServiceId(1L);

        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch(baseUrl + "/{cpeId}/{event}", id, Event.available)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{    \"equipmentId\" : 5,    \"orderId\" : \"1\",    \"serialNumber\" : \"8937710330000000011\",    \"externalNumber\" : null,    \"accessType\" : \"DOCSIS\",    \"activity\" : null,    \"activationDate\" : null,    \"assignmentDate\" : null,    \"status\" : \"AVAILABLE\",    \"nature\" : \"ADDITIONAL\",    \"recyclable\" : false,    \"preactivated\" : false,    \"batchNumber\" : \"1\",    \"provider\" : {      \"providerId\" : 1,      \"name\" : \"Toto\",      \"accessType\" : \"DOCSIS\"    },    \"warehouse\" : {      \"warehouseId\" : 1,      \"name\" : \"TOTO\",      \"resellerCode\" : \"TOTO\"    },    \"category\" : \"CPE\",    \"events\" : null,    \"macAddressLan\" : \"00:0A:95:9D:68:24\",    \"macAddressCpe\" : \"00:0A:95:9D:68:18\",    \"macAddressRouter\" : \"00:0A:95:9D:68:24\",    \"macAddressVoip\" : \"00:0A:95:9D:68:38\",    \"macAddress4G\" : \"00:0A:95:9D:68:49\",    \"macAddress5G\" : \"00:0A:95:9D:68:74\",    \"chipsetId\" : null,    \"modelName\" : \"Toti\",    \"hwVersion\" : null,    \"wpaKey\" : null,    \"serviceId\" : null,    \"numberRecycles\" : 1,    \"model\" : {      \"id\" : 2,      \"name\" : \"Toti\",      \"currentFirmware\" : \" Toto-1\",      \"provider\" : {        \"providerId\" : 1,        \"name\" : \"Toto\",        \"accessType\" : \"DOCSIS\"      },      \"accessType\" : \"FTTH\",      \"category\" : \"CPE\"    }  }"));
    }

    @Test
    void setEventOnCpe_success() throws Exception {
        final long id = 1L;

        ChangeStatusDto dto = new ChangeStatusDto();
        dto.setServiceId(1L);

        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch(baseUrl + "/{cpeId}/{event}", id, Event.assign)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{    \"equipmentId\" : 1,    \"orderId\" : null,    \"serialNumber\" : \"8937710330000000007\",    \"externalNumber\" : null,    \"accessType\" : \"DOCSIS\",    \"activity\" : null,    \"activationDate\" : null,    \"assignmentDate\" : \"2020-05-31T20:35:24\",    \"status\" : \"ASSIGNED\",    \"nature\" : \"MAIN\",    \"recyclable\" : false,    \"preactivated\" : false,    \"batchNumber\" : null,    \"provider\" : {      \"providerId\" : 1,      \"name\" : \"Toto\",      \"accessType\" : \"DOCSIS\"    },    \"warehouse\" : {      \"warehouseId\" : 2,      \"name\" : \"TATA\",      \"resellerCode\" : \"TATA\"    },    \"category\" : \"CPE\",    \"events\" : null,    \"macAddressLan\" : \"00:0A:95:9D:68:20\",    \"macAddressCpe\" : \"00:0A:95:9D:68:14\",    \"macAddressRouter\" : \"00:0A:95:9D:68:20\",    \"macAddressVoip\" : \"00:0A:95:9D:68:34\",    \"macAddress4G\" : \"00:0A:95:9D:68:45\",    \"macAddress5G\" : \"00:0A:95:9D:68:70\",    \"chipsetId\" : null,    \"modelName\" : \"Toti\",    \"hwVersion\" : null,    \"wpaKey\" : null,    \"serviceId\" : 1,    \"numberRecycles\" : 0,    \"model\" : {      \"id\" : 2,      \"name\" : \"Toti\",      \"currentFirmware\" : \" Toto-1\",      \"provider\" : {        \"providerId\" : 1,        \"name\" : \"Toto\",        \"accessType\" : \"DOCSIS\"      },      \"accessType\" : \"FTTH\",      \"category\" : \"CPE\"    }  }"));
    }

    @Test
    void setEventOnCpe_activate_success() throws Exception {
        final long id = 3L;

        ChangeStatusDto dto = new ChangeStatusDto();
        dto.setServiceId(2L);

        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch(baseUrl + "/{cpeId}/{event}", id, Event.activate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{    \"equipmentId\" : 3,    \"orderId\" : null,    \"serialNumber\" : \"8937710330000000009\",    \"externalNumber\" : \"37799900001\",    \"accessType\" : \"FTTH\",    \"activity\" : null,    \"activationDate\" : \"2020-05-31T20:35:24\",    \"assignmentDate\" : \"2021-01-18T08:45:58\",    \"status\" : \"ACTIVATED\",    \"nature\" : \"MAIN\",    \"recyclable\" : false,    \"preactivated\" : false,    \"batchNumber\" : \"1\",    \"provider\" : {      \"providerId\" : 1,      \"name\" : \"Toto\",      \"accessType\" : \"DOCSIS\"    },    \"warehouse\" : {      \"warehouseId\" : 1,      \"name\" : \"TOTO\",      \"resellerCode\" : \"TOTO\"    },    \"category\" : \"CPE\",    \"events\" : null,    \"macAddressLan\" : \"00:0A:95:9D:68:22\",    \"macAddressCpe\" : \"00:0A:95:9D:68:16\",    \"macAddressRouter\" : \"00:0A:95:9D:68:22\",    \"macAddressVoip\" : \"00:0A:95:9D:68:36\",    \"macAddress4G\" : \"00:0A:95:9D:68:47\",    \"macAddress5G\" : \"00:0A:95:9D:68:72\",    \"chipsetId\" : null,    \"modelName\" : \"Toti\",    \"hwVersion\" : null,    \"wpaKey\" : null,    \"serviceId\" : 2,    \"numberRecycles\" : 0,    \"model\" : {      \"id\" : 2,      \"name\" : \"Toti\",      \"currentFirmware\" : \" Toto-1\",      \"provider\" : {        \"providerId\" : 1,        \"name\" : \"Toto\",        \"accessType\" : \"DOCSIS\"      },      \"accessType\" : \"FTTH\",      \"category\" : \"CPE\"    }  }"));
    }

    @Test
    void setEventOnCpe_notBody_success() throws Exception {
        final long id = 3L;

        mockMvc.perform(patch(baseUrl + "/{cpeId}/{event}", id, Event.unassign)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{    \"equipmentId\" : 3,    \"orderId\" : null,    \"serialNumber\" : \"8937710330000000009\",    \"externalNumber\" : \"37799900001\",    \"accessType\" : \"FTTH\",    \"activity\" : null,    \"activationDate\" : null,    \"assignmentDate\" : null,    \"status\" : \"AVAILABLE\",    \"nature\" : \"MAIN\",    \"recyclable\" : false,    \"preactivated\" : false,    \"batchNumber\" : \"1\",    \"provider\" : {      \"providerId\" : 1,      \"name\" : \"Toto\",      \"accessType\" : \"DOCSIS\"    },    \"warehouse\" : {      \"warehouseId\" : 1,      \"name\" : \"TOTO\",      \"resellerCode\" : \"TOTO\"    },    \"category\" : \"CPE\",    \"events\" : null,    \"macAddressLan\" : \"00:0A:95:9D:68:22\",    \"macAddressCpe\" : \"00:0A:95:9D:68:16\",    \"macAddressRouter\" : \"00:0A:95:9D:68:22\",    \"macAddressVoip\" : \"00:0A:95:9D:68:36\",    \"macAddress4G\" : \"00:0A:95:9D:68:47\",    \"macAddress5G\" : \"00:0A:95:9D:68:72\",    \"chipsetId\" : null,    \"modelName\" : \"Toti\",    \"hwVersion\" : null,    \"wpaKey\" : null,    \"serviceId\" : null,    \"numberRecycles\" : 0,    \"model\" : {      \"id\" : 2,      \"name\" : \"Toti\",      \"currentFirmware\" : \" Toto-1\",      \"provider\" : {        \"providerId\" : 1,        \"name\" : \"Toto\",        \"accessType\" : \"DOCSIS\"      },      \"accessType\" : \"FTTH\",      \"category\" : \"CPE\"    }  }"));
    }

    @Test
    void setEventOnCpe_notFound() throws Exception {
        final long id = 8L;

        ChangeStatusDto dto = new ChangeStatusDto();
        dto.setServiceId(1L);

        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch(baseUrl + "/{cpeId}/{event}", id, Event.assign)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\": \"CPE with ID '8' could not be found\"}"));
    }

    @Test
    void setEventOnCpe_unassign() throws Exception {
        final long id = 3L;

        ChangeStatusDto dto = new ChangeStatusDto();

        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch(baseUrl + "/{cpeId}/{event}", id, Event.unassign)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{    \"equipmentId\" : 3,    \"orderId\" : null,    \"serialNumber\" : \"8937710330000000009\",    \"externalNumber\" : \"37799900001\",    \"accessType\" : \"FTTH\",    \"activity\" : null,    \"activationDate\" : null,    \"assignmentDate\" : null,    \"status\" : \"AVAILABLE\",    \"nature\" : \"MAIN\",    \"recyclable\" : false,    \"preactivated\" : false,    \"batchNumber\" : \"1\",    \"provider\" : {      \"providerId\" : 1,      \"name\" : \"Toto\",      \"accessType\" : \"DOCSIS\"    },    \"warehouse\" : {      \"warehouseId\" : 1,      \"name\" : \"TOTO\",      \"resellerCode\" : \"TOTO\"    },    \"category\" : \"CPE\",    \"events\" : null,    \"macAddressLan\" : \"00:0A:95:9D:68:22\",    \"macAddressCpe\" : \"00:0A:95:9D:68:16\",    \"macAddressRouter\" : \"00:0A:95:9D:68:22\",    \"macAddressVoip\" : \"00:0A:95:9D:68:36\",    \"macAddress4G\" : \"00:0A:95:9D:68:47\",    \"macAddress5G\" : \"00:0A:95:9D:68:72\",    \"chipsetId\" : null,    \"modelName\" : \"Toti\",    \"hwVersion\" : null,    \"wpaKey\" : null,    \"serviceId\" : null,    \"numberRecycles\" : 0,    \"model\" : {      \"id\" : 2,      \"name\" : \"Toti\",      \"currentFirmware\" : \" Toto-1\",      \"provider\" : {        \"providerId\" : 1,        \"name\" : \"Toto\",        \"accessType\" : \"DOCSIS\"      },      \"accessType\" : \"FTTH\",      \"category\" : \"CPE\"    }  }"));
    }

    @Test
    void getRevisions_success() throws Exception {
        mockMvc.perform(get(baseUrl + "/1/revisions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"_embedded\" : {\"revisions\" : [ {  \"date\" : \"2020-02-25T10:52:37\",  \"entity\" : { \"equipmentId\" : 1, \"orderId\" : \"1\", \"serialNumber\" : \"8937710330000000007\", \"externalNumber\" : null, \"accessType\" : \"DOCSIS\", \"activity\" : null, \"activationDate\" : null, \"assignmentDate\" : null, \"status\" : \"BOOKED\", \"nature\" : \"MAIN\", \"recyclable\" : false, \"preactivated\" : false, \"batchNumber\" : null, \"provider\" : null, \"warehouse\" : {\"warehouseId\" : 1,\"name\" : \"TOTO\",\"resellerCode\" : \"TOTO\" }, \"category\" : \"CPE\", \"events\" : null, \"macAddressLan\" : \"00:0A:95:9D:68:20\", \"macAddressCpe\" : \"00:0A:95:9D:68:14\", \"macAddressRouter\" : \"00:0A:95:9D:68:20\", \"macAddressVoip\" : \"00:0A:95:9D:68:34\", \"macAddress4G\" : \"00:0A:95:9D:68:45\", \"macAddress5G\" : \"00:0A:95:9D:68:70\", \"chipsetId\" : null, \"modelName\" : null, \"hwVersion\" : null, \"wpaKey\" : null, \"serviceId\" : null, \"numberRecycles\" : null, \"model\" : null  },  \"author\" : \"g.,fantappie\"} ] }, \"_links\" : {\"self\" : {  \"href\" : \"http://localhost\"} }, \"page\" : {\"size\" : 20,\"totalElements\" : 1,\"totalPages\" : 1,\"number\" : 0 }  }"));
    }

    @Test
    void getRevisions_missing() throws Exception {
        mockMvc.perform(get(baseUrl + "/99/revisions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{   \"_links\" : {     \"self\" : {       \"href\" : \"http://localhost\"     }   },   \"page\" : {     \"size\" : 20,     \"totalElements\" : 0,     \"totalPages\" : 0,     \"number\" : 0   } }"));
    }
}
