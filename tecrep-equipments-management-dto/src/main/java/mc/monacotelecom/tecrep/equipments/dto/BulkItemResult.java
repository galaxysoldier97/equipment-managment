package mc.monacotelecom.tecrep.equipments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkItemResult {
    private int index;
    private String target;
    private Map<String, Long> key;
    private String status;
    private String message;
}
