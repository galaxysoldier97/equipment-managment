package mc.monacotelecom.tecrep.equipments.process.repository;

import mc.monacotelecom.tecrep.equipments.dto.StandardLoadItemDTO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StandardLoadItemRepository {

    private static final String INSERT_MODEL_SQL = "INSERT INTO standard_load_equipmentmodel (id_standard, id_model, min_qty, max_qty, created_at) VALUES (?, ?, ?, ?, NOW()) ON DUPLICATE KEY UPDATE min_qty = VALUES(min_qty), max_qty = VALUES(max_qty)";
    private static final String INSERT_MATERIAL_SQL = "INSERT INTO standard_load_equipmentmaterial (id_standard, id_material, min_qty, max_qty, created_at) VALUES (?, ?, ?, ?, NOW()) ON DUPLICATE KEY UPDATE min_qty = VALUES(min_qty), max_qty = VALUES(max_qty)";
    private static final String INSERT_GROUP_SQL = "INSERT INTO standard_load_equipmentgroups (id_standard, id_group, min_qty, max_qty, created_at) VALUES (?, ?, ?, ?, NOW()) ON DUPLICATE KEY UPDATE min_qty = VALUES(min_qty), max_qty = VALUES(max_qty)";

    private final JdbcTemplate jdbcTemplate;

    public StandardLoadItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int[] upsertModels(List<StandardLoadItemDTO> items) {
        return jdbcTemplate.batchUpdate(INSERT_MODEL_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                StandardLoadItemDTO item = items.get(i);
                ps.setLong(1, item.getIdStandard());
                ps.setLong(2, item.getIdModel());
                ps.setInt(3, item.getMinQty());
                ps.setInt(4, item.getMaxQty());
            }

            @Override
            public int getBatchSize() {
                return items.size();
            }
        });
    }

    public int[] upsertMaterials(List<StandardLoadItemDTO> items) {
        return jdbcTemplate.batchUpdate(INSERT_MATERIAL_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                StandardLoadItemDTO item = items.get(i);
                ps.setLong(1, item.getIdStandard());
                ps.setLong(2, item.getIdMaterial());
                ps.setInt(3, item.getMinQty());
                ps.setInt(4, item.getMaxQty());
            }

            @Override
            public int getBatchSize() {
                return items.size();
            }
        });
    }

    public int[] upsertGroups(List<StandardLoadItemDTO> items) {
        return jdbcTemplate.batchUpdate(INSERT_GROUP_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                StandardLoadItemDTO item = items.get(i);
                ps.setLong(1, item.getIdStandard());
                ps.setLong(2, item.getIdGroup());
                ps.setInt(3, item.getMinQty());
                ps.setInt(4, item.getMaxQty());
            }

            @Override
            public int getBatchSize() {
                return items.size();
            }
        });
    }
}
