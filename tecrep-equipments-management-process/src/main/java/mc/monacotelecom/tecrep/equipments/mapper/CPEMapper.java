package mc.monacotelecom.tecrep.equipments.mapper;

import mc.monacotelecom.inventory.common.audit.AuditEnversInfo;
import mc.monacotelecom.tecrep.equipments.dto.CPEDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddCPEDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddCPEDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.CPEDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.RevisionDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.CPE;
import mc.monacotelecom.tecrep.equipments.importer.interfaces.ICPELines;
import mc.monacotelecom.tecrep.equipments.importer.implementations.context.CPEImporterContext;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revision;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;

@Mapper(componentModel = "spring", uses = {WarehouseMapper.class, EquipmentModelMapper.class})
public abstract class CPEMapper {
    @Autowired
    private ZoneId zoneId;

    @Mapping(target = "provider", source = "model.provider")
    @Mapping(target = "modelName", source = "model.name")
    public abstract CPEDTO toDto(CPE entity);

    @Mapping(target = "id", source = "entity.equipmentId")
    public abstract CPEDTOV2 toDtoV2(CPE entity);

    @Mapping(target = "model", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "category", constant = "CPE")
    public abstract CPE toEntity(CPEDTO dto);

    @Mapping(target = "model", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "category", constant = "CPE")
    public abstract CPE toEntity(AddCPEDTOV2 dto);

    @Mapping(target = "model", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "category", constant = "CPE")
    public abstract CPE toEntity(AddCPEDTO dto);

    @Mapping(target = "serialNumber", source = "csvLine.serialNumber")
    @Mapping(target = "externalNumber", source = "csvLine.externalNumber")
    @Mapping(target = "macAddressCpe", source = "csvLine.macAddressCpe", qualifiedByName = "formatMacAddress")
    @Mapping(target = "macAddressRouter", source = "csvLine.macAddressRouter", qualifiedByName = "formatMacAddress")
    @Mapping(target = "macAddressVoip", source = "csvLine.macAddressVoip", qualifiedByName = "formatMacAddress")
    @Mapping(target = "macAddressLan", source = "csvLine.macAddressLan", qualifiedByName = "formatMacAddress")
    @Mapping(target = "macAddress4G", source = "csvLine.macAddress4G", qualifiedByName = "formatMacAddress")
    @Mapping(target = "macAddress5G", source = "csvLine.macAddress5G", qualifiedByName = "formatMacAddress")
    @Mapping(target = "wpaKey", source = "csvLine.wpaKey")
    @Mapping(target = "hwVersion", source = "csvLine.hwVersion")
    @Mapping(target = "preactivated", constant = "false")
    @Mapping(target = "recyclable", source = "importerContext.recyclable")
    @Mapping(target = "status", source = "importerContext.status")
    @Mapping(target = "nature", constant = "MAIN")
    @Mapping(target = "category", constant = "CPE")
    @Mapping(target = "numberRecycles", constant = "0L")
    @Mapping(target = "warehouse", source = "importerContext.warehouse")
    @Mapping(target = "model", source = "importerContext.equipmentModel")
    @Mapping(target = "accessType", source = "importerContext.equipmentModel.accessType")
    @Mapping(target = "batchNumber", source = "importerContext.batchNumber")
    @Mapping(target = "chipsetId", source = "csvLine.chipsetId")
    @Mapping(target = "model.name", source = "importerContext.equipmentModel.name")
    public abstract CPE toNode(ICPELines csvLine, CPEImporterContext importerContext);

    @Mapping(target = "date", source = "revision", qualifiedByName = "getDate")
    @Mapping(target = "author", source = "revision", qualifiedByName = "getAuthor")
    public abstract RevisionDTOV2<CPEDTOV2> toRevisionDto(Revision<Integer, CPE> revision);

    @Named("getDate")
    LocalDateTime getDate(Revision<Integer, CPE> revision) {
        return revision.getRequiredRevisionInstant().atZone(zoneId).toLocalDateTime().truncatedTo(ChronoUnit.SECONDS);
    }

    @Named("getAuthor")
    String getAuthor(Revision<Integer, CPE> revision) {
        return ((AuditEnversInfo) revision.getMetadata().getDelegate()).getUserId();
    }

    @Named("formatMacAddress")
    public static String formatMacAddress(String macAddress) {
        if (StringUtils.isNotBlank(macAddress)) {
            return macAddress.length() <= 17 ? macAddress : macAddress.substring(0, 17);
        }
        return macAddress;
    }
}