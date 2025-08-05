package mc.monacotelecom.tecrep.equipments.process;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.CommonFunctions;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.dto.EquipmentModelDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.EquipmentModelCreateDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchEquipmentModelDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.EquipmentModelDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentModel;
import mc.monacotelecom.tecrep.equipments.dto.v2.EquipmentModelNameDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.Provider;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmConflictException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;
import mc.monacotelecom.tecrep.equipments.mapper.EquipmentModelMapper;
import mc.monacotelecom.tecrep.equipments.repository.AncillaryRepository;
import mc.monacotelecom.tecrep.equipments.repository.CPERepository;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentModelRepository;
import mc.monacotelecom.tecrep.equipments.repository.ProviderRepository;
import mc.monacotelecom.tecrep.equipments.repository.specification.EquipmentModelSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;


@Slf4j
@Component
@AllArgsConstructor
public class EquipmentModelProcess implements IEquipmentModelProcess {

    private final EquipmentModelRepository equipmentModelRepository;
    private final ProviderRepository providerRepository;
    private final CPERepository cpeRepository;
    private final AncillaryRepository ancillaryRepository;
    private final EquipmentModelMapper equipmentModelMapper;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    @Override
    @Deprecated(since = "2.21.0", forRemoval = true)
    public EquipmentModelDTO getByIdV1(final long id) {
        final var equipmentModel = equipmentModelRepository.findById(id)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, MODEL_NOT_FOUND, id));
        return equipmentModelMapper.toDto(equipmentModel);
    }

    @Override
    public EquipmentModelDTOV2 getById(final long id) {
        final var equipmentModel = equipmentModelRepository.findById(id)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, MODEL_NOT_FOUND, id));
        return equipmentModelMapper.toDtoV2(equipmentModel);
    }

    @Override
    public EquipmentModel getByNameAndCategory(final String name, final EquipmentModelCategory category) {
        return equipmentModelRepository.findByNameAndCategory(name, category)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, MODEL_NOT_FOUND_NAME_CATEGORY, name, category));
    }

    @Override
    public EquipmentModel create(final EquipmentModelCreateDTO equipmentModelDTO) {
        if (equipmentModelRepository.existsByNameAndCategory(equipmentModelDTO.getName(), equipmentModelDTO.getCategory())) {
            throw new EqmConflictException(localizedMessageBuilder, MODEL_ALREADY_EXISTS_NAME_CATEGORY, equipmentModelDTO.getName(), equipmentModelDTO.getCategory());
        }

        final var provider = providerRepository.findById(equipmentModelDTO.getProviderId())
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, PROVIDER_ID_NOT_FOUND, equipmentModelDTO.getProviderId()));

        final var equipmentModel = equipmentModelMapper.toEntity(equipmentModelDTO);
        equipmentModel.setProvider(provider);
        return equipmentModelRepository.save(equipmentModel);
    }

    @Override
    @Deprecated(since = "2.21.0", forRemoval = true)
    public EquipmentModelDTO mapV1(final EquipmentModel equipmentModel) {
        return equipmentModelMapper.toDto(equipmentModel);
    }

    @Override
    public EquipmentModelDTOV2 map(final EquipmentModel equipmentModel) {
        return equipmentModelMapper.toDtoV2(equipmentModel);
    }

    @Override
    @Deprecated(since = "2.21.0", forRemoval = true)
    public Page<EquipmentModelDTO> getAllV1(final Pageable pageable) {
        return equipmentModelRepository.findAll(pageable).map(equipmentModelMapper::toDto);
    }

    @Override
    public Page<EquipmentModelDTOV2> search(final SearchEquipmentModelDTO dto, final Pageable pageable) {
        return equipmentModelRepository.findAll(prepareSpecifications(dto), pageable).map(equipmentModelMapper::toDtoV2);
    }

    private Specification<EquipmentModel> prepareSpecifications(final SearchEquipmentModelDTO dto) {
        Specification<EquipmentModel> specification = null;

        specification = Objects.nonNull(dto.getCategory()) ? CommonFunctions.addSpecification(specification, EquipmentModelSpecifications.hasCategory(dto.getCategory())) : specification;

        return specification;
    }

    @Override
    @Deprecated(since = "2.21.0", forRemoval = true)
    public EquipmentModelDTO updateV1(final long id, final EquipmentModelCreateDTO equipmentModelCreateDTO) {
        return equipmentModelMapper.toDto(commonUpdate(id, equipmentModelCreateDTO));
    }

    @Override
    public EquipmentModelDTOV2 update(final long id, final EquipmentModelCreateDTO equipmentModelCreateDTO) {
        return equipmentModelMapper.toDtoV2(commonUpdate(id, equipmentModelCreateDTO));
    }

    private EquipmentModel commonUpdate(long id, EquipmentModelCreateDTO dto) {
        var equipmentModel = equipmentModelRepository.findById(id)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, MODEL_NOT_FOUND, id));

        equipmentModelRepository.findByNameAndCategory(dto.getName(), dto.getCategory())
                .ifPresent(existingModel -> {
                        if (existingModel.getId() != id) {
                            throw new EqmConflictException(localizedMessageBuilder, MODEL_ALREADY_EXISTS_NAME_CATEGORY, dto.getName(), dto.getCategory());
                        }
                });

        final Provider provider = providerRepository.findById(dto.getProviderId())
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, PROVIDER_ID_NOT_FOUND, dto.getProviderId()));

        equipmentModel.setCategory(dto.getCategory());
        equipmentModel.setAccessType(dto.getAccessType());
        equipmentModel.setCurrentFirmware(dto.getCurrentFirmware());
        equipmentModel.setName(dto.getName());
        equipmentModel.setProvider(provider);

        return equipmentModelRepository.save(equipmentModel);
    }

    @Override
    public void delete(final long id) {
        final var equipmentModel = equipmentModelRepository.findById(id)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, MODEL_NOT_FOUND, id));

        if (ancillaryRepository.existsByModel(equipmentModel) || cpeRepository.existsByModel(equipmentModel)) {
            throw new EqmConflictException(localizedMessageBuilder, MODEL_DELETION_USED_NAME_CATEGORY, equipmentModel.getName(), equipmentModel.getCategory());
        }

        equipmentModelRepository.delete(equipmentModel);
    }
     @Override
    public List<AccessType> getAccessTypesByCategory(final EquipmentModelCategory category) {
        return equipmentModelRepository.findDistinctAccessTypesByCategory(category);
    }

    @Override
    public List<EquipmentModelNameDTOV2> getNamesByCategoryAndAccessType(final EquipmentModelCategory category, final AccessType accessType) {
        return equipmentModelRepository.findIdAndNameByCategoryAndAccessType(category, accessType)
                .stream()
                .map(p -> new EquipmentModelNameDTOV2(p.getId(), p.getName()))
                .collect(java.util.stream.Collectors.toList());
    }
}