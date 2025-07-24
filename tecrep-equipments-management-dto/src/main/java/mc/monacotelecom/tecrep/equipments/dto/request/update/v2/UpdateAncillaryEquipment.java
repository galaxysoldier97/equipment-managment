package mc.monacotelecom.tecrep.equipments.dto.request.update.v2;

public interface UpdateAncillaryEquipment {
    String getMacAddress();

    String getOrderId();

    Boolean getIndependent();

    String getSfpVersion();

    String getBatchNumber();

    String getExternalNumber();

    Long getServiceId();

    Long getPairedEquipmentId();
}
