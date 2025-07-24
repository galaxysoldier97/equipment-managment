package mc.monacotelecom.tecrep.equipments.integration.importer;

import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;

import java.io.File;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sql/clean.sql", "/sql/simcard_data.sql"})
class SimCardImportTests extends BaseIntegrationTest {

    @Test
    void import_monaco_success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "importSimCard_OK.OUT", "text/plain",
                new ClassPathResource("data/simCard/importSimCard_OK.OUT").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/SimCard/start")
                        .file(file)
                        .param("accessType", "FREEDHOME")
                        .param("configuration", "GEMALTO")
                        .param("batchNumber", "1")
                        .param("plmnId", "1")
                        .param("preactivated", "false")
                        .param("sync", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.importStatus").value("COMPLETED"))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].line").value(19))
                .andExpect(jsonPath("$.errors[0].error").value("mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException IMSI number '123456789012345' is already used by another SIM card"));

        var simCardOpt1 = simCardRepository.findBySerialNumberAndCategory("8937710330000340288", EquipmentCategory.SIMCARD);
        assertTrue(simCardOpt1.isPresent());
        var simCard1 = simCardOpt1.get();
        assertAll(
                () -> assertEquals("8937710330000340288", simCard1.getSerialNumber()),
                () -> assertEquals(8, simCard1.getCheckDigit()),
                () -> assertEquals("212103300034028", simCard1.getImsiNumber()),
                () -> assertEquals("208011502954028", simCard1.getImsiSponsorNumber()),
                () -> assertEquals("36983119", simCard1.getPuk1Code()),
                () -> assertEquals("14230460", simCard1.getPuk2Code()),
                () -> assertEquals("703537E0230A0F6E482D9812D58B8228", simCard1.getAdminCode()),
                () -> assertEquals("225C77B76E855D25ADABDD2CDD280DBF", simCard1.getAuthKey()),
                () -> assertEquals("0C.00", simCard1.getSimProfile()),
                () -> assertEquals("F84A285ECFBF3EE0D1AE5B19918F0223", simCard1.getOtaSignatureKey()),
                () -> assertEquals("0835B39E1FAF408B3857EDD2DAB8338F", simCard1.getPutDescriptionKey())
        );

        var simCardOpt2 = simCardRepository.findBySerialNumberAndCategory("8937710330000340296", EquipmentCategory.SIMCARD);
        assertTrue(simCardOpt2.isPresent());
        var simCard2 = simCardOpt2.get();
        assertAll(
                () -> assertEquals("8937710330000340296", simCard2.getSerialNumber()),
                () -> assertEquals(6, simCard2.getCheckDigit()),
                () -> assertEquals("212103300034029", simCard2.getImsiNumber()),
                () -> assertEquals("208011502954029", simCard2.getImsiSponsorNumber()),
                () -> assertEquals("82089119", simCard2.getPuk1Code()),
                () -> assertEquals("74593044", simCard2.getPuk2Code()),
                () -> assertEquals("71D7CB4C9970E3E23F67338E20CF6179", simCard2.getAdminCode()),
                () -> assertEquals("2802BA0B235470FF52AFBECE21610390", simCard2.getAuthKey()),
                () -> assertEquals("CFBB662899EFB001EEECF8134C1FCA8B", simCard2.getOtaSignatureKey()),
                () -> assertEquals("104D56442868DBDBFA666E0A47B1701A", simCard2.getPutDescriptionKey())
        );

        assertFalse(simCardRepository.findProjectionBySerialNumberAndCategory("8937710330000340273", EquipmentCategory.SIMCARD).isPresent());

        assertTrue(allImportsCompleted());
    }

    @SuppressWarnings("java:S5961")
    @Test
    void importESimCard_salt_success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "importESimCard2_OK.out", "text/plain",
                new ClassPathResource("data/simCard/importESimCard2_OK.out").getInputStream());


        mockMvc.perform(multipart("/private/auth/import/SimCard/start")
                        .file(file)
                        .param("accessType", "FREEDHOME")
                        .param("configuration", "GEMALTO")
                        .param("batchNumber", "1")
                        .param("plmnId", "1")
                        .param("preactivated", "false")
                        .param("sync", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"importStatus\": \"COMPLETED\",\"errors\": []}"));

        var simCardOpt1 = simCardRepository.findBySerialNumberAndCategory("8937710330000340288", EquipmentCategory.SIMCARD);
        assertTrue(simCardOpt1.isPresent());
        var simCard1 = simCardOpt1.get();
        assertAll(
                () -> assertEquals("8937710330000340288", simCard1.getSerialNumber()),
                () -> assertEquals("212103300034028", simCard1.getImsiNumber()),
                () -> assertEquals("208011502954028", simCard1.getImsiSponsorNumber()),
                () -> assertEquals("36983119", simCard1.getPuk1Code()),
                () -> assertEquals("14230460", simCard1.getPuk2Code()),
                () -> assertEquals("703537E0230A0F6E482D9812D58B8228", simCard1.getAdminCode()),
                () -> assertEquals("225C77B76E855D25ADABDD2CDD280DBF", simCard1.getAuthKey()),
                () -> assertEquals("F84A285ECFBF3EE0D1AE5B19918F0223", simCard1.getOtaSignatureKey()),
                () -> assertEquals("0835B39E1FAF408B3857EDD2DAB8338F", simCard1.getPutDescriptionKey()),
                () -> assertEquals("0E.00", simCard1.getSimProfile()),
                () -> assertEquals("BCEB92ACD3E2CCC72FB4ADDD258AD981", simCard1.getOtaSalt()),
                () -> assertTrue(simCard1.isEsim())
        );

        var simCardOpt2 = simCardRepository.findBySerialNumberAndCategory("8937710330000340296", EquipmentCategory.SIMCARD);
        assertTrue(simCardOpt2.isPresent());
        var simCard2 = simCardOpt2.get();
        assertAll(
                () -> assertEquals("8937710330000340296", simCard2.getSerialNumber()),
                () -> assertEquals("212103300034029", simCard2.getImsiNumber()),
                () -> assertEquals("208011502954029", simCard2.getImsiSponsorNumber()),
                () -> assertEquals("82089119", simCard2.getPuk1Code()),
                () -> assertEquals("74593044", simCard2.getPuk2Code()),
                () -> assertEquals("71D7CB4C9970E3E23F67338E20CF6179", simCard2.getAdminCode()),
                () -> assertEquals("2802BA0B235470FF52AFBECE21610390", simCard2.getAuthKey()),
                () -> assertEquals("CFBB662899EFB001EEECF8134C1FCA8B", simCard2.getOtaSignatureKey()),
                () -> assertEquals("104D56442868DBDBFA666E0A47B1701A", simCard2.getPutDescriptionKey()),
                () -> assertEquals("BCEB92ACD3E2CCC72FB4ADDD258AD983", simCard2.getOtaSalt()),
                () -> assertTrue(simCard2.isEsim())
        );

        assertTrue(allImportsCompleted());
    }

    @Test
    void importEsimCard_false() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "importESimCard_false.out", "text/plain",
                new ClassPathResource("data/simCard/importESimCard_false.out").getInputStream());


        mockMvc.perform(multipart("/private/auth/import/SimCard/start")
                        .file(file)
                        .param("accessType", "FREEDHOME")
                        .param("configuration", "GEMALTO")
                        .param("batchNumber", "1")
                        .param("plmnId", "1")
                        .param("preactivated", "false")
                        .param("sync", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"importStatus\": \"COMPLETED\",\"errors\": []}"));

        var simCardOpt1 = simCardRepository.findBySerialNumberAndCategory("8937710330000340288", EquipmentCategory.SIMCARD);
        assertTrue(simCardOpt1.isPresent());
        var simCard1 = simCardOpt1.get();
        assertFalse(simCard1.isEsim());
    }

    @Test
    void importESimCard_typeNotPresent() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "importESimCard_typeNotPresent.out", "text/plain",
                new ClassPathResource("data/simCard/importESimCard_typeNotPresent.out").getInputStream());


        mockMvc.perform(multipart("/private/auth/import/SimCard/start")
                        .file(file)
                        .param("accessType", "FREEDHOME")
                        .param("configuration", "GEMALTO")
                        .param("batchNumber", "1")
                        .param("plmnId", "1")
                        .param("preactivated", "false")
                        .param("sync", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"importStatus\": \"COMPLETED\",\"errors\": []}"));

        var simCardOpt1 = simCardRepository.findBySerialNumberAndCategory("8937710330000340288", EquipmentCategory.SIMCARD);
        assertTrue(simCardOpt1.isPresent());
        var simCard1 = simCardOpt1.get();
        assertFalse(simCard1.isEsim());
    }

    @Test
    void importESimCard_typeDifferentLine() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "importESimCard_differentLines.out", "text/plain",
                new ClassPathResource("data/simCard/importESimCard_differentLines.out").getInputStream());


        mockMvc.perform(multipart("/private/auth/import/SimCard/start")
                        .file(file)
                        .param("accessType", "FREEDHOME")
                        .param("configuration", "GEMALTO")
                        .param("batchNumber", "1")
                        .param("plmnId", "1")
                        .param("preactivated", "false")
                        .param("sync", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"importStatus\": \"COMPLETED\",\"errors\": []}"));

        var simCardOpt1 = simCardRepository.findBySerialNumberAndCategory("8937710330000340288", EquipmentCategory.SIMCARD);
        assertTrue(simCardOpt1.isPresent());
        var simCard1 = simCardOpt1.get();
        assertTrue(simCard1.isEsim());
    }

    @Test
    void import_idemiaOldWayByBatchNumber_success() throws Exception {
        File file = new ClassPathResource("data/simCard/TEST.out").getFile();

        when(incomingFileResolver.getImportFile("TEST.out")).thenReturn(file);


        mockMvc.perform(patch("/private/auth/simcards/import/batch/1/?sync=true")
                        .param("accessType", "FREEDHOME")
                        .param("batchNumber", "1")
                        .param("plmnId", "1")
                        .param("preactivated", "false")
                        .param("sync", "true")
                        .param("configuration", "IDEMIA_EIR"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.importStatus").value("COMPLETED"))
                .andExpect(jsonPath("$.fileName").value("TEST.out"))
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[0].error").value("mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException Serial number '893771033000000008' does not belong to batch '1'"))
                .andExpect(jsonPath("$.errors[1].error").value("mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException Serial number with checkDigit '8937710330000000092F' is invalid"));

        var simCardOpt1 = simCardRepository.findBySerialNumberAndCategory("893771033000000007", EquipmentCategory.SIMCARD);
        assertTrue(simCardOpt1.isPresent());
        var simCard1 = simCardOpt1.get();
        assertAll(
                () -> assertEquals("893771033000000007", simCard1.getSerialNumber()),
                () -> assertEquals(0, simCard1.getCheckDigit()),
                () -> assertEquals("123456789012345", simCard1.getImsiNumber()),
                () -> assertNull(simCard1.getImsiSponsorNumber()),
                () -> assertEquals("35796003368", simCard1.getNumber()),
                () -> assertEquals("58347693", simCard1.getPuk1Code()),
                () -> assertEquals("34859960", simCard1.getPuk2Code()),
                () -> assertEquals("ELEBW182OQCO9TSY", simCard1.getAdminCode()),
                () -> assertEquals("J3NLYXM3FQF9IPOD1FXAHCPH498Y51SE", simCard1.getAuthKey()),
                () -> assertEquals("1974", simCard1.getPin1Code()),
                () -> assertEquals("2815", simCard1.getPin2Code())
        );
    }

    @Test
    void import_idemiaOldWayByBatchNumber_successWithoutNumber() throws Exception {
        File file = new ClassPathResource("data/simCard/TEST_withoutNumber.out").getFile();

        when(incomingFileResolver.getImportFile("TEST.out")).thenReturn(file);

        mockMvc.perform(patch("/private/auth/simcards/import/batch/1?sync=true")
                        .param("configuration", "IDEMIA_EIR")
                        .param("accessType", "FREEDHOME"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.importStatus").value("COMPLETED"))
                .andExpect(jsonPath("$.fileName").value("TEST_withoutNumber.out"))
                .andExpect(jsonPath("$.errors", hasSize(1)));

        var simCardOpt1 = simCardRepository.findBySerialNumberAndCategory("893771033000000007", EquipmentCategory.SIMCARD);
        assertTrue(simCardOpt1.isPresent());
        var simCard1 = simCardOpt1.get();
        assertAll(
                () -> assertEquals("893771033000000007", simCard1.getSerialNumber()),
                () -> assertEquals("123456789012345", simCard1.getImsiNumber()),
                () -> assertNull(simCard1.getImsiSponsorNumber()),
                () -> assertNull(simCard1.getNumber()),
                () -> assertEquals("58347693", simCard1.getPuk1Code()),
                () -> assertEquals("34859960", simCard1.getPuk2Code()),
                () -> assertEquals("ELEBW182OQCO9TSY", simCard1.getAdminCode()),
                () -> assertEquals("J3NLYXM3FQF9IPOD1FXAHCPH498Y51SE", simCard1.getAuthKey()),
                () -> assertEquals("1974", simCard1.getPin1Code()),
                () -> assertEquals("2815", simCard1.getPin2Code())
        );
    }

    @Test
    void import_idemiaOldWayByBatchNumber_fileNotFound() throws Exception {
        // Without the last trailing slash, the test fails somehow: runtime is ok anyway
        mockMvc.perform(patch("/private/auth/simcards/import/file/TEST2.in"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\": \"Expected import file 'TEST2.in' has not been found\"}"));
    }

    @Test
    void import_idemiaOldWayByFileName_success() throws Exception {
        File file = new ClassPathResource("data/simCard/TEST.out").getFile();

        when(incomingFileResolver.getImportFile("TEST.out")).thenReturn(file);

        mockMvc.perform(patch("/private/auth/simcards/import/file/TEST.out?sync=true")
                        .param("configuration", "IDEMIA_EIR")
                        .param("accessType", "FREEDHOME"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.importStatus").value("COMPLETED"))
                .andExpect(jsonPath("$.fileName").value("TEST.out"))
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors[0].error").value("mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException Serial number '893771033000000008' does not belong to batch '1'"))
                .andExpect(jsonPath("$.errors[1].error").value("mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException Serial number with checkDigit '8937710330000000092F' is invalid"));

        var simCardOpt1 = simCardRepository.findBySerialNumberAndCategory("893771033000000007", EquipmentCategory.SIMCARD);
        assertTrue(simCardOpt1.isPresent());
        var simCard1 = simCardOpt1.get();
        assertAll(
                () -> assertEquals("893771033000000007", simCard1.getSerialNumber()),
                () -> assertEquals("123456789012345", simCard1.getImsiNumber()),
                () -> assertNull(simCard1.getImsiSponsorNumber()),
                () -> assertEquals("35796003368", simCard1.getNumber()),
                () -> assertEquals("58347693", simCard1.getPuk1Code()),
                () -> assertEquals("34859960", simCard1.getPuk2Code()),
                () -> assertEquals("ELEBW182OQCO9TSY", simCard1.getAdminCode()),
                () -> assertEquals("J3NLYXM3FQF9IPOD1FXAHCPH498Y51SE", simCard1.getAuthKey()),
                () -> assertEquals("1974", simCard1.getPin1Code()),
                () -> assertEquals("2815", simCard1.getPin2Code())
        );
    }


    @Test
    void import_idemiaThroughImporterApi_failureIfMissingBatch() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "TEST.out", "text/plain",
                new ClassPathResource("data/simCard/TEST.out").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/SimCard/start")
                        .file(file)
                        .param("accessType", "FREEDHOME")
                        .param("batchNumber", "99")
                        .param("plmnId", "1")
                        .param("preactivated", "false")
                        .param("sync", "true")
                        .param("configuration", "IDEMIA_EIR")
                        .param("batchNumber", "99"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors[0].error").value("mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException Batch with number '99' not found"))
                .andExpect(jsonPath("$.errors[1].error").value("mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException Batch with number '99' not found"))
                .andExpect(jsonPath("$.errors[2].error").value("mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException Serial number with checkDigit '8937710330000000092F' is invalid"));

        assertTrue(allImportsCompleted());
    }

    @Test
    void import_idemiaThroughImporterApi_success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "TEST.out", "text/plain",
                new ClassPathResource("data/simCard/TEST.out").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/SimCard/start")
                        .file(file)
                        .param("accessType", "FREEDHOME")
                        .param("batchNumber", "1")
                        .param("plmnId", "1")
                        .param("preactivated", "false")
                        .param("sync", "true")
                        .param("configuration", "IDEMIA_EIR")
                        .param("status", "AVAILABLE"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.importStatus").value("COMPLETED"))
                .andExpect(jsonPath("$.fileName").value("TEST.out"))
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[0].error").value("mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException Serial number '893771033000000008' does not belong to batch '1'"))
                .andExpect(jsonPath("$.errors[1].error").value("mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException Serial number with checkDigit '8937710330000000092F' is invalid"));

        var simCardOpt1 = simCardRepository.findBySerialNumberAndCategory("893573362031110002", EquipmentCategory.SIMCARD);
        assertTrue(simCardOpt1.isPresent());
        var simCard1 = simCardOpt1.get();
        assertAll(
                () -> assertEquals("893573362031110002", simCard1.getSerialNumber()),
                () -> assertEquals("280101633110002", simCard1.getImsiNumber()),
                () -> assertNull(simCard1.getImsiSponsorNumber()),
                () -> assertEquals("35796003370", simCard1.getNumber()),
                () -> assertEquals("52520069", simCard1.getPuk1Code()),
                () -> assertEquals("00517926", simCard1.getPuk2Code()),
                () -> assertEquals("508E37PNF1MYOXO0", simCard1.getAdminCode()),
                () -> assertEquals("SGL1V3XTXG6B3978Q9ZH0JF28YD27G4Y", simCard1.getAuthKey()),
                () -> assertEquals("1247", simCard1.getPin1Code()),
                () -> assertEquals("8978", simCard1.getPin2Code()),
                () -> assertEquals(Status.AVAILABLE, simCard1.getStatus())
        );

        assertTrue(allImportsCompleted());
    }

    @Test
    void import_idemiaThroughImporterApi_successWithoutBatchNumber() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "TEST.out", "text/plain",
                new ClassPathResource("data/simCard/TEST.out").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/SimCard/start")
                        .file(file)
                        .param("accessType", "FREEDHOME")
                        .param("plmnId", "1")
                        .param("preactivated", "false")
                        .param("sync", "true")
                        .param("configuration", "IDEMIA_EIR"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].error").value("mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException Serial number with checkDigit '8937710330000000092F' is invalid"));

        var simCardOpt1 = simCardRepository.findBySerialNumberAndCategory("893771033000000007", EquipmentCategory.SIMCARD);
        assertTrue(simCardOpt1.isPresent());
        var simCard1 = simCardOpt1.get();
        assertAll(
                () -> assertEquals("893771033000000007", simCard1.getSerialNumber()),
                () -> assertEquals("123456789012345", simCard1.getImsiNumber()),
                () -> assertNull(simCard1.getImsiSponsorNumber()),
                () -> assertEquals("35796003368", simCard1.getNumber()),
                () -> assertEquals("58347693", simCard1.getPuk1Code()),
                () -> assertEquals("34859960", simCard1.getPuk2Code()),
                () -> assertEquals("ELEBW182OQCO9TSY", simCard1.getAdminCode()),
                () -> assertEquals("J3NLYXM3FQF9IPOD1FXAHCPH498Y51SE", simCard1.getAuthKey()),
                () -> assertEquals("1974", simCard1.getPin1Code()),
                () -> assertEquals("2815", simCard1.getPin2Code()),
                () -> assertEquals(Status.INSTORE, simCard1.getStatus())
        );

        assertTrue(allImportsCompleted());
    }

    @Test
    void import_idemiaThroughImporterApi_successWithoutNumber() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "TEST_withoutNumber.out", "text/plain",
                new ClassPathResource("data/simCard/TEST_withoutNumber.out").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/SimCard/start")
                        .file(file)
                        .param("accessType", "FREEDHOME")
                        .param("batchNumber", "1")
                        .param("plmnId", "1")
                        .param("preactivated", "false")
                        .param("sync", "true")
                        .param("configuration", "IDEMIA_EIR"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors", hasSize(0)));

        var simCardOpt1 = simCardRepository.findBySerialNumberAndCategory("893771033000000007", EquipmentCategory.SIMCARD);
        assertTrue(simCardOpt1.isPresent());
        var simCard1 = simCardOpt1.get();
        assertAll(
                () -> assertEquals("893771033000000007", simCard1.getSerialNumber()),
                () -> assertEquals("123456789012345", simCard1.getImsiNumber()),
                () -> assertNull(simCard1.getImsiSponsorNumber()),
                () -> assertNull(simCard1.getNumber()),
                () -> assertEquals("58347693", simCard1.getPuk1Code()),
                () -> assertEquals("34859960", simCard1.getPuk2Code()),
                () -> assertEquals("ELEBW182OQCO9TSY", simCard1.getAdminCode()),
                () -> assertEquals("J3NLYXM3FQF9IPOD1FXAHCPH498Y51SE", simCard1.getAuthKey()),
                () -> assertEquals("1974", simCard1.getPin1Code()),
                () -> assertEquals("2815", simCard1.getPin2Code()),
                () -> assertEquals(Status.INSTORE, simCard1.getStatus())
        );
        assertTrue(allImportsCompleted());
    }

    @Test
    void import_gomo_success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "import_gomo.out", "text/plain",
                new ClassPathResource("data/simCard/import_gomo.out").getInputStream());


        mockMvc.perform(multipart("/private/auth/import/SimCard/start")
                        .file(file)
                        .param("accessType", "FREEDHOME")
                        .param("batchNumber", "1")
                        .param("plmnId", "1")
                        .param("preactivated", "false")
                        .param("status","AVAILABLE")
                        .param("configuration", "IDEMIA_GOMO")
                        .param("sync", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"importStatus\": \"COMPLETED\",\"errors\": []}"));

        var simCardOpt1 = simCardRepository.findBySerialNumberAndCategory("893574330522040002", EquipmentCategory.SIMCARD);
        assertTrue(simCardOpt1.isPresent());
        var simCard = simCardOpt1.get();

        assertEquals(Status.AVAILABLE, simCard.getStatus());
        assertEquals("280101334040002", simCard.getImsiNumber());
        assertEquals("3629", simCard.getPin1Code());
        assertEquals("42659463", simCard.getPuk1Code());
        assertEquals("3907", simCard.getPin2Code());
        assertEquals("72881346", simCard.getPuk2Code());
        assertEquals("86743E18163E17E8", simCard.getAdminCode());
        assertEquals("0004", simCard.getAccessControlClass());
        assertEquals(6, simCard.getCheckDigit());
        assertEquals("360BBC5267FA85FB0B15385B5615784A", simCard.getAuthKey());
    }
}
