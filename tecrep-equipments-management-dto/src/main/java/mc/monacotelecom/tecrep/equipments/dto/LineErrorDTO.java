package mc.monacotelecom.tecrep.equipments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor  
@NoArgsConstructor
public class LineErrorDTO {
    private int lineNumber;
    private String errorMessage;
    private String lineContent;
    private String fieldName;
    private String moreInfo; // (puede quedar null)
}

