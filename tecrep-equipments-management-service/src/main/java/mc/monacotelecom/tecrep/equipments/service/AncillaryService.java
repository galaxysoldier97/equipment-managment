package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.ImportResultDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.AncillaryChangeStateDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddAncillaryDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchAncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateAncillaryEquipmentDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.AncillaryEquipmentDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.RevisionDTOV2;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.process.ancillary.*;
import mc.monacotelecom.tecrep.equipments.process.ancillary.searcher.IAncillarySearcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.List;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryImportJob;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryImportError;

@Service
@RequiredArgsConstructor
public class AncillaryService implements IAncillaryService {

    private final AncillaryRetriever ancillaryRetriever;
    private final IAncillarySearcher ancillarySearcher;
    private final AncillaryRevisionRetriever ancillaryRevisionRetriever;
    private final AncillaryCreator ancillaryCreator;
    private final AncillaryUpdater ancillaryUpdater;
    private final AncillaryStatusChanger ancillaryStatusChanger;
    private final AncillaryDeleter ancillaryDeleter;
    private final AncillaryExporter ancillaryExporter;
    private final AncillaryImportService ancillaryImportService;

    @Override
    @Transactional(readOnly = true)
    public AncillaryEquipmentDTOV2 getById(Long id) {
        return ancillaryRetriever.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public AncillaryEquipmentDTOV2 getBySerialNumber(String serialNumber) {
        return ancillaryRetriever.getBySerialNumber(serialNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public AncillaryEquipmentDTOV2 getByPairedEquipmentSerial(String serialNumber) {
        return ancillaryRetriever.getByPairedEquipmentSerial(serialNumber);
    }

    // Not read-only, for on-the-fly Ancillary creation
    @Override
    @Transactional
    public Page<AncillaryEquipmentDTOV2> search(SearchAncillaryEquipmentDTO dto, Pageable pageable) {
        return ancillarySearcher.search(dto, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RevisionDTOV2<AncillaryEquipmentDTOV2>> findRevisions(Long id, Pageable pageable) {
        return ancillaryRevisionRetriever.findRevisions(id, pageable);
    }

    @Override
    @Transactional
    public AncillaryEquipmentDTOV2 add(AddAncillaryDTOV2 dto) {
        return ancillaryCreator.add(dto);
    }

    @Override
    @Transactional
    public AncillaryEquipmentDTOV2 update(Long id, UpdateAncillaryEquipmentDTOV2 dto) {
        return ancillaryUpdater.update(id, dto);
    }

    @Override
    @Transactional
    public AncillaryEquipmentDTOV2 partialUpdate(Long id, UpdateAncillaryEquipmentDTOV2 dto) {
        return ancillaryUpdater.partialUpdate(id, dto);
    }

    @Override
    @Transactional
    public AncillaryEquipmentDTOV2 changeState(String id, AncillaryChangeStateDTO dto, Event event) {
        return ancillaryStatusChanger.applyEvent(id, dto, event);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ancillaryDeleter.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ByteArrayInputStream export(final SearchAncillaryEquipmentDTO dto) {
        return ancillaryExporter.export(dto);
    }

    @Override
    @Transactional
    public ImportResultDTO importFromFile(MultipartFile file, String format, boolean continueOnError) {
        return ancillaryImportService.processFile(file, format, continueOnError);
    }
  
    //scheduleImportJob
    @Override
    @Transactional
    public ImportResultDTO scheduleImportJob(MultipartFile file, String format, boolean continueOnError) {
        Long jobId = ancillaryImportService.scheduleImportJob(file, format, continueOnError);
        ImportResultDTO dto = new ImportResultDTO();
        dto.setJobId(jobId);
        dto.setStatus("PENDING");
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AncillaryImportJob> getImportJobById(Long id) {
        return ancillaryImportService.findJobById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AncillaryImportError> getImportErrorsByJobId(Long jobId) {
        return ancillaryImportService.findErrorsByJobId(jobId);
    }
}
