package mc.monacotelecom.tecrep.equipments.enums;

public enum AllotmentType {
    PREPAID,
    /**
     * @deprecated Use POSTPAID_B2B and POSTPAID_B2C instead.
     */
    @Deprecated
    POSTPAID,
    B2B,
    B2C,
    REPLACEMENT_SIM_CARD
}
