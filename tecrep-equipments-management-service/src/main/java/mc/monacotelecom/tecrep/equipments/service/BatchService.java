package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.tecrep.equipments.dto.BatchDTO;
import mc.monacotelecom.tecrep.equipments.dto.GenerateSimCardsResponseDTO;
import mc.monacotelecom.tecrep.equipments.dto.SimCardPartialDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.CreateBatchRequestDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.GenerateSimCardsRequestDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchBatchDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.BatchDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.GenerateSimCardsResponseDTOV2;
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
public class BatchService {

    private final BatchProcess batchProcess;
    private final SimCardGenerationConfigurationMapper simCardGenerationConfigurationMapper;

    @Transactional(readOnly = true)
    public Page<BatchDTOV2> getAll(Pageable pageable) {
        return batchProcess.getAll(pageable);
    }

    @Transactional(readOnly = true)
    public BatchDTOV2 getByBatchNumber(final Long batchNumber) {
        return batchProcess.getByBatchNumber(batchNumber);
    }

    @Transactional(readOnly = true)
    public Page<BatchDTOV2> search(SearchBatchDTO searchBatchDTO, Pageable pageable) {
        return batchProcess.search(searchBatchDTO, pageable);
    }

    @Transactional
    public BatchDTOV2 setReturnedDate(Long batchNumber) {
        return batchProcess.setReturnedDate(batchNumber);
    }

    @Transactional
    public BatchDTOV2 setReturnedDate(String importFileName) {
        return batchProcess.setReturnedDate(importFileName);
    }

    @Transactional
    public BatchDTOV2 setProcessedDate(Long batchNumber) {
        return batchProcess.setProcessedDate(batchNumber);
    }

    @Transactional
    public BatchDTOV2 createBatch(CreateBatchRequestDTO createBatchRequestDTO) {
        return batchProcess.createBatch(createBatchRequestDTO);
    }

    /**
     * Generates a quantity of SIM Cards in a given batch
     * Synchronized is here to prevent concurrent executions of SIM generations, that would induce database issues
     */
    public synchronized GenerateSimCardsResponseDTOV2 generateSimCards(Long batchNumber, GenerateSimCardsRequestDTO generateSimCardsRequestDTO) {
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
        return new GenerateSimCardsResponseDTOV2(batch.getExportFileName(), simCardGenerationConfigurationMapper.toDtoV2(configuration), simCardDTOs);
    }

    @Transactional
    public void uploadImportFile(final Long batchNumber, final MultipartFile file) {
        batchProcess.uploadImportFile(batchNumber, file);
    }

    @Transactional
    public void deleteImportFile(final Long batchNumber) {
        batchProcess.deleteImportFile(batchNumber);
    }

    public InputStream getImportFile(final String fileName) {
        return batchProcess.getImportFile(fileName);
    }

    @Transactional
    public void deleteByBatchNumber(final Long batchNumber) {
        batchProcess.deleteByBatchNumber(batchNumber);
    }
}
