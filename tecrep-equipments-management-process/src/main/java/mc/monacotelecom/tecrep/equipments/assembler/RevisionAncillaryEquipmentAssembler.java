package mc.monacotelecom.tecrep.equipments.assembler;

import mc.monacotelecom.inventory.common.audit.AuditEnversInfo;
import mc.monacotelecom.tecrep.equipments.dto.AncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.RevisionDTO;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.entity.Equipment;
import mc.monacotelecom.tecrep.equipments.mapper.AncillaryMapper;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentRepository;
import org.springframework.data.history.Revision;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class RevisionAncillaryEquipmentAssembler extends RepresentationModelAssemblerSupport<Revision<Integer, AncillaryEquipment>, RevisionDTO<AncillaryEquipmentDTO>> {

    private final AncillaryEquipmentResourceAssembler ancillaryEquipmentResourceAssembler;
    private final ZoneId zoneId;

    private static class RevisionAncillaryEquipmentDTO extends RevisionDTO<AncillaryEquipmentDTO> {
    }

    private RevisionAncillaryEquipmentAssembler(Class<?> controllerClass,
                                                final EquipmentRepository<Equipment> equipmentRepository,
                                                final ZoneId zoneId,
                                                final AncillaryMapper ancillaryMapper) {
        super(controllerClass, (Class<RevisionDTO<AncillaryEquipmentDTO>>) RevisionAncillaryEquipmentAssembler.RevisionAncillaryEquipmentDTO.class.getSuperclass());
        this.ancillaryEquipmentResourceAssembler = AncillaryEquipmentResourceAssembler.of(controllerClass, equipmentRepository, ancillaryMapper);
        this.zoneId = zoneId;
    }

    public static RevisionAncillaryEquipmentAssembler of(Class<?> controllerClass, EquipmentRepository<Equipment> equipmentRepository, ZoneId zoneId, AncillaryMapper ancillaryMapper) {
        return new RevisionAncillaryEquipmentAssembler(controllerClass, equipmentRepository, zoneId, ancillaryMapper);
    }

    @Override
    public RevisionDTO<AncillaryEquipmentDTO> toModel(Revision<Integer, AncillaryEquipment> revision) {
        RevisionDTO<AncillaryEquipmentDTO> revisionAncillaryEquipmentDTO = new RevisionDTO<>();
        revisionAncillaryEquipmentDTO.setDate(revision.getRequiredRevisionInstant().atZone(zoneId).toLocalDateTime().truncatedTo(ChronoUnit.SECONDS));
        revisionAncillaryEquipmentDTO.setEntity(ancillaryEquipmentResourceAssembler.toModel(revision.getEntity()));
        revisionAncillaryEquipmentDTO.setAuthor(((AuditEnversInfo) revision.getMetadata().getDelegate()).getUserId());
        return revisionAncillaryEquipmentDTO;
    }

}
