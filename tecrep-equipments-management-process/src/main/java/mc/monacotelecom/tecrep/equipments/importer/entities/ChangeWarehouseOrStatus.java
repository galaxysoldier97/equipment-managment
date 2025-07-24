package mc.monacotelecom.tecrep.equipments.importer.entities;

import lombok.Data;
import mc.monacotelecom.inventory.common.importer.domain.entity.IEntity;
import mc.monacotelecom.tecrep.equipments.entity.Equipment;
import mc.monacotelecom.tecrep.equipments.entity.Warehouse;
import mc.monacotelecom.tecrep.equipments.enums.Event;

import javax.validation.constraints.NotNull;

@Data
public class ChangeWarehouseOrStatus implements IEntity {

    @NotNull
    Equipment equipment;

    Warehouse warehouse;

    Event event;

    @Override
    public Equipment getInstance() {
        return equipment;
    }

    @Override
    public String getDatabaseId() {
        return equipment.getDatabaseId();
    }
}
