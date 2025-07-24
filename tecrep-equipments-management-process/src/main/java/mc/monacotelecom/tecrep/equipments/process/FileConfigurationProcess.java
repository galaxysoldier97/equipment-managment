package mc.monacotelecom.tecrep.equipments.process;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.inventory.common.Lambdas;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.dto.FileConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchFileConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateFileConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.entity.FileConfiguration;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmConflictException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.mapper.FileConfigurationMapper;
import mc.monacotelecom.tecrep.equipments.repository.FileConfigurationRepository;
import mc.monacotelecom.tecrep.equipments.repository.specification.FileConfigurationSpecifications;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;


@Component
@RequiredArgsConstructor
public class FileConfigurationProcess {

    private final FileConfigurationRepository fileConfigurationRepository;
    private final FileConfigurationMapper fileConfigurationMapper;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    public Page<FileConfigurationDTO> search(final SearchFileConfigurationDTO searchFileConfigurationDTO, final Pageable pageable) {
        final Specification<FileConfiguration> specification = this.prepareSpecifications(searchFileConfigurationDTO);

        return fileConfigurationRepository.findAll(specification, pageable).map(fileConfigurationMapper::toDto);
    }

    private Specification<FileConfiguration> prepareSpecifications(final SearchFileConfigurationDTO searchFileConfigurationDTO) {
        Specification<FileConfiguration> specification;

        specification = StringUtils.isNotBlank(searchFileConfigurationDTO.getName()) ? Specification.where(FileConfigurationSpecifications.hasNameLike(searchFileConfigurationDTO.getName())) : null;

        return specification;
    }

    public FileConfigurationDTO getByName(final String name) {
        final var fileConfiguration = fileConfigurationRepository.findByName(name)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, FILE_CONFIGURATION_NAME_NOT_FOUND, name));
        return fileConfigurationMapper.toDto(fileConfiguration);
    }

    public void deleteByName(final String name) {
        final var fileConfiguration = fileConfigurationRepository.findByName(name)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, FILE_CONFIGURATION_NAME_NOT_FOUND, name));

        fileConfiguration.getSimCardGenerationConfigurationsAsImport().stream().findFirst().ifPresent(scgc -> {
            throw new EqmValidationException(localizedMessageBuilder, FILE_CONFIGURATION_NOT_DELETABLE_IMPORT, name, scgc.getName());
        });

        fileConfiguration.getSimCardGenerationConfigurationsAsExport().stream().findFirst().ifPresent(scgc -> {
            throw new EqmValidationException(localizedMessageBuilder, FILE_CONFIGURATION_NOT_DELETABLE_EXPORT, name, scgc.getName());
        });

        fileConfigurationRepository.deleteByName(name);
    }

    public FileConfigurationDTO create(final FileConfigurationDTO fileConfigurationDTO) {
        if (fileConfigurationRepository.existsByName(fileConfigurationDTO.getName())) {
            throw new EqmConflictException(localizedMessageBuilder, FILE_CONFIGURATION_NAME_ALREADY_EXISTS, fileConfigurationDTO.getName());
        }

        return fileConfigurationMapper.toDto(fileConfigurationRepository.save(fileConfigurationMapper.toEntity(fileConfigurationDTO)));
    }

    public FileConfigurationDTO update(final String name, final UpdateFileConfigurationDTO fileConfigurationDto) {
        final var fileConfiguration = fileConfigurationRepository.findByName(name)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, FILE_CONFIGURATION_NAME_NOT_FOUND, name));

        final var existingFileConfigurationWithNewName = fileConfigurationRepository.findByName(fileConfigurationDto.getName());
        if (existingFileConfigurationWithNewName.isPresent() && !existingFileConfigurationWithNewName.get().getId().equals(fileConfiguration.getId())) {
            throw new EqmConflictException(localizedMessageBuilder, FILE_CONFIGURATION_NAME_ALREADY_EXISTS, fileConfigurationDto.getName());
        }

        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(fileConfigurationDto.getName()), fileConfiguration::setName, fileConfigurationDto.getName());
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(fileConfigurationDto.getPrefix()), fileConfiguration::setPrefix, fileConfigurationDto.getPrefix());
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(fileConfigurationDto.getSuffix()), fileConfiguration::setSuffix, fileConfigurationDto.getSuffix());
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(fileConfigurationDto.getHeaderFormat()), fileConfiguration::setHeaderFormat, fileConfigurationDto.getHeaderFormat());
        Lambdas.verifyAndApplyString.accept(StringUtils.isNotBlank(fileConfigurationDto.getRecordFormat()), fileConfiguration::setRecordFormat, fileConfigurationDto.getRecordFormat());

        return fileConfigurationMapper.toDto(fileConfigurationRepository.save(fileConfiguration));
    }
}
