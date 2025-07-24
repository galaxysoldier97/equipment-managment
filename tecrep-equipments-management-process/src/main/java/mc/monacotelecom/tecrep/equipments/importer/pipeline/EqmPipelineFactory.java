package mc.monacotelecom.tecrep.equipments.importer.pipeline;

import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.importer.domain.entity.IEntity;
import mc.monacotelecom.inventory.common.importer.process.ImporterResolver;
import mc.monacotelecom.inventory.common.importer.process.pipeline.PersistStage;
import mc.monacotelecom.inventory.common.importer.process.pipeline.PipelineFactory;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.entity.*;
import mc.monacotelecom.tecrep.equipments.importer.entities.ChangeWarehouseOrStatus;
import mc.monacotelecom.tecrep.equipments.importer.pipeline.stage.*;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EqmPipelineFactory<T extends IEntity> extends PipelineFactory<T> {

    private final PlmnPersistStage plmnPersistStage;
    private final ProviderPersistStage providerPersistStage;
    private final SimCardPersistStage simcardPersistStage;
    private final WarehousePersistStage warehousePersistStage;
    private final ChangeWarehouseOrStatusPersistStage<? extends Equipment> changeWarehouseOrStatusPersistStage;
    private final CPEPersistStage cpePersistStage;
    private final SimCardGenerationConfigurationPersistStage simCardGenerationConfigurationPersistStage;
    private final InventoryPoolPersistStage inventoryPoolPersistStage;
    private final FileConfigurationPersistStage fileConfigurationPersistStage;
    private final AncillaryEquipmentPersistStage ancillaryEquipmentPersistStage;

    public EqmPipelineFactory(final PlmnPersistStage plmnPersistStage,
                              final ProviderPersistStage providerPersistStage,
                              final SimCardPersistStage simcardPersistStage,
                              final WarehousePersistStage warehousePersistStage,
                              final ChangeWarehouseOrStatusPersistStage<? extends Equipment> changeWarehouseOrStatusPersistStage,
                              final CPEPersistStage cpePersistStage,
                              final SimCardGenerationConfigurationPersistStage simCardGenerationConfigurationPersistStage,
                              final InventoryPoolPersistStage inventoryPoolPersistStage,
                              final FileConfigurationPersistStage fileConfigurationPersistStage,
                              final AncillaryEquipmentPersistStage ancillaryEquipmentPersistStage,
                              final LocalizedMessageBuilder localizedMessageBuilder,
                              final ImporterResolver importerResolver) {
        super(importerResolver, localizedMessageBuilder);
        this.plmnPersistStage = plmnPersistStage;
        this.providerPersistStage = providerPersistStage;
        this.simcardPersistStage = simcardPersistStage;
        this.warehousePersistStage = warehousePersistStage;
        this.changeWarehouseOrStatusPersistStage = changeWarehouseOrStatusPersistStage;
        this.cpePersistStage = cpePersistStage;
        this.simCardGenerationConfigurationPersistStage = simCardGenerationConfigurationPersistStage;
        this.inventoryPoolPersistStage = inventoryPoolPersistStage;
        this.fileConfigurationPersistStage = fileConfigurationPersistStage;
        this.ancillaryEquipmentPersistStage = ancillaryEquipmentPersistStage;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected PersistStage<T> resolvePersistStageForType(Class<? extends IEntity> type) {

        if (Provider.class.isAssignableFrom(type)) {
            return (PersistStage<T>) providerPersistStage;
        }

        if (Plmn.class.isAssignableFrom(type)) {
            return (PersistStage<T>) plmnPersistStage;
        }

        if (Warehouse.class.isAssignableFrom(type)) {
            return (PersistStage<T>) warehousePersistStage;
        }

        if (SimCard.class.isAssignableFrom(type)) {
            return (PersistStage<T>) simcardPersistStage;
        }

        if (ChangeWarehouseOrStatus.class.isAssignableFrom(type)) {
            return (PersistStage<T>) changeWarehouseOrStatusPersistStage;
        }

        if (CPE.class.isAssignableFrom(type)) {
            return (PersistStage<T>) cpePersistStage;
        }

        if (SimCardGenerationConfiguration.class.isAssignableFrom(type)) {
            return (PersistStage<T>) simCardGenerationConfigurationPersistStage;
        }

        if (InventoryPool.class.isAssignableFrom(type)) {
            return (PersistStage<T>) inventoryPoolPersistStage;
        }

        if (FileConfiguration.class.isAssignableFrom(type)) {
            return (PersistStage<T>) fileConfigurationPersistStage;
        }

        if (AncillaryEquipment.class.isAssignableFrom(type)) {
            return (PersistStage<T>) ancillaryEquipmentPersistStage;
        }

        return new PersistStage<>() {
            @Override
            public void run() {
                log.debug("No Persist Stage for type '{}'", type);
            }
        };
    }
}
