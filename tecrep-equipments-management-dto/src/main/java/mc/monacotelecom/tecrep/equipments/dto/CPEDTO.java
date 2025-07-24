package mc.monacotelecom.tecrep.equipments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "cpes")
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated(since = "2.21.0", forRemoval = true)
public class CPEDTO extends EquipmentDTO {

    @Length(max = 17)
    private String macAddressLan;

    @Length(max = 17)
    private String macAddressCpe;

    @Length(max = 17)
    private String macAddressRouter;

    @Length(max = 17)
    private String macAddressVoip;

    @Length(max = 17)
    private String macAddress4G;

    @Length(max = 17)
    private String macAddress5G;

    @Length(max = 40)
    private String chipsetId;

    @Length(max = 25)
    private String modelName;

    @Length(max = 25)
    private String hwVersion;

    @Length(max = 25)
    private String wpaKey;

    private Long serviceId;

    @Schema(description = "number of time equipment CPE has been recycled")
    private Long numberRecycles = 0L;

    @Schema(description = "Related Equipment model")
    private EquipmentModelDTO model;
}
