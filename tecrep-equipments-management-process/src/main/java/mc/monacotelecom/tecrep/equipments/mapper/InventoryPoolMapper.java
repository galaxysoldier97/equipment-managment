package mc.monacotelecom.tecrep.equipments.mapper;

import mc.monacotelecom.importer.csv.CsvFileReader;
import mc.monacotelecom.tecrep.equipments.dto.InventoryPoolDTO;
import mc.monacotelecom.tecrep.equipments.dto.InventoryPoolDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddInventoryPoolDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddInventoryPoolDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.InventoryPool;
import mc.monacotelecom.tecrep.equipments.importer.data.GenericEquipementCsvLines;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryPoolMapper extends CsvFileReader.CsvImportMapper<InventoryPool, GenericEquipementCsvLines.InventoryPoolCsvLine> {

    @Deprecated(forRemoval = true, since = "2.21")
    InventoryPoolDTO toDto(InventoryPool entity);

    @Mapping(target = "id", source = "inventoryPoolId")
    InventoryPoolDTOV2 toDtoV2(InventoryPool entity);

    InventoryPool toEntity(AddInventoryPoolDTOV2 dto);

    @Deprecated(forRemoval = true, since = "2.21")
    InventoryPool toEntity(AddInventoryPoolDTO dto);

    InventoryPool toNode(GenericEquipementCsvLines.InventoryPoolCsvLine inventoryPoolCsvLine);
}
