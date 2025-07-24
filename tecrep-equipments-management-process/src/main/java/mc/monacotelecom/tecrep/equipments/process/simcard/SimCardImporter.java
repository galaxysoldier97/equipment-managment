package mc.monacotelecom.tecrep.equipments.process.simcard;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.importer.ImportParameters;
import mc.monacotelecom.inventory.common.importer.dto.ImportHistoryDTO;
import mc.monacotelecom.inventory.common.importer.process.ImporterProcess;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.entity.Batch;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.repository.BatchRepository;
import mc.monacotelecom.tecrep.equipments.utils.IncomingFileResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.BATCH_NOT_FOUND;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.BATCH_NOT_FOUND_FOR_IMPORT_FILE_NAME;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "incoming", name = "files.processed-directory")
public class SimCardImporter {
    private final BatchRepository batchRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final IncomingFileResolver incomingFileResolver;
    private final ImporterProcess importerProcess;

    public ImportHistoryDTO importPrepayBatch(Long batchNumber, String sessionId, ImportParameters importParameters) {

        Batch batch = batchRepository.findById(batchNumber)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, BATCH_NOT_FOUND, batchNumber));

        return importPrepayBatch(batch, sessionId, importParameters);
    }

    public ImportHistoryDTO importPrepayBatch(String fileName, String sessionId, ImportParameters importParameters) {

        Batch batch = batchRepository.findByImportFileName(fileName)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, BATCH_NOT_FOUND_FOR_IMPORT_FILE_NAME, fileName));

        return importPrepayBatch(batch, sessionId, importParameters);
    }

    /**
     * Calls the SIM Card Idemia Import with a file already located on the file system
     *
     * @param batchEntity Related Batch
     * @param sessionId   Current session ID, for logging
     * @return Freshly-created import history
     */
    public ImportHistoryDTO importPrepayBatch(Batch batchEntity, String sessionId, ImportParameters importParameters) {

        File file = incomingFileResolver.getImportFile(batchEntity.getImportFileName());
        if (!file.exists()) {
            throw new EqmValidationException(localizedMessageBuilder, "Expected import file '" + batchEntity.getImportFileName() + "' has not been found");
        }

        MultipartFile multipartFile = incomingFileResolver.getMultipartFile(file);

        importParameters.addParameter("configuration", new String[]{"IDEMIA"});
        importParameters.addParameter("batchNumber", new String[]{String.valueOf(batchEntity.getBatchNumber())});

        return importerProcess.launchImport(sessionId, Optional.of(multipartFile), importParameters, "SimCard", file.getName());
    }
}
