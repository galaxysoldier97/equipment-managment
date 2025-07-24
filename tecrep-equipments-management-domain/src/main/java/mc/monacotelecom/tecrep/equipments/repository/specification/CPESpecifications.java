package mc.monacotelecom.tecrep.equipments.repository.specification;

import mc.monacotelecom.tecrep.equipments.entity.CPE;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import org.springframework.data.jpa.domain.Specification;

public final class CPESpecifications {
    private CPESpecifications() {
    }

    public static Specification<CPE> serialNumberContain(String serialNumber) {
        return EquipmentSpecifications.serialNumberStartWith(serialNumber);
    }

    public static Specification<CPE> hasMacAddress(String macAddress) {
        return hasMacAddressCpe(macAddress).or(hasMacAddressRouter(macAddress)).or(hasMacAddressVoip(macAddress));
    }

    public static Specification<CPE> hasMacAddressCpe(String macAddressCpe) {
        String md = "%" + macAddressCpe + "%";
        return (root, query, cb) -> cb.like(root.get("macAddressCpe"), md);
    }

    public static Specification<CPE> hasMacAddressRouter(String macAddressRouter) {
        String md = "%" + macAddressRouter + "%";
        return (root, query, cb) -> cb.like(root.get("macAddressRouter"), md);
    }

    public static Specification<CPE> hasMacAddressVoip(String macAddressVoip) {
        String md = "%" + macAddressVoip + "%";
        return (root, query, cb) -> cb.like(root.get("macAddressVoip"), md);
    }

    public static Specification<CPE> hasMacAddressLan(String macAddressLan) {
        String md = "%" + macAddressLan + "%";
        return (root, query, cb) -> cb.like(root.get("macAddressLan"), md);
    }

    public static Specification<CPE> hasMacAddress5G(String macAddress5G) {
        String md = "%" + macAddress5G + "%";
        return (root, query, cb) -> cb.like(root.get("macAddress5G"), md);
    }

    public static Specification<CPE> hasMacAddress24G(String macAddress24G) {
        String md = "%" + macAddress24G + "%";
        return (root, query, cb) -> cb.like(root.get("macAddress4G"), md);
    }

    public static Specification<CPE> hasHwVersion(String hwVersion) {
        return (root, query, cb) -> cb.equal(root.get("hwVersion"), hwVersion);
    }

    public static Specification<CPE> hasModelName(String modelName) {
        return (root, query, cb) -> cb.equal(root.get("model").get("name"), modelName);
    }

    public static Specification<CPE> hasStatus(Status status) {
        return EquipmentSpecifications.hasStatus(status);
    }

    public static Specification<CPE> hasNature(EquipmentNature nature) {
        return EquipmentSpecifications.hasNature(nature);
    }

    public static Specification<CPE> hasProviderName(String providerName) {
        return (root, query, cb) -> cb.equal(root.get("model").get("provider").get("name"), providerName);
    }

    public static Specification<CPE> hasWarehouse(String warehouseName) {
        return EquipmentSpecifications.hasWarehouseName(warehouseName);
    }

    public static Specification<CPE> hasAccessType(AccessType accessType) {
        return EquipmentSpecifications.hasAccessType(accessType);
    }

    public static Specification<CPE> hasBatchNumber(String batchNumber) {
        return EquipmentSpecifications.hasBatchNumber(batchNumber);
    }

    public static Specification<CPE> externalNumberStartWith(String externalNumber) {
        return EquipmentSpecifications.hasExternalNumber(externalNumber);
    }

    public static Specification<CPE> hasOrderId(String orderId) {
        return EquipmentSpecifications.hasOrderId(orderId);
    }

    public static Specification<CPE> hasOrder() {
        return EquipmentSpecifications.hasOrder();
    }

    public static Specification<CPE> hasNoOrder() {
        return EquipmentSpecifications.hasNoOrder();
    }

    public static Specification<CPE> hasServiceId(String serviceId) {
        return EquipmentSpecifications.hasServiceId(serviceId);
    }

    public static Specification<CPE> hasService() {
        return EquipmentSpecifications.hasService();
    }

    public static Specification<CPE> hasNoService() {
        return EquipmentSpecifications.hasNoService();
    }
}