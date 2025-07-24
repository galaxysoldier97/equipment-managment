package mc.monacotelecom.tecrep.equipments.mapper;

import mc.monacotelecom.inventory.common.audit.AuditEnversInfo;
import mc.monacotelecom.tecrep.equipments.dto.AncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddAncillaryDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddAncillaryDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.AncillaryEquipmentDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.RevisionDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.entity.Equipment;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentModel;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentName;
import mc.monacotelecom.tecrep.equipments.importer.implementations.context.EquipmentImporterContext;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revision;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Mapper(componentModel = "spring", uses = {WarehouseMapper.class, EquipmentModelMapper.class})
public abstract class AncillaryMapper {

    @Autowired
    private ZoneId zoneId;

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Mapping(target = "provider", source = "model.provider")
    @Mapping(target = "modelName", source = "model.name")
    @Mapping(target = "pairedEquipmentId", source = "pairedEquipment.equipmentId")
    @Mapping(target = "pairedEquipmentCategory", source = "pairedEquipment.category")
    public abstract AncillaryEquipmentDTO toDtoV1(AncillaryEquipment entity);

    @Mapping(target = "id", source = "entity.equipmentId")
    @Mapping(target = "pairedEquipmentId", source = "pairedEquipment.equipmentId")
    @Mapping(target = "pairedEquipmentCategory", source = "pairedEquipment.category")
    public abstract AncillaryEquipmentDTOV2 toDtoV2(AncillaryEquipment entity);

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Mapping(target = "model", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "category", constant = "ANCILLARY")
    public abstract AncillaryEquipment toEntity(AncillaryEquipmentDTO dto);

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Mapping(target = "model", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "category", constant = "ANCILLARY")
    public abstract AncillaryEquipment toEntity(AddAncillaryDTO dto);

    @Mapping(target = "model", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "category", constant = "ANCILLARY")
    public abstract AncillaryEquipment toEntity(AddAncillaryDTOV2 dto);

    @Mapping(target = "preactivated", constant = "false")
    @Mapping(target = "independent", constant = "true")
    @Mapping(target = "nature", constant = "MAIN")
    @Mapping(target = "category", constant = "ANCILLARY")
    @Mapping(target = "numberRecycles", constant = "0L")
    @Mapping(target = "warehouse", source = "context.warehouse")
    @Mapping(target = "model", source = "context.equipmentModel")
    @Mapping(target = "accessType", source = "context.equipmentModel.accessType")
    @Mapping(target = "recyclable", source = "context.recyclable")
    @Mapping(target = "status", source = "context.status")
    @Mapping(target = "equipmentName", source = "equipmentName")
    public abstract AncillaryEquipment toNode(EquipmentImporterContext context, EquipmentName equipmentName);

    @Mapping(target = "nature", constant = "MAIN")
    @Mapping(target = "recyclable", source = "context.recyclable")
    @Mapping(target = "preactivated", constant = "false")
    @Mapping(target = "independent", constant = "false")
    @Mapping(target = "status", constant = "INSTORE")
    @Mapping(target = "category", constant = "ANCILLARY")
    @Mapping(target = "numberRecycles", constant = "0L")
    @Mapping(target = "warehouse", expression = "java(null)")
    @Mapping(target = "model", source = "equipmentModel")
    @Mapping(target = "batchNumber", source = "context.batchNumber")
    @Mapping(target = "accessType", source = "equipmentModel", qualifiedByName = "getAccessType")
    @Mapping(target = "pairedEquipment", source = "cpe")
    @Mapping(target = "equipmentName", source = "equipmentName")
    public abstract AncillaryEquipment toNodeFromCPE(EquipmentImporterContext context, Equipment cpe, EquipmentModel equipmentModel, EquipmentName equipmentName);

    @Mapping(target = "date", source = "revision", qualifiedByName = "getDate")
    @Mapping(target = "author", source = "revision", qualifiedByName = "getAuthor")
    public abstract RevisionDTOV2<AncillaryEquipmentDTOV2> toRevisionDto(Revision<Integer, AncillaryEquipment> revision);

    @Named("getDate")
    LocalDateTime getDate(Revision<Integer, AncillaryEquipment> revision) {
        return revision.getRequiredRevisionInstant().atZone(zoneId).toLocalDateTime().truncatedTo(ChronoUnit.SECONDS);
    }

    @Named("getAuthor")
    String getAuthor(Revision<Integer, AncillaryEquipment> revision) {
        return ((AuditEnversInfo) revision.getMetadata().getDelegate()).getUserId();
    }

    @Named("getAccessType")
    AccessType getAccessType(EquipmentModel equipmentModel){
        return Objects.nonNull(equipmentModel) ? equipmentModel.getAccessType() : null;
    }
}
