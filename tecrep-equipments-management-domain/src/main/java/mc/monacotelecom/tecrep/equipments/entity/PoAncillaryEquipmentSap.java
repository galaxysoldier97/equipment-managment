package mc.monacotelecom.tecrep.equipments.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "po_ancillary_equipment_sap")
public class PoAncillaryEquipmentSap implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "po_no", length = 50)
    private String poNo;

    @Column(name = "model", length = 50)
    private String model;

    @Column(name = "status")
    private String status;
}
