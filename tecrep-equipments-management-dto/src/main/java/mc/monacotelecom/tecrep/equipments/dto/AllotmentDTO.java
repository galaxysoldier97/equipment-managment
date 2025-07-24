package mc.monacotelecom.tecrep.equipments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mc.monacotelecom.tecrep.equipments.enums.AllotmentType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllotmentDTO {

    private Long allotmentId;
    @Schema(description = "allotment number within the related batch")
    private Long allotmentNumber;
    private String batchNumber;
    private AllotmentType allotmentType;
    private InventoryPoolDTO inventoryPool;
    private Integer quantity;
    private Boolean packWithHandset;
    private String pricePlan;
    private Integer initialCredit;
    private Boolean preProvisioning;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;
    private Boolean isSentToLogistics;
    private Boolean isProvisioned;
}
