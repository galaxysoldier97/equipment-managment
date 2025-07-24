package mc.monacotelecom.tecrep.equipments.connectors.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TABSSimResponse {

    private String authKey;

    private String imsi;

    private String pin1;

    private String pin2;

    private String puk1;

    private String puk2;

    @JsonSetter("authKey")
    public void setAuthKeyCapital(String authKey) {
        this.authKey = authKey;
    }

    @JsonSetter("authkey")
    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }
}
