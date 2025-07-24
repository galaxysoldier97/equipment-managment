package mc.monacotelecom.tecrep.equipments.repository.specification;

import mc.monacotelecom.tecrep.equipments.entity.Provider;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import org.springframework.data.jpa.domain.Specification;

public final class ProviderSpecifications {
    private ProviderSpecifications() {
    }

    public static Specification<Provider> nameStartWith(String name) {
        return (root, query, cb) -> cb.like(root.get("name"), name + "%");
    }

    public static Specification<Provider> hasAccessType(AccessType accessType) {
        return (root, query, cb) -> cb.equal(root.get("accessType"), accessType);
    }

}
