package mc.monacotelecom.tecrep.equipments.importer.interfaces;

public interface ICPELines {

    default String getExternalNumber() {return "";}
    default String getSerialNumber() {return null;}
    default String getMacAddressLan() {
        return null;
    }
    default String getMacAddress5G() {
        return null;
    }
    default String getMacAddress4G() {
        return null;
    }
    default String getMacAddressCpe() {
        return null;
    }
    default String getGponDef() {
        return "";
    }
    default String getMacAddressRouter() {
        return null;
    }
    default String getOntSn() {
        return "";
    }
    default String getOntPw() {
        return "";
    }
    default String getHwVersion() {
        return "";
    }
    default String getSfpVersion() {
        return "";
    }
    default String getWpaKey() {
        return "";
    }
    default String getModel() {
        return "";
    }
    default String getMacAddressVoip() {
        return null;
    }
    default String getDefaultSsid() {
        return "";
    }
    default String getDefaultWifiPassword() {
        return "";
    }
    default String getStbSn() {
        return "";
    }
    default String getChipsetId() {
        return "";
    }
    default String getPalletNumber() {
        return "";
    }
}
