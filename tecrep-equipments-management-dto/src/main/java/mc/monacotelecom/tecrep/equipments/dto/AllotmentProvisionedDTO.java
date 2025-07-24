package mc.monacotelecom.tecrep.equipments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllotmentProvisionedDTO {

    private Long allotmentId;

    @NotNull
    private Integer successQuantity;

    @NotNull
    private List<PartialSimCardDTO> failures;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PartialSimCardDTO {
        private String imsiNumber;
    }
}
