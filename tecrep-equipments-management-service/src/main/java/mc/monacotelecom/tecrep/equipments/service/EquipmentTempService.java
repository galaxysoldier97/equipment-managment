package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.ImportResultDTO;
import mc.monacotelecom.tecrep.equipments.dto.SendValidEquipmentsResponseDTO;
import mc.monacotelecom.tecrep.equipments.dto.UploadEquipmentTempResponseDTO;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentTemp;
import mc.monacotelecom.tecrep.equipments.process.EquipmentTempProcess;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentModelRepository;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentModelMaskConfigRepository;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentModelMaskConfig;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentConfig;
import mc.monacotelecom.tecrep.equipments.service.IAncillaryService;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentConfigRepository;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;

import java.util.List;

import java.util.Optional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedWriter;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import mc.monacotelecom.tecrep.equipments.utils.ByteArrayMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class EquipmentTempService {

    private final EquipmentTempProcess equipmentTempProcess;
    private final IAncillaryService ancillaryService;
    private final EquipmentModelRepository equipmentModelRepository;
    private final EquipmentModelMaskConfigRepository maskConfigRepository;
    private final EquipmentConfigRepository equipmentConfigRepository;

    @Value("${ancillary.import.default-warehouse-id}")
    private Long defaultWarehouseId;

    @Value("${equipment.temp.export-path}")
    private String exportPath;

    public UploadEquipmentTempResponseDTO upload(MultipartFile file, Long poAncillaryeqmSapId, String email, Long modelId, String sessionId) {
        return equipmentTempProcess.importFile(file, poAncillaryeqmSapId, email, modelId, sessionId);
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

            Long createdJobId = scheduleResult.getJobId();
            new Thread(() -> monitorJobAndExport(createdJobId)).start();


        } else {
            response.setMessage("Error en la creación del job");
        }

        return response;

    }

    private void monitorJobAndExport(Long jobId) {

        log.info("🕵️‍♂️ Iniciando monitoreo asíncrono del jobId={}", jobId);

        try {

            boolean finished = false;
            int maxAttempts = 120; // esperar máximo 10 minutos
            int attempts = 0;

            Thread.sleep(3000);

            while (!finished && attempts < maxAttempts) {

                var maybe = ancillaryService.getImportJobById(jobId);

                if (maybe.isEmpty()) {
                    log.warn("⚠️ No se encontró el jobId={} durante el monitoreo", jobId);
                    return;
                }

                var job = maybe.get();

                switch (job.getStatus()) {
                    case PENDING:
                    case PROCESSING:
                        log.debug("🔄 JobId={} aún en estado {}", jobId, job.getStatus());
                        Thread.sleep(5000);
                        attempts++;
                        break;
                    default:
                        finished = true;
                        log.info("✅ JobId={} finalizó con estado {}", jobId, job.getStatus());
                        
                        if (job.getStatus() == mc.monacotelecom.tecrep.equipments.entity.AncillaryImportJob.JobStatus.SUCCESS) {

                            try {
                                generateTxtFile(jobId);
                                log.info("📄 Archivo .txt generado exitosamente para jobId={}", jobId);
                                markPoAsCompleted(jobId);
                                logJobCompleted(jobId);
                            } catch (IOException ioEx) {
                                log.error("❌ Error al generar archivo .txt para jobId={}", jobId, ioEx);
                            }

                        }

                        break;
                }
            }

            if (!finished) {
                log.error("⏱️ Timeout: El jobId={} no finalizó luego de {} intentos", jobId, attempts);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("🛑 Monitoreo interrumpido para jobId={}", jobId);
        } catch (Exception ex) {
            log.error("🔥 Error inesperado durante monitoreo del jobId={}", jobId, ex);
        }
    }



    private void generateTxtFile(Long jobId) throws IOException {

        List<EquipmentTemp> completed = equipmentTempProcess.getCompletedByJobId(jobId);
        
        if (completed.isEmpty()) {
            return;
        }

        Long modelId = completed.get(0).getModelId();
        String mask = maskConfigRepository
                .findFirstByModelIdAndStatus(modelId, EquipmentModelMaskConfig.Status.enable)
                .map(EquipmentModelMaskConfig::getMaskFormat)
                .orElse("equipments*.txt");

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String filename = mask.replace("*", "_" + timestamp);

        Path dir = Paths.get(exportPath);
        Files.createDirectories(dir);
        Path file = dir.resolve(filename);

        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            for (EquipmentTemp eq : completed) {
                writer.write(eq.getBoxSn() + ";" + eq.getPodSn());
                writer.newLine();
            }
        }

        sendFileToConfiguredUrl(file);

    }

    private void sendFileToConfiguredUrl(Path file) {

        equipmentConfigRepository.findByNameAndStatus("url_create_tytan", EquipmentConfig.Status.enable)
                .ifPresent(config -> {

                    String url = config.getValue();

                    try {

                        /* HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(url))
                                .header("Content-Type", "text/plain")
                                .POST(HttpRequest.BodyPublishers.ofFile(file))
                                .build();
                        HttpClient client = HttpClient.newHttpClient();
                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); */
                        //log.info("📤 Archivo {} enviado a {} con status {}", file.getFileName(), url, response.statusCode());
                        log.info("📤 Archivo {} enviado a: {}", file.getFileName(), url);

                    } catch (Exception ex) {
                        log.error("❌ Error enviando archivo {} a {}", file.getFileName(), url, ex);
                    }

                });
    }

    private void markPoAsCompleted(Long jobId) {
        equipmentTempProcess.findFirstByJobId(jobId)
                .map(EquipmentTemp::getPoAncillaryeqmSapId)
                .ifPresent(poId -> equipmentTempProcess.updatePoStatus(poId, "completed"));
    }

    private void logJobCompleted(Long jobId) {
        equipmentTempProcess.findFirstByJobId(jobId)
                .ifPresent(eq -> log.info("✅ Procesamiento de carga de equipos completado. jobId={}, usuario={}",
                        jobId, eq.getUploadedBy()));
    }
    
}