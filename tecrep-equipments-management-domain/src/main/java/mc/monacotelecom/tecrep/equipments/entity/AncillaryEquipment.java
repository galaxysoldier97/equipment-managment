package mc.monacotelecom.tecrep.equipments.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mc.monacotelecom.inventory.common.exporter.annotations.Exported;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentName;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Audited
@Table(name = "ancillary_equipment")
public class AncillaryEquipment extends Equipment implements Serializable {

    @Length(max = 17)
    @Exported(order = 8)
    @Column(name = "mac_address", length = 17)
    private String macAddress;

    @Exported(order = 12)
    @Column(name = "independent", columnDefinition = "bit(1)")
    private Boolean independent;

    @Length(max = 25)
    @Column(name = "password", length = 25, columnDefinition = "varchar(25)")
    private String password;

    @Length(max = 25)
    @Exported(order = 13)
    @Column(name = "sfp_version", length = 25, columnDefinition = "varchar(25)")
    private String sfpVersion;

    @Enumerated(EnumType.STRING)
    @Exported(order = 10)
    @Column(name = "equipment_name", columnDefinition = "ENUM('BRDBOX', 'HDD', 'ONT', 'STB')")
    private EquipmentName equipmentName;

    @Exported(value = "name", order = 9)
    @NotAudited
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipment_model_id", referencedColumnName = "id", nullable = false)
    private EquipmentModel model;

    @ManyToOne(fetch = FetchType.EAGER)
    @Exported(value = "equipmentId", order = 11)
    @JoinColumn(name = "paired_equipment_id", referencedColumnName = "id")
    private Equipment pairedEquipment;

    @Column(name = "number_recycles", columnDefinition = "bigint", nullable = false)
    private Long numberRecycles;

    // Only here for exporter to take it into account
    @Setter(AccessLevel.NONE)
    @Transient
    @Exported(value = "provider", order = 19)
    private String provider;

    // Only here for exporter to take it into account
    public String getProvider() {
        return getModel().getProvider().getName();
    }

    @Override
    public AncillaryEquipment getInstance() {
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
