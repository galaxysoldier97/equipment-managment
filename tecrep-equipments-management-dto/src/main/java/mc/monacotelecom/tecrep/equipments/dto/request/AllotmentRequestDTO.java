package mc.monacotelecom.tecrep.equipments.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mc.monacotelecom.tecrep.equipments.enums.AllotmentType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class AllotmentRequestDTO {

    public static final String DEFAULT_FIRST_SIMCARD_NUMBER = "0";

    @NotNull
    private Long batchNumber;

    @Min(1)
    private int quantity;

    @Schema(description = "Serial Number of the first SIM Card to allot", example = "111111111111")
    private String firstSerialNumber = DEFAULT_FIRST_SIMCARD_NUMBER;

    @NotNull
    private AllotmentType allotmentType;

    private String inventoryPoolCode;

    // offerName is not for EIR
    private String offerName;

    // pricePlan, packWithHandset and initialCredit are for EIR
    private String pricePlan;
    private Boolean packWithHandset;
    private Integer initialCredit;
}
