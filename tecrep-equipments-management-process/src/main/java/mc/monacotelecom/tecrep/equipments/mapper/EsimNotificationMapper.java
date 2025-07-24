package mc.monacotelecom.tecrep.equipments.mapper;

import mc.monacotelecom.tecrep.equipments.dto.ESimNotificationDTO;
import mc.monacotelecom.tecrep.equipments.dto.EsimNotificationIngress;
import mc.monacotelecom.tecrep.equipments.entity.EsimNotification;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public abstract class EsimNotificationMapper {

    @Autowired
    private Clock clock;

    @Mapping(target = "equipmentId", source = "entity.equipment.equipmentId")
    public abstract ESimNotificationDTO toDTO(EsimNotification entity);

    @Mapping(target = "status", source = "notification.notificationPointStatus.status")
    @Mapping(target = "reasonCode", source = "notification.notificationPointStatus.statusCodeData.reasonCode")
    @Mapping(target = "checkPoint", source = "notification.checkPoint")
    @Mapping(target = "message", source = "notification.notificationPointStatus.statusCodeData.message")
    @Mapping(target = "date", expression = "java(getDate())")
    @Mapping(target = "timeStamp", source = "notification.timestamp")
    public abstract EsimNotification toEntity(EsimNotificationIngress notification);

    LocalDateTime getDate() {
        return LocalDateTime.now(clock);
    }
}
