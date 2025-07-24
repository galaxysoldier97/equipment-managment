package mc.monacotelecom.tecrep.equipments.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Relation(collectionRelation = "plmns")
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated(since = "2.21.0", forRemoval = true)
public class PlmnDTO extends RepresentationModel<PlmnDTO> {

    private Long plmnId;

    private String code;

    private String networkName;

    private String tadigCode;

    private String countryIsoCode;

    private String countryName;

    private String rangesPrefix;
}
