package mc.monacotelecom.tecrep.equipments.service;

import mc.monacotelecom.tecrep.equipments.dto.ImportResultDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.AncillaryChangeStateDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddAncillaryDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchAncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateAncillaryEquipmentDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.AncillaryEquipmentDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.RevisionDTOV2;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.List;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryImportJob;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryImportError;



public interface IAncillaryService {
    AncillaryEquipmentDTOV2 getById(Long id);

    AncillaryEquipmentDTOV2 getBySerialNumber(String serialNumber);

    AncillaryEquipmentDTOV2 getByPairedEquipmentSerial(String serialNumber);

    Page<AncillaryEquipmentDTOV2> search(SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTOV2, Pageable pageable);

    Page<RevisionDTOV2<AncillaryEquipmentDTOV2>> findRevisions(Long id, Pageable pageable);

    AncillaryEquipmentDTOV2 add(AddAncillaryDTOV2 dto);

    AncillaryEquipmentDTOV2 update(Long id, UpdateAncillaryEquipmentDTOV2 dto);

    AncillaryEquipmentDTOV2 partialUpdate(Long id, UpdateAncillaryEquipmentDTOV2 dto);

    AncillaryEquipmentDTOV2 changeState(String id, AncillaryChangeStateDTO changeStateDTO, Event event);

    void delete(Long id);

    ByteArrayInputStream export(final SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTOV2);

    ImportResultDTO importFromFile(MultipartFile file, String format, boolean continueOnError);
 
     /**
     * Inicia y persiste un job de importación asincrónico. Devuelve un DTO con jobId y estado inicial.
     */
    ImportResultDTO scheduleImportJob(MultipartFile file, String format, boolean continueOnError);

    /**
     * Devuelve Optional<AncillaryImportJob> para poder consultar el estado del job.
     */
    Optional<AncillaryImportJob> getImportJobById(Long jobId);

    /**
     * Lista los errores almacenados para un job de importación.
     */
    List<AncillaryImportError> getImportErrorsByJobId(Long jobId);

}