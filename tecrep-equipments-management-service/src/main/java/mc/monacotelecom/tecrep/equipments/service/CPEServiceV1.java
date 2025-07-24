package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.CPEDTO;
import mc.monacotelecom.tecrep.equipments.dto.RevisionDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.ChangeStatusDto;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddCPEDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchCpeDto;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateCPEDTO;
import mc.monacotelecom.tecrep.equipments.entity.CPE;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.process.cpe.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Deprecated(since = "2.21.0", forRemoval = true)
public class CPEServiceV1 implements ICPEServiceV1 {

    private final CPERetriever cpeRetriever;
    private final CPESearcher cpeSearcher;
    private final CPERevisionRetriever cpeRevisionRetriever;
    private final CPECreator cpeCreator;
    private final CPEUpdater cpeUpdater;
    private final CPEStatusChanger cpeStatusChanger;

    @Override
    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public CPEDTO getByIdV1(Long cpeId) {
        return cpeRetriever.getByIdV1(cpeId);
    }

    @Override
    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<CPEDTO> getAllV1(Pageable pageable, PagedResourcesAssembler<CPE> assembler) {
        return cpeSearcher.getAllV1(pageable, assembler);
    }

    @Override
    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<CPEDTO> searchV1(SearchCpeDto searchCpeDto, Pageable pageable, PagedResourcesAssembler<CPE> assembler) {
        return cpeSearcher.searchV1(searchCpeDto, pageable, assembler);
    }

    @Override
    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<RevisionDTO<CPEDTO>> findRevisionsV1(Long cpeId, Pageable pageable, PagedResourcesAssembler<Revision<Integer, CPE>> assembler) {
        return cpeRevisionRetriever.findRevisionsV1(cpeId, pageable, assembler);
    }

    @Override
    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public CPEDTO addV1(AddCPEDTO cpedto) {
        return cpeCreator.addV1(cpedto);
    }

    @Override
    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public CPEDTO updateV1(Long id, UpdateCPEDTO dto) {
        return cpeUpdater.updateV1(id, dto);
    }

    @Override
    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public CPEDTO partialUpdateV1(Long id, UpdateCPEDTO dto) {
        return cpeUpdater.partialUpdateV1(id, dto);
    }

    @Override
    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public CPEDTO changeStatusV1(Long id, ChangeStatusDto changeStatusDto, Event event) {
        return cpeStatusChanger.applyEventV1(id, changeStatusDto, event);
    }
}
