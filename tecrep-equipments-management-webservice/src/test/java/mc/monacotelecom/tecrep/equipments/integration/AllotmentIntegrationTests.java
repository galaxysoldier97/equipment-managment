package mc.monacotelecom.tecrep.equipments.integration;

import mc.monacotelecom.tecrep.equipments.dto.AllotmentProvisionedDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.AllotmentExportRequestDTO;
import mc.monacotelecom.tecrep.equipments.entity.AllotmentSummary;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sql/clean.sql", "/sql/allotment_data.sql"})
class AllotmentIntegrationTests extends BaseIntegrationTest {

    final String baseUrl = "/private/auth/allotments";

    @Test
    void getAllAllotment_allotmentCRUD_returned() throws Exception {
        mockMvc.perform(get(baseUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].allotmentId").value(7))
                .andExpect(jsonPath("$.content[0].batchNumber").value("6"))
                .andExpect(jsonPath("$.content[0].allotmentType").value("PREPAID"))
                .andExpect(jsonPath("$.content[0].inventoryPool.inventoryPoolId").value(5))
                .andExpect(jsonPath("$.content[0].inventoryPool.code").value("METEOR_PAIRED_CUSTOMER_POOL"))
                .andExpect(jsonPath("$.content[0].inventoryPool.description").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].inventoryPool.mvno").value(0))
                .andExpect(jsonPath("$.content[0].inventoryPool.simProfile").value("DEFAULT"))
                .andExpect(jsonPath("$.content[0].quantity").value(10))
                .andExpect(jsonPath("$.content[0].packWithHandset").value(true))
                .andExpect(jsonPath("$.content[0].pricePlan").value("pricePlan"))
                .andExpect(jsonPath("$.content[0].initialCredit").value(10))
                .andExpect(jsonPath("$.content[0].preProvisioning").value(true))
                .andExpect(jsonPath("$.content[0].creationDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[0].isSentToLogistics").value(false))
                .andExpect(jsonPath("$.content[0].isProvisioned").value(false));
    }

    @Test
    void getAllotmentById_allotmentCRUD_returned() throws Exception {
        mockMvc.perform(get(baseUrl + "/7"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allotmentId").value(7))
                .andExpect(jsonPath("$.batchNumber").value("6"))
                .andExpect(jsonPath("$.allotmentType").value("PREPAID"))
                .andExpect(jsonPath("$.inventoryPool.inventoryPoolId").value(5))
                .andExpect(jsonPath("$.inventoryPool.code").value("METEOR_PAIRED_CUSTOMER_POOL"))
                .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.inventoryPool.mvno").value(0))
                .andExpect(jsonPath("$.inventoryPool.simProfile").value("DEFAULT"))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.packWithHandset").value(true))
                .andExpect(jsonPath("$.pricePlan").value("pricePlan"))
                .andExpect(jsonPath("$.initialCredit").value(10))
                .andExpect(jsonPath("$.preProvisioning").value(true))
                .andExpect(jsonPath("$.creationDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.isSentToLogistics").value(false))
                .andExpect(jsonPath("$.isProvisioned").value(false));
    }

    @Test
    void getAllotmentById_allotmentCRUD_missing() throws Exception {
        mockMvc.perform(get(baseUrl + "/999"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"Allotment summary with id = 999 not found\"}"));
    }

    @Test
    void getAllotmentByBatchId_allotmentCRUD_found() throws Exception {
        mockMvc.perform(get(baseUrl + "/batch/6"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].allotmentId").value(7))
                .andExpect(jsonPath("$.content[0].batchNumber").value("6"))
                .andExpect(jsonPath("$.content[0].allotmentType").value("PREPAID"))
                .andExpect(jsonPath("$.content[0].inventoryPool.inventoryPoolId").value(5))
                .andExpect(jsonPath("$.content[0].inventoryPool.code").value("METEOR_PAIRED_CUSTOMER_POOL"))
                .andExpect(jsonPath("$.content[0].inventoryPool.description").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].inventoryPool.mvno").value(0))
                .andExpect(jsonPath("$.content[0].inventoryPool.simProfile").value("DEFAULT"))
                .andExpect(jsonPath("$.content[0].quantity").value(10))
                .andExpect(jsonPath("$.content[0].packWithHandset").value(true))
                .andExpect(jsonPath("$.content[0].pricePlan").value("pricePlan"))
                .andExpect(jsonPath("$.content[0].initialCredit").value(10))
                .andExpect(jsonPath("$.content[0].preProvisioning").value(true))
                .andExpect(jsonPath("$.content[0].creationDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[0].isSentToLogistics").value(false))
                .andExpect(jsonPath("$.content[0].isProvisioned").value(false));
    }

    @Test
    void getAllotmentByBatchId_allotmentCRUD_missingBatch() throws Exception {
        mockMvc.perform(get(baseUrl + "/batch/999"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[],\"pageable\":{\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"pageSize\":20,\"pageNumber\":0,\"offset\":0,\"paged\":true,\"unpaged\":false},\"totalPages\":0,\"totalElements\":0,\"last\":true,\"number\":0,\"size\":20,\"numberOfElements\":0,\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"first\":true,\"empty\":true}"));
    }

    @Test
    void getAllotmentByBatchId_allotmentCRUD_noneFoundForBatch() throws Exception {
        mockMvc.perform(get(baseUrl + "/batch/61"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[],\"pageable\":{\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"pageSize\":20,\"pageNumber\":0,\"offset\":0,\"paged\":true,\"unpaged\":false},\"totalPages\":0,\"totalElements\":0,\"last\":true,\"number\":0,\"size\":20,\"numberOfElements\":0,\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"first\":true,\"empty\":true}"));
    }

    @Test
    void performAllotment_allotmentCRUD_failureWithoutEnoughAvailableSimCards() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"batchNumber\": \"61\", \"quantity\": 10, \"allotmentType\": \"PREPAID\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Quantity 10 is too high. There are 2 non-allotted SIM cards for batch 61\"}"));
    }

    @Test
    void performAllotment_allotmentCRUD_withFirstSerialNumber_failureWithoutEnoughAvailableSimCards() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"batchNumber\": \"61\",\"firstSerialNumber\": \"1111111111111111112\", \"quantity\": 10, \"allotmentType\": \"PREPAID\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Quantity 10 is too high. There are 2 non-allotted SIM cards for batch 61\"}"));
    }

    @Test
    void performAllotment_allotmentCRUD_successPrepaid() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"batchNumber\": \"61\", \"quantity\": 1, \"allotmentType\": \"PREPAID\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.allotmentId").value(8))
                .andExpect(jsonPath("$.batchNumber").value("61"))
                .andExpect(jsonPath("$.allotmentType").value("PREPAID"))
                .andExpect(jsonPath("$.inventoryPool.inventoryPoolId").value(5))
                .andExpect(jsonPath("$.inventoryPool.code").value("METEOR_PAIRED_CUSTOMER_POOL"))
                .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.inventoryPool.mvno").value(0))
                .andExpect(jsonPath("$.inventoryPool.simProfile").value("DEFAULT"))
                .andExpect(jsonPath("$.quantity").value(1))
                .andExpect(jsonPath("$.packWithHandset").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.pricePlan").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.initialCredit").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.preProvisioning").value(true))
                .andExpect(jsonPath("$.isSentToLogistics").value(false))
                .andExpect(jsonPath("$.isProvisioned").value(false));

        AllotmentSummary allotmentSummary = allotmentRepository.findById(8L).orElseThrow(() -> new Exception("Allotment should have been retrieved from repo."));
        Collection<SimCard> simcards = simCardRepository.findByAllotment(allotmentSummary);
        assertEquals(1, simcards.size());
        simcards.forEach(simcard -> {
            assertEquals(8L, (long) simcard.getAllotment().getAllotmentId());
        });
    }

    @Test
    void performAllotment_allotmentCRUD_successPrepaid_withFirstSerialNumber() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"batchNumber\": \"61\",\"firstSerialNumber\": \"1111111111111111112\", \"quantity\": 1, \"allotmentType\": \"PREPAID\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.allotmentId").value(8))
                .andExpect(jsonPath("$.batchNumber").value("61"))
                .andExpect(jsonPath("$.allotmentType").value("PREPAID"))
                .andExpect(jsonPath("$.inventoryPool.inventoryPoolId").value(5))
                .andExpect(jsonPath("$.inventoryPool.code").value("METEOR_PAIRED_CUSTOMER_POOL"))
                .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.inventoryPool.mvno").value(0))
                .andExpect(jsonPath("$.inventoryPool.simProfile").value("DEFAULT"))
                .andExpect(jsonPath("$.quantity").value(1))
                .andExpect(jsonPath("$.packWithHandset").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.pricePlan").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.initialCredit").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.preProvisioning").value(true))
                .andExpect(jsonPath("$.isSentToLogistics").value(false))
                .andExpect(jsonPath("$.isProvisioned").value(false));

        AllotmentSummary allotmentSummary = allotmentRepository.findById(8L).orElseThrow(() -> new Exception("Allotment should have been retrieved from repo."));
        Collection<SimCard> simcards = simCardRepository.findByAllotment(allotmentSummary);
        assertEquals(1, simcards.size());
        simcards.forEach(simcard -> {
            assertEquals(8L, (long) simcard.getAllotment().getAllotmentId());
        });
    }

    @Test
    void performAllotment_allotmentCRUD_successReplacement() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"batchNumber\": \"62\", \"quantity\": 2, \"allotmentType\": \"REPLACEMENT_SIM_CARD\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.allotmentId").value(8))
                .andExpect(jsonPath("$.batchNumber").value("62"))
                .andExpect(jsonPath("$.allotmentType").value("REPLACEMENT_SIM_CARD"))
                .andExpect(jsonPath("$.inventoryPool.inventoryPoolId").value(51))
                .andExpect(jsonPath("$.inventoryPool.code").value("TEST_REPLACEMENT"))
                .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.inventoryPool.mvno").value(0))
                .andExpect(jsonPath("$.inventoryPool.simProfile").value("REPLACEMENT"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.packWithHandset").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.pricePlan").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.initialCredit").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.preProvisioning").value(false))
                .andExpect(jsonPath("$.isSentToLogistics").value(false))
                .andExpect(jsonPath("$.isProvisioned").value(false));

        AllotmentSummary allotmentSummary = allotmentRepository.findById(8L).orElseThrow(() -> new Exception("Allotment should have been retrieved from repo."));
        Collection<SimCard> simcards = simCardRepository.findByAllotment(allotmentSummary);
        assertEquals(2, simcards.size());
        simcards.forEach(simcard -> {
            assertEquals(8L, (long) simcard.getAllotment().getAllotmentId());
        });
    }

    @Test
    void performAllotment_allotmentCRUD_successNotPrepaid_withFirstSerialNumber() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"batchNumber\": \"62\",\"firstSerialNumber\": \"1111111111111111114\", \"quantity\": 2, \"allotmentType\": \"REPLACEMENT_SIM_CARD\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.allotmentId").value(8))
                .andExpect(jsonPath("$.batchNumber").value("62"))
                .andExpect(jsonPath("$.allotmentType").value("REPLACEMENT_SIM_CARD"))
                .andExpect(jsonPath("$.inventoryPool.inventoryPoolId").value(51))
                .andExpect(jsonPath("$.inventoryPool.code").value("TEST_REPLACEMENT"))
                .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.inventoryPool.mvno").value(0))
                .andExpect(jsonPath("$.inventoryPool.simProfile").value("REPLACEMENT"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.packWithHandset").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.pricePlan").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.initialCredit").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.preProvisioning").value(false))
                .andExpect(jsonPath("$.isSentToLogistics").value(false))
                .andExpect(jsonPath("$.isProvisioned").value(false));

        AllotmentSummary allotmentSummary = allotmentRepository.findById(8L).orElseThrow(() -> new Exception("Allotment should have been retrieved from repo."));
        Collection<SimCard> simcards = simCardRepository.findByAllotment(allotmentSummary);
        assertEquals(2, simcards.size());
        simcards.forEach(simcard -> {
            assertEquals(8L, (long) simcard.getAllotment().getAllotmentId());
        });
    }

    @Test
    void provisioned_success() throws Exception {
        final long id = 7L;
        AllotmentProvisionedDTO dto = new AllotmentProvisionedDTO();
        dto.setAllotmentId(id);
        dto.setSuccessQuantity(10);
        dto.setFailures(List.of());
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl + "/{allotmentId}/provisioned", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.allotmentId").value(7))
                .andExpect(jsonPath("$.batchNumber").value("6"))
                .andExpect(jsonPath("$.allotmentType").value("PREPAID"))
                .andExpect(jsonPath("$.inventoryPool.inventoryPoolId").value(5))
                .andExpect(jsonPath("$.inventoryPool.code").value("METEOR_PAIRED_CUSTOMER_POOL"))
                .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.inventoryPool.mvno").value(0))
                .andExpect(jsonPath("$.inventoryPool.simProfile").value("DEFAULT"))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.packWithHandset").value(true))
                .andExpect(jsonPath("$.pricePlan").value("pricePlan"))
                .andExpect(jsonPath("$.initialCredit").value(10))
                .andExpect(jsonPath("$.preProvisioning").value(true))
                .andExpect(jsonPath("$.creationDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.isSentToLogistics").value(false))
                .andExpect(jsonPath("$.isProvisioned").value(true));
    }

    @Test
    void provisioned_notMatchingAllotment() throws Exception {
        final long id = 7L;
        AllotmentProvisionedDTO dto = new AllotmentProvisionedDTO();
        dto.setAllotmentId(8L);
        dto.setSuccessQuantity(10);
        dto.setFailures(List.of());
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl + "/{allotmentId}/provisioned", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Allotment id in request '8' does not match allotment id in body '7'\"}"));
    }

    @Test
    void provisioned_notFound() throws Exception {
        final long id = 20L;
        AllotmentProvisionedDTO dto = new AllotmentProvisionedDTO();
        dto.setAllotmentId(id);
        dto.setSuccessQuantity(10);
        dto.setFailures(List.of());
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl + "/{allotmentId}/provisioned", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"Allotment summary with id = 20 not found\"}"));
    }

    @Test
    void export_success() throws Exception {
        var dto = new AllotmentExportRequestDTO();
        dto.setAllotmentId(7L);
        dto.setFileConfigurationName("default_export");

        var expectedFile = "*************************************\n" +
                "* HEADER DESCRIPTION\n" +
                "*************************************\n" +
                "Customer: EPIC Cyprus\n" +
                "Quantity: 10\n" +
                "SIMType: PREPAID\n" +
                "Fields: Batch_ID Allotment_ID Article Sim_Number MSISDN PIN1 PUK1 PIN2 PUK2 Amount Product_Code\n" +
                "111111111111111 1111111111111111111 35790001261\n";

        MvcResult result = mockMvc.perform(post(baseUrl + "/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/force-download"))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), expectedFile);
    }
}
