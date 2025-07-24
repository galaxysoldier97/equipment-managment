package mc.monacotelecom.tecrep.equipments.integration;

import mc.monacotelecom.tecrep.equipments.dto.request.ChangeStatusDto;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddSimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateSimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.ProviderDTOV2;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.Activity;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sql/clean.sql", "/sql/simcard_data.sql"})
class SimCardIntegrationV2Test extends BaseIntegrationTest {

    final String baseUrl = "/api/v2/private/auth/simcards";

    @Nested
    @DisplayName("GetById")
    class GetById {
        @Test
        void getById_success() throws Exception {
            final long id = 1L;

            mockMvc.perform(get(baseUrl + "/{simcardId}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.orderId").value("1"))
                    .andExpect(jsonPath("$.serialNumber").value("893771033000000007"))
                    .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.activity").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.activationDate").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.assignmentDate").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.status").value("BOOKED"))
                    .andExpect(jsonPath("$.nature").value("MAIN"))
                    .andExpect(jsonPath("$.recyclable").value(false))
                    .andExpect(jsonPath("$.preactivated").value(false))
                    .andExpect(jsonPath("$.batchNumber").value("1"))
                    .andExpect(jsonPath("$.provider.id").value(1))
                    .andExpect(jsonPath("$.provider.name").value("Toto"))
                    .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.warehouse.id").value(2))
                    .andExpect(jsonPath("$.warehouse.name").value("TATA"))
                    .andExpect(jsonPath("$.warehouse.resellerCode").value("TATA"))
                    .andExpect(jsonPath("$.category").value("SIMCARD"))
                    .andExpect(jsonPath("$.events", hasSize(4)))
                    .andExpect(jsonPath("$.events[0]").value("free"))
                    .andExpect(jsonPath("$.events[1]").value("assign"))
                    .andExpect(jsonPath("$.events[2]").value("activate"))
                    .andExpect(jsonPath("$.events[3]").value("deactivate"))
                    .andExpect(jsonPath("$.imsiNumber").value("123456789012345"))
                    .andExpect(jsonPath("$.imsiSponsorNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.puk1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.puk2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.plmn.id").value(1))
                    .andExpect(jsonPath("$.plmn.code").value("12345"))
                    .andExpect(jsonPath("$.plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.packId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.transportKey").value("0"))
                    .andExpect(jsonPath("$.algorithmVersion").value(0))
                    .andExpect(jsonPath("$.checkDigit").value(0))
                    .andExpect(jsonPath("$.inventoryPool.id").value(1))
                    .andExpect(jsonPath("$.inventoryPool.code").value("1234"))
                    .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.allotmentId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.esim").value(false))
                    .andExpect(jsonPath("$.otaSalt").value(IsNull.nullValue()));
        }

        @Test
        void getById_successWithAllotment() throws Exception {
            final long id = 3L;

            mockMvc.perform(get(baseUrl + "/{simcardId}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(3))
                    .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.serialNumber").value("893771033000000009"))
                    .andExpect(jsonPath("$.externalNumber").value("37799900001"))
                    .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.status").value("DEACTIVATED"))
                    .andExpect(jsonPath("$.nature").value("MAIN"))
                    .andExpect(jsonPath("$.recyclable").value(false))
                    .andExpect(jsonPath("$.preactivated").value(true))
                    .andExpect(jsonPath("$.batchNumber").value("1"))
                    .andExpect(jsonPath("$.provider.id").value(2))
                    .andExpect(jsonPath("$.provider.name").value("Tata"))
                    .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.warehouse.id").value(1))
                    .andExpect(jsonPath("$.warehouse.name").value("TOTO"))
                    .andExpect(jsonPath("$.warehouse.resellerCode").value("TOTO"))
                    .andExpect(jsonPath("$.category").value("SIMCARD"))
                    .andExpect(jsonPath("$.events", hasSize(2)))
                    .andExpect(jsonPath("$.events[0]").value("rollback_deactivate"))
                    .andExpect(jsonPath("$.events[1]").value("available"))
                    .andExpect(jsonPath("$.imsiNumber").value("123456789012347"))
                    .andExpect(jsonPath("$.imsiSponsorNumber").value("123456"))
                    .andExpect(jsonPath("$.puk1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.puk2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.plmn.id").value(1))
                    .andExpect(jsonPath("$.plmn.code").value("12345"))
                    .andExpect(jsonPath("$.plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.serviceId").value(2))
                    .andExpect(jsonPath("$.packId").value("7"))
                    .andExpect(jsonPath("$.transportKey").value("0"))
                    .andExpect(jsonPath("$.algorithmVersion").value(0))
                    .andExpect(jsonPath("$.checkDigit").value(0))
                    .andExpect(jsonPath("$.inventoryPool.id").value(2))
                    .andExpect(jsonPath("$.inventoryPool.code").value("1235"))
                    .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.allotmentId").value("6"));
        }

        @Test
        void getById_notFound() throws Exception {
            final long id = 8L;
            mockMvc.perform(get(baseUrl + "/{simcardId}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().json("{\"errorMessage\":\"Simcard with ID '8' could not be found\"}"));
        }
    }

    @Nested
    @DisplayName("GetByImsi")
    class GetByImsi {
        @Test
        void getByImsi_success() throws Exception {
            final String imsi = "123456789012345";

            mockMvc.perform(get(baseUrl + "/imsi/{imsi}", imsi)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.orderId").value("1"))
                    .andExpect(jsonPath("$.serialNumber").value("893771033000000007"))
                    .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.status").value("BOOKED"))
                    .andExpect(jsonPath("$.nature").value("MAIN"))
                    .andExpect(jsonPath("$.recyclable").value(false))
                    .andExpect(jsonPath("$.preactivated").value(false))
                    .andExpect(jsonPath("$.batchNumber").value("1"))
                    .andExpect(jsonPath("$.provider.id").value(1))
                    .andExpect(jsonPath("$.provider.name").value("Toto"))
                    .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.warehouse.id").value(2))
                    .andExpect(jsonPath("$.warehouse.name").value("TATA"))
                    .andExpect(jsonPath("$.warehouse.resellerCode").value("TATA"))
                    .andExpect(jsonPath("$.category").value("SIMCARD"))
                    .andExpect(jsonPath("$.events", hasSize(4)))
                    .andExpect(jsonPath("$.events[0]").value("free"))
                    .andExpect(jsonPath("$.events[1]").value("assign"))
                    .andExpect(jsonPath("$.events[2]").value("activate"))
                    .andExpect(jsonPath("$.events[3]").value("deactivate"))
                    .andExpect(jsonPath("$.imsiNumber").value("123456789012345"))
                    .andExpect(jsonPath("$.imsiSponsorNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.puk1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.puk2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.plmn.id").value(1))
                    .andExpect(jsonPath("$.plmn.code").value("12345"))
                    .andExpect(jsonPath("$.plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.packId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.transportKey").value("0"))
                    .andExpect(jsonPath("$.algorithmVersion").value(0))
                    .andExpect(jsonPath("$.checkDigit").value(0))
                    .andExpect(jsonPath("$.inventoryPool.id").value(1))
                    .andExpect(jsonPath("$.inventoryPool.code").value("1234"))
                    .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.simProfile").value(IsNull.nullValue()));
        }

        @Test
        void getByImsi_notFound() throws Exception {
            final String imsi = "123456789012380";

            mockMvc.perform(get(baseUrl + "/imsi/{imsi}", imsi)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().json("{\"errorMessage\":\"Simcard with IMSI '123456789012380' could not be found\"}"));
        }
    }

    @Nested
    @DisplayName("GetByIccid")
    class GetByIccid {
        @Test
        void getByIccid_success() throws Exception {
            final String serialNumber = "893771033000000008";

            mockMvc.perform(get(baseUrl + "/iccid/{iccid}", serialNumber)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(2))
                    .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.serialNumber").value("893771033000000008"))
                    .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.status").value("AVAILABLE"))
                    .andExpect(jsonPath("$.nature").value("MAIN"))
                    .andExpect(jsonPath("$.recyclable").value(false))
                    .andExpect(jsonPath("$.preactivated").value(false))
                    .andExpect(jsonPath("$.batchNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.provider.id").value(1))
                    .andExpect(jsonPath("$.provider.name").value("Toto"))
                    .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.warehouse.id").value(1))
                    .andExpect(jsonPath("$.warehouse.name").value("TOTO"))
                    .andExpect(jsonPath("$.warehouse.resellerCode").value("TOTO"))
                    .andExpect(jsonPath("$.category").value("SIMCARD"))
                    .andExpect(jsonPath("$.events", hasSize(2)))
                    .andExpect(jsonPath("$.events[0]").value("instore"))
                    .andExpect(jsonPath("$.events[1]").value("book"))
                    .andExpect(jsonPath("$.imsiNumber").value("123456789012346"))
                    .andExpect(jsonPath("$.imsiSponsorNumber").value("123457"))
                    .andExpect(jsonPath("$.puk1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.puk2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.plmn.id").value(1))
                    .andExpect(jsonPath("$.plmn.code").value("12345"))
                    .andExpect(jsonPath("$.plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.packId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.transportKey").value("0"))
                    .andExpect(jsonPath("$.algorithmVersion").value(0))
                    .andExpect(jsonPath("$.checkDigit").value(0))
                    .andExpect(jsonPath("$.inventoryPool.id").value(1))
                    .andExpect(jsonPath("$.inventoryPool.code").value("1234"))
                    .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.simProfile").value(IsNull.nullValue()));
        }

        @Test
        void getByIccid_notFound() throws Exception {
            final String serialNumber = "8937710330000000011";

            mockMvc.perform(get(baseUrl + "/iccid/{iccid}", serialNumber)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().json("{\"errorMessage\":\"Simcard with ICCID '8937710330000000011' could not be found\"}"));
        }
    }

    @Nested
    @DisplayName("Delete")
    class Delete {
        @Test
        void delete_success() throws Exception {
            final long id = 3L;

            mockMvc.perform(delete(baseUrl + "/{simcardId}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @Test
        void delete_incompatibleStatus() throws Exception {
            final long id = 4L;

            mockMvc.perform(delete(baseUrl + "/{simcardId}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value("Bad Request"))
                    .andExpect(jsonPath("$.errorMessage").value("SIM Card with serial number '893771033000000010' cannot be deleted because it is 'BOOKED', it should be INSTORE, DEACTIVATED or DEPRECATED"));
        }
    }

    @Nested
    @DisplayName("Add")
    class Add {
        @Test
        void add_success() throws Exception {
            var dto = new AddSimCardDTOV2();
            dto.setSerialNumber("8937710330000000012");
            dto.setProviderName("Tata");
            dto.setImsiNumber("123456789012350");
            dto.setPuk1Code("10000003");
            dto.setPin1Code("1003");
            dto.setPuk2Code("10000003");
            dto.setPin2Code("2000");
            dto.setPlmnCode("12345");
            dto.setActivationCode("12345");
            dto.setConfirmationCode(12345);
            dto.setBrand("test_brand");

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(post(baseUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(6))
                    .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.serialNumber").value("8937710330000000012"))
                    .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessType").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.activity").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.activationDate").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.assignmentDate").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.status").value("INSTORE"))
                    .andExpect(jsonPath("$.nature").value("MAIN"))
                    .andExpect(jsonPath("$.recyclable").value(false))
                    .andExpect(jsonPath("$.preactivated").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.batchNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.warehouse").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.category").value("SIMCARD"))
                    .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.imsiNumber").value("123456789012350"))
                    .andExpect(jsonPath("$.imsiSponsorNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.puk1Code").value("10000003"))
                    .andExpect(jsonPath("$.pin1Code").value("1003"))
                    .andExpect(jsonPath("$.puk2Code").value("10000003"))
                    .andExpect(jsonPath("$.pin2Code").value("2000"))
                    .andExpect(jsonPath("$.authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.plmn.id").value(1))
                    .andExpect(jsonPath("$.plmn.code").value("12345"))
                    .andExpect(jsonPath("$.plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.provider.id").value(2))
                    .andExpect(jsonPath("$.provider.name").value("Tata"))
                    .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.packId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.transportKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.algorithmVersion").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.checkDigit").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.allotmentId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.esim").value(false))
                    .andExpect(jsonPath("$.otaSalt").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.activationCode").value("12345"))
                    .andExpect(jsonPath("$.brand").value("test_brand"))
                    .andExpect(jsonPath("$.confirmationCode").value(12345));
        }

        @Test
        void add_fails_serial_number_exists() throws Exception {
            var dto = new AddSimCardDTOV2();
            dto.setSerialNumber("893771033000000007");
            dto.setProviderName("Tata");
            dto.setImsiNumber("123456789012350");
            dto.setPuk1Code("10000003");
            dto.setPin1Code("1003");
            dto.setPuk2Code("10000003");
            dto.setPin2Code("2000");
            dto.setPlmnCode("12345");

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(post(baseUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(content().json("{\"errorMessage\": \"The serial number '893771033000000007' is already in use by SIMCARD with ID '1'\"}"));
        }
    }

    @Nested
    @DisplayName("Full update")
    class FullUpdate {
        @Test
        void put_success() throws Exception {

            final long id = 2L;
            var providerDTO = new ProviderDTOV2();
            providerDTO.setId(2L);

            UpdateSimCardDTOV2 dto = new UpdateSimCardDTOV2();
            dto.setSerialNumber("8937710330000000012");
            dto.setImsiNumber("123456789012350");
            dto.setPuk1Code("10000003");
            dto.setPin1Code("1003");
            dto.setPuk2Code("10000003");
            dto.setPin2Code("2000");
            dto.setNature(EquipmentNature.ADDITIONAL);
            dto.setWarehouseName("TATA");
            dto.setActivationCode("232323");
            dto.setConfirmationCode(2345);
            dto.setBrand("test_brand");
            dto.setQrCode("z8GvroazuixHhi6CtZtafw==");

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(put(baseUrl + "/{simcardId}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(2))
                    .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.serialNumber").value("893771033000000008"))
                    .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.status").value("AVAILABLE"))
                    .andExpect(jsonPath("$.nature").value("ADDITIONAL"))
                    .andExpect(jsonPath("$.recyclable").value(false))
                    .andExpect(jsonPath("$.preactivated").value(false))
                    .andExpect(jsonPath("$.batchNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.provider.id").value(1))
                    .andExpect(jsonPath("$.provider.name").value("Toto"))
                    .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.warehouse.id").value(2))
                    .andExpect(jsonPath("$.warehouse.name").value("TATA"))
                    .andExpect(jsonPath("$.warehouse.resellerCode").value("TATA"))
                    .andExpect(jsonPath("$.category").value("SIMCARD"))
                    .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.imsiNumber").value("123456789012350"))
                    .andExpect(jsonPath("$.imsiSponsorNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.puk1Code").value("10000003"))
                    .andExpect(jsonPath("$.pin1Code").value("1003"))
                    .andExpect(jsonPath("$.puk2Code").value("10000003"))
                    .andExpect(jsonPath("$.pin2Code").value("2000"))
                    .andExpect(jsonPath("$.authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.plmn.id").value(1))
                    .andExpect(jsonPath("$.plmn.code").value("12345"))
                    .andExpect(jsonPath("$.plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.packId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.transportKey").value("0"))
                    .andExpect(jsonPath("$.algorithmVersion").value(0))
                    .andExpect(jsonPath("$.checkDigit").value(0))
                    .andExpect(jsonPath("$.inventoryPool.id").value(1))
                    .andExpect(jsonPath("$.inventoryPool.code").value("1234"))
                    .andExpect(jsonPath("$.brand").value("test_brand"))
                    .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.qrCode").value("z8GvroazuixHhi6CtZtafw=="))
                    .andExpect(jsonPath("$.inventoryPool.simProfile").value(IsNull.nullValue()));
        }

        @Test
        void put_missingWarehouse() throws Exception {

            final long id = 2L;
            var providerDTO = new ProviderDTOV2();
            providerDTO.setId(2L);

            UpdateSimCardDTOV2 dto = new UpdateSimCardDTOV2();
            dto.setSerialNumber("8937710330000000012");
            dto.setImsiNumber("123456789012350");
            dto.setPuk1Code("10000003");
            dto.setPin1Code("1003");
            dto.setPuk2Code("10000003");
            dto.setPin2Code("2000");
            dto.setNature(EquipmentNature.ADDITIONAL);
            dto.setWarehouseName("TITI");

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(put(baseUrl + "/{simcardId}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json("{\"errorMessage\":\"Warehouse with name 'TITI' not found\"}"));
        }

        @Test
        void put_notFound() throws Exception {

            final long id = 8L;
            var providerDTO = new ProviderDTOV2();
            providerDTO.setId(2L);

            UpdateSimCardDTOV2 dto = new UpdateSimCardDTOV2();
            dto.setSerialNumber("8937710330000000012");
            dto.setImsiNumber("123456789012350");
            dto.setPuk1Code("10000003");
            dto.setPin1Code("1003");
            dto.setPuk2Code("10000003");
            dto.setPin2Code("2000");

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(put(baseUrl + "/{simcardId}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json("{\"errorMessage\": \"Simcard with ID '8' could not be found\"}"));
        }

        @Test
        void put_accessType_success() throws Exception {

            final long id = 2L;
            var providerDTO = new ProviderDTOV2();
            providerDTO.setId(2L);

            UpdateSimCardDTOV2 dto = new UpdateSimCardDTOV2();
            dto.setSerialNumber("8937710330000000012");
            dto.setImsiNumber("123456789012350");
            dto.setPuk1Code("10000003");
            dto.setPin1Code("1003");
            dto.setPuk2Code("10000003");
            dto.setPin2Code("2000");
            dto.setAccessType(AccessType.FTTH);
            dto.setNature(EquipmentNature.MAIN);
            dto.setWarehouseName("TATA");

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(put(baseUrl + "/{simcardId}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(2))
                    .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.serialNumber").value("893771033000000008"))
                    .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessType").value("FTTH"))
                    .andExpect(jsonPath("$.status").value("AVAILABLE"))
                    .andExpect(jsonPath("$.nature").value("MAIN"))
                    .andExpect(jsonPath("$.recyclable").value(false))
                    .andExpect(jsonPath("$.preactivated").value(false))
                    .andExpect(jsonPath("$.batchNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.provider.id").value(1))
                    .andExpect(jsonPath("$.provider.name").value("Toto"))
                    .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.warehouse.id").value(2))
                    .andExpect(jsonPath("$.warehouse.name").value("TATA"))
                    .andExpect(jsonPath("$.warehouse.resellerCode").value("TATA"))
                    .andExpect(jsonPath("$.category").value("SIMCARD"))
                    .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.imsiNumber").value("123456789012350"))
                    .andExpect(jsonPath("$.imsiSponsorNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.puk1Code").value("10000003"))
                    .andExpect(jsonPath("$.pin1Code").value("1003"))
                    .andExpect(jsonPath("$.puk2Code").value("10000003"))
                    .andExpect(jsonPath("$.pin2Code").value("2000"))
                    .andExpect(jsonPath("$.authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.plmn.id").value(1))
                    .andExpect(jsonPath("$.plmn.code").value("12345"))
                    .andExpect(jsonPath("$.plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.packId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.transportKey").value("0"))
                    .andExpect(jsonPath("$.algorithmVersion").value(0))
                    .andExpect(jsonPath("$.checkDigit").value(0))
                    .andExpect(jsonPath("$.inventoryPool.id").value(1))
                    .andExpect(jsonPath("$.inventoryPool.code").value("1234"))
                    .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.simProfile").value(IsNull.nullValue()));
        }
    }

    @Nested
    @DisplayName("PartialUpdate")
    class PartialUpdate {
        @Test
        void patchById_success() throws Exception {

            final long id = 2L;
            var providerDTO = new ProviderDTOV2();
            providerDTO.setId(2L);

            UpdateSimCardDTOV2 dto = new UpdateSimCardDTOV2();
            dto.setSerialNumber("8937710330000000012");
            dto.setImsiNumber("123456789012350");
            dto.setPuk1Code("10000003");
            dto.setPin1Code("1003");
            dto.setPuk2Code("10000003");
            dto.setPin2Code("2000");
            dto.setActivity(Activity.MOBILE);
            dto.setEsim(true);
            dto.setNature(EquipmentNature.ADDITIONAL);
            dto.setBrand("test_brand");
            dto.setQrCode("z8GvroazuixHhi6CtZtafw==");

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(patch(baseUrl + "/{simcardId}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(2))
                    .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.serialNumber").value("893771033000000008"))
                    .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.activity").value("MOBILE"))
                    .andExpect(jsonPath("$.status").value("AVAILABLE"))
                    .andExpect(jsonPath("$.nature").value("ADDITIONAL"))
                    .andExpect(jsonPath("$.recyclable").value(false))
                    .andExpect(jsonPath("$.preactivated").value(false))
                    .andExpect(jsonPath("$.batchNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.provider.id").value(1))
                    .andExpect(jsonPath("$.provider.name").value("Toto"))
                    .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.warehouse.id").value(1))
                    .andExpect(jsonPath("$.warehouse.name").value("TOTO"))
                    .andExpect(jsonPath("$.warehouse.resellerCode").value("TOTO"))
                    .andExpect(jsonPath("$.category").value("SIMCARD"))
                    .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.imsiNumber").value("123456789012350"))
                    .andExpect(jsonPath("$.imsiSponsorNumber").value("123457"))
                    .andExpect(jsonPath("$.puk1Code").value("10000003"))
                    .andExpect(jsonPath("$.pin1Code").value("1003"))
                    .andExpect(jsonPath("$.puk2Code").value("10000003"))
                    .andExpect(jsonPath("$.pin2Code").value("2000"))
                    .andExpect(jsonPath("$.authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.plmn.id").value(1))
                    .andExpect(jsonPath("$.plmn.code").value("12345"))
                    .andExpect(jsonPath("$.plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.packId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.transportKey").value("0"))
                    .andExpect(jsonPath("$.algorithmVersion").value(0))
                    .andExpect(jsonPath("$.checkDigit").value(0))
                    .andExpect(jsonPath("$.inventoryPool.id").value(1))
                    .andExpect(jsonPath("$.inventoryPool.code").value("1234"))
                    .andExpect(jsonPath("$.brand").value("test_brand"))
                    .andExpect(jsonPath("$.qrCode").value("z8GvroazuixHhi6CtZtafw=="))
                    .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.esim").value(true));
        }

        @Test
        void patchById_activationCode() throws Exception {

            final long id = 2L;
            var providerDTO = new ProviderDTOV2();
            providerDTO.setId(2L);

            UpdateSimCardDTOV2 dto = new UpdateSimCardDTOV2();
            dto.setActivationCode("33333");
            dto.setConfirmationCode(23456);

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(patch(baseUrl + "/{simcardId}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(2))
                    .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.serialNumber").value("893771033000000008"))
                    .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.activity").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.activationDate").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.assignmentDate").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.status").value("AVAILABLE"))
                    .andExpect(jsonPath("$.nature").value("MAIN"))
                    .andExpect(jsonPath("$.recyclable").value(false))
                    .andExpect(jsonPath("$.preactivated").value(false))
                    .andExpect(jsonPath("$.batchNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.warehouse.id").value(1))
                    .andExpect(jsonPath("$.warehouse.name").value("TOTO"))
                    .andExpect(jsonPath("$.warehouse.resellerCode").value("TOTO"))
                    .andExpect(jsonPath("$.category").value("SIMCARD"))
                    .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.imsiNumber").value("123456789012346"))
                    .andExpect(jsonPath("$.imsiSponsorNumber").value("123457"))
                    .andExpect(jsonPath("$.puk1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.puk2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.plmn.id").value(1))
                    .andExpect(jsonPath("$.plmn.code").value("12345"))
                    .andExpect(jsonPath("$.plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.provider.id").value(1))
                    .andExpect(jsonPath("$.provider.name").value("Toto"))
                    .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.packId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.transportKey").value("0"))
                    .andExpect(jsonPath("$.algorithmVersion").value(0))
                    .andExpect(jsonPath("$.checkDigit").value(0))
                    .andExpect(jsonPath("$.inventoryPool.id").value(1))
                    .andExpect(jsonPath("$.inventoryPool.code").value("1234"))
                    .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.allotmentId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.esim").value(false))
                    .andExpect(jsonPath("$.otaSalt").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.activationCode").value("33333"))
                    .andExpect(jsonPath("$.confirmationCode").value(23456));
        }


        @Test
        void patchBySerialNumber_success() throws Exception {

            final String serialNumber = "893771033000000008";
            var providerDTO = new ProviderDTOV2();
            providerDTO.setId(2L);

            UpdateSimCardDTOV2 dto = new UpdateSimCardDTOV2();
            dto.setImsiNumber("123456789012350");
            dto.setPuk1Code("10000003");
            dto.setPin1Code("1003");
            dto.setPuk2Code("10000003");
            dto.setPin2Code("2000");
            dto.setEsim(true);

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(patch(baseUrl + "/serialNumber/{serialNumber}", serialNumber)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(2))
                    .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.serialNumber").value("893771033000000008"))
                    .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.status").value("AVAILABLE"))
                    .andExpect(jsonPath("$.nature").value("MAIN"))
                    .andExpect(jsonPath("$.recyclable").value(false))
                    .andExpect(jsonPath("$.preactivated").value(false))
                    .andExpect(jsonPath("$.batchNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.provider.id").value(1))
                    .andExpect(jsonPath("$.provider.name").value("Toto"))
                    .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.warehouse.id").value(1))
                    .andExpect(jsonPath("$.warehouse.name").value("TOTO"))
                    .andExpect(jsonPath("$.warehouse.resellerCode").value("TOTO"))
                    .andExpect(jsonPath("$.category").value("SIMCARD"))
                    .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.imsiNumber").value("123456789012350"))
                    .andExpect(jsonPath("$.imsiSponsorNumber").value("123457"))
                    .andExpect(jsonPath("$.puk1Code").value("10000003"))
                    .andExpect(jsonPath("$.pin1Code").value("1003"))
                    .andExpect(jsonPath("$.puk2Code").value("10000003"))
                    .andExpect(jsonPath("$.pin2Code").value("2000"))
                    .andExpect(jsonPath("$.authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.plmn.id").value(1))
                    .andExpect(jsonPath("$.plmn.code").value("12345"))
                    .andExpect(jsonPath("$.plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.packId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.transportKey").value("0"))
                    .andExpect(jsonPath("$.algorithmVersion").value(0))
                    .andExpect(jsonPath("$.checkDigit").value(0))
                    .andExpect(jsonPath("$.inventoryPool.id").value(1))
                    .andExpect(jsonPath("$.inventoryPool.code").value("1234"))
                    .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.esim").value(true));
        }

        @Test
        void patchByImsi_success() throws Exception {

            final String imsi = "123456789012346";
            var providerDTO = new ProviderDTOV2();
            providerDTO.setId(2L);

            UpdateSimCardDTOV2 dto = new UpdateSimCardDTOV2();
            dto.setImsiNumber("123456789012350");
            dto.setPuk1Code("10000003");
            dto.setPin1Code("1003");
            dto.setPuk2Code("10000003");
            dto.setPin2Code("2000");
            dto.setEsim(true);

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(patch(baseUrl + "/imsi/{imsi}", imsi)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(2))
                    .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.serialNumber").value("893771033000000008"))
                    .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.status").value("AVAILABLE"))
                    .andExpect(jsonPath("$.nature").value("MAIN"))
                    .andExpect(jsonPath("$.recyclable").value(false))
                    .andExpect(jsonPath("$.preactivated").value(false))
                    .andExpect(jsonPath("$.batchNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.provider.id").value(1))
                    .andExpect(jsonPath("$.provider.name").value("Toto"))
                    .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.warehouse.id").value(1))
                    .andExpect(jsonPath("$.warehouse.name").value("TOTO"))
                    .andExpect(jsonPath("$.warehouse.resellerCode").value("TOTO"))
                    .andExpect(jsonPath("$.category").value("SIMCARD"))
                    .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.imsiNumber").value("123456789012350"))
                    .andExpect(jsonPath("$.imsiSponsorNumber").value("123457"))
                    .andExpect(jsonPath("$.puk1Code").value("10000003"))
                    .andExpect(jsonPath("$.pin1Code").value("1003"))
                    .andExpect(jsonPath("$.puk2Code").value("10000003"))
                    .andExpect(jsonPath("$.pin2Code").value("2000"))
                    .andExpect(jsonPath("$.authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.plmn.id").value(1))
                    .andExpect(jsonPath("$.plmn.code").value("12345"))
                    .andExpect(jsonPath("$.plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.packId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.transportKey").value("0"))
                    .andExpect(jsonPath("$.algorithmVersion").value(0))
                    .andExpect(jsonPath("$.checkDigit").value(0))
                    .andExpect(jsonPath("$.inventoryPool.id").value(1))
                    .andExpect(jsonPath("$.inventoryPool.code").value("1234"))
                    .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.esim").value(true));
        }

        @Test
        void patch_accessType_success() throws Exception {

            final long id = 2L;
            var providerDTO = new ProviderDTOV2();
            providerDTO.setId(2L);

            UpdateSimCardDTOV2 dto = new UpdateSimCardDTOV2();
            dto.setSerialNumber("8937710330000000012");
            dto.setImsiNumber("123456789012350");
            dto.setPuk1Code("10000003");
            dto.setPin1Code("1003");
            dto.setPuk2Code("10000003");
            dto.setPin2Code("2000");
            dto.setAccessType(AccessType.FTTH);

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(patch(baseUrl + "/{simcardId}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(2))
                    .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.serialNumber").value("893771033000000008"))
                    .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessType").value("FTTH"))
                    .andExpect(jsonPath("$.status").value("AVAILABLE"))
                    .andExpect(jsonPath("$.nature").value("MAIN"))
                    .andExpect(jsonPath("$.recyclable").value(false))
                    .andExpect(jsonPath("$.preactivated").value(false))
                    .andExpect(jsonPath("$.batchNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.provider.id").value(1))
                    .andExpect(jsonPath("$.provider.name").value("Toto"))
                    .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.warehouse.id").value(1))
                    .andExpect(jsonPath("$.warehouse.name").value("TOTO"))
                    .andExpect(jsonPath("$.warehouse.resellerCode").value("TOTO"))
                    .andExpect(jsonPath("$.category").value("SIMCARD"))
                    .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.imsiNumber").value("123456789012350"))
                    .andExpect(jsonPath("$.imsiSponsorNumber").value("123457"))
                    .andExpect(jsonPath("$.puk1Code").value("10000003"))
                    .andExpect(jsonPath("$.pin1Code").value("1003"))
                    .andExpect(jsonPath("$.puk2Code").value("10000003"))
                    .andExpect(jsonPath("$.pin2Code").value("2000"))
                    .andExpect(jsonPath("$.authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.plmn.id").value(1))
                    .andExpect(jsonPath("$.plmn.code").value("12345"))
                    .andExpect(jsonPath("$.plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.packId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.transportKey").value("0"))
                    .andExpect(jsonPath("$.algorithmVersion").value(0))
                    .andExpect(jsonPath("$.checkDigit").value(0))
                    .andExpect(jsonPath("$.inventoryPool.id").value(1))
                    .andExpect(jsonPath("$.inventoryPool.code").value("1234"))
                    .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.simProfile").value(IsNull.nullValue()));
        }
    }

    @Nested
    @DisplayName("UpdatePack")
    class UpdatePack {
        @Test
        void updatePackById_success() throws Exception {

            final long id = 2L;
            var providerDTO = new ProviderDTOV2();
            providerDTO.setId(2L);

            UpdateSimCardDTOV2 dto = new UpdateSimCardDTOV2();
            dto.setSerialNumber("8937710330000000012");
            dto.setImsiNumber("123456789012350");
            dto.setPuk1Code("10000003");
            dto.setPin1Code("1003");
            dto.setPuk2Code("10000003");
            dto.setPin2Code("2000");
            dto.setPackId("1SIMIONFT73601297");

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(patch(baseUrl + "/{simcardId}/pack", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(2))
                    .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.serialNumber").value("893771033000000008"))
                    .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.status").value("AVAILABLE"))
                    .andExpect(jsonPath("$.nature").value("MAIN"))
                    .andExpect(jsonPath("$.recyclable").value(false))
                    .andExpect(jsonPath("$.preactivated").value(false))
                    .andExpect(jsonPath("$.batchNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.provider.id").value(1))
                    .andExpect(jsonPath("$.provider.name").value("Toto"))
                    .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.warehouse.id").value(1))
                    .andExpect(jsonPath("$.warehouse.name").value("TOTO"))
                    .andExpect(jsonPath("$.warehouse.resellerCode").value("TOTO"))
                    .andExpect(jsonPath("$.category").value("SIMCARD"))
                    .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.imsiNumber").value("123456789012346"))
                    .andExpect(jsonPath("$.imsiSponsorNumber").value("123457"))
                    .andExpect(jsonPath("$.puk1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.puk2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.plmn.id").value(1))
                    .andExpect(jsonPath("$.plmn.code").value("12345"))
                    .andExpect(jsonPath("$.plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.packId").value("1SIMIONFT73601297"))
                    .andExpect(jsonPath("$.transportKey").value("0"))
                    .andExpect(jsonPath("$.algorithmVersion").value(0))
                    .andExpect(jsonPath("$.checkDigit").value(0))
                    .andExpect(jsonPath("$.inventoryPool.id").value(1))
                    .andExpect(jsonPath("$.inventoryPool.code").value("1234"))
                    .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.simProfile").value(IsNull.nullValue()));
        }

        @Test
        void updatePackByIMSI_success() throws Exception {

            var providerDTO = new ProviderDTOV2();
            providerDTO.setId(2L);

            UpdateSimCardDTOV2 dto = new UpdateSimCardDTOV2();
            dto.setSerialNumber("893771033000000008");
            dto.setImsiNumber("123456789012347");
            dto.setPuk1Code("10000003");
            dto.setPin1Code("1003");
            dto.setPuk2Code("10000003");
            dto.setPin2Code("2000");
            dto.setPackId("1");

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(patch(baseUrl + "/pack")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(3))
                    .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.serialNumber").value("893771033000000009"))
                    .andExpect(jsonPath("$.externalNumber").value("37799900001"))
                    .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.status").value("DEACTIVATED"))
                    .andExpect(jsonPath("$.nature").value("MAIN"))
                    .andExpect(jsonPath("$.recyclable").value(false))
                    .andExpect(jsonPath("$.preactivated").value(true))
                    .andExpect(jsonPath("$.batchNumber").value("1"))
                    .andExpect(jsonPath("$.provider.id").value(2))
                    .andExpect(jsonPath("$.provider.name").value("Tata"))
                    .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.warehouse.id").value(1))
                    .andExpect(jsonPath("$.warehouse.name").value("TOTO"))
                    .andExpect(jsonPath("$.warehouse.resellerCode").value("TOTO"))
                    .andExpect(jsonPath("$.category").value("SIMCARD"))
                    .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.imsiNumber").value("123456789012347"))
                    .andExpect(jsonPath("$.imsiSponsorNumber").value("123456"))
                    .andExpect(jsonPath("$.puk1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.puk2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.plmn.id").value(1))
                    .andExpect(jsonPath("$.plmn.code").value("12345"))
                    .andExpect(jsonPath("$.plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.serviceId").value(2))
                    .andExpect(jsonPath("$.packId").value("1"))
                    .andExpect(jsonPath("$.transportKey").value("0"))
                    .andExpect(jsonPath("$.algorithmVersion").value(0))
                    .andExpect(jsonPath("$.checkDigit").value(0))
                    .andExpect(jsonPath("$.inventoryPool.id").value(2))
                    .andExpect(jsonPath("$.inventoryPool.code").value("1235"))
                    .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.simProfile").value(IsNull.nullValue()));
        }

        @Test
        void updatePackByIMSI_ImsiNotFound() throws Exception {

            var providerDTO = new ProviderDTOV2();
            providerDTO.setId(2L);

            UpdateSimCardDTOV2 dto = new UpdateSimCardDTOV2();
            dto.setSerialNumber("893771033000000008");
            dto.setImsiNumber("123456789012350");
            dto.setPuk1Code("10000003");
            dto.setPin1Code("1003");
            dto.setPuk2Code("10000003");
            dto.setPin2Code("2000");
            dto.setPackId("1");

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(patch(baseUrl + "/pack")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json("{\"errorMessage\": \"Simcard with IMSI '123456789012350' could not be found\"}"));
        }
    }

    @Nested
    @DisplayName("Search")
    class Search {
        @Test
        void search_success() throws Exception {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("imsi", "123456789012345");

            mockMvc.perform(get(baseUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .params(map))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(1)))
                    .andExpect(jsonPath("$.content[0].id").value(1))
                    .andExpect(jsonPath("$.content[0].orderId").value("1"))
                    .andExpect(jsonPath("$.content[0].serialNumber").value("893771033000000007"))
                    .andExpect(jsonPath("$.content[0].externalNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.content[0].activity").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].activationDate").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].assignmentDate").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].status").value("BOOKED"))
                    .andExpect(jsonPath("$.content[0].nature").value("MAIN"))
                    .andExpect(jsonPath("$.content[0].recyclable").value(false))
                    .andExpect(jsonPath("$.content[0].preactivated").value(false))
                    .andExpect(jsonPath("$.content[0].batchNumber").value("1"))
                    .andExpect(jsonPath("$.content[0].warehouse.id").value(2))
                    .andExpect(jsonPath("$.content[0].warehouse.name").value("TATA"))
                    .andExpect(jsonPath("$.content[0].warehouse.resellerCode").value("TATA"))
                    .andExpect(jsonPath("$.content[0].category").value("SIMCARD"))
                    .andExpect(jsonPath("$.content[0].events", hasSize(4)))
                    .andExpect(jsonPath("$.content[0].events[0]").value("free"))
                    .andExpect(jsonPath("$.content[0].events[1]").value("assign"))
                    .andExpect(jsonPath("$.content[0].events[2]").value("activate"))
                    .andExpect(jsonPath("$.content[0].events[3]").value("deactivate"))
                    .andExpect(jsonPath("$.content[0].imsiNumber").value("123456789012345"))
                    .andExpect(jsonPath("$.content[0].imsiSponsorNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].puk1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].pin1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].puk2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].pin2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].plmn.id").value(1))
                    .andExpect(jsonPath("$.content[0].plmn.code").value("12345"))
                    .andExpect(jsonPath("$.content[0].plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.content[0].plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.content[0].plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.content[0].plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.content[0].plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.content[0].provider.id").value(1))
                    .andExpect(jsonPath("$.content[0].provider.name").value("Toto"))
                    .andExpect(jsonPath("$.content[0].provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.content[0].serviceId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].packId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].transportKey").value("0"))
                    .andExpect(jsonPath("$.content[0].algorithmVersion").value(0))
                    .andExpect(jsonPath("$.content[0].checkDigit").value(0))
                    .andExpect(jsonPath("$.content[0].inventoryPool.id").value(1))
                    .andExpect(jsonPath("$.content[0].inventoryPool.code").value("1234"))
                    .andExpect(jsonPath("$.content[0].inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].inventoryPool.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].allotmentId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].esim").value(false))
                    .andExpect(jsonPath("$.content[0].otaSalt").value(IsNull.nullValue()));
        }

        @Test
        void search_with_eSim_success() throws Exception {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("esim", "true");

            mockMvc.perform(get(baseUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .params(map))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(3)))
                    .andExpect(jsonPath("$.content[0].id").value(3))
                    .andExpect(jsonPath("$.content[0].orderId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].serialNumber").value("893771033000000009"))
                    .andExpect(jsonPath("$.content[0].externalNumber").value("37799900001"))
                    .andExpect(jsonPath("$.content[0].accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.content[0].activity").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].activationDate").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].assignmentDate").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].status").value("DEACTIVATED"))
                    .andExpect(jsonPath("$.content[0].nature").value("MAIN"))
                    .andExpect(jsonPath("$.content[0].recyclable").value(false))
                    .andExpect(jsonPath("$.content[0].preactivated").value(true))
                    .andExpect(jsonPath("$.content[0].batchNumber").value("1"))
                    .andExpect(jsonPath("$.content[0].warehouse.id").value(1))
                    .andExpect(jsonPath("$.content[0].warehouse.name").value("TOTO"))
                    .andExpect(jsonPath("$.content[0].warehouse.resellerCode").value("TOTO"))
                    .andExpect(jsonPath("$.content[0].category").value("SIMCARD"))
                    .andExpect(jsonPath("$.content[0].events", hasSize(2)))
                    .andExpect(jsonPath("$.content[0].events[0]").value("rollback_deactivate"))
                    .andExpect(jsonPath("$.content[0].events[1]").value("available"))
                    .andExpect(jsonPath("$.content[0].imsiNumber").value("123456789012347"))
                    .andExpect(jsonPath("$.content[0].imsiSponsorNumber").value("123456"))
                    .andExpect(jsonPath("$.content[0].puk1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].pin1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].puk2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].pin2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].plmn.id").value(1))
                    .andExpect(jsonPath("$.content[0].plmn.code").value("12345"))
                    .andExpect(jsonPath("$.content[0].plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.content[0].plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.content[0].plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.content[0].plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.content[0].plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.content[0].provider.id").value(2))
                    .andExpect(jsonPath("$.content[0].provider.name").value("Tata"))
                    .andExpect(jsonPath("$.content[0].provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.content[0].serviceId").value(2))
                    .andExpect(jsonPath("$.content[0].packId").value("7"))
                    .andExpect(jsonPath("$.content[0].transportKey").value("0"))
                    .andExpect(jsonPath("$.content[0].algorithmVersion").value(0))
                    .andExpect(jsonPath("$.content[0].checkDigit").value(0))
                    .andExpect(jsonPath("$.content[0].inventoryPool.id").value(2))
                    .andExpect(jsonPath("$.content[0].inventoryPool.code").value("1235"))
                    .andExpect(jsonPath("$.content[0].inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].inventoryPool.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].allotmentId").value("6"))
                    .andExpect(jsonPath("$.content[0].esim").value(true))
                    .andExpect(jsonPath("$.content[0].otaSalt").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[1].id").value(4))
                    .andExpect(jsonPath("$.content[1].orderId").value("2"))
                    .andExpect(jsonPath("$.content[1].serialNumber").value("893771033000000010"))
                    .andExpect(jsonPath("$.content[1].externalNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[1].accessType").value("FREEDHOME"))
                    .andExpect(jsonPath("$.content[1].activity").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[1].activationDate").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[1].assignmentDate").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[1].status").value("BOOKED"))
                    .andExpect(jsonPath("$.content[1].nature").value("ADDITIONAL"))
                    .andExpect(jsonPath("$.content[1].recyclable").value(false))
                    .andExpect(jsonPath("$.content[1].preactivated").value(false))
                    .andExpect(jsonPath("$.content[1].batchNumber").value("2"))
                    .andExpect(jsonPath("$.content[1].warehouse.id").value(1))
                    .andExpect(jsonPath("$.content[1].warehouse.name").value("TOTO"))
                    .andExpect(jsonPath("$.content[1].warehouse.resellerCode").value("TOTO"))
                    .andExpect(jsonPath("$.content[1].category").value("SIMCARD"))
                    .andExpect(jsonPath("$.content[1].events", hasSize(4)))
                    .andExpect(jsonPath("$.content[1].events[0]").value("free"))
                    .andExpect(jsonPath("$.content[1].events[1]").value("assign"))
                    .andExpect(jsonPath("$.content[1].events[2]").value("activate"))
                    .andExpect(jsonPath("$.content[1].events[3]").value("deactivate"))
                    .andExpect(jsonPath("$.content[1].imsiNumber").value("123456789012348"))
                    .andExpect(jsonPath("$.content[1].imsiSponsorNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[1].puk1Code").value("10015953"))
                    .andExpect(jsonPath("$.content[1].pin1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[1].puk2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[1].pin2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[1].authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[1].accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[1].simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[1].plmn.id").value(1))
                    .andExpect(jsonPath("$.content[1].plmn.code").value("12345"))
                    .andExpect(jsonPath("$.content[1].plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.content[1].plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.content[1].plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.content[1].plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.content[1].plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.content[1].provider.id").value(1))
                    .andExpect(jsonPath("$.content[1].provider.name").value("Toto"))
                    .andExpect(jsonPath("$.content[1].provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.content[1].serviceId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[1].packId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[1].transportKey").value("0"))
                    .andExpect(jsonPath("$.content[1].algorithmVersion").value(0))
                    .andExpect(jsonPath("$.content[1].checkDigit").value(0))
                    .andExpect(jsonPath("$.content[1].inventoryPool.id").value(1))
                    .andExpect(jsonPath("$.content[1].inventoryPool.code").value("1234"))
                    .andExpect(jsonPath("$.content[1].inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[1].inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[1].inventoryPool.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[1].allotmentId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[1].esim").value(true))
                    .andExpect(jsonPath("$.content[1].otaSalt").value("BCEB92ACD3E2CCC72FB4ADDD258AD981"))
                    .andExpect(jsonPath("$.content[2].id").value(5))
                    .andExpect(jsonPath("$.content[2].orderId").value("2"))
                    .andExpect(jsonPath("$.content[2].serialNumber").value("893771033000000011"))
                    .andExpect(jsonPath("$.content[2].externalNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[2].accessType").value("FREEDHOME"))
                    .andExpect(jsonPath("$.content[2].activity").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[2].activationDate").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[2].assignmentDate").value("2021-01-18T08:45:58"))
                    .andExpect(jsonPath("$.content[2].status").value("ASSIGNED"))
                    .andExpect(jsonPath("$.content[2].nature").value("ADDITIONAL"))
                    .andExpect(jsonPath("$.content[2].recyclable").value(false))
                    .andExpect(jsonPath("$.content[2].preactivated").value(false))
                    .andExpect(jsonPath("$.content[2].batchNumber").value("2"))
                    .andExpect(jsonPath("$.content[2].warehouse.id").value(1))
                    .andExpect(jsonPath("$.content[2].warehouse.name").value("TOTO"))
                    .andExpect(jsonPath("$.content[2].warehouse.resellerCode").value("TOTO"))
                    .andExpect(jsonPath("$.content[2].category").value("SIMCARD"))
                    .andExpect(jsonPath("$.content[2].events", hasSize(3)))
                    .andExpect(jsonPath("$.content[2].events[0]").value("unassign"))
                    .andExpect(jsonPath("$.content[2].events[1]").value("activate"))
                    .andExpect(jsonPath("$.content[2].events[2]").value("deactivate"))
                    .andExpect(jsonPath("$.content[2].imsiNumber").value("123456789012349"))
                    .andExpect(jsonPath("$.content[2].imsiSponsorNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[2].puk1Code").value("10015955"))
                    .andExpect(jsonPath("$.content[2].pin1Code").value("1001"))
                    .andExpect(jsonPath("$.content[2].puk2Code").value("10015954"))
                    .andExpect(jsonPath("$.content[2].pin2Code").value("1002"))
                    .andExpect(jsonPath("$.content[2].authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[2].accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[2].simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[2].plmn.id").value(1))
                    .andExpect(jsonPath("$.content[2].plmn.code").value("12345"))
                    .andExpect(jsonPath("$.content[2].plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.content[2].plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.content[2].plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.content[2].plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.content[2].plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.content[2].provider.id").value(1))
                    .andExpect(jsonPath("$.content[2].provider.name").value("Toto"))
                    .andExpect(jsonPath("$.content[2].provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.content[2].serviceId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[2].packId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[2].transportKey").value("0"))
                    .andExpect(jsonPath("$.content[2].algorithmVersion").value(0))
                    .andExpect(jsonPath("$.content[2].checkDigit").value(0))
                    .andExpect(jsonPath("$.content[2].inventoryPool.id").value(1))
                    .andExpect(jsonPath("$.content[2].inventoryPool.code").value("1234"))
                    .andExpect(jsonPath("$.content[2].inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[2].inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[2].inventoryPool.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[2].allotmentId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[2].esim").value(true))
                    .andExpect(jsonPath("$.content[2].otaSalt").value("BCEB92ACD3E2CCC72FB4ADDD258AD981"));
        }
    }

    @Nested
    @DisplayName("SetEvent")
    class SetEvent {
        @Test
        void setEventById_selfEvent_success() throws Exception {
            final long id = 1L;

            ChangeStatusDto dto = new ChangeStatusDto();
            dto.setOrderId("1L");

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(patch(baseUrl + "/{simcardId}/{event}", id, Event.book)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.orderId").value("1L"))
                    .andExpect(jsonPath("$.serialNumber").value("893771033000000007"))
                    .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.activity").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.status").value("BOOKED"))
                    .andExpect(jsonPath("$.nature").value("MAIN"))
                    .andExpect(jsonPath("$.recyclable").value(false))
                    .andExpect(jsonPath("$.preactivated").value(false))
                    .andExpect(jsonPath("$.batchNumber").value("1"))
                    .andExpect(jsonPath("$.provider.id").value(1))
                    .andExpect(jsonPath("$.provider.name").value("Toto"))
                    .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.warehouse.id").value(2))
                    .andExpect(jsonPath("$.warehouse.name").value("TATA"))
                    .andExpect(jsonPath("$.warehouse.resellerCode").value("TATA"))
                    .andExpect(jsonPath("$.category").value("SIMCARD"))
                    .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.imsiNumber").value("123456789012345"))
                    .andExpect(jsonPath("$.imsiSponsorNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.puk1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.puk2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.plmn.id").value(1))
                    .andExpect(jsonPath("$.plmn.code").value("12345"))
                    .andExpect(jsonPath("$.plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.packId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.transportKey").value("0"))
                    .andExpect(jsonPath("$.algorithmVersion").value(0))
                    .andExpect(jsonPath("$.checkDigit").value(0))
                    .andExpect(jsonPath("$.inventoryPool.id").value(1))
                    .andExpect(jsonPath("$.inventoryPool.code").value("1234"))
                    .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.allotmentId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.esim").value(false))
                    .andExpect(jsonPath("$.otaSalt").value(IsNull.nullValue()));
        }

        @Test
        void setEventById_notValid() throws Exception {
            final long id = 3L;

            ChangeStatusDto dto = new ChangeStatusDto();
            dto.setOrderId("1L");

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(patch(baseUrl + "/{simcardId}/{event}", id, Event.book)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json("{\"errorMessage\": \"Cannot apply event 'book' on SIMCARD with status 'DEACTIVATED' and ICCID '893771033000000009'\"}"));
        }

        @Test
        void setEventById_success() throws Exception {
            final long id = 2L;

            ChangeStatusDto dto = new ChangeStatusDto();
            dto.setOrderId("1L");

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(patch(baseUrl + "/{simcardId}/{event}", id, Event.book)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(2))
                    .andExpect(jsonPath("$.orderId").value("1L"))
                    .andExpect(jsonPath("$.serialNumber").value("893771033000000008"))
                    .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.status").value("BOOKED"))
                    .andExpect(jsonPath("$.nature").value("MAIN"))
                    .andExpect(jsonPath("$.recyclable").value(false))
                    .andExpect(jsonPath("$.preactivated").value(false))
                    .andExpect(jsonPath("$.batchNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.provider.id").value(1))
                    .andExpect(jsonPath("$.provider.name").value("Toto"))
                    .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.warehouse.id").value(1))
                    .andExpect(jsonPath("$.warehouse.name").value("TOTO"))
                    .andExpect(jsonPath("$.warehouse.resellerCode").value("TOTO"))
                    .andExpect(jsonPath("$.category").value("SIMCARD"))
                    .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.imsiNumber").value("123456789012346"))
                    .andExpect(jsonPath("$.imsiSponsorNumber").value("123457"))
                    .andExpect(jsonPath("$.puk1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.puk2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.plmn.id").value(1))
                    .andExpect(jsonPath("$.plmn.code").value("12345"))
                    .andExpect(jsonPath("$.plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.packId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.transportKey").value("0"))
                    .andExpect(jsonPath("$.algorithmVersion").value(0))
                    .andExpect(jsonPath("$.checkDigit").value(0))
                    .andExpect(jsonPath("$.inventoryPool.id").value(1))
                    .andExpect(jsonPath("$.inventoryPool.code").value("1234"))
                    .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.simProfile").value(IsNull.nullValue()));
        }

        @Test
        void setEventById_successWithoutBody() throws Exception {
            final long id = 2L;

            mockMvc.perform(patch(baseUrl + "/{simcardId}/{event}", id, Event.book)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(2))
                    .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.serialNumber").value("893771033000000008"))
                    .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.status").value("BOOKED"))
                    .andExpect(jsonPath("$.nature").value("MAIN"))
                    .andExpect(jsonPath("$.recyclable").value(false))
                    .andExpect(jsonPath("$.preactivated").value(false))
                    .andExpect(jsonPath("$.batchNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.provider.id").value(1))
                    .andExpect(jsonPath("$.provider.name").value("Toto"))
                    .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.warehouse.id").value(1))
                    .andExpect(jsonPath("$.warehouse.name").value("TOTO"))
                    .andExpect(jsonPath("$.warehouse.resellerCode").value("TOTO"))
                    .andExpect(jsonPath("$.category").value("SIMCARD"))
                    .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.imsiNumber").value("123456789012346"))
                    .andExpect(jsonPath("$.imsiSponsorNumber").value("123457"))
                    .andExpect(jsonPath("$.puk1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.puk2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.plmn.id").value(1))
                    .andExpect(jsonPath("$.plmn.code").value("12345"))
                    .andExpect(jsonPath("$.plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.packId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.transportKey").value("0"))
                    .andExpect(jsonPath("$.algorithmVersion").value(0))
                    .andExpect(jsonPath("$.checkDigit").value(0))
                    .andExpect(jsonPath("$.inventoryPool.id").value(1))
                    .andExpect(jsonPath("$.inventoryPool.code").value("1234"))
                    .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.simProfile").value(IsNull.nullValue()));
        }

        @Test
        void setEventById_activate_success() throws Exception {
            final long id = 5L;

            var dto = new ChangeStatusDto();
            dto.setServiceId(12344L);

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(patch(baseUrl + "/{simcardId}/{event}", id, Event.activate)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(5))
                    .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.serialNumber").value("893771033000000011"))
                    .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessType").value("FREEDHOME"))
                    .andExpect(jsonPath("$.activity").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.activationDate").value("2020-05-31T20:35:24"))
                    .andExpect(jsonPath("$.assignmentDate").value("2021-01-18T08:45:58"))
                    .andExpect(jsonPath("$.status").value("ACTIVATED"))
                    .andExpect(jsonPath("$.nature").value("ADDITIONAL"))
                    .andExpect(jsonPath("$.recyclable").value(false))
                    .andExpect(jsonPath("$.preactivated").value(false))
                    .andExpect(jsonPath("$.batchNumber").value("2"))
                    .andExpect(jsonPath("$.provider.id").value(1))
                    .andExpect(jsonPath("$.provider.name").value("Toto"))
                    .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.warehouse.id").value(1))
                    .andExpect(jsonPath("$.warehouse.name").value("TOTO"))
                    .andExpect(jsonPath("$.warehouse.resellerCode").value("TOTO"))
                    .andExpect(jsonPath("$.category").value("SIMCARD"))
                    .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.imsiNumber").value("123456789012349"))
                    .andExpect(jsonPath("$.imsiSponsorNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.puk1Code").value("10015955"))
                    .andExpect(jsonPath("$.pin1Code").value("1001"))
                    .andExpect(jsonPath("$.puk2Code").value("10015954"))
                    .andExpect(jsonPath("$.pin2Code").value("1002"))
                    .andExpect(jsonPath("$.authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.plmn.id").value(1))
                    .andExpect(jsonPath("$.plmn.code").value("12345"))
                    .andExpect(jsonPath("$.plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.serviceId").value(12344))
                    .andExpect(jsonPath("$.packId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.transportKey").value("0"))
                    .andExpect(jsonPath("$.algorithmVersion").value(0))
                    .andExpect(jsonPath("$.checkDigit").value(0))
                    .andExpect(jsonPath("$.inventoryPool.id").value(1))
                    .andExpect(jsonPath("$.inventoryPool.code").value("1234"))
                    .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.allotmentId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.esim").value(true))
                    .andExpect(jsonPath("$.otaSalt").value("BCEB92ACD3E2CCC72FB4ADDD258AD981"));
        }

        @Test
        void setEventBySerialNumber_success() throws Exception {
            final String serialNumber = "893771033000000008";

            ChangeStatusDto dto = new ChangeStatusDto();
            dto.setOrderId("1L");

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(patch(baseUrl + "/serialNumber/{serialNumber}/{event}", serialNumber, Event.book)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(2))
                    .andExpect(jsonPath("$.orderId").value("1L"))
                    .andExpect(jsonPath("$.serialNumber").value("893771033000000008"))
                    .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.status").value("BOOKED"))
                    .andExpect(jsonPath("$.nature").value("MAIN"))
                    .andExpect(jsonPath("$.recyclable").value(false))
                    .andExpect(jsonPath("$.preactivated").value(false))
                    .andExpect(jsonPath("$.batchNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.provider.id").value(1))
                    .andExpect(jsonPath("$.provider.name").value("Toto"))
                    .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.warehouse.id").value(1))
                    .andExpect(jsonPath("$.warehouse.name").value("TOTO"))
                    .andExpect(jsonPath("$.warehouse.resellerCode").value("TOTO"))
                    .andExpect(jsonPath("$.category").value("SIMCARD"))
                    .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.imsiNumber").value("123456789012346"))
                    .andExpect(jsonPath("$.imsiSponsorNumber").value("123457"))
                    .andExpect(jsonPath("$.puk1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.puk2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.plmn.id").value(1))
                    .andExpect(jsonPath("$.plmn.code").value("12345"))
                    .andExpect(jsonPath("$.plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.packId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.transportKey").value("0"))
                    .andExpect(jsonPath("$.algorithmVersion").value(0))
                    .andExpect(jsonPath("$.checkDigit").value(0))
                    .andExpect(jsonPath("$.inventoryPool.id").value(1))
                    .andExpect(jsonPath("$.inventoryPool.code").value("1234"))
                    .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.simProfile").value(IsNull.nullValue()));
        }

        @Test
        void setEventByImsi_success() throws Exception {
            final String imsi = "123456789012346";

            ChangeStatusDto dto = new ChangeStatusDto();
            dto.setOrderId("1L");

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(patch(baseUrl + "/imsi/{imsi}/{event}", imsi, Event.book)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(2))
                    .andExpect(jsonPath("$.orderId").value("1L"))
                    .andExpect(jsonPath("$.serialNumber").value("893771033000000008"))
                    .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.status").value("BOOKED"))
                    .andExpect(jsonPath("$.nature").value("MAIN"))
                    .andExpect(jsonPath("$.recyclable").value(false))
                    .andExpect(jsonPath("$.preactivated").value(false))
                    .andExpect(jsonPath("$.batchNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.provider.id").value(1))
                    .andExpect(jsonPath("$.provider.name").value("Toto"))
                    .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.warehouse.id").value(1))
                    .andExpect(jsonPath("$.warehouse.name").value("TOTO"))
                    .andExpect(jsonPath("$.warehouse.resellerCode").value("TOTO"))
                    .andExpect(jsonPath("$.category").value("SIMCARD"))
                    .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.imsiNumber").value("123456789012346"))
                    .andExpect(jsonPath("$.imsiSponsorNumber").value("123457"))
                    .andExpect(jsonPath("$.puk1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.puk2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.plmn.id").value(1))
                    .andExpect(jsonPath("$.plmn.code").value("12345"))
                    .andExpect(jsonPath("$.plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.serviceId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.packId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.transportKey").value("0"))
                    .andExpect(jsonPath("$.algorithmVersion").value(0))
                    .andExpect(jsonPath("$.checkDigit").value(0))
                    .andExpect(jsonPath("$.inventoryPool.id").value(1))
                    .andExpect(jsonPath("$.inventoryPool.code").value("1234"))
                    .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.simProfile").value(IsNull.nullValue()));
        }

        @Test
        void setEventById_notFound() throws Exception {
            final long id = 10L;

            ChangeStatusDto dto = new ChangeStatusDto();
            dto.setOrderId("1L");

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(patch(baseUrl + "/{simcardId}/{event}", id, Event.book)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json("{\"errorMessage\": \"Simcard with ID '10' could not be found\"}"));
        }
    }

    @Nested
    @DisplayName("FindEvent")
    class FindEvent {
        @Test
        void findEventForState_success() throws Exception {
            final long id = 1L;

            mockMvc.perform(get(baseUrl + "/{simcardId}/events", id, Event.book)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("[\"free\",\"assign\",\"activate\",\"deactivate\"]"));
        }

        @Test
        void findEventForState_assign_success() throws Exception {
            final long id = 1L;
            ChangeStatusDto dto = new ChangeStatusDto();
            dto.setServiceId(11L);
            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(patch(baseUrl + "/{simcardId}/{event}", id, Event.assign)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.orderId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.serialNumber").value("893771033000000007"))
                    .andExpect(jsonPath("$.externalNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.activity").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.activationDate").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.assignmentDate").value("2020-05-31T20:35:24"))
                    .andExpect(jsonPath("$.status").value("ASSIGNED"))
                    .andExpect(jsonPath("$.nature").value("MAIN"))
                    .andExpect(jsonPath("$.recyclable").value(false))
                    .andExpect(jsonPath("$.preactivated").value(false))
                    .andExpect(jsonPath("$.batchNumber").value("1"))
                    .andExpect(jsonPath("$.provider.id").value(1))
                    .andExpect(jsonPath("$.provider.name").value("Toto"))
                    .andExpect(jsonPath("$.provider.accessType").value("DOCSIS"))
                    .andExpect(jsonPath("$.warehouse.id").value(2))
                    .andExpect(jsonPath("$.warehouse.name").value("TATA"))
                    .andExpect(jsonPath("$.warehouse.resellerCode").value("TATA"))
                    .andExpect(jsonPath("$.category").value("SIMCARD"))
                    .andExpect(jsonPath("$.events").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.imsiNumber").value("123456789012345"))
                    .andExpect(jsonPath("$.imsiSponsorNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.puk1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.puk2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.pin2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.plmn.id").value(1))
                    .andExpect(jsonPath("$.plmn.code").value("12345"))
                    .andExpect(jsonPath("$.plmn.networkName").value("NET"))
                    .andExpect(jsonPath("$.plmn.tadigCode").value("1234"))
                    .andExpect(jsonPath("$.plmn.countryIsoCode").value("123"))
                    .andExpect(jsonPath("$.plmn.countryName").value("France"))
                    .andExpect(jsonPath("$.plmn.rangesPrefix").value("33"))
                    .andExpect(jsonPath("$.serviceId").value(11))
                    .andExpect(jsonPath("$.packId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.transportKey").value("0"))
                    .andExpect(jsonPath("$.algorithmVersion").value(0))
                    .andExpect(jsonPath("$.checkDigit").value(0))
                    .andExpect(jsonPath("$.inventoryPool.id").value(1))
                    .andExpect(jsonPath("$.inventoryPool.code").value("1234"))
                    .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.inventoryPool.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.allotmentId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.esim").value(false))
                    .andExpect(jsonPath("$.otaSalt").value(IsNull.nullValue()));
        }
    }

    @Nested
    @DisplayName("GetRevisions")
    class GetRevisions {
        @Test
        void getRevisions_success() throws Exception {
            mockMvc.perform(get(baseUrl + "/1/revisions"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(1)))
                    .andExpect(jsonPath("$.content[0].date").value("2020-02-25T10:52:37"))
                    .andExpect(jsonPath("$.content[0].entity.id").value(1))
                    .andExpect(jsonPath("$.content[0].entity.orderId").value("1"))
                    .andExpect(jsonPath("$.content[0].entity.serviceId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].entity.serialNumber").value("893771033000000007"))
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
                    .andExpect(jsonPath("$.content[0].entity.warehouse").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].entity.category").value("SIMCARD"))
                    .andExpect(jsonPath("$.content[0].entity.events").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].entity.imsiNumber").value("123456789012345"))
                    .andExpect(jsonPath("$.content[0].entity.imsiSponsorNumber").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].entity.puk1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].entity.pin1Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].entity.puk2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].entity.pin2Code").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].entity.authKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].entity.accessControlClass").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].entity.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].entity.plmn").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].entity.provider").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].entity.packId").value("1"))
                    .andExpect(jsonPath("$.content[0].entity.transportKey").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].entity.algorithmVersion").value(0))
                    .andExpect(jsonPath("$.content[0].entity.checkDigit").value(0))
                    .andExpect(jsonPath("$.content[0].entity.brand").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].entity.inventoryPool.id").value(1))
                    .andExpect(jsonPath("$.content[0].entity.inventoryPool.code").value("1234"))
                    .andExpect(jsonPath("$.content[0].entity.inventoryPool.description").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].entity.inventoryPool.mvno").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].entity.inventoryPool.simProfile").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].entity.allotmentId").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].entity.esim").value(false))
                    .andExpect(jsonPath("$.content[0].entity.otaSalt").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].entity.activationCode").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].entity.confirmationCode").value(IsNull.nullValue()))
                    .andExpect(jsonPath("$.content[0].author").value("g.,fantappie"));
        }

        @Test
        void getRevisions_missing() throws Exception {
            mockMvc.perform(get(baseUrl + "/99/revisions"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"content\":[],\"pageable\":{\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"offset\":0,\"pageSize\":20,\"pageNumber\":0,\"paged\":true,\"unpaged\":false},\"last\":true,\"totalElements\":0,\"totalPages\":0,\"number\":0,\"size\":20,\"sort\":{\"empty\":true,\"sorted\":false,\"unsorted\":true},\"first\":true,\"numberOfElements\":0,\"empty\":true}"));
        }
    }
}
