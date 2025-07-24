package mc.monacotelecom.tecrep.equipments.importer.implementations.context;


import lombok.Getter;
import mc.monacotelecom.importer.ImportParameters;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentModel;
import mc.monacotelecom.tecrep.equipments.entity.Warehouse;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentModelRepository;
import mc.monacotelecom.tecrep.equipments.repository.WarehouseRepository;
import org.apache.logging.log4j.util.Strings;

import java.util.List;

import static mc.monacotelecom.tecrep.equipments.enums.Status.AVAILABLE;
import static mc.monacotelecom.tecrep.equipments.enums.Status.INSTORE;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;

@Getter
public class EquipmentImporterContext {

    //constant
    public static final String RECYCLABLE_KEY = "recyclable";
    public static final String MODELNAME_KEY = "modelName";
    public static final String WAREHOUSE_ID_KEY = "warehouseId";
    public static final String STATUS_KEY = "status";
    public static final String BATCH_NUMBER = "batchNumber";
    public static final String CONFIGURATION = "configuration";

    //constuctor values
    protected final WarehouseRepository warehouseRepository;
    protected final EquipmentModelRepository equipmentModelRepository;
    protected final ImportParameters importParameters;
    protected final LocalizedMessageBuilder localizedMessageBuilder;

    //instance values
    protected Warehouse warehouse;
    protected EquipmentModel equipmentModel;
    protected boolean recyclable;
    protected Status status;
    protected String batchNumber;

    protected final EquipmentModelCategory equipmentModelCategory;

    public EquipmentImporterContext(final WarehouseRepository warehouseRepository,
                                    final EquipmentModelRepository equipmentModelRepository,
                                    final ImportParameters importParameters,
                                    final LocalizedMessageBuilder localizedMessageBuilder,
                                    final EquipmentModelCategory equipmentModelCategory) {

        this.warehouseRepository = warehouseRepository;
        this.equipmentModelRepository = equipmentModelRepository;
        this.importParameters = importParameters;
        this.localizedMessageBuilder = localizedMessageBuilder;
        this.equipmentModelCategory = equipmentModelCategory;
        this.wareHouse();
        this.modelName();
        this.recyclable();
        this.status();
        this.batchNumber();
    }

    private void wareHouse() {
        importParameters.getParameter(WAREHOUSE_ID_KEY).map(Long::parseLong).ifPresent(id ->
                this.warehouse = this.warehouseRepository.findById(id).orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, WAREHOUSE_ID_NOT_FOUND, id)));
    }

    private void modelName() {
        importParameters.getParameter(MODELNAME_KEY)
                .ifPresentOrElse(equipmentName ->
                                this.equipmentModel = equipmentModelRepository.findByNameAndCategory(equipmentName, this.equipmentModelCategory)
                                        .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, EQUIPMENT_NAME_NOT_FOUND, equipmentName))
                        , () -> {
                            throw new EqmValidationException(localizedMessageBuilder, "Missing modelName in request");
                        });
    }

    private void recyclable() {
        this.recyclable = importParameters.getParameter(RECYCLABLE_KEY)
                .map(Boolean::parseBoolean)
                .orElse(true);
    }

    private void status() {
        try {
            importParameters.getParameter(STATUS_KEY)
                    .map(String::toUpperCase)
                    .map(Status::valueOf)
                    .ifPresentOrElse(inputStatus -> {
                        if (!List.of(AVAILABLE, INSTORE).contains(inputStatus)) {
                            throw new EqmValidationException(localizedMessageBuilder, ANCILLARY_IMPORT_STB_INVALID_STATUS, inputStatus);
                        }
                        this.status = inputStatus;
                    }, () -> this.status = INSTORE);
        } catch (IllegalArgumentException e) {
            throw new EqmValidationException(localizedMessageBuilder, "Invalid value for 'status' parameter");
        }
    }

    private void batchNumber() {
        this.batchNumber = importParameters.getParameter(BATCH_NUMBER)
                .orElse(Strings.EMPTY);
    }
}
