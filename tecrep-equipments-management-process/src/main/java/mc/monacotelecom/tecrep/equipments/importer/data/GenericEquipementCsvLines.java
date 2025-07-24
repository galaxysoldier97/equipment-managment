package mc.monacotelecom.tecrep.equipments.importer.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import mc.monacotelecom.importer.csv.CsvColumn;
import mc.monacotelecom.importer.csv.CsvFileReader;
import mc.monacotelecom.inventory.common.importer.domain.entity.IEntity;
import mc.monacotelecom.tecrep.equipments.entity.*;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.importer.interfaces.ICPELines;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


class GemaltoLine<T> extends CsvFileReader.CsvLine<T> {

    @Override
    public String getGroup() {
        return "Gemalto";
    }
}

public interface GenericEquipementCsvLines {

    @Data
    @EqualsAndHashCode(callSuper = true)
    class ProviderCsvLine extends CsvFileReader.CsvLine<Provider> {

        @CsvColumn(0)
        @NotNull
        String name;

        @CsvColumn(1)
        @NotNull
        String accessType;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    class WarehouseCsvLine extends CsvFileReader.CsvLine<Warehouse> {

        @CsvColumn(0)
        @NotNull
        String name;

        @CsvColumn(1)
        @NotNull
        String resellerCode;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    class PlmnCsvLine extends CsvFileReader.CsvLine<Plmn> {

        @CsvColumn(0)
        @NotEmpty
        String code;

        @CsvColumn(1)
        String networkName;

        @CsvColumn(2)
        String tadigCode;

        @CsvColumn(3)
        String countryIsoCode;

        @CsvColumn(4)
        String countryName;

        @CsvColumn(5)
        String rangesPrefix;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    class SimCardGemaltoLine extends GemaltoLine<SimCard> {

        @CsvColumn(0)
        String serialNumber;

        @CsvColumn(1)
        String imsiNumber;

        @CsvColumn(2)
        String imsiSponsorNumber;

        @CsvColumn(3)
        String puk1Code;

        @CsvColumn(4)
        String puk2Code;

        @CsvColumn(5)
        String adminCode;

        @CsvColumn(6)
        String authKey;

        @CsvColumn(7)
        String otaSignatureKey;

        @CsvColumn(8)
        String putDescriptionKey;

        @CsvColumn(10)
        String otaSalt;

        String pin1Code = "0000";

        EquipmentNature nature = EquipmentNature.MAIN;

        Boolean recyclable = Boolean.FALSE;

        Status status = Status.INSTORE;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    class SimCardGomoLine extends CsvFileReader.CsvLine<SimCard> {

        @CsvColumn(0)
        String serialNumber;

        @CsvColumn(1)
        String imsiNumber;

        @CsvColumn(2)
        String pin1Code;

        @CsvColumn(3)
        String puk1Code;

        @CsvColumn(4)
        String pin2Code;

        @CsvColumn(5)
        String puk2Code;

        @CsvColumn(6)
        String adminCode;

        @CsvColumn(7)
        String accessControlClass;

        @CsvColumn(8)
        String authentificationKey;

        Status status = Status.INSTORE;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    class SimCardDeliveryFileLine extends CsvFileReader.CsvLine<IEntity> {

        @CsvColumn(2)
        @NotEmpty
        String serialNumber;

        @CsvColumn(3)
        @NotEmpty
        String number;

        @CsvColumn(6)
        @NotEmpty
        String packId;

        @CsvColumn(9)
        @NotEmpty
        String orderId;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    class SimCardIdemiaLine extends CsvFileReader.CsvLine<SimCard> {

        @CsvColumn(0)
        String imsiNumber;

        @CsvColumn(1)
        String serialNumber;

        @CsvColumn(2)
        String number;

        @CsvColumn(3)
        String authKey;

        @CsvColumn(4)
        String pin1Code;

        @CsvColumn(5)
        String puk1Code;

        @CsvColumn(6)
        String pin2Code;

        @CsvColumn(7)
        String puk2Code;

        @CsvColumn(8)
        String adminCode;

        Status status = Status.INSTORE;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    class CpeDOCSISCsvLine extends CsvFileReader.CsvLine<CPE> implements ICPELines {

        @CsvColumn(0)
        String model;

        @CsvColumn(1)
        String serialNumber;

        @CsvColumn(2)
        String macAddressCpe;

        @CsvColumn(3)
        String macAddressRouter;

        @CsvColumn(4)
        String macAddressVoip;

        @CsvColumn(5)
        String defaultSsid;

        @CsvColumn(6)
        String defaultWifiPassword;

        @CsvColumn(7)
        String hwVersion;

        @CsvColumn(8)
        String stbSn;

        @CsvColumn(9)
        String chipsetId;

        @CsvColumn(10)
        String palletNumber;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    class CPEFTTHCsvLine extends CsvFileReader.CsvLine<CPE> implements ICPELines {

        @CsvColumn(0)
        String externalNumber;

        @CsvColumn(1)
        String serialNumber;

        @CsvColumn(3)
        String macAddressLan;

        @CsvColumn(4)
        String macAddress5G;

        @CsvColumn(5)
        String macAddress4G;

        @CsvColumn(6)
        String gponDef;

        @CsvColumn(7)
        String macAddressCpe;

        @CsvColumn(8)
        String macAddressRouter;

        @CsvColumn(9)
        String macAddressVoip;

        @CsvColumn(10)
        String ontSn = "";

        @CsvColumn(11)
        String ontPw;

        @CsvColumn(12)
        String hwVersion;

        @CsvColumn(13)
        String sfpVersion;

        @CsvColumn(14)
        String wpaKey;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    class SimCardGenerationConfigurationCsvLine extends CsvFileReader.CsvLine<SimCardGenerationConfiguration> {

        @CsvColumn(0)
        @NotEmpty
        String name;

        @CsvColumn(1)
        @NotEmpty
        String exportFileConfigurationName;

        @CsvColumn(2)
        @NotEmpty
        String importFileConfigurationName;

        @CsvColumn(3)
        String transportKey;

        @CsvColumn(4)
        String algorithmVersion;

        @CsvColumn(5)
        @NotEmpty
        String plmnCode;

        @CsvColumn(6)
        @NotEmpty
        String providerName;

        @CsvColumn(7)
        @NotEmpty
        String msinSequence;

        @CsvColumn(8)
        @NotEmpty
        String iccidSequence;

        @CsvColumn(9)
        String artwork;

        @CsvColumn(10)
        String simReference;

        @CsvColumn(11)
        String type;

        @CsvColumn(12)
        String fixedPrefix;

        @CsvColumn(13)
        String sequencePrefix;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    class InventoryPoolCsvLine extends CsvFileReader.CsvLine<InventoryPool> {

        @CsvColumn(0)
        @NotEmpty
        String code;

        @CsvColumn(1)
        String description;

        @CsvColumn(2)
        String mvno;

        @CsvColumn(3)
        String simProfile;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    class FileConfigurationCsvLine extends CsvFileReader.CsvLine<FileConfiguration> {

        @CsvColumn(0)
        String name;

        @CsvColumn(1)
        String prefix;

        @CsvColumn(2)
        String suffix;

        @CsvColumn(3)
        String headerFormat;

        @CsvColumn(4)
        String recordFormat;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    class AncillaryEquipmentCsvLine extends CsvFileReader.CsvLine<AncillaryEquipment> {

        @CsvColumn(4)
        String shipOrderNo;

        @CsvColumn(10)
        String ontSn;

        @CsvColumn(12)
        String ontMac;

        @CsvColumn(25)
        String softRevision;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    class AncillaryONTGenexisCsvLine extends CsvFileReader.CsvLine<AncillaryEquipment> {

        @CsvColumn(12)
        String batchNumber;

        @CsvColumn(15)
        String serialNumber;

        @CsvColumn(3)
        String macAddress;

        @CsvColumn(4)
        String softRevision;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    class AncillaryEquipmentHDDSagemcomsvLine extends CsvFileReader.CsvLine<AncillaryEquipment> {

        @CsvColumn(0)
        String ref;

        @CsvColumn(1)
        String sn;

        @CsvColumn(2)
        String sn2;

        @CsvColumn(3)
        String palletNumber;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    class AncillaryEquipmentSTBCsvLine extends CsvFileReader.CsvLine<AncillaryEquipment> {

        @CsvColumn(3)
        String serialNumber;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    class IndependentAncillaryEquipmentCsvLine extends CsvFileReader.CsvLine<AncillaryEquipment> {
    
        @CsvColumn(0)
        @NotEmpty
        String macAddress;
    
        @CsvColumn(1)
        @NotEmpty
        String modelName;
    
        @CsvColumn(2)
        @NotEmpty
        String accessType;
    
        @CsvColumn(3)
        @NotEmpty
        String equipmentName;
    
        @CsvColumn(4)
        @NotEmpty
        String serialNumber;
    
        @CsvColumn(5)
        @NotEmpty
        String warehouseName;
    
        @CsvColumn(6)
        @NotEmpty
        String batchNumber;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    class CustomFormatAncillaryCsvLine extends CsvFileReader.CsvLine<AncillaryEquipment> {
    
        @CsvColumn(0)
        @NotEmpty
        String serialNumber;
    
        @CsvColumn(1)
        @NotEmpty
        String macAddress;
    
        @CsvColumn(2)
        @NotEmpty
        String warehouseId;
    
        @CsvColumn(3)
        @NotEmpty
        String equipmentModelId;

        Boolean recyclable = Boolean.TRUE;
        
        // Campo para trackear el número de línea durante la importación
        private int lineNumber;
        
        public int getLineNumber() {
            return lineNumber;
        }
        
        public void setLineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
        }
    }
    
}
