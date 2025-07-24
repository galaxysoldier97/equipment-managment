package mc.monacotelecom.tecrep.equipments.integration.simcardgenerator;

import mc.monacotelecom.tecrep.equipments.dto.request.GenerateSimCardsRequestDTO;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import mc.monacotelecom.tecrep.equipments.process.simcardgenerator.SimCardIdentifiersGeneratorEir;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SimCardIdentifiersGeneratorEir.class})
@ActiveProfiles({"eir"})
@Sql({"/sql/clean.sql", "/sql/batch_data.sql", "/sql/sequence_numbers.sql"})
class SimCardGenerationEirTest extends BaseIntegrationTest {

    final String baseUrl = "/private/auth/batch";

    @Test
    void generateSimCard_quantityNull() throws Exception {
        GenerateSimCardsRequestDTO dto = new GenerateSimCardsRequestDTO();

        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl + "/6/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Rejected value 'null' for field 'quantity'\"}"));
    }

    @Test
    void generateSimCard_success_withEsim() throws Exception {
        GenerateSimCardsRequestDTO dto = new GenerateSimCardsRequestDTO();
        dto.setQuantity(3);
        dto.setEsim(true);
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl + "/6/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"filename\":\"TEST.out\",\"configuration\":{\"name\":\"TEST_SCGC\",\"exportFileConfiguration\":{\"name\":\"default_export\",\"prefix\":\"MMC\",\"suffix\":\".inp\",\"headerFormat\":\"<div>${CONTENT}</div>\",\"recordFormat\":\"<div>${CONTENT}</div>\"},\"importFileConfiguration\":{\"name\":\"default_import\",\"prefix\":\"MMC\",\"suffix\":\".out\",\"headerFormat\":null,\"recordFormat\":null},\"transportKey\":\"06\",\"artwork\":null,\"simReference\":null,\"type\":null,\"fixedPrefix\":\"8935303\",\"sequencePrefix\":\"0524\",\"algorithmVersion\":1,\"plmn\":{\"plmnId\":4,\"code\":\"27203\",\"networkName\":\"Meteor Mobile Telecommunications Limited\",\"tadigCode\":\"IRLME\",\"countryIsoCode\":\"IRL\",\"countryName\":\"Ireland\",\"rangesPrefix\":\"3368900890401\",\"links\":[]},\"msinSequence\":\"DEFAULT\",\"iccidSequence\":\"DEFAULT\",\"notify\":\"test@test.test\"},\"simCards\":[{\"equipmentId\":16,\"imsiNumber\":\"272030100000001\",\"serialNumber\":\"8935303052410000001\",\"number\":null},{\"equipmentId\":17,\"imsiNumber\":\"272030100000002\",\"serialNumber\":\"8935303052410000002\",\"number\":null},{\"equipmentId\":18,\"imsiNumber\":\"272030100000003\",\"serialNumber\":\"8935303052410000003\",\"number\":null}]}\n"));

        // Check the created simCards all have Esim set as True
        Collection<SimCard> createdSimCards = simCardRepository.findByBatchNumber("6");
        assertFalse(createdSimCards.isEmpty());
        for (SimCard simCard : createdSimCards) {
            assertTrue(simCard.isEsim());
        }
    }

    @Test
    void generateSimCard_sequencePrefixNull() throws Exception {
        GenerateSimCardsRequestDTO dto = new GenerateSimCardsRequestDTO();
        dto.setQuantity(3);
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl + "/63/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"SIM Card generation configuration with name 'EIR_NO_SEQUENCE_PREFIX' must have its 'sequencePrefix' valued\"}"));
    }

    @Test
    void generateSimCard_fixedPrefixNull() throws Exception {
        GenerateSimCardsRequestDTO dto = new GenerateSimCardsRequestDTO();
        dto.setQuantity(3);
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl + "/64/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"SIM Card generation configuration with name 'EIR_NO_FIXED_PREFIX' must have its 'fixedPrefix' valued\"}"));
    }

    @Test
    void generateSimCard_success_withoutEsim() throws Exception {
        GenerateSimCardsRequestDTO dto = new GenerateSimCardsRequestDTO();
        dto.setQuantity(3);
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl + "/6/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"filename\":\"TEST.out\",\"configuration\":{\"name\":\"TEST_SCGC\",\"exportFileConfiguration\":{\"name\":\"default_export\",\"prefix\":\"MMC\",\"suffix\":\".inp\",\"headerFormat\":\"<div>${CONTENT}</div>\",\"recordFormat\":\"<div>${CONTENT}</div>\"},\"importFileConfiguration\":{\"name\":\"default_import\",\"prefix\":\"MMC\",\"suffix\":\".out\",\"headerFormat\":null,\"recordFormat\":null},\"transportKey\":\"06\",\"artwork\":null,\"simReference\":null,\"type\":null,\"fixedPrefix\":\"8935303\",\"sequencePrefix\":\"0524\",\"algorithmVersion\":1,\"plmn\":{\"plmnId\":4,\"code\":\"27203\",\"networkName\":\"Meteor Mobile Telecommunications Limited\",\"tadigCode\":\"IRLME\",\"countryIsoCode\":\"IRL\",\"countryName\":\"Ireland\",\"rangesPrefix\":\"3368900890401\",\"links\":[]},\"msinSequence\":\"DEFAULT\",\"iccidSequence\":\"DEFAULT\",\"notify\":\"test@test.test\"},\"simCards\":[{\"equipmentId\":16,\"imsiNumber\":\"272030100000001\",\"serialNumber\":\"8935303052410000001\",\"number\":null},{\"equipmentId\":17,\"imsiNumber\":\"272030100000002\",\"serialNumber\":\"8935303052410000002\",\"number\":null},{\"equipmentId\":18,\"imsiNumber\":\"272030100000003\",\"serialNumber\":\"8935303052410000003\",\"number\":null}]}\n"));

        // Check the created simCards all have Esim set as false
        Collection<SimCard> createdSimCards = simCardRepository.findByBatchNumber("6");
        assertFalse(createdSimCards.isEmpty());
        for (SimCard simCard : createdSimCards) {
            assertFalse(simCard.isEsim());
        }

    }
}
