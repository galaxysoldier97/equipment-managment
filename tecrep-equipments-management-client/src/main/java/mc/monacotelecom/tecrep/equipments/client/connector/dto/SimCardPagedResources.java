package mc.monacotelecom.tecrep.equipments.client.connector.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Deprecated(since = "2.21.0", forRemoval = true)
public class SimCardPagedResources {
    @JsonProperty("_embedded")
    SimCardSearchResponse embedded;
}
