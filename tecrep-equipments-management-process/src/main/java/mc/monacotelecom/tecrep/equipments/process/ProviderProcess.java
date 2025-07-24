package mc.monacotelecom.tecrep.equipments.process;

import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.CommonFunctions;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.assembler.ProviderResourceAssembler;
import mc.monacotelecom.tecrep.equipments.dto.ProviderDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchProviderDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.ProviderDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.Provider;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.mapper.ProviderMapper;
import mc.monacotelecom.tecrep.equipments.repository.ProviderRepository;
import mc.monacotelecom.tecrep.equipments.repository.specification.ProviderSpecifications;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.PROVIDER_ID_NOT_FOUND;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.PROVIDER_NAME_ALREADY_EXISTS;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@Component
public class ProviderProcess {

    private final ProviderRepository providerRepository;
    private final ProviderResourceAssembler providerResourceAssembler;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final ProviderMapper providerMapper;

    public ProviderProcess(final ProviderRepository providerRepository,
                           final LocalizedMessageBuilder localizedMessageBuilder,
                           final ProviderMapper providerMapper) {
        this.providerRepository = providerRepository;
        this.localizedMessageBuilder = localizedMessageBuilder;
        this.providerMapper = providerMapper;
        providerResourceAssembler = ProviderResourceAssembler.of(ProviderProcess.class);
    }

    public void delete(Long providerId) {
        providerRepository.deleteById(providerId);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<ProviderDTO> searchV1(final SearchProviderDTO searchProviderDTO, final Pageable pageable, final PagedResourcesAssembler<Provider> assembler) {
        final Specification<Provider> specification = this.prepareSpecifications(searchProviderDTO);

        Page<Provider> providers = providerRepository.findAll(specification, pageable);

        return assembler.toModel(providers, providerResourceAssembler, linkTo(ProviderProcess.class).slash("/providers").withSelfRel());
    }

    public Page<ProviderDTOV2> search(final SearchProviderDTO searchProviderDTO, final Pageable pageable) {
        final Specification<Provider> specification = this.prepareSpecifications(searchProviderDTO);
        return providerRepository.findAll(specification, pageable).map(providerMapper::toDtoV2);
    }

    private Specification<Provider> prepareSpecifications(final SearchProviderDTO searchProviderDTO) {
        Specification<Provider> specification = null;

        specification = StringUtils.isNotBlank(searchProviderDTO.getName()) ? Specification.where(ProviderSpecifications.nameStartWith(searchProviderDTO.getName())) : specification;
        specification = Objects.nonNull(searchProviderDTO.getAccessType()) ? CommonFunctions.addSpecification(specification, ProviderSpecifications.hasAccessType(searchProviderDTO.getAccessType())) : specification;

        return specification;
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public ProviderDTO getByIdV1(Long providerId) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, PROVIDER_ID_NOT_FOUND, providerId));
        return providerResourceAssembler.toModel(provider);
    }

    public ProviderDTOV2 getById(Long providerId) {
        Provider provider = providerRepository.findById(providerId)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, PROVIDER_ID_NOT_FOUND, providerId));
        return providerMapper.toDtoV2(provider);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<ProviderDTO> getAll(Pageable pageable, PagedResourcesAssembler<Provider> assembler) {
        Page<Provider> providers = providerRepository.findAll(pageable);
        return assembler.toModel(providers, providerResourceAssembler, linkTo(ProviderProcess.class).slash("/providers").withSelfRel());
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public ProviderDTO updateV1(Long providerId, ProviderDTO providerDTO) {
        Provider provider = providerMapper.toEntity(providerDTO);
        providerDTO.setProviderId(providerId);
        return providerResourceAssembler.toModel(providerRepository.save(provider));
    }

    public ProviderDTOV2 update(Long providerId, ProviderDTOV2 providerDTO) {
        Provider provider = providerMapper.toEntity(providerDTO);
        provider.setProviderId(providerId);
        return providerMapper.toDtoV2(providerRepository.save(provider));
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public ProviderDTO addV1(ProviderDTO providerDTO) {
        Optional<Provider> providerOptional = providerRepository.findByName(providerDTO.getName());
        if (providerOptional.isPresent()) {
            throw new EqmValidationException(localizedMessageBuilder, PROVIDER_NAME_ALREADY_EXISTS, providerDTO.getName());
        }
        return providerResourceAssembler.toModel(providerRepository.save(providerMapper.toEntity(providerDTO)));
    }

    public ProviderDTOV2 add(ProviderDTOV2 providerDTO) {
        Optional<Provider> providerOptional = providerRepository.findByName(providerDTO.getName());
        if (providerOptional.isPresent()) {
            throw new EqmValidationException(localizedMessageBuilder, PROVIDER_NAME_ALREADY_EXISTS, providerDTO.getName());
        }
        return providerMapper.toDtoV2(providerRepository.save(providerMapper.toEntity(providerDTO)));
    }
}
