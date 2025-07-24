package mc.monacotelecom.tecrep.equipments.integration;

import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Sql({"/sql/clean.sql", "/sql/audit_clean.sql"})
class AuditCleanerTest extends BaseIntegrationTest {

    final String baseSimCardUrl = "/private/auth/simcards";
    final String baseCpeUrl = "/private/auth/cpes";
    final String baseAncillaryUrl = "/private/auth/ancillaryequipments";

    final long simCardId = 20;
    final long cpeId = 42192;
    final long ancillaryId = 42193;

    @Nested
    @DisplayName("cleaning audit data for action: REPACKAGING to AVAILABLE")
    class cleaningAuditDataTest {
        @Test
        @DisplayName("test cleaning audit data for simcard")
        void success_simcard() throws Exception {

            var before = simCardRepository.findRevisions(simCardId).getContent().size();

            mockMvc.perform(patch(baseSimCardUrl + "/{id}/{event}", simCardId, Event.available)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(Status.AVAILABLE.name()));

            var after = simCardRepository.findRevisions(simCardId).getContent().size();

            assertAll(
                    () -> assertThat(before).isEqualTo(4),
                    () -> assertThat(after).isEqualTo(2)
            );
        }

        @Test
        @DisplayName("test cleaning audit data for cpe")
        void success_cpe() throws Exception {

            var before = cpeRepository.findRevisions(cpeId).getContent().size();

            mockMvc.perform(patch(baseCpeUrl + "/{id}/{event}", cpeId, Event.available)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(Status.AVAILABLE.name()));

            var after = cpeRepository.findRevisions(cpeId).getContent().size();

            assertAll(
                    () -> assertThat(before).isEqualTo(4),
                    () -> assertThat(after).isEqualTo(2)
            );
        }

        @Test
        @DisplayName("test cleaning audit data for ancillary")
        void success_ancillary() throws Exception {

            var before = ancillaryRepository.findRevisions(ancillaryId).getContent().size();

            mockMvc.perform(patch(baseAncillaryUrl + "/{id}/{event}", ancillaryId, Event.available)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(Status.AVAILABLE.name()));

            var after = ancillaryRepository.findRevisions(ancillaryId).getContent().size();

            assertAll(
                    () -> assertThat(before).isEqualTo(4),
                    () -> assertThat(after).isEqualTo(2)
            );
        }
    }
}
