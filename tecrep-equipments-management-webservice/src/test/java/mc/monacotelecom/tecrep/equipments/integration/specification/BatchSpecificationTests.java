package mc.monacotelecom.tecrep.equipments.integration.specification;

import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchBatchDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.BatchDTOV2;
import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql({"/sql/clean.sql", "/sql/batch_data.sql"})
class BatchSpecificationTests extends BaseIntegrationTest {

    @Test
    void search_withProcessableTrue() {
        SearchBatchDTO searchBatchDTO = new SearchBatchDTO();
        searchBatchDTO.setProcessable(true);

        Collection<BatchDTOV2> result = batchProcess.search(searchBatchDTO, pageable).getContent();

        assertEquals(3, result.size());
    }

    @Test
    void search_withProcessableFalse() {
        SearchBatchDTO searchBatchDTO = new SearchBatchDTO();
        searchBatchDTO.setProcessable(false);

        Collection<BatchDTOV2> result = batchProcess.search(searchBatchDTO, pageable).getContent();

        assertEquals(5, result.size());
    }

    @Test
    void search_withImportFileNameFound() {
        SearchBatchDTO searchBatchDTO = new SearchBatchDTO();
        searchBatchDTO.setImportFileName("TEST2.in");

        Collection<BatchDTOV2> result = batchProcess.search(searchBatchDTO, pageable).getContent();

        assertEquals(1, result.size());
        final var batchDTO = result.stream().findFirst().get();
        assertEquals((Long) 61L, batchDTO.getBatchNumber());
    }

    @Test
    void search_withImportFileNameNotFound() {
        SearchBatchDTO searchBatchDTO = new SearchBatchDTO();
        searchBatchDTO.setImportFileName("UNKNOWN");

        Collection<BatchDTOV2> result = batchProcess.search(searchBatchDTO, pageable).getContent();

        assertEquals(0, result.size());
    }

    @Test
    void search_withImportFileNameNotValued() {
        SearchBatchDTO searchBatchDTO = new SearchBatchDTO();
        searchBatchDTO.setImportFileName("");

        Collection<BatchDTOV2> result = batchProcess.search(searchBatchDTO, pageable).getContent();

        assertEquals(5, result.size());
    }

    @Test
    void search_withExportFileNameFound() {
        SearchBatchDTO searchBatchDTO = new SearchBatchDTO();
        searchBatchDTO.setExportFileName("TEST.out");

        Collection<BatchDTOV2> result = batchProcess.search(searchBatchDTO, pageable).getContent();

        assertEquals(1, result.size());
        final var batchDTO = result.stream().findFirst().get();
        assertEquals((Long) 6L, batchDTO.getBatchNumber());
    }

    @Test
    void search_withExportFileNameNotFound() {
        SearchBatchDTO searchBatchDTO = new SearchBatchDTO();
        searchBatchDTO.setExportFileName("UNKNOWN");

        Collection<BatchDTOV2> result = batchProcess.search(searchBatchDTO, pageable).getContent();

        assertEquals(0, result.size());
    }

    @Test
    void search_withExportFileNameNotValued() {
        SearchBatchDTO searchBatchDTO = new SearchBatchDTO();
        searchBatchDTO.setExportFileName("");

        Collection<BatchDTOV2> result = batchProcess.search(searchBatchDTO, pageable).getContent();

        assertEquals(5, result.size());
    }

    @Test
    void search_withInventoryPoolCodeFound() {
        SearchBatchDTO searchBatchDTO = new SearchBatchDTO();
        searchBatchDTO.setInventoryPoolCode("TEST_REPLACEMENT");

        Collection<BatchDTOV2> result = batchProcess.search(searchBatchDTO, pageable).getContent();

        assertEquals(1, result.size());
        final var batchDTO = result.stream().findFirst().get();
        assertEquals((Long) 62L, batchDTO.getBatchNumber());
    }

    @Test
    void search_withInventoryPoolCodeNotFound() {
        SearchBatchDTO searchBatchDTO = new SearchBatchDTO();
        searchBatchDTO.setInventoryPoolCode("UNKNOWN");

        Collection<BatchDTOV2> result = batchProcess.search(searchBatchDTO, pageable).getContent();

        assertEquals(0, result.size());
    }

    @Test
    void search_withInventoryPoolCodeNotValued() {
        SearchBatchDTO searchBatchDTO = new SearchBatchDTO();
        searchBatchDTO.setInventoryPoolCode("");

        Collection<BatchDTOV2> result = batchProcess.search(searchBatchDTO, pageable).getContent();

        assertEquals(5, result.size());
    }

    @Test
    void search_withConfigurationNameFound() {
        SearchBatchDTO searchBatchDTO = new SearchBatchDTO();
        searchBatchDTO.setConfigurationName("TEST_SCGC");

        Collection<BatchDTOV2> result = batchProcess.search(searchBatchDTO, pageable).getContent();

        assertEquals(1, result.size());
        final var batchDTO = result.stream().findFirst().get();
        assertEquals((Long) 6L, batchDTO.getBatchNumber());
    }

    @Test
    void search_withConfigurationNameNotFound() {
        SearchBatchDTO searchBatchDTO = new SearchBatchDTO();
        searchBatchDTO.setConfigurationName("UNKNOWN");

        Collection<BatchDTOV2> result = batchProcess.search(searchBatchDTO, pageable).getContent();

        assertEquals(0, result.size());
    }

    @Test
    void search_withConfigurationNameNotValued() {
        SearchBatchDTO searchBatchDTO = new SearchBatchDTO();
        searchBatchDTO.setConfigurationName("");

        Collection<BatchDTOV2> result = batchProcess.search(searchBatchDTO, pageable).getContent();

        assertEquals(5, result.size());
    }
}
