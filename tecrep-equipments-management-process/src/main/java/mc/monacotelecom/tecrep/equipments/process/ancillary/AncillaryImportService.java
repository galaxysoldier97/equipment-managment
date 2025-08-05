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
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.UUID;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryImportJob;
import mc.monacotelecom.tecrep.equipments.factory.AncillaryEquipmentFactory;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;

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
        private Optional<Long> handleSapAncillaryTempFormat(MultipartFile file, String format, boolean continueOnError) {
    if (!"SAP_ANCILLARY_TEMP".equalsIgnoreCase(format)) {
        return Optional.empty();
    }

    String originalFilename = Optional.ofNullable(file.getOriginalFilename()).orElse("");
    if (!originalFilename.matches("^[^.]+\\.xml$")) {
        throw new EqmValidationException(localizedMessageBuilder, "IMPORT_INVALID_EXTENSION", "El archivo debe ser un XML");
    }

    AncillaryImportJob job = new AncillaryImportJob();
    try {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file.getInputStream());
        doc.getDocumentElement().normalize();

        job.setOriginalFilename(originalFilename);
        job.setFormat(format);
        job.setContinueOnError(continueOnError);
        job.setStatus(AncillaryImportJob.JobStatus.PENDING);
        job.setStartedAt(LocalDateTime.now());
        job.setTotalLines(0);
        job.setSuccessfulLines(0);
        job.setErrorCount(0);
        job = jobRepository.save(job);

        // 1. Validar y obtener PO_NUMBER (todos deben ser iguales)
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
        if (poAncillaryEquipmentSapRepository.existsByPoNo(poNoValue)) {
            failJobAndThrow(job, "IMPORT_PO_NO_EXISTS", "El PO_NO ya existe en la tabla maestra");
        }

        // 2. Mapear MATERIAL por MATDOC_ITM (puede haber varios modelos)
        Map<String, String> itemToModel = new HashMap<>();
        NodeList itemNodes = doc.getElementsByTagName("E1BP2017_GM_ITEM_CREATE");
        for (int i = 0; i < itemNodes.getLength(); i++) {
            Element itemEl = (Element) itemNodes.item(i);
            String material = getText(itemEl, "MATERIAL");
            String itemNo = String.format("%04d", i + 1);
            if (material != null && !material.isBlank()) {
                itemToModel.put(itemNo, material.trim());
            }
        }
        if (itemToModel.isEmpty()) {
            failJobAndThrow(job, "IMPORT_MODEL_MISSING", "No se encontró ningún MATERIAL en el XML");
        }

        // 3. Guardar PO en tabla maestra utilizando el primer modelo disponible
        String modelValue = itemToModel.values().iterator().next();
        PoAncillaryEquipmentSap poEntity = new PoAncillaryEquipmentSap();
        poEntity.setPoNo(poNoValue);
        poEntity.setModel(modelValue);
        poEntity.setStatus("init");
        poEntity = poAncillaryEquipmentSapRepository.save(poEntity);
        Long poId = poEntity.getId();

        // 4. Insertar registros en equipments_temp
        NodeList serialNodes = doc.getElementsByTagName("E1BP2017_GM_SERIALNUMBER");
        int insertedCount = 0;
        int totalRecords = 0;
        for (int i = 0; i < serialNodes.getLength(); i++) {
            totalRecords++;
            Element snEl = (Element) serialNodes.item(i);
            String serial = getText(snEl, "SERIALNO");
            String matdoc = getText(snEl, "MATDOC_ITM");
            String nodeModel = itemToModel.getOrDefault(matdoc, modelValue);

            if (serial == null || serial.isBlank() || nodeModel == null || nodeModel.isBlank()) {
                failJobAndThrow(job, "IMPORT_MISSING_BOX_OR_MODEL",
                        String.format("Faltan datos obligatorios: BOX_SN='%s', NODE_MODEL='%s'", serial, nodeModel));
            }

            if (equipmentTempRepository.existsByBoxSn(serial)) {
                continue;
            }

            Optional<HomologacionMaterialSap> homOpt = homologacionMaterialSapRepository.findByIdMaterialSap(nodeModel);
            if (homOpt.isEmpty()) {
                failJobAndThrow(job, "IMPORT_MODEL_NOT_FOUND",
                        String.format("Modelo no homologado: NODE_MODEL='%s' no encontrado en homologacion_material_sap", nodeModel));
            }
            HomologacionMaterialSap homologacion = homOpt.get();
            if (!"Habilitado".equalsIgnoreCase(homologacion.getStatus())) {
                failJobAndThrow(job, "IMPORT_MODEL_DISABLED",
                        String.format("Modelo no homologado: NODE_MODEL='%s' no está Habilitado", nodeModel));
            }

            EquipmentTemp temp = new EquipmentTemp();
            temp.setBoxSn(serial.trim());
            temp.setModelId(homologacion.getEquipmentModelId());
            temp.setPoAncillaryeqmSapId(poId);
            temp.setStatus("temporal");
            temp.setCreatedAt(LocalDateTime.now());
            equipmentTempRepository.save(temp);
            insertedCount++;
        }

        if (insertedCount == 0) {
            failJobAndThrow(job, "IMPORT_NO_INSERTS", "No se pudo insertar ningún registro. Todos fueron omitidos por duplicados o datos inválidos.");
        }

        String inputDir = resultBasePath + "/inputs";
        Files.createDirectories(Path.of(inputDir));
        String savedName = "job-" + job.getId() + "_" + originalFilename;
        Path destination = Path.of(inputDir, savedName);
        file.transferTo(destination.toFile());
        job.setInputFilePath(destination.toString());

        if (insertedCount < totalRecords) {
            job.setStatus(AncillaryImportJob.JobStatus.SUCCESS_WITH_ERRORS);
        } else {
            job.setStatus(AncillaryImportJob.JobStatus.SUCCESS);
        }
        job.setFinishedAt(LocalDateTime.now());
        jobRepository.save(job);

        log.info("SAP_ANCILLARY_TEMP: PO {} registrado, archivo guardado, Job #{} SUCCESS", poNoValue, job.getId());
        return Optional.of(job.getId());

    } catch (IOException | ParserConfigurationException | SAXException e) {
        job.setStatus(AncillaryImportJob.JobStatus.FAILED);
        job.setFinishedAt(LocalDateTime.now());
        jobRepository.save(job);
        throw new EqmValidationException(localizedMessageBuilder, "IMPORT_FILE_READ_ERROR", e.getMessage());
    } catch (EqmValidationException ve) {
        if (job.getId() != null) {
            job.setStatus(AncillaryImportJob.JobStatus.FAILED);
            job.setFinishedAt(LocalDateTime.now());
            jobRepository.save(job);
        }
        throw ve;
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
    job.setStatus(AncillaryImportJob.JobStatus.FAILED);
    job.setFinishedAt(LocalDateTime.now());
    jobRepository.save(job);
    throw new EqmValidationException(localizedMessageBuilder, errorCode, message);
}

           
}
