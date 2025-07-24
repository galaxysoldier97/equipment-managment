package mc.monacotelecom.tecrep.equipments.mapper;

import mc.monacotelecom.inventory.common.audit.AuditEnversInfo;
import mc.monacotelecom.tecrep.equipments.dto.SimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.SimCardPartialDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddSimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddSimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.RevisionDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.SimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.importer.data.GenericEquipementCsvLines;
import org.apache.commons.lang3.math.NumberUtils;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revision;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Mapper(componentModel = "spring", uses = {WarehouseMapper.class, PlmnMapper.class, ProviderMapper.class, InventoryPoolMapper.class})
public abstract class SimCardMapper {

    @Autowired
    private ZoneId zoneId;

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Mapping(target = "allotmentId", source = "entity.allotment.allotmentId")
    public abstract SimCardDTO toDtoV1(SimCard entity);

    @Mapping(target = "id", source = "entity.equipmentId")
    @Mapping(target = "allotmentId", source = "entity.allotment.allotmentId")
    public abstract SimCardDTOV2 toDtoV2(SimCard entity);

    public abstract SimCardPartialDTO toPartialDto(SimCard entity);

    @Deprecated(since = "2.21.0", forRemoval = true)
    public abstract SimCard toEntity(SimCardDTO simCardDTO);

    public abstract SimCard toEntity(AddSimCardDTOV2 simCardDTO);

    @Deprecated(since = "2.21.0", forRemoval = true)
    public abstract SimCard toEntity(AddSimCardDTO simCardDTO);

    @Mapping(target = "date", source = "revision", qualifiedByName = "getDate")
    @Mapping(target = "author", source = "revision", qualifiedByName = "getAuthor")
    public abstract RevisionDTOV2<SimCardDTOV2> toRevisionDto(Revision<Integer, SimCard> revision);

    @Mapping(target = "serialNumber", ignore = true)
    public abstract void update(@MappingTarget SimCard simCard, GenericEquipementCsvLines.SimCardIdemiaLine simCardIdemiaLine);

    @Mapping(target = "serialNumber", ignore = true)
    @Mapping(target = "imsiNumber", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateGomo(@MappingTarget SimCard simCard, GenericEquipementCsvLines.SimCardGomoLine simCardGomoLine);


    @Mapping(target = "serialNumber", source = "serialNumber", qualifiedByName = "getSerialNumberImporter")
    @Mapping(target = "checkDigit", source = "serialNumber", qualifiedByName = "getCheckDigitImporter")
    @Mapping(target = "authKey", source = "authentificationKey")
    public abstract SimCard toSimCardGomo(GenericEquipementCsvLines.SimCardGomoLine simCardGomoLine);

    @Mapping(target = "serialNumber", source = "serialNumber", qualifiedByName = "getSerialNumberImporter")
    @Mapping(target = "checkDigit", source = "serialNumber", qualifiedByName = "getCheckDigitImporter")
    public abstract SimCard toSimCardIdemiaEir(GenericEquipementCsvLines.SimCardIdemiaLine simCardIdemiaLine);

    @Named("getDate")
    LocalDateTime getDate(Revision<Integer, SimCard> revision) {
        return revision.getRequiredRevisionInstant().atZone(zoneId).toLocalDateTime().truncatedTo(ChronoUnit.SECONDS);
    }

    @Named("getAuthor")
    String getAuthor(Revision<Integer, SimCard> revision) {
        return ((AuditEnversInfo) revision.getMetadata().getDelegate()).getUserId();
    }

    @Named("getSerialNumberImporter")
    String getSerialNumberImporter(String serialNumberLines) {
        return serialNumberLines.substring(0, 18);
    }

    @Named("getCheckDigitImporter")
    Integer getCheckDigitImporter(String serialNumberLines) {
        return  (serialNumberLines.length() >= 19 && NumberUtils.isParsable(serialNumberLines.substring(18, 19))) ?
                Integer.valueOf(serialNumberLines.substring(18, 19)) : null;
    }
}
