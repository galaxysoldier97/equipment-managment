package mc.monacotelecom.tecrep.equipments.integration;

import mc.monacotelecom.tecrep.equipments.entity.AllotmentSummary;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.process.simcardgenerator.SimCardIdentifiersGeneratorEir;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SimCardIdentifiersGeneratorEir.class)
@ActiveProfiles({"eir"})
@Sql({"/sql/clean.sql", "/sql/allotment_data.sql"})
class AllotmentEirIntegrationTests extends BaseIntegrationTest {

    @Test
    void performAllotment_allotmentCRUD_failureWithoutPricePlan() throws Exception {
        mockMvc.perform(post("/private/auth/allotments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"batchNumber\": \"61\", \"quantity\": 3, \"allotmentType\": \"PREPAID\", \"initialCredit\": 10}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"The pricePlan and initialCredit parameters are mandatory when the Type is set to Prepaid\"}"));
    }

    @Test
    void performAllotment_allotmentCRUD_failureWithoutInitialCredit() throws Exception {
        mockMvc.perform(post("/private/auth/allotments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"batchNumber\": \"61\", \"quantity\": 3, \"allotmentType\": \"PREPAID\", \"pricePlan\": \"Something\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"The pricePlan and initialCredit parameters are mandatory when the Type is set to Prepaid\"}"));
    }

    @Test
    void performAllotment_allotmentCRUD_failureWithCreditTooHigh() throws Exception {
        mockMvc.perform(post("/private/auth/allotments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"batchNumber\": \"61\", \"quantity\": 3, \"allotmentType\": \"PREPAID\", \"pricePlan\": \"Something\", \"initialCredit\": 1000}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Initial Credit 1000 is too high. It cannot exceed 999\"}"));
    }

    @Test
    void performAllotment_allotmentCRUD_failureWithoutPackageWithHandset() throws Exception {
        mockMvc.perform(post("/private/auth/allotments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"batchNumber\": \"61\", \"quantity\": 3, \"allotmentType\": \"PREPAID\", \"pricePlan\": \"Something\", \"initialCredit\": 999}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"The 'packWithHandset' parameter is mandatory when the Inventory Pool is NOT REPLACEMENT\"}"));
    }

    @Test
    void performAllotment_allotmentCRUD_failureWithoutEnoughAvailableSimCards() throws Exception {
        mockMvc.perform(post("/private/auth/allotments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"batchNumber\": \"61\", \"quantity\": 10, \"allotmentType\": \"PREPAID\", \"pricePlan\": \"Something\", \"initialCredit\": 999, \"packWithHandset\": true}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Quantity 10 is too high. There are 2 non-allotted SIM cards for batch 61\"}"));
    }

    @Test
    void performAllotment_allotmentCRUD_successPrepaid() throws Exception {
        mockMvc.perform(post("/private/auth/allotments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"batchNumber\": \"61\", \"quantity\": 1, \"allotmentType\": \"PREPAID\", \"pricePlan\": \"Something\", \"initialCredit\": 999, \"packWithHandset\": true}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"allotmentId\":8,\"allotmentNumber\":1,\"batchNumber\":\"61\",\"allotmentType\":\"PREPAID\",\"inventoryPool\":{\"inventoryPoolId\":5,\"code\":\"METEOR_PAIRED_CUSTOMER_POOL\",\"description\":null,\"mvno\":0,\"simProfile\":\"DEFAULT\"},\"quantity\":1,\"packWithHandset\":true,\"pricePlan\":\"Something\",\"initialCredit\":999,\"preProvisioning\":true,\"isSentToLogistics\":false,\"isProvisioned\":false}"));

        AllotmentSummary allotmentSummary = allotmentRepository.findById(8L).orElseThrow(() -> new Exception("Allotment should have been retrieved from repo."));
        Collection<SimCard> simcards = simCardRepository.findByAllotment(allotmentSummary);
        assertEquals(1, simcards.size());
        assertEquals(1, allotmentSummary.getAllotmentNumber());
        simcards.forEach(simcard -> {
            assertEquals(8L, (long) simcard.getAllotment().getAllotmentId());
        });
    }

    @Test
    void performAllotment_allotmentCRUD_multiAllotments() throws Exception {
        mockMvc.perform(post("/private/auth/allotments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"batchNumber\": \"61\", \"quantity\": 1, \"allotmentType\": \"PREPAID\", \"pricePlan\": \"Something\", \"initialCredit\": 999, \"packWithHandset\": true}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"allotmentId\":8,\"allotmentNumber\":1,\"batchNumber\":\"61\",\"allotmentType\":\"PREPAID\",\"inventoryPool\":{\"inventoryPoolId\":5,\"code\":\"METEOR_PAIRED_CUSTOMER_POOL\",\"description\":null,\"mvno\":0,\"simProfile\":\"DEFAULT\"},\"quantity\":1,\"packWithHandset\":true,\"pricePlan\":\"Something\",\"initialCredit\":999,\"preProvisioning\":true,\"isSentToLogistics\":false,\"isProvisioned\":false}"));

        mockMvc.perform(post("/private/auth/allotments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"batchNumber\": \"61\", \"quantity\": 1, \"allotmentType\": \"PREPAID\", \"pricePlan\": \"Something\", \"initialCredit\": 999, \"packWithHandset\": true}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.allotmentId").value(9))
                .andExpect(jsonPath("$.allotmentNumber").value(2))
                .andExpect(jsonPath("$.batchNumber").value("61"))
                .andExpect(jsonPath("$.allotmentType").value("PREPAID"))
                .andExpect(jsonPath("$.inventoryPool.inventoryPoolId").value(5))
                .andExpect(jsonPath("$.inventoryPool.code").value("METEOR_PAIRED_CUSTOMER_POOL"))
                .andExpect(jsonPath("$.inventoryPool.description").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.inventoryPool.mvno").value(0))
                .andExpect(jsonPath("$.inventoryPool.simProfile").value("DEFAULT"))
                .andExpect(jsonPath("$.quantity").value(1))
                .andExpect(jsonPath("$.packWithHandset").value(true))
                .andExpect(jsonPath("$.pricePlan").value("Something"))
                .andExpect(jsonPath("$.initialCredit").value(999))
                .andExpect(jsonPath("$.preProvisioning").value(true))
                .andExpect(jsonPath("$.isSentToLogistics").value(false))
                .andExpect(jsonPath("$.isProvisioned").value(false));

        AllotmentSummary allotmentSummary = allotmentRepository.findById(9L).orElseThrow(() -> new Exception("Allotment should have been retrieved from repo."));
        Collection<SimCard> simcards = simCardRepository.findByAllotment(allotmentSummary);
        assertEquals(1, simcards.size());
        assertEquals(2, allotmentSummary.getAllotmentNumber());
    }
}
