package mc.monacotelecom.tecrep.equipments.projections;

/**
 * Projection of SIM Card Entity, to be used for better performances.
 * (Avoid joining with other tables such as plmn, provider...)
 */
public interface SimCardProjection {
    Long getEquipmentId();

    String getImsiNumber();

    String getSerialNumber();
}
