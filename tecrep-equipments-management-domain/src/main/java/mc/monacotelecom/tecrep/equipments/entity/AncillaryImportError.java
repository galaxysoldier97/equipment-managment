package mc.monacotelecom.tecrep.equipments.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ancillary_import_error")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class AncillaryImportError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    //JobId is used to link the error to a specific import job AncillaryImportJob
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", referencedColumnName = "id", nullable = false)
    private AncillaryImportJob job;


    @Column(name = "line_number", nullable = false)
    private int lineNumber;

    @Column(name = "error_message", nullable = false, length = 1024)
    private String errorMessage;

    @Column(name = "original_line", length = 1024)
    private String originalLine;
}
