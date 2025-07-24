package mc.monacotelecom.tecrep.equipments.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mc.monacotelecom.inventory.common.recycling.entities.BaseJobConfiguration;
import mc.monacotelecom.tecrep.equipments.enums.JobRecyclingOperation;
import mc.monacotelecom.tecrep.equipments.enums.Status;

import javax.persistence.*;

@Entity
@Table(name = "job_configuration")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobConfiguration extends BaseJobConfiguration<JobRecyclingOperation> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "operation", columnDefinition = "enum('AUDIT_CLEAN','EQT_PURGE', 'EQT_UNBOOK')", nullable = false)
    @Enumerated(EnumType.STRING)
    private JobRecyclingOperation operation;

    @Column(name = "days", columnDefinition = "int(11)", nullable = false)
    private long days;

    @Column(name = "enabled", columnDefinition = "bit(1)", nullable = false)
    private boolean enabled;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "enum('INSTORE', 'AVAILABLE', 'BOOKED', 'ASSIGNED', 'ACTIVATED', 'ONHOLD', 'RETURNED', 'NOTRETURNED', 'DEPRECATED', 'REPACKAGING', 'DEACTIVATED', 'FINAL')", nullable = false)
    private Status status;

    @Column(name = "recyclable", columnDefinition = "bit(1)")
    private Boolean recyclable;
}
