package mc.monacotelecom.tecrep.equipments.mapper;

import mc.monacotelecom.tecrep.equipments.dto.EquipmentConfigDTO;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EquipmentConfigMapper {
    EquipmentConfigDTO toDto(EquipmentConfig entity);
    EquipmentConfig toEntity(EquipmentConfigDTO dto);
}