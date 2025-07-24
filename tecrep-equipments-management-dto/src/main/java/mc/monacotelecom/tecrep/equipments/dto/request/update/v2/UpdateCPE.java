package mc.monacotelecom.tecrep.equipments.dto.request.update.v2;

public interface UpdateCPE {
    String getMacAddressCpe();

    String getMacAddressRouter();

    String getMacAddressVoip();

    String getMacAddress5G();

    String getMacAddressLan();

    String getMacAddress4G();

    String getChipsetId();

    String getHwVersion();

    String getOrderId();

    String getBatchNumber();

    String getExternalNumber();

    Long getServiceId();

    String getSerialNumber();
}
