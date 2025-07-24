package mc.monacotelecom.tecrep.equipments.importer.implementations.context;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.importer.ImportParameters;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentModeSelection;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;

import java.util.Optional;

@RequiredArgsConstructor
public class ChangeWarehouseOrStatusContext {
    public static final String START_RANGE_KEY = "startRange";
    public static final String FINAL_RANGE_KEY = "finalRange";
    public static final String SELECTION_KEY = "selection";
    public static final String EVENT_KEY = "event";
    public static final String NAME_KEY = "name";
    public static final String CATEGORY_KEY = "category";
    public static final String BATCHNUMBER_KEY = "batchNumber";

    private final ImportParameters importParameters;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    public String getStartRange() {
        return importParameters.getParameter(START_RANGE_KEY)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, "Missing startRange in request"));
    }

    public String getFinalRange() {
        return importParameters.getParameter(FINAL_RANGE_KEY)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, "Missing finalRange in request"));
    }

    public EquipmentModeSelection getSelection() {
        return importParameters.getParameter(SELECTION_KEY)
                .map(EquipmentModeSelection::valueOf)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, "Missing selection in request"));
    }

    public Optional<Event> getEvent() {
        return importParameters.getParameter(EVENT_KEY)
                .map(String::toLowerCase)
                .map(Event::valueOf);
    }

    public Optional<String> getName() {
        return importParameters.getParameter(NAME_KEY);
    }

    public EquipmentCategory getCategory() {
        return importParameters.getParameter(CATEGORY_KEY)
                .map(EquipmentCategory::valueOf)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, "Missing category in request"));
    }

    public String getBatchNumber() {
        return importParameters.getParameter(BATCHNUMBER_KEY)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, "Missing batchNumber in request"));
    }
}
