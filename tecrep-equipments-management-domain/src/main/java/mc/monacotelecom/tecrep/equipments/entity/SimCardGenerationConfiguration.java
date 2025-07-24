package mc.monacotelecom.tecrep.equipments.entity;

import lombok.*;
import mc.monacotelecom.inventory.common.importer.domain.entity.IEntity;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "simcard_generation")
public class SimCardGenerationConfiguration implements Serializable, IEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "bigint(20)")
    private Long simcardGenerationId;

    @NotEmpty
    @Column(name = "name", nullable = false, unique = true, columnDefinition = "varchar(40)")
    private String name;

    @ManyToOne
    @JoinColumn(name = "export_file_config", columnDefinition = "bigint(20)", nullable = false)
    private FileConfiguration exportFileConfiguration;

    @ManyToOne
    @JoinColumn(name = "import_file_config", columnDefinition = "bigint(20)", nullable = false)
    private FileConfiguration importFileConfiguration;

    @Column(name = "transportKey", columnDefinition = "varchar(20)")
    private String transportKey;

    @Column(name = "artwork", columnDefinition = "varchar(50)")
    private String artwork;

    @Column(name = "sim_reference", columnDefinition = "varchar(50)")
    private String simReference;

    @Column(name = "algorithmVersion", columnDefinition = "int(11)")
    private Integer algorithmVersion;

    @Column(name = "type", columnDefinition = "varchar(40)")
    private String type;

    @Column(name = "fixed_prefix", columnDefinition = "varchar(10)")
    private String fixedPrefix;

    @Column(name = "sequence_prefix", columnDefinition = "varchar(10)")
    private String sequencePrefix;

    // NB. The Plmn.code contains the MCC (mobile country code) and MNC
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plmn_id", referencedColumnName = "id")
    private Plmn plmn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "provider_id", referencedColumnName = "id")
    private Provider provider;

    @Column(name = "msin_sequence", columnDefinition = "varchar(30)")
    private String msinSequence;

    @Column(name = "iccid_sequence", columnDefinition = "varchar(30)")
    private String iccidSequence;

    @NotAudited
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "simCardGenerationConfiguration", fetch = FetchType.LAZY
    )
    private List<Batch> batches = new ArrayList<>();

    @Override
    public SimCardGenerationConfiguration getInstance() {
        return this;
    }

    @Override
    public String getDatabaseId() {
        return String.valueOf(simcardGenerationId);
    }
}
