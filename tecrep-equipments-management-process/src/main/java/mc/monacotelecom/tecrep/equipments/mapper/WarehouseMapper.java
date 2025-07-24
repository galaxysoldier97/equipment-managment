package mc.monacotelecom.tecrep.equipments.mapper;

import mc.monacotelecom.tecrep.equipments.dto.WarehouseDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.WarehouseDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {
    WarehouseMapper INSTANCE = Mappers.getMapper(WarehouseMapper.class);

    @Deprecated(since = "2.21.0", forRemoval = true)
    WarehouseDTO toDto(Warehouse entity);

    @Mapping(target = "id", source = "warehouseId")
    WarehouseDTOV2 toDtoV2(Warehouse entity);

    @Deprecated(since = "2.21.0", forRemoval = true)
    Warehouse toEntity(WarehouseDTO warehouseDTO);

    Warehouse toEntity(WarehouseDTOV2 warehouseDTO);
}
