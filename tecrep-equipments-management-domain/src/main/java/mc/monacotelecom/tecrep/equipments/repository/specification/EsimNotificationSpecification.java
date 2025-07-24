package mc.monacotelecom.tecrep.equipments.repository.specification;

import mc.monacotelecom.tecrep.equipments.entity.EsimNotification;
import org.springframework.data.jpa.domain.Specification;

public final class EsimNotificationSpecification {

    private EsimNotificationSpecification(){}

    public static Specification<EsimNotification> hasEquipmentId(Long id){
        return (root, query, cb) -> cb.equal(root.get("equipment").get("equipmentId"), id);
    }

    public static Specification<EsimNotification> hasIccid(String iccid){
        return (root, query, cb) -> cb.equal(root.get("iccid"), iccid);
    }

    public static Specification<EsimNotification> hasProfileType(String profileType){
        return (root, query, cb) -> cb.equal(root.get("profileType"), profileType);
    }
}
