package mc.monacotelecom.tecrep.equipments.entity;


import lombok.*;
import mc.monacotelecom.inventory.common.exporter.annotations.Exported;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Audited
@Table(name = "cpe")
public class CPE extends Equipment implements Serializable {

    @Length(max = 17)
    @Exported(order = 9)
    @Column(name = "mac_address_lan", length = 17, columnDefinition = "varchar(17)")
    private String macAddressLan;

    @Length(max = 17)
    @Exported(order = 12)
    @Column(name = "mac_address2_4g", length = 17, columnDefinition = "varchar(17)")
    private String macAddress4G;

    @Length(max = 17)
    @Exported(order = 13)
    @Column(name = "mac_address_5g", length = 17, columnDefinition = "varchar(17)")
    private String macAddress5G;

    @Length(max = 17)
    @Exported(order = 8)
    @Column(name = "mac_address_cpe", length = 17, columnDefinition = "varchar(17)")
    private String macAddressCpe;

    @Length(max = 17)
    @Exported(order = 10)
    @Column(name = "mac_address_router", length = 17, columnDefinition = "varchar(17)")
    private String macAddressRouter;

    @Length(max = 17)
    @Exported(order = 11)
    @Column(name = "mac_address_voip", length = 17, columnDefinition = "varchar(17)")
    private String macAddressVoip;

    @Length(max = 40)
    @Exported(order = 14)
    @Column(name = "chipset_id", length = 40, columnDefinition = "varchar(40)")
    private String chipsetId;

    @Length(max = 25)
    @Exported(order = 16)
    @Column(name = "hw_version", length = 25, columnDefinition = "varchar(25)")
    private String hwVersion;

    @Length(max = 25)
    @Column(name = "wpa_key", length = 25, columnDefinition = "varchar(25)")
    private String wpaKey;

    @Exported(value = "name", order = 15)
    @NotAudited
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipment_model_id", referencedColumnName = "id", nullable = false)
    private EquipmentModel model;

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
    public CPE getInstance() {
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