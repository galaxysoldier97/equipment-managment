package mc.monacotelecom.tecrep.equipments.repository.specification;

import mc.monacotelecom.tecrep.equipments.entity.EquipmentModel;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory;
import org.springframework.data.jpa.domain.Specification;

public final class EquipmentModelSpecifications {
    private EquipmentModelSpecifications() {}

    public static Specification<EquipmentModel> hasCategory(EquipmentModelCategory category) {
        return (root, query, cb) -> cb.equal(root.get("category"), category);
    }
}
