package mc.monacotelecom.tecrep.equipments.dto.request.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchEsimNotificationDTO {

    private Long equipmentId;

    private String iccid;

    private String profileType;
}
