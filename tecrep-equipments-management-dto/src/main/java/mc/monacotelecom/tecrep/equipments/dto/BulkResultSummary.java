package mc.monacotelecom.tecrep.equipments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkResultSummary {
    private int inserted;
    private int updated;
    private int skipped;
    private int validationErrors;
}
