package mc.monacotelecom.tecrep.equipments.entity;

import lombok.*;
import mc.monacotelecom.inventory.common.importer.domain.entity.IEntity;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "warehouse")
public class Warehouse implements IEntity, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long warehouseId;

    @Length(max = 40)
    @Column(name = "name", nullable = false, unique = true, length = 40, columnDefinition = "varchar(40)")
    private String name;

    @Length(max = 20)
    @Column(name = "reseller_code", length = 20, columnDefinition = "varchar(20)")
    private String resellerCode;

    @NotAudited
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(
            mappedBy = "warehouse",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Equipment> equipments = new ArrayList<>();

    @Override
    public String getDatabaseId() {
        return String.valueOf(warehouseId);
    }

    @Override
    public Warehouse getInstance() {
        return this;
    }
}
