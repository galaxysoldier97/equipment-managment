package mc.monacotelecom.tecrep.equipments.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Data
@Entity
@Audited
@Table(name = "batch")
public class Batch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "batch_number", unique = true, nullable = false)
    private Long batchNumber;

    @NotEmpty
    @Column(name = "export_file_name", nullable = false, unique = true, columnDefinition = "varchar(40)", length = 40)
    private String exportFileName;

    @NotEmpty
    @Column(name = "import_file_name", nullable = false, unique = true, columnDefinition = "varchar(40)", length = 40)
    private String importFileName;

    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "returned_date")
    private LocalDateTime returnedDate;

    @Column(name = "return_processed_date")
    private LocalDateTime processedDate;

    @Audited(targetAuditMode = NOT_AUDITED)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inventory_pool_id", referencedColumnName = "id", nullable = false)
    private InventoryPool inventoryPool;

    @Audited(targetAuditMode = NOT_AUDITED)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "simcard_generation_configuration_id", columnDefinition = "bigint(20)", nullable = false)
    private SimCardGenerationConfiguration simCardGenerationConfiguration;

    @NotAudited
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(
            mappedBy = "batch",
            fetch = FetchType.LAZY
    )
    private List<AllotmentSummary> allotmentSummaries = new ArrayList<>();

    @NotAudited
    @Formula("(select count(1) from equipment e where e.batch_number = batch_number)")
    private int simCardsCount;

    @Transient
    public String formattedBatchNumber() {
        // pack up to 5 leading zero
        return String.format("%05d", batchNumber);
    }
}
