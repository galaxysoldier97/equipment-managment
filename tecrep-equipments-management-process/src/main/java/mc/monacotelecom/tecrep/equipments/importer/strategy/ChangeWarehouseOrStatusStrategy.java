package mc.monacotelecom.tecrep.equipments.importer.strategy;

import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.importer.ImportStrategy;
import mc.monacotelecom.inventory.common.importer.process.strategy.StrategyWithoutMandatoryFile;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.entity.Equipment;
import mc.monacotelecom.tecrep.equipments.entity.Warehouse;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.importer.entities.ChangeWarehouseOrStatus;
import mc.monacotelecom.tecrep.equipments.importer.implementations.context.ChangeWarehouseOrStatusContext;
import mc.monacotelecom.tecrep.equipments.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.IMPORT_UNSUPPORTED_CATEGORY;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.WAREHOUSE_NAME_NOT_FOUND;

@Slf4j
@Component
public class ChangeWarehouseOrStatusStrategy extends ImportStrategy<ChangeWarehouseOrStatus, ChangeWarehouseOrStatusLine> implements StrategyWithoutMandatoryFile {
    private final SimCardRepository simCardRepository;
    private final CPERepository cpeRepository;
    private final EquipmentRepository<?> equipmentRepository;
    private final AncillaryRepository ancillaryRepository;
    private final WarehouseRepository warehouseRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    public ChangeWarehouseOrStatusStrategy(final SimCardRepository simCardRepository,
                                           final CPERepository cpeRepository,
                                           final EquipmentRepository<?> equipmentRepository,
                                           final AncillaryRepository ancillaryRepository,
                                           final WarehouseRepository warehouseRepository,
                                           final LocalizedMessageBuilder localizedMessageBuilder) {
        this.simCardRepository = simCardRepository;
        this.cpeRepository = cpeRepository;
        this.equipmentRepository = equipmentRepository;
        this.ancillaryRepository = ancillaryRepository;
        this.warehouseRepository = warehouseRepository;
        this.localizedMessageBuilder = localizedMessageBuilder;
    }

    @Override
    public Class<ChangeWarehouseOrStatus> getNodeType() {
        return ChangeWarehouseOrStatus.class;
    }

    @Override
    public Class<ChangeWarehouseOrStatusLine> getLineType() {
        return ChangeWarehouseOrStatusLine.class;
    }

    @Override
    public void parse() {
        Stream<ChangeWarehouseOrStatusLine> inputStream = this.getLines();
        Collection<ChangeWarehouseOrStatusLine> processedLines = new ArrayList<>();
        inputStream.forEach(line -> {
            try {
                lineProcessors.descendingIterator().forEachRemaining(processor -> processor.accept(line));
                processedLines.add(line);
            } catch (RuntimeException e) {
                line.setMessage(e.getMessage());
                line.setSeverity(Level.ERROR);
                line.setNodes(Collections.emptyList());
                lineProcessors.descendingIterator().forEachRemaining(processor -> processor.accept(line));
                processedLines.add(line);
            }
        });
        this.setResults(processedLines);
    }

    public Stream<ChangeWarehouseOrStatusLine> getLines() {
        var changeWarehouseOrStatusContext = new ChangeWarehouseOrStatusContext(this.importParameters, localizedMessageBuilder);

        final Collection<Equipment> equipments = new ArrayList<>();
        switch (changeWarehouseOrStatusContext.getSelection()) {
            case BATCH:
                if (EquipmentCategory.CPE.equals(changeWarehouseOrStatusContext.getCategory())) {
                    equipments.addAll(cpeRepository.findByBatchNumber(changeWarehouseOrStatusContext.getBatchNumber()));
                } else if (EquipmentCategory.SIMCARD.equals(changeWarehouseOrStatusContext.getCategory())) {
                    equipments.addAll(simCardRepository.findByBatchNumber(changeWarehouseOrStatusContext.getBatchNumber()));
                } else if (EquipmentCategory.ANCILLARY.equals(changeWarehouseOrStatusContext.getCategory())) {
                    equipments.addAll(ancillaryRepository.findByBatchNumber(changeWarehouseOrStatusContext.getBatchNumber()));
                } else {
                    equipments.addAll(equipmentRepository.findByBatchNumber(changeWarehouseOrStatusContext.getBatchNumber()));
                }
                break;
            case IMSI:
                if (EquipmentCategory.SIMCARD.equals(changeWarehouseOrStatusContext.getCategory())) {
                    equipments.addAll(simCardRepository.findByRange(Long.parseLong(changeWarehouseOrStatusContext.getStartRange()), Long.parseLong(changeWarehouseOrStatusContext.getFinalRange())));
                } else {
                    throw new EqmValidationException(localizedMessageBuilder, IMPORT_UNSUPPORTED_CATEGORY, changeWarehouseOrStatusContext.getSelection(), changeWarehouseOrStatusContext.getCategory());
                }
                break;
            case SERIAL_NUMBER:
                if (EquipmentCategory.CPE.equals(changeWarehouseOrStatusContext.getCategory())) {
                    equipments.addAll(cpeRepository.findBySerialNumberLessThanEqualAndSerialNumberGreaterThanEqual(changeWarehouseOrStatusContext.getFinalRange(), changeWarehouseOrStatusContext.getStartRange()));
                } else if (EquipmentCategory.SIMCARD.equals(changeWarehouseOrStatusContext.getCategory())) {
                    equipments.addAll(simCardRepository.findBySerialNumberLessThanEqualAndSerialNumberGreaterThanEqual(changeWarehouseOrStatusContext.getFinalRange(), changeWarehouseOrStatusContext.getStartRange()));
                } else if (EquipmentCategory.ANCILLARY.equals(changeWarehouseOrStatusContext.getCategory())) {
                    equipments.addAll(ancillaryRepository.findBySerialNumberLessThanEqualAndSerialNumberGreaterThanEqual(changeWarehouseOrStatusContext.getFinalRange(), changeWarehouseOrStatusContext.getStartRange()));
                } else {
                    equipments.addAll(equipmentRepository.findBySerialNumberLessThanEqualAndSerialNumberGreaterThanEqual(changeWarehouseOrStatusContext.getFinalRange(), changeWarehouseOrStatusContext.getStartRange()));
                }
                break;
            case FILE:
                InputStream inputStream = getInputStream();

                final var br = new BufferedReader(new InputStreamReader(inputStream));
                List<String> serialNumberList = br.lines().collect(Collectors.toList());

                serialNumberList.forEach(serialNumber -> {
                    Optional<? extends Equipment> equipmentFound;
                    if (StringUtils.isNotBlank(serialNumber)) {
                        if (EquipmentCategory.CPE.equals(changeWarehouseOrStatusContext.getCategory())) {
                            equipmentFound = cpeRepository.findByCategoryAndSerialNumber(changeWarehouseOrStatusContext.getCategory(), serialNumber);
                        } else if (EquipmentCategory.ANCILLARY.equals(changeWarehouseOrStatusContext.getCategory())) {
                            equipmentFound = ancillaryRepository.findByCategoryAndSerialNumber(changeWarehouseOrStatusContext.getCategory(), serialNumber);
                        } else {
                            throw new EqmValidationException(localizedMessageBuilder, IMPORT_UNSUPPORTED_CATEGORY, changeWarehouseOrStatusContext.getSelection(), changeWarehouseOrStatusContext.getCategory());
                        }

                        equipmentFound.ifPresentOrElse(
                                equipments::add,
                                () -> log.warn(String.format("Could not find %s with serial number %s", changeWarehouseOrStatusContext.getCategory(), serialNumber))
                        );
                    }
                });
                break;
        }

        Warehouse warehouse = null;
        final var warehouseNameOpt = changeWarehouseOrStatusContext.getName();
        if (warehouseNameOpt.isPresent()) {
            warehouse = warehouseRepository.findByName(warehouseNameOpt.get())
                    .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, WAREHOUSE_NAME_NOT_FOUND, changeWarehouseOrStatusContext.getName()));
        }

        Collection<ChangeWarehouseOrStatusLine> mappedLines = new ArrayList<>();
        final Warehouse finalWarehouse = warehouse;
        equipments.forEach(equipment -> {
            var dto = new ChangeWarehouseOrStatus();
            dto.setEquipment(equipment);
            dto.setWarehouse(finalWarehouse);
            changeWarehouseOrStatusContext.getEvent().ifPresent(dto::setEvent);
            mappedLines.add(new ChangeWarehouseOrStatusLine(Collections.singletonList(dto)));
        });

        return mappedLines.stream();
    }
}
