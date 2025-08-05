package mc.monacotelecom.tecrep.equipments.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "homologacion_material_sap")
public class HomologacionMaterialSap implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_material_sap")
    private String idMaterialSap;

    @Column(name = "name_sap")
    private String nameSap;

    @Column(name = "equipment_model_id")
    private Long equipmentModelId;

    @Column(name = "status")
    private String status;
}