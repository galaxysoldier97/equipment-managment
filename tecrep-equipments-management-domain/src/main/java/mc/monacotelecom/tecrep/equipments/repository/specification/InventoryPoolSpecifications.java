package mc.monacotelecom.tecrep.equipments.repository.specification;

import mc.monacotelecom.tecrep.equipments.entity.InventoryPool;
import mc.monacotelecom.tecrep.equipments.enums.SimCardSimProfile;
import org.springframework.data.jpa.domain.Specification;

public final class InventoryPoolSpecifications {

    private InventoryPoolSpecifications() {
    }

    public static Specification<InventoryPool> hasCode(String code) {
        return (root, query, cb) -> cb.equal(root.get("code"), code);
    }

    public static Specification<InventoryPool> hasMvno(Integer mvno) {
        return (root, query, cb) -> cb.equal(root.get("mvno"), mvno);
    }

    public static Specification<InventoryPool> hasSimProfile(SimCardSimProfile simProfile) {
        return (root, query, cb) -> cb.equal(root.get("simProfile"), simProfile);
    }
}
