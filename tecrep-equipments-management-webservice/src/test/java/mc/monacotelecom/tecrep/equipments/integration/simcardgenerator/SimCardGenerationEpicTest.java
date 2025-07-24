package mc.monacotelecom.tecrep.equipments.integration.simcardgenerator;

import mc.monacotelecom.tecrep.equipments.dto.request.GenerateSimCardsRequestDTO;
import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({"epic"})
@Sql({"/sql/clean.sql", "/sql/batch_data.sql", "/sql/sequence_numbers_epic.sql"})
class SimCardGenerationEpicTest extends BaseIntegrationTest {

    final String baseUrl = "/private/auth/batch";

    @Test
    void generateSimCard_isNullValue() throws Exception {
        GenerateSimCardsRequestDTO dto = new GenerateSimCardsRequestDTO();
        dto.setQuantity(3);
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl + "/6/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void generateSimCard_success() throws Exception {
        GenerateSimCardsRequestDTO dto = new GenerateSimCardsRequestDTO();
        dto.setQuantity(3);
        dto.setBrand("POSTPAID");
        dto.setSimCardType("MICRO");
        dto.setProvider("CALLSAT");
        dto.setProfile("32K");

        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl + "/6/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"filename\":\"TEST.out\",\"configuration\":{\"name\":\"TEST_SCGC\",\"exportFileConfiguration\":{\"name\":\"default_export\",\"prefix\":\"MMC\",\"suffix\":\".inp\",\"headerFormat\":\"<div>${CONTENT}</div>\",\"recordFormat\":\"<div>${CONTENT}</div>\"},\"importFileConfiguration\":{\"name\":\"default_import\",\"prefix\":\"MMC\",\"suffix\":\".out\",\"headerFormat\":null,\"recordFormat\":null},\"transportKey\":\"06\",\"artwork\":null,\"simReference\":null,\"type\":null,\"fixedPrefix\":\"8935303\",\"sequencePrefix\":\"0524\",\"algorithmVersion\":1,\"plmn\":{\"plmnId\":4,\"code\":\"27203\",\"networkName\":\"Meteor Mobile Telecommunications Limited\",\"tadigCode\":\"IRLME\",\"countryIsoCode\":\"IRL\",\"countryName\":\"Ireland\",\"rangesPrefix\":\"3368900890401\",\"links\":[]},\"msinSequence\":\"DEFAULT\",\"iccidSequence\":\"DEFAULT\",\"notify\":\"test@test.test\"},\"simCards\":[{\"equipmentId\":16,\"imsiNumber\":\"280101528100006\",\"serialNumber\":\"893578250121100006\",\"number\":null},{\"equipmentId\":17,\"imsiNumber\":\"280101528100007\",\"serialNumber\":\"893578250121100007\",\"number\":null},{\"equipmentId\":18,\"imsiNumber\":\"280101528100008\",\"serialNumber\":\"893578250121100008\",\"number\":null}]}\n"));
    }

    @Test
    void generateSimCard_valueNotMapped() throws Exception {
        GenerateSimCardsRequestDTO dto = new GenerateSimCardsRequestDTO();
        dto.setQuantity(3);
        dto.setBrand("POSTPAID_");
        dto.setSimCardType("MICRO");
        dto.setProvider("CALLSAT");
        dto.setProfile("32K");

        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl + "/6/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Configuration is not provided for value 'POSTPAID_'\"}"));
    }
}
