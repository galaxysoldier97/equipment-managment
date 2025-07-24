package mc.monacotelecom.tecrep.equipments.mapper;

import mc.monacotelecom.tecrep.equipments.dto.BatchDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.BatchDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.Batch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BatchMapper {
    @Deprecated(since = "2.21.0", forRemoval = true)
    @Mapping(target = "configurationName", source = "simCardGenerationConfiguration.name")
    @Mapping(target = "inventoryPoolCode", source = "inventoryPool.code")
    BatchDTO toDto(Batch entity);

    @Deprecated(since = "2.21.0", forRemoval = true)
    Batch toEntity(BatchDTO batchDTO);

    @Mapping(target = "configurationName", source = "simCardGenerationConfiguration.name")
    @Mapping(target = "inventoryPoolCode", source = "inventoryPool.code")
    BatchDTOV2 toDtoV2(Batch entity);

    Batch toEntity(BatchDTOV2 batchDTO);
}
