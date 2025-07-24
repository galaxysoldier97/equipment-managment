package mc.monacotelecom.tecrep.equipments.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ancillary_import_job")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class AncillaryImportJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Column(name = "format", nullable = false)
    private String format;

    @Column(name = "continue_on_error", nullable = false)
    private boolean continueOnError;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private JobStatus status;

    @Column(name = "total_lines", nullable = false)
    private int totalLines;

    @Column(name = "successful_lines", nullable = false)
    private int successfulLines;

    @Column(name = "error_count", nullable = false)
    private int errorCount;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

   
     /** NUEVO: Ruta donde quedó guardado el CSV de entrada **/
    @Column(name = "input_file_path", length = 1024)
    private String inputFilePath;

    /** EXISTENTE / RENOMBRADO: Ruta donde se genera el CSV de salida **/
    @Column(name = "result_file_path", length = 1024)
    private String resultFilePath;


    public enum JobStatus {
        PENDING,
        PROCESSING,
        SUCCESS,
        SUCCESS_WITH_ERRORS,
        FAILED
    }
}
