package mc.monacotelecom.tecrep.equipments.repository.specification;

import mc.monacotelecom.tecrep.equipments.entity.FileConfiguration;
import org.springframework.data.jpa.domain.Specification;

public final class FileConfigurationSpecifications {
    private FileConfigurationSpecifications() {
    }

    public static Specification<FileConfiguration> hasNameLike(String name) {
        return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
    }
}
