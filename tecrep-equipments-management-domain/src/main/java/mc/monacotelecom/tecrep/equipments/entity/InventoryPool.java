package mc.monacotelecom.tecrep.equipments.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import mc.monacotelecom.inventory.common.importer.domain.entity.IEntity;
import mc.monacotelecom.tecrep.equipments.enums.SimCardSimProfile;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "inventory_pool")
public class InventoryPool implements Serializable, IEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long inventoryPoolId;

    @NotEmpty
    @Column(name = "code", unique = true, columnDefinition = "varchar(40)")
    private String code;

    @Column(name = "description", columnDefinition = "varchar(40)")
    private String description;

    @Column(name = "mvno", columnDefinition = "int(11)")
    private Integer mvno;

    @Enumerated(EnumType.STRING)
    @Column(name = "sim_profile", columnDefinition = "ENUM('DEFAULT','REPLACEMENT')")
    private SimCardSimProfile simProfile;

    @NotAudited
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(
            mappedBy = "inventoryPool",
            fetch = FetchType.LAZY
    )
    private List<SimCard> simCards = new ArrayList<>();

    @NotAudited
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(
            mappedBy = "inventoryPool",
            fetch = FetchType.LAZY
    )
    private List<Batch> batches = new ArrayList<>();

    @NotAudited
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(
            mappedBy = "inventoryPool",
            fetch = FetchType.LAZY
    )
    private List<AllotmentSummary> allotments = new ArrayList<>();

    @Override
    public InventoryPool getInstance() {
        return this;
    }

    @Override
    public String getDatabaseId() {
        return String.valueOf(inventoryPoolId);
    }
}
