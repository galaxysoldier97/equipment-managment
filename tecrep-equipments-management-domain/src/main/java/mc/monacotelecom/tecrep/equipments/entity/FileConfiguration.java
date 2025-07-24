package mc.monacotelecom.tecrep.equipments.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import mc.monacotelecom.inventory.common.importer.domain.entity.IEntity;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "file_configuration")
public class FileConfiguration implements IEntity, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", columnDefinition = "varchar(50)", nullable = false, unique = true)
    private String name;

    @Column(name = "suffix", columnDefinition = "varchar(50)")
    private String suffix;

    @Column(name = "prefix", columnDefinition = "varchar(50)")
    private String prefix;

    @Column(name = "header_format", columnDefinition = "longtext")
    private String headerFormat;

    @Column(name = "record_format", columnDefinition = "longtext")
    private String recordFormat;

    @NotAudited
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(
            mappedBy = "importFileConfiguration",
            fetch = FetchType.LAZY
    )
    private List<SimCardGenerationConfiguration> simCardGenerationConfigurationsAsImport = new ArrayList<>();

    @NotAudited
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(
            mappedBy = "exportFileConfiguration",
            fetch = FetchType.LAZY
    )
    private List<SimCardGenerationConfiguration> simCardGenerationConfigurationsAsExport = new ArrayList<>();

    @Override
    public FileConfiguration getInstance() {
        return this;
    }

    @Override
    public String getDatabaseId() {
        return String.valueOf(id);
    }
}
