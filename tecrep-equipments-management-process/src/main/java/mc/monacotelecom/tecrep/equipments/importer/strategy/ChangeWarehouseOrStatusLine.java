package mc.monacotelecom.tecrep.equipments.importer.strategy;

import lombok.Data;
import mc.monacotelecom.importer.ActionType;
import mc.monacotelecom.importer.ImportLine;
import mc.monacotelecom.tecrep.equipments.importer.entities.ChangeWarehouseOrStatus;
import org.slf4j.event.Level;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static mc.monacotelecom.importer.ActionType.PERSIST;

@Data
public class ChangeWarehouseOrStatusLine implements ImportLine {

    Collection<ChangeWarehouseOrStatus> nodes;
    private Level severity;
    private String message;

    public ChangeWarehouseOrStatusLine(List<ChangeWarehouseOrStatus> nodes) {
        this.nodes = nodes;
    }

    @Override
    public List<Class<? extends ImportLine>> getSheetMapClasses() {
        return null;
    }

    @Override
    public Set<Long> getExcludedIds() {
        return Collections.emptySet();
    }

    @Override
    public ActionType getActionType() {
        return PERSIST;
    }

    @Override
    public AtomicInteger getSaveDepth() {
        return new AtomicInteger(0);
    }

    @Override
    public String getGroup() {
        return null;
    }

    @Override
    public int getIndex() {
        return 0;
    }

    @Override
    public int getIndexInGroup() {
        return 0;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public ImportLine withoutNodes() {
        return null;
    }
}