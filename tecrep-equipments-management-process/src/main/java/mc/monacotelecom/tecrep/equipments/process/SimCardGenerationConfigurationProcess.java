package mc.monacotelecom.tecrep.equipments.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.CommonFunctions;
import mc.monacotelecom.inventory.common.Lambdas;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.dto.SimCardGenerationConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddSimCardGenerationConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddSimCardGenerationConfigurationDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchSimCardGenerationConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateSimCardGenerationConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateSimCardGenerationConfiguration;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateSimCardGenerationConfigurationV2DTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.SimCardGenerationConfigurationDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.SimCardGenerationConfiguration;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmConflictException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.mapper.SimCardGenerationConfigurationMapper;
import mc.monacotelecom.tecrep.equipments.repository.FileConfigurationRepository;
import mc.monacotelecom.tecrep.equipments.repository.PlmnRepository;
import mc.monacotelecom.tecrep.equipments.repository.SimCardGenerationConfigurationRepository;
import mc.monacotelecom.tecrep.equipments.repository.specification.SimCardGenerationConfigurationSpecifications;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimCardGenerationConfigurationProcess {

    private final SimCardGenerationConfigurationRepository repository;
    private final SimCardGenerationConfigurationMapper mapper;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final FileConfigurationRepository fileConfigurationRepository;
    private final PlmnRepository plmnRepository;

    @Deprecated(since = "2.21.0", forRemoval = true)
    public Page<SimCardGenerationConfigurationDTO> searchV1(final SearchSimCardGenerationConfigurationDTO searchSimCardGenerationConfigurationDTO, final Pageable pageable) {
        final Specification<SimCardGenerationConfiguration> specification = this.prepareSpecifications(searchSimCardGenerationConfigurationDTO);
        return repository.findAll(specification, pageable).map(mapper::toDto);
    }

    public Page<SimCardGenerationConfigurationDTOV2> search(final SearchSimCardGenerationConfigurationDTO searchSimCardGenerationConfigurationDTO, final Pageable pageable) {
        final Specification<SimCardGenerationConfiguration> specification = this.prepareSpecifications(searchSimCardGenerationConfigurationDTO);
        return repository.findAll(specification, pageable).map(mapper::toDtoV2);
    }

    private Specification<SimCardGenerationConfiguration> prepareSpecifications(final SearchSimCardGenerationConfigurationDTO searchSimCardGenerationConfigurationDTO) {
        Specification<SimCardGenerationConfiguration> specification;

        specification = StringUtils.isNotBlank(searchSimCardGenerationConfigurationDTO.getName()) ? Specification.where(SimCardGenerationConfigurationSpecifications.nameStartWith(searchSimCardGenerationConfigurationDTO.getName())) : null;
        specification = Objects.nonNull(searchSimCardGenerationConfigurationDTO.getTransportKey()) ? CommonFunctions.addSpecification(specification, SimCardGenerationConfigurationSpecifications.hasTransportKey(searchSimCardGenerationConfigurationDTO.getTransportKey())) : specification;

        return specification;
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardGenerationConfigurationDTO getByNameV1(final String name) {
        final var simCardGenerationConfiguration = repository.findByName(name)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, SIMCARD_GENERATION_CONFIGURATION_NAME_NOT_FOUND, name));
        return mapper.toDto(simCardGenerationConfiguration);
    }

    public SimCardGenerationConfigurationDTOV2 getByName(final String name) {
        final var simCardGenerationConfiguration = repository.findByName(name)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, SIMCARD_GENERATION_CONFIGURATION_NAME_NOT_FOUND, name));
        return mapper.toDtoV2(simCardGenerationConfiguration);
    }

    public void deleteByName(final String name) {
        final var simCardGenerationConfiguration = repository.findByName(name)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, SIMCARD_GENERATION_CONFIGURATION_NAME_NOT_FOUND, name));

        simCardGenerationConfiguration.getBatches().stream().findFirst().ifPresent(batch -> {
            throw new EqmValidationException(localizedMessageBuilder, SIMCARD_GENERATION_CONFIGURATION_NOT_DELETABLE_BATCH, name, batch.getBatchNumber());
        });

        repository.deleteByName(name);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardGenerationConfigurationDTO createV1(final AddSimCardGenerationConfigurationDTO dto) {
        if (repository.existsByName(dto.getName())) {
            throw new EqmConflictException(localizedMessageBuilder, SIMCARD_GENERATION_CONFIGURATION_NAME_ALREADY_EXISTS, dto.getName());
        }

        final var simCardGenerationConfiguration = mapper.toEntity(dto);

        var exportFileConfiguration = fileConfigurationRepository.findByName(dto.getExportFileConfiguration().getName())
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, FILE_CONFIGURATION_NAME_NOT_FOUND, dto.getExportFileConfiguration().getName()));
        simCardGenerationConfiguration.setExportFileConfiguration(exportFileConfiguration);

        var importFileConfiguration = fileConfigurationRepository.findByName(dto.getImportFileConfiguration().getName())
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, FILE_CONFIGURATION_NAME_NOT_FOUND, dto.getImportFileConfiguration().getName()));
        simCardGenerationConfiguration.setImportFileConfiguration(importFileConfiguration);

        if (Objects.nonNull(dto.getPlmn())) {
            var plmn = plmnRepository.findByCode(dto.getPlmn().getCode())
                    .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, PLMN_NOT_FOUND_CODE, dto.getPlmn().getCode()));
            simCardGenerationConfiguration.setPlmn(plmn);
        }

        return mapper.toDto(repository.save(simCardGenerationConfiguration));
    }

    public SimCardGenerationConfigurationDTOV2 create(final AddSimCardGenerationConfigurationDTOV2 dto) {
        if (repository.existsByName(dto.getName())) {
            throw new EqmConflictException(localizedMessageBuilder, SIMCARD_GENERATION_CONFIGURATION_NAME_ALREADY_EXISTS, dto.getName());
        }

        final var simCardGenerationConfiguration = mapper.toEntity(dto);

        var exportFileConfiguration = fileConfigurationRepository.findByName(dto.getExportFileConfigurationName())
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, FILE_CONFIGURATION_NAME_NOT_FOUND, dto.getExportFileConfigurationName()));
        simCardGenerationConfiguration.setExportFileConfiguration(exportFileConfiguration);

        var importFileConfiguration = fileConfigurationRepository.findByName(dto.getImportFileConfigurationName())
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, FILE_CONFIGURATION_NAME_NOT_FOUND, dto.getImportFileConfigurationName()));
        simCardGenerationConfiguration.setImportFileConfiguration(importFileConfiguration);

        if (StringUtils.isNotBlank(dto.getPlmnCode())) {
            var plmn = plmnRepository.findByCode(dto.getPlmnCode())
                    .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, PLMN_NOT_FOUND_CODE, dto.getPlmnCode()));
            simCardGenerationConfiguration.setPlmn(plmn);
        }

        return mapper.toDtoV2(repository.save(simCardGenerationConfiguration));
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardGenerationConfigurationDTO updateV1(final String name, final UpdateSimCardGenerationConfigurationDTO dto) {
        SimCardGenerationConfiguration simCardGenerationConfiguration = commonUpdate(name, dto);

        if (Objects.nonNull(dto.getPlmn())) {
            var plmn = plmnRepository.findByCode(dto.getPlmn().getCode())
                    .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, PLMN_NOT_FOUND_CODE, dto.getPlmn().getCode()));
            simCardGenerationConfiguration.setPlmn(plmn);
        }

        if (Objects.nonNull(dto.getExportFileConfiguration())) {
            var exportFileConfiguration = fileConfigurationRepository.findByName(dto.getExportFileConfiguration().getName())
                    .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, FILE_CONFIGURATION_NAME_NOT_FOUND, dto.getExportFileConfiguration().getName()));
            simCardGenerationConfiguration.setExportFileConfiguration(exportFileConfiguration);
        }

        if (Objects.nonNull(dto.getImportFileConfiguration())) {
            var importFileConfiguration = fileConfigurationRepository.findByName(dto.getImportFileConfiguration().getName())
                    .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, FILE_CONFIGURATION_NAME_NOT_FOUND, dto.getImportFileConfiguration().getName()));
            simCardGenerationConfiguration.setImportFileConfiguration(importFileConfiguration);
        }

        return mapper.toDto(repository.save(simCardGenerationConfiguration));
    }

    public SimCardGenerationConfigurationDTOV2 update(final String name, final UpdateSimCardGenerationConfigurationV2DTO dto) {
        SimCardGenerationConfiguration simCardGenerationConfiguration = commonUpdate(name, dto);

        if (StringUtils.isNotBlank(dto.getPlmnCode())) {
            var plmn = plmnRepository.findByCode(dto.getPlmnCode())
                    .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, PLMN_NOT_FOUND_CODE, dto.getPlmnCode()));
            simCardGenerationConfiguration.setPlmn(plmn);
        }

        if (StringUtils.isNotBlank(dto.getExportFileConfigurationName())) {
            var exportFileConfiguration = fileConfigurationRepository.findByName(dto.getExportFileConfigurationName())
                    .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, FILE_CONFIGURATION_NAME_NOT_FOUND, dto.getExportFileConfigurationName()));
            simCardGenerationConfiguration.setExportFileConfiguration(exportFileConfiguration);
        }

        if (StringUtils.isNotBlank(dto.getImportFileConfigurationName())) {
            var importFileConfiguration = fileConfigurationRepository.findByName(dto.getImportFileConfigurationName())
                    .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, FILE_CONFIGURATION_NAME_NOT_FOUND, dto.getImportFileConfigurationName()));
            simCardGenerationConfiguration.setImportFileConfiguration(importFileConfiguration);
        }

        return mapper.toDtoV2(repository.save(simCardGenerationConfiguration));
    }

    private SimCardGenerationConfiguration commonUpdate(String name, UpdateSimCardGenerationConfiguration dto) {
        final var simCardGenerationConfiguration = repository.findByName(name)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, SIMCARD_GENERATION_CONFIGURATION_NAME_NOT_FOUND, name));

        final var existingConfigurationWithNewName = repository.findByName(dto.getName());
        if (existingConfigurationWithNewName.isPresent() && !existingConfigurationWithNewName.get().getSimcardGenerationId().equals(simCardGenerationConfiguration.getSimcardGenerationId())) {
            throw new EqmConflictException(localizedMessageBuilder, SIMCARD_GENERATION_CONFIGURATION_NAME_ALREADY_EXISTS, dto.getName());
        }

        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(dto.getName()), simCardGenerationConfiguration::setName, dto.getName());
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(dto.getIccidSequence()), simCardGenerationConfiguration::setIccidSequence, dto.getIccidSequence());
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(dto.getMsinSequence()), simCardGenerationConfiguration::setMsinSequence, dto.getMsinSequence());
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(dto.getTransportKey()), simCardGenerationConfiguration::setTransportKey, dto.getTransportKey());
        Lambdas.verifyAndApplyObj.accept(Objects.nonNull(dto.getAlgorithmVersion()), algorithmVersion -> simCardGenerationConfiguration.setAlgorithmVersion((Integer) algorithmVersion), dto.getAlgorithmVersion());
        Lambdas.verifyAndApplyString.accept(Objects.nonNull(dto.getArtwork()), simCardGenerationConfiguration::setArtwork, dto.getArtwork());
        Lambdas.verifyAndApplyString.accept(Objects.nonNull(dto.getSimReference()), simCardGenerationConfiguration::setSimReference, dto.getSimReference());
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(dto.getType()), simCardGenerationConfiguration::setType, dto.getType());
        Lambdas.verifyAndApplyString.accept(Objects.nonNull(dto.getFixedPrefix()), simCardGenerationConfiguration::setFixedPrefix, dto.getFixedPrefix());
        Lambdas.verifyAndApplyString.accept(Objects.nonNull(dto.getSequencePrefix()), simCardGenerationConfiguration::setSequencePrefix, dto.getSequencePrefix());

        return simCardGenerationConfiguration;
    }
}
