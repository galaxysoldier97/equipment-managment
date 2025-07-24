package mc.monacotelecom.tecrep.equipments.service;

import mc.monacotelecom.tecrep.equipments.dto.AncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.RevisionDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.AncillaryChangeStateDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddAncillaryDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchAncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateAncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;

@Deprecated(since = "2.21.0", forRemoval = true)
public interface IAncillaryServiceV1 {
    AncillaryEquipmentDTO getById(Long id);

    AncillaryEquipmentDTO getBySerialNumber(String serialNumber);

    AncillaryEquipmentDTO getByPairedEquipmentSerial(String serialNumber);

    PagedModel<AncillaryEquipmentDTO> getAll(Pageable pageable, PagedResourcesAssembler<AncillaryEquipment> assembler);

    PagedModel<AncillaryEquipmentDTO> search(SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO, Pageable pageable, PagedResourcesAssembler<AncillaryEquipment> assembler);

    PagedModel<RevisionDTO<AncillaryEquipmentDTO>> findRevisions(Long id, Pageable pageable, PagedResourcesAssembler<Revision<Integer, AncillaryEquipment>> assembler);

    AncillaryEquipmentDTO add(AddAncillaryDTO dto);

    AncillaryEquipmentDTO update(Long id, UpdateAncillaryEquipmentDTO dto);

    AncillaryEquipmentDTO partialUpdate(Long id, UpdateAncillaryEquipmentDTO dto);

    AncillaryEquipmentDTO changeState(String id, AncillaryChangeStateDTO changeStateDTO, Event event);
}
