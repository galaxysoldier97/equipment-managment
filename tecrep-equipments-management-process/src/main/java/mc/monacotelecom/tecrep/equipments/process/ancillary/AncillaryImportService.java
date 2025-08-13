package mc.monacotelecom.tecrep.equipments.process.ancillary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.dto.ImportResultDTO;
import mc.monacotelecom.tecrep.equipments.dto.LineErrorDTO;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryImportError;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.importer.implementations.ancillary.IAncillaryImporter;
import mc.monacotelecom.tecrep.equipments.repository.AncillaryRepository;
import mc.monacotelecom.tecrep.equipments.repository.AncillaryImportErrorRepository;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentModelRepository;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory;
import mc.monacotelecom.tecrep.equipments.repository.WarehouseRepository;
import mc.monacotelecom.tecrep.equipments.util.PgpDecryptUtil;
import org.bouncycastle.openpgp.PGPException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.repository.AncillaryImportJobRepository;
import mc.monacotelecom.tecrep.equipments.repository.PoAncillaryEquipmentSapRepository;
import mc.monacotelecom.tecrep.equipments.entity.PoAncillaryEquipmentSap;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentTempRepository;
import mc.monacotelecom.tecrep.equipments.repository.HomologacionMaterialSapRepository;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentTemp;
import mc.monacotelecom.tecrep.equipments.entity.HomologacionMaterialSap;
import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.UUID;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryImportJob;
import mc.monacotelecom.tecrep.equipments.factory.AncillaryEquipmentFactory;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.TransactionDefinition;


//orElseThrow
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;

 

@Slf4j
@Service
@RequiredArgsConstructor
public class AncillaryImportService {

    private final AncillaryRepository ancillaryRepository;
    private final EquipmentModelRepository equipmentModelRepository;
    private final WarehouseRepository warehouseRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final Map<String, IAncillaryImporter> ancillaryImporters;
    private final AncillaryImportErrorRepository importErrorRepo; // repo para errores
    private final AncillaryEquipmentFactory equipmentFactory;

    private final AncillaryImportJobRepository jobRepository;
    private final PoAncillaryEquipmentSapRepository poAncillaryEquipmentSapRepository;
    private final EquipmentTempRepository equipmentTempRepository;
    private final HomologacionMaterialSapRepository homologacionMaterialSapRepository;
    private final PlatformTransactionManager txManager;

    private TransactionTemplate newTxTemplate() {
        TransactionTemplate tt = new TransactionTemplate(txManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return tt;
    }
     

    @Value("${ancillary.import.result-base-path}")
    private String resultBasePath;

    @Value("${ancillary.import.default-warehouse-id}")
    private Long defaultWarehouseId;

        /** NUEVO: Para ejecutar jobs en segundo plano **/
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    @PostConstruct
    public void ensureResultDirExists() throws IOException {
        Files.createDirectories(Path.of(resultBasePath));
    }

    /**
     * Validates equipment serial number and MAC address for duplicates.
     * Extracted from IAncillaryImporter.checkAncillaryExist() logic.
     */
    private void validateEquipment(String serial, String mac) {
        // Use same validation as IAncillaryImporter.checkAncillaryExist()
        ancillaryRepository.findByCategoryAndSerialNumber(
            EquipmentCategory.ANCILLARY, 
            serial
        ).ifPresent(id -> {
            throw new EqmValidationException(localizedMessageBuilder, CPE_CATEGORY_SERIAL_NUMBER_DUPLICATED, EquipmentCategory.ANCILLARY, serial);
        });
        
        // Add MAC validation
        if (ancillaryRepository.existsByMacAddress(mac)) {
            throw new EqmValidationException(localizedMessageBuilder, EQUIPMENT_MAC_ADDRESS_ALREADY_IN_USE, mac);
        }
    }

    @Transactional
    public Long scheduleImportJob(MultipartFile file, String format, boolean continueOnError) {
        // 1) Validar “format” rápido: si no hay importador registrado, podemos fallar inmediatamente
        IAncillaryImporter importer = getImporterForFormat(format);
        if (importer == null) {
            throw new EqmValidationException(localizedMessageBuilder, "IMPORT_FORMAT_NOT_SUPPORTED", format);
        }

        // 1.1) Si el importador  viene con el format = SAP_ANCILLARY_TEMP
        Optional<Long> jobIdIfHandled = handleSapAncillaryTempFormat(file, format, continueOnError);
        if (jobIdIfHandled.isPresent()) {

            return jobIdIfHandled.get();
        } 

        // 2) Crear el registro de job con estado PENDING
        AncillaryImportJob job = new AncillaryImportJob();
        job.setOriginalFilename(file.getOriginalFilename());
        job.setFormat(format);
        job.setContinueOnError(continueOnError);
        job.setStatus(AncillaryImportJob.JobStatus.PENDING);
        job.setTotalLines(0);
        job.setSuccessfulLines(0);
        job.setErrorCount(0);
        job.setStartedAt(LocalDateTime.now());

        // 3) Guardar el archivo en disco (en un directorio persistente)
        String inputDir = resultBasePath + "/inputs"; // por ejemplo, dentro de resultBasePath puedes tener una subcarpeta “inputs”
        try {
            Files.createDirectories(Path.of(inputDir));
        } catch (IOException e) {
            throw new EqmValidationException(localizedMessageBuilder, "IMPORT_IO_ERROR", "No puede crear directorio de inputs: " + e.getMessage());
        }
        String savedName = "job-" + job.getId() + "_" + file.getOriginalFilename();
        Path destination = Path.of(inputDir, savedName);
        try {
            file.transferTo(destination.toFile());
        } catch (IOException e) {
            throw new EqmValidationException(localizedMessageBuilder, "IMPORT_IO_ERROR", "No puede guardar CSV de entrada: " + e.getMessage());
        }
        job.setInputFilePath(destination.toString());

        // 4) Persiste el registro del job
        job = jobRepository.save(job);

        // 5) Arrancar el procesamiento en background
        //launchImportJobAsync(job.getId());
        // 5) Arrancar el procesamiento en background solo después del commit
        Long jobId = job.getId();
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCommit() {
                        launchImportJobAsync(jobId);
                    }
                }
        );

        // 6) Devolver el ID del job para que el cliente consulte estado
        //return job.getId();
        return jobId;
    }
    
    public void launchImportJobAsync(Long jobId) {
    executor.submit(() -> executeImportJob(jobId));
    }

    @Transactional
public void executeImportJob(Long jobId) {
    Optional<AncillaryImportJob> maybe = jobRepository.findById(jobId);
    if (maybe.isEmpty()) {
        log.error("No se encontró job con ID {}", jobId);
        return;
    }
    AncillaryImportJob job = maybe.get();

//trabajar aqui ..

    // 1) Marcar como PROCESSING
    job.setStatus(AncillaryImportJob.JobStatus.PROCESSING);
    job.setStartedAt(LocalDateTime.now());
    jobRepository.save(job);

    int totalLines = 0;
    int successfulLines = 0;
    List<AncillaryImportError> errors = new ArrayList<>();
    List<String[]> successfulRows = new ArrayList<>();

    Path inputPath = Path.of(job.getInputFilePath());
    try (
        Reader reader = Files.newBufferedReader(inputPath);
        CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withTrim())
    ) {
        // 2) Verificar cabeceras
        Map<String, Integer> headerMap = parser.getHeaderMap();
        if (!headerMap.containsKey("BOX_SN") ||
            !headerMap.containsKey("POD_SN") ||
            !headerMap.containsKey("NODE_MODEL")) {
            throw new RuntimeException("Faltan columnas obligatorias en el CSV: BOX_SN y/o POD_SN y/o NODE_MODEL");
        }

        // 3) Iterar registros
        for (CSVRecord record : parser) {
            totalLines++;
            try {
                String serial    = record.get("BOX_SN").trim();
                String mac       = record.get("POD_SN").trim();
                String modelName = record.get("NODE_MODEL").trim();

                // Validación no vacío
                if (serial.isEmpty() || mac.isEmpty() || modelName.isEmpty()) {
                    throw new RuntimeException(
                        "Los campos BOX_SN, POD_SN y NODE_MODEL no pueden estar vacíos (fila " 
                        + record.getRecordNumber() + ")"
                    );
                }
                // Validar duplicados antes de salvar usando método extraído
                try {
                    validateEquipment(serial, mac);
                } catch (EqmValidationException e) {
                 
                    log.warn("Validation failed for equipment - Serial: {}, MAC: {}", serial, mac);
                    
                    AncillaryImportError errEnt = new AncillaryImportError();
                    errEnt.setJob(job);
                    errEnt.setLineNumber((int) record.getRecordNumber());
                    errEnt.setErrorMessage(e.getMessage());
                    errEnt.setOriginalLine(serial + "," + mac + "," + modelName);
                    errors.add(errEnt);
                    if (!job.isContinueOnError()) break;
                    else continue;
                }

       
                var modelOpt =  equipmentModelRepository.findByNameAndCategory(modelName, EquipmentModelCategory.ANCILLARY);
                 
                if (modelOpt.isEmpty()) {
                    AncillaryImportError errEnt = new AncillaryImportError();
                    errEnt.setJob(job);
                    errEnt.setLineNumber((int) record.getRecordNumber());
                    errEnt.setErrorMessage("Modelo no encontrado: " + modelName);
                    errEnt.setOriginalLine(serial + "," + mac + "," + modelName);
                    errors.add(errEnt);
                    if (!job.isContinueOnError()) break;
                    else continue;
                }
                var equipmentModel = modelOpt.get();
                //imprimir cantidad de modelos encontrados
               
                //si ahi mas de un modelo con ese nombre, se lanza una excepción equipmentModel
                if (equipmentModel.getCategory() != EquipmentModelCategory.ANCILLARY) {
                    throw new RuntimeException("El modelo " + modelName + " no es de categoría ANCILLARY");
                }
                
                // Buscar Warehouse por defecto
                var whOpt = warehouseRepository.findById(defaultWarehouseId);
                if (whOpt.isEmpty()) {
                    throw new RuntimeException("Almacén no encontrado con ID: " + defaultWarehouseId);
                }
                var warehouse = whOpt.get();

                // Crear AncillaryEquipment using factory
                AncillaryEquipment ancillary = equipmentFactory.createFromCsvRecord(
                    equipmentModel,
                    warehouse,
                    serial,
                    mac
                );
                //independent
                ancillary.setIndependent(Boolean.TRUE);
                ancillary.setStatus(Status.INSTORE);

                // Guardar en BD
                ancillaryRepository.save(ancillary);
                successfulLines++;
                successfulRows.add(new String[]{
                    serial, mac,
                    String.valueOf(defaultWarehouseId),
                    String.valueOf(equipmentModel.getId())
                });
                

            } catch (Exception exRow) {
                AncillaryImportError errEnt = new AncillaryImportError();
                errEnt.setJob(job);
                errEnt.setLineNumber((int) record.getRecordNumber());
                errEnt.setErrorMessage(exRow.getMessage());
                errEnt.setOriginalLine(
                    record.get("BOX_SN") + "," 
                    + record.get("POD_SN") + "," 
                    + record.get("NODE_MODEL")
                );
                errors.add(errEnt);
                if (!job.isContinueOnError()) break;
            }
        }

        // 4) Persistir errores de fila
        importErrorRepo.saveAll(errors);

        // 5) Generar CSV de resultados
        String baseName = FilenameUtils.getBaseName(job.getOriginalFilename());
        String resultFilename = baseName + "_result_" + jobId + ".csv";
        String fullPath = Path.of(resultBasePath, resultFilename).toString();
        writeCsvResult(fullPath, successfulRows, errors);
        job.setResultFilePath(fullPath);

        // 6) Actualizar conteos y marcar FINISHED
        job.setTotalLines(totalLines);
        job.setSuccessfulLines(successfulLines);
        job.setErrorCount(errors.size());
        job.setFinishedAt(LocalDateTime.now());
        if (errors.isEmpty()) {
            job.setStatus(AncillaryImportJob.JobStatus.SUCCESS);
        } else {
            job.setStatus(successfulLines > 0
                ? AncillaryImportJob.JobStatus.SUCCESS_WITH_ERRORS
                : AncillaryImportJob.JobStatus.FAILED
            );
        }
        jobRepository.save(job);

    } catch (Exception fatal) {
        // 7) En caso de error “global” marcamos el job como FAILED
        job.setTotalLines(totalLines);
        job.setSuccessfulLines(successfulLines);
        job.setErrorCount(errors.size());
        job.setFinishedAt(LocalDateTime.now());
        job.setStatus(AncillaryImportJob.JobStatus.FAILED);
        jobRepository.save(job);
        log.error("Job {} falló: {}", jobId, fatal.getMessage(), fatal);
    }
}

    @Transactional
    public ImportResultDTO processFile(MultipartFile file, String format, boolean continueOnError) {
        log.info("Inicio importación Ancillary – archivo: {}, formato: {}, continueOnError: {}",
                 file.getOriginalFilename(), format, continueOnError);
       
        AncillaryImportJob job = new AncillaryImportJob();
        job.setOriginalFilename(file.getOriginalFilename());
        job.setFormat(format);
        job.setContinueOnError(continueOnError);
        job.setStatus(AncillaryImportJob.JobStatus.PENDING);
        job.setTotalLines(0);
        job.setSuccessfulLines(0);
        job.setErrorCount(0);
        job.setStartedAt(LocalDateTime.now());
        job = jobRepository.save(job);
        log.info("Job de importación creado con ID: {}", job.getId());


        // 1) Verificar que exista un importador que soporte ese 'format'
        IAncillaryImporter importer = getImporterForFormat(format);
        if (importer == null) {
             job.setStatus(AncillaryImportJob.JobStatus.FAILED);
             job.setFinishedAt(LocalDateTime.now());
             jobRepository.save(job);
            throw new EqmValidationException(localizedMessageBuilder, "IMPORT_FORMAT_NOT_SUPPORTED", format);
        }

        job.setStatus(AncillaryImportJob.JobStatus.PROCESSING);
        jobRepository.save(job);




        // 2) Listas para acumular filas exitosas y errores
        List<String[]> successfulRows = new ArrayList<>();
        List<AncillaryImportError> errors = new ArrayList<>();
        int totalLines = 0;
        int successfulLines = 0;

        try (
            Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withTrim())
        ) {
            // 3) Verificar cabeceras obligatorias
            Map<String, Integer> headerMap = parser.getHeaderMap();
            if (!headerMap.containsKey("BOX_SN") ||
                !headerMap.containsKey("POD_SN") ||
                !headerMap.containsKey("NODE_MODEL")) {
                throw new RuntimeException("Faltan columnas obligatorias en el CSV: BOX_SN y/o POD_SN y/o NODE_MODEL");
            }

            // 4) Iterar cada registro (fila) del CSV
            for (CSVRecord record : parser) {
                totalLines++;
                try {
                    String serial    = record.get("BOX_SN").trim();
                    String mac       = record.get("POD_SN").trim();
                    String modelName = record.get("NODE_MODEL").trim();

                    // 4.1) Validar no vacío
                    if (serial.isEmpty() || mac.isEmpty() || modelName.isEmpty()) {
                        throw new RuntimeException(
                            "Los campos BOX_SN, POD_SN y NODE_MODEL no pueden estar vacíos (fila "
                            + record.getRecordNumber() + ")");
                    }

                    // 4.2) Validar duplicados *antes* de salvar usando método extraído
                    try {
                        validateEquipment(serial, mac);
                    } catch (EqmValidationException e) {
                        AncillaryImportError errEnt = new AncillaryImportError();
                        errEnt.setJob(job);
                        errEnt.setLineNumber((int) record.getRecordNumber());
                        errEnt.setErrorMessage(e.getMessage());
                        String originalLine = serial + "," + mac + "," + modelName;
                        errEnt.setOriginalLine(originalLine);
                        errors.add(errEnt);
                        if (!continueOnError) break;
                        continue;
                    }

                    // 5) Buscar el EquipmentModel (por nombre)
                    var modelOpt = equipmentModelRepository.findByName(modelName);

                    if (modelOpt.isEmpty()) {
                        throw new RuntimeException("Modelo de equipo no encontrado: " + modelName);
                    }
                    var equipmentModel = modelOpt.get();

                    // 6) Buscar el Warehouse por defecto
                    var whOpt = warehouseRepository.findById(defaultWarehouseId);
                    if (whOpt.isEmpty()) {
                        throw new RuntimeException("Almacén no encontrado con ID: " + defaultWarehouseId);
                    }
                    var warehouse = whOpt.get();

                    // 7) Crear el AncillaryEquipment using factory
                    AncillaryEquipment ancillary = equipmentFactory.createFromCsvRecord(
                        equipmentModel,
                        warehouse,
                        serial,
                        mac
                    );
                    ancillary.setStatus(Status.INSTORE);

                    // 8) Guardar en base de datos
                    ancillaryRepository.save(ancillary);
                    successfulLines++;
                    successfulRows.add(new String[] {
                        serial,
                        mac,
                        String.valueOf(defaultWarehouseId),
                        String.valueOf(equipmentModel.getId())
                    });

                } catch (Exception exRow) {
                    // 9) Si hay error en esta fila (ej. modelo no existe), lo registramos
                    AncillaryImportError errEnt = new AncillaryImportError();
                    errEnt.setJob(job);
                    errEnt.setLineNumber((int) record.getRecordNumber());
                    errEnt.setErrorMessage(exRow.getMessage());
                    String originalLine = record.get("BOX_SN") + "," +
                                          record.get("POD_SN") + "," +
                                          record.get("NODE_MODEL");
                    errEnt.setOriginalLine(originalLine);
                    errors.add(errEnt);

                    if (!continueOnError) {
                        break;
                    }
                }
            }

            // 10) Persistir todos los errores en tabla "ancillary_import_error"
            importErrorRepo.saveAll(errors);

            // 11) Generar un único CSV de resultados
            String baseName = FilenameUtils.getBaseName(file.getOriginalFilename());
            String resultFilename = baseName + "_result_" + job.getId().toString() + ".csv";
            String fullPath = Path.of(resultBasePath, resultFilename).toString();
            writeCsvResult(fullPath, successfulRows, errors);


            job.setTotalLines(totalLines);
            job.setSuccessfulLines(successfulLines);
            job.setErrorCount(errors.size());
            job.setResultFilePath(fullPath);
            job.setFinishedAt(LocalDateTime.now());

            if (errors.isEmpty()) {
                    job.setStatus(AncillaryImportJob.JobStatus.SUCCESS);
             } else {
                    if (successfulLines > 0) 
                        job.setStatus(AncillaryImportJob.JobStatus.SUCCESS_WITH_ERRORS);
                    else 
                        job.setStatus(AncillaryImportJob.JobStatus.FAILED);
                }
                jobRepository.save(job);


            // 12) Construir el DTO de retorno
            ImportResultDTO dto = new ImportResultDTO();
            dto.setTotalLines(totalLines);
            dto.setSuccessfulLines(successfulLines);
            dto.setErrorLines(errors.size());

            List<LineErrorDTO> errorDTOs = new ArrayList<>();
            for (AncillaryImportError e : errors) {
                errorDTOs.add(new LineErrorDTO(
                    e.getLineNumber(),
                    e.getErrorMessage(),
                    e.getOriginalLine(),
                    file.getOriginalFilename(),
                    null
                ));
            }
            dto.setErrors(errorDTOs);
            dto.setStatus(errors.isEmpty()
                          ? "SUCCESS"
                          : (successfulLines == 0 ? "FAILED" : "PARTIAL_SUCCESS"));
            dto.setImportDate(LocalDateTime.now());
            dto.setFileName(file.getOriginalFilename());
            dto.setFormat(format);

            log.info("Importación finalizada – tot={}, ok={}, err={}, status={}",
                     totalLines, successfulLines, errors.size(), dto.getStatus());
            return dto;

        } catch (IOException ioe) {
            job.setStatus(AncillaryImportJob.JobStatus.FAILED);
            job.setTotalLines(totalLines);
            job.setSuccessfulLines(successfulLines);
            job.setErrorCount(errors.size());
            job.setFinishedAt(LocalDateTime.now());
            jobRepository.save(job);
            log.error("Error leyendo CSV: {}", ioe.getMessage(), ioe);
            throw new EqmValidationException(localizedMessageBuilder, "IMPORT_FILE_READ_ERROR", ioe.getMessage());
        } catch (Exception e) {
            log.error("Error durante importAncillary: {}", e.getMessage(), e);
            throw new EqmValidationException(localizedMessageBuilder, "IMPORT_FAILED", e.getMessage());
        }
    }

    /** Busca un importador que soporte el format solicitado */
    private IAncillaryImporter getImporterForFormat(String format) {
        if (ancillaryImporters.containsKey(format)) {
            return ancillaryImporters.get(format);
        }
        return ancillaryImporters.values().stream()
                .filter(i -> i.getNames().contains(format))
                .findFirst()
                .orElse(null);
    }

    /**
     * Escribe un CSV de resultados con encabezado:
     * status,serialNumber,macAddress,warehouseId,equipmentModelId,lineNumber,errorMessage,originalLine
     * - Filas SUCCESS => status="SUCCESS", serial,mac,warehouseId,modelId
     * - Filas ERROR   => status="ERROR", vacía series iniciales, y pone lineNumber,errorMessage,originalLine
     */
    private void writeCsvResult(
            String fullPath,
            List<String[]> successfulRows,
            List<AncillaryImportError> errors
    ) throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT
                .withHeader("status",
                            "serialNumber",
                            "macAddress",
                            "warehouseId",
                            "equipmentModelId",
                            "lineNumber",
                            "errorMessage",
                            "originalLine");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath));
             CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {

            // 1) Filas exitosas
            for (String[] fields : successfulRows) {
                csvPrinter.printRecord(
                        "SUCCESS",
                        fields[0],
                        fields[1],
                        fields[2],
                        fields[3],
                        "", "", ""
                );
            }
            // 2) Filas con error
            for (AncillaryImportError e : errors) {
                csvPrinter.printRecord(
                        "ERROR",
                        "", "", "", "",
                        e.getLineNumber(),
                        e.getErrorMessage(),
                        e.getOriginalLine()
                );
            }
            csvPrinter.flush();
        }
        log.info("CSV de resultados creado en: {}", fullPath);
    }

    //getImportJobById
    @Transactional(readOnly = true)
    public Optional<AncillaryImportJob> findJobById(Long jobId) {
        return jobRepository.findById(jobId);
    }

    /**
     * Devuelve los errores registrados para un job de importación.
     */
    @Transactional(readOnly = true)
    public List<AncillaryImportError> findErrorsByJobId(Long jobId) {
        return importErrorRepo.findAllByJob_Id(jobId);
    }
 

    /**
     * Si el formato es SAP_ANCILLARY_TEMP y las cabeceras requeridas existen,
     * procesa el archivo y guarda los datos en la tabla maestra y temporal.
     */
   
   // Asincrono
    private Optional<Long> handleSapAncillaryTempFormat(MultipartFile file, String format, boolean continueOnError) {
        if (!"SAP_ANCILLARY_TEMP".equalsIgnoreCase(format)) {
            return Optional.empty();
        }

        String originalFilename = Optional.ofNullable(file.getOriginalFilename()).orElse("input.xml");

        // 1) Crear job en estado PENDING
        AncillaryImportJob job = new AncillaryImportJob();
        job.setOriginalFilename(originalFilename);
        job.setFormat(format);
        job.setContinueOnError(continueOnError);
        job.setStatus(AncillaryImportJob.JobStatus.PENDING);
        job.setStartedAt(LocalDateTime.now());
        job.setTotalLines(0);
        job.setSuccessfulLines(0);
        job.setErrorCount(0);
        job = jobRepository.save(job);

        // 2) Guardar archivo subido en STAGING (temporal) - no es el definitivo
        try {
            String inputDir = resultBasePath + "/inputs/staging";
            Files.createDirectories(Path.of(inputDir));

            String ext = Optional.ofNullable(FilenameUtils.getExtension(originalFilename))
                    .filter(s -> !s.isBlank()).orElse("xml");

            Path stagedPath = Path.of(inputDir, "job-" + job.getId() + "_staged." + ext);
            file.transferTo(stagedPath.toFile());

            // Guardamos la ruta de staging; será eliminada al finalizar el procesamiento real
            job.setInputFilePath(stagedPath.toString());
            jobRepository.save(job);
        } catch (IOException e) {
            job.setStatus(AncillaryImportJob.JobStatus.FAILED);
            job.setFinishedAt(LocalDateTime.now());
            jobRepository.save(job);

            recordJobError(job, "IMPORT_IO_ERROR", "No puede guardar archivo de entrada: " + e.getMessage());
            throw new EqmValidationException(localizedMessageBuilder, "IMPORT_IO_ERROR",
                    "No puede guardar archivo de entrada: " + e.getMessage());
        }

        // 3) Agendar procesamiento real después del commit
        final Long jobId = job.getId();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                executor.submit(() -> executeSapAncillaryTempJob(jobId));
            }
        });

        // 4) Regresar inmediatamente el ID
        return Optional.of(jobId);
    }

    @Transactional
    public void executeSapAncillaryTempJob(Long jobId) {
        AncillaryImportJob job = jobRepository.findById(jobId).orElse(null);
        if (job == null) {
            log.error("executeSapAncillaryTempJob: job {} no existe", jobId);
            return;
        }

        boolean wasPgp = false;

        try {
            job.setStatus(AncillaryImportJob.JobStatus.PROCESSING);
            job.setStartedAt(LocalDateTime.now());
            jobRepository.save(job);

            // 1) Leer archivo STAGING guardado en fase de ingesta
            Path stagedPath = Path.of(job.getInputFilePath());
            byte[] uploadedBytes = Files.readAllBytes(stagedPath);

            // 2) Detectar/descifrar PGP -> siempre producir xmlBytes "usable"
            byte[] xmlBytes;
            if (isAsciiArmoredPgp(uploadedBytes)) {
                wasPgp = true;
                try (InputStream privateKeyIn = getClass().getResourceAsStream("/keys/milicom_dev_secretkey.asc")) {
                    if (privateKeyIn == null) {
                        throw new IllegalStateException("No se encontró la clave PGP en /keys/milicom_dev_secretkey.asc");
                    }
                    xmlBytes = PgpDecryptUtil.decrypt(
                            new ByteArrayInputStream(uploadedBytes), privateKeyIn, "Milicom2020");
                    log.info("Job {}: archivo cifrado PGP descifrado OK.", jobId);
                }
            } else {
                xmlBytes = uploadedBytes;
                log.info("Job {}: archivo no PGP, se procesa como XML.", jobId);
            }

            // 3) Parsear XML
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new ByteArrayInputStream(xmlBytes));
            doc.getDocumentElement().normalize();

            // 4) Validar MOVE_TYPE (igual que antes)
            Set<String> permitidos = Set.of("101", "303");
            Set<String> encontrados = new HashSet<>();
            NodeList moveTypeNodes = doc.getElementsByTagName("MOVE_TYPE");
            for (int i = 0; i < moveTypeNodes.getLength(); i++) {
                String mt = moveTypeNodes.item(i).getTextContent().trim();
                encontrados.add(mt);
                if (!permitidos.contains(mt)) {
                    failJobAndThrow(job, "IMPORT_INVALID_MOVE_TYPE",
                            String.format("MOVE_TYPE '%s' no permitido. Valores permitidos: %s", mt, permitidos));
                }
            }
            if (encontrados.isEmpty()) {
                failJobAndThrow(job, "IMPORT_MOVE_TYPE_MISSING", "No se encontró ningún nodo MOVE_TYPE en el XML.");
            }
            if (encontrados.size() > 1) {
                failJobAndThrow(job, "IMPORT_MULTIPLE_MOVE_TYPE",
                        String.format("Se encontraron múltiples MOVE_TYPE: %s. No está soportado.", encontrados));
            }
            String moveType = encontrados.iterator().next();

            // 5) Crear carpeta MOVE-<type>
            Path moveDir = Path.of(resultBasePath, "inputs", "MOVE-" + moveType);
            Files.createDirectories(moveDir);

            // 6) Construir nombre final: job-<id>-<basename(original)>.xml
            String baseName = FilenameUtils.getBaseName(
                    Optional.ofNullable(job.getOriginalFilename()).orElse("input"));
            // Limpieza básica de nombre
            baseName = baseName.replaceAll("[\\s]+", "_").replaceAll("[^A-Za-z0-9._-]", "");
            String finalFileName = "job-" + job.getId() + "-" + baseName + ".xml";
            Path finalPath = moveDir.resolve(finalFileName);

            // 7) Escribir ÚNICO archivo final (XML usable)
            Files.write(finalPath, xmlBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            // 8) Actualizar ruta del job al archivo definitivo y borrar staging
            job.setInputFilePath(finalPath.toString());
            jobRepository.save(job);

            try {
                Files.deleteIfExists(stagedPath);
            } catch (Exception delEx) {
                log.warn("Job {}: no se pudo eliminar staging {}: {}", jobId, stagedPath, delEx.getMessage());
            }

            // 9) Enrutar por MOVE_TYPE (igual que antes)
            switch (moveType) {
                case "101":
                    processMoveType101(doc, job.getOriginalFilename(), xmlBytes, job);
                    break;
                case "303":
                    processMoveType303(doc, job.getOriginalFilename(), xmlBytes, job);
                    break;
                default:
                    failJobAndThrow(job, "IMPORT_INVALID_MOVE_TYPE",
                            String.format("MOVE_TYPE '%s' aún no está implementado.", moveType));
            }

        } catch (EqmValidationException ve) {
            if (job.getId() != null && job.getFinishedAt() == null) {
                job.setStatus(AncillaryImportJob.JobStatus.FAILED);
                job.setFinishedAt(LocalDateTime.now());
                jobRepository.save(job);
            }
            throw ve;
        } catch (Exception e) {
            String code = "IMPORT_UNEXPECTED_ERROR";
            String msg  = (e.getMessage() != null) ? e.getMessage() : e.getClass().getSimpleName();

            recordJobError(job, code, msg);

            log.error("Job {} falló en executeSapAncillaryTempJob: {}", jobId, e.getMessage(), e);
            job.setStatus(AncillaryImportJob.JobStatus.FAILED);
            job.setFinishedAt(LocalDateTime.now());
            jobRepository.save(job);
        }
    }


    private String getText(Element parent, String tag) {
        NodeList list = parent.getElementsByTagName(tag);
        if (list.getLength() == 0) {
            return null;
        }
        return list.item(0).getTextContent();
    }

    private void failJobAndThrow(AncillaryImportJob job, String errorCode, String message) {

        // 1) registrar error global
        recordJobError(job, errorCode, message);

        // 2) cerrar el job
        job.setStatus(AncillaryImportJob.JobStatus.FAILED);
        job.setFinishedAt(LocalDateTime.now());
        jobRepository.save(job);

        // 3) lanzar
        throw new EqmValidationException(localizedMessageBuilder, errorCode, message);
    }

    private boolean isAsciiArmoredPgp(byte[] content) {
        // Busca la cabecera típica de PGP ASCII armor
        String head = new String(content, 0, Math.min(content.length, 200), StandardCharsets.US_ASCII);
        return head.contains("-----BEGIN PGP MESSAGE-----");
    }

//Funcion cuando el MOVE_TYPE es 101
// Funcion cuando el MOVE_TYPE es 101
private Optional<Long> processMoveType101(Document doc,
                                          String originalFilename,
                                          byte[] uploadedBytes,
                                          AncillaryImportJob job) {
    try {
        // (A) Validar PO_NUMBER (todos iguales)
        NodeList poNodes = doc.getElementsByTagName("PO_NUMBER");
        if (poNodes.getLength() == 0) {
            failJobAndThrow(job, "IMPORT_PO_NO_MISSING", "Falta el nodo PO_NUMBER en el XML");
        }
        String poNoValue = poNodes.item(0).getTextContent().trim();
        for (int i = 1; i < poNodes.getLength(); i++) {
            String po = poNodes.item(i).getTextContent().trim();
            if (!poNoValue.equals(po)) {
                failJobAndThrow(job, "IMPORT_DIFFERENT_PO_NO", "Todos los registros deben tener el mismo PO_NUMBER");
            }
        }
        // OJO: ya NO bloqueamos por existsByPoNo(poNoValue), porque habrá 1 fila por MATERIAL.

        // === (A.1) Validar que existan seriales con SERIALNO válido ===
        NodeList serialNodesCheck = doc.getElementsByTagName("E1BP2017_GM_SERIALNUMBER");

        boolean hasValidSerial = false;
        for (int i = 0; i < serialNodesCheck.getLength(); i++) {
            Element snEl = (Element) serialNodesCheck.item(i);
            String serial = getText(snEl, "SERIALNO");
            if (serial != null && !serial.isBlank()) {
                hasValidSerial = true;
                break;
            }
        }

        if (!hasValidSerial) {
            // No se encontraron seriales -> responder 422 y devolver PO_NUMBER
            job.setStatus(AncillaryImportJob.JobStatus.FAILED);
            job.setFinishedAt(LocalDateTime.now());
            jobRepository.save(job);

            // Registrar error en AncillaryImportError
            recordJobError(
                job,
                "IMPORT_NO_SERIALS_FOUND",
                String.format("No se encontraron seriales. PO_NUMBER: %s", poNoValue)
            );

            throw new EqmValidationException(
                localizedMessageBuilder,
                "IMPORT_NO_SERIALS_FOUND",
                String.format("No se encontraron seriales. PO_NUMBER: %s", poNoValue)
            );
        }
        // === Fin validación de seriales ===

        // (B) Mapear ITEMs/MATERIAL por MATDOC_ITM; prevalidar homologación
        Map<String, String> itemNoToMaterial = new LinkedHashMap<>(); // "0001" -> "1400002265"
        Map<String, Long>   itemNoToModelId = new HashMap<>();        // "0001" -> equipmentModelId (si homologado y Enabled)
        Map<String, String> itemToStorage = new HashMap<>();
        Set<String>         invalidItems = new HashSet<>();           // sin homologación
        Set<String>         disabledItems = new HashSet<>();          // homologado pero NO Enabled

        NodeList itemNodes = doc.getElementsByTagName("E1BP2017_GM_ITEM_CREATE");
        for (int i = 0; i < itemNodes.getLength(); i++) {
            Element itemEl = (Element) itemNodes.item(i);
            String material = getText(itemEl, "MATERIAL");
            String storageLoc = getText(itemEl, "STGE_LOC"); // extraer el storage location
            String itemNo = String.format("%04d", i + 1); // 0001, 0002, ...

            if (material == null || material.isBlank()) {
                log.warn("ITEM {} sin MATERIAL. Se omitirá.", itemNo);
                invalidItems.add(itemNo);
                continue;
            }

            String cleanedMaterial = material.trim().replaceFirst("^0+(?!$)", "");
            itemNoToMaterial.put(itemNo, cleanedMaterial);

            if (storageLoc != null && !storageLoc.isBlank()) {
                itemToStorage.put(itemNo, storageLoc.trim());
            }

            Optional<HomologacionMaterialSap> homOpt = homologacionMaterialSapRepository.findByIdMaterialSap(cleanedMaterial);
            if (homOpt.isEmpty()) {
                invalidItems.add(itemNo);
                log.warn("ITEM {} MATERIAL {} sin homologación. Se insertará en temporal con status=error.", itemNo, cleanedMaterial);
                continue;
            }
            HomologacionMaterialSap homologacion = homOpt.get();
            if (!"Enabled".equalsIgnoreCase(homologacion.getStatus())) {
                disabledItems.add(itemNo);
                log.warn("ITEM {} MATERIAL {} homologado pero NO Enabled. Se insertará en temporal con status=error.", itemNo, cleanedMaterial);
                continue;
            }
            itemNoToModelId.put(itemNo, homologacion.getEquipmentModelId());
        }

        if (itemNoToMaterial.isEmpty()) {
            failJobAndThrow(job, "IMPORT_MODEL_MISSING", "No se encontró ningún MATERIAL en el XML");
        }

        // (C) Insertar TODOS los materiales en la tabla maestra y mapear material -> poId
        Map<String, Long> materialToPoId = new HashMap<>();
        for (String materialValue : new LinkedHashSet<>(itemNoToMaterial.values())) {
            PoAncillaryEquipmentSap poEntity = new PoAncillaryEquipmentSap();
            poEntity.setPoNo(poNoValue);
            poEntity.setModel(materialValue);
            poEntity.setStatus("temporal");
            poEntity = poAncillaryEquipmentSapRepository.save(poEntity);
            materialToPoId.put(materialValue, poEntity.getId());
        }

        if (materialToPoId.isEmpty()) {
            failJobAndThrow(job, "IMPORT_NO_VALID_ITEMS", "No fue posible registrar materiales en la tabla maestra del PO.");
        }

        // (D) Insertar en temporal usando el poId por MATERIAL
        NodeList serialNodes = doc.getElementsByTagName("E1BP2017_GM_SERIALNUMBER");
        int insertedCount = 0;   // insertados OK (status="temporal")
        int totalRecords = 0;

        // === NO INSERTADOS (skips hard) ===
        int skippedByMissingData = 0;    // sin SERIALNO o sin MATDOC_ITM
        int skippedByItemMapMissing = 0; // MATDOC_ITM no mapea a MATERIAL
        int skippedByMissingPo = 0;      // sin poId para MATERIAL
        int skippedByDuplicate = 0;      // serial duplicado en tabla

        // === INSERTADOS CON STATUS=error (se insertan en temporal) ===
        int errInsertedNotHomologated = 0; // sin homologación
        int errInsertedDisabled = 0;       // homologado pero NO Enabled
        int errInsertedUnmappedModel = 0;  // no mapeado a modelId (caso raro distinto de los 2 anteriores)

        for (int i = 0; i < serialNodes.getLength(); i++) {
            totalRecords++;
            Element snEl = (Element) serialNodes.item(i);

            String serial = getText(snEl, "SERIALNO");
            String matdoc = getText(snEl, "MATDOC_ITM"); // 0001, 0002, ...

            if (serial == null || serial.isBlank() || matdoc == null || matdoc.isBlank()) {
                skippedByMissingData++;
                log.warn("Serial omitido por datos faltantes: MATDOC_ITM='{}', SERIALNO='{}'", matdoc, serial);
                continue; // NO INSERTA
            }

            String materialForItem = itemNoToMaterial.get(matdoc);
            String storageForItem = itemToStorage.get(matdoc);
            if (materialForItem == null || materialForItem.isBlank()) {
                skippedByItemMapMissing++;
                log.warn("MATDOC_ITM '{}' no corresponde a MATERIAL válido.", matdoc);
                continue; // NO INSERTA
            }

            Long poIdForMaterial = materialToPoId.get(materialForItem);
            if (poIdForMaterial == null) {
                skippedByMissingPo++;
                log.warn("No se encontró poId para MATERIAL '{}' (MATDOC_ITM '{}').", materialForItem, matdoc);
                continue; // NO INSERTA
            }

            Long modelId = itemNoToModelId.get(matdoc);

            // === INSERTADOS CON STATUS=error ===
            if (modelId == null) {
                String status = "error";
                String errorMessage;

                if (invalidItems.contains(matdoc)) {
                    errorMessage = "Sin homologar: " + materialForItem;
                    errInsertedNotHomologated++;
                } else if (disabledItems.contains(matdoc)) {
                    errorMessage = "Homologado pero no Enabled: " + materialForItem;
                    errInsertedDisabled++;
                } else {
                    errorMessage = "Item no mapeado a modelId: " + materialForItem;
                    errInsertedUnmappedModel++;
                }

                // Se INSERTA en temporal con status=error
                EquipmentTemp temp = new EquipmentTemp();
                temp.setBoxSn(serial.trim());
                temp.setPoAncillaryeqmSapId(poIdForMaterial);
                temp.setStatus(status);
                temp.setMessages(errorMessage);
                temp.setStorageCurrent(storageForItem);
                temp.setCreatedAt(LocalDateTime.now());
                equipmentTempRepository.save(temp);

                continue; // NO cuenta como "insertedCount" (éxito), pero SÍ fue insertado con error
            }

            // === Duplicados (NO INSERTA) ===
            if (equipmentTempRepository.existsByBoxSn(serial.trim())) {
                skippedByDuplicate++;
                continue;
            }

            // === INSERTADO OK ===
            EquipmentTemp temp = new EquipmentTemp();
            temp.setBoxSn(serial.trim());
            temp.setModelId(modelId);
            temp.setPoAncillaryeqmSapId(poIdForMaterial); // poId del MATERIAL específico
            temp.setStatus("temporal");
            temp.setStorageCurrent(storageForItem);
            temp.setCreatedAt(LocalDateTime.now());
            equipmentTempRepository.save(temp);

            insertedCount++;
        }

        log.info("IMPORT 101 -> total={}, insertedOK={}, notInserted: missingData={}, noItemMap={}, noPO={}, duplicates={}, insertedAsError: notHomologated={}, disabled={}, unmappedModel={}",
                totalRecords, insertedCount,
                skippedByMissingData, skippedByItemMapMissing, skippedByMissingPo, skippedByDuplicate,
                errInsertedNotHomologated, errInsertedDisabled, errInsertedUnmappedModel);

        
       if (insertedCount == 0) {
            // Totales por categoría
            int notInsertedTotal = skippedByMissingData + skippedByItemMapMissing + skippedByMissingPo + skippedByDuplicate;
            int insertedWithErrorTotal = errInsertedNotHomologated + errInsertedDisabled + errInsertedUnmappedModel;

            // Detalles para el mensaje
            List<String> partesNoInsert = new ArrayList<>();
            if (skippedByDuplicate > 0)      partesNoInsert.add(String.format("%d duplicados", skippedByDuplicate));
            if (skippedByMissingData > 0)    partesNoInsert.add(String.format("%d con datos faltantes", skippedByMissingData));
            if (skippedByItemMapMissing > 0) partesNoInsert.add(String.format("%d sin mapear MATDOC->MATERIAL", skippedByItemMapMissing));
            if (skippedByMissingPo > 0)      partesNoInsert.add(String.format("%d sin PO/MATERIAL", skippedByMissingPo));

            List<String> partesErrInsert = new ArrayList<>();
            if (errInsertedNotHomologated > 0) partesErrInsert.add(String.format("%d sin homologación", errInsertedNotHomologated));
            if (errInsertedDisabled > 0)       partesErrInsert.add(String.format("%d no Enabled", errInsertedDisabled));
            if (errInsertedUnmappedModel > 0)  partesErrInsert.add(String.format("%d no mapeados (modelId)", errInsertedUnmappedModel));

            String detallesNoInsert = partesNoInsert.isEmpty() ? "-" : String.join(", ", partesNoInsert);
            String detallesErrInsert = partesErrInsert.isEmpty() ? "-" : String.join(", ", partesErrInsert);

            String msg = String.format(
                "No se insertó ningún registro válido. No insertados=%d: %s. Insertados con status=error=%d: %s.",
                notInsertedTotal, detallesNoInsert, insertedWithErrorTotal, detallesErrInsert
            );

            // Marcar FAILED y guardar
            job.setStatus(AncillaryImportJob.JobStatus.FAILED);
            job.setFinishedAt(LocalDateTime.now());
            jobRepository.save(job);

            // Registrar error (1 fila en AncillaryImportError)
            recordJobError(job, "IMPORT_NO_INSERTS", msg);

            // Ajustar contador final (no insertados + insertados con error)
            job.setErrorCount(notInsertedTotal + insertedWithErrorTotal);
            jobRepository.save(job);

            throw new EqmValidationException(localizedMessageBuilder, "IMPORT_NO_INSERTS", msg);
        }



        // (E) Guardar archivo original tal como llegó (opcional)
        /*
        String inputDir = resultBasePath + "/inputs";
        Files.createDirectories(Path.of(inputDir));
        Path originalPath = Path.of(inputDir, "job-" + job.getId() + "_" + originalFilename);
        Files.write(originalPath, uploadedBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        job.setInputFilePath(originalPath.toString());
        */

        // (F) Finalizar estado con RESUMEN ÚNICO correcto
        job.setFinishedAt(LocalDateTime.now());

        int notInsertedTotal = skippedByMissingData + skippedByItemMapMissing + skippedByMissingPo + skippedByDuplicate;
        int insertedWithErrorTotal = errInsertedNotHomologated + errInsertedDisabled + errInsertedUnmappedModel;

        // Listas para construir mensaje
        List<String> partesNoInsert = new ArrayList<>();
        if (skippedByDuplicate > 0)           partesNoInsert.add(String.format("%d duplicados", skippedByDuplicate));
        if (skippedByMissingData > 0)         partesNoInsert.add(String.format("%d con datos faltantes", skippedByMissingData));
        if (skippedByItemMapMissing > 0)      partesNoInsert.add(String.format("%d sin mapear MATDOC→MATERIAL", skippedByItemMapMissing));
        if (skippedByMissingPo > 0)           partesNoInsert.add(String.format("%d sin PO/MATERIAL", skippedByMissingPo));

        List<String> partesErrInsert = new ArrayList<>();
        if (errInsertedNotHomologated > 0)    partesErrInsert.add(String.format("%d sin homologación", errInsertedNotHomologated));
        if (errInsertedDisabled > 0)          partesErrInsert.add(String.format("%d no Enabled", errInsertedDisabled));
        if (errInsertedUnmappedModel > 0)     partesErrInsert.add(String.format("%d no mapeados (modelId)", errInsertedUnmappedModel));

        if (notInsertedTotal > 0 || insertedWithErrorTotal > 0) {
            StringBuilder resumen = new StringBuilder();
            if (notInsertedTotal > 0) {
                resumen.append(String.format("No se insertaron %d registro(s)", notInsertedTotal));
                if (!partesNoInsert.isEmpty()) {
                    resumen.append(": ").append(String.join(", ", partesNoInsert));
                }
                resumen.append(". ");
            }
            if (insertedWithErrorTotal > 0) {
                resumen.append(String.format("Se insertaron %d con status=error", insertedWithErrorTotal));
                if (!partesErrInsert.isEmpty()) {
                    resumen.append(": ").append(String.join(", ", partesErrInsert));
                }
                resumen.append(".");
            }

            // ÚNICA fila en AncillaryImportError con el breakdown final (correcto)
            recordJobError(job, "IMPORT_SUMMARY", resumen.toString());

            // errorCount = no insertados + insertados con error
            job.setErrorCount(notInsertedTotal + insertedWithErrorTotal);
        }

        job.setStatus((notInsertedTotal > 0 || insertedWithErrorTotal > 0)
                ? AncillaryImportJob.JobStatus.SUCCESS_WITH_ERRORS
                : AncillaryImportJob.JobStatus.SUCCESS);
        job.setTotalLines(totalRecords);
        job.setSuccessfulLines(insertedCount);
        jobRepository.save(job);

        return Optional.of(job.getId());

    } catch (EqmValidationException ve) {
        if (job.getId() != null) {
            job.setStatus(AncillaryImportJob.JobStatus.FAILED);
            job.setFinishedAt(LocalDateTime.now());
            jobRepository.save(job);
        }
        throw ve;
    } catch (Exception e) {
        job.setStatus(AncillaryImportJob.JobStatus.FAILED);
        job.setFinishedAt(LocalDateTime.now());
        jobRepository.save(job);

        String code = "IMPORT_FILE_READ_ERROR";
        String msg = "No puede guardar archivo de entrada: " + e.getMessage();

        // Registrar error
        recordJobError(job, code, msg);

        throw new EqmValidationException(localizedMessageBuilder, "IMPORT_FILE_READ_ERROR", e.getMessage());
    }
}


//Funcion cuando el MOVE_TYPE es 303 EJEMPLO.
private Optional<Long> processMoveType303(Document doc,
                                          String originalFilename,
                                          byte[] uploadedBytes,
                                          AncillaryImportJob job) {
    // TODO: Implementar reglas específicas para 303.
    failJobAndThrow(job, "IMPORT_MOVE_TYPE_303_UNSUPPORTED",
            "El MOVE_TYPE 303 está permitido pero su procesamiento aún no está implementado.");
    return Optional.empty(); // inalcanzable
}

    private void recordJobError(AncillaryImportJob job, String errorCode, String message,
                                Integer lineNumber, String originalLine) {
        newTxTemplate().execute(status -> {


            //Log Global para errores
            log.warn("Fallo en el job {}: [{}] {}", 
                  (job != null ? job.getId() : "null"), 
                  (errorCode != null && !errorCode.isBlank()) ? errorCode : "UNKNOWN", 
                  message);


            AncillaryImportError err = new AncillaryImportError();
            err.setJob(job);

            Integer ln = lineNumber;          // evita comparar primitivo con null
            if (ln == null) ln = 0;
            err.setLineNumber(ln);

            String code = (errorCode != null && !errorCode.isBlank()) ? errorCode : "UNKNOWN";
            err.setErrorMessage("[" + code + "] " + message);
            err.setOriginalLine(originalLine);
            importErrorRepo.save(err);

            int current = job.getErrorCount(); // si es int, nunca será null
            job.setErrorCount(current + 1);
            jobRepository.save(job);
            return null;
        });
    }

    private void recordJobError(AncillaryImportJob job, String errorCode, String message) {
        recordJobError(job, errorCode, message, 0, null);
    }


           
}
