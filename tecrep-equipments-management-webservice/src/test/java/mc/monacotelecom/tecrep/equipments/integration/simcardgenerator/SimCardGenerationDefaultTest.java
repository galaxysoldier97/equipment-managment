package mc.monacotelecom.tecrep.equipments.integration.simcardgenerator;

import mc.monacotelecom.tecrep.equipments.dto.request.GenerateSimCardsRequestDTO;
import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import mc.monacotelecom.tecrep.equipments.process.simcardgenerator.SimCardIdentifiersGeneratorDefault;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SimCardIdentifiersGeneratorDefault.class})
@Sql({"/sql/clean.sql", "/sql/batch_data.sql", "/sql/sequence_numbers.sql"})
class SimCardGenerationDefaultTest extends BaseIntegrationTest {

    final String baseUrl = "/private/auth/batch";

    @Test
    void generateSimCard_noEnvironment() throws Exception {
        GenerateSimCardsRequestDTO dto = new GenerateSimCardsRequestDTO();
        dto.setQuantity(3);
        final String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(baseUrl + "/6/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Functionality not implemented for this environment\"}"));
    }
}
