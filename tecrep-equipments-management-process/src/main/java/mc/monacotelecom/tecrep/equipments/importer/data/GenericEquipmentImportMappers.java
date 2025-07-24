package mc.monacotelecom.tecrep.equipments.importer.data;

import mc.monacotelecom.importer.csv.CsvFileReader;
import mc.monacotelecom.tecrep.equipments.entity.Plmn;
import mc.monacotelecom.tecrep.equipments.entity.Provider;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.entity.Warehouse;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import org.apache.commons.lang3.math.NumberUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

public interface GenericEquipmentImportMappers {


    @Mapper(componentModel = "spring")
    interface ProviderMapper extends CsvFileReader.CsvImportMapper<Provider, GenericEquipementCsvLines.ProviderCsvLine> {

        default AccessType accessTypeToUpperCase(GenericEquipementCsvLines.ProviderCsvLine providerCsvLine) {
            return AccessType.valueOf(providerCsvLine.getAccessType().toUpperCase());
        }

        @Override
        @Mapping(target = "accessType", expression = "java(accessTypeToUpperCase(providerCsvLine))")
        Provider toNode(GenericEquipementCsvLines.ProviderCsvLine providerCsvLine);
    }

    @Mapper(componentModel = "spring")
    interface WarehouseMapper extends CsvFileReader.CsvImportMapper<Warehouse, GenericEquipementCsvLines.WarehouseCsvLine> {

        @Override
        Warehouse toNode(GenericEquipementCsvLines.WarehouseCsvLine warehouseCsvLine);
    }

    @Mapper(componentModel = "spring")
    interface PlmnMapper extends CsvFileReader.CsvImportMapper<Plmn, GenericEquipementCsvLines.PlmnCsvLine> {

        @Override
        Plmn toNode(GenericEquipementCsvLines.PlmnCsvLine plmnCsvLine);
    }

    @Mapper(componentModel = "spring")
    interface SimCardGemaltoMapper extends CsvFileReader.CsvImportMapper<SimCard, GenericEquipementCsvLines.SimCardGemaltoLine> {

        @Named("extractCheckDigit")
        default Integer extractCheckDigit(String serialNumber) {
            if (serialNumber.length() >= 19 && NumberUtils.isParsable(serialNumber.substring(18, 19))) {
                return Integer.valueOf(serialNumber.substring(18, 19));
            }
            return null;
        }

        @Override
        @Mapping(target = "checkDigit", source = "serialNumber", qualifiedByName = "extractCheckDigit")
        SimCard toNode(GenericEquipementCsvLines.SimCardGemaltoLine simCardGemaltoLine);
    }

    @Mapper(implementationName = "simCardDeliveryFileMapper", componentModel = "spring")
    interface SimCardDeliveryFileMapper {

        @Mapping(target = "serialNumber", source = "serialNumber", qualifiedByName = "removeLastCharacterFromSerialNumber")
        SimCard toNode(GenericEquipementCsvLines.SimCardDeliveryFileLine simCardDeliveryFileLine);

        // ICCID in delivery fle always contain a checksum character in the 19th (last) position that needs to be removed
        @Named("removeLastCharacterFromSerialNumber")
        default String removeLastCharacterFromSerialNumber(String serialNumber) {
            return serialNumber.substring(0, serialNumber.length() - 1);
        }
    }

}
