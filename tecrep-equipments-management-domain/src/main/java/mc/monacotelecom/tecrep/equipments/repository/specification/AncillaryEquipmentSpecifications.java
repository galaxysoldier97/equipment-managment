package mc.monacotelecom.tecrep.equipments.repository.specification;

import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentName;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import org.springframework.data.jpa.domain.Specification;

public final class AncillaryEquipmentSpecifications {

    private static final String PAIRED_EQUIPMENT_COLUMN = "pairedEquipment";

    private AncillaryEquipmentSpecifications() {
    }

    public static Specification<AncillaryEquipment> hasSfpVersion(String sfpVersion) {
        return (root, query, cb) -> cb.like(root.get("sfpVersion"), sfpVersion);
    }

    public static Specification<AncillaryEquipment> hasServiceId(Long serviceId) {
        return (root, query, cb) -> cb.equal(root.get("serviceId"), serviceId);
    }

    public static Specification<AncillaryEquipment> containSerialNumber(String serialNumber) {
        String md = "%" + serialNumber + "%";
        return (root, query, cb) -> cb.like(root.get("serialNumber"), md);
    }

    public static Specification<AncillaryEquipment> hasExternalNumber(String externalNumber) {
        return EquipmentSpecifications.hasExternalNumber(externalNumber);
    }

    public static Specification<AncillaryEquipment> hasMacAddress(String macAddress) {
        if (!macAddress.contains(":")) {
            macAddress = formatMacAddress(macAddress);
        }
        String md = "%" + macAddress + "%";
        return (root, query, cb) -> cb.like(root.get("macAddress"), md);
    }

    public static Specification<AncillaryEquipment> hasModelName(String modelName) {
        return (root, query, cb) -> cb.equal(root.get("model").get("name"), modelName);
    }

    public static Specification<AncillaryEquipment> hasStatus(Status status) {
        return EquipmentSpecifications.hasStatus(status);
    }

    public static Specification<AncillaryEquipment> hasEquipmentName(EquipmentName equipmentName) {
        return (root, query, cb) -> cb.equal(root.get("equipmentName"), equipmentName);
    }

    public static Specification<AncillaryEquipment> hasNature(EquipmentNature nature) {
        return EquipmentSpecifications.hasNature(nature);
    }

    public static Specification<AncillaryEquipment> hasPairedEquipmentId(Long pairedEquipmentId) {
        return (root, query, cb) -> cb.equal(root.get(PAIRED_EQUIPMENT_COLUMN).get("equipmentId"), pairedEquipmentId);
    }

    public static Specification<AncillaryEquipment> hasProviderName(String providerName) {
        return (root, query, cb) -> cb.equal(root.get("model").get("provider").get("name"), providerName);
    }

    public static Specification<AncillaryEquipment> hasWarehouse(String warehouseName) {
        return EquipmentSpecifications.hasWarehouseName(warehouseName);
    }

    public static Specification<AncillaryEquipment> hasAccessType(AccessType accessType) {
        return EquipmentSpecifications.hasAccessType(accessType);
    }

    public static Specification<AncillaryEquipment> hasOrderId(String orderId) {
        return EquipmentSpecifications.hasOrderId(orderId);
    }

    public static Specification<AncillaryEquipment> hasOrder() {
        return EquipmentSpecifications.hasOrder();
    }

    public static Specification<AncillaryEquipment> hasNoOrder() {
        return EquipmentSpecifications.hasNoOrder();
    }

    public static Specification<AncillaryEquipment> hasEquipment() {
        return (root, query, cb) -> cb.isNotNull(root.get(PAIRED_EQUIPMENT_COLUMN));
    }

    public static Specification<AncillaryEquipment> hasNoEquipment() {
        return (root, query, cb) -> cb.isNull(root.get(PAIRED_EQUIPMENT_COLUMN));
    }

    public static Specification<AncillaryEquipment> isIndependent() {
        return (root, query, cb) -> cb.isTrue(root.get("independent"));
    }

    public static Specification<AncillaryEquipment> hasBatchNumber(String batchNumber) {
        return EquipmentSpecifications.hasBatchNumber(batchNumber);
    }

    public static Specification<AncillaryEquipment> isNotIndependent() {
        return (root, query, cb) -> cb.isFalse(root.get("independent"));
    }

    public static String formatMacAddress(String unformattedMAC) {
        char divisionChar = ':';
        return unformattedMAC.replaceAll("(.{2})", "$1" + divisionChar).substring(0, 17);
    }
}
