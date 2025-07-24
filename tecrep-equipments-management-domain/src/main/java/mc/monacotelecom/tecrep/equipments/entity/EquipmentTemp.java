package mc.monacotelecom.tecrep.equipments.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "equipments_temp", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"box_sn", "pod_sn"})
})
public class EquipmentTemp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "po_ancillaryeqm_sap_id", nullable = false)
    private Long poAncillaryeqmSapId;

    @Column(name = "part_no")
    private String partNo;

    @Column(name = "box_sn")
    private String boxSn;

    @Column(name = "pod_sn")
    private String podSn;

    @Column(name = "scanned")
    private Boolean scanned = false;

    @Column(name = "scanned_at")
    private LocalDateTime scannedAt;

    @Column(name = "model_id")
    private Long modelId;

    @Column(name = "uploaded_by")
    private String uploadedBy;

    @Column(name = "session_id", length = 36, columnDefinition = "char(36)")
    private String sessionId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "status")
    private String status;

    @Column(name = "order_upload_id")
    private Long orderUploadId;

    @Column(name = "job_id")
    private Long jobId;

    @Column(name = "process_date")
    private LocalDateTime processDate;
    
}