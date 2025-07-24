package mc.monacotelecom.tecrep.equipments.integration;


import mc.monacotelecom.tecrep.equipments.dto.SequenceDTO;
import mc.monacotelecom.tecrep.equipments.entity.SequenceBatchNumber;
import mc.monacotelecom.tecrep.equipments.entity.SequenceICCID;
import mc.monacotelecom.tecrep.equipments.entity.SequenceMSIN;
import mc.monacotelecom.tecrep.equipments.enums.SequenceType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sql/clean.sql", "/sql/sequence_data.sql"})
class SequenceIntegrationTest extends BaseIntegrationTest {

    final String baseUrl = "/api/v1/private/auth/sequences";

    @Nested
    class BatchNumberSequence {
        @Test
        void get_success() throws Exception {
            mockMvc.perform(get(baseUrl + "/{Type}", SequenceType.BATCHNUMBER)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("[{\"first\":2,\"last\":8}]"));
        }

        @Test
        void get_missing() throws Exception {
            sequenceBatchNumberRepository.deleteAll();

            mockMvc.perform(get(baseUrl + "/{Type}", SequenceType.BATCHNUMBER)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("[]"));
        }

        @Test
        void add_success() throws Exception {
            SequenceDTO sequenceDTO = new SequenceDTO();
            sequenceDTO.setValue(10L);

            final String requestJson = objectMapper.writeValueAsString(sequenceDTO);

            mockMvc.perform(post(baseUrl + "/{Type}", SequenceType.BATCHNUMBER)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().json("{\"value\":10,\"name\":null}"));
        }

        @Test
        void add_failureWhenExists() throws Exception {
            SequenceDTO sequenceDTO = new SequenceDTO();
            sequenceDTO.setValue(2L);

            final String requestJson = objectMapper.writeValueAsString(sequenceDTO);

            mockMvc.perform(post(baseUrl + "/{Type}", SequenceType.BATCHNUMBER)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json("{\"errorMessage\":\"BatchNumber sequence with value '2' already exists\"}"));
        }

        @Test
        void delete_success() throws Exception {
            SequenceDTO sequenceDTO = new SequenceDTO();
            sequenceDTO.setValue(2L);
            final String requestJson = objectMapper.writeValueAsString(sequenceDTO);

            mockMvc.perform(delete(baseUrl + "/{Type}", SequenceType.BATCHNUMBER)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            List<SequenceBatchNumber> defaultSequence = sequenceBatchNumberRepository.findAll();
            assertFalse(defaultSequence.isEmpty());
            assertEquals(3L, defaultSequence.get(0).getValue());
        }

        @Test
        void deleteAll_success() throws Exception {
            SequenceDTO sequenceDTO = new SequenceDTO();
            sequenceDTO.setName("DEFAULT");
            final String requestJson = objectMapper.writeValueAsString(sequenceDTO);

            mockMvc.perform(delete(baseUrl + "/{Type}", SequenceType.BATCHNUMBER)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            assertTrue(sequenceBatchNumberRepository.findAll().isEmpty());
        }
    }

    @Nested
    class MSINSequence {
        @Test
        void get_success() throws Exception {
            mockMvc.perform(get(baseUrl + "/{Type}", SequenceType.MSIN)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("[{\"first\":11,\"last\":14,\"name\":\"DEFAULT\"},{\"first\":15,\"last\":16,\"name\":\"TEST\"}]"));
        }

        @Test
        void get_missing() throws Exception {
            sequenceMSINRepository.deleteAll();

            mockMvc.perform(get(baseUrl + "/{Type}", SequenceType.MSIN)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("[]"));
        }

        @Test
        void add_success() throws Exception {
            SequenceDTO sequenceDTO = new SequenceDTO();
            sequenceDTO.setValue(10L);
            sequenceDTO.setName("DEFAULT2");

            final String requestJson = objectMapper.writeValueAsString(sequenceDTO);

            mockMvc.perform(post(baseUrl + "/{Type}", SequenceType.MSIN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().json("{\"value\":10,\"name\":\"DEFAULT2\"}"));
        }

        @Test
        void add_failureWhenExists() throws Exception {
            SequenceDTO sequenceDTO = new SequenceDTO();
            sequenceDTO.setValue(11L);
            sequenceDTO.setName("DEFAULT");

            final String requestJson = objectMapper.writeValueAsString(sequenceDTO);

            mockMvc.perform(post(baseUrl + "/{Type}", SequenceType.MSIN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json("{\"errorMessage\":\"MSIN sequence with value '11' already exists\"}"));
        }

        @Test
        void delete_success() throws Exception {
            SequenceDTO sequenceDTO = new SequenceDTO();
            sequenceDTO.setName("DEFAULT");
            sequenceDTO.setValue(11L);
            final String requestJson = objectMapper.writeValueAsString(sequenceDTO);

            mockMvc.perform(delete(baseUrl + "/{Type}", SequenceType.MSIN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            List<SequenceMSIN> defaultSequence = sequenceMSINRepository.findByNameOrderByValueAsc("DEFAULT");
            assertFalse(defaultSequence.isEmpty());
            assertEquals(12L, defaultSequence.get(0).getValue());
        }

        @Test
        void deleteAll_success() throws Exception {
            SequenceDTO sequenceDTO = new SequenceDTO();
            sequenceDTO.setName("DEFAULT");
            final String requestJson = objectMapper.writeValueAsString(sequenceDTO);

            mockMvc.perform(delete(baseUrl + "/{Type}", SequenceType.MSIN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            assertTrue(sequenceMSINRepository.findByName("DEFAULT").isEmpty());
        }
    }

    @Nested
    class ICCIDSequence {
        @Test
        void get_success() throws Exception {
            mockMvc.perform(get(baseUrl + "/{Type}", SequenceType.ICCID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("[{\"first\":1,\"last\":4,\"name\":\"DEFAULT\"},{\"first\":5,\"last\":6,\"name\":\"TEST\"}]"));
        }

        @Test
        void get_missing() throws Exception {
            sequenceICCIDRepository.deleteAll();

            mockMvc.perform(get(baseUrl + "/{Type}", SequenceType.ICCID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("[]"));
        }

        @Test
        void add_success() throws Exception {
            SequenceDTO sequenceDTO = new SequenceDTO();
            sequenceDTO.setValue(10L);
            sequenceDTO.setName("DEFAULT2");

            final String requestJson = objectMapper.writeValueAsString(sequenceDTO);

            mockMvc.perform(post(baseUrl + "/{Type}", SequenceType.ICCID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().json("{\"value\":10,\"name\":\"DEFAULT2\"}"));
        }

        @Test
        void add_failureWhenExists() throws Exception {
            SequenceDTO sequenceDTO = new SequenceDTO();
            sequenceDTO.setValue(1L);
            sequenceDTO.setName("DEFAULT");

            final String requestJson = objectMapper.writeValueAsString(sequenceDTO);

            mockMvc.perform(post(baseUrl + "/{Type}", SequenceType.ICCID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json("{\"errorMessage\":\"ICCID sequence with value '1' already exists\"}"));
        }

        @Test
        void delete_success() throws Exception {
            SequenceDTO sequenceDTO = new SequenceDTO();
            sequenceDTO.setName("DEFAULT");
            sequenceDTO.setValue(1L);
            final String requestJson = objectMapper.writeValueAsString(sequenceDTO);

            mockMvc.perform(delete(baseUrl + "/{Type}", SequenceType.ICCID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            List<SequenceICCID> defaultSequence = sequenceICCIDRepository.findByNameOrderByValueAsc("DEFAULT");
            assertFalse(defaultSequence.isEmpty());
            assertEquals(2L, defaultSequence.get(0).getValue());
        }

        @Test
        void deleteAll_success() throws Exception {
            SequenceDTO sequenceDTO = new SequenceDTO();
            sequenceDTO.setName("DEFAULT");
            final String requestJson = objectMapper.writeValueAsString(sequenceDTO);

            mockMvc.perform(delete(baseUrl + "/{Type}", SequenceType.ICCID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            assertTrue(sequenceICCIDRepository.findByName("DEFAULT").isEmpty());
        }
    }
}
