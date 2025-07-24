package mc.monacotelecom.tecrep.equipments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimCardPartialDTO implements java.io.Serializable {
    private Long equipmentId;
    private String imsiNumber;
    private String serialNumber;
    private String number;

}
