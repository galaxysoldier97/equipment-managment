package mc.monacotelecom.tecrep.equipments.integration;

import mc.monacotelecom.tecrep.equipments.dto.request.ChangeStatusDto;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddCPEDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchCpeDto;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateCPEDTOV2;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sql/clean.sql", "/sql/cpe_data.sql"})
class CPEIntegrationV2Test extends BaseIntegrationTest {

    final String baseUrl = "/api/v2/private/auth/cpes";

    @Test
    void getById_success() throws Exception {
        final long id = 1L;

        mockMvc.perform(get(baseUrl + "/{cpeId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderId").value("1"))
                .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.serialNumber").value("8937710330000000007"))
                .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.activity").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.activationDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.assignmentDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.status").value("BOOKED"))
                .andExpect(jsonPath("$.nature").value("MAIN"))
                .andExpect(jsonPath("$.recyclable").value(false))
                .andExpect(jsonPath("$.preactivated").value(false))
                .andExpect(jsonPath("$.batchNumber").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.warehouse.id").value(2))
                .andExpect(jsonPath("$.warehouse.name").value("TATA"))
                .andExpect(jsonPath("$.warehouse.resellerCode").value("TATA"))
                .andExpect(jsonPath("$.category").value("CPE"))
                .andExpect(jsonPath("$.events", hasSize(4)))
                .andExpect(jsonPath("$.events[0]").value("free"))
                .andExpect(jsonPath("$.events[1]").value("assign"))
                .andExpect(jsonPath("$.events[2]").value("activate"))
                .andExpect(jsonPath("$.events[3]").value("deactivate"))
                .andExpect(jsonPath("$.macAddressLan").value("00:0A:95:9D:68:20"))
                .andExpect(jsonPath("$.macAddressCpe").value("00:0A:95:9D:68:14"))
                .andExpect(jsonPath("$.macAddressRouter").value("00:0A:95:9D:68:20"))
                .andExpect(jsonPath("$.macAddressVoip").value("00:0A:95:9D:68:34"))
                .andExpect(jsonPath("$.macAddress4G").value("00:0A:95:9D:68:45"))
                .andExpect(jsonPath("$.macAddress5G").value("00:0A:95:9D:68:70"))
                .andExpect(jsonPath("$.chipsetId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.hwVersion").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.wpaKey").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.numberRecycles").value(0))
                .andExpect(jsonPath("$.model.id").value(2))
                .andExpect(jsonPath("$.model.name").value("Toti"))
                .andExpect(jsonPath("$.model.currentFirmware").value(" Toto-1"))
                .andExpect(jsonPath("$.model.provider.id").value(1))
                .andExpect(jsonPath("$.model.provider.name").value("Toto"))
                .andExpect(jsonPath("$.model.provider.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.model.accessType").value("FTTH"))
                .andExpect(jsonPath("$.model.category").value("CPE"));
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
    void getidsIn_success() throws Exception {

        List<Long> ids = List.of(1L, 2L, 3L);

        mockMvc.perform(post(baseUrl + "/equipmentIdsIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].equipmentId").value("1"))
                .andExpect(jsonPath("$.content[0].serialNumber").value("8937710330000000007"))
                .andExpect(jsonPath("$.content[0].macAddressCpe").value("00:0A:95:9D:68:14"))
                .andExpect(jsonPath("$.content[1].equipmentId").value("2"))
                .andExpect(jsonPath("$.content[1].serialNumber").value("8937710330000000008"))
                .andExpect(jsonPath("$.content[1].macAddressCpe").value("00:0A:95:9D:68:15"))
                .andExpect(jsonPath("$.content[2].equipmentId").value("3"))
                .andExpect(jsonPath("$.content[2].serialNumber").value("8937710330000000009"))
                .andExpect(jsonPath("$.content[2].macAddressCpe").value("00:0A:95:9D:68:16"));
    }

    @Test
    void getidsIn_notFound() throws Exception {

        List<Long> ids = List.of(99L, 100L, 101L);

        mockMvc.perform(post(baseUrl + "/equipmentIdsIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ids.toString()))
                .andDo(print())
                .andExpect(status().isOk());
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
                .andExpect(jsonPath("$.errorCode").value("Not Found"))
                .andExpect(jsonPath("$.errorMessage").value("CPE with ID '400' could not be found"));
    }

    @Test
    void delete_failureIfPaired() throws Exception {
        final long id = 5L;

        mockMvc.perform(delete(baseUrl + "/{cpeId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("Bad Request"))
                .andExpect(jsonPath("$.errorMessage").value("CPE with serial number '8937710330000000011' cannot be removed because it has a paired ancillary"));
    }

    @Test
    void delete_successIfPairedAndForced() throws Exception {
        final long id = 5L;

        mockMvc.perform(delete(baseUrl + "/{cpeId}?forced=true", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertTrue(ancillaryRepository.findById(6L).isEmpty(), "Paired ancillary equipment should have been removed at the same time as the CPE");
    }

    @Test
    void delete_incompatibleStatus() throws Exception {
        final long id = 1L;

        mockMvc.perform(delete(baseUrl + "/{cpeId}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("Bad Request"))
                .andExpect(jsonPath("$.errorMessage").value("CPE with serial number '8937710330000000007' cannot be deleted because it is 'BOOKED', it should be INSTORE, DEACTIVATED or DEPRECATED"));
    }

    @Test
    void search_success() throws Exception {
        SearchCpeDto searchDto = new SearchCpeDto();
        searchDto.setSerialNumber("8937710330000000007");

        final String requestJson = objectMapper.writeValueAsString(searchDto);

        mockMvc.perform(get(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].orderId").value(1))
                .andExpect(jsonPath("$.content[0].serialNumber").value("8937710330000000007"))
                .andExpect(jsonPath("$.content[0].status").value("BOOKED"));
    }

    @Test
    void add_success() throws Exception {
        var dto = new AddCPEDTOV2();
        dto.setSerialNumber("GFAB02600017");
        dto.setWarehouseName("TATA");
        dto.setModelName("Toti");

        final String requestJson = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.serialNumber").value("GFAB02600017"))
                .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.accessType").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.activity").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.activationDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.assignmentDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.status").value("INSTORE"))
                .andExpect(jsonPath("$.nature").value("MAIN"))
                .andExpect(jsonPath("$.recyclable").value(true))
                .andExpect(jsonPath("$.preactivated").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.batchNumber").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.warehouse.id").value(2))
                .andExpect(jsonPath("$.warehouse.name").value("TATA"))
                .andExpect(jsonPath("$.warehouse.resellerCode").value("TATA"))
                .andExpect(jsonPath("$.category").value("CPE"))
                .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.macAddressLan").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.macAddressCpe").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.macAddressRouter").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.macAddressVoip").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.macAddress4G").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.macAddress5G").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.chipsetId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.hwVersion").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.wpaKey").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.numberRecycles").value(0))
                .andExpect(jsonPath("$.model.id").value(2))
                .andExpect(jsonPath("$.model.name").value("Toti"))
                .andExpect(jsonPath("$.model.currentFirmware").value(" Toto-1"))
                .andExpect(jsonPath("$.model.provider.id").value(1))
                .andExpect(jsonPath("$.model.provider.name").value("Toto"))
                .andExpect(jsonPath("$.model.provider.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.model.accessType").value("FTTH"))
                .andExpect(jsonPath("$.model.category").value("CPE"));
    }

    @Test
    void put_success() throws Exception {
        final long id = 3L;

        UpdateCPEDTOV2 dto = new UpdateCPEDTOV2();
        dto.setMacAddressRouter("00:0A:95:9D:68:18");
        dto.setMacAddressVoip("00:0A:95:9D:68:36");
        dto.setMacAddressCpe("00:0A:95:9D:68:30");
        dto.setWarehouseName("TATA");

        final String requestJson = objectMapper.writeValueAsString(dto);
        mockMvc.perform(put(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.serviceId").value(2))
                .andExpect(jsonPath("$.serialNumber").value("8937710330000000009"))
                .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.accessType").value("FTTH"))
                .andExpect(jsonPath("$.activity").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.activationDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.assignmentDate").value("2021-01-18T08:45:58"))
                .andExpect(jsonPath("$.status").value("ASSIGNED"))
                .andExpect(jsonPath("$.nature").value("MAIN"))
                .andExpect(jsonPath("$.recyclable").value(false))
                .andExpect(jsonPath("$.preactivated").value(false))
                .andExpect(jsonPath("$.batchNumber").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.warehouse.id").value(2))
                .andExpect(jsonPath("$.warehouse.name").value("TATA"))
                .andExpect(jsonPath("$.warehouse.resellerCode").value("TATA"))
                .andExpect(jsonPath("$.category").value("CPE"))
                .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.macAddressLan").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.macAddressCpe").value("00:0A:95:9D:68:30"))
                .andExpect(jsonPath("$.macAddressRouter").value("00:0A:95:9D:68:18"))
                .andExpect(jsonPath("$.macAddressVoip").value("00:0A:95:9D:68:36"))
                .andExpect(jsonPath("$.macAddress4G").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.macAddress5G").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.chipsetId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.hwVersion").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.wpaKey").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.numberRecycles").value(0))
                .andExpect(jsonPath("$.model.id").value(2))
                .andExpect(jsonPath("$.model.name").value("Toti"))
                .andExpect(jsonPath("$.model.currentFirmware").value(" Toto-1"))
                .andExpect(jsonPath("$.model.provider.id").value(1))
                .andExpect(jsonPath("$.model.provider.name").value("Toto"))
                .andExpect(jsonPath("$.model.provider.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.model.accessType").value("FTTH"))
                .andExpect(jsonPath("$.model.category").value("CPE"));
    }

    @Test
    void put_missingWarehouse() throws Exception {
        final long id = 3L;

        UpdateCPEDTOV2 dto = new UpdateCPEDTOV2();
        dto.setMacAddressRouter("00:0A:95:9D:68:18");
        dto.setMacAddressVoip("00:0A:95:9D:68:36");
        dto.setMacAddressCpe("00:0A:95:9D:68:30");
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
    void put_notFound() throws Exception {
        final long id = 8L;

        UpdateCPEDTOV2 dto = new UpdateCPEDTOV2();
        dto.setMacAddressRouter("00:0A:95:9D:68:18");
        dto.setMacAddressVoip("00:0A:95:9D:68:36");
        dto.setMacAddressCpe("00:0A:95:9D:68:30");

        final String requestJson = objectMapper.writeValueAsString(dto);
        mockMvc.perform(put(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\": \"CPE with ID '8' could not be found\"}"));
    }

    @Test
    void patch_success() throws Exception {
        final long id = 3L;

        UpdateCPEDTOV2 dto = new UpdateCPEDTOV2();
        dto.setMacAddressRouter("00:0A:95:9D:68:18");
        dto.setMacAddressVoip("00:0A:95:9D:68:36");
        dto.setMacAddressCpe("00:0A:95:9D:68:30");

        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(patch(baseUrl + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.serviceId").value(2))
                .andExpect(jsonPath("$.serialNumber").value("8937710330000000009"))
                .andExpect(jsonPath("$.externalNumber").value("37799900001"))
                .andExpect(jsonPath("$.accessType").value("FTTH"))
                .andExpect(jsonPath("$.activity").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.activationDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.assignmentDate").value("2021-01-18T08:45:58"))
                .andExpect(jsonPath("$.status").value("ASSIGNED"))
                .andExpect(jsonPath("$.nature").value("MAIN"))
                .andExpect(jsonPath("$.recyclable").value(false))
                .andExpect(jsonPath("$.preactivated").value(false))
                .andExpect(jsonPath("$.batchNumber").value("1"))
                .andExpect(jsonPath("$.warehouse.id").value(1))
                .andExpect(jsonPath("$.warehouse.name").value("TOTO"))
                .andExpect(jsonPath("$.warehouse.resellerCode").value("TOTO"))
                .andExpect(jsonPath("$.category").value("CPE"))
                .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.macAddressLan").value("00:0A:95:9D:68:22"))
                .andExpect(jsonPath("$.macAddressCpe").value("00:0A:95:9D:68:30"))
                .andExpect(jsonPath("$.macAddressRouter").value("00:0A:95:9D:68:18"))
                .andExpect(jsonPath("$.macAddressVoip").value("00:0A:95:9D:68:36"))
                .andExpect(jsonPath("$.macAddress4G").value("00:0A:95:9D:68:47"))
                .andExpect(jsonPath("$.macAddress5G").value("00:0A:95:9D:68:72"))
                .andExpect(jsonPath("$.chipsetId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.hwVersion").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.wpaKey").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.numberRecycles").value(0))
                .andExpect(jsonPath("$.model.id").value(2))
                .andExpect(jsonPath("$.model.name").value("Toti"))
                .andExpect(jsonPath("$.model.currentFirmware").value(" Toto-1"))
                .andExpect(jsonPath("$.model.provider.id").value(1))
                .andExpect(jsonPath("$.model.provider.name").value("Toto"))
                .andExpect(jsonPath("$.model.provider.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.model.accessType").value("FTTH"))
                .andExpect(jsonPath("$.model.category").value("CPE"));
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
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.serviceId").value(2))
                .andExpect(jsonPath("$.serialNumber").value("8937710330000000009"))
                .andExpect(jsonPath("$.externalNumber").value("37799900001"))
                .andExpect(jsonPath("$.accessType").value("FTTH"))
                .andExpect(jsonPath("$.activity").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.activationDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.assignmentDate").value("2020-05-31T20:35:24"))
                .andExpect(jsonPath("$.status").value("ASSIGNED"))
                .andExpect(jsonPath("$.nature").value("MAIN"))
                .andExpect(jsonPath("$.recyclable").value(false))
                .andExpect(jsonPath("$.preactivated").value(false))
                .andExpect(jsonPath("$.batchNumber").value("1"))
                .andExpect(jsonPath("$.warehouse.id").value(1))
                .andExpect(jsonPath("$.warehouse.name").value("TOTO"))
                .andExpect(jsonPath("$.warehouse.resellerCode").value("TOTO"))
                .andExpect(jsonPath("$.category").value("CPE"))
                .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.macAddressLan").value("00:0A:95:9D:68:22"))
                .andExpect(jsonPath("$.macAddressCpe").value("00:0A:95:9D:68:16"))
                .andExpect(jsonPath("$.macAddressRouter").value("00:0A:95:9D:68:22"))
                .andExpect(jsonPath("$.macAddressVoip").value("00:0A:95:9D:68:36"))
                .andExpect(jsonPath("$.macAddress4G").value("00:0A:95:9D:68:47"))
                .andExpect(jsonPath("$.macAddress5G").value("00:0A:95:9D:68:72"))
                .andExpect(jsonPath("$.chipsetId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.hwVersion").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.wpaKey").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.numberRecycles").value(0))
                .andExpect(jsonPath("$.model.id").value(2))
                .andExpect(jsonPath("$.model.name").value("Toti"))
                .andExpect(jsonPath("$.model.currentFirmware").value(" Toto-1"))
                .andExpect(jsonPath("$.model.provider.id").value(1))
                .andExpect(jsonPath("$.model.provider.name").value("Toto"))
                .andExpect(jsonPath("$.model.provider.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.model.accessType").value("FTTH"))
                .andExpect(jsonPath("$.model.category").value("CPE"));
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
                .andExpect(content().json("{\"errorMessage\": \"Cannot apply event 'assign' on CPE with status 'DEPRECATED' and Serial Number '8937710330000000010'\"}"));
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
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.orderId").value("1"))
                .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.serialNumber").value("8937710330000000011"))
                .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.activity").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.activationDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.assignmentDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.status").value("AVAILABLE"))
                .andExpect(jsonPath("$.nature").value("ADDITIONAL"))
                .andExpect(jsonPath("$.recyclable").value(false))
                .andExpect(jsonPath("$.preactivated").value(false))
                .andExpect(jsonPath("$.batchNumber").value("1"))
                .andExpect(jsonPath("$.warehouse.id").value(1))
                .andExpect(jsonPath("$.warehouse.name").value("TOTO"))
                .andExpect(jsonPath("$.warehouse.resellerCode").value("TOTO"))
                .andExpect(jsonPath("$.category").value("CPE"))
                .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.macAddressLan").value("00:0A:95:9D:68:24"))
                .andExpect(jsonPath("$.macAddressCpe").value("00:0A:95:9D:68:18"))
                .andExpect(jsonPath("$.macAddressRouter").value("00:0A:95:9D:68:24"))
                .andExpect(jsonPath("$.macAddressVoip").value("00:0A:95:9D:68:38"))
                .andExpect(jsonPath("$.macAddress4G").value("00:0A:95:9D:68:49"))
                .andExpect(jsonPath("$.macAddress5G").value("00:0A:95:9D:68:74"))
                .andExpect(jsonPath("$.chipsetId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.hwVersion").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.wpaKey").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.numberRecycles").value(1))
                .andExpect(jsonPath("$.model.id").value(2))
                .andExpect(jsonPath("$.model.name").value("Toti"))
                .andExpect(jsonPath("$.model.currentFirmware").value(" Toto-1"))
                .andExpect(jsonPath("$.model.provider.id").value(1))
                .andExpect(jsonPath("$.model.provider.name").value("Toto"))
                .andExpect(jsonPath("$.model.provider.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.model.accessType").value("FTTH"))
                .andExpect(jsonPath("$.model.category").value("CPE"));
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
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.serviceId").value(1))
                .andExpect(jsonPath("$.serialNumber").value("8937710330000000007"))
                .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.activity").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.activationDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.assignmentDate").value("2020-05-31T20:35:24"))
                .andExpect(jsonPath("$.status").value("ASSIGNED"))
                .andExpect(jsonPath("$.nature").value("MAIN"))
                .andExpect(jsonPath("$.recyclable").value(false))
                .andExpect(jsonPath("$.preactivated").value(false))
                .andExpect(jsonPath("$.batchNumber").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.warehouse.id").value(2))
                .andExpect(jsonPath("$.warehouse.name").value("TATA"))
                .andExpect(jsonPath("$.warehouse.resellerCode").value("TATA"))
                .andExpect(jsonPath("$.category").value("CPE"))
                .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.macAddressLan").value("00:0A:95:9D:68:20"))
                .andExpect(jsonPath("$.macAddressCpe").value("00:0A:95:9D:68:14"))
                .andExpect(jsonPath("$.macAddressRouter").value("00:0A:95:9D:68:20"))
                .andExpect(jsonPath("$.macAddressVoip").value("00:0A:95:9D:68:34"))
                .andExpect(jsonPath("$.macAddress4G").value("00:0A:95:9D:68:45"))
                .andExpect(jsonPath("$.macAddress5G").value("00:0A:95:9D:68:70"))
                .andExpect(jsonPath("$.chipsetId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.hwVersion").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.wpaKey").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.numberRecycles").value(0))
                .andExpect(jsonPath("$.model.id").value(2))
                .andExpect(jsonPath("$.model.name").value("Toti"))
                .andExpect(jsonPath("$.model.currentFirmware").value(" Toto-1"))
                .andExpect(jsonPath("$.model.provider.id").value(1))
                .andExpect(jsonPath("$.model.provider.name").value("Toto"))
                .andExpect(jsonPath("$.model.provider.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.model.accessType").value("FTTH"))
                .andExpect(jsonPath("$.model.category").value("CPE"));
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
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.serviceId").value(2))
                .andExpect(jsonPath("$.serialNumber").value("8937710330000000009"))
                .andExpect(jsonPath("$.externalNumber").value("37799900001"))
                .andExpect(jsonPath("$.accessType").value("FTTH"))
                .andExpect(jsonPath("$.activity").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.activationDate").value("2020-05-31T20:35:24"))
                .andExpect(jsonPath("$.assignmentDate").value("2021-01-18T08:45:58"))
                .andExpect(jsonPath("$.status").value("ACTIVATED"))
                .andExpect(jsonPath("$.nature").value("MAIN"))
                .andExpect(jsonPath("$.recyclable").value(false))
                .andExpect(jsonPath("$.preactivated").value(false))
                .andExpect(jsonPath("$.batchNumber").value("1"))
                .andExpect(jsonPath("$.warehouse.id").value(1))
                .andExpect(jsonPath("$.warehouse.name").value("TOTO"))
                .andExpect(jsonPath("$.warehouse.resellerCode").value("TOTO"))
                .andExpect(jsonPath("$.category").value("CPE"))
                .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.macAddressLan").value("00:0A:95:9D:68:22"))
                .andExpect(jsonPath("$.macAddressCpe").value("00:0A:95:9D:68:16"))
                .andExpect(jsonPath("$.macAddressRouter").value("00:0A:95:9D:68:22"))
                .andExpect(jsonPath("$.macAddressVoip").value("00:0A:95:9D:68:36"))
                .andExpect(jsonPath("$.macAddress4G").value("00:0A:95:9D:68:47"))
                .andExpect(jsonPath("$.macAddress5G").value("00:0A:95:9D:68:72"))
                .andExpect(jsonPath("$.chipsetId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.hwVersion").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.wpaKey").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.numberRecycles").value(0))
                .andExpect(jsonPath("$.model.id").value(2))
                .andExpect(jsonPath("$.model.name").value("Toti"))
                .andExpect(jsonPath("$.model.currentFirmware").value(" Toto-1"))
                .andExpect(jsonPath("$.model.provider.id").value(1))
                .andExpect(jsonPath("$.model.provider.name").value("Toto"))
                .andExpect(jsonPath("$.model.provider.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.model.accessType").value("FTTH"))
                .andExpect(jsonPath("$.model.category").value("CPE"));
    }

    @Test
    void setEventOnCpe_notBody_success() throws Exception {
        final long id = 3L;

        mockMvc.perform(patch(baseUrl + "/{cpeId}/{event}", id, Event.unassign)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.serialNumber").value("8937710330000000009"))
                .andExpect(jsonPath("$.externalNumber").value("37799900001"))
                .andExpect(jsonPath("$.accessType").value("FTTH"))
                .andExpect(jsonPath("$.activity").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.activationDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.assignmentDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.status").value("AVAILABLE"))
                .andExpect(jsonPath("$.nature").value("MAIN"))
                .andExpect(jsonPath("$.recyclable").value(false))
                .andExpect(jsonPath("$.preactivated").value(false))
                .andExpect(jsonPath("$.batchNumber").value("1"))
                .andExpect(jsonPath("$.warehouse.id").value(1))
                .andExpect(jsonPath("$.warehouse.name").value("TOTO"))
                .andExpect(jsonPath("$.warehouse.resellerCode").value("TOTO"))
                .andExpect(jsonPath("$.category").value("CPE"))
                .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.macAddressLan").value("00:0A:95:9D:68:22"))
                .andExpect(jsonPath("$.macAddressCpe").value("00:0A:95:9D:68:16"))
                .andExpect(jsonPath("$.macAddressRouter").value("00:0A:95:9D:68:22"))
                .andExpect(jsonPath("$.macAddressVoip").value("00:0A:95:9D:68:36"))
                .andExpect(jsonPath("$.macAddress4G").value("00:0A:95:9D:68:47"))
                .andExpect(jsonPath("$.macAddress5G").value("00:0A:95:9D:68:72"))
                .andExpect(jsonPath("$.chipsetId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.hwVersion").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.wpaKey").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.numberRecycles").value(0))
                .andExpect(jsonPath("$.model.id").value(2))
                .andExpect(jsonPath("$.model.name").value("Toti"))
                .andExpect(jsonPath("$.model.currentFirmware").value(" Toto-1"))
                .andExpect(jsonPath("$.model.provider.id").value(1))
                .andExpect(jsonPath("$.model.provider.name").value("Toto"))
                .andExpect(jsonPath("$.model.provider.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.model.accessType").value("FTTH"))
                .andExpect(jsonPath("$.model.category").value("CPE"));
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
                .andExpect(content().json("{\"errorMessage\": \"CPE with ID '8' could not be found\"}"));
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
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.serialNumber").value("8937710330000000009"))
                .andExpect(jsonPath("$.externalNumber").value("37799900001"))
                .andExpect(jsonPath("$.accessType").value("FTTH"))
                .andExpect(jsonPath("$.activity").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.activationDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.assignmentDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.status").value("AVAILABLE"))
                .andExpect(jsonPath("$.nature").value("MAIN"))
                .andExpect(jsonPath("$.recyclable").value(false))
                .andExpect(jsonPath("$.preactivated").value(false))
                .andExpect(jsonPath("$.batchNumber").value("1"))
                .andExpect(jsonPath("$.warehouse.id").value(1))
                .andExpect(jsonPath("$.warehouse.name").value("TOTO"))
                .andExpect(jsonPath("$.warehouse.resellerCode").value("TOTO"))
                .andExpect(jsonPath("$.category").value("CPE"))
                .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.macAddressLan").value("00:0A:95:9D:68:22"))
                .andExpect(jsonPath("$.macAddressCpe").value("00:0A:95:9D:68:16"))
                .andExpect(jsonPath("$.macAddressRouter").value("00:0A:95:9D:68:22"))
                .andExpect(jsonPath("$.macAddressVoip").value("00:0A:95:9D:68:36"))
                .andExpect(jsonPath("$.macAddress4G").value("00:0A:95:9D:68:47"))
                .andExpect(jsonPath("$.macAddress5G").value("00:0A:95:9D:68:72"))
                .andExpect(jsonPath("$.chipsetId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.hwVersion").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.wpaKey").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.numberRecycles").value(0))
                .andExpect(jsonPath("$.model.id").value(2))
                .andExpect(jsonPath("$.model.name").value("Toti"))
                .andExpect(jsonPath("$.model.currentFirmware").value(" Toto-1"))
                .andExpect(jsonPath("$.model.provider.id").value(1))
                .andExpect(jsonPath("$.model.provider.name").value("Toto"))
                .andExpect(jsonPath("$.model.provider.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.model.accessType").value("FTTH"))
                .andExpect(jsonPath("$.model.category").value("CPE"));
    }

    @Test
    void getRevisions_success() throws Exception {
        mockMvc.perform(get(baseUrl + "/1/revisions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].date").value("2020-02-25T10:52:37"))
                .andExpect(jsonPath("$.content[0].entity.id").value(1))
                .andExpect(jsonPath("$.content[0].entity.orderId").value("1"))
                .andExpect(jsonPath("$.content[0].entity.serialNumber").value("8937710330000000007"))
                .andExpect(jsonPath("$.content[0].entity.externalNumber").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].entity.accessType").value("DOCSIS"))
                .andExpect(jsonPath("$.content[0].entity.activity").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].entity.activationDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].entity.assignmentDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].entity.status").value("BOOKED"))
                .andExpect(jsonPath("$.content[0].entity.nature").value("MAIN"))
                .andExpect(jsonPath("$.content[0].entity.recyclable").value(false))
                .andExpect(jsonPath("$.content[0].entity.preactivated").value(false))
                .andExpect(jsonPath("$.content[0].entity.batchNumber").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].entity.warehouse.name").value("TOTO"))
                .andExpect(jsonPath("$.content[0].entity.category").value("CPE"))
                .andExpect(jsonPath("$.content[0].entity.events").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].entity.macAddressLan").value("00:0A:95:9D:68:20"))
                .andExpect(jsonPath("$.content[0].entity.macAddressCpe").value("00:0A:95:9D:68:14"))
                .andExpect(jsonPath("$.content[0].entity.macAddressRouter").value("00:0A:95:9D:68:20"))
                .andExpect(jsonPath("$.content[0].entity.macAddressVoip").value("00:0A:95:9D:68:34"))
                .andExpect(jsonPath("$.content[0].entity.macAddress4G").value("00:0A:95:9D:68:45"))
                .andExpect(jsonPath("$.content[0].entity.macAddress5G").value("00:0A:95:9D:68:70"))
                .andExpect(jsonPath("$.content[0].entity.chipsetId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].entity.hwVersion").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].entity.wpaKey").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].entity.serviceId").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].entity.numberRecycles").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].entity.model").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].author").value("g.,fantappie"));
    }

    @Test
    void getRevisions_missing() throws Exception {
        mockMvc.perform(get(baseUrl + "/99/revisions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[],\"pageable\":{\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"pageNumber\":0,\"pageSize\":20,\"offset\":0,\"paged\":true,\"unpaged\":false},\"last\":true,\"totalElements\":0,\"totalPages\":0,\"first\":true,\"numberOfElements\":0,\"number\":0,\"size\":20,\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"empty\":true}"));
    }
}
