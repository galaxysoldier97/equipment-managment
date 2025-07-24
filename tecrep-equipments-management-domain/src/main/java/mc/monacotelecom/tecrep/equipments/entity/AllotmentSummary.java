package mc.monacotelecom.tecrep.equipments.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import mc.monacotelecom.tecrep.equipments.enums.AllotmentType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@NoArgsConstructor
@Data
@Entity
@Audited
@Table(name = "allotment_summary")
public class AllotmentSummary implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allotment_id", unique = true, nullable = false)
    private Long allotmentId;

    @Column(name = "allotment_number", nullable = false, updatable = false)
    private Integer allotmentNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "batch_number", referencedColumnName = "batch_number", nullable = false)
    private Batch batch;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @Audited(targetAuditMode = NOT_AUDITED)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inventorypool_id", referencedColumnName = "id", nullable = false)
    private InventoryPool inventoryPool;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "allotment_type", columnDefinition = "varchar(50)", nullable = false)
    private AllotmentType allotmentType;

    @Column(name = "quantity", nullable = false, columnDefinition = "int(11)")
    private Integer quantity;

    @Column(name = "price_plan", columnDefinition = "varchar(20)", length = 20)
    private String pricePlan;

    @Column(name = "initial_credit", columnDefinition = "int(11)")
    private Integer initialCredit;

    @Column(name = "pre_provisioning_required", columnDefinition = "bit(1)")
    private Boolean preProvisioning;

    @Column(name = "pre_provisioning_failures", columnDefinition = "varchar(11)")
    private Integer preProvisioningFailures;

    @Column(name = "provisioned_date")
    private LocalDateTime provisionedDate;

    @Column(name = "sent_to_logistics_date")
    private LocalDateTime sentToLogisticsDate;

    @Column(name = "pack_with_handset", columnDefinition = "bit(1)")
    private Boolean packWithHandset;

    @Transient
    public String topUp() {
        return String.format("%03d", getInitialCredit());
    }
}
