package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.tecrep.equipments.dto.BatchDTO;
import mc.monacotelecom.tecrep.equipments.dto.GenerateSimCardsResponseDTO;
import mc.monacotelecom.tecrep.equipments.dto.SimCardPartialDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.CreateBatchRequestDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.GenerateSimCardsRequestDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchBatchDTO;
import mc.monacotelecom.tecrep.equipments.mapper.SimCardGenerationConfigurationMapper;
import mc.monacotelecom.tecrep.equipments.process.BatchProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Deprecated(since = "2.21.0", forRemoval = true)
public class BatchServiceV1 {

    private final BatchProcess batchProcess;
    private final SimCardGenerationConfigurationMapper simCardGenerationConfigurationMapper;

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Transactional(readOnly = true)
    public Page<BatchDTO> getAll(Pageable pageable) {
        return batchProcess.getAllV1(pageable);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Transactional(readOnly = true)
    public BatchDTO getByBatchNumber(final Long batchNumber) {
        return batchProcess.getByBatchNumberV1(batchNumber);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Transactional(readOnly = true)
    public Page<BatchDTO> search(SearchBatchDTO searchBatchDTO, Pageable pageable) {
        return batchProcess.searchV1(searchBatchDTO, pageable);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Transactional
    public BatchDTO setReturnedDate(Long batchNumber) {
        return batchProcess.setReturnedDateV1(batchNumber);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Transactional
    public BatchDTO setReturnedDate(String importFileName) {
        return batchProcess.setReturnedDateV1(importFileName);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Transactional
    public BatchDTO setProcessedDate(Long batchNumber) {
        return batchProcess.setProcessedDateV1(batchNumber);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Transactional
    public BatchDTO createBatch(CreateBatchRequestDTO createBatchRequestDTO) {
        return batchProcess.createBatchV1(createBatchRequestDTO);
    }


    /**
     * Generates a quantity of SIM Cards in a given batch
     * Synchronized is here to prevent concurrent executions of SIM generations, that would induce database issues
     */
    @Deprecated(since = "2.21.0", forRemoval = true)
    public synchronized GenerateSimCardsResponseDTO generateSimCards(Long batchNumber, GenerateSimCardsRequestDTO generateSimCardsRequestDTO) {
        final var batch = batchProcess.getEntityByBatchNumber(batchNumber);
        final var configuration = batch.getSimCardGenerationConfiguration();

        final List<String> numbers = generateSimCardsRequestDTO.getNumbers();

        var firstIccid = batchProcess.updateSequenceIccid(generateSimCardsRequestDTO.getQuantity(), configuration.getIccidSequence());
        var firstMsin = batchProcess.updateSequenceMsin(generateSimCardsRequestDTO.getQuantity(), configuration.getMsinSequence());

        final List<SimCardPartialDTO> simCardDTOs = new ArrayList<>();
        for (var index = 1; index <= generateSimCardsRequestDTO.getQuantity(); index++) {
            firstIccid++;
            firstMsin++;
            final String number = numbers.size() >= index ? numbers.get(index - 1) : null;
            simCardDTOs.add(batchProcess.generateSimCard(batch, configuration, generateSimCardsRequestDTO, number, firstIccid, firstMsin));
        }
        return new GenerateSimCardsResponseDTO(batch.getExportFileName(), simCardGenerationConfigurationMapper.toDto(configuration), simCardDTOs);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Transactional
    public void uploadImportFile(final Long batchNumber, final MultipartFile file) {
        batchProcess.uploadImportFile(batchNumber, file);
    }
    @Deprecated(since = "2.21.0", forRemoval = true)
    public InputStream getImportFile(final String fileName) {
        return batchProcess.getImportFile(fileName);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Transactional
    public void deleteByBatchNumber(final Long batchNumber) {
        batchProcess.deleteByBatchNumber(batchNumber);
    }
}
