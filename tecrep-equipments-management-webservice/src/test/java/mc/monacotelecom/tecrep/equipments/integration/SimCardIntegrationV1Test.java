package mc.monacotelecom.tecrep.equipments.integration;

import mc.monacotelecom.tecrep.equipments.dto.PlmnDTO;
import mc.monacotelecom.tecrep.equipments.dto.ProviderDTO;
import mc.monacotelecom.tecrep.equipments.dto.SimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.ChangeStatusDto;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateSimCardDTO;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.Activity;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sql/clean.sql", "/sql/simcard_data.sql"})
class SimCardIntegrationV1Test extends BaseIntegrationTest {

    final String baseUrl = "/private/auth/simcards";

    @Nested
    @DisplayName("GetById")
    class GetById {
        @Test
        void getById_success() throws Exception {
            final long id = 1L;

            mockMvc.perform(get(baseUrl + "/{simcardId}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().json("{\"equipmentId\" : 1,\"orderId\" : \"1\",\"serialNumber\" : \"893771033000000007\",\"externalNumber\" : null,\"accessType\" : \"DOCSIS\",\"activity\" : null,\"activationDate\" : null,\"assignmentDate\" : null,\"status\" : \"BOOKED\",\"nature\" : \"MAIN\",\"recyclable\" : false,\"preactivated\" : false,\"batchNumber\" : \"1\",\"provider\" : {\"providerId\" : 1,\"name\" : \"Toto\",\"accessType\" : \"DOCSIS\"},\"warehouse\" : {\"warehouseId\" : 2,\"name\" : \"TATA\",\"resellerCode\" : \"TATA\"},\"category\" : \"SIMCARD\",\"events\" : [ \"free\", \"assign\", \"activate\", \"deactivate\" ],\"imsiNumber\" : \"123456789012345\",\"imsiSponsorNumber\" : null,\"puk1Code\" : null,\"pin1Code\" : null,\"puk2Code\" : null,\"pin2Code\" : null,\"authKey\" : null,\"accessControlClass\" : null,\"simProfile\" : null,\"number\" : null,\"plmn\" : {\"plmnId\" : 1,\"code\" : \"12345\",\"networkName\" : \"NET\",\"tadigCode\" : \"1234\",\"countryIsoCode\" : \"123\",\"countryName\" : \"France\",\"rangesPrefix\" : \"33\"},\"serviceId\" : null,\"packId\" : null,\"transportKey\" : \"0\",\"algorithmVersion\" : 0,\"checkDigit\" : 0,\"inventoryPool\" : {\"inventoryPoolId\" : 1,\"code\" : \"1234\",\"description\" : null,\"mvno\" : null,\"simProfile\" : null},\"allotmentId\" : null,\"esim\" : false,\"otaSalt\" : null}"));
        }

        @Test
        void getById_successWithAllotment() throws Exception {
            final long id = 3L;

            mockMvc.perform(get(baseUrl + "/{simcardId}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().json("{\"equipmentId\":3,\"orderId\":null,\"serialNumber\":\"893771033000000009\",\"externalNumber\":\"37799900001\",\"accessType\":\"DOCSIS\",\"status\":\"DEACTIVATED\",\"nature\":\"MAIN\",\"recyclable\":false,\"preactivated\":true,\"batchNumber\":\"1\",\"provider\":{\"providerId\":2,\"name\":\"Tata\",\"accessType\":\"DOCSIS\"},\"warehouse\":{\"warehouseId\":1,\"name\":\"TOTO\",\"resellerCode\":\"TOTO\"},\"category\":\"SIMCARD\",\"events\":[\"rollback_deactivate\",\"available\"],\"imsiNumber\":\"123456789012347\",\"imsiSponsorNumber\":\"123456\",\"puk1Code\":null,\"pin1Code\":null,\"puk2Code\":null,\"pin2Code\":null,\"authKey\":null,\"accessControlClass\":null,\"simProfile\":null,\"number\":\"456789\",\"plmn\":{\"plmnId\":1,\"code\":\"12345\",\"networkName\":\"NET\",\"tadigCode\":\"1234\",\"countryIsoCode\":\"123\",\"countryName\":\"France\",\"rangesPrefix\":\"33\"},\"serviceId\":2,\"packId\":\"7\",\"transportKey\":\"0\",\"algorithmVersion\":0,\"checkDigit\":0,\"inventoryPool\":{\"inventoryPoolId\":2,\"code\":\"1235\",\"description\":null,\"mvno\":null,\"simProfile\":null},\"allotmentId\":\"6\"}\n"));
        }

        @Test
        void getById_notFound() throws Exception {
            final long id = 8L;
            mockMvc.perform(get(baseUrl + "/{simcardId}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().json("{\"error\":\"Simcard with ID '8' could not be found\"}"));
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
                    .andExpect(content().json("{\"equipmentId\":1,\"orderId\":\"1\",\"serialNumber\":\"893771033000000007\",\"externalNumber\":null,\"accessType\":\"DOCSIS\",\"status\":\"BOOKED\",\"nature\":\"MAIN\",\"recyclable\":false,\"preactivated\":false,\"batchNumber\":\"1\",\"provider\":{\"providerId\":1,\"name\":\"Toto\",\"accessType\":\"DOCSIS\"},\"warehouse\":{\"warehouseId\":2,\"name\":\"TATA\",\"resellerCode\":\"TATA\"},\"category\":\"SIMCARD\",\"events\":[\"free\",\"assign\",\"activate\",\"deactivate\"],\"imsiNumber\":\"123456789012345\",\"imsiSponsorNumber\":null,\"puk1Code\":null,\"pin1Code\":null,\"puk2Code\":null,\"pin2Code\":null,\"authKey\":null,\"accessControlClass\":null,\"simProfile\":null,\"number\":null,\"plmn\":{\"plmnId\":1,\"code\":\"12345\",\"networkName\":\"NET\",\"tadigCode\":\"1234\",\"countryIsoCode\":\"123\",\"countryName\":\"France\",\"rangesPrefix\":\"33\"},\"serviceId\":null,\"packId\":null,\"transportKey\":\"0\",\"algorithmVersion\":0,\"checkDigit\":0,\"inventoryPool\":{\"inventoryPoolId\":1,\"code\":\"1234\",\"description\":null,\"mvno\":null,\"simProfile\":null}}\n"));
        }

        @Test
        void getByImsi_notFound() throws Exception {
            final String imsi = "123456789012380";

            mockMvc.perform(get(baseUrl + "/imsi/{imsi}", imsi)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().json("{\"error\":\"Simcard with IMSI '123456789012380' could not be found\"}"));
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
                    .andExpect(content().json("{\"equipmentId\":2,\"orderId\":null,\"serialNumber\":\"893771033000000008\",\"externalNumber\":null,\"accessType\":\"DOCSIS\",\"status\":\"AVAILABLE\",\"nature\":\"MAIN\",\"recyclable\":false,\"preactivated\":false,\"batchNumber\":null,\"provider\":{\"providerId\":1,\"name\":\"Toto\",\"accessType\":\"DOCSIS\"},\"warehouse\":{\"warehouseId\":1,\"name\":\"TOTO\",\"resellerCode\":\"TOTO\"},\"category\":\"SIMCARD\",\"events\":[\"instore\",\"book\"],\"imsiNumber\":\"123456789012346\",\"imsiSponsorNumber\":\"123457\",\"puk1Code\":null,\"pin1Code\":null,\"puk2Code\":null,\"pin2Code\":null,\"authKey\":null,\"accessControlClass\":null,\"simProfile\":null,\"number\":\"456821\",\"plmn\":{\"plmnId\":1,\"code\":\"12345\",\"networkName\":\"NET\",\"tadigCode\":\"1234\",\"countryIsoCode\":\"123\",\"countryName\":\"France\",\"rangesPrefix\":\"33\"},\"serviceId\":null,\"packId\":null,\"transportKey\":\"0\",\"algorithmVersion\":0,\"checkDigit\":0,\"inventoryPool\":{\"inventoryPoolId\":1,\"code\":\"1234\",\"description\":null,\"mvno\":null,\"simProfile\":null}}"));
        }

        @Test
        void getByIccid_notFound() throws Exception {
            final String serialNumber = "8937710330000000011";

            mockMvc.perform(get(baseUrl + "/iccid/{iccid}", serialNumber)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().json("{\"error\":\"Simcard with ICCID '8937710330000000011' could not be found\"}"));
        }
    }

    @Nested
    @DisplayName("GetAll")
    class GetAll {
        @Test
        void getAll_success() throws Exception {

            mockMvc.perform(get(baseUrl)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.page.size").value(20))
                    .andExpect(jsonPath("$.page.totalElements").value(5))
                    .andExpect(jsonPath("$._embedded.simcards[0].equipmentId").value(1))
                    .andExpect(jsonPath("$._embedded.simcards[0].serialNumber").value("893771033000000007"))
                    .andExpect(jsonPath("$._embedded.simcards[0].status").value("BOOKED"))
                    .andExpect(jsonPath("$._embedded.simcards[0].nature").value("MAIN"))
                    .andExpect(jsonPath("$._embedded.simcards[0].category").value("SIMCARD"));
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
                    .andExpect(content().json("{\"error\": \"SIM Card with serial number '893771033000000010' cannot be deleted because it is 'BOOKED', it should be INSTORE, DEACTIVATED or DEPRECATED\"}"));
        }
    }

    @Nested
    @DisplayName("Add")
    class Add {
        @Test
        void add_success() throws Exception {
            ProviderDTO providerDTO = new ProviderDTO();
            providerDTO.setProviderId(2L);

            PlmnDTO plmnDTO = new PlmnDTO();
            plmnDTO.setPlmnId(1L);

            SimCardDTO dto = new SimCardDTO();
            dto.setSerialNumber("8937710330000000012");
            dto.setProvider(providerDTO);
            dto.setImsiNumber("123456789012350");
            dto.setPuk1Code("10000003");
            dto.setPin1Code("1003");
            dto.setPuk2Code("10000003");
            dto.setPin2Code("2000");
            dto.setPlmn(plmnDTO);

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(post(baseUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().json("{\"equipmentId\":6,\"orderId\":null,\"serialNumber\":\"8937710330000000012\",\"externalNumber\":null,\"accessType\":null,\"status\":\"INSTORE\",\"nature\":\"MAIN\",\"recyclable\":false,\"preactivated\":null,\"batchNumber\":null,\"provider\":{\"providerId\":2,\"name\":\"Tata\",\"accessType\":\"DOCSIS\"},\"warehouse\":null,\"category\":\"SIMCARD\",\"events\":null,\"imsiNumber\":\"123456789012350\",\"imsiSponsorNumber\":null,\"puk1Code\":\"10000003\",\"pin1Code\":\"1003\",\"puk2Code\":\"10000003\",\"pin2Code\":\"2000\",\"authKey\":null,\"accessControlClass\":null,\"simProfile\":null,\"number\":null,\"plmn\":{\"plmnId\":1,\"code\":\"12345\",\"networkName\":\"NET\",\"tadigCode\":\"1234\",\"countryIsoCode\":\"123\",\"countryName\":\"France\",\"rangesPrefix\":\"33\"},\"serviceId\":null,\"packId\":null,\"transportKey\":null,\"algorithmVersion\":null,\"checkDigit\":null,\"inventoryPool\":null}\n"));
        }

        @Test
        void add_fails_serial_number_exists() throws Exception {
            ProviderDTO providerDTO = new ProviderDTO();
            providerDTO.setProviderId(2L);

            PlmnDTO plmnDTO = new PlmnDTO();
            plmnDTO.setPlmnId(1L);

            SimCardDTO dto = new SimCardDTO();
            dto.setSerialNumber("893771033000000007");
            dto.setProvider(providerDTO);
            dto.setImsiNumber("123456789012350");
            dto.setPuk1Code("10000003");
            dto.setPin1Code("1003");
            dto.setPuk2Code("10000003");
            dto.setPin2Code("2000");
            dto.setPlmn(plmnDTO);

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(post(baseUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(content().json("{\"error\": \"The serial number '893771033000000007' is already in use by SIMCARD with ID '1'\"}"));
        }
    }

    @Nested
    @DisplayName("Full update")
    class FullUpdate {
        @Test
        void put_success() throws Exception {

            final long id = 2L;
            ProviderDTO providerDTO = new ProviderDTO();
            providerDTO.setProviderId(2L);

            UpdateSimCardDTO dto = new UpdateSimCardDTO();
            dto.setSerialNumber("8937710330000000012");
            dto.setImsiNumber("123456789012350");
            dto.setPuk1Code("10000003");
            dto.setPin1Code("1003");
            dto.setPuk2Code("10000003");
            dto.setPin2Code("2000");
            dto.setNature(EquipmentNature.ADDITIONAL);

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(put(baseUrl + "/{simcardId}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"equipmentId\":2,\"orderId\":null,\"serialNumber\":\"893771033000000008\",\"externalNumber\":null,\"accessType\":\"DOCSIS\",\"status\":\"AVAILABLE\",\"nature\":\"ADDITIONAL\",\"recyclable\":false,\"preactivated\":false,\"batchNumber\":null,\"provider\":{\"providerId\":1,\"name\":\"Toto\",\"accessType\":\"DOCSIS\"},\"warehouse\":null,\"category\":\"SIMCARD\",\"events\":null,\"imsiNumber\":\"123456789012350\",\"imsiSponsorNumber\":null,\"puk1Code\":\"10000003\",\"pin1Code\":\"1003\",\"puk2Code\":\"10000003\",\"pin2Code\":\"2000\",\"authKey\":null,\"accessControlClass\":null,\"simProfile\":null,\"number\":null,\"plmn\":{\"plmnId\":1,\"code\":\"12345\",\"networkName\":\"NET\",\"tadigCode\":\"1234\",\"countryIsoCode\":\"123\",\"countryName\":\"France\",\"rangesPrefix\":\"33\"},\"serviceId\":null,\"packId\":null,\"transportKey\":\"0\",\"algorithmVersion\":0,\"checkDigit\":0,\"inventoryPool\":{\"inventoryPoolId\":1,\"code\":\"1234\",\"description\":null,\"mvno\":null,\"simProfile\":null}}\n"));
        }

        @Test
        void put_notFound() throws Exception {

            final long id = 8L;
            ProviderDTO providerDTO = new ProviderDTO();
            providerDTO.setProviderId(2L);

            UpdateSimCardDTO dto = new UpdateSimCardDTO();
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
                    .andExpect(content().json("{\"error\": \"Simcard with ID '8' could not be found\"}"));
        }

        @Test
        void put_accessType_success() throws Exception {

            final long id = 2L;
            ProviderDTO providerDTO = new ProviderDTO();
            providerDTO.setProviderId(2L);

            UpdateSimCardDTO dto = new UpdateSimCardDTO();
            dto.setSerialNumber("8937710330000000012");
            dto.setImsiNumber("123456789012350");
            dto.setPuk1Code("10000003");
            dto.setPin1Code("1003");
            dto.setPuk2Code("10000003");
            dto.setPin2Code("2000");
            dto.setAccessType(AccessType.FTTH);
            dto.setNature(EquipmentNature.MAIN);

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(put(baseUrl + "/{simcardId}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"equipmentId\":2,\"orderId\":null,\"serialNumber\":\"893771033000000008\",\"externalNumber\":null,\"accessType\":\"FTTH\",\"status\":\"AVAILABLE\",\"nature\":\"MAIN\",\"recyclable\":false,\"preactivated\":false,\"batchNumber\":null,\"provider\":{\"providerId\":1,\"name\":\"Toto\",\"accessType\":\"DOCSIS\"},\"warehouse\":null,\"category\":\"SIMCARD\",\"events\":null,\"imsiNumber\":\"123456789012350\",\"imsiSponsorNumber\":null,\"puk1Code\":\"10000003\",\"pin1Code\":\"1003\",\"puk2Code\":\"10000003\",\"pin2Code\":\"2000\",\"authKey\":null,\"accessControlClass\":null,\"simProfile\":null,\"number\":null,\"plmn\":{\"plmnId\":1,\"code\":\"12345\",\"networkName\":\"NET\",\"tadigCode\":\"1234\",\"countryIsoCode\":\"123\",\"countryName\":\"France\",\"rangesPrefix\":\"33\"},\"serviceId\":null,\"packId\":null,\"transportKey\":\"0\",\"algorithmVersion\":0,\"checkDigit\":0,\"inventoryPool\":{\"inventoryPoolId\":1,\"code\":\"1234\",\"description\":null,\"mvno\":null,\"simProfile\":null}}\n"));
        }
    }

    @Nested
    @DisplayName("PartialUpdate")
    class PartialUpdate {
        @Test
        void patchById_success() throws Exception {

            final long id = 2L;
            ProviderDTO providerDTO = new ProviderDTO();
            providerDTO.setProviderId(2L);

            UpdateSimCardDTO dto = new UpdateSimCardDTO();
            dto.setSerialNumber("8937710330000000012");
            dto.setImsiNumber("123456789012350");
            dto.setPuk1Code("10000003");
            dto.setPin1Code("1003");
            dto.setPuk2Code("10000003");
            dto.setPin2Code("2000");
            dto.setActivity(Activity.MOBILE);
            dto.setEsim(true);
            dto.setNature(EquipmentNature.ADDITIONAL);

            final String requestJson = objectMapper.writeValueAsString(dto);

            mockMvc.perform(patch(baseUrl + "/{simcardId}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"equipmentId\":2,\"orderId\":null,\"serialNumber\":\"893771033000000008\",\"externalNumber\":null,\"accessType\":\"DOCSIS\",\"activity\":\"MOBILE\",\"status\":\"AVAILABLE\",\"nature\":\"ADDITIONAL\",\"recyclable\":false,\"preactivated\":false,\"batchNumber\":null,\"provider\":{\"providerId\":1,\"name\":\"Toto\",\"accessType\":\"DOCSIS\"},\"warehouse\":{\"warehouseId\":1,\"name\":\"TOTO\",\"resellerCode\":\"TOTO\"},\"category\":\"SIMCARD\",\"events\":null,\"imsiNumber\":\"123456789012350\",\"imsiSponsorNumber\":\"123457\",\"puk1Code\":\"10000003\",\"pin1Code\":\"1003\",\"puk2Code\":\"10000003\",\"pin2Code\":\"2000\",\"authKey\":null,\"accessControlClass\":null,\"simProfile\":null,\"number\":\"456821\",\"plmn\":{\"plmnId\":1,\"code\":\"12345\",\"networkName\":\"NET\",\"tadigCode\":\"1234\",\"countryIsoCode\":\"123\",\"countryName\":\"France\",\"rangesPrefix\":\"33\"},\"serviceId\":null,\"packId\":null,\"transportKey\":\"0\",\"algorithmVersion\":0,\"checkDigit\":0,\"inventoryPool\":{\"inventoryPoolId\":1,\"code\":\"1234\",\"description\":null,\"mvno\":null,\"simProfile\":null},\"esim\":true}\n"));
        }

        @Test
        void patchBySerialNumber_success() throws Exception {

            final String serialNumber = "893771033000000008";
            ProviderDTO providerDTO = new ProviderDTO();
            providerDTO.setProviderId(2L);

            UpdateSimCardDTO dto = new UpdateSimCardDTO();
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
                    .andExpect(content().json("{\"equipmentId\":2,\"orderId\":null,\"serialNumber\":\"893771033000000008\",\"externalNumber\":null,\"accessType\":\"DOCSIS\",\"status\":\"AVAILABLE\",\"nature\":\"MAIN\",\"recyclable\":false,\"preactivated\":false,\"batchNumber\":null,\"provider\":{\"providerId\":1,\"name\":\"Toto\",\"accessType\":\"DOCSIS\"},\"warehouse\":{\"warehouseId\":1,\"name\":\"TOTO\",\"resellerCode\":\"TOTO\"},\"category\":\"SIMCARD\",\"events\":null,\"imsiNumber\":\"123456789012350\",\"imsiSponsorNumber\":\"123457\",\"puk1Code\":\"10000003\",\"pin1Code\":\"1003\",\"puk2Code\":\"10000003\",\"pin2Code\":\"2000\",\"authKey\":null,\"accessControlClass\":null,\"simProfile\":null,\"number\":\"456821\",\"plmn\":{\"plmnId\":1,\"code\":\"12345\",\"networkName\":\"NET\",\"tadigCode\":\"1234\",\"countryIsoCode\":\"123\",\"countryName\":\"France\",\"rangesPrefix\":\"33\"},\"serviceId\":null,\"packId\":null,\"transportKey\":\"0\",\"algorithmVersion\":0,\"checkDigit\":0,\"inventoryPool\":{\"inventoryPoolId\":1,\"code\":\"1234\",\"description\":null,\"mvno\":null,\"simProfile\":null},\"esim\":true}\n"));
        }

        @Test
        void patchByImsi_success() throws Exception {

            final String imsi = "123456789012346";
            ProviderDTO providerDTO = new ProviderDTO();
            providerDTO.setProviderId(2L);

            UpdateSimCardDTO dto = new UpdateSimCardDTO();
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
                    .andExpect(content().json("{\"equipmentId\":2,\"orderId\":null,\"serialNumber\":\"893771033000000008\",\"externalNumber\":null,\"accessType\":\"DOCSIS\",\"status\":\"AVAILABLE\",\"nature\":\"MAIN\",\"recyclable\":false,\"preactivated\":false,\"batchNumber\":null,\"provider\":{\"providerId\":1,\"name\":\"Toto\",\"accessType\":\"DOCSIS\"},\"warehouse\":{\"warehouseId\":1,\"name\":\"TOTO\",\"resellerCode\":\"TOTO\"},\"category\":\"SIMCARD\",\"events\":null,\"imsiNumber\":\"123456789012350\",\"imsiSponsorNumber\":\"123457\",\"puk1Code\":\"10000003\",\"pin1Code\":\"1003\",\"puk2Code\":\"10000003\",\"pin2Code\":\"2000\",\"authKey\":null,\"accessControlClass\":null,\"simProfile\":null,\"number\":\"456821\",\"plmn\":{\"plmnId\":1,\"code\":\"12345\",\"networkName\":\"NET\",\"tadigCode\":\"1234\",\"countryIsoCode\":\"123\",\"countryName\":\"France\",\"rangesPrefix\":\"33\"},\"serviceId\":null,\"packId\":null,\"transportKey\":\"0\",\"algorithmVersion\":0,\"checkDigit\":0,\"inventoryPool\":{\"inventoryPoolId\":1,\"code\":\"1234\",\"description\":null,\"mvno\":null,\"simProfile\":null}, \"esim\":true}\n"));
        }

        @Test
        void patch_accessType_success() throws Exception {

            final long id = 2L;
            ProviderDTO providerDTO = new ProviderDTO();
            providerDTO.setProviderId(2L);

            UpdateSimCardDTO dto = new UpdateSimCardDTO();
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
                    .andExpect(content().json("{\"equipmentId\":2,\"orderId\":null,\"serialNumber\":\"893771033000000008\",\"externalNumber\":null,\"accessType\":\"FTTH\",\"status\":\"AVAILABLE\",\"nature\":\"MAIN\",\"recyclable\":false,\"preactivated\":false,\"batchNumber\":null,\"provider\":{\"providerId\":1,\"name\":\"Toto\",\"accessType\":\"DOCSIS\"},\"warehouse\":{\"warehouseId\":1,\"name\":\"TOTO\",\"resellerCode\":\"TOTO\"},\"category\":\"SIMCARD\",\"events\":null,\"imsiNumber\":\"123456789012350\",\"imsiSponsorNumber\":\"123457\",\"puk1Code\":\"10000003\",\"pin1Code\":\"1003\",\"puk2Code\":\"10000003\",\"pin2Code\":\"2000\",\"authKey\":null,\"accessControlClass\":null,\"simProfile\":null,\"number\":\"456821\",\"plmn\":{\"plmnId\":1,\"code\":\"12345\",\"networkName\":\"NET\",\"tadigCode\":\"1234\",\"countryIsoCode\":\"123\",\"countryName\":\"France\",\"rangesPrefix\":\"33\"},\"serviceId\":null,\"packId\":null,\"transportKey\":\"0\",\"algorithmVersion\":0,\"checkDigit\":0,\"inventoryPool\":{\"inventoryPoolId\":1,\"code\":\"1234\",\"description\":null,\"mvno\":null,\"simProfile\":null}}"));
        }
    }

    @Nested
    @DisplayName("UpdatePack")
    class UpdatePack {
        @Test
        void updatePackById_success() throws Exception {

            final long id = 2L;
            ProviderDTO providerDTO = new ProviderDTO();
            providerDTO.setProviderId(2L);

            UpdateSimCardDTO dto = new UpdateSimCardDTO();
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
                    .andExpect(content().json("{\"equipmentId\":2,\"orderId\":null,\"serialNumber\":\"893771033000000008\",\"externalNumber\":null,\"accessType\":\"DOCSIS\",\"status\":\"AVAILABLE\",\"nature\":\"MAIN\",\"recyclable\":false,\"preactivated\":false,\"batchNumber\":null,\"provider\":{\"providerId\":1,\"name\":\"Toto\",\"accessType\":\"DOCSIS\"},\"warehouse\":{\"warehouseId\":1,\"name\":\"TOTO\",\"resellerCode\":\"TOTO\"},\"category\":\"SIMCARD\",\"events\":null,\"imsiNumber\":\"123456789012346\",\"imsiSponsorNumber\":\"123457\",\"puk1Code\":null,\"pin1Code\":null,\"puk2Code\":null,\"pin2Code\":null,\"authKey\":null,\"accessControlClass\":null,\"simProfile\":null,\"number\":\"456821\",\"plmn\":{\"plmnId\":1,\"code\":\"12345\",\"networkName\":\"NET\",\"tadigCode\":\"1234\",\"countryIsoCode\":\"123\",\"countryName\":\"France\",\"rangesPrefix\":\"33\"},\"serviceId\":null,\"packId\":\"1SIMIONFT73601297\",\"transportKey\":\"0\",\"algorithmVersion\":0,\"checkDigit\":0,\"inventoryPool\":{\"inventoryPoolId\":1,\"code\":\"1234\",\"description\":null,\"mvno\":null,\"simProfile\":null}}"));
        }

        @Test
        void updatePackByIMSI_success() throws Exception {

            ProviderDTO providerDTO = new ProviderDTO();
            providerDTO.setProviderId(2L);

            UpdateSimCardDTO dto = new UpdateSimCardDTO();
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
                    .andExpect(content().json("{\"equipmentId\":3,\"orderId\":null,\"serialNumber\":\"893771033000000009\",\"externalNumber\":\"37799900001\",\"accessType\":\"DOCSIS\",\"status\":\"DEACTIVATED\",\"nature\":\"MAIN\",\"recyclable\":false,\"preactivated\":true,\"batchNumber\":\"1\",\"provider\":{\"providerId\":2,\"name\":\"Tata\",\"accessType\":\"DOCSIS\"},\"warehouse\":{\"warehouseId\":1,\"name\":\"TOTO\",\"resellerCode\":\"TOTO\"},\"category\":\"SIMCARD\",\"events\":null,\"imsiNumber\":\"123456789012347\",\"imsiSponsorNumber\":\"123456\",\"puk1Code\":null,\"pin1Code\":null,\"puk2Code\":null,\"pin2Code\":null,\"authKey\":null,\"accessControlClass\":null,\"simProfile\":null,\"number\":\"456789\",\"plmn\":{\"plmnId\":1,\"code\":\"12345\",\"networkName\":\"NET\",\"tadigCode\":\"1234\",\"countryIsoCode\":\"123\",\"countryName\":\"France\",\"rangesPrefix\":\"33\"},\"serviceId\":2,\"packId\":\"1\",\"transportKey\":\"0\",\"algorithmVersion\":0,\"checkDigit\":0,\"inventoryPool\":{\"inventoryPoolId\":2,\"code\":\"1235\",\"description\":null,\"mvno\":null,\"simProfile\":null}}"));
        }

        @Test
        void updatePackByIMSI_ImsiNotFound() throws Exception {

            ProviderDTO providerDTO = new ProviderDTO();
            providerDTO.setProviderId(2L);

            UpdateSimCardDTO dto = new UpdateSimCardDTO();
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
                    .andExpect(content().json("{\"error\": \"Simcard with IMSI '123456789012350' could not be found\"}"));
        }
    }

    @Nested
    @DisplayName("Search")
    class Search {
        @Test
        void search_success() throws Exception {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("imsi", "123456789012345");

            mockMvc.perform(get(baseUrl + "/search")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .params(map))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"_embedded\":{\"simcards\":[{\"equipmentId\":1,\"orderId\":\"1\",\"serialNumber\":\"893771033000000007\",\"externalNumber\":null,\"accessType\":\"DOCSIS\",\"status\":\"BOOKED\",\"nature\":\"MAIN\",\"recyclable\":false,\"preactivated\":false,\"batchNumber\":\"1\",\"provider\":{\"providerId\":1,\"name\":\"Toto\",\"accessType\":\"DOCSIS\"},\"warehouse\":{\"warehouseId\":2,\"name\":\"TATA\",\"resellerCode\":\"TATA\"},\"category\":\"SIMCARD\",\"events\":[\"free\",\"assign\",\"activate\",\"deactivate\"],\"imsiNumber\":\"123456789012345\",\"imsiSponsorNumber\":null,\"puk1Code\":null,\"pin1Code\":null,\"puk2Code\":null,\"pin2Code\":null,\"authKey\":null,\"accessControlClass\":null,\"simProfile\":null,\"number\":null,\"plmn\":{\"plmnId\":1,\"code\":\"12345\",\"networkName\":\"NET\",\"tadigCode\":\"1234\",\"countryIsoCode\":\"123\",\"countryName\":\"France\",\"rangesPrefix\":\"33\"},\"serviceId\":null,\"packId\":null,\"transportKey\":\"0\",\"algorithmVersion\":0,\"checkDigit\":0,\"inventoryPool\":{\"inventoryPoolId\":1,\"code\":\"1234\",\"description\":null,\"mvno\":null,\"simProfile\":null},\"allotmentId\":null}]},\"_links\":{\"self\":{\"href\":\"http://localhost/simcards\"}},\"page\":{\"size\":20,\"totalElements\":1,\"totalPages\":1,\"number\":0}}"));
        }

        @Test
        void search_with_eSim_success() throws Exception {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("esim", "true");

            mockMvc.perform(get(baseUrl + "/search")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .params(map))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"_embedded\" : {\"simcards\" : [ {\"equipmentId\" : 3,\"orderId\" : null,\"serialNumber\" : \"893771033000000009\",\"externalNumber\" : \"37799900001\",\"accessType\" : \"DOCSIS\",\"activity\" : null,\"activationDate\" : null,\"assignmentDate\" : null,\"status\" : \"DEACTIVATED\",\"nature\" : \"MAIN\",\"recyclable\" : false,\"preactivated\" : true,\"batchNumber\" : \"1\",\"provider\" : {\"providerId\" : 2,\"name\" : \"Tata\",\"accessType\" : \"DOCSIS\"},\"warehouse\" : {\"warehouseId\" : 1,\"name\" : \"TOTO\",\"resellerCode\" : \"TOTO\"},\"category\" : \"SIMCARD\",\"events\" : [ \"rollback_deactivate\", \"available\" ],\"imsiNumber\" : \"123456789012347\",\"imsiSponsorNumber\" : \"123456\",\"puk1Code\" : null,\"pin1Code\" : null,\"puk2Code\" : null,\"pin2Code\" : null,\"authKey\" : null,\"accessControlClass\" : null,\"simProfile\" : null,\"number\" : \"456789\",\"plmn\" : {\"plmnId\" : 1,\"code\" : \"12345\",\"networkName\" : \"NET\",\"tadigCode\" : \"1234\",\"countryIsoCode\" : \"123\",\"countryName\" : \"France\",\"rangesPrefix\" : \"33\"},\"serviceId\" : 2,\"packId\" : \"7\",\"transportKey\" : \"0\",\"algorithmVersion\" : 0,\"checkDigit\" : 0,\"inventoryPool\" : {\"inventoryPoolId\" : 2,\"code\" : \"1235\",\"description\" : null,\"mvno\" : null,\"simProfile\" : null},\"allotmentId\" : \"6\",\"esim\" : true,\"otaSalt\" : null}, {\"equipmentId\" : 4,\"orderId\" : \"2\",\"serialNumber\" : \"893771033000000010\",\"externalNumber\" : null,\"accessType\" : \"FREEDHOME\",\"activity\" : null,\"activationDate\" : null,\"assignmentDate\" : null,\"status\" : \"BOOKED\",\"nature\" : \"ADDITIONAL\",\"recyclable\" : false,\"preactivated\" : false,\"batchNumber\" : \"2\",\"provider\" : {\"providerId\" : 1,\"name\" : \"Toto\",\"accessType\" : \"DOCSIS\"},\"warehouse\" : {\"warehouseId\" : 1,\"name\" : \"TOTO\",\"resellerCode\" : \"TOTO\"},\"category\" : \"SIMCARD\",\"events\" : [ \"free\", \"assign\", \"activate\", \"deactivate\" ],\"imsiNumber\" : \"123456789012348\",\"imsiSponsorNumber\" : null,\"puk1Code\" : \"10015953\",\"pin1Code\" : null,\"puk2Code\" : null,\"pin2Code\" : null,\"authKey\" : null,\"accessControlClass\" : null,\"simProfile\" : null,\"number\" : null,\"plmn\" : {\"plmnId\" : 1,\"code\" : \"12345\",\"networkName\" : \"NET\",\"tadigCode\" : \"1234\",\"countryIsoCode\" : \"123\",\"countryName\" : \"France\",\"rangesPrefix\" : \"33\"},\"serviceId\" : null,\"packId\" : null,\"transportKey\" : \"0\",\"algorithmVersion\" : 0,\"checkDigit\" : 0,\"inventoryPool\" : {\"inventoryPoolId\" : 1,\"code\" : \"1234\",\"description\" : null,\"mvno\" : null,\"simProfile\" : null},\"allotmentId\" : null,\"esim\" : true,\"otaSalt\" : \"BCEB92ACD3E2CCC72FB4ADDD258AD981\"}, {\"equipmentId\" : 5,\"orderId\" : \"2\",\"serialNumber\" : \"893771033000000011\",\"externalNumber\" : null,\"accessType\" : \"FREEDHOME\",\"activity\" : null,\"activationDate\" : null,\"assignmentDate\" : \"2021-01-18T08:45:58\",\"status\" : \"ASSIGNED\",\"nature\" : \"ADDITIONAL\",\"recyclable\" : false,\"preactivated\" : false,\"batchNumber\" : \"2\",\"provider\" : {\"providerId\" : 1,\"name\" : \"Toto\",\"accessType\" : \"DOCSIS\"},\"warehouse\" : {\"warehouseId\" : 1,\"name\" : \"TOTO\",\"resellerCode\" : \"TOTO\"},\"category\" : \"SIMCARD\",\"events\" : [ \"unassign\", \"activate\", \"deactivate\" ],\"imsiNumber\" : \"123456789012349\",\"imsiSponsorNumber\" : null,\"puk1Code\" : \"10015955\",\"pin1Code\" : \"1001\",\"puk2Code\" : \"10015954\",\"pin2Code\" : \"1002\",\"authKey\" : null,\"accessControlClass\" : null,\"simProfile\" : null,\"number\" : null,\"plmn\" : {\"plmnId\" : 1,\"code\" : \"12345\",\"networkName\" : \"NET\",\"tadigCode\" : \"1234\",\"countryIsoCode\" : \"123\",\"countryName\" : \"France\",\"rangesPrefix\" : \"33\"},\"serviceId\" : null,\"packId\" : null,\"transportKey\" : \"0\",\"algorithmVersion\" : 0,\"checkDigit\" : 0,\"inventoryPool\" : {\"inventoryPoolId\" : 1,\"code\" : \"1234\",\"description\" : null,\"mvno\" : null,\"simProfile\" : null},\"allotmentId\" : null,\"esim\" : true,\"otaSalt\" : \"BCEB92ACD3E2CCC72FB4ADDD258AD981\"} ]},\"_links\" : {\"self\" : {\"href\" : \"http://localhost/simcards\"}},\"page\" : {\"size\" : 20,\"totalElements\" : 3,\"totalPages\" : 1,\"number\" : 0}}"));
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
                    .andExpect(content().json("{\"equipmentId\" : 1,\"orderId\" : \"1L\",\"serialNumber\" : \"893771033000000007\",\"externalNumber\" : null,\"accessType\" : \"DOCSIS\",\"activity\" : null,\"status\" : \"BOOKED\",\"nature\" : \"MAIN\",\"recyclable\" : false,\"preactivated\" : false,\"batchNumber\" : \"1\",\"provider\" : {\"providerId\" : 1,\"name\" : \"Toto\",\"accessType\" : \"DOCSIS\"},\"warehouse\" : {\"warehouseId\" : 2,\"name\" : \"TATA\",\"resellerCode\" : \"TATA\"},\"category\" : \"SIMCARD\",\"events\" : null,\"imsiNumber\" : \"123456789012345\",\"imsiSponsorNumber\" : null,\"puk1Code\" : null,\"pin1Code\" : null,\"puk2Code\" : null,\"pin2Code\" : null,\"authKey\" : null,\"accessControlClass\" : null,\"simProfile\" : null,\"number\" : null,\"plmn\" : {\"plmnId\" : 1,\"code\" : \"12345\",\"networkName\" : \"NET\",\"tadigCode\" : \"1234\",\"countryIsoCode\" : \"123\",\"countryName\" : \"France\",\"rangesPrefix\" : \"33\"},\"serviceId\" : null,\"packId\" : null,\"transportKey\" : \"0\",\"algorithmVersion\" : 0,\"checkDigit\" : 0,\"inventoryPool\" : {\"inventoryPoolId\" : 1,\"code\" : \"1234\",\"description\" : null,\"mvno\" : null,\"simProfile\" : null},\"allotmentId\" : null,\"esim\" : false,\"otaSalt\" : null}"));
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
                    .andExpect(content().json("{\"error\": \"Cannot apply event 'book' on SIMCARD with status 'DEACTIVATED' and ICCID '893771033000000009'\"}"));
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
                    .andExpect(content().json("{\"equipmentId\":2,\"orderId\":\"1L\",\"serialNumber\":\"893771033000000008\",\"externalNumber\":null,\"accessType\":\"DOCSIS\",\"status\":\"BOOKED\",\"nature\":\"MAIN\",\"recyclable\":false,\"preactivated\":false,\"batchNumber\":null,\"provider\":{\"providerId\":1,\"name\":\"Toto\",\"accessType\":\"DOCSIS\"},\"warehouse\":{\"warehouseId\":1,\"name\":\"TOTO\",\"resellerCode\":\"TOTO\"},\"category\":\"SIMCARD\",\"events\":null,\"imsiNumber\":\"123456789012346\",\"imsiSponsorNumber\":\"123457\",\"puk1Code\":null,\"pin1Code\":null,\"puk2Code\":null,\"pin2Code\":null,\"authKey\":null,\"accessControlClass\":null,\"simProfile\":null,\"number\":\"456821\",\"plmn\":{\"plmnId\":1,\"code\":\"12345\",\"networkName\":\"NET\",\"tadigCode\":\"1234\",\"countryIsoCode\":\"123\",\"countryName\":\"France\",\"rangesPrefix\":\"33\"},\"serviceId\":null,\"packId\":null,\"transportKey\":\"0\",\"algorithmVersion\":0,\"checkDigit\":0,\"inventoryPool\":{\"inventoryPoolId\":1,\"code\":\"1234\",\"description\":null,\"mvno\":null,\"simProfile\":null}}"));
        }

        @Test
        void setEventById_successWithoutBody() throws Exception {
            final long id = 2L;

            mockMvc.perform(patch(baseUrl + "/{simcardId}/{event}", id, Event.book)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"equipmentId\":2,\"orderId\":null,\"serialNumber\":\"893771033000000008\",\"externalNumber\":null,\"accessType\":\"DOCSIS\",\"status\":\"BOOKED\",\"nature\":\"MAIN\",\"recyclable\":false,\"preactivated\":false,\"batchNumber\":null,\"provider\":{\"providerId\":1,\"name\":\"Toto\",\"accessType\":\"DOCSIS\"},\"warehouse\":{\"warehouseId\":1,\"name\":\"TOTO\",\"resellerCode\":\"TOTO\"},\"category\":\"SIMCARD\",\"events\":null,\"imsiNumber\":\"123456789012346\",\"imsiSponsorNumber\":\"123457\",\"puk1Code\":null,\"pin1Code\":null,\"puk2Code\":null,\"pin2Code\":null,\"authKey\":null,\"accessControlClass\":null,\"simProfile\":null,\"number\":\"456821\",\"plmn\":{\"plmnId\":1,\"code\":\"12345\",\"networkName\":\"NET\",\"tadigCode\":\"1234\",\"countryIsoCode\":\"123\",\"countryName\":\"France\",\"rangesPrefix\":\"33\"},\"serviceId\":null,\"packId\":null,\"transportKey\":\"0\",\"algorithmVersion\":0,\"checkDigit\":0,\"inventoryPool\":{\"inventoryPoolId\":1,\"code\":\"1234\",\"description\":null,\"mvno\":null,\"simProfile\":null}}"));
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
                    .andExpect(content().json("{\"equipmentId\" : 5,\"orderId\" : null,\"serialNumber\" : \"893771033000000011\",\"externalNumber\" : null,\"accessType\" : \"FREEDHOME\",\"activity\" : null,\"activationDate\" : \"2020-05-31T20:35:24\",\"assignmentDate\" : \"2021-01-18T08:45:58\",\"status\" : \"ACTIVATED\",\"nature\" : \"ADDITIONAL\",\"recyclable\" : false,\"preactivated\" : false,\"batchNumber\" : \"2\",\"provider\" : {\"providerId\" : 1,\"name\" : \"Toto\",\"accessType\" : \"DOCSIS\"},\"warehouse\" : {\"warehouseId\" : 1,\"name\" : \"TOTO\",\"resellerCode\" : \"TOTO\"},\"category\" : \"SIMCARD\",\"events\" : null,\"imsiNumber\" : \"123456789012349\",\"imsiSponsorNumber\" : null,\"puk1Code\" : \"10015955\",\"pin1Code\" : \"1001\",\"puk2Code\" : \"10015954\",\"pin2Code\" : \"1002\",\"authKey\" : null,\"accessControlClass\" : null,\"simProfile\" : null,\"number\" : null,\"plmn\" : {\"plmnId\" : 1,\"code\" : \"12345\",\"networkName\" : \"NET\",\"tadigCode\" : \"1234\",\"countryIsoCode\" : \"123\",\"countryName\" : \"France\",\"rangesPrefix\" : \"33\"},\"serviceId\" : 12344,\"packId\" : null,\"transportKey\" : \"0\",\"algorithmVersion\" : 0,\"checkDigit\" : 0,\"inventoryPool\" : {\"inventoryPoolId\" : 1,\"code\" : \"1234\",\"description\" : null,\"mvno\" : null,\"simProfile\" : null},\"allotmentId\" : null,\"esim\" : true,\"otaSalt\" : \"BCEB92ACD3E2CCC72FB4ADDD258AD981\"}"));
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
                    .andExpect(content().json("{\"equipmentId\":2,\"orderId\":\"1L\",\"serialNumber\":\"893771033000000008\",\"externalNumber\":null,\"accessType\":\"DOCSIS\",\"status\":\"BOOKED\",\"nature\":\"MAIN\",\"recyclable\":false,\"preactivated\":false,\"batchNumber\":null,\"provider\":{\"providerId\":1,\"name\":\"Toto\",\"accessType\":\"DOCSIS\"},\"warehouse\":{\"warehouseId\":1,\"name\":\"TOTO\",\"resellerCode\":\"TOTO\"},\"category\":\"SIMCARD\",\"events\":null,\"imsiNumber\":\"123456789012346\",\"imsiSponsorNumber\":\"123457\",\"puk1Code\":null,\"pin1Code\":null,\"puk2Code\":null,\"pin2Code\":null,\"authKey\":null,\"accessControlClass\":null,\"simProfile\":null,\"number\":\"456821\",\"plmn\":{\"plmnId\":1,\"code\":\"12345\",\"networkName\":\"NET\",\"tadigCode\":\"1234\",\"countryIsoCode\":\"123\",\"countryName\":\"France\",\"rangesPrefix\":\"33\"},\"serviceId\":null,\"packId\":null,\"transportKey\":\"0\",\"algorithmVersion\":0,\"checkDigit\":0,\"inventoryPool\":{\"inventoryPoolId\":1,\"code\":\"1234\",\"description\":null,\"mvno\":null,\"simProfile\":null}}"));
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
                    .andExpect(content().json("{\"equipmentId\":2,\"orderId\":\"1L\",\"serialNumber\":\"893771033000000008\",\"externalNumber\":null,\"accessType\":\"DOCSIS\",\"status\":\"BOOKED\",\"nature\":\"MAIN\",\"recyclable\":false,\"preactivated\":false,\"batchNumber\":null,\"provider\":{\"providerId\":1,\"name\":\"Toto\",\"accessType\":\"DOCSIS\"},\"warehouse\":{\"warehouseId\":1,\"name\":\"TOTO\",\"resellerCode\":\"TOTO\"},\"category\":\"SIMCARD\",\"events\":null,\"imsiNumber\":\"123456789012346\",\"imsiSponsorNumber\":\"123457\",\"puk1Code\":null,\"pin1Code\":null,\"puk2Code\":null,\"pin2Code\":null,\"authKey\":null,\"accessControlClass\":null,\"simProfile\":null,\"number\":\"456821\",\"plmn\":{\"plmnId\":1,\"code\":\"12345\",\"networkName\":\"NET\",\"tadigCode\":\"1234\",\"countryIsoCode\":\"123\",\"countryName\":\"France\",\"rangesPrefix\":\"33\"},\"serviceId\":null,\"packId\":null,\"transportKey\":\"0\",\"algorithmVersion\":0,\"checkDigit\":0,\"inventoryPool\":{\"inventoryPoolId\":1,\"code\":\"1234\",\"description\":null,\"mvno\":null,\"simProfile\":null}}"));
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
                    .andExpect(content().json("{\"error\": \"Simcard with ID '10' could not be found\"}"));
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
                    .andExpect(content().json("{\"equipmentId\" : 1,\"orderId\" : null,\"serialNumber\" : \"893771033000000007\",\"externalNumber\" : null,\"accessType\" : \"DOCSIS\",\"activity\" : null,\"activationDate\" : null,\"assignmentDate\" : \"2020-05-31T20:35:24\",\"status\" : \"ASSIGNED\",\"nature\" : \"MAIN\",\"recyclable\" : false,\"preactivated\" : false,\"batchNumber\" : \"1\",\"provider\" : {\"providerId\" : 1,\"name\" : \"Toto\",\"accessType\" : \"DOCSIS\"},\"warehouse\" : {\"warehouseId\" : 2,\"name\" : \"TATA\",\"resellerCode\" : \"TATA\"},\"category\" : \"SIMCARD\",\"events\" : null,\"imsiNumber\" : \"123456789012345\",\"imsiSponsorNumber\" : null,\"puk1Code\" : null,\"pin1Code\" : null,\"puk2Code\" : null,\"pin2Code\" : null,\"authKey\" : null,\"accessControlClass\" : null,\"simProfile\" : null,\"number\" : null,\"plmn\" : {\"plmnId\" : 1,\"code\" : \"12345\",\"networkName\" : \"NET\",\"tadigCode\" : \"1234\",\"countryIsoCode\" : \"123\",\"countryName\" : \"France\",\"rangesPrefix\" : \"33\"},\"serviceId\" : 11,\"packId\" : null,\"transportKey\" : \"0\",\"algorithmVersion\" : 0,\"checkDigit\" : 0,\"inventoryPool\" : {\"inventoryPoolId\" : 1,\"code\" : \"1234\",\"description\" : null,\"mvno\" : null,\"simProfile\" : null},\"allotmentId\" : null,\"esim\" : false,\"otaSalt\" : null}"));
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
                    .andExpect(content().json("{  \"_embedded\" : { \"revisions\" : [ {\"date\" : \"2020-02-25T10:52:37\",\"entity\" : {  \"equipmentId\" : 1,  \"orderId\" : \"1\",  \"serialNumber\" : \"893771033000000007\",  \"externalNumber\" : null,  \"accessType\" : \"DOCSIS\",  \"activity\" : null,  \"activationDate\" : null,  \"assignmentDate\" : null,  \"status\" : \"BOOKED\",  \"nature\" : \"MAIN\",  \"recyclable\" : false,  \"preactivated\" : false,  \"batchNumber\" : null,  \"provider\" : null,  \"warehouse\" : null,  \"category\" : \"SIMCARD\",  \"events\" : null,  \"imsiNumber\" : \"123456789012345\",  \"imsiSponsorNumber\" : null,  \"puk1Code\" : null,  \"pin1Code\" : null,  \"puk2Code\" : null,  \"pin2Code\" : null,  \"authKey\" : null,  \"accessControlClass\" : null,  \"simProfile\" : null,  \"number\" : null,  \"plmn\" : null,  \"serviceId\" : null,  \"packId\" : \"1\",  \"transportKey\" : null,  \"algorithmVersion\" : 0,  \"checkDigit\" : 0,  \"inventoryPool\" : { \"inventoryPoolId\" : 1, \"code\" : \"1234\", \"description\" : null, \"mvno\" : null, \"simProfile\" : null  },  \"allotmentId\" : null,  \"esim\" : false,  \"otaSalt\" : null},\"author\" : \"g.,fantappie\" } ]  },  \"_links\" : { \"self\" : {\"href\" : \"http://localhost\" }  },  \"page\" : { \"size\" : 20, \"totalElements\" : 1, \"totalPages\" : 1, \"number\" : 0  }}"));
        }

        @Test
        void getRevisions_missing() throws Exception {
            mockMvc.perform(get(baseUrl + "/99/revisions"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("{   \"_links\" : {     \"self\" : {       \"href\" : \"http://localhost\"     }   },   \"page\" : {     \"size\" : 20,     \"totalElements\" : 0,     \"totalPages\" : 0,     \"number\" : 0   } }"));
        }
    }
}
