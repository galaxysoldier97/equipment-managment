package mc.monacotelecom.tecrep.equipments.importer.strategy;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.importer.ImportStrategy;
import mc.monacotelecom.importer.csv.CsvImportStrategyBuilder;
import mc.monacotelecom.inventory.common.importer.domain.entity.IEntity;
import mc.monacotelecom.inventory.common.importer.process.strategy.CommonImportStrategyFactory;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.entity.*;
import mc.monacotelecom.tecrep.equipments.importer.data.GenericEquipementCsvLines;
import mc.monacotelecom.tecrep.equipments.importer.data.GenericEquipmentImportMappers;
import mc.monacotelecom.tecrep.equipments.importer.data.GenericEquipmentImportMappers.PlmnMapper;
import mc.monacotelecom.tecrep.equipments.importer.data.GenericEquipmentImportMappers.ProviderMapper;
import mc.monacotelecom.tecrep.equipments.importer.data.GenericEquipmentImportMappers.WarehouseMapper;
import mc.monacotelecom.tecrep.equipments.mapper.FileConfigurationMapper;
import mc.monacotelecom.tecrep.equipments.mapper.InventoryPoolMapper;
import mc.monacotelecom.tecrep.equipments.mapper.SimCardGenerationConfigurationMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
//independentAncillaryEquipmentImporter
//import mc.monacotelecom.tecrep.equipments.importer.implementations.independentancillary.IndependentAncillaryEquipmentImporter;

@Slf4j
@Component
@EqualsAndHashCode(callSuper = true)
public class EqmImportStrategyFactory extends CommonImportStrategyFactory {
    private final ProviderMapper providerMapper;
    private final WarehouseMapper warehouseMapper;
    private final ConversionService conversionService;
    private final PlmnMapper plmnMapper;
    private final InventoryPoolMapper inventoryPoolMapper;
    private final GenericEquipmentImportMappers.SimCardGemaltoMapper simCardGemaltoMapper;
    private final SimCardGenerationConfigurationMapper simCardGenerationConfigurationMapper;
    private final FileConfigurationMapper fileConfigurationMapper;
    private final ChangeWarehouseOrStatusStrategy changeWarehouseOrStatusStrategy;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    private static final String ALL_SPACE_DELIMITER = "(?<= ) (?! )|(?<! ) (?= )|(?<! ) (?! )";

    private static final BiConsumer<Map<String, String>, List<String>> extractHeadersGomo = (headers, temp) -> {
        if (temp.size() == 2 && temp.get(0).contains(":")) {
            headers.put(temp.get(0).replace(":", ""), temp.get(1));
        }
    };


    public EqmImportStrategyFactory(final ProviderMapper providerMapper,
                                    final WarehouseMapper warehouseMapper,
                                    final @Qualifier("mvcConversionService") ConversionService conversionService,
                                    final PlmnMapper plmnMapper,
                                    final InventoryPoolMapper inventoryPoolMapper,
                                    final GenericEquipmentImportMappers.SimCardGemaltoMapper simCardGemaltoMapper,
                                    final SimCardGenerationConfigurationMapper simCardGenerationConfigurationMapper,
                                    final FileConfigurationMapper fileConfigurationMapper,
                                    final ChangeWarehouseOrStatusStrategy changeWarehouseOrStatusStrategy,
                                    final LocalizedMessageBuilder localizedMessageBuilder) {
        this.providerMapper = providerMapper;
        this.warehouseMapper = warehouseMapper;
        this.conversionService = conversionService;
        this.plmnMapper = plmnMapper;
        this.inventoryPoolMapper = inventoryPoolMapper;
        this.simCardGemaltoMapper = simCardGemaltoMapper;
        this.simCardGenerationConfigurationMapper = simCardGenerationConfigurationMapper;
        this.fileConfigurationMapper = fileConfigurationMapper;
        this.changeWarehouseOrStatusStrategy = changeWarehouseOrStatusStrategy;
        this.localizedMessageBuilder = localizedMessageBuilder;
    }

    @PostConstruct
    void setup() {
        var strategies = new ArrayList<ImportStrategy<?, ?>>();

        strategies.add(new CsvImportStrategyBuilder<Plmn, GenericEquipementCsvLines.PlmnCsvLine>() {
        }.withConversionService(conversionService).withRowMapper(plmnMapper).withPostProcessCondition(ImportStrategy.PostProcessCondition.ON_EACH_LINE).build());

        strategies.add(new CsvImportStrategyBuilder<Provider, GenericEquipementCsvLines.ProviderCsvLine>() {
        }.withConversionService(conversionService).withRowMapper(providerMapper).withPostProcessCondition(ImportStrategy.PostProcessCondition.ON_EACH_LINE).build());

        strategies.add(new CsvImportStrategyBuilder<Warehouse, GenericEquipementCsvLines.WarehouseCsvLine>() {
        }.withConversionService(conversionService).withRowMapper(warehouseMapper).withPostProcessCondition(ImportStrategy.PostProcessCondition.ON_EACH_LINE).build());

        strategies.add(new CsvImportStrategyBuilder<IEntity, GenericEquipementCsvLines.SimCardDeliveryFileLine>() {
        }.withConversionService(conversionService)
                .withPostProcessCondition(ImportStrategy.PostProcessCondition.ON_EACH_LINE)
                .withDelimiterPattern(ALL_SPACE_DELIMITER)
                .skipLines(0)
                .build());

        strategies.add(new CsvImportStrategyBuilder<SimCard, GenericEquipementCsvLines.SimCardGemaltoLine>() {
        }.withConversionService(conversionService)
                .withRowMapper(simCardGemaltoMapper)
                .withPostProcessCondition(ImportStrategy.PostProcessCondition.ON_EACH_LINE)
                .withDelimiterPattern(ALL_SPACE_DELIMITER)
                .skipLines(16)
                .build());

        strategies.add(new CsvImportStrategyBuilder<SimCard, GenericEquipementCsvLines.SimCardIdemiaLine>() {
        }.withConversionService(conversionService)
                .withPostProcessCondition(ImportStrategy.PostProcessCondition.ON_EACH_LINE)
                .withDelimiterPattern(ALL_SPACE_DELIMITER)
                .skipLines(27)
                .build());

        strategies.add(new CsvImportStrategyBuilder<SimCard, GenericEquipementCsvLines.SimCardGomoLine>() {
        }.withConversionService(conversionService)
                .withPostProcessCondition(ImportStrategy.PostProcessCondition.ON_EACH_LINE)
                .withDelimiterPattern(ALL_SPACE_DELIMITER)
                .withExtractHeaders(extractHeadersGomo)
                .skipLines(34)
                .build());


        strategies.add(new CsvImportStrategyBuilder<CPE, GenericEquipementCsvLines.CPEFTTHCsvLine>() {
        }.withConversionService(conversionService).withPostProcessCondition(ImportStrategy.PostProcessCondition.ON_EACH_LINE).build());

        strategies.add(new CsvImportStrategyBuilder<CPE, GenericEquipementCsvLines.CpeDOCSISCsvLine>() {
        }.withConversionService(conversionService).withPostProcessCondition(ImportStrategy.PostProcessCondition.ON_EACH_LINE).build());

        strategies.add(new CsvImportStrategyBuilder<SimCardGenerationConfiguration, GenericEquipementCsvLines.SimCardGenerationConfigurationCsvLine>() {
        }.withConversionService(conversionService).withRowMapper(simCardGenerationConfigurationMapper).withPostProcessCondition(ImportStrategy.PostProcessCondition.ON_EACH_LINE).build());

        strategies.add(new CsvImportStrategyBuilder<InventoryPool, GenericEquipementCsvLines.InventoryPoolCsvLine>() {
        }.withConversionService(conversionService)
                .withRowMapper(inventoryPoolMapper)
                .withPostProcessCondition(ImportStrategy.PostProcessCondition.ON_EACH_LINE)
                .build());

        strategies.add(new CsvImportStrategyBuilder<FileConfiguration, GenericEquipementCsvLines.FileConfigurationCsvLine>() {
        }.withConversionService(conversionService).withRowMapper(fileConfigurationMapper).withPostProcessCondition(ImportStrategy.PostProcessCondition.ON_EACH_LINE).build());

        strategies.add(new CsvImportStrategyBuilder<AncillaryEquipment, GenericEquipementCsvLines.AncillaryEquipmentCsvLine>() {
        }.withConversionService(conversionService)
                .withPostProcessCondition(ImportStrategy.PostProcessCondition.ON_EACH_LINE)
                .skipLines(1)
                .build());

        strategies.add(new CsvImportStrategyBuilder<AncillaryEquipment, GenericEquipementCsvLines.AncillaryEquipmentHDDSagemcomsvLine>() {
        }.withConversionService(conversionService)
                .withPostProcessCondition(ImportStrategy.PostProcessCondition.ON_EACH_LINE)
                .build());

        strategies.add(new CsvImportStrategyBuilder<AncillaryEquipment, GenericEquipementCsvLines.AncillaryEquipmentSTBCsvLine>() {
        }.withConversionService(conversionService)
                .withPostProcessCondition(ImportStrategy.PostProcessCondition.ON_EACH_LINE)
                .build());

        strategies.add(new CsvImportStrategyBuilder<AncillaryEquipment, GenericEquipementCsvLines.AncillaryONTGenexisCsvLine>() {
        }.withConversionService(conversionService)
                .skipLines(1)
                .withPostProcessCondition(ImportStrategy.PostProcessCondition.ON_EACH_LINE)
                .build());


                
        strategies.add(new CsvImportStrategyBuilder<AncillaryEquipment, GenericEquipementCsvLines.CustomFormatAncillaryCsvLine>() {
        }.withConversionService(conversionService)
                .skipLines(1)
                .withPostProcessCondition(ImportStrategy.PostProcessCondition.ON_EACH_LINE)
                .build());
   // strategies.put("INDEPENDENT_ANCILLARY_IMPORT", independentAncillaryEquipmentImporter);


        strategies.add(changeWarehouseOrStatusStrategy);

        this.setStrategies(strategies);
    }
}
