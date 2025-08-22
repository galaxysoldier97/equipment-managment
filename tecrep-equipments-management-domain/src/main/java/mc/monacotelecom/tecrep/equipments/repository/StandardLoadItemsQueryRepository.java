package mc.monacotelecom.tecrep.equipments.repository;

import mc.monacotelecom.tecrep.equipments.dto.GroupItem;
import mc.monacotelecom.tecrep.equipments.dto.MaterialItem;
import mc.monacotelecom.tecrep.equipments.dto.ModelItem;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class StandardLoadItemsQueryRepository {

    private static final String SQL_EXIST = "SELECT 1 FROM standard_load WHERE id = :idStandard";

    private static final String SQL_MODEL = "SELECT slem.id, slem.id_standard, slem.id_model, slem.min_qty, slem.max_qty, slem.created_at, em.name AS model_name " +
            "FROM standard_load_equipmentmodel slem JOIN equipment_model em ON em.id = slem.id_model " +
            "WHERE slem.id_standard = :idStandard ORDER BY em.name";

    private static final String SQL_MATERIAL = "SELECT slemat.id, slemat.id_standard, slemat.id_material, slemat.min_qty, slemat.max_qty, slemat.created_at, m.name AS material_name " +
            "FROM standard_load_equipmentmaterial slemat JOIN materials m ON m.id = slemat.id_material " +
            "WHERE slemat.id_standard = :idStandard ORDER BY m.name";

    private static final String SQL_GROUP = "SELECT sleg.id, sleg.id_standard, sleg.id_group, sleg.min_qty, sleg.max_qty, sleg.created_at, g.name AS group_name " +
            "FROM standard_load_equipmentgroups sleg JOIN equipment_groups g ON g.id = sleg.id_group " +
            "WHERE sleg.id_standard = :idStandard ORDER BY g.name";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public StandardLoadItemsQueryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsStandard(long idStandard) {
        try {
            jdbcTemplate.queryForObject(SQL_EXIST, Map.of("idStandard", idStandard), Integer.class);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    public List<ModelItem> findModels(long idStandard) {
        return jdbcTemplate.query(SQL_MODEL, new MapSqlParameterSource("idStandard", idStandard), new RowMapper<ModelItem>() {
            @Override
            public ModelItem mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ModelItem(
                        rs.getLong("id"),
                        rs.getLong("id_model"),
                        rs.getInt("min_qty"),
                        rs.getInt("max_qty"),
                        rs.getTimestamp("created_at").toInstant(),
                        rs.getString("model_name")
                );
            }
        });
    }

    public List<MaterialItem> findMaterials(long idStandard) {
        return jdbcTemplate.query(SQL_MATERIAL, new MapSqlParameterSource("idStandard", idStandard), new RowMapper<MaterialItem>() {
            @Override
            public MaterialItem mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new MaterialItem(
                        rs.getLong("id"),
                        rs.getLong("id_material"),
                        rs.getInt("min_qty"),
                        rs.getInt("max_qty"),
                        rs.getTimestamp("created_at").toInstant(),
                        rs.getString("material_name")
                );
            }
        });
    }

    public List<GroupItem> findGroups(long idStandard) {
        return jdbcTemplate.query(SQL_GROUP, new MapSqlParameterSource("idStandard", idStandard), new RowMapper<GroupItem>() {
            @Override
            public GroupItem mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new GroupItem(
                        rs.getLong("id"),
                        rs.getLong("id_group"),
                        rs.getInt("min_qty"),
                        rs.getInt("max_qty"),
                        rs.getTimestamp("created_at").toInstant(),
                        rs.getString("group_name")
                );
            }
        });
    }
}
