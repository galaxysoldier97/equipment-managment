package mc.monacotelecom.tecrep.equipments.repository.specification;

import mc.monacotelecom.tecrep.equipments.entity.Plmn;
import org.springframework.data.jpa.domain.Specification;

public final class PlmnSpecifications {
    private PlmnSpecifications() {
    }

    public static Specification<Plmn> hasCode(String code) {
        return (root, query, cb) -> cb.equal(root.get("code"), code);
    }

    public static Specification<Plmn> prefixLike(String prefix) {
        return (root, query, cb) -> cb.like(root.get("rangesPrefix"), "%" + prefix + "%");
    }
}
