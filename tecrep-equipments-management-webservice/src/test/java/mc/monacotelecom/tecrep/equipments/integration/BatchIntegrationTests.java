package mc.monacotelecom.tecrep.equipments.integration;


import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;

import java.io.InputStream;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sql/clean.sql", "/sql/batch_data.sql"})
class BatchIntegrationTests extends BaseIntegrationTest {

    final String baseUrl = "/private/auth/batch";

    @Test
    void retrieveAll_batchCRUD_returned() throws Exception {
        mockMvc.perform(get(baseUrl + "?size=3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].batchNumber").value(6))
                .andExpect(jsonPath("$.content[0].exportFileName").value("TEST.out"))
                .andExpect(jsonPath("$.content[0].importFileName").value("TEST.out"))
                .andExpect(jsonPath("$.content[0].creationDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[0].returnedDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[0].processedDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].inventoryPoolCode").value("METEOR_PAIRED_CUSTOMER_POOL"))
                .andExpect(jsonPath("$.content[0].configurationName").value("TEST_SCGC"))
                .andExpect(jsonPath("$.content[0].simCardsCount").value(0))
                .andExpect(jsonPath("$.content[1].batchNumber").value(61))
                .andExpect(jsonPath("$.content[1].exportFileName").value("TEST2.out"))
                .andExpect(jsonPath("$.content[1].importFileName").value("TEST2.in"))
                .andExpect(jsonPath("$.content[1].creationDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[1].returnedDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[1].processedDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[1].inventoryPoolCode").value("METEOR_PAIRED_CUSTOMER_POOL"))
                .andExpect(jsonPath("$.content[1].configurationName").value("EIRCOM_FCS_MOB_LANDLINE_UNPAIR"))
                .andExpect(jsonPath("$.content[1].simCardsCount").value(3))
                .andExpect(jsonPath("$.content[2].batchNumber").value(62))
                .andExpect(jsonPath("$.content[2].exportFileName").value("TEST3.out"))
                .andExpect(jsonPath("$.content[2].importFileName").value("TEST3.in"))
                .andExpect(jsonPath("$.content[2].creationDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[2].returnedDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[2].processedDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[2].inventoryPoolCode").value("TEST_REPLACEMENT"))
                .andExpect(jsonPath("$.content[2].configurationName").value("EIRCOM_FCS_MOB_LANDLINE_UNPAIR"))
                .andExpect(jsonPath("$.content[2].simCardsCount").value(2));
    }

    @Test
    void retrieveAll_batchCRUD_withSort() throws Exception {
        mockMvc.perform(get(baseUrl + "?size=3&sort=batchNumber,desc"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].batchNumber").value(64))
                .andExpect(jsonPath("$.content[0].exportFileName").value("TEST5.out"))
                .andExpect(jsonPath("$.content[0].importFileName").value("TEST5.out"))
                .andExpect(jsonPath("$.content[0].creationDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[0].returnedDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[0].processedDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].inventoryPoolCode").value("METEOR_PAIRED_CUSTOMER_POOL"))
                .andExpect(jsonPath("$.content[0].configurationName").value("EIR_NO_FIXED_PREFIX"))
                .andExpect(jsonPath("$.content[0].simCardsCount").value(1))
                .andExpect(jsonPath("$.content[1].batchNumber").value(63))
                .andExpect(jsonPath("$.content[1].exportFileName").value("TEST4.out"))
                .andExpect(jsonPath("$.content[1].importFileName").value("TEST4.out"))
                .andExpect(jsonPath("$.content[1].creationDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[1].returnedDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[1].processedDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[1].inventoryPoolCode").value("METEOR_PAIRED_CUSTOMER_POOL"))
                .andExpect(jsonPath("$.content[1].configurationName").value("EIR_NO_SEQUENCE_PREFIX"))
                .andExpect(jsonPath("$.content[1].simCardsCount").value(0))
                .andExpect(jsonPath("$.content[2].batchNumber").value(62))
                .andExpect(jsonPath("$.content[2].exportFileName").value("TEST3.out"))
                .andExpect(jsonPath("$.content[2].importFileName").value("TEST3.in"))
                .andExpect(jsonPath("$.content[2].creationDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[2].returnedDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[2].processedDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[2].inventoryPoolCode").value("TEST_REPLACEMENT"))
                .andExpect(jsonPath("$.content[2].configurationName").value("EIRCOM_FCS_MOB_LANDLINE_UNPAIR"))
                .andExpect(jsonPath("$.content[2].simCardsCount").value(2));
    }

    @Test
    void search_batchCRUD_withImportFileName() throws Exception {
        mockMvc.perform(get(baseUrl + "/search?importFileName=TEST3.in"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].batchNumber").value(62))
                .andExpect(jsonPath("$.content[0].exportFileName").value("TEST3.out"))
                .andExpect(jsonPath("$.content[0].importFileName").value("TEST3.in"))
                .andExpect(jsonPath("$.content[0].creationDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[0].returnedDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[0].processedDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[0].inventoryPoolCode").value("TEST_REPLACEMENT"))
                .andExpect(jsonPath("$.content[0].configurationName").value("EIRCOM_FCS_MOB_LANDLINE_UNPAIR"))
                .andExpect(jsonPath("$.content[0].simCardsCount").value(2));
    }

    @Test
    void search_batchCRUD_withExportFileName() throws Exception {
        mockMvc.perform(get(baseUrl + "/search?exportFileName=TEST2.out"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].batchNumber").value(61))
                .andExpect(jsonPath("$.content[0].exportFileName").value("TEST2.out"))
                .andExpect(jsonPath("$.content[0].importFileName").value("TEST2.in"))
                .andExpect(jsonPath("$.content[0].creationDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[0].returnedDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].processedDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].inventoryPoolCode").value("METEOR_PAIRED_CUSTOMER_POOL"))
                .andExpect(jsonPath("$.content[0].configurationName").value("EIRCOM_FCS_MOB_LANDLINE_UNPAIR"))
                .andExpect(jsonPath("$.content[0].simCardsCount").value(3));
    }

    @Test
    void search_batchCRUD_withInventoryPoolCode() throws Exception {
        mockMvc.perform(get(baseUrl + "/search?inventoryPoolCode=TEST_REPLACEMENT"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].batchNumber").value(62))
                .andExpect(jsonPath("$.content[0].exportFileName").value("TEST3.out"))
                .andExpect(jsonPath("$.content[0].importFileName").value("TEST3.in"))
                .andExpect(jsonPath("$.content[0].creationDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[0].returnedDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[0].processedDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[0].inventoryPoolCode").value("TEST_REPLACEMENT"))
                .andExpect(jsonPath("$.content[0].configurationName").value("EIRCOM_FCS_MOB_LANDLINE_UNPAIR"))
                .andExpect(jsonPath("$.content[0].simCardsCount").value(2));
    }

    @Test
    void search_batchCRUD_withConfigurationName() throws Exception {
        mockMvc.perform(get(baseUrl + "/search?configurationName=TEST_SCGC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].batchNumber").value(6))
                .andExpect(jsonPath("$.content[0].exportFileName").value("TEST.out"))
                .andExpect(jsonPath("$.content[0].importFileName").value("TEST.out"))
                .andExpect(jsonPath("$.content[0].creationDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[0].returnedDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[0].processedDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].inventoryPoolCode").value("METEOR_PAIRED_CUSTOMER_POOL"))
                .andExpect(jsonPath("$.content[0].configurationName").value("TEST_SCGC"))
                .andExpect(jsonPath("$.content[0].simCardsCount").value(0));
    }

    @Test
    void search_batchCRUD_withProcessable() throws Exception {
        mockMvc.perform(get(baseUrl + "/search?processable=true&size=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].batchNumber").value(6))
                .andExpect(jsonPath("$.content[0].exportFileName").value("TEST.out"))
                .andExpect(jsonPath("$.content[0].importFileName").value("TEST.out"))
                .andExpect(jsonPath("$.content[0].creationDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[0].returnedDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.content[0].processedDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].inventoryPoolCode").value("METEOR_PAIRED_CUSTOMER_POOL"))
                .andExpect(jsonPath("$.content[0].configurationName").value("TEST_SCGC"))
                .andExpect(jsonPath("$.content[0].simCardsCount").value(0));
    }

    @Test
    void create_batchCRUD_failureWithoutInventoryPool() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"configurationName\": \"EIRCOM_FCS_MOB_LANDLINE_UNPAIR\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_batchCRUD_failureWithoutConfigurationName() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"inventoryPoolCode\": \"METEOR_PAIRED_CUSTOMER_POOL\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_batchCRUD_success() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"inventoryPoolCode\": \"METEOR_PAIRED_CUSTOMER_POOL\", \"configurationName\": \"EIRCOM_FCS_MOB_LANDLINE_UNPAIR\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batchNumber").value(63))
                .andExpect(jsonPath("$.exportFileName").value("MMC00063.inp"))
                .andExpect(jsonPath("$.inventoryPoolCode").value("METEOR_PAIRED_CUSTOMER_POOL"))
                .andExpect(jsonPath("$.configurationName").value("EIRCOM_FCS_MOB_LANDLINE_UNPAIR"))
                .andExpect(jsonPath("$.simCardsCount").value(0));
    }

    @Test
    void download_importFile_success() throws Exception {
        InputStream is = mock(InputStream.class);
        // Mock the file as InputStream
        // TODO: Find a way to mock the content of the file
        when(is.read(any()))
                .thenReturn(-1);
        doReturn(is).when(batchProcess).getImportFile("TEST2.in");
        mockMvc.perform(get("/private/auth/batch/61/download-import"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void download_importFile_missing() throws Exception {
        mockMvc.perform(get(baseUrl + "/61/download-import"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"Expected import file 'TEST2.in' has not been found\"}"));
    }

    @Test
    void download_importFile_missingBatch() throws Exception {
        mockMvc.perform(get(baseUrl + "/620/download-import"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"Batch with number '620' not found\"}"));
    }

    @Test
    void upload_importFile_success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "importSGC_OK.xlsx", "text/plain",
                new ClassPathResource("data/simCardGenerationConfiguration/import_OK.xlsx").getInputStream());

        doNothing().when(batchProcess).uploadImportFile(62L, file);
        mockMvc.perform(multipart(baseUrl + "/62/upload-import")
                        .file(file))
                .andDo(print())
                .andExpect(status().isCreated());
        verify(batchProcess).uploadImportFile(62L, file);
    }

    @Test
    void upload_importFile_missingBatch() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "importSGC_OK.xlsx", "text/plain",
                new ClassPathResource("data/simCardGenerationConfiguration/import_OK.xlsx").getInputStream());

        mockMvc.perform(multipart(baseUrl + "/620/upload-import")
                        .file(file))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"Batch with number '620' not found\"}"));
    }

    @Test
    void getByBatchNumber_success() throws Exception {
        final long id = 6L;
        mockMvc.perform(get(baseUrl + "/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batchNumber").value(6))
                .andExpect(jsonPath("$.exportFileName").value("TEST.out"))
                .andExpect(jsonPath("$.importFileName").value("TEST.out"))
                .andExpect(jsonPath("$.creationDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.returnedDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.processedDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.inventoryPoolCode").value("METEOR_PAIRED_CUSTOMER_POOL"))
                .andExpect(jsonPath("$.configurationName").value("TEST_SCGC"))
                .andExpect(jsonPath("$.simCardsCount").value(0));
    }

    @Test
    void getByBatchNumber_notFound() throws Exception {
        final long id = 1L;
        mockMvc.perform(get(baseUrl + "/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"Batch with number '1' not found\"}"));
    }

    @Test
    void deleteByBatchNumber_notFound() throws Exception {
        mockMvc.perform(delete(baseUrl + "/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"Batch with number '1' not found\"}"));
    }

    @Test
    void deleteByBatchNumber_failureIfAlloted() throws Exception {
        mockMvc.perform(delete(baseUrl + "/6"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Cannot delete batch with number '6' because it has already been alloted\"}"));

        assertTrue(batchRepository.findById(63L).isPresent());
    }

    @Test
    void deleteByBatchNumber_failureIfProcessed() throws Exception {
        mockMvc.perform(delete(baseUrl + "/62"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Cannot delete batch with number '62' because it has already been processed\"}"));

        assertTrue(batchRepository.findById(62L).isPresent());
        assertTrue(simCardRepository.findById(13L).isPresent(), "Simcards related to batch 62 should still exist");
    }

    @Test
    void deleteByBatchNumber_success() throws Exception {
        mockMvc.perform(delete(baseUrl + "/64"))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertFalse(batchRepository.findById(64L).isPresent());
        assertFalse(simCardRepository.findById(15L).isPresent(), "Simcards related to batch 64 should have been removed");
    }

    @Test
    void setProcessedDate_success() throws Exception {
        mockMvc.perform(patch(baseUrl + "/6/process"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batchNumber").value(6))
                .andExpect(jsonPath("$.exportFileName").value("TEST.out"))
                .andExpect(jsonPath("$.importFileName").value("TEST.out"))
                .andExpect(jsonPath("$.creationDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.returnedDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.processedDate").value("2020-05-31 20:35:24"))
                .andExpect(jsonPath("$.inventoryPoolCode").value("METEOR_PAIRED_CUSTOMER_POOL"))
                .andExpect(jsonPath("$.configurationName").value("TEST_SCGC"))
                .andExpect(jsonPath("$.simCardsCount").value(0));
    }

    @Test
    void setProcessedDate_missing() throws Exception {
        mockMvc.perform(patch(baseUrl + "/99/process"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"Batch with number '99' not found\"}"));
    }

    @Test
    void setReturnedDate_success() throws Exception {
        mockMvc.perform(patch(baseUrl + "/61/return"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batchNumber").value(61))
                .andExpect(jsonPath("$.exportFileName").value("TEST2.out"))
                .andExpect(jsonPath("$.importFileName").value("TEST2.in"))
                .andExpect(jsonPath("$.creationDate").value("2021-01-18 08:45:58"))
                .andExpect(jsonPath("$.returnedDate").value("2020-05-31 20:35:24"))
                .andExpect(jsonPath("$.processedDate").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.inventoryPoolCode").value("METEOR_PAIRED_CUSTOMER_POOL"))
                .andExpect(jsonPath("$.configurationName").value("EIRCOM_FCS_MOB_LANDLINE_UNPAIR"))
                .andExpect(jsonPath("$.simCardsCount").value(3));
    }

    @Test
    void setReturnedDate_missing() throws Exception {
        mockMvc.perform(patch(baseUrl + "/99/return"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"Batch with number '99' not found\"}"));
    }
}
