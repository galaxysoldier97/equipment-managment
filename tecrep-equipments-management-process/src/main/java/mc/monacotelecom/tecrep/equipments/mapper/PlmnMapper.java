package mc.monacotelecom.tecrep.equipments.mapper;

import mc.monacotelecom.tecrep.equipments.dto.PlmnDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.PlmnDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.Plmn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PlmnMapper {
    PlmnMapper INSTANCE = Mappers.getMapper(PlmnMapper.class);

    @Deprecated(since = "2.21.0", forRemoval = true)
    PlmnDTO toDto(Plmn entity);

    @Mapping(target = "id", source = "plmnId")
    PlmnDTOV2 toDtoV2(Plmn entity);

    @Deprecated(since = "2.21.0", forRemoval = true)
    Plmn toEntity(PlmnDTO plmnDTO);

    Plmn toEntity(PlmnDTOV2 plmnDTO);
}
