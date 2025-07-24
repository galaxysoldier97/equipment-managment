package mc.monacotelecom.tecrep.equipments.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mc.monacotelecom.inventory.common.importer.domain.entity.IEntity;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
@Table(name = "provider")
@NaturalIdCache
public class Provider implements IEntity, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long providerId;

    @NotEmpty
    @Length(max = 40)
    @Column(name = "name", nullable = false, unique = true, length = 40, columnDefinition = "varchar(40)")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_type", nullable = false, columnDefinition = "ENUM('DOCSIS', 'EMTA', 'FTTH', 'FIREWALL', 'STB_HD', 'STB_ANDROID', 'STB_STICKTV', 'STB_ONT', 'STB_DTH', 'EXTENSOR_WIFI', 'EXTENSOR_WIFI_PLUS', 'EXTENSOR_WIFI_B2B', 'ALARMS', 'CENTRAL_TELEFONICA', 'SONDAS_DE_MONITOREO', 'MOBILE')") 
    private AccessType accessType;

    @Override
    public Provider getInstance() {
        return this;
    }

    @Override
    public String getDatabaseId() {
        return String.valueOf(providerId);
    }
}
