package mc.monacotelecom.tecrep.equipments.service;

import mc.monacotelecom.tecrep.equipments.dto.RevisionDTO;
import mc.monacotelecom.tecrep.equipments.dto.SimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.ChangeStatusDto;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddSimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchSimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateSimCardDTO;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;

@Deprecated(since = "2.21.0", forRemoval = true)
public interface ISimCardServiceV1 {

    @Deprecated(since = "2.21.0", forRemoval = true)
    SimCardDTO getByIdV1(Long simCardId);

    @Deprecated(since = "2.21.0", forRemoval = true)
    SimCardDTO getByICCIDV1(String iccid);

    @Deprecated(since = "2.21.0", forRemoval = true)
    SimCardDTO getByIMSIV1(String imsi);

    @Deprecated(since = "2.21.0", forRemoval = true)
    PagedModel<SimCardDTO> searchV1(SearchSimCardDTO searchSimCardDTO, Pageable pageable, PagedResourcesAssembler<SimCard> assembler);

    @Deprecated(since = "2.21.0", forRemoval = true)
    PagedModel<SimCardDTO> getAllV1(Pageable pageable, PagedResourcesAssembler<SimCard> assembler);

    @Deprecated(since = "2.21.0", forRemoval = true)
    PagedModel<RevisionDTO<SimCardDTO>> findRevisionsV1(Long equipmentId, Pageable pageable, PagedResourcesAssembler<Revision<Integer, SimCard>> assembler);

    @Deprecated(since = "2.21.0", forRemoval = true)
    SimCardDTO addV1(AddSimCardDTO simCardDto);

    @Deprecated(since = "2.21.0", forRemoval = true)
    SimCardDTO updateV1(Long simCardId, UpdateSimCardDTO dto);

    @Deprecated(since = "2.21.0", forRemoval = true)
    SimCardDTO partialUpdateByIdV1(Long simCardId, UpdateSimCardDTO dto);

    @Deprecated(since = "2.21.0", forRemoval = true)
    SimCardDTO partialUpdateByICCIDV1(String iccid, UpdateSimCardDTO dto);

    @Deprecated(since = "2.21.0", forRemoval = true)
    SimCardDTO partialUpdateByIMSIV1(String imsi, UpdateSimCardDTO dto);

    @Deprecated(since = "2.21.0", forRemoval = true)
    SimCardDTO updatePackByIdV1(Long simCardId, UpdateSimCardDTO dto);

    @Deprecated(since = "2.21.0", forRemoval = true)
    SimCardDTO updatePackByIMSIV1(UpdateSimCardDTO dto);

    @Deprecated(since = "2.21.0", forRemoval = true)
    SimCardDTO changeStateByIdV1(Long simCardId, Event event, ChangeStatusDto changeStatusDto);

    @Deprecated(since = "2.21.0", forRemoval = true)
    SimCardDTO changeStateByICCIDV1(String iccid, Event event, ChangeStatusDto changeStatusDto);

    @Deprecated(since = "2.21.0", forRemoval = true)
    SimCardDTO changeStateByIMSIV1(String imsi, Event event, ChangeStatusDto changeStatusDto);
}
