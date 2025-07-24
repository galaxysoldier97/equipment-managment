package mc.monacotelecom.tecrep.equipments.mapper;

import mc.monacotelecom.tecrep.equipments.dto.ProviderDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.ProviderDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.Provider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProviderMapper {
    ProviderMapper INSTANCE = Mappers.getMapper(ProviderMapper.class);

    @Deprecated(since = "2.21.0", forRemoval = true)
    ProviderDTO toDto(Provider entity);

    @Mapping(target = "id", source = "providerId")
    ProviderDTOV2 toDtoV2(Provider entity);

    @Deprecated(since = "2.21.0", forRemoval = true)
    Provider toEntity(ProviderDTO providerDTO);

    Provider toEntity(ProviderDTOV2 providerDTO);
}
