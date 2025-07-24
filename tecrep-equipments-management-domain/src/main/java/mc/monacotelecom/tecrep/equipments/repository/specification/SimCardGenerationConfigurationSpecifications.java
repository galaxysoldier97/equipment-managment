package mc.monacotelecom.tecrep.equipments.repository.specification;

import mc.monacotelecom.tecrep.equipments.entity.SimCardGenerationConfiguration;
import org.springframework.data.jpa.domain.Specification;

public final class SimCardGenerationConfigurationSpecifications {
    private SimCardGenerationConfigurationSpecifications() {
    }

    public static Specification<SimCardGenerationConfiguration> nameStartWith(String name) {
        return (root, query, cb) -> cb.like(root.get("name"), name + "%");
    }

    public static Specification<SimCardGenerationConfiguration> hasTransportKey(final Integer transportKey) {
        return (root, query, cb) -> cb.equal(root.get("transportKey"), transportKey);
    }
}
