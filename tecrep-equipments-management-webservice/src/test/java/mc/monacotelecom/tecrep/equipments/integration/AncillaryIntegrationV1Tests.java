package mc.monacotelecom.tecrep.equipments.integration;

import mc.monacotelecom.tecrep.equipments.dto.EquipmentModelDTO;
import mc.monacotelecom.tecrep.equipments.dto.ProviderDTO;
import mc.monacotelecom.tecrep.equipments.dto.WarehouseDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.AncillaryChangeStateDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddAncillaryDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchAncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateAncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sql/clean.sql", "/sql/ancillary_data.sql"})
class AncillaryIntegrationV1Tests extends BaseIntegrationTest {

    final String baseUrl = "/private/auth/ancillaryequipments";

    @Test
    void getById_success() throws Exception {

        final long id = 1L;

        mockMvc.perform(get(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "  \"equipmentId\" : 1," +
                        "  \"orderId\" : \"1\"," +
                        "  \"serialNumber\" : \"GFAB02600017\"," +
                        "  \"externalNumber\" : null," +
                        "  \"accessType\" : \"DOCSIS\"," +
                        "  \"activity\" : null," +
                        "  \"activationDate\" : null," +
                        "  \"assignmentDate\" : null," +
                        "  \"status\" : \"BOOKED\"," +
                        "  \"nature\" : \"MAIN\"," +
                        "  \"recyclable\" : false," +
                        "  \"preactivated\" : true," +
                        "  \"batchNumber\" : \"20001\"," +
                        "  \"provider\" : {" +
                        "    \"providerId\" : 1," +
                        "    \"name\" : \"Toto\"," +
                        "    \"accessType\" : \"DOCSIS\"" +
                        "  }," +
                        "  \"warehouse\" : {" +
                        "    \"warehouseId\" : 1," +
                        "    \"name\" : \"TOTO\"," +
                        "    \"resellerCode\" : \"TOTO\"" +
                        "  }," +
                        "  \"category\" : \"ANCILLARY\"," +
                        "  \"events\" : [ \"free\", \"assign\", \"activate\", \"deactivate\" ]," +
                        "  \"macAddress\" : \"00:0A:95:9D:68:14\"," +
                        "  \"modelName\" : \"test\"," +
                        "  \"pairedEquipmentId\" : null," +
                        "  \"independent\" : false," +
                        "  \"pairedEquipmentCategory\" : null," +
                        "  \"equipmentName\" : \"BRDBOX\"," +
                        "  \"sfpVersion\" : null," +
                        "  \"model\" : {" +
                        "    \"id\" : 1," +
                        "    \"name\" : \"test\"," +
                        "    \"currentFirmware\" : \" test-2\"," +
                        "    \"provider\" : {" +
                        "      \"providerId\" : 1," +
                        "      \"name\" : \"Toto\"," +
                        "      \"accessType\" : \"DOCSIS\"" +
                        "    }," +
                        "    \"accessType\" : \"DOCSIS\"," +
                        "    \"category\" : \"ANCILLARY\"" +
                        "  }" +
                        "}"));
    }

    @Test
    void getAll_success() throws Exception {

        mockMvc.perform(get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").value(20))
                .andExpect(jsonPath("$._embedded.ancillaryequipments[0].equipmentId").value(1))
                .andExpect(jsonPath("$._embedded.ancillaryequipments[0].serialNumber").value("GFAB02600017"))
                .andExpect(jsonPath("$._embedded.ancillaryequipments[0].status").value("BOOKED"))
                .andExpect(jsonPath("$._embedded.ancillaryequipments[0].batchNumber").value("20001"));
    }

    @Test
    void delete_success() throws Exception {

        final long id = 2L;

        mockMvc.perform(delete(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void getBySerialNumber_success() throws Exception {
        final String serialNumber = "GFAB02600021";

        mockMvc.perform(get(baseUrl + "/serialnumber/{serialNumber}", serialNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{   \"equipmentId\" : 5,   \"orderId\" : null,   \"serialNumber\" : \"GFAB02600021\",   \"externalNumber\" : null,   \"accessType\" : \"DOCSIS\",   \"activity\" : null,   \"activationDate\" : null,   \"assignmentDate\" : null,   \"status\" : \"AVAILABLE\",   \"nature\" : \"ADDITIONAL\",   \"recyclable\" : false,   \"preactivated\" : true,   \"batchNumber\" : null,   \"provider\" : {     \"providerId\" : 1,     \"name\" : \"Toto\",     \"accessType\" : \"DOCSIS\"   },   \"warehouse\" : {     \"warehouseId\" : 1,     \"name\" : \"TOTO\",     \"resellerCode\" : \"TOTO\"   },   \"category\" : \"ANCILLARY\",   \"events\" : [ \"instore\", \"book\" ],   \"macAddress\" : \"00:0A:95:9D:68:18\",   \"modelName\" : \"test\",   \"pairedEquipmentId\" : null,   \"independent\" : true,   \"pairedEquipmentCategory\" : null,   \"equipmentName\" : \"BRDBOX\",   \"sfpVersion\" : \"PO2020071700001\",   \"numberRecycles\" : 0,   \"model\" : {     \"id\" : 1,     \"name\" : \"test\",     \"currentFirmware\" : \" test-2\",     \"provider\" : {       \"providerId\" : 1,       \"name\" : \"Toto\",       \"accessType\" : \"DOCSIS\"     },     \"accessType\" : \"DOCSIS\",     \"category\" : \"ANCILLARY\"   } }"));
    }

    @Test
    void add_success_oldModelName() throws Exception {
        ProviderDTO provider = new ProviderDTO();
        provider.setProviderId(1L);

        WarehouseDTO warehouseDTO = new WarehouseDTO();
        warehouseDTO.setWarehouseId(1L);

        var dto = new AddAncillaryDTO();
        dto.setMacAddress("00:0A:95:9D:68:14");
        dto.setProvider(provider);
        dto.setWarehouse(warehouseDTO);
        dto.setSerialNumber("GFAB02600017L");
        dto.setModelName("test");

        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{" +
                        "  \"equipmentId\" : 11," +
                        "  \"orderId\" : null," +
                        "  \"serialNumber\" : \"GFAB02600017L\"," +
                        "  \"externalNumber\" : null," +
                        "  \"accessType\" : null," +
                        "  \"activity\" : null," +
                        "  \"status\" : \"INSTORE\"," +
                        "  \"nature\" : \"MAIN\"," +
                        "  \"recyclable\" : false," +
                        "  \"preactivated\" : null," +
                        "  \"batchNumber\" : null," +
                        "  \"provider\" : {" +
                        "    \"providerId\" : 1," +
                        "    \"name\" : \"Toto\"," +
                        "    \"accessType\" : \"DOCSIS\"" +
                        "  }," +
                        "  \"warehouse\" : {" +
                        "    \"warehouseId\" : 1," +
                        "    \"name\" : \"TOTO\"," +
                        "    \"resellerCode\" : \"TOTO\"" +
                        "  }," +
                        "  \"category\" : \"ANCILLARY\"," +
                        "  \"events\" : null," +
                        "  \"macAddress\" : \"00:0A:95:9D:68:14\"," +
                        "  \"modelName\" : \"test\"," +
                        "  \"pairedEquipmentId\" : null," +
                        "  \"independent\" : null," +
                        "  \"pairedEquipmentCategory\" : null," +
                        "  \"equipmentName\" : null," +
                        "  \"sfpVersion\" : null," +
                        "  \"model\" : {" +
                        "    \"id\" : 1," +
                        "    \"name\" : \"test\"," +
                        "    \"currentFirmware\" : \" test-2\"," +
                        "    \"provider\" : {" +
                        "      \"providerId\" : 1," +
                        "      \"name\" : \"Toto\"," +
                        "      \"accessType\" : \"DOCSIS\"" +
                        "    }," +
                        "    \"accessType\" : \"DOCSIS\"," +
                        "    \"category\" : \"ANCILLARY\"" +
                        "  }" +
                        "}"));
    }

    @Test
    void add_success_newModelName() throws Exception {
        ProviderDTO provider = new ProviderDTO();
        provider.setProviderId(1L);

        WarehouseDTO warehouseDTO = new WarehouseDTO();
        warehouseDTO.setWarehouseId(1L);

        var dto = new AddAncillaryDTO();
        dto.setMacAddress("00:0A:95:9D:68:14");
        dto.setProvider(provider);
        dto.setWarehouse(warehouseDTO);
        dto.setSerialNumber("GFAB02600017L");
        var equipmentModelDto = new EquipmentModelDTO();
        equipmentModelDto.setName("test");
        dto.setModel(equipmentModelDto);

        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{" +
                        "  \"equipmentId\" : 11," +
                        "  \"orderId\" : null," +
                        "  \"serialNumber\" : \"GFAB02600017L\"," +
                        "  \"externalNumber\" : null," +
                        "  \"accessType\" : null," +
                        "  \"activity\" : null," +
                        "  \"status\" : \"INSTORE\"," +
                        "  \"nature\" : \"MAIN\"," +
                        "  \"recyclable\" : false," +
                        "  \"preactivated\" : null," +
                        "  \"batchNumber\" : null," +
                        "  \"provider\" : {" +
                        "    \"providerId\" : 1," +
                        "    \"name\" : \"Toto\"," +
                        "    \"accessType\" : \"DOCSIS\"" +
                        "  }," +
                        "  \"warehouse\" : {" +
                        "    \"warehouseId\" : 1," +
                        "    \"name\" : \"TOTO\"," +
                        "    \"resellerCode\" : \"TOTO\"" +
                        "  }," +
                        "  \"category\" : \"ANCILLARY\"," +
                        "  \"events\" : null," +
                        "  \"macAddress\" : \"00:0A:95:9D:68:14\"," +
                        "  \"modelName\" : \"test\"," +
                        "  \"pairedEquipmentId\" : null," +
                        "  \"independent\" : null," +
                        "  \"pairedEquipmentCategory\" : null," +
                        "  \"equipmentName\" : null," +
                        "  \"sfpVersion\" : null," +
                        "  \"model\" : {" +
                        "    \"id\" : 1," +
                        "    \"name\" : \"test\"," +
                        "    \"currentFirmware\" : \" test-2\"," +
                        "    \"provider\" : {" +
                        "      \"providerId\" : 1," +
                        "      \"name\" : \"Toto\"," +
                        "      \"accessType\" : \"DOCSIS\"" +
                        "    }," +
                        "    \"accessType\" : \"DOCSIS\"," +
                        "    \"category\" : \"ANCILLARY\"" +
                        "  }" +
                        "}"));
    }

    @Test
    void add_success_recycle_true() throws Exception {
        ProviderDTO provider = new ProviderDTO();
        provider.setProviderId(1L);

        WarehouseDTO warehouseDTO = new WarehouseDTO();
        warehouseDTO.setWarehouseId(1L);

        var dto = new AddAncillaryDTO();
        dto.setMacAddress("00:0A:95:9D:68:14");
        dto.setProvider(provider);
        dto.setRecyclable(true);
        dto.setWarehouse(warehouseDTO);
        dto.setSerialNumber("GFAB02600017L");
        var equipmentModelDto = new EquipmentModelDTO();
        equipmentModelDto.setName("test");
        dto.setModel(equipmentModelDto);

        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{" +
                        "  \"equipmentId\" : 11," +
                        "  \"orderId\" : null," +
                        "  \"serialNumber\" : \"GFAB02600017L\"," +
                        "  \"externalNumber\" : null," +
                        "  \"accessType\" : null," +
                        "  \"activity\" : null," +
                        "  \"status\" : \"INSTORE\"," +
                        "  \"nature\" : \"MAIN\"," +
                        "  \"recyclable\" : true," +
                        "  \"preactivated\" : null," +
                        "  \"batchNumber\" : null," +
                        "  \"provider\" : {" +
                        "    \"providerId\" : 1," +
                        "    \"name\" : \"Toto\"," +
                        "    \"accessType\" : \"DOCSIS\"" +
                        "  }," +
                        "  \"warehouse\" : {" +
                        "    \"warehouseId\" : 1," +
                        "    \"name\" : \"TOTO\"," +
                        "    \"resellerCode\" : \"TOTO\"" +
                        "  }," +
                        "  \"category\" : \"ANCILLARY\"," +
                        "  \"events\" : null," +
                        "  \"macAddress\" : \"00:0A:95:9D:68:14\"," +
                        "  \"modelName\" : \"test\"," +
                        "  \"pairedEquipmentId\" : null," +
                        "  \"independent\" : null," +
                        "  \"pairedEquipmentCategory\" : null," +
                        "  \"equipmentName\" : null," +
                        "  \"sfpVersion\" : null," +
                        "  \"model\" : {" +
                        "    \"id\" : 1," +
                        "    \"name\" : \"test\"," +
                        "    \"currentFirmware\" : \" test-2\"," +
                        "    \"provider\" : {" +
                        "      \"providerId\" : 1," +
                        "      \"name\" : \"Toto\"," +
                        "      \"accessType\" : \"DOCSIS\"" +
                        "    }," +
                        "    \"accessType\" : \"DOCSIS\"," +
                        "    \"category\" : \"ANCILLARY\"" +
                        "  }" +
                        "}"));
    }

    @Test
    void put_success() throws Exception {

        final long id = 1L;
        UpdateAncillaryEquipmentDTO dto = new UpdateAncillaryEquipmentDTO();
        dto.setIndependent(true);
        dto.setMacAddress("00:0A:95:9D:68:14");

        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "  \"equipmentId\" : 1," +
                        "  \"orderId\" : null," +
                        "  \"serialNumber\" : \"GFAB02600017\"," +
                        "  \"externalNumber\" : null," +
                        "  \"accessType\" : \"DOCSIS\"," +
                        "  \"activity\" : null," +
                        "  \"status\" : \"BOOKED\"," +
                        "  \"nature\" : \"MAIN\"," +
                        "  \"recyclable\" : false," +
                        "  \"preactivated\" : true," +
                        "  \"batchNumber\" : null," +
                        "  \"provider\" : {" +
                        "    \"providerId\" : 1," +
                        "    \"name\" : \"Toto\"," +
                        "    \"accessType\" : \"DOCSIS\"" +
                        "  }," +
                        "  \"warehouse\" : null," +
                        "  \"category\" : \"ANCILLARY\"," +
                        "  \"events\" : null," +
                        "  \"macAddress\" : \"00:0A:95:9D:68:14\"," +
                        "  \"modelName\" : \"test\"," +
                        "  \"pairedEquipmentId\" : null," +
                        "  \"independent\" : true," +
                        "  \"pairedEquipmentCategory\" : null," +
                        "  \"equipmentName\" : \"BRDBOX\"," +
                        "  \"sfpVersion\" : null," +
                        "  \"model\" : {" +
                        "    \"id\" : 1," +
                        "    \"name\" : \"test\"," +
                        "    \"currentFirmware\" : \" test-2\"," +
                        "    \"provider\" : {" +
                        "      \"providerId\" : 1," +
                        "      \"name\" : \"Toto\"," +
                        "      \"accessType\" : \"DOCSIS\"" +
                        "    }," +
                        "    \"accessType\" : \"DOCSIS\"," +
                        "    \"category\" : \"ANCILLARY\"" +
                        "  }" +
                        "}"));
    }

    @Test
    void put_success_with_pairedEquipement() throws Exception {
        UpdateAncillaryEquipmentDTO dto = new UpdateAncillaryEquipmentDTO();
        dto.setIndependent(true);
        dto.setPairedEquipmentId(6L);
        dto.setMacAddress("00:0A:95:9D:68:14");

        mockMvc.perform(put(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"Paired equipment ID cannot be filled for independent ancillary when status is not ACTIVATED or ASSIGNED\"}"));
    }


    @Test
    void patch_success_with_nonNull_pairedEquipment_And_null_independentMode() throws Exception {

        final long id = 1L;
        UpdateAncillaryEquipmentDTO dto = new UpdateAncillaryEquipmentDTO();
        dto.setIndependent(null);
        dto.setPairedEquipmentId(7L);
        dto.setMacAddress("00:0A:95:9D:68:14");

        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "  \"equipmentId\" : 1," +
                        "  \"orderId\" : \"1\"," +
                        "  \"serialNumber\" : \"GFAB02600017\"," +
                        "  \"externalNumber\" : null," +
                        "  \"accessType\" : \"DOCSIS\"," +
                        "  \"activity\" : null," +
                        "  \"status\" : \"BOOKED\"," +
                        "  \"nature\" : \"MAIN\"," +
                        "  \"recyclable\" : false," +
                        "  \"preactivated\" : true," +
                        "  \"batchNumber\" : \"20001\"," +
                        "  \"provider\" : {" +
                        "    \"providerId\" : 1," +
                        "    \"name\" : \"Toto\"," +
                        "    \"accessType\" : \"DOCSIS\"" +
                        "  }," +
                        "  \"warehouse\" : {" +
                        "    \"warehouseId\" : 1," +
                        "    \"name\" : \"TOTO\"," +
                        "    \"resellerCode\" : \"TOTO\"" +
                        "  }," +
                        "  \"category\" : \"ANCILLARY\"," +
                        "  \"events\" : null," +
                        "  \"macAddress\" : \"00:0A:95:9D:68:14\"," +
                        "  \"modelName\" : \"test\"," +
                        "  \"pairedEquipmentId\" : 7," +
                        "  \"independent\" : false," +
                        "  \"pairedEquipmentCategory\" : \"SIMCARD\"," +
                        "  \"equipmentName\" : \"BRDBOX\"," +
                        "  \"sfpVersion\" : null," +
                        "  \"model\" : {" +
                        "    \"id\" : 1," +
                        "    \"name\" : \"test\"," +
                        "    \"currentFirmware\" : \" test-2\"," +
                        "    \"provider\" : {" +
                        "      \"providerId\" : 1," +
                        "      \"name\" : \"Toto\"," +
                        "      \"accessType\" : \"DOCSIS\"" +
                        "    }," +
                        "    \"accessType\" : \"DOCSIS\"," +
                        "    \"category\" : \"ANCILLARY\"" +
                        "  }" +
                        "}"));
    }

    @Test
    void patch_success() throws Exception {

        final long id = 1L;

        UpdateAncillaryEquipmentDTO dto = new UpdateAncillaryEquipmentDTO();
        dto.setIndependent(true);
        dto.setMacAddress("00:0A:95:9D:68:14");
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "  \"equipmentId\" : 1," +
                        "  \"orderId\" : \"1\"," +
                        "  \"serialNumber\" : \"GFAB02600017\"," +
                        "  \"externalNumber\" : null," +
                        "  \"accessType\" : \"DOCSIS\"," +
                        "  \"activity\" : null," +
                        "  \"status\" : \"BOOKED\"," +
                        "  \"nature\" : \"MAIN\"," +
                        "  \"recyclable\" : false," +
                        "  \"preactivated\" : true," +
                        "  \"batchNumber\" : \"20001\"," +
                        "  \"provider\" : {" +
                        "    \"providerId\" : 1," +
                        "    \"name\" : \"Toto\"," +
                        "    \"accessType\" : \"DOCSIS\"" +
                        "  }," +
                        "  \"warehouse\" : {" +
                        "    \"warehouseId\" : 1," +
                        "    \"name\" : \"TOTO\"," +
                        "    \"resellerCode\" : \"TOTO\"" +
                        "  }," +
                        "  \"category\" : \"ANCILLARY\"," +
                        "  \"events\" : null," +
                        "  \"macAddress\" : \"00:0A:95:9D:68:14\"," +
                        "  \"modelName\" : \"test\"," +
                        "  \"pairedEquipmentId\" : null," +
                        "  \"independent\" : true," +
                        "  \"pairedEquipmentCategory\" : null," +
                        "  \"equipmentName\" : \"BRDBOX\"," +
                        "  \"sfpVersion\" : null," +
                        "  \"model\" : {" +
                        "    \"id\" : 1," +
                        "    \"name\" : \"test\"," +
                        "    \"currentFirmware\" : \" test-2\"," +
                        "    \"provider\" : {" +
                        "      \"providerId\" : 1," +
                        "      \"name\" : \"Toto\"," +
                        "      \"accessType\" : \"DOCSIS\"" +
                        "    }," +
                        "    \"accessType\" : \"DOCSIS\"," +
                        "    \"category\" : \"ANCILLARY\"" +
                        "  }" +
                        "}"));
    }

    @Test
    void search_ancillaryCRUD_noParameters() throws Exception {
        SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();

        final String requestJson = objectMapper.writeValueAsString(searchAncillaryEquipmentDTO);

        mockMvc.perform(get(baseUrl + "/search?page=1&size=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"_embedded\" : {  \"ancillaryequipments\" : [ { \"equipmentId\" : 2, \"orderId\" : \"3\", \"serialNumber\" : \"GFAB02600018\", \"externalNumber\" : null, \"accessType\" : \"DOCSIS\", \"activity\" : null, \"activationDate\" : null, \"assignmentDate\" : null, \"status\" : \"DEACTIVATED\", \"nature\" : \"MAIN\", \"recyclable\" : false, \"preactivated\" : true, \"batchNumber\" : null, \"provider\" : {\"providerId\" : 1,\"name\" : \"Toto\",\"accessType\" : \"DOCSIS\" }, \"warehouse\" : {\"warehouseId\" : 1,\"name\" : \"TOTO\",\"resellerCode\" : \"TOTO\" }, \"category\" : \"ANCILLARY\", \"events\" : [ \"rollback_deactivate\", \"available\" ], \"macAddress\" : \"00:0A:95:9D:68:15\", \"modelName\" : \"sagem\", \"pairedEquipmentId\" : null, \"independent\" : true, \"pairedEquipmentCategory\" : null, \"equipmentName\" : \"BRDBOX\", \"sfpVersion\" : null, \"numberRecycles\" : 0, \"model\" : {\"id\" : 2,\"name\" : \"sagem\",\"currentFirmware\" : \" test-3\",\"provider\" : {  \"providerId\" : 1,  \"name\" : \"Toto\",  \"accessType\" : \"DOCSIS\"},\"accessType\" : \"DOCSIS\",\"category\" : \"ANCILLARY\" }  } ]},\"_links\" : {  \"first\" : { \"href\" : \"http://localhost/ancillaryequipments?page=0&size=1\"  },  \"prev\" : { \"href\" : \"http://localhost/ancillaryequipments?page=0&size=1\"  },  \"self\" : { \"href\" : \"http://localhost/ancillaryequipments\"  },  \"next\" : { \"href\" : \"http://localhost/ancillaryequipments?page=2&size=1\"  },  \"last\" : { \"href\" : \"http://localhost/ancillaryequipments?page=5&size=1\"  }},\"page\" : {  \"size\" : 1,  \"totalElements\" : 6,  \"totalPages\" : 6,  \"number\" : 1} }"));
    }

    @Test
    void search_ancillaryCRUD_independent() throws Exception {
        SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
        searchAncillaryEquipmentDTO.setIndependent(true);

        final String requestJson = objectMapper.writeValueAsString(searchAncillaryEquipmentDTO);

        mockMvc.perform(get(baseUrl + "/search?page=1&size=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{   \"_embedded\" : {\"ancillaryequipments\" : [ {  \"equipmentId\" : 2,  \"orderId\" : \"3\",  \"serialNumber\" : \"GFAB02600018\",  \"externalNumber\" : null,  \"accessType\" : \"DOCSIS\",  \"activity\" : null,  \"activationDate\" : null,  \"assignmentDate\" : null,  \"status\" : \"DEACTIVATED\",  \"nature\" : \"MAIN\",  \"recyclable\" : false,  \"preactivated\" : true,  \"batchNumber\" : null,  \"provider\" : {    \"providerId\" : 1,    \"name\" : \"Toto\",    \"accessType\" : \"DOCSIS\"  },  \"warehouse\" : {    \"warehouseId\" : 1,    \"name\" : \"TOTO\",    \"resellerCode\" : \"TOTO\"  },  \"category\" : \"ANCILLARY\",  \"events\" : [ \"rollback_deactivate\", \"available\" ],  \"macAddress\" : \"00:0A:95:9D:68:15\",  \"modelName\" : \"sagem\",  \"pairedEquipmentId\" : null,  \"independent\" : true,  \"pairedEquipmentCategory\" : null,  \"equipmentName\" : \"BRDBOX\",  \"sfpVersion\" : null,  \"numberRecycles\" : 0,  \"model\" : {    \"id\" : 2,    \"name\" : \"sagem\",    \"currentFirmware\" : \" test-3\",    \"provider\" : { \"providerId\" : 1, \"name\" : \"Toto\", \"accessType\" : \"DOCSIS\"    },    \"accessType\" : \"DOCSIS\",    \"category\" : \"ANCILLARY\"  }} ]   },   \"_links\" : {\"first\" : {  \"href\" : \"http://localhost/ancillaryequipments?page=0&size=1\"},\"prev\" : {  \"href\" : \"http://localhost/ancillaryequipments?page=0&size=1\"},\"self\" : {  \"href\" : \"http://localhost/ancillaryequipments\"},\"next\" : {  \"href\" : \"http://localhost/ancillaryequipments?page=2&size=1\"},\"last\" : {  \"href\" : \"http://localhost/ancillaryequipments?page=5&size=1\"}   },   \"page\" : {\"size\" : 1,\"totalElements\" : 6,\"totalPages\" : 6,\"number\" : 1   } }"));
    }

    @Test
    void search_ancillaryCRUD_notIndependent() throws Exception {
        SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
        searchAncillaryEquipmentDTO.setIndependent(false);

        final String requestJson = objectMapper.writeValueAsString(searchAncillaryEquipmentDTO);

        mockMvc.perform(get(baseUrl + "/search?page=1&size=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{   \"_embedded\" : {     \"ancillaryequipments\" : [ {       \"equipmentId\" : 2,       \"orderId\" : \"3\",       \"serialNumber\" : \"GFAB02600018\",       \"externalNumber\" : null,       \"accessType\" : \"DOCSIS\",       \"activity\" : null,       \"activationDate\" : null,       \"assignmentDate\" : null,       \"status\" : \"DEACTIVATED\",       \"nature\" : \"MAIN\",       \"recyclable\" : false,       \"preactivated\" : true,       \"batchNumber\" : null,       \"provider\" : {         \"providerId\" : 1,         \"name\" : \"Toto\",         \"accessType\" : \"DOCSIS\"       },       \"warehouse\" : {         \"warehouseId\" : 1,         \"name\" : \"TOTO\",         \"resellerCode\" : \"TOTO\"       },       \"category\" : \"ANCILLARY\",       \"events\" : [ \"rollback_deactivate\", \"available\" ],       \"macAddress\" : \"00:0A:95:9D:68:15\",       \"modelName\" : \"sagem\",       \"pairedEquipmentId\" : null,       \"independent\" : true,       \"pairedEquipmentCategory\" : null,       \"equipmentName\" : \"BRDBOX\",       \"sfpVersion\" : null,       \"numberRecycles\" : 0,       \"model\" : {         \"id\" : 2,         \"name\" : \"sagem\",         \"currentFirmware\" : \" test-3\",         \"provider\" : {           \"providerId\" : 1,           \"name\" : \"Toto\",           \"accessType\" : \"DOCSIS\"         },         \"accessType\" : \"DOCSIS\",         \"category\" : \"ANCILLARY\"       }     } ]   },   \"_links\" : {     \"first\" : {       \"href\" : \"http://localhost/ancillaryequipments?page=0&size=1\"     },     \"prev\" : {       \"href\" : \"http://localhost/ancillaryequipments?page=0&size=1\"     },     \"self\" : {       \"href\" : \"http://localhost/ancillaryequipments\"     },     \"next\" : {       \"href\" : \"http://localhost/ancillaryequipments?page=2&size=1\"     },     \"last\" : {       \"href\" : \"http://localhost/ancillaryequipments?page=5&size=1\"     }   },   \"page\" : {     \"size\" : 1,     \"totalElements\" : 6,     \"totalPages\" : 6,     \"number\" : 1   } }"));
    }

    @Test
    void setEventOnAncillary_numberRecycle_increment() throws Exception {

        final long ancillaryEq = 4L;

        AncillaryChangeStateDTO dto = new AncillaryChangeStateDTO();
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch(baseUrl + "/{ancillaryEq}/{event}", ancillaryEq, Event.available)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"equipmentId\" : 4,\"serialNumber\" : \"GFAB02600020\",\"externalNumber\" : \"37799900002\",\"accessType\" : \"FTTH\",\"activity\" : null,\"status\" : \"AVAILABLE\",\"nature\" : \"MAIN\",\"recyclable\" : false,\"preactivated\" : true,\"batchNumber\" : null,\"provider\" : {\"providerId\" : 1,\"name\" : \"Toto\",\"accessType\" : \"DOCSIS\"},\"warehouse\" : {\"warehouseId\" : 1,\"name\" : \"TOTO\",\"resellerCode\" : \"TOTO\"},\"category\" : \"ANCILLARY\",\"events\" : null,\"macAddress\" : \"00:0A:95:9D:68:17\",\"modelName\" : \"sagem\",\"pairedEquipmentId\" : null,\"independent\" : true,\"pairedEquipmentCategory\" : null,\"equipmentName\" : \"HDD\",\"sfpVersion\" : null,\"numberRecycles\" : 1,\"model\" : {\"id\" : 2,\"name\" : \"sagem\",\"currentFirmware\" : \" test-3\",\"provider\" : {\"providerId\" : 1,\"name\" : \"Toto\",\"accessType\" : \"DOCSIS\"},\"accessType\" : \"DOCSIS\",\"category\" : \"ANCILLARY\"}}"));
    }

    @Test
    void setEventOnAncillary_selEvent_success() throws Exception {

        final long ancillaryEq = 3L;

        AncillaryChangeStateDTO dto = new AncillaryChangeStateDTO();
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch(baseUrl + "/{ancillaryEq}/{event}", ancillaryEq, Event.assign)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{  \"equipmentId\" : 3,  \"orderId\" : null,  \"serialNumber\" : \"GFAB02600019\",  \"externalNumber\" : null,  \"accessType\" : \"DOCSIS\",  \"activity\" : null,  \"status\" : \"ASSIGNED\",  \"nature\" : \"MAIN\",  \"recyclable\" : true,  \"preactivated\" : true,  \"batchNumber\" : null,  \"provider\" : {   \"providerId\" : 1,   \"name\" : \"Toto\",   \"accessType\" : \"DOCSIS\"  },  \"warehouse\" : {   \"warehouseId\" : 1,   \"name\" : \"TOTO\",   \"resellerCode\" : \"TOTO\"  },  \"category\" : \"ANCILLARY\",  \"events\" : null,  \"macAddress\" : \"00:0A:95:9D:68:16\",  \"modelName\" : \"HDD500\",  \"pairedEquipmentId\" : 1,  \"independent\" : true,  \"pairedEquipmentCategory\" : \"ANCILLARY\",  \"equipmentName\" : \"BRDBOX\",  \"sfpVersion\" : null,  \"numberRecycles\" : 0,  \"model\" : {   \"id\" : 3,   \"name\" : \"HDD500\",   \"currentFirmware\" : \" test-4\",   \"provider\" : {    \"providerId\" : 1,    \"name\" : \"Toto\",    \"accessType\" : \"DOCSIS\"   },   \"accessType\" : \"DOCSIS\",   \"category\" : \"ANCILLARY\"  } }"));
    }

    @Test
    void setEventOnAncillary_notValid() throws Exception {

        final long ancillaryEq = 4L;

        AncillaryChangeStateDTO dto = new AncillaryChangeStateDTO();
        dto.setOrderId("1L");
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch(baseUrl + "/{ancillaryEq}/{event}", ancillaryEq, Event.book)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Cannot apply event 'book' on Ancillary Equipment with status 'DEACTIVATED' and Serial Number 'GFAB02600020'"));
    }

    @Test
    void setEventOnAncillary_success() throws Exception {

        final long ancillaryEq = 5L;

        AncillaryChangeStateDTO dto = new AncillaryChangeStateDTO();
        dto.setOrderId("1L");
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch(baseUrl + "/{ancillaryEq}/{event}", ancillaryEq, Event.book)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"equipmentId\" : 5,\"orderId\" : \"1L\",\"serialNumber\" : \"GFAB02600021\",\"externalNumber\" : null,\"accessType\" : \"DOCSIS\",\"activity\" : null,\"activationDate\" : null,\"assignmentDate\" : null,\"status\" : \"BOOKED\",\"nature\" : \"ADDITIONAL\",\"recyclable\" : false,\"preactivated\" : true,\"batchNumber\" : null,\"provider\" : {\"providerId\" : 1,\"name\" : \"Toto\",\"accessType\" : \"DOCSIS\"},\"warehouse\" : {\"warehouseId\" : 1,\"name\" : \"TOTO\",\"resellerCode\" : \"TOTO\"},\"category\" : \"ANCILLARY\",\"events\" : null,\"macAddress\" : \"00:0A:95:9D:68:18\",\"modelName\" : \"test\",\"pairedEquipmentId\" : null,\"independent\" : true,\"pairedEquipmentCategory\" : null,\"equipmentName\" : \"BRDBOX\",\"sfpVersion\" : \"PO2020071700001\",\"numberRecycles\" : 0,\"model\" : {\"id\" : 1,\"name\" : \"test\",\"currentFirmware\" : \" test-2\",\"provider\" : {\"providerId\" : 1,\"name\" : \"Toto\",\"accessType\" : \"DOCSIS\"},\"accessType\" : \"DOCSIS\",\"category\" : \"ANCILLARY\"}}"));
    }

    @Test
    void setEventOnAncillary_activate_success() throws Exception {

        final long ancillaryEq = 3L;

        mockMvc.perform(patch(baseUrl + "/{ancillaryEq}/{event}", ancillaryEq, Event.activate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"equipmentId\" : 3,\"orderId\" : null,\"serialNumber\" : \"GFAB02600019\",\"externalNumber\" : null,\"accessType\" : \"DOCSIS\",\"activity\" : null,\"activationDate\" : \"2020-05-31T20:35:24\",\"status\" : \"ACTIVATED\",\"nature\" : \"MAIN\",\"recyclable\" : true,\"preactivated\" : true,\"batchNumber\" : null,\"provider\" : {\"providerId\" : 1,\"name\" : \"Toto\",\"accessType\" : \"DOCSIS\"},\"warehouse\" : {\"warehouseId\" : 1,\"name\" : \"TOTO\",\"resellerCode\" : \"TOTO\"},\"category\" : \"ANCILLARY\",\"events\" : null,\"macAddress\" : \"00:0A:95:9D:68:16\",\"modelName\" : \"HDD500\",\"pairedEquipmentId\" : 1,\"independent\" : true,\"pairedEquipmentCategory\" : \"ANCILLARY\",\"equipmentName\" : \"BRDBOX\",\"sfpVersion\" : null,\"numberRecycles\" : 0,\"model\" : {\"id\" : 3,\"name\" : \"HDD500\",\"currentFirmware\" : \" test-4\",\"provider\" : {\"providerId\" : 1,\"name\" : \"Toto\",\"accessType\" : \"DOCSIS\"},\"accessType\" : \"DOCSIS\",\"category\" : \"ANCILLARY\"}}"));
    }

    @Test
    void setEventOnAncillary_noBody_success() throws Exception {

        final long ancillaryEq = 5L;

        mockMvc.perform(patch(baseUrl + "/{ancillaryEq}/{event}", ancillaryEq, Event.book)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"equipmentId\" : 5,\"orderId\" : null,\"serialNumber\" : \"GFAB02600021\",\"externalNumber\" : null,\"accessType\" : \"DOCSIS\",\"activity\" : null,\"activationDate\" : null,\"assignmentDate\" : null,\"status\" : \"BOOKED\",\"nature\" : \"ADDITIONAL\",\"recyclable\" : false,\"preactivated\" : true,\"batchNumber\" : null,\"provider\" : {\"providerId\" : 1,\"name\" : \"Toto\",\"accessType\" : \"DOCSIS\"},\"warehouse\" : {\"warehouseId\" : 1,\"name\" : \"TOTO\",\"resellerCode\" : \"TOTO\"},\"category\" : \"ANCILLARY\",\"events\" : null,\"macAddress\" : \"00:0A:95:9D:68:18\",\"modelName\" : \"test\",\"pairedEquipmentId\" : null,\"independent\" : true,\"pairedEquipmentCategory\" : null,\"equipmentName\" : \"BRDBOX\",\"sfpVersion\" : \"PO2020071700001\",\"numberRecycles\" : 0,\"model\" : {\"id\" : 1,\"name\" : \"test\",\"currentFirmware\" : \" test-2\",\"provider\" : {\"providerId\" : 1,\"name\" : \"Toto\",\"accessType\" : \"DOCSIS\"},\"accessType\" : \"DOCSIS\",\"category\" : \"ANCILLARY\"}}"));
    }

    @Test
    void setEventOnAncillary_rollbackOnhold_success() throws Exception {
        final long ancillaryEq = 3L;

        // Set the pairedEquipment
        mockMvc.perform(patch(baseUrl + "/{ancillaryEq}", ancillaryEq)
                        .content("{\"pairedEquipmentId\": 6, \"recyclable\": true}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ASSIGNED"))
                .andExpect(jsonPath("$.pairedEquipmentId").value(6L))
                .andExpect(jsonPath("$.pairedEquipmentCategory").value("SIMCARD"));

        // Onhold -> Paired equipment should be removed
        mockMvc.perform(patch(baseUrl + "/{ancillaryEq}/{event}", ancillaryEq, Event.onhold)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ONHOLD"))
                .andExpect(jsonPath("$.pairedEquipmentId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.pairedEquipmentCategory").value(IsNull.nullValue()));

        // Rollback Onhold -> Paired equipment should be back
        mockMvc.perform(patch(baseUrl + "/{ancillaryEq}/{event}", ancillaryEq, Event.rollback_onhold)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVATED"))
                .andExpect(jsonPath("$.pairedEquipmentId").value(6L))
                .andExpect(jsonPath("$.pairedEquipmentCategory").value("SIMCARD"));
    }

    @Test
    void setEventOnAncillary_rollbackOnhold_failureIfMissingOldPairedEqt() throws Exception {
        final long ancillaryEq = 3L;

        // Set the pairedEquipment
        mockMvc.perform(patch(baseUrl + "/{ancillaryEq}", ancillaryEq)
                        .content("{\"pairedEquipmentId\": 6, \"recyclable\": true}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ASSIGNED"))
                .andExpect(jsonPath("$.pairedEquipmentId").value(6L))
                .andExpect(jsonPath("$.pairedEquipmentCategory").value("SIMCARD"));

        // Onhold -> Paired equipment should be removed
        mockMvc.perform(patch(baseUrl + "/{ancillaryEq}/{event}", ancillaryEq, Event.onhold)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ONHOLD"))
                .andExpect(jsonPath("$.pairedEquipmentId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.pairedEquipmentCategory").value(IsNull.nullValue()));

        when(equipmentRepository.findById(6L)).thenReturn(Optional.empty());

        // Rollback Onhold -> Paired equipment should be back
        mockMvc.perform(patch(baseUrl + "/{ancillaryEq}/{event}", ancillaryEq, Event.rollback_onhold)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Unable to 'rollback_onhold' equipment with serial number 'GFAB02600019' as its old paired equipment with internal ID '6' cannot be found"));

        Mockito.reset(equipmentRepository);
    }

    @Test
    void getByPairedEquipmentSerial_success() throws Exception {

        final String serialNumber = "GFAB02600017";

        mockMvc.perform(get(baseUrl + "/pairedEquipment/{serialNumber}", serialNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "  \"equipmentId\" : 3,\n" +
                        "  \"orderId\" : null,\n" +
                        "  \"serialNumber\" : \"GFAB02600019\",\n" +
                        "  \"externalNumber\" : null,\n" +
                        "  \"accessType\" : \"DOCSIS\",\n" +
                        "  \"activity\" : null,\n" +
                        "  \"activationDate\" : null,\n" +
                        "  \"assignmentDate\" : \"2021-01-18T08:45:58\",\n" +
                        "  \"status\" : \"ASSIGNED\",\n" +
                        "  \"nature\" : \"MAIN\",\n" +
                        "  \"recyclable\" : true,\n" +
                        "  \"preactivated\" : true,\n" +
                        "  \"batchNumber\" : null,\n" +
                        "  \"provider\" : {\n" +
                        "    \"providerId\" : 1,\n" +
                        "    \"name\" : \"Toto\",\n" +
                        "    \"accessType\" : \"DOCSIS\"\n" +
                        "  },\n" +
                        "  \"warehouse\" : {\n" +
                        "    \"warehouseId\" : 1,\n" +
                        "    \"name\" : \"TOTO\",\n" +
                        "    \"resellerCode\" : \"TOTO\"\n" +
                        "  },\n" +
                        "  \"category\" : \"ANCILLARY\",\n" +
                        "  \"events\" : [ \"unassign\", \"activate\", \"onhold\" ],\n" +
                        "  \"macAddress\" : \"00:0A:95:9D:68:16\",\n" +
                        "  \"modelName\" : \"HDD500\",\n" +
                        "  \"pairedEquipmentId\" : 1,\n" +
                        "  \"independent\" : true,\n" +
                        "  \"pairedEquipmentCategory\" : \"ANCILLARY\",\n" +
                        "  \"equipmentName\" : \"BRDBOX\",\n" +
                        "  \"sfpVersion\" : null,\n" +
                        "  \"numberRecycles\" : 0,\n" +
                        "  \"model\" : {\n" +
                        "    \"id\" : 3,\n" +
                        "    \"name\" : \"HDD500\",\n" +
                        "    \"currentFirmware\" : \" test-4\",\n" +
                        "    \"provider\" : {\n" +
                        "      \"providerId\" : 1,\n" +
                        "      \"name\" : \"Toto\",\n" +
                        "      \"accessType\" : \"DOCSIS\"\n" +
                        "    },\n" +
                        "    \"accessType\" : \"DOCSIS\",\n" +
                        "    \"category\" : \"ANCILLARY\"\n" +
                        "  }\n" +
                        "}"));
    }

    @Test
    void getByPairedEquipmentSerial_failed() throws Exception {

        final String serialNumber = "GFAB03600014";

        mockMvc.perform(get(baseUrl + "/pairedEquipment/{serialNumber}", serialNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"status\":404,\"error\":\"Ancillary equipment with pairedEquipment-serialNumber 'GFAB03600014' could not be found\",\"extraData\":null}"));
    }

    @Test
    void getRevisions_success() throws Exception {
        mockMvc.perform(get(baseUrl + "/1/revisions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"_embedded\" : {\"revisions\" : [ {  \"date\" : \"2020-02-25T10:52:37\",  \"entity\" : { \"equipmentId\" : 1, \"orderId\" : null, \"serialNumber\" : \"GFAB02600070\", \"externalNumber\" : null, \"accessType\" : \"FTTH\", \"activity\" : null, \"activationDate\" : null, \"assignmentDate\" : null, \"status\" : \"AVAILABLE\", \"nature\" : \"MAIN\", \"recyclable\" : true, \"preactivated\" : false, \"batchNumber\" : \"20001\", \"provider\" : null, \"warehouse\" : {\"warehouseId\" : 1,\"name\" : \"TOTO\",\"resellerCode\" : \"TOTO\" }, \"category\" : \"ANCILLARY\", \"events\" : null, \"macAddress\" : \"6C:99:61:00:02:01\", \"modelName\" : null, \"pairedEquipmentId\" : null, \"independent\" : true, \"pairedEquipmentCategory\" : null, \"equipmentName\" : \"ONT\", \"sfpVersion\" : \"No SFP module \", \"numberRecycles\" : null, \"model\" : null  },  \"author\" : \"g.,fantappie\"} ] }, \"_links\" : {\"self\" : {  \"href\" : \"http://localhost\"} }, \"page\" : {\"size\" : 20,\"totalElements\" : 1,\"totalPages\" : 1,\"number\" : 0 }  }"));
    }

    @Test
    void getRevisions_missing() throws Exception {
        mockMvc.perform(get(baseUrl + "/99/revisions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{   \"_links\" : {     \"self\" : {       \"href\" : \"http://localhost\"     }   },   \"page\" : {     \"size\" : 20,     \"totalElements\" : 0,     \"totalPages\" : 0,     \"number\" : 0   } }"));
    }
}
