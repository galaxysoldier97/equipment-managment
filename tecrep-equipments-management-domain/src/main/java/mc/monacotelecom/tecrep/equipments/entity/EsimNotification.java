package mc.monacotelecom.tecrep.equipments.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@Table(name = "esim_notification")
public class EsimNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", referencedColumnName = "id")
    private Equipment equipment;

    @Length(max = 20)
    @Column(name = "iccid", columnDefinition = "varchar(20)")
    private String iccid;

    @Column(name = "profile_type", columnDefinition = "varchar(32)")
    private String profileType;

    @Column(name = "timestamp", columnDefinition = "timestamp")
    private LocalDateTime timeStamp;

    @Column(name = "notification_point_id", columnDefinition = "bigint")
    private Long notificationPointId;

    @Column(name = "check_point", columnDefinition = "varchar(225)")
    private String checkPoint;

    @Column(name = "status", columnDefinition = "varchar(32)")
    private String status;

    @Column(name = "message", columnDefinition = "varchar(225)")
    private String message;

    @Column(name = "reason_code", columnDefinition = "varchar(32)")
    private String reasonCode;

    @Column(name = "date", columnDefinition = "timestamp")
    private LocalDateTime date;
}
