package mc.monacotelecom.tecrep.equipments.mapper;

import mc.monacotelecom.tecrep.equipments.dto.EquipmentModelDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.EquipmentModelCreateDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.EquipmentModelDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProviderMapper.class})
public interface EquipmentModelMapper {

    @Deprecated(since = "2.21.0", forRemoval = true)
    EquipmentModel toEntity(EquipmentModelDTO dto);

    EquipmentModel toEntity(EquipmentModelCreateDTO dto);


    @Deprecated(since = "2.21.0", forRemoval = true)
    EquipmentModelDTO toDto(EquipmentModel entity);

    EquipmentModelDTOV2 toDtoV2(EquipmentModel entity);
}
