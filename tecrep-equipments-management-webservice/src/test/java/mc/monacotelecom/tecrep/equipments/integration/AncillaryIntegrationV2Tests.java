package mc.monacotelecom.tecrep.equipments.integration;

import mc.monacotelecom.tecrep.equipments.dto.request.AncillaryChangeStateDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddAncillaryDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchAncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateAncillaryEquipmentDTOV2;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static mc.monacotelecom.tecrep.equipments.enums.Status.ACTIVATED;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sql/clean.sql", "/sql/ancillary_data.sql"})
class AncillaryIntegrationV2Tests extends BaseIntegrationTest {

    final String baseUrl = "/api/v2/private/auth/ancillaryequipments";

    @Test
    void getById_success() throws Exception {

        final long id = 1L;

        mockMvc.perform(get(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "  \"id\" : 1," +
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
                        "  \"warehouse\" : {" +
                        "    \"id\" : 1," +
                        "    \"name\" : \"TOTO\"," +
                        "    \"resellerCode\" : \"TOTO\"" +
                        "  }," +
                        "  \"category\" : \"ANCILLARY\"," +
                        "  \"events\" : [ \"free\", \"assign\", \"activate\", \"deactivate\" ]," +
                        "  \"macAddress\" : \"00:0A:95:9D:68:14\"," +
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
                        "      \"id\" : 1," +
                        "      \"name\" : \"Toto\"," +
                        "      \"accessType\" : \"DOCSIS\"" +
                        "    }," +
                        "    \"accessType\" : \"DOCSIS\"," +
                        "    \"category\" : \"ANCILLARY\"" +
                        "  }" +
                        "}"));
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
    void delete_wrongStatus() throws Exception {

        final long id = 1L;

        mockMvc.perform(delete(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("Bad Request"))
                .andExpect(jsonPath("$.errorMessage").value("Ancillary equipment with serial number 'GFAB02600017' cannot be deleted because it is 'BOOKED', it should be INSTORE, DEACTIVATED or DEPRECATED"));
    }

    @Test
    void getBySerialNumber_success() throws Exception {
        final String serialNumber = "GFAB02600021";

        mockMvc.perform(get(baseUrl + "/serialnumber/{serialNumber}", serialNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{   \"id\" : 5,   \"orderId\" : null,   \"serialNumber\" : \"GFAB02600021\",   \"externalNumber\" : null,   \"accessType\" : \"DOCSIS\",   \"activity\" : null,   \"activationDate\" : null,   \"assignmentDate\" : null,   \"status\" : \"AVAILABLE\",   \"nature\" : \"ADDITIONAL\",   \"recyclable\" : false,   \"preactivated\" : true,   \"batchNumber\" : null,     \"warehouse\" : {     \"id\" : 1,     \"name\" : \"TOTO\",     \"resellerCode\" : \"TOTO\"   },   \"category\" : \"ANCILLARY\",   \"events\" : [ \"instore\", \"book\" ],   \"macAddress\" : \"00:0A:95:9D:68:18\",   \"pairedEquipmentId\" : null,   \"independent\" : true,   \"pairedEquipmentCategory\" : null,   \"equipmentName\" : \"BRDBOX\",   \"sfpVersion\" : \"PO2020071700001\",   \"numberRecycles\" : 0,   \"model\" : {     \"id\" : 1,     \"name\" : \"test\",     \"currentFirmware\" : \" test-2\",     \"provider\" : {       \"id\" : 1,       \"name\" : \"Toto\",       \"accessType\" : \"DOCSIS\"     },     \"accessType\" : \"DOCSIS\",     \"category\" : \"ANCILLARY\"   } }"));
    }

    @Test
    void add_success_newModelName() throws Exception {
        var dto = new AddAncillaryDTOV2();
        dto.setMacAddress("00:0A:95:9D:68:14");
        dto.setWarehouseName("TOTO");
        dto.setSerialNumber("GFAB02600017L");
        dto.setModelName("test");

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{" +
                        "  \"id\" : 11," +
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
                        "  \"warehouse\" : {" +
                        "    \"id\" : 1," +
                        "    \"name\" : \"TOTO\"," +
                        "    \"resellerCode\" : \"TOTO\"" +
                        "  }," +
                        "  \"category\" : \"ANCILLARY\"," +
                        "  \"events\" : null," +
                        "  \"macAddress\" : \"00:0A:95:9D:68:14\"," +
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
                        "      \"id\" : 1," +
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
        var dto = new AddAncillaryDTOV2();
        dto.setMacAddress("00:0A:95:9D:68:14");
        dto.setRecyclable(true);
        dto.setWarehouseName("TOTO");
        dto.setSerialNumber("GFAB02600017L");
        dto.setModelName("test");

        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{" +
                        "  \"id\" : 11," +
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
                        "  \"warehouse\" : {" +
                        "    \"id\" : 1," +
                        "    \"name\" : \"TOTO\"," +
                        "    \"resellerCode\" : \"TOTO\"" +
                        "  }," +
                        "  \"category\" : \"ANCILLARY\"," +
                        "  \"events\" : null," +
                        "  \"macAddress\" : \"00:0A:95:9D:68:14\"," +
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
                        "      \"id\" : 1," +
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
        UpdateAncillaryEquipmentDTOV2 dto = new UpdateAncillaryEquipmentDTOV2();
        dto.setIndependent(true);
        dto.setMacAddress("00:0A:95:9D:68:14");
        dto.setWarehouseName("TATA");

        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "  \"id\" : 1," +
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
                        "  \"warehouse\" : {\"id\":2,\"name\":\"TATA\",\"resellerCode\":\"TATA\"}," +
                        "  \"category\" : \"ANCILLARY\"," +
                        "  \"events\" : null," +
                        "  \"macAddress\" : \"00:0A:95:9D:68:14\"," +
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
                        "      \"id\" : 1," +
                        "      \"name\" : \"Toto\"," +
                        "      \"accessType\" : \"DOCSIS\"" +
                        "    }," +
                        "    \"accessType\" : \"DOCSIS\"," +
                        "    \"category\" : \"ANCILLARY\"" +
                        "  }" +
                        "}"));
    }


    @Test
    void put_missingWarehouse() throws Exception {
        final long id = 1L;
        UpdateAncillaryEquipmentDTOV2 dto = new UpdateAncillaryEquipmentDTOV2();
        dto.setIndependent(true);
        dto.setMacAddress("00:0A:95:9D:68:14");
        dto.setWarehouseName("TITI");

        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Warehouse with name 'TITI' not found\"}"));
    }


    @Test
    void put_success_with_pairedEquipement() throws Exception {
        UpdateAncillaryEquipmentDTOV2 dto = new UpdateAncillaryEquipmentDTOV2();
        dto.setIndependent(true);
        dto.setPairedEquipmentId(6L);
        dto.setMacAddress("00:0A:95:9D:68:14");

        mockMvc.perform(put(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Paired equipment ID cannot be filled for independent ancillary when status is not ACTIVATED or ASSIGNED\"}"));
    }


    @Test
    void patch_success_with_nonNull_pairedEquipment_And_null_independentMode() throws Exception {

        final long id = 1L;
        UpdateAncillaryEquipmentDTOV2 dto = new UpdateAncillaryEquipmentDTOV2();
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
                        "  \"id\" : 1," +
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
                        "  \"warehouse\" : {" +
                        "    \"id\" : 1," +
                        "    \"name\" : \"TOTO\"," +
                        "    \"resellerCode\" : \"TOTO\"" +
                        "  }," +
                        "  \"category\" : \"ANCILLARY\"," +
                        "  \"events\" : null," +
                        "  \"macAddress\" : \"00:0A:95:9D:68:14\"," +
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
                        "      \"id\" : 1," +
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

        UpdateAncillaryEquipmentDTOV2 dto = new UpdateAncillaryEquipmentDTOV2();
        dto.setIndependent(true);
        dto.setMacAddress("00:0A:95:9D:68:14");
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "  \"id\" : 1," +
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
                        "  \"warehouse\" : {" +
                        "    \"id\" : 1," +
                        "    \"name\" : \"TOTO\"," +
                        "    \"resellerCode\" : \"TOTO\"" +
                        "  }," +
                        "  \"category\" : \"ANCILLARY\"," +
                        "  \"events\" : null," +
                        "  \"macAddress\" : \"00:0A:95:9D:68:14\"," +
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
                        "      \"id\" : 1," +
                        "      \"name\" : \"Toto\"," +
                        "      \"accessType\" : \"DOCSIS\"" +
                        "    }," +
                        "    \"accessType\" : \"DOCSIS\"," +
                        "    \"category\" : \"ANCILLARY\"" +
                        "  }" +
                        "}"));
    }

    @Test
    void patch_successWithServiceId() throws Exception {
        UpdateAncillaryEquipmentDTOV2 dto = new UpdateAncillaryEquipmentDTOV2();
        dto.setServiceId(99L);

        mockMvc.perform(patch(baseUrl + "/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "  \"id\": 3," +
                        "  \"orderId\": null," +
                        "  \"serviceId\": 99," +
                        "  \"serialNumber\": \"GFAB02600019\"," +
                        "  \"externalNumber\": null," +
                        "  \"accessType\": \"DOCSIS\"," +
                        "  \"activity\": null," +
                        "  \"activationDate\": null," +
                        "  \"assignmentDate\": \"2021-01-18T08:45:58\"," +
                        "  \"status\": \"ASSIGNED\"," +
                        "  \"nature\": \"MAIN\"," +
                        "  \"recyclable\": true," +
                        "  \"preactivated\": true," +
                        "  \"batchNumber\": null," +
                        "  \"warehouse\": {" +
                        "    \"id\": 1," +
                        "    \"name\": \"TOTO\"," +
                        "    \"resellerCode\": \"TOTO\"" +
                        "  }," +
                        "  \"category\": \"ANCILLARY\"," +
                        "  \"events\": null," +
                        "  \"macAddress\": \"00:0A:95:9D:68:16\"," +
                        "  \"independent\": true," +
                        "  \"pairedEquipmentId\": null," +
                        "  \"pairedEquipmentCategory\": null," +
                        "  \"equipmentName\": \"BRDBOX\"," +
                        "  \"sfpVersion\": null," +
                        "  \"numberRecycles\": 0," +
                        "  \"model\": {" +
                        "    \"id\": 3," +
                        "    \"name\": \"HDD500\"," +
                        "    \"currentFirmware\": \" test-4\"," +
                        "    \"provider\": {" +
                        "      \"id\": 1," +
                        "      \"name\": \"Toto\"," +
                        "      \"accessType\": \"DOCSIS\"" +
                        "    }," +
                        "    \"accessType\": \"DOCSIS\"," +
                        "    \"category\": \"ANCILLARY\"" +
                        "  }" +
                        "}"));
    }

    @Test
    void patch_failureWithServiceIdAlreadyUsed() throws Exception {
        UpdateAncillaryEquipmentDTOV2 dto = new UpdateAncillaryEquipmentDTOV2();
        dto.setServiceId(3L);

        mockMvc.perform(patch(baseUrl + "/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Service ID '3' is already used by ancillary with SN 'GFAB02600022'\"}"));
    }

    @Test
    void patch_failureWithServiceIdIfNotIndependent() throws Exception {
        UpdateAncillaryEquipmentDTOV2 dto = new UpdateAncillaryEquipmentDTOV2();
        dto.setServiceId(99L);
        dto.setPairedEquipmentId(7L);

        mockMvc.perform(patch(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Service ID cannot be set on ancillary with SN 'GFAB02600017', it needs to be both independent and ACTIVATED/ASSIGNED\"}"));
    }

    @Test
    void patch_failureWithServiceIdIfBadStatus() throws Exception {
        UpdateAncillaryEquipmentDTOV2 dto = new UpdateAncillaryEquipmentDTOV2();
        dto.setServiceId(99L);

        mockMvc.perform(patch(baseUrl + "/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Service ID cannot be set on ancillary with SN 'GFAB02600021', it needs to be both independent and ACTIVATED/ASSIGNED\"}"));
    }

    @Test
    void search_ancillaryCRUD_noParameters() throws Exception {
        SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();

        final String requestJson = objectMapper.writeValueAsString(searchAncillaryEquipmentDTO);

        mockMvc.perform(get(baseUrl + "?page=1&size=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(2))
                .andExpect(jsonPath("$.pageable.pageNumber").value(1))
                .andExpect(jsonPath("$.totalElements").value(6));
    }

    @Test
    void search_ancillaryCRUD_independent() throws Exception {
        SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
        searchAncillaryEquipmentDTO.setIndependent(true);

        final String requestJson = objectMapper.writeValueAsString(searchAncillaryEquipmentDTO);

        mockMvc.perform(get(baseUrl + "?page=1&size=1&independent=true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(3))
                .andExpect(jsonPath("$.pageable.pageNumber").value(1))
                .andExpect(jsonPath("$.totalElements").value(5));
    }

    @Test
    void search_ancillaryCRUD_notIndependent() throws Exception {
        SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
        searchAncillaryEquipmentDTO.setIndependent(false);

        final String requestJson = objectMapper.writeValueAsString(searchAncillaryEquipmentDTO);

        mockMvc.perform(get(baseUrl + "?page=0&size=1&independent=false")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.totalElements").value(1));
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
                .andExpect(content().json("{\"id\" : 4,\"serialNumber\" : \"GFAB02600020\",\"externalNumber\" : \"37799900002\",\"accessType\" : \"FTTH\",\"activity\" : null,\"status\" : \"AVAILABLE\",\"nature\" : \"MAIN\",\"recyclable\" : false,\"preactivated\" : true,\"batchNumber\" : null,\"warehouse\" : {\"id\" : 1,\"name\" : \"TOTO\",\"resellerCode\" : \"TOTO\"},\"category\" : \"ANCILLARY\",\"events\" : null,\"macAddress\" : \"00:0A:95:9D:68:17\",\"pairedEquipmentId\" : null,\"independent\" : true,\"pairedEquipmentCategory\" : null,\"equipmentName\" : \"HDD\",\"sfpVersion\" : null,\"numberRecycles\" : 1,\"model\" : {\"id\" : 2,\"name\" : \"sagem\",\"currentFirmware\" : \" test-3\",\"provider\" : {\"id\" : 1,\"name\" : \"Toto\",\"accessType\" : \"DOCSIS\"},\"accessType\" : \"DOCSIS\",\"category\" : \"ANCILLARY\"}}"));
    }

    @Test
    void setEventOnAncillary_selfEvent_success() throws Exception {
        AncillaryChangeStateDTO dto = new AncillaryChangeStateDTO();
        dto.setServiceId(5L);
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch(baseUrl + "/3/{event}", Event.assign)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{  \"id\" : 3,  \"orderId\" : null, \"serviceId\": 5,  \"serialNumber\" : \"GFAB02600019\",  \"externalNumber\" : null,  \"accessType\" : \"DOCSIS\",  \"activity\" : null,  \"status\" : \"ASSIGNED\",  \"nature\" : \"MAIN\",  \"recyclable\" : true,  \"preactivated\" : true,  \"batchNumber\" : null,   \"warehouse\" : {   \"id\" : 1,   \"name\" : \"TOTO\",   \"resellerCode\" : \"TOTO\"  },  \"category\" : \"ANCILLARY\",  \"events\" : null,  \"macAddress\" : \"00:0A:95:9D:68:16\",   \"pairedEquipmentId\" : 1,  \"independent\" : true,  \"pairedEquipmentCategory\" : \"ANCILLARY\",  \"equipmentName\" : \"BRDBOX\",  \"sfpVersion\" : null,  \"numberRecycles\" : 0,  \"model\" : {   \"id\" : 3,   \"name\" : \"HDD500\",   \"currentFirmware\" : \" test-4\",   \"provider\" : {    \"id\" : 1,    \"name\" : \"Toto\",    \"accessType\" : \"DOCSIS\"   },   \"accessType\" : \"DOCSIS\",   \"category\" : \"ANCILLARY\"  } }"));
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
                .andExpect(jsonPath("$.errorMessage").value("Cannot apply event 'book' on Ancillary Equipment with status 'DEACTIVATED' and Serial Number 'GFAB02600020'"));
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
                .andExpect(content().json("{\"id\" : 5,\"orderId\" : \"1L\",\"serialNumber\" : \"GFAB02600021\",\"externalNumber\" : null,\"accessType\" : \"DOCSIS\",\"activity\" : null,\"activationDate\" : null,\"assignmentDate\" : null,\"status\" : \"BOOKED\",\"nature\" : \"ADDITIONAL\",\"recyclable\" : false,\"preactivated\" : true,\"batchNumber\" : null,\"warehouse\" : {\"id\" : 1,\"name\" : \"TOTO\",\"resellerCode\" : \"TOTO\"},\"category\" : \"ANCILLARY\",\"events\" : null,\"macAddress\" : \"00:0A:95:9D:68:18\",\"pairedEquipmentId\" : null,\"independent\" : true,\"pairedEquipmentCategory\" : null,\"equipmentName\" : \"BRDBOX\",\"sfpVersion\" : \"PO2020071700001\",\"numberRecycles\" : 0,\"model\" : {\"id\" : 1,\"name\" : \"test\",\"currentFirmware\" : \" test-2\",\"provider\" : {\"id\" : 1,\"name\" : \"Toto\",\"accessType\" : \"DOCSIS\"},\"accessType\" : \"DOCSIS\",\"category\" : \"ANCILLARY\"}}"));
    }

    @Test
    void setEventOnAncillary_activate_success() throws Exception {
        AncillaryChangeStateDTO dto = new AncillaryChangeStateDTO();

        mockMvc.perform(patch(baseUrl + "/3/{event}", Event.activate)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\" : 3,\"orderId\" : null, \"serviceId\":  5,\"serialNumber\" : \"GFAB02600019\",\"externalNumber\" : null,\"accessType\" : \"DOCSIS\",\"activity\" : null,\"activationDate\" : \"2020-05-31T20:35:24\",\"status\" : \"ACTIVATED\",\"nature\" : \"MAIN\",\"recyclable\" : true,\"preactivated\" : true,\"batchNumber\" : null,\"warehouse\" : {\"id\" : 1,\"name\" : \"TOTO\",\"resellerCode\" : \"TOTO\"},\"category\" : \"ANCILLARY\",\"events\" : null,\"macAddress\" : \"00:0A:95:9D:68:16\",\"pairedEquipmentId\" : 1,\"independent\" : true,\"pairedEquipmentCategory\" : \"ANCILLARY\",\"equipmentName\" : \"BRDBOX\",\"sfpVersion\" : null,\"numberRecycles\" : 0,\"model\" : {\"id\" : 3,\"name\" : \"HDD500\",\"currentFirmware\" : \" test-4\",\"provider\" : {\"id\" : 1,\"name\" : \"Toto\",\"accessType\" : \"DOCSIS\"},\"accessType\" : \"DOCSIS\",\"category\" : \"ANCILLARY\"}}"));
    }

    @Test
    void setEventOnAncillary_activate_failureIfNotIndependent() throws Exception {
        AncillaryChangeStateDTO dto = new AncillaryChangeStateDTO();
        dto.setServiceId(6L);

        mockMvc.perform(patch(baseUrl + "/1/{event}", Event.activate)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Ancillary Equipment with ID '1' must be independent\"}"));
    }

    @Test
    void setEventOnAncillary_activate_successWithServiceId() throws Exception {
        AncillaryChangeStateDTO dto = new AncillaryChangeStateDTO();
        dto.setServiceId(6L);

        mockMvc.perform(patch(baseUrl + "/5/{event}", Event.book))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(patch(baseUrl + "/5/{event}", Event.activate)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.status").value(ACTIVATED.name()))
                .andExpect(jsonPath("$.serviceId").value(6L));
    }

    @Test
    void setEventOnAncillary_activate_failureWithServiceIdAlreadyUsed() throws Exception {
        AncillaryChangeStateDTO dto = new AncillaryChangeStateDTO();
        dto.setServiceId(5L);

        mockMvc.perform(patch(baseUrl + "/5/{event}", Event.book))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(patch(baseUrl + "/5/{event}", Event.assign)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Service ID '5' is already used by ancillary with SN 'GFAB02600019'\"}"));
    }

    @Test
    void setEventOnAncillary_activate_failureIfDifferentServiceId() throws Exception {
        AncillaryChangeStateDTO dto = new AncillaryChangeStateDTO();
        dto.setServiceId(6L);

        mockMvc.perform(patch(baseUrl + "/3/{event}", Event.activate)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Provided service ID '6' is not the same as current Ancillary service ID '5'\"}"));
    }

    @Test
    void setEventOnAncillary_noBody_success() throws Exception {
        mockMvc.perform(patch(baseUrl + "/5/{event}", Event.book)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\" : 5,\"orderId\" : null,\"serialNumber\" : \"GFAB02600021\",\"externalNumber\" : null,\"accessType\" : \"DOCSIS\",\"activity\" : null,\"activationDate\" : null,\"assignmentDate\" : null,\"status\" : \"BOOKED\",\"nature\" : \"ADDITIONAL\",\"recyclable\" : false,\"preactivated\" : true,\"batchNumber\" : null,\"warehouse\" : {\"id\" : 1,\"name\" : \"TOTO\",\"resellerCode\" : \"TOTO\"},\"category\" : \"ANCILLARY\",\"events\" : null,\"macAddress\" : \"00:0A:95:9D:68:18\",\"pairedEquipmentId\" : null,\"independent\" : true,\"pairedEquipmentCategory\" : null,\"equipmentName\" : \"BRDBOX\",\"sfpVersion\" : \"PO2020071700001\",\"numberRecycles\" : 0,\"model\" : {\"id\" : 1,\"name\" : \"test\",\"currentFirmware\" : \" test-2\",\"provider\" : {\"id\" : 1,\"name\" : \"Toto\",\"accessType\" : \"DOCSIS\"},\"accessType\" : \"DOCSIS\",\"category\" : \"ANCILLARY\"}}"));
    }

    @Test
    void setEventOnAncillary_rollbackOnhold_success() throws Exception {
        final long ancillaryEq = 3L;

        // Set the pairedEquipment
        mockMvc.perform(patch(baseUrl + "/{ancillaryEq}", ancillaryEq)
                        .content("{\"pairedEquipmentId\": 6, \"recyclable\": true, \"serviceId\": 8}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ASSIGNED"))
                .andExpect(jsonPath("$.serviceId").value(8L))
                .andExpect(jsonPath("$.pairedEquipmentId").value(6L))
                .andExpect(jsonPath("$.pairedEquipmentCategory").value("SIMCARD"));

        // Onhold -> Paired equipment should be removed
        mockMvc.perform(patch(baseUrl + "/{ancillaryEq}/{event}", ancillaryEq, Event.onhold)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ONHOLD"))
                .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.pairedEquipmentId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.pairedEquipmentCategory").value(IsNull.nullValue()));

        // Rollback Onhold -> Paired equipment should be back
        mockMvc.perform(patch(baseUrl + "/{ancillaryEq}/{event}", ancillaryEq, Event.rollback_onhold)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVATED"))
                .andExpect(jsonPath("$.serviceId").value(8L))
                .andExpect(jsonPath("$.pairedEquipmentId").value(6L))
                .andExpect(jsonPath("$.pairedEquipmentCategory").value("SIMCARD"));
    }

    @Test
    void setEventOnAncillary_rollbackDeactivate_successWithoutPairedEquipment() throws Exception {
        final long ancillaryEq = 5L;

        // Set ancillary as BOOKED
        mockMvc.perform(patch(baseUrl + "/{ancillaryEq}/{event}", ancillaryEq, Event.book))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("BOOKED"));

        // Set ancillary as ACTIVATED
        mockMvc.perform(patch(baseUrl + "/{ancillaryEq}/{event}", ancillaryEq, Event.activate)
                        .content("{\"serviceId\": 951}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVATED"))
                .andExpect(jsonPath("$.serviceId").value(951))
                .andExpect(jsonPath("$.pairedEquipmentId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.pairedEquipmentCategory").value(IsNull.nullValue()));

        // Onhold -> Paired equipment should be removed
        mockMvc.perform(patch(baseUrl + "/{ancillaryEq}/{event}", ancillaryEq, Event.deactivate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DEACTIVATED"))
                .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.pairedEquipmentId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.pairedEquipmentCategory").value(IsNull.nullValue()));

        // Rollback Onhold -> ServiceID should be back
        mockMvc.perform(patch(baseUrl + "/{ancillaryEq}/{event}", ancillaryEq, Event.rollback_deactivate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVATED"))
                .andExpect(jsonPath("$.serviceId").value(951))
                .andExpect(jsonPath("$.pairedEquipmentId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.pairedEquipmentCategory").value(IsNull.nullValue()));
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
                .andExpect(jsonPath("$.errorMessage").value("Unable to 'rollback_onhold' equipment with serial number 'GFAB02600019' as its old paired equipment with internal ID '6' cannot be found"));

        Mockito.reset(equipmentRepository);
    }

    @Test
    void getByPairedEquipmentSerial_success() throws Exception {

        final String serialNumber = "GFAB02600017";

        mockMvc.perform(get(baseUrl + "/pairedEquipment/{serialNumber}", serialNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "  \"id\" : 3," +
                        "  \"orderId\" : null," +
                        "  \"serialNumber\" : \"GFAB02600019\"," +
                        "  \"externalNumber\" : null," +
                        "  \"accessType\" : \"DOCSIS\"," +
                        "  \"activity\" : null," +
                        "  \"activationDate\" : null," +
                        "  \"assignmentDate\" : \"2021-01-18T08:45:58\"," +
                        "  \"status\" : \"ASSIGNED\"," +
                        "  \"nature\" : \"MAIN\"," +
                        "  \"recyclable\" : true," +
                        "  \"preactivated\" : true," +
                        "  \"batchNumber\" : null," +
                        "  \"warehouse\" : {" +
                        "    \"id\" : 1," +
                        "    \"name\" : \"TOTO\"," +
                        "    \"resellerCode\" : \"TOTO\"" +
                        "  }," +
                        "  \"category\" : \"ANCILLARY\"," +
                        "  \"events\" : [ \"unassign\", \"activate\", \"onhold\" ]," +
                        "  \"macAddress\" : \"00:0A:95:9D:68:16\"," +
                        "  \"pairedEquipmentId\" : 1," +
                        "  \"independent\" : true," +
                        "  \"pairedEquipmentCategory\" : \"ANCILLARY\"," +
                        "  \"equipmentName\" : \"BRDBOX\"," +
                        "  \"sfpVersion\" : null," +
                        "  \"numberRecycles\" : 0," +
                        "  \"model\" : {" +
                        "    \"id\" : 3," +
                        "    \"name\" : \"HDD500\"," +
                        "    \"currentFirmware\" : \" test-4\"," +
                        "    \"provider\" : {" +
                        "      \"id\" : 1," +
                        "      \"name\" : \"Toto\"," +
                        "      \"accessType\" : \"DOCSIS\"" +
                        "    }," +
                        "    \"accessType\" : \"DOCSIS\"," +
                        "    \"category\" : \"ANCILLARY\"" +
                        "  }" +
                        "}"));
    }

    @Test
    void getByPairedEquipmentSerial_failed() throws Exception {

        final String serialNumber = "GFAB03600014";

        mockMvc.perform(get(baseUrl + "/pairedEquipment/{serialNumber}", serialNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"Ancillary equipment with pairedEquipment-serialNumber 'GFAB03600014' could not be found\"}"));
    }

    @Test
    void getRevisions_success() throws Exception {
        mockMvc.perform(get(baseUrl + "/1/revisions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[{\"date\":\"2020-02-25T10:52:37\",\"entity\":{\"id\":1,\"orderId\":null,\"serviceId\":null,\"serialNumber\":\"GFAB02600070\",\"externalNumber\":null,\"accessType\":\"FTTH\",\"activity\":null,\"activationDate\":null,\"assignmentDate\":null,\"status\":\"AVAILABLE\",\"nature\":\"MAIN\",\"recyclable\":true,\"preactivated\":false,\"batchNumber\":\"20001\",\"warehouse\":{\"id\":1,\"name\":\"TOTO\",\"resellerCode\":\"TOTO\"},\"category\":\"ANCILLARY\",\"events\":null,\"macAddress\":\"6C:99:61:00:02:01\",\"independent\":true,\"pairedEquipmentId\":null,\"pairedEquipmentCategory\":null,\"equipmentName\":\"ONT\",\"sfpVersion\":\"No SFP module \",\"numberRecycles\":null,\"model\":null},\"author\":\"g.,fantappie\"}],\"pageable\":{\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"offset\":0,\"pageNumber\":0,\"pageSize\":20,\"unpaged\":false,\"paged\":true},\"last\":true,\"totalElements\":1,\"totalPages\":1,\"size\":20,\"number\":0,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"first\":true,\"numberOfElements\":1,\"empty\":false}"));
    }

    @Test
    void getRevisions_missing() throws Exception {
        mockMvc.perform(get(baseUrl + "/99/revisions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[],\"pageable\":{\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"offset\":0,\"pageNumber\":0,\"pageSize\":20,\"paged\":true,\"unpaged\":false},\"last\":true,\"totalPages\":0,\"totalElements\":0,\"number\":0,\"size\":20,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"first\":true,\"numberOfElements\":0,\"empty\":true}"));
    }
}
