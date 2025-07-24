package mc.monacotelecom.tecrep.equipments.mapper;

import mc.monacotelecom.importer.csv.CsvFileReader;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.dto.SimCardGenerationConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddSimCardGenerationConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddSimCardGenerationConfigurationDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.SimCardGenerationConfigurationDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.FileConfiguration;
import mc.monacotelecom.tecrep.equipments.entity.Plmn;
import mc.monacotelecom.tecrep.equipments.entity.Provider;
import mc.monacotelecom.tecrep.equipments.entity.SimCardGenerationConfiguration;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.importer.data.GenericEquipementCsvLines;
import mc.monacotelecom.tecrep.equipments.repository.FileConfigurationRepository;
import mc.monacotelecom.tecrep.equipments.repository.PlmnRepository;
import mc.monacotelecom.tecrep.equipments.repository.ProviderRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;
import static org.apache.commons.lang3.StringUtils.isNumeric;

@Mapper(componentModel = "spring", uses = {FileConfigurationMapper.class, PlmnMapper.class})
public abstract class SimCardGenerationConfigurationMapper implements CsvFileReader.CsvImportMapper<SimCardGenerationConfiguration, GenericEquipementCsvLines.SimCardGenerationConfigurationCsvLine> {

    @Value("${incoming.files.notify-address:test@test.test}")
    protected String notify;

    @Autowired
    private FileConfigurationRepository fileConfigurationRepository;
    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private PlmnRepository plmnRepository;
    @Autowired
    private LocalizedMessageBuilder localizedMessageBuilder;

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Mapping(target = "notify", expression = "java(this.notify)")
    public abstract SimCardGenerationConfigurationDTO toDto(SimCardGenerationConfiguration entity);

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Mapping(source = "fixedPrefix", target = "fixedPrefix", qualifiedByName = "validateFixedPrefix")
    @Mapping(source = "sequencePrefix", target = "sequencePrefix", qualifiedByName = "validateSequencePrefix")
    public abstract SimCardGenerationConfiguration toEntity(SimCardGenerationConfigurationDTO dto);

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Mapping(source = "fixedPrefix", target = "fixedPrefix", qualifiedByName = "validateFixedPrefix")
    @Mapping(source = "sequencePrefix", target = "sequencePrefix", qualifiedByName = "validateSequencePrefix")
    public abstract SimCardGenerationConfiguration toEntity(AddSimCardGenerationConfigurationDTO dto);

    @Mapping(target = "notify", expression = "java(this.notify)")
    public abstract SimCardGenerationConfigurationDTOV2 toDtoV2(SimCardGenerationConfiguration entity);

    @Mapping(source = "fixedPrefix", target = "fixedPrefix", qualifiedByName = "validateFixedPrefix")
    @Mapping(source = "sequencePrefix", target = "sequencePrefix", qualifiedByName = "validateSequencePrefix")
    public abstract SimCardGenerationConfiguration toEntity(SimCardGenerationConfigurationDTOV2 dto);

    @Mapping(source = "fixedPrefix", target = "fixedPrefix", qualifiedByName = "validateFixedPrefix")
    @Mapping(source = "sequencePrefix", target = "sequencePrefix", qualifiedByName = "validateSequencePrefix")
    @Mapping(target = "exportFileConfiguration", source = "exportFileConfigurationName")
    @Mapping(target = "importFileConfiguration", source = "importFileConfigurationName")
    public abstract SimCardGenerationConfiguration toEntity(AddSimCardGenerationConfigurationDTOV2 dto);

    @Named("validateFixedPrefix")
    public String validateFixedPrefix(String fixedPrefix) {
        if (Objects.nonNull(fixedPrefix) && !isNumeric(fixedPrefix)) {
            throw new EqmValidationException(localizedMessageBuilder, SIMCARD_GENERATION_CONFIGURATION_FIXED_PREFIX_NOT_NUMERIC, fixedPrefix);
        }
        return fixedPrefix;
    }

    @Named("validateSequencePrefix")
    public String validateSequencePrefix(String sequencePrefix) {
        if (Objects.nonNull(sequencePrefix) && !isNumeric(sequencePrefix)) {
            throw new EqmValidationException(localizedMessageBuilder, SIMCARD_GENERATION_CONFIGURATION_SEQUENCE_PREFIX_NOT_NUMERIC, sequencePrefix);
        }
        return sequencePrefix;
    }

    @Override
    @Mapping(target = "exportFileConfiguration", source = "exportFileConfigurationName")
    @Mapping(target = "importFileConfiguration", source = "importFileConfigurationName")
    @Mapping(target = "provider", source = "providerName")
    @Mapping(target = "plmn", source = "plmnCode")
    public abstract SimCardGenerationConfiguration toNode(GenericEquipementCsvLines.SimCardGenerationConfigurationCsvLine simCardGenerationConfigurationCsvLine);

    public FileConfiguration toFileConfiguration(String name) {
        return fileConfigurationRepository.findByName(name).orElseThrow(
                () -> new EqmValidationException(localizedMessageBuilder, FILE_CONFIGURATION_NAME_NOT_FOUND, name)
        );
    }

    public Provider toProvider(String name) {
        return providerRepository.findByName(name).orElseThrow(
                () -> new EqmValidationException(localizedMessageBuilder, PROVIDER_NOT_FOUND_NAME, name)
        );
    }

    public Plmn toPlmn(String code) {
        return plmnRepository.findByCode(code).orElseThrow(
                () -> new EqmValidationException(localizedMessageBuilder, PLMN_NOT_FOUND_CODE, code)
        );
    }
}
