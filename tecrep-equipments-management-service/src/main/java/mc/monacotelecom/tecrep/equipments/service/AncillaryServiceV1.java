package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.AncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.RevisionDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.AncillaryChangeStateDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddAncillaryDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchAncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateAncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.process.ancillary.*;
import mc.monacotelecom.tecrep.equipments.process.ancillary.searcher.IAncillarySearcher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Deprecated(since = "2.21.0", forRemoval = true)
public class AncillaryServiceV1 implements IAncillaryServiceV1 {

    private final AncillaryRetriever ancillaryRetriever;
    private final IAncillarySearcher ancillarySearcher;
    private final AncillaryRevisionRetriever ancillaryRevisionRetriever;
    private final AncillaryCreator ancillaryCreator;
    private final AncillaryUpdater ancillaryUpdater;
    private final AncillaryStatusChanger ancillaryStatusChanger;

    @Override
    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public AncillaryEquipmentDTO getById(Long id) {
        return ancillaryRetriever.getByIdV1(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public AncillaryEquipmentDTO getBySerialNumber(String serialNumber) {
        return ancillaryRetriever.getBySerialNumberV1(serialNumber);
    }

    @Override
    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public AncillaryEquipmentDTO getByPairedEquipmentSerial(String serialNumber) {
        return ancillaryRetriever.getByPairedEquipmentSerialV1(serialNumber);
    }

    @Override
    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<AncillaryEquipmentDTO> getAll(Pageable pageable, PagedResourcesAssembler<AncillaryEquipment> assembler) {
        return ancillarySearcher.getAllV1(pageable, assembler);
    }

    // Not read-only, for on-the-fly Ancillary creation
    @Override
    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<AncillaryEquipmentDTO> search(SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO, Pageable pageable, PagedResourcesAssembler<AncillaryEquipment> assembler) {
        return ancillarySearcher.searchV1(searchAncillaryEquipmentDTO, pageable, assembler);
    }

    @Override
    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<RevisionDTO<AncillaryEquipmentDTO>> findRevisions(Long id, Pageable pageable, PagedResourcesAssembler<Revision<Integer, AncillaryEquipment>> assembler) {
        return ancillaryRevisionRetriever.findRevisionsV1(id, pageable, assembler);
    }

    @Override
    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public AncillaryEquipmentDTO add(AddAncillaryDTO dto) {
        return ancillaryCreator.addV1(dto);
    }

    @Override
    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public AncillaryEquipmentDTO update(Long id, UpdateAncillaryEquipmentDTO dto) {
        return ancillaryUpdater.updateV1(id, dto);
    }

    @Override
    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public AncillaryEquipmentDTO partialUpdate(Long id, UpdateAncillaryEquipmentDTO dto) {
        return ancillaryUpdater.partialUpdateV1(id, dto);
    }

    @Override
    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public AncillaryEquipmentDTO changeState(String id, AncillaryChangeStateDTO changeStateDTO, Event event) {
        return ancillaryStatusChanger.applyEventV1(id, changeStateDTO, event);
    }
}
