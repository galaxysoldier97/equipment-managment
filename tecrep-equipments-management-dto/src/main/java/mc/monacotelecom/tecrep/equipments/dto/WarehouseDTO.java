package mc.monacotelecom.tecrep.equipments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotBlank;

@Schema
@Data
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "warehouses")
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated(since = "2.21.0", forRemoval = true)
public class WarehouseDTO extends RepresentationModel<WarehouseDTO> {

    Long warehouseId;

    @Schema(required = true, description = "Warehouse code", example = "Toto")
    @NotBlank
    private String name;

    @Schema(required = true, description = "Warehouse reseller code", example = "Toto")
    @NotBlank
    private String resellerCode;

}
