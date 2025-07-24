package mc.monacotelecom.tecrep.equipments.projections;

import mc.monacotelecom.inventory.common.recycling.interfaces.BaseEntityProjection;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import mc.monacotelecom.tecrep.equipments.enums.Status;

public interface EquipmentProjection extends BaseEntityProjection<Status> {

    String getSerialNumber();

    Long getEquipmentId();

    Boolean getRecyclable();

    @Override
    Status getStatus();

    EquipmentCategory getCategory();
}
