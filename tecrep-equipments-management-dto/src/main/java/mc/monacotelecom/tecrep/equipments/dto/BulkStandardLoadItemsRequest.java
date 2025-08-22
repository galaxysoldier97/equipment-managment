package mc.monacotelecom.tecrep.equipments.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BulkStandardLoadItemsRequest {

    @NotNull
    @Valid
    private List<StandardLoadItemDTO> items;
}
