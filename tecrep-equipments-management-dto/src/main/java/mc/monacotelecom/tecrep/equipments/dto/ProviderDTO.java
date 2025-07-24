package mc.monacotelecom.tecrep.equipments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@EqualsAndHashCode(callSuper = true)
@Schema
@Data
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "providers")
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated(since = "2.21.0", forRemoval = true)
public class ProviderDTO extends RepresentationModel<ProviderDTO> {

    private Long providerId;

    @Schema(required = true, description = "Provider code", example = "Toto")
    private String name;

    @Schema(required = true, description = "Provider access type")
    private AccessType accessType;
}
