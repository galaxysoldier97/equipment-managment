package mc.monacotelecom.tecrep.equipments.dto.request.search;

import lombok.Data;

@Data
public class SearchBatchDTO {
    private boolean processable = false;
    private String importFileName;
    private String exportFileName;
    private String inventoryPoolCode;
    private String configurationName;
}
