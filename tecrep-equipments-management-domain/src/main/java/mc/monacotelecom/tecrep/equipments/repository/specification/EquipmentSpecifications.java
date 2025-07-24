package mc.monacotelecom.tecrep.equipments.repository.specification;

import mc.monacotelecom.tecrep.equipments.entity.Equipment;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import org.springframework.data.jpa.domain.Specification;

public final class EquipmentSpecifications {

    public static final String ORDER_ID = "orderId";
    public static final String SERVICE_ID = "serviceId";

    private EquipmentSpecifications() {
    }

    public static <T extends Equipment> Specification<T> serialNumberStartWith(String serialNumber) {
        String sn = !serialNumber.contains("%") ? "%" + serialNumber + "%" : serialNumber;
        return (root, query, cb) -> cb.like(root.get("serialNumber"), sn);
    }

    public static <T extends Equipment> Specification<T> hasStatus(Status status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static <T extends Equipment> Specification<T> hasNature(EquipmentNature nature) {
        return (root, query, cb) -> cb.equal(root.get("nature"), nature);
    }

    public static <T extends Equipment> Specification<T> hasWarehouseName(String warehouseName) {
        return (root, query, cb) -> cb.equal(root.get("warehouse").get("name"), warehouseName);
    }

    public static <T extends Equipment> Specification<T> hasAccessType(AccessType accessType) {
        return (root, query, cb) -> cb.equal(root.get("accessType"), accessType);
    }

    public static <T extends Equipment> Specification<T> hasOrderId(String orderId) {
        return (root, query, cb) -> cb.equal(root.get(ORDER_ID), orderId);
    }

    public static <T extends Equipment> Specification<T> hasOrder() {
        return (root, query, cb) -> cb.isNotNull(root.get(ORDER_ID));
    }

    public static <T extends Equipment> Specification<T> hasNoOrder() {
        return (root, query, cb) -> cb.isNull(root.get(ORDER_ID));
    }

    public static <T extends Equipment> Specification<T> hasPreactivated(Boolean preactivated) {
        return (root, query, cb) -> cb.equal(root.get("preactivated"), preactivated);
    }

    public static <T extends Equipment> Specification<T> hasBatchNumber(String batchNumber) {
        return (root, query, cb) -> cb.equal(root.get("batchNumber"), batchNumber);
    }

    public static <T extends Equipment> Specification<T> hasPackId(String packId) {
        return (root, query, cb) -> cb.equal(root.get("packId"), packId);
    }

    public static <T extends Equipment> Specification<T> hasInventoryPoolCode(String inventoryPoolCode) {
        return (root, query, cb) -> cb.equal(root.get("inventoryPool").get("code"), inventoryPoolCode);
    }

    public static <T extends Equipment> Specification<T> hasInventoryPoolId(String inventoryPoolId) {
        return (root, query, cb) -> cb.equal(root.get("inventoryPool").get("inventoryPoolId"), inventoryPoolId);
    }

    public static <T extends Equipment> Specification<T> hasServiceId(String serviceid) {
        return (root, query, cb) -> cb.equal(root.get(SERVICE_ID), serviceid);
    }

    public static <T extends Equipment> Specification<T> hasService() {
        return (root, query, cb) -> cb.isNotNull(root.get(SERVICE_ID));
    }

    public static <T extends Equipment> Specification<T> hasNoService() {
        return (root, query, cb) -> cb.isNull(root.get(SERVICE_ID));
    }

    public static <T extends Equipment> Specification<T> hasExternalNumber(String externalNumber) {
        return (root, query, cb) -> cb.like(root.get("externalNumber"), "%" + externalNumber + "%");
    }
}