package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.request.ChangeStatusDto;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddCPEDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchCpeDto;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateCPEDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.CPEDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.RevisionDTOV2;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.process.cpe.*;
import mc.monacotelecom.tecrep.equipments.projections.CpeUnmProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CPEService implements ICPEService {

    private final CPERetriever cpeRetriever;
    private final CPESearcher cpeSearcher;
    private final CPERevisionRetriever cpeRevisionRetriever;
    private final CPECreator cpeCreator;
    private final CPEUpdater cpeUpdater;
    private final CPEStatusChanger cpeStatusChanger;
    private final CPEDeleter cpeDeleter;
    private final CPEExporter cpeExporter;

    @Override
    @Transactional(readOnly = true)
    public List<CpeUnmProjection> getCpeIdsIn(List<Long> ids) {
        return cpeRetriever.getCpeIdsIn(ids);
    }

    @Override
    @Transactional
    public void delete(Long id, boolean forced) {
        cpeDeleter.delete(id, forced);
    }

    @Override
    @Transactional(readOnly = true)
    public ByteArrayInputStream export(final SearchCpeDto dto) {
        return cpeExporter.export(dto);
    }

    @Override
    @Transactional(readOnly = true)
    public CPEDTOV2 getById(Long cpeId) {
        return cpeRetriever.getById(cpeId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CPEDTOV2> search(SearchCpeDto dto, Pageable pageable) {
        return cpeSearcher.search(dto, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RevisionDTOV2<CPEDTOV2>> findRevisions(Long id, Pageable pageable) {
        return cpeRevisionRetriever.findRevisions(id, pageable);
    }

    @Override
    @Transactional
    public CPEDTOV2 add(AddCPEDTOV2 cpedto) {
        return cpeCreator.add(cpedto);
    }

    @Override
    @Transactional
    public CPEDTOV2 update(Long id, UpdateCPEDTOV2 dto) {
        return cpeUpdater.update(id, dto);
    }

    @Override
    @Transactional
    public CPEDTOV2 partialUpdate(Long id, UpdateCPEDTOV2 dto) {
        return cpeUpdater.partialUpdate(id, dto);
    }

    @Override
    @Transactional
    public CPEDTOV2 changeStatus(Long id, ChangeStatusDto changeStatusDto, Event event) {
        return cpeStatusChanger.applyEvent(id, changeStatusDto, event);
    }
}
