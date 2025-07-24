package mc.monacotelecom.tecrep.equipments.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentTemp;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmInternalException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentTempRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

import org.apache.commons.io.input.BOMInputStream;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.BATCH_UPLOAD_IMPORT_FAILURE;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.EQUIPMENT_TEMP_PO_NOT_FOUND;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.EQUIPMENT_TEMP_BOX_SN_NOT_FOUND;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.EQUIPMENT_TEMP_PENDING_NOT_FOUND;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.EQUIPMENT_TEMP_PENDING_EMAIL_NOT_FOUND;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.EQUIPMENT_TEMP_COMPLETED_EMAIL_NOT_FOUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class EquipmentTempProcess {

    private final EquipmentTempRepository equipmentTempRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    @Transactional
    public Long importFile(MultipartFile file, Long modelId, String email, String sessionId) {
        Long orderUploadId = Optional.ofNullable(
                equipmentTempRepository.findFirstByOrderByOrderUploadIdDesc())
                .map(EquipmentTemp::getOrderUploadId)
                .orElse(0L) + 1;
        try (Reader reader = new InputStreamReader(
                new BOMInputStream(file.getInputStream()), StandardCharsets.UTF_8)) {
            CSVParser parser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim()
                    .parse(reader);
            for (CSVRecord record : parser) {
                EquipmentTemp temp = new EquipmentTemp();
                temp.setPoNo(record.get("po_no"));
                temp.setPartNo(record.get("part_no"));
                temp.setBoxSn(record.get("box_sn"));
                temp.setPodSn(record.get("pod_sn"));
                temp.setModelId(modelId);
                temp.setUploadedBy(email);
                temp.setSessionId(sessionId);
                temp.setOrderUploadId(orderUploadId);
                temp.setCreatedAt(LocalDateTime.now());
                temp.setStatus("pending");
                equipmentTempRepository.save(temp);
            }
        } catch (Exception e) {
            log.error("Failed to import equipments temp file", e);
            throw new EqmInternalException(e, localizedMessageBuilder, BATCH_UPLOAD_IMPORT_FAILURE);
        }
        return orderUploadId;
    }

    public EquipmentTemp getByBoxSn(String boxSn) {
        EquipmentTemp equipmentTemp = equipmentTempRepository.findByBoxSn(boxSn)
                .filter(eq -> "pending".equalsIgnoreCase(eq.getStatus()))
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, EQUIPMENT_TEMP_BOX_SN_NOT_FOUND, boxSn));
        
        equipmentTemp.setScanned(true);
        equipmentTemp.setScannedAt(LocalDateTime.now());
        return equipmentTempRepository.save(equipmentTemp);
    }

    @Transactional
    public EquipmentTemp unscanByBoxSn(String boxSn) {
        EquipmentTemp equipmentTemp = equipmentTempRepository.findByBoxSn(boxSn)
                .filter(eq -> "pending".equalsIgnoreCase(eq.getStatus()))
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, EQUIPMENT_TEMP_BOX_SN_NOT_FOUND, boxSn));

        equipmentTemp.setScanned(false);
        equipmentTemp.setScannedAt(null);
        return equipmentTempRepository.save(equipmentTemp);
    }

    @Transactional(readOnly = true)
    public List<EquipmentTemp> getPendingByOrderUploadId(Long orderUploadId) {
        List<EquipmentTemp> equipments = equipmentTempRepository
                .findAllByStatusAndOrderUploadId("pending", orderUploadId);
        if (equipments.isEmpty()) {
            throw new EqmNotFoundException(localizedMessageBuilder, EQUIPMENT_TEMP_PENDING_NOT_FOUND, orderUploadId);
        }
        return equipments;
    }

    @Transactional(readOnly = true)
    public List<EquipmentTemp> getPendingByEmail(String email) {
        List<EquipmentTemp> equipments = equipmentTempRepository
                .findAllByStatusAndUploadedBy("pending", email);
        if (equipments.isEmpty()) {
            throw new EqmNotFoundException(localizedMessageBuilder, EQUIPMENT_TEMP_PENDING_EMAIL_NOT_FOUND, email);
        }
        return equipments;
    }

    @Transactional(readOnly = true)
    public List<EquipmentTemp> getCompletedByEmail(String email) {
        List<EquipmentTemp> equipments = equipmentTempRepository
                .findAllByStatusAndUploadedBy("completed", email);
        if (equipments.isEmpty()) {
            throw new EqmNotFoundException(localizedMessageBuilder, EQUIPMENT_TEMP_COMPLETED_EMAIL_NOT_FOUND, email);
        }
        return equipments;
    }

    @Transactional(readOnly = true)
    public List<EquipmentTemp> findAllByIds(List<Long> ids) {
        return equipmentTempRepository.findAllById(ids);
    }

    @Transactional
    public void updateStatusCompleted(List<Long> ids) {
        equipmentTempRepository.updateStatusByIds("completed", ids);
    }

    @Transactional
    public void updateValidEquipmentsInfo(List<Long> ids, Long jobId, LocalDateTime processDate) {
        equipmentTempRepository.updateValidEquipmentsInfo(jobId, processDate, ids);
    }
}