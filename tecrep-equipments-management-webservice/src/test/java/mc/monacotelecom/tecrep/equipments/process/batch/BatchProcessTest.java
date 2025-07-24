package mc.monacotelecom.tecrep.equipments.process.batch;

import mc.monacotelecom.tecrep.equipments.entity.Batch;
import mc.monacotelecom.tecrep.equipments.exceptions.DeleteFileException;
import mc.monacotelecom.tecrep.equipments.process.BatchProcess;
import mc.monacotelecom.tecrep.equipments.repository.BatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles({"test"})
@Import(mc.monacotelecom.tecrep.equipments.resources.config.EqmTestConfiguration.class)
@MockBeans({
@MockBean(ServletContext.class)
})
class BatchProcessTest {
    @MockBean(name = "batchRepository", value = BatchRepository.class)
    private BatchRepository batchRepository;
    @Autowired
    BatchProcess batchProcess;

    @Value("${incoming.files.queue-directory:/tmp}")
    private String queueDirectory;
    private Batch batch;
    @BeforeEach
    void setup() {
        batch = new Batch();
        batch.setReturnedDate(LocalDateTime.now());
        batch.setImportFileName("some-file.inp");
        batch.setBatchNumber(1L);
    }
    @Test
    void testDeleteImportFile_success() {
        when(batchRepository.findById(1L)).thenReturn(Optional.ofNullable(batch));
        batchProcess.deleteImportFile(1L);
        verify(batchRepository, times(1)).save(batch);
    }

    @Test
    void testDeleteImportFile_processed() {
        batch.setProcessedDate(LocalDateTime.now());
        when(batchRepository.findById(1L)).thenReturn(Optional.ofNullable(batch));
        batchProcess.deleteImportFile(1L);
        verify(batchRepository, times(0)).save(batch);
    }

    @Test
    void testDeleteFile_ThrowsDeleteFileException() {
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            batch.setProcessedDate(null);
            when(batchRepository.findById(1L)).thenReturn(Optional.of(batch));
            mockedFiles.when(() -> Files.deleteIfExists(Path.of(queueDirectory, batch.getImportFileName()))).thenThrow(IOException.class);

            DeleteFileException exception = assertThrows(DeleteFileException.class, () -> batchProcess.deleteImportFile(1L));

            assertEquals("batch.delete.import.failure", exception.getMessageKey());
            assertInstanceOf(DeleteFileException.class, exception);
        }
    }
}
