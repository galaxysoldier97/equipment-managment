package mc.monacotelecom.tecrep.equipments.assembler;

import mc.monacotelecom.inventory.common.audit.AuditEnversInfo;
import mc.monacotelecom.tecrep.equipments.dto.CPEDTO;
import mc.monacotelecom.tecrep.equipments.dto.RevisionDTO;
import mc.monacotelecom.tecrep.equipments.entity.CPE;
import mc.monacotelecom.tecrep.equipments.mapper.CPEMapper;
import org.springframework.data.history.Revision;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class RevisionCpeAssembler extends RepresentationModelAssemblerSupport<Revision<Integer, CPE>, RevisionDTO<CPEDTO>> {

    private final CPEResourceAssembler cpeResourceAssembler;
    private final ZoneId zoneId;

    private static class RevisionCpeDTO extends RevisionDTO<CPEDTO> {
    }

    private RevisionCpeAssembler(Class<?> controllerClass, ZoneId zoneId, CPEMapper cpeMapper) {
        super(controllerClass, (Class<RevisionDTO<CPEDTO>>) RevisionCpeDTO.class.getSuperclass());
        this.cpeResourceAssembler = CPEResourceAssembler.of(controllerClass, cpeMapper);
        this.zoneId = zoneId;
    }

    public static RevisionCpeAssembler of(Class<?> controllerClass, ZoneId zoneId, CPEMapper cpeMapper) {
        return new RevisionCpeAssembler(controllerClass, zoneId, cpeMapper);
    }

    @Override
    public RevisionDTO<CPEDTO> toModel(Revision<Integer, CPE> revision) {
        RevisionDTO<CPEDTO> revisionCpeDTO = new RevisionDTO<>();
        revisionCpeDTO.setDate(revision.getRequiredRevisionInstant().atZone(zoneId).toLocalDateTime().truncatedTo(ChronoUnit.SECONDS));
        revisionCpeDTO.setEntity(cpeResourceAssembler.toModel(revision.getEntity()));
        revisionCpeDTO.setAuthor(((AuditEnversInfo) revision.getMetadata().getDelegate()).getUserId());
        return revisionCpeDTO;
    }
}
