package mc.monacotelecom.tecrep.equipments.dto.request.update.v2;

import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.Activity;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentNature;
import mc.monacotelecom.tecrep.equipments.enums.SimCardSimProfile;

public interface UpdateSimCard {
    String getImsiNumber();

    String getImsiSponsorNumber();

    String getPin1Code();

    String getPin2Code();

    String getPuk1Code();

    String getPuk2Code();

    String getAuthKey();

    String getAccessControlClass();

    String getPackId();

    String getOrderId();

    String getSimProfile();

    String getNumber();

    String getBatchNumber();

    String getExternalNumber();

    Activity getActivity();

    EquipmentNature getNature();

    AccessType getAccessType();

    Long getServiceId();

    String getSerialNumber();

    Boolean getEsim();

    String getBrand();
}
