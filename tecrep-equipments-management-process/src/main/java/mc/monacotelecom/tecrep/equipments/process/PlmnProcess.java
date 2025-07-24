package mc.monacotelecom.tecrep.equipments.process;

import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.CommonFunctions;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.assembler.PlmnResourceAssembler;
import mc.monacotelecom.tecrep.equipments.dto.PlmnDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchPlmnDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.PlmnDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.Plmn;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.mapper.PlmnMapper;
import mc.monacotelecom.tecrep.equipments.repository.PlmnRepository;
import mc.monacotelecom.tecrep.equipments.repository.specification.PlmnSpecifications;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@Component
public class PlmnProcess {

    private final PlmnRepository plmnRepository;
    private final PlmnResourceAssembler plmnResourceAssembler;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final PlmnMapper plmnMapper;

    public PlmnProcess(final PlmnRepository plmnRepository,
                       final LocalizedMessageBuilder localizedMessageBuilder,
                       final PlmnMapper plmnMapper) {
        this.plmnRepository = plmnRepository;
        this.localizedMessageBuilder = localizedMessageBuilder;
        this.plmnMapper = plmnMapper;
        this.plmnResourceAssembler = PlmnResourceAssembler.of(PlmnProcess.class);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<PlmnDTO> searchV1(final SearchPlmnDTO searchPlmnDTO, final Pageable pageable, final PagedResourcesAssembler<Plmn> assembler) {
        final Specification<Plmn> specification = this.prepareSpecifications(searchPlmnDTO);

        Page<Plmn> plmns = plmnRepository.findAll(specification, pageable);

        if (plmns.isEmpty()) {
            throw new EqmValidationException(localizedMessageBuilder, PLMN_NOT_FOUND);
        }
        return assembler.toModel(plmns, plmnResourceAssembler, linkTo(PlmnProcess.class).slash("/plmns").withSelfRel());
    }

    public Page<PlmnDTOV2> search(final SearchPlmnDTO searchPlmnDTO, final Pageable pageable) {
        final Specification<Plmn> specification = this.prepareSpecifications(searchPlmnDTO);
        return plmnRepository.findAll(specification, pageable).map(plmnMapper::toDtoV2);
    }

    private Specification<Plmn> prepareSpecifications(final SearchPlmnDTO searchPlmnDTO) {
        Specification<Plmn> specification = null;

        specification = StringUtils.isNotBlank(searchPlmnDTO.getCode()) ? Specification.where(PlmnSpecifications.hasCode(searchPlmnDTO.getCode())) : specification;
        specification = StringUtils.isNotBlank(searchPlmnDTO.getPrefix()) ? CommonFunctions.addSpecification(specification, PlmnSpecifications.prefixLike(searchPlmnDTO.getPrefix())) : specification;

        return specification;
    }

    public void delete(Long plmnId) {
        plmnRepository.deleteById(plmnId);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public PlmnDTO getByIdV1(Long plmnId) {
        Plmn plmn = plmnRepository.findById(plmnId)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, PLMN_ID_NOT_FOUND, plmnId));
        return plmnResourceAssembler.toModel(plmn);
    }

    public PlmnDTOV2 getById(Long plmnId) {
        Plmn plmn = plmnRepository.findById(plmnId)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, PLMN_ID_NOT_FOUND, plmnId));
        return plmnMapper.toDtoV2(plmn);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<PlmnDTO> getAll(Pageable pageable, PagedResourcesAssembler<Plmn> assembler) {
        Page<Plmn> plmns = plmnRepository.findAll(pageable);
        return assembler.toModel(plmns, plmnResourceAssembler, linkTo(PlmnProcess.class).slash("/plmns").withSelfRel());
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public PlmnDTO updateV1(Long plmnId, PlmnDTO plmnDTO) {
        if (!plmnRepository.existsById(plmnId)) {
            throw new EqmValidationException(localizedMessageBuilder, PLMN_ID_NOT_FOUND, plmnId);
        }
        plmnDTO.setPlmnId(plmnId);
        return plmnMapper.toDto(plmnRepository.save(plmnMapper.toEntity(plmnDTO)));
    }

    public PlmnDTOV2 update(Long id, PlmnDTOV2 plmnDTO) {
        if (!plmnRepository.existsById(id)) {
            throw new EqmValidationException(localizedMessageBuilder, PLMN_ID_NOT_FOUND, id);
        }
        Plmn plmn = plmnMapper.toEntity(plmnDTO);
        plmn.setPlmnId(id);
        return plmnMapper.toDtoV2(plmnRepository.save(plmn));
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public PlmnDTO addV1(PlmnDTO plmnDTO) {
        if (plmnRepository.existsByCode(plmnDTO.getCode())) {
            throw new EqmValidationException(localizedMessageBuilder, PLMN_CODE_ALREADY_EXISTS, plmnDTO.getCode());
        }
        return plmnResourceAssembler.toModel(plmnRepository.save(plmnMapper.toEntity(plmnDTO)));
    }

    public PlmnDTOV2 add(PlmnDTOV2 plmnDTO) {
        if (plmnRepository.existsByCode(plmnDTO.getCode())) {
            throw new EqmValidationException(localizedMessageBuilder, PLMN_CODE_ALREADY_EXISTS, plmnDTO.getCode());
        }
        return plmnMapper.toDtoV2(plmnRepository.save(plmnMapper.toEntity(plmnDTO)));
    }
}
