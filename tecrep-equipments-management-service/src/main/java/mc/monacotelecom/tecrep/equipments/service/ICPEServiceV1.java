package mc.monacotelecom.tecrep.equipments.service;

import mc.monacotelecom.tecrep.equipments.dto.CPEDTO;
import mc.monacotelecom.tecrep.equipments.dto.RevisionDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.ChangeStatusDto;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddCPEDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchCpeDto;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateCPEDTO;
import mc.monacotelecom.tecrep.equipments.entity.CPE;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;

@Deprecated(since = "2.21.0", forRemoval = true)
public interface ICPEServiceV1 {

    @Deprecated(since = "2.21.0", forRemoval = true)
    CPEDTO getByIdV1(Long cpeId);

    @Deprecated(since = "2.21.0", forRemoval = true)
    PagedModel<CPEDTO> getAllV1(Pageable pageable, PagedResourcesAssembler<CPE> assembler);

    @Deprecated(since = "2.21.0", forRemoval = true)
    PagedModel<CPEDTO> searchV1(SearchCpeDto searchCpeDto, Pageable pageable, PagedResourcesAssembler<CPE> assembler);

    @Deprecated(since = "2.21.0", forRemoval = true)
    PagedModel<RevisionDTO<CPEDTO>> findRevisionsV1(Long cpeId, Pageable pageable, PagedResourcesAssembler<Revision<Integer, CPE>> assembler);

    @Deprecated(since = "2.21.0", forRemoval = true)
    CPEDTO addV1(AddCPEDTO cpedto);

    @Deprecated(since = "2.21.0", forRemoval = true)
    CPEDTO updateV1(Long id, UpdateCPEDTO dto);

    @Deprecated(since = "2.21.0", forRemoval = true)
    CPEDTO partialUpdateV1(Long id, UpdateCPEDTO dto);

    @Deprecated(since = "2.21.0", forRemoval = true)
    CPEDTO changeStatusV1(Long id, ChangeStatusDto changeStatusDto, Event event);
}
