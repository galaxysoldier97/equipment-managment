package mc.monacotelecom.tecrep.equipments.process.simcard.searcher;

import mc.monacotelecom.tecrep.equipments.dto.SimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchSimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.SimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;

public interface ISimCardSearcher {
    @Deprecated(since = "2.21.0", forRemoval = true)
    PagedModel<SimCardDTO> getAllV1(Pageable pageable, PagedResourcesAssembler<SimCard> assembler);

    @Deprecated(since = "2.21.0", forRemoval = true)
    PagedModel<SimCardDTO> searchV1(final SearchSimCardDTO searchSimCardDTO, final Pageable pageable, final PagedResourcesAssembler<SimCard> assembler);

    Page<SimCardDTOV2> search(SearchSimCardDTO searchSimCardDTO, Pageable pageable);
}
