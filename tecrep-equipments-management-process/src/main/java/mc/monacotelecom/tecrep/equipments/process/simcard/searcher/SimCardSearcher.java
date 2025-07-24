package mc.monacotelecom.tecrep.equipments.process.simcard.searcher;

import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.tecrep.equipments.dto.SimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchSimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.SimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.mapper.SimCardMapper;
import mc.monacotelecom.tecrep.equipments.repository.SimCardRepository;
import mc.monacotelecom.tecrep.equipments.state.StateMachineService;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("!epic")
public class SimCardSearcher extends AbstractSimCardSearcher {

    public SimCardSearcher(final StateMachineService stateMachineService,
                           final SimCardRepository simCardRepository,
                           final SimCardMapper simCardMapper) {
        super(stateMachineService, simCardRepository, simCardMapper);
    }

    @Override
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<SimCardDTO> searchV1(final SearchSimCardDTO searchSimCardDTO, final Pageable pageable, final PagedResourcesAssembler<SimCard> assembler) {
        final Specification<SimCard> specification = prepareSpecifications(searchSimCardDTO);
        Page<SimCard> simCards = simCardRepository.findAll(specification, pageable);
        return mapToDTOsV1(assembler, simCards);
    }

    @Override
    public Page<SimCardDTOV2> search(SearchSimCardDTO searchSimCardDTO, Pageable pageable) {
        final Specification<SimCard> specification = prepareSpecifications(searchSimCardDTO);
        Page<SimCard> simCards = simCardRepository.findAll(specification, pageable);
        return mapToDTOsV2(simCards);
    }
}
