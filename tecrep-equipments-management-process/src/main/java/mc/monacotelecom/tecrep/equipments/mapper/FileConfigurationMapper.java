package mc.monacotelecom.tecrep.equipments.mapper;

import mc.monacotelecom.importer.csv.CsvFileReader;
import mc.monacotelecom.tecrep.equipments.dto.FileConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.entity.FileConfiguration;
import mc.monacotelecom.tecrep.equipments.importer.data.GenericEquipementCsvLines;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileConfigurationMapper extends CsvFileReader.CsvImportMapper<FileConfiguration, GenericEquipementCsvLines.FileConfigurationCsvLine> {
    FileConfigurationDTO toDto(FileConfiguration entity);

    FileConfiguration toEntity(FileConfigurationDTO dto);

    FileConfiguration toNode(GenericEquipementCsvLines.FileConfigurationCsvLine fileConfigurationCsvLine);
}
