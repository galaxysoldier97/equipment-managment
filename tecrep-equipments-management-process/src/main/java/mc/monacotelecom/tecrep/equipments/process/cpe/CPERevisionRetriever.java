package mc.monacotelecom.tecrep.equipments.process.cpe;

import mc.monacotelecom.tecrep.equipments.assembler.RevisionCpeAssembler;
import mc.monacotelecom.tecrep.equipments.dto.CPEDTO;
import mc.monacotelecom.tecrep.equipments.dto.RevisionDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.CPEDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.RevisionDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.CPE;
import mc.monacotelecom.tecrep.equipments.mapper.CPEMapper;
import mc.monacotelecom.tecrep.equipments.repository.CPERepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class CPERevisionRetriever {
    private final CPERepository cpeRepository;
    private final RevisionCpeAssembler revisionCpeAssembler;
    private final CPEMapper cpeMapper;

    public CPERevisionRetriever(final CPERepository cpeRepository,
                                final ZoneId zoneId,
                                final CPEMapper cpeMapper) {
        this.cpeRepository = cpeRepository;
        this.cpeMapper = cpeMapper;
        this.revisionCpeAssembler = RevisionCpeAssembler.of(CPERevisionRetriever.class, zoneId, cpeMapper);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<RevisionDTO<CPEDTO>> findRevisionsV1(Long id, Pageable pageable, PagedResourcesAssembler<Revision<Integer, CPE>> assembler) {
        return assembler.toModel(cpeRepository.findRevisions(id, pageable), revisionCpeAssembler, linkTo(CPERevisionRetriever.class).slash("/").withSelfRel());
    }

    public Page<RevisionDTOV2<CPEDTOV2>> findRevisions(Long id, Pageable pageable) {
        return cpeRepository.findRevisions(id, pageable).map(cpeMapper::toRevisionDto);
    }
}
