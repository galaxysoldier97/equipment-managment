package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.tecrep.equipments.dto.RevisionDTO;
import mc.monacotelecom.tecrep.equipments.dto.SimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.ChangeStatusDto;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddSimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchSimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateSimCardDTO;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.process.simcard.*;
import mc.monacotelecom.tecrep.equipments.process.simcard.searcher.ISimCardSearcher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Deprecated(since = "2.21.0", forRemoval = true)
public class SimCardServiceV1 implements ISimCardServiceV1 {

    private final SimCardRetriever simCardRetriever;
    private final ISimCardSearcher simCardSearcher;
    private final SimCardRevisionRetriever simCardRevisionRetriever;
    private final SimCardCreator simCardCreator;
    private final SimCardUpdater simCardUpdater;
    private final SimCardStatusChanger simCardStatusChanger;

    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO getByIdV1(Long simCardId) {
        return simCardRetriever.getByIdV1(simCardId);
    }

    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO getByICCIDV1(String iccid) {
        return simCardRetriever.getByICCIDV1(iccid);
    }

    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO getByIMSIV1(String imsi) {
        return simCardRetriever.getByIMSIV1(imsi);
    }

    // Not read-only, for on-the-fly SIM Card creation
    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<SimCardDTO> searchV1(SearchSimCardDTO searchSimCardDTO, Pageable pageable, PagedResourcesAssembler<SimCard> assembler) {
        return simCardSearcher.searchV1(searchSimCardDTO, pageable, assembler);
    }

    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<SimCardDTO> getAllV1(Pageable pageable, PagedResourcesAssembler<SimCard> assembler) {
        return simCardSearcher.getAllV1(pageable, assembler);
    }

    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<RevisionDTO<SimCardDTO>> findRevisionsV1(Long equipmentId, Pageable pageable, PagedResourcesAssembler<Revision<Integer, SimCard>> assembler) {
        return simCardRevisionRetriever.findRevisionsV1(equipmentId, pageable, assembler);
    }

    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO addV1(AddSimCardDTO simCardDto) {
        return simCardCreator.createAndMapV1(simCardDto);
    }

    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO updateV1(Long simCardId, UpdateSimCardDTO dto) {
        return simCardUpdater.updateV1(simCardId, dto);
    }

    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO partialUpdateByIdV1(Long simCardId, UpdateSimCardDTO dto) {
        return simCardUpdater.partialUpdateByIdV1(simCardId, dto);
    }

    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO partialUpdateByICCIDV1(String iccid, UpdateSimCardDTO dto) {
        return simCardUpdater.partialUpdateByIccidV1(iccid, dto);
    }

    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO partialUpdateByIMSIV1(String imsi, UpdateSimCardDTO dto) {
        return simCardUpdater.partialUpdateByImsiV1(imsi, dto);
    }

    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO updatePackByIdV1(Long simCardId, UpdateSimCardDTO dto) {
        return simCardUpdater.updatePackByIdV1(simCardId, dto);
    }

    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO updatePackByIMSIV1(UpdateSimCardDTO dto) {
        return simCardUpdater.updatePackByImsiV1(dto);
    }

    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO changeStateByIdV1(Long simCardId, Event event, ChangeStatusDto changeStatusDto) {
        return simCardStatusChanger.changeStateByIdV1(simCardId, changeStatusDto, event);
    }

    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO changeStateByICCIDV1(String iccid, Event event, ChangeStatusDto changeStatusDto) {
        return simCardStatusChanger.changeStateByICCIDV1(iccid, changeStatusDto, event);
    }

    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO changeStateByIMSIV1(String imsi, Event event, ChangeStatusDto changeStatusDto) {
        return simCardStatusChanger.changeStateByIMSIV1(imsi, changeStatusDto, event);
    }
}
