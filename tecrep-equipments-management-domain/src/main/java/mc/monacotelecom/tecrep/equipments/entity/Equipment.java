package mc.monacotelecom.tecrep.equipments.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import mc.monacotelecom.inventory.common.exporter.annotations.Exported;
import mc.monacotelecom.inventory.common.importer.domain.entity.IEntity;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.*;
import org.hibernate.Hibernate;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
@Table(name = "equipment", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"category", "serial_number"}),
        @UniqueConstraint(columnNames = {"service_id"})
})
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Equipment implements IEntity, Serializable {

    @Id
    @Exported
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long equipmentId;

    @Length(max = 20)
    @NotEmpty
    @Exported(order = 1)
    @Column(name = "serial_number", length = 20, nullable = false, columnDefinition = "varchar(20)")
    private String serialNumber;

    @Length(max = 40)
    @Exported(order = 2)
    @Column(name = "external_number", length = 40, columnDefinition = "varchar(40)")
    private String externalNumber;

    @Enumerated(EnumType.STRING)
    @Exported(order = 3)
    @Column(name = "access_type", columnDefinition = "ENUM('DOCSIS', 'EMTA', 'FTTH', 'FIREWALL', 'STB_HD', 'STB_ANDROID', 'STB_STICKTV', 'STB_ONT', 'STB_DTH', 'EXTENSOR_WIFI', 'EXTENSOR_WIFI_PLUS', 'EXTENSOR_WIFI_B2B', 'ALARMS', 'CENTRAL_TELEFONICA', 'SONDAS_DE_MONITOREO', 'MOBILE')")
    private AccessType accessType;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity", columnDefinition = "ENUM('MOBILE', 'INTERNET', 'TELEPHONY', 'TV', 'NDD', 'OX', 'MEVO')")
    private Activity activity;

    @Enumerated(EnumType.STRING)
    @Exported(order = 4)
    @Column(name = "status", columnDefinition = "ENUM('INSTORE', 'AVAILABLE', 'BOOKED', 'ASSIGNED', 'ACTIVATED', 'DEACTIVATED', 'ONHOLD', 'RETURNED', 'NOTRETURNED', 'DEPRECATED', 'REPACKAGING')")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Exported(order = 5)
    @Column(name = "nature", columnDefinition = "ENUM('MAIN','ADDITIONAL') DEFAULT 'MAIN'", nullable = false)
    private EquipmentNature nature = EquipmentNature.MAIN;

    @Exported(order = 8)
    @Column(name = "recyclable", nullable = false, columnDefinition = "bit(1)")
    private Boolean recyclable;

    @Column(name = "preactivated", columnDefinition = "bit(1)")
    private Boolean preactivated;

    @Length(max = 20)
    @Exported(order = 6)
    @Column(name = "batch_number", length = 20, columnDefinition = "varchar(20)")
    private String batchNumber;

    @Exported(order = 21)
    @Column(name = "service_id", columnDefinition = "bigint(20)")
    private Long serviceId;

    @Column(name = "order_id", columnDefinition = "varchar(255)")
    private String orderId;

    @NotAudited
    @Exported(order = 23)
    @Column(name = "activation_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime activationDate;

    @NotAudited
    @Exported(order = 22)
    @Column(name = "assigment_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime assignmentDate;

    @Audited(targetAuditMode = NOT_AUDITED)
    @Exported(value = "name", order = 20)
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_id", referencedColumnName = "id")
    private Warehouse warehouse;

    @Column(name = "category", columnDefinition = "ENUM('SIMCARD', 'CPE', 'ANCILLARY')")
    @Enumerated(EnumType.STRING)
    private EquipmentCategory category;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "pairedEquipment")
    private List<AncillaryEquipment> ancillaryEquipments = new ArrayList<>();

    @Override
    public String getDatabaseId() {
        return String.valueOf(equipmentId);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        final Equipment equipment = (Equipment) o;
        return equipmentId != null && Objects.equals(equipmentId, equipment.equipmentId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}