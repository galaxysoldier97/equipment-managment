package mc.monacotelecom.tecrep.equipments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkStandardLoadItemsResponse {
    private BulkResultSummary summary;
    private List<BulkItemResult> results;
}
