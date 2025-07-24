package mc.monacotelecom.tecrep.equipments.repository.specification;

import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import org.springframework.data.jpa.domain.Specification;

public final class SimCardSpecifications {

    private SimCardSpecifications() {
    }

    public static Specification<SimCard> imsiNumberStartWith(String imsiNumber) {
        return (root, query, cb) -> cb.like(root.get("imsiNumber"), "%" + imsiNumber + "%");
    }

    public static Specification<SimCard> serialNumberStartWith(String serialNumber) {
        return EquipmentSpecifications.serialNumberStartWith(serialNumber);
    }

    public static Specification<SimCard> hasStatus(Status status) {
        return EquipmentSpecifications.hasStatus(status);
    }

    public static Specification<SimCard> hasNature(EquipmentNature nature) {
        return EquipmentSpecifications.hasNature(nature);
    }

    public static Specification<SimCard> imsiSponsorNumberNumberStartWith(String imsiSponsorNumber) {
        return (root, query, cb) -> cb.like(root.get("imsiSponsorNumber"), "%" + imsiSponsorNumber + "%");
    }

    public static Specification<SimCard> hasPairedEquipmentSerialNumber(String serialNumber) {
        return (root, query, cb) -> cb.equal(root.get("pairedEquipment").get("serialNumber"), serialNumber);
    }

    public static Specification<SimCard> isESim(final boolean eSim) {
        return (root, query, cb) -> cb.equal(root.get("esim"), eSim);
    }

    public static Specification<SimCard> hasProviderName(String providerName) {
        return (root, query, cb) -> cb.equal(root.get("provider").get("name"), providerName);
    }

    public static Specification<SimCard> hasWarehouse(String warehouseName) {
        return EquipmentSpecifications.hasWarehouseName(warehouseName);
    }

    public static Specification<SimCard> hasServiceId(String serviceId) {
        return EquipmentSpecifications.hasServiceId(serviceId);
    }

    public static Specification<SimCard> hasService() {
        return EquipmentSpecifications.hasService();
    }

    public static Specification<SimCard> hasNoService() {
        return EquipmentSpecifications.hasNoService();
    }

    public static Specification<SimCard> hasOrderId(String orderId) {
        return EquipmentSpecifications.hasOrderId(orderId);
    }

    public static Specification<SimCard> hasOrder() {
        return EquipmentSpecifications.hasOrder();
    }

    public static Specification<SimCard> hasNoOrder() {
        return EquipmentSpecifications.hasNoOrder();
    }

    public static Specification<SimCard> hasAccessType(AccessType accessType) {
        return EquipmentSpecifications.hasAccessType(accessType);
    }

    public static Specification<SimCard> externalNumberStartWith(String externalNumber) {
        return EquipmentSpecifications.hasExternalNumber(externalNumber);
    }

    public static Specification<SimCard> hasPreactivated(Boolean preactivated) {
        return EquipmentSpecifications.hasPreactivated(preactivated);
    }

    public static Specification<SimCard> hasBatchNumber(String batchNumber) {
        return EquipmentSpecifications.hasBatchNumber(batchNumber);
    }

    public static Specification<SimCard> hasPackId(String packId) {
        return EquipmentSpecifications.hasPackId(packId);
    }

    public static Specification<SimCard> hasInventoryPoolCode(String invPool) {
        return EquipmentSpecifications.hasInventoryPoolCode(invPool);
    }

    public static Specification<SimCard> hasInventoryPoolId(String invPool) {
        return EquipmentSpecifications.hasInventoryPoolId(invPool);
    }

    public static Specification<SimCard> hasAllotmentId(String allotmentId) {
        return (root, query, cb) -> cb.equal(root.get("allotment").get("allotmentId"), allotmentId);
    }

    public static Specification<SimCard> hasNumber(final String number) {
        return (root, query, cb) -> cb.equal(root.get("number"), number);
    }

    public static Specification<SimCard> hasPlmnCode(String plmnCode) {
        return (root, query, cb) -> cb.equal(root.get("plmn").get("code"), plmnCode);
    }

    public static Specification<SimCard> hasBrand(String brand){
        return (root,query, cb) -> cb.equal(root.get("brand"), brand);
    }

    public static Specification<SimCard> hasPin1Code(String code) {
        return (root, query, cb) -> cb.like(root.get("pin1Code"), code);
    }

    public static Specification<SimCard> hasPin2Code(String code) {
        return (root, query, cb) -> cb.like(root.get("pin2Code"), code);
    }

    public static Specification<SimCard> hasPuk1Code(String code) {
        return (root, query, cb) -> cb.like(root.get("puk1Code"), code);
    }

    public static Specification<SimCard> hasPuk2Code(String code) {
        return (root, query, cb) -> cb.like(root.get("puk2Code"), code);
    }

    public static Specification<SimCard> hasSimProfile(String simProfile) {
        return (root, query, cb) -> cb.like(root.get("simProfile"), simProfile);
    }
}
