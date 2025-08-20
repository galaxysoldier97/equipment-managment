package mc.monacotelecom.tecrep.equipments.mapper;

import mc.monacotelecom.tecrep.equipments.dto.v2.StandardLoadDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.StandardLoad;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StandardLoadMapper {
    StandardLoadDTOV2 toDto(StandardLoad entity);
    StandardLoad toEntity(StandardLoadDTOV2 dto);
}

