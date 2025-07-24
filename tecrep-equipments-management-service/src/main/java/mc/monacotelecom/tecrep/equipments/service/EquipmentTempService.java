package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.ImportResultDTO;
import mc.monacotelecom.tecrep.equipments.dto.SendValidEquipmentsResponseDTO;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentTemp;
import mc.monacotelecom.tecrep.equipments.process.EquipmentTempProcess;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentModelRepository;
import mc.monacotelecom.tecrep.equipments.service.IAncillaryService;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import mc.monacotelecom.tecrep.equipments.utils.ByteArrayMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class EquipmentTempService {

    private final EquipmentTempProcess equipmentTempProcess;
    private final IAncillaryService ancillaryService;
    private final EquipmentModelRepository equipmentModelRepository;

    @Value("${ancillary.import.default-warehouse-id}")
    private Long defaultWarehouseId;

    public Long upload(MultipartFile file, Long modelId, String email, String sessionId) {
        return equipmentTempProcess.importFile(file, modelId, email, sessionId);
    }

    @Transactional
    public EquipmentTemp getByBoxSn(String boxSn) {
        return equipmentTempProcess.getByBoxSn(boxSn);
    }

    @Transactional
    public EquipmentTemp unscanByBoxSn(String boxSn) {
        return equipmentTempProcess.unscanByBoxSn(boxSn);
    }

    @Transactional(readOnly = true)
    public List<EquipmentTemp> getPendingByOrderUploadId(Long orderUploadId) {
        return equipmentTempProcess.getPendingByOrderUploadId(orderUploadId);
    }

    @Transactional(readOnly = true)
    public List<EquipmentTemp> getPendingByEmail(String email) {
        return equipmentTempProcess.getPendingByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<EquipmentTemp> getCompletedByEmail(String email) {
        return equipmentTempProcess.getCompletedByEmail(email);
    }
    
    @Transactional
    public SendValidEquipmentsResponseDTO sendValidEquipments(List<Long> ids) {
        SendValidEquipmentsResponseDTO response = new SendValidEquipmentsResponseDTO();

        boolean sapResponseOk = true; // Simulation of SAP integration

        if (sapResponseOk) {
            List<EquipmentTemp> equipments = equipmentTempProcess.findAllByIds(ids);

            StringBuilder sb = new StringBuilder();
            sb.append("BOX_SN,POD_SN,NODE_MODEL\n");
            for (EquipmentTemp eq : equipments) {
                String modelName = equipmentModelRepository.findById(eq.getModelId())
                        .map(m -> m.getName())
                        .orElse("");
                sb.append(eq.getBoxSn()).append(',')
                  .append(eq.getPodSn()).append(',')
                  .append(modelName).append('\n');
            }

            ByteArrayMultipartFile file = new ByteArrayMultipartFile(
                    "file",
                    "valid_equipments_upload_tecrep.csv",
                    "text/csv",
                    sb.toString().getBytes(StandardCharsets.UTF_8)
            );

            ImportResultDTO scheduleResult = ancillaryService.scheduleImportJob(file, "CUSTOM_ANCILLARY", true);
            
            if (scheduleResult != null && scheduleResult.getJobId() != null && !"FAILED".equalsIgnoreCase(scheduleResult.getStatus())) {

                equipmentTempProcess.updateStatusCompleted(ids);
                equipmentTempProcess.updateValidEquipmentsInfo(ids, scheduleResult.getJobId(), LocalDateTime.now());
                
                response.setJobId(scheduleResult.getJobId());
                response.setStatus(scheduleResult.getStatus());
                response.setMessage("Registro exitoso");

            } else {
                response.setMessage("Error en la creación del job");
            }

        } else {
            
            response.setMessage("Error en el envio");
        }

        return response;
    }
    
}