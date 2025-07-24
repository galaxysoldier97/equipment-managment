package mc.monacotelecom.tecrep.equipments.service;

import mc.monacotelecom.tecrep.equipments.dto.request.ChangeStatusDto;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddCPEDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchCpeDto;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateCPEDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.CPEDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.RevisionDTOV2;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.projections.CpeUnmProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface ICPEService {

    CPEDTOV2 getById(Long cpeId);

    List<CpeUnmProjection> getCpeIdsIn(List<Long> ids);

    Page<CPEDTOV2> search(SearchCpeDto searchCpeDto, Pageable pageable);

    Page<RevisionDTOV2<CPEDTOV2>> findRevisions(Long cpeId, Pageable pageable);

    CPEDTOV2 add(AddCPEDTOV2 cpedto);

    CPEDTOV2 update(Long id, UpdateCPEDTOV2 dto);

    CPEDTOV2 partialUpdate(Long id, UpdateCPEDTOV2 dto);

    CPEDTOV2 changeStatus(Long id, ChangeStatusDto changeStatusDto, Event event);

    void delete(Long cpeId, boolean forced);

    ByteArrayInputStream export(final SearchCpeDto searchCpeDto);
}
