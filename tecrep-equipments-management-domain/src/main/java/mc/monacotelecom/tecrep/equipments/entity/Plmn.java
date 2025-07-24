package mc.monacotelecom.tecrep.equipments.entity;

import lombok.*;
import mc.monacotelecom.inventory.common.importer.domain.entity.IEntity;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
@Table(name = "plmn")
public class Plmn implements Serializable, IEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long plmnId;

    @NotEmpty
    @Length(max = 12)
    @Column(name = "plmn_code", nullable = false, unique = true, length = 12, columnDefinition = "varchar(12)")
    private String code;

    @Column(name = "network_name", columnDefinition = "varchar(255)")
    private String networkName;

    @Length(max = 12)
    @Column(name = "tadig_code", length = 12, columnDefinition = "varchar(12)")
    private String tadigCode;

    @Length(max = 6)
    @Column(name = "country_iso_code", length = 6, columnDefinition = "varchar(6)")
    private String countryIsoCode;

    @Length(max = 40)
    @Column(name = "country_name", length = 40, columnDefinition = "varchar(40)")
    private String countryName;

    @Column(name = "ranges_prefix")
    private String rangesPrefix;

    @NotAudited
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(
            mappedBy = "plmn",
            fetch = FetchType.LAZY
    )
    private List<SimCard> simCards = new ArrayList<>();

    @Override
    public Plmn getInstance() {
        return this;
    }

    @Override
    public String getDatabaseId() {
        return String.valueOf(plmnId);
    }
}
