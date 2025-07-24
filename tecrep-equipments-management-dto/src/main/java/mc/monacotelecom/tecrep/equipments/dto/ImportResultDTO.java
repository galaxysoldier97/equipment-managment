package mc.monacotelecom.tecrep.equipments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportResultDTO {
    private Long jobId;
    private int totalLines;
    private int successfulLines;
    private int errorLines;
    private List<LineErrorDTO> errors;
    private String status;
    private LocalDateTime importDate;
    private String fileName;
    private String format;
}

