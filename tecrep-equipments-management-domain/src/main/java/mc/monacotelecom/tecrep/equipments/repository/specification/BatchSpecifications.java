package mc.monacotelecom.tecrep.equipments.repository.specification;

import mc.monacotelecom.tecrep.equipments.entity.Batch;
import org.springframework.data.jpa.domain.Specification;

public final class BatchSpecifications {
    private BatchSpecifications() {
    }

    public static Specification<Batch> hasNotProcessedDate() {
        return (root, query, cb) -> cb.isNull(root.get("processedDate"));
    }

    public static Specification<Batch> hasReturnedDate() {
        return (root, query, cb) -> cb.isNotNull(root.get("returnedDate"));
    }

    public static Specification<Batch> hasImportFileNameLike(String importFileName) {
        return (root, query, cb) -> cb.like(root.get("importFileName"), "%" + importFileName + "%");
    }

    public static Specification<Batch> hasExportFileNameLike(String exportFileName) {
        return (root, query, cb) -> cb.like(root.get("exportFileName"), "%" + exportFileName + "%");
    }

    public static Specification<Batch> hasInventoryPoolCode(String inventoryPoolCode) {
        return (root, query, cb) -> cb.equal(root.get("inventoryPool").get("code"), inventoryPoolCode);
    }

    public static Specification<Batch> hasConfigurationName(String configurationName) {
        return (root, query, cb) -> cb.equal(root.get("simCardGenerationConfiguration").get("name"), configurationName);
    }
}
