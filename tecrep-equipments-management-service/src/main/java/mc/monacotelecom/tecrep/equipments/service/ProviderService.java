package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.ProviderDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchProviderDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.ProviderDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.Provider;
import mc.monacotelecom.tecrep.equipments.process.ProviderProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ProviderService {

    private final ProviderProcess providerProcess;

    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<ProviderDTO> searchV1(SearchProviderDTO searchProviderDTO, Pageable pageable, PagedResourcesAssembler<Provider> assembler) {
        return providerProcess.searchV1(searchProviderDTO, pageable, assembler);
    }

    @Transactional(readOnly = true)
    public Page<ProviderDTOV2> search(SearchProviderDTO searchProviderDTO, Pageable pageable) {
        return providerProcess.search(searchProviderDTO, pageable);
    }

    @Transactional
    public void delete(Long providerId) {
        providerProcess.delete(providerId);
    }

    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public ProviderDTO getByIdV1(Long providerId) {
        return providerProcess.getByIdV1(providerId);
    }

    @Transactional(readOnly = true)
    public ProviderDTOV2 getById(Long providerId) {
        return providerProcess.getById(providerId);
    }

    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<ProviderDTO> getAll(Pageable pageable, PagedResourcesAssembler<Provider> assembler) {
        return providerProcess.getAll(pageable, assembler);
    }

    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public ProviderDTO updateV1(Long providerId, ProviderDTO providerDTO) {
        return providerProcess.updateV1(providerId, providerDTO);
    }

    @Transactional
    public ProviderDTOV2 update(Long providerId, ProviderDTOV2 providerDTO) {
        return providerProcess.update(providerId, providerDTO);
    }

    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public ProviderDTO addV1(ProviderDTO providerDTO) {
        return providerProcess.addV1(providerDTO);
    }

    @Transactional
    public ProviderDTOV2 add(ProviderDTOV2 providerDTO) {
        return providerProcess.add(providerDTO);
    }
}
