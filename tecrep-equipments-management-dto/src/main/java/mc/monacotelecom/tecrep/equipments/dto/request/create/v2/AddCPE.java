package mc.monacotelecom.tecrep.equipments.dto.request.create.v2;

public interface AddCPE {
    String getSerialNumber();

    String getMacAddressCpe();

    String getMacAddressRouter();

    String getMacAddressVoip();

    String getMacAddressLan();

    String getMacAddress5G();

    String getMacAddress4G();
}
