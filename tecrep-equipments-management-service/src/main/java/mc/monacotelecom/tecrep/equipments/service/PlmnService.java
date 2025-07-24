package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.PlmnDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchPlmnDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.PlmnDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.Plmn;
import mc.monacotelecom.tecrep.equipments.process.PlmnProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlmnService {

    private final PlmnProcess plmnProcess;

    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<PlmnDTO> searchV1(SearchPlmnDTO searchPlmnDTO, Pageable pageable, PagedResourcesAssembler<Plmn> assembler) {
        return plmnProcess.searchV1(searchPlmnDTO, pageable, assembler);
    }

    @Transactional(readOnly = true)
    public Page<PlmnDTOV2> search(SearchPlmnDTO searchPlmnDTO, Pageable pageable) {
        return plmnProcess.search(searchPlmnDTO, pageable);
    }

    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<PlmnDTO> getAll(Pageable pageable, PagedResourcesAssembler<Plmn> assembler) {
        return plmnProcess.getAll(pageable, assembler);
    }

    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PlmnDTO updateV1(Long plmnId, PlmnDTO plmnDTO) {
        return plmnProcess.updateV1(plmnId, plmnDTO);
    }

    @Transactional
    public PlmnDTOV2 update(Long plmnId, PlmnDTOV2 plmnDTO) {
        return plmnProcess.update(plmnId, plmnDTO);
    }

    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PlmnDTO addV1(PlmnDTO plmnDTO) {
        return plmnProcess.addV1(plmnDTO);
    }

    @Transactional
    public PlmnDTOV2 add(PlmnDTOV2 plmnDTO) {
        return plmnProcess.add(plmnDTO);
    }

    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PlmnDTO getByIdV1(Long plmnId) {
        return plmnProcess.getByIdV1(plmnId);
    }

    @Transactional(readOnly = true)
    public PlmnDTOV2 getById(Long plmnId) {
        return plmnProcess.getById(plmnId);
    }

    @Transactional
    public void delete(Long plmnId) {
        plmnProcess.delete(plmnId);
    }
}
