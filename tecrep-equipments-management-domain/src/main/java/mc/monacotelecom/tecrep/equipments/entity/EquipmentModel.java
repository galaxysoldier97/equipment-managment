package mc.monacotelecom.tecrep.equipments.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import mc.monacotelecom.tecrep.equipments.enums.EquipmentName;

@Data
@NoArgsConstructor
@Entity
@Table(name = "equipment_model", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "category"})
})
public class EquipmentModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long id;

    @Length(max = 60)
    @NotNull
    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(60)")
    private String name;

    @Column(name = "category", columnDefinition = "ENUM('CPE', 'ANCILLARY')", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private EquipmentModelCategory category;

    @Length(max = 40)
    @Column(name = "currentFirmware", columnDefinition = "VARCHAR(40)")
    private String currentFirmware;

    @NotAudited
    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "provider_id", referencedColumnName = "id", nullable = false)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "access_type", columnDefinition =  "ENUM('DOCSIS', 'EMTA', 'FTTH', 'FIREWALL', 'STB_HD', 'STB_ANDROID', 'STB_STICKTV', 'STB_ONT', 'STB_DTH', 'EXTENSOR_WIFI', 'EXTENSOR_WIFI_PLUS', 'EXTENSOR_WIFI_B2B', 'ALARMS', 'CENTRAL_TELEFONICA', 'SONDAS_DE_MONITOREO', 'MOBILE')", nullable = false)
    private AccessType accessType;
     
    @Enumerated(EnumType.STRING)
    @Column(name = "equipment_name", columnDefinition = "ENUM('BRDBOX', 'HDD', 'ONT', 'STB')")
    private EquipmentName equipmentName;


}
