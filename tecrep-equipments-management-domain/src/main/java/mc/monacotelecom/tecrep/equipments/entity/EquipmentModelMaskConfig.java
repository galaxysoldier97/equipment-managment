package mc.monacotelecom.tecrep.equipments.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "equipment_model_mask_config")
public class EquipmentModelMaskConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_id", nullable = false)
    private Long modelId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "mask_format", nullable = false)
    private String maskFormat;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('enable','disable')")
    private Status status;

    public enum Status {
        enable,
        disable
    }
}