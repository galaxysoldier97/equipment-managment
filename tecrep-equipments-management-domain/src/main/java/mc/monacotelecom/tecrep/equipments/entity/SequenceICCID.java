package mc.monacotelecom.tecrep.equipments.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "SEQUENCE_ICCID")
public class SequenceICCID implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "seq_value", unique = true, nullable = false)
    private Long value;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(30)")
    private String name;
}
