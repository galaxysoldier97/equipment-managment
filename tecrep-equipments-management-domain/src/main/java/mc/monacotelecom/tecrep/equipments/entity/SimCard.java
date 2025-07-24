package mc.monacotelecom.tecrep.equipments.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mc.monacotelecom.inventory.common.exporter.annotations.Exported;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;


@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Audited
@Table(name = "simcard")
public class SimCard extends Equipment implements Serializable {

    @NotEmpty
    @Exported(order = 10)
    @Length(max = 15)
    @Column(name = "imsi_number", length = 15, nullable = false, unique = true, columnDefinition = "varchar(15)")
    private String imsiNumber;

    @Length(max = 15)
    @Exported(order = 11)
    @Column(name = "imsi_sponsor_number", length = 15, columnDefinition = "varchar(15)")
    private String imsiSponsorNumber;

    @Length(max = 8, min = 8)
    @Exported(order = 12)
    @Column(name = "puk1_code", length = 8, columnDefinition = "varchar(8)")
    @Pattern(regexp = "^([0-9A-F]{8})$")
    private String puk1Code;

    @Length(min = 4, max = 4)
    @Exported(order = 14)
    @Column(name = "pin1_code", length = 4, columnDefinition = "varchar(4)")
    @Pattern(regexp = "^([0-9A-F]{4})$")
    private String pin1Code;

    @Exported(order = 13)
    @Column(name = "puk2_code", length = 8, columnDefinition = "varchar(8)")
    @Pattern(regexp = "^([0-9A-F]{8})$")
    private String puk2Code;

    @Exported(order = 15)
    @Column(name = "pin2_code", length = 4, columnDefinition = "varchar(4)")
    private String pin2Code;

    @Length(max = 32)
    @Column(name = "authentification_key", length = 32, columnDefinition = "varchar(32)")
    private String authKey;

    @Length(max = 4)
    @Column(name = "access_control_class", length = 4, columnDefinition = "varchar(4)")
    private String accessControlClass;

    @Exported(order = 16)
    @Column(name = "sim_profile", columnDefinition = "varchar(32)")
    private String simProfile;

    @Length(max = 32)
    @Column(name = "ota_ciphering_key", length = 32, columnDefinition = "varchar(32)")
    private String otaCipheringKey;

    @Length(max = 32)
    @Column(name = "ota_signature_key", length = 32, columnDefinition = "varchar(32)")
    private String otaSignatureKey;

    @Length(max = 32)
    @Column(name = "put_description_key", length = 32, columnDefinition = "varchar(32)")
    private String putDescriptionKey;

    @Length(max = 32)
    @Column(name = "admin_code", length = 32, columnDefinition = "varchar(32)")
    private String adminCode;

    @Exported(order = 17)
    @Length(max = 32)
    @Column(name = "number", length = 32, columnDefinition = "varchar(32)")
    private String number;

    @Exported(value = "code", order = 11)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plmn_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private Plmn plmn;

    @NotAudited
    @Exported(value = "name", order = 19)
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", referencedColumnName = "id")
    @ToString.Exclude
    private Provider provider;

    @Length(max = 32)
    @Column(name = "pack_id", length = 32, columnDefinition = "varchar(32)")
    private String packId;

    @Column(name = "transportKey", columnDefinition = "varchar(20)")
    private String transportKey;

    @Column(name = "algorithmVersion", columnDefinition = "int(11)")
    private Integer algorithmVersion;

    @Column(name = "checkDigit", columnDefinition = "int(11)")
    private Integer checkDigit;

    @Exported(order = 7)
    @Column(name = "brand", columnDefinition = "varchar(225)")
    private String brand;

    @Exported(order = 9)
    @Column(name = "is_esim", columnDefinition = "bit(1) default false", nullable = false)
    private boolean esim;

    @Column(name = "ota_salt", columnDefinition = "varchar(225)")
    private String otaSalt;

    @NotAudited
    @Column(name = "qr_code", columnDefinition = "LONGTEXT")
    private String qrCode;

    @Audited(targetAuditMode = NOT_AUDITED)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inventorypool_id", referencedColumnName = "id")
    private InventoryPool inventoryPool;

    @Exported(value = "allotmentId", order = 18)
    @Audited(targetAuditMode = NOT_AUDITED)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "allotment_id", referencedColumnName = "allotment_id")
    private AllotmentSummary allotment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paired_equipment_id", referencedColumnName = "id")
    private Equipment pairedEquipment;

    @Exported(order = 24)
    @Column(name = "activation_code", columnDefinition = "varchar(225)")
    private String activationCode;

    @Exported(order = 25)
    @Column(name = "confirmation_code", columnDefinition = "integer")
    private Integer confirmationCode;

    @Override
    public SimCard getInstance() {
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


