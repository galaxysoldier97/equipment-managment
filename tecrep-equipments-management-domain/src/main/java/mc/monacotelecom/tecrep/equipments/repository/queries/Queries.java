package mc.monacotelecom.tecrep.equipments.repository.queries;

public final class Queries {

    private Queries() {
    }

    public static final String DELETE_AUDIT_EQUIPMENT_QUERY = "delete " +
            "from equipment_aud " +
            "where revision_id in " +
            "      (select revision_id " +
            "       from (select id, " +
            "                    revision_id, " +
            "                    row_number() over (partition by id order by revision_id desc) as n " +
            "             from equipment_aud eqm_aud " +
            "             where id = :id) tmp " +
            "       where n > 1) ";

    public static final String DELETE_ALL_EQM_AUDIT_IN_QUERY = "delete eqm " +
            "from equipment_aud as eqm " +
            "where eqm.id in :ids ";

    public static final String DELETE_ALL_SIMCARD_AUDIT_IN_QUERY = "delete eqm " +
            "from simcard_aud as eqm " +
            "where eqm.id in :ids ";

    public static final String DELETE_ALL_CPE_AUDIT_IN_QUERY = "delete eqm " +
            "from cpe_aud as eqm " +
            "where eqm.id in :ids ";

    public static final String DELETE_ALL_ANCYLLARY_AUDIT_IN_QUERY = "delete eqm " +
            "from ancillary_equipment as eqm " +
            "where eqm.id in :ids ";

    public static final String UPDATE_AUDIT_REMOVE_PAIRED_EQUIPMENT = "update ancillary_equipment_aud " +
            "set paired_equipment_id = null " +
            "where paired_equipment_id in :ids ";
    public static final String DELETE_AUDIT_CPE_QUERY = "delete " +
            "from cpe_aud " +
            "where revision_id in " +
            "      (select revision_id " +
            "       from (select id, " +
            "                    revision_id, " +
            "                    row_number() over (partition by id order by revision_id desc) as n " +
            "             from equipment_aud eqm_aud " +
            "             where id = :id) tmp " +
            "       where n > 1) ";

    public static final String DELETE_AUDIT_SIMCARD_QUERY = "delete " +
            "from simcard_aud " +
            "where revision_id in " +
            "      (select revision_id " +
            "       from (select id, " +
            "                    revision_id, " +
            "                    row_number() over (partition by id order by revision_id desc) as n " +
            "             from equipment_aud eqm_aud " +
            "             where id = :id) tmp " +
            "       where n > 1) ";

    public static final String DELETE_AUDIT_ANCILARY_QUERY = "delete " +
            "from ancillary_equipment_aud " +
            "where revision_id in " +
            "      (select revision_id " +
            "       from (select id, " +
            "                    revision_id, " +
            "                    row_number() over (partition by id order by revision_id desc) as n " +
            "             from equipment_aud eqm_aud " +
            "             where id = :id) tmp " +
            "       where n > 1) ";
}
