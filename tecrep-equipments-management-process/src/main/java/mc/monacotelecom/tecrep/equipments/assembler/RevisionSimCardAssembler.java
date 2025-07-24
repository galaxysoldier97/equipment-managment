package mc.monacotelecom.tecrep.equipments.assembler;

import mc.monacotelecom.inventory.common.audit.AuditEnversInfo;
import mc.monacotelecom.tecrep.equipments.dto.RevisionDTO;
import mc.monacotelecom.tecrep.equipments.dto.SimCardDTO;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.mapper.SimCardMapper;
import org.springframework.data.history.Revision;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class RevisionSimCardAssembler extends RepresentationModelAssemblerSupport<Revision<Integer, SimCard>, RevisionDTO<SimCardDTO>> {

    private final SimCardResourceAssembler simcardResourceAssembler;
    private final ZoneId zoneId;

    private static class RevisionSimcardDTO extends RevisionDTO<SimCardDTO> {
    }

    private RevisionSimCardAssembler(Class<?> controllerClass, ZoneId zoneId, SimCardMapper simCardMapper) {
        super(controllerClass, (Class<RevisionDTO<SimCardDTO>>) RevisionSimcardDTO.class.getSuperclass());
        this.simcardResourceAssembler = SimCardResourceAssembler.of(controllerClass, simCardMapper);
        this.zoneId = zoneId;
    }

    public static RevisionSimCardAssembler of(Class<?> controllerClass, ZoneId zoneId, SimCardMapper simCardMapper) {
        return new RevisionSimCardAssembler(controllerClass, zoneId, simCardMapper);
    }

    @Override
    public RevisionDTO<SimCardDTO> toModel(Revision<Integer, SimCard> revision) {
        RevisionDTO<SimCardDTO> revisionSimCardDTO = new RevisionDTO<>();
        revisionSimCardDTO.setDate(revision.getRequiredRevisionInstant().atZone(zoneId).toLocalDateTime().truncatedTo(ChronoUnit.SECONDS));
        revisionSimCardDTO.setEntity(simcardResourceAssembler.toModel(revision.getEntity()));
        revisionSimCardDTO.setAuthor(((AuditEnversInfo) revision.getMetadata().getDelegate()).getUserId());
        return revisionSimCardDTO;
    }
}

