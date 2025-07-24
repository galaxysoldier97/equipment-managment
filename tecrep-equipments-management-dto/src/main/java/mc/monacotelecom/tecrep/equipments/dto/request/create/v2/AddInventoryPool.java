package mc.monacotelecom.tecrep.equipments.dto.request.create.v2;

import mc.monacotelecom.tecrep.equipments.enums.SimCardSimProfile;

public interface AddInventoryPool {
    String getCode();

    String getDescription();

    Integer getMvno();

    SimCardSimProfile getSimProfile();
}
