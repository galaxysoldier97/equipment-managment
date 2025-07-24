package mc.monacotelecom.tecrep.equipments.process.simcard;

import mc.monacotelecom.tecrep.equipments.assembler.RevisionSimCardAssembler;
import mc.monacotelecom.tecrep.equipments.dto.RevisionDTO;
import mc.monacotelecom.tecrep.equipments.dto.SimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.RevisionDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.SimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.mapper.SimCardMapper;
import mc.monacotelecom.tecrep.equipments.repository.SimCardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class SimCardRevisionRetriever {
    private final SimCardRepository simCardRepository;
    private final RevisionSimCardAssembler revisionSimcardAssembler;
    private final SimCardMapper simCardMapper;

    public SimCardRevisionRetriever(final SimCardRepository simCardRepository,
                                    final ZoneId zoneId,
                                    final SimCardMapper simCardMapper) {
        this.simCardRepository = simCardRepository;
        this.simCardMapper = simCardMapper;
        this.revisionSimcardAssembler = RevisionSimCardAssembler.of(this.getClass(), zoneId, simCardMapper);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<RevisionDTO<SimCardDTO>> findRevisionsV1(Long equipmentId, Pageable pageable, PagedResourcesAssembler<Revision<Integer, SimCard>> assembler) {
        Page<Revision<Integer, SimCard>> revisions = simCardRepository.findRevisions(equipmentId, pageable);
        return assembler.toModel(revisions, revisionSimcardAssembler, linkTo(this.getClass()).slash("/").withSelfRel());
    }

    public Page<RevisionDTOV2<SimCardDTOV2>> findRevisions(Long id, Pageable pageable) {
        return simCardRepository.findRevisions(id, pageable).map(simCardMapper::toRevisionDto);
    }
}
