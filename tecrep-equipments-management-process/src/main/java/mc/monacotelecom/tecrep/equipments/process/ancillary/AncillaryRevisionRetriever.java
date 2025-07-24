package mc.monacotelecom.tecrep.equipments.process.ancillary;

import mc.monacotelecom.tecrep.equipments.assembler.RevisionAncillaryEquipmentAssembler;
import mc.monacotelecom.tecrep.equipments.dto.AncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.RevisionDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.AncillaryEquipmentDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.RevisionDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.entity.Equipment;
import mc.monacotelecom.tecrep.equipments.mapper.AncillaryMapper;
import mc.monacotelecom.tecrep.equipments.repository.AncillaryRepository;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class AncillaryRevisionRetriever {
    private final AncillaryRepository ancillaryRepository;
    private final RevisionAncillaryEquipmentAssembler revisionAncillaryEquipmentAssembler;
    private final AncillaryMapper ancillaryMapper;

    public AncillaryRevisionRetriever(final AncillaryRepository ancillaryRepository,
                                      final EquipmentRepository<Equipment> equipmentRepository,
                                      final ZoneId zoneId,
                                      final AncillaryMapper ancillaryMapper) {
        this.ancillaryRepository = ancillaryRepository;
        this.ancillaryMapper = ancillaryMapper;
        this.revisionAncillaryEquipmentAssembler = RevisionAncillaryEquipmentAssembler.of(AncillaryRevisionRetriever.class, equipmentRepository, zoneId, ancillaryMapper);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<RevisionDTO<AncillaryEquipmentDTO>> findRevisionsV1(Long id, Pageable pageable, PagedResourcesAssembler<Revision<Integer, AncillaryEquipment>> assembler) {
        Page<Revision<Integer, AncillaryEquipment>> revisions = ancillaryRepository.findRevisions(id, pageable);
        return assembler.toModel(revisions, revisionAncillaryEquipmentAssembler, linkTo(AncillaryRevisionRetriever.class).slash("/").withSelfRel());
    }

    public Page<RevisionDTOV2<AncillaryEquipmentDTOV2>> findRevisions(Long id, Pageable pageable) {
        return ancillaryRepository.findRevisions(id, pageable).map(ancillaryMapper::toRevisionDto);
    }
}
