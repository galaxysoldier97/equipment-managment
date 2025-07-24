package mc.monacotelecom.tecrep.equipments.repository.specification;

import mc.monacotelecom.tecrep.equipments.entity.Warehouse;
import org.springframework.data.jpa.domain.Specification;

public final class WarehouseSpecifications {
    private WarehouseSpecifications() {
    }

    public static Specification<Warehouse> nameStartWith(String name) {
        return (root, query, cb) -> cb.like(root.get("name"), name + "%");
    }

    public static Specification<Warehouse> resellerCodeStartWith(String resellerCode) {
        return (root, query, cb) -> cb.like(root.get("resellerCode"), resellerCode + "%");
    }

}
