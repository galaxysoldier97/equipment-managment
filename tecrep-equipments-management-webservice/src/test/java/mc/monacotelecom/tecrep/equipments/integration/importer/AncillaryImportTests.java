package mc.monacotelecom.tecrep.equipments.integration.importer;

import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentName;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.importer.data.GenericEquipementCsvLines;
import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sql/clean.sql", "/sql/ancillary_data.sql"})
class AncillaryImportTests extends BaseIntegrationTest {

    @Test
    void import_ONT_Huawei_ok() throws Exception {
        MockMultipartFile ancillaries = new MockMultipartFile("file", "ONT_Huawei.csv",
                "text/plain", new ClassPathResource("data/ancillary/ONT_Huawei.csv").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/AncillaryEquipment/start?sync=true")
                        .file(ancillaries)
                        .param("preactivated", "false")
                        .param("warehouseId", "1")
                        .param("modelName", "test")
                        .param("recyclable", "true")
                        .param("configuration", "ONT_Huawei"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"importStatus\":\"COMPLETED\",\"fileName\":\"ONT_Huawei.csv\",\"errors\":[]}"));

        Mockito.verify(ancillaryEquipmentDefaultImporter, Mockito.atLeast(1)).onParseLine(any(GenericEquipementCsvLines.AncillaryEquipmentCsvLine.class));
        Mockito.verify(ancillaryEquipmentHDDmporter, times(0)).onParseLine(any());

        //Assert mapping after import
        Optional<AncillaryEquipment> ancillaryEquipment = ancillaryRepository.findBySerialNumber("2102312LQP6RKA000023");
        Assertions.assertTrue(ancillaryEquipment.isPresent());
        assertAll(
                () -> Assertions.assertEquals("E0:00:84:36:34:0B", ancillaryEquipment.get().getMacAddress()),
                () -> Assertions.assertEquals("test", ancillaryEquipment.get().getModel().getName()),
                () -> Assertions.assertEquals("V300R019C20SPC030", ancillaryEquipment.get().getSfpVersion()),
                () -> Assertions.assertEquals("2102312LQP6RKA000023", ancillaryEquipment.get().getSerialNumber()),
                () -> Assertions.assertEquals("test", ancillaryEquipment.get().getModel().getName()),
                () -> Assertions.assertEquals(Status.INSTORE, ancillaryEquipment.get().getStatus()),
                () -> Assertions.assertEquals(AccessType.DOCSIS, ancillaryEquipment.get().getAccessType())
        );
    }

    @Test
    void import_HDD_Sagemcom_ok() throws Exception {
        MockMultipartFile ancillaries = new MockMultipartFile("file", "HDD_Sagemcom.csv",
                "text/plain", new ClassPathResource("data/ancillary/HDD_Sagemcom.csv").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/AncillaryEquipment/start?sync=true")
                        .file(ancillaries)
                        .param("preactivated", "false")
                        .param("warehouseId", "1")
                        .param("modelName", "HDD500")
                        .param("recyclable", "true")
                        .param("configuration", "HDD_Sagemcom")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"importStatus\":\"COMPLETED\",\"fileName\":\"HDD_Sagemcom.csv\",\"errors\":[]}"));

        Mockito.verify(ancillaryEquipmentHDDmporter, Mockito.atLeast(1)).onParseLine(any(GenericEquipementCsvLines.AncillaryEquipmentHDDSagemcomsvLine.class));
        Mockito.verify(ancillaryEquipmentDefaultImporter, times(0)).onParseLine(any());


        //Assert mapping after import
        Optional<AncillaryEquipment> ancillaryEquipment = ancillaryRepository.findBySerialNumber("116039000000");
        Assertions.assertTrue(ancillaryEquipment.isPresent());
        assertAll(
                () -> Assertions.assertEquals("HDD500", ancillaryEquipment.get().getModel().getName()),
                () -> Assertions.assertEquals("WXE1A952L03P", ancillaryEquipment.get().getExternalNumber()),
                () -> Assertions.assertEquals("PF16021CER83432", ancillaryEquipment.get().getBatchNumber()),
                () -> Assertions.assertEquals(Status.INSTORE, ancillaryEquipment.get().getStatus()),
                () -> Assertions.assertEquals(AccessType.DOCSIS, ancillaryEquipment.get().getAccessType())
        );
    }

    @Test
    void import_ancillary_unknown_configuration() throws Exception {
        MockMultipartFile ancillaries = new MockMultipartFile("file", "HDD_Sagecom.csv",
                "text/plain", new ClassPathResource("data/ancillary/HDD_Sagemcom.csv").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/AncillaryEquipment/start?sync=true")
                        .file(ancillaries)
                        .param("preactivated", "false")
                        .param("warehouseId", "1")
                        .param("modelName", "sagem")
                        .param("configuration", "Unknown"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Could not find an importer able to process your request\"}"));

        Mockito.verify(ancillaryEquipmentHDDmporter, times(0)).onParseLine(any());
        Mockito.verify(ancillaryEquipmentDefaultImporter, times(0)).onParseLine(any());
    }

    @Test
    void import_STB_Skyworth_ancillary_ok() throws Exception {
        MockMultipartFile ancillaries = new MockMultipartFile("file", "STB_Skyworth.xlsx",
                "text/plain", new ClassPathResource("data/ancillary/STB_Skyworth.xlsx").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/AncillaryEquipment/start?sync=true")
                        .file(ancillaries)
                        .param("preactivated", "false")
                        .param("warehouseId", "1")
                        .param("recyclable", "true")
                        .param("modelName", "sagem")
                        .param("configuration", "STB_Skyworth")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"importStatus\":\"COMPLETED\",\"fileName\":\"STB_Skyworth.xlsx\",\"errors\":[]}"));

        Mockito.verify(ancillaryEquipmentSTBSegemcomImporter, Mockito.times(2)).onParseLine(any(GenericEquipementCsvLines.AncillaryEquipmentSTBCsvLine.class));
        Mockito.verify(ancillaryEquipmentHDDmporter, times(0)).onParseLine(any());
        Mockito.verify(ancillaryEquipmentDefaultImporter, times(0)).onParseLine(any());

        //Assert mapping after import
        Optional<AncillaryEquipment> ancillaryEquipment = ancillaryRepository.findBySerialNumber("140118112139360");
        Assertions.assertTrue(ancillaryEquipment.isPresent());
        assertAll(
                () -> Assertions.assertNull(ancillaryEquipment.get().getMacAddress()),
                () -> Assertions.assertEquals("sagem", ancillaryEquipment.get().getModel().getName()),
                () -> Assertions.assertNull(ancillaryEquipment.get().getSfpVersion()),
                () -> Assertions.assertEquals("140118112139360", ancillaryEquipment.get().getSerialNumber()),
                () -> Assertions.assertEquals(Status.INSTORE, ancillaryEquipment.get().getStatus()),
                () -> Assertions.assertEquals(AccessType.DOCSIS, ancillaryEquipment.get().getAccessType()),
                () -> Assertions.assertEquals(EquipmentName.STB, ancillaryEquipment.get().getEquipmentName()),
                () -> Assertions.assertFalse(ancillaryEquipment.get().getPreactivated())
        );

        Optional<AncillaryEquipment> ancillaryEquipment2 = ancillaryRepository.findBySerialNumber("140118112135522");
        Assertions.assertTrue(ancillaryEquipment2.isPresent());
        assertAll(
                () -> Assertions.assertNull(ancillaryEquipment2.get().getMacAddress()),
                () -> Assertions.assertEquals("sagem", ancillaryEquipment2.get().getModel().getName()),
                () -> Assertions.assertNull(ancillaryEquipment2.get().getSfpVersion()),
                () -> Assertions.assertEquals("140118112135522", ancillaryEquipment2.get().getSerialNumber()),
                () -> Assertions.assertEquals(Status.INSTORE, ancillaryEquipment2.get().getStatus()),
                () -> Assertions.assertEquals(AccessType.DOCSIS, ancillaryEquipment2.get().getAccessType()),
                () -> Assertions.assertEquals(EquipmentName.STB, ancillaryEquipment.get().getEquipmentName()),
                () -> Assertions.assertFalse(ancillaryEquipment2.get().getPreactivated())
        );
    }

    @Test
    void import_STB_Skyworth_available() throws Exception {
        MockMultipartFile ancillaries = new MockMultipartFile("file", "STB_Skyworth.xlsx",
                "text/plain", new ClassPathResource("data/ancillary/STB_Skyworth.xlsx").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/AncillaryEquipment/start?sync=true")
                        .file(ancillaries)
                        .param("warehouseId", "1")
                        .param("recyclable", "true")
                        .param("modelName", "sagem")
                        .param("configuration", "STB_Skyworth")
                        .param("status", "available")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"importStatus\":\"COMPLETED\",\"fileName\":\"STB_Skyworth.xlsx\",\"errors\":[]}"));

        Mockito.verify(ancillaryEquipmentSTBSegemcomImporter, Mockito.times(2)).onParseLine(any(GenericEquipementCsvLines.AncillaryEquipmentSTBCsvLine.class));
        Mockito.verify(ancillaryEquipmentHDDmporter, times(0)).onParseLine(any());
        Mockito.verify(ancillaryEquipmentDefaultImporter, times(0)).onParseLine(any());

        //Assert mapping after import
        Optional<AncillaryEquipment> ancillaryEquipment = ancillaryRepository.findBySerialNumber("140118112139360");
        Assertions.assertTrue(ancillaryEquipment.isPresent());
        assertAll(
                () -> Assertions.assertNull(ancillaryEquipment.get().getMacAddress()),
                () -> Assertions.assertEquals("sagem", ancillaryEquipment.get().getModel().getName()),
                () -> Assertions.assertNull(ancillaryEquipment.get().getSfpVersion()),
                () -> Assertions.assertEquals("140118112139360", ancillaryEquipment.get().getSerialNumber()),
                () -> Assertions.assertEquals(Status.AVAILABLE, ancillaryEquipment.get().getStatus()),
                () -> Assertions.assertEquals(AccessType.DOCSIS, ancillaryEquipment.get().getAccessType()),
                () -> Assertions.assertEquals(EquipmentName.STB, ancillaryEquipment.get().getEquipmentName()),
                () -> Assertions.assertFalse(ancillaryEquipment.get().getPreactivated())
        );

        Optional<AncillaryEquipment> ancillaryEquipment2 = ancillaryRepository.findBySerialNumber("140118112135522");
        Assertions.assertTrue(ancillaryEquipment2.isPresent());
        assertAll(
                () -> Assertions.assertNull(ancillaryEquipment2.get().getMacAddress()),
                () -> Assertions.assertEquals("sagem", ancillaryEquipment2.get().getModel().getName()),
                () -> Assertions.assertNull(ancillaryEquipment2.get().getSfpVersion()),
                () -> Assertions.assertEquals("140118112135522", ancillaryEquipment2.get().getSerialNumber()),
                () -> Assertions.assertEquals(Status.AVAILABLE, ancillaryEquipment2.get().getStatus()),
                () -> Assertions.assertEquals(AccessType.DOCSIS, ancillaryEquipment2.get().getAccessType()),
                () -> Assertions.assertEquals(EquipmentName.STB, ancillaryEquipment.get().getEquipmentName()),
                () -> Assertions.assertFalse(ancillaryEquipment2.get().getPreactivated())
        );
    }

    @Test
    void import_STB_Skyworth_invalidStatus() throws Exception {
        MockMultipartFile ancillaries = new MockMultipartFile("file", "STB_Skyworth.xlsx",
                "text/plain", new ClassPathResource("data/ancillary/STB_Skyworth.xlsx").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/AncillaryEquipment/start?sync=true")
                        .file(ancillaries)
                        .param("preactivated", "false")
                        .param("warehouseId", "1")
                        .param("recyclable", "true")
                        .param("modelName", "sagem")
                        .param("configuration", "STB_Skyworth")
                        .param("status", "activated")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"importStatus\":\"COMPLETED\",\"fileName\":\"STB_Skyworth.xlsx\",\"errors\":[{\"line\":2,\"error\":\"mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException Invalid status 'ACTIVATED', should be AVAILABLE or INSTORE\"},{\"line\":3,\"error\":\"mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException Invalid status 'ACTIVATED', should be AVAILABLE or INSTORE\"}]}"));

        Mockito.verify(ancillaryEquipmentSTBSegemcomImporter, Mockito.times(2)).onParseLine(any(GenericEquipementCsvLines.AncillaryEquipmentSTBCsvLine.class));
        Mockito.verify(ancillaryEquipmentHDDmporter, times(0)).onParseLine(any());
        Mockito.verify(ancillaryEquipmentDefaultImporter, times(0)).onParseLine(any());

        //Assert mapping after import
        Optional<AncillaryEquipment> ancillaryEquipment = ancillaryRepository.findBySerialNumber("140118112139360");
        Assertions.assertFalse(ancillaryEquipment.isPresent());

        Optional<AncillaryEquipment> ancillaryEquipment2 = ancillaryRepository.findBySerialNumber("140118112135522");
        Assertions.assertFalse(ancillaryEquipment2.isPresent());
    }

    @Test
    void import_ONT_Genexis() throws Exception {
        MockMultipartFile ancillaries = new MockMultipartFile("file", "ONT_Genexis",
                "text/plain", new ClassPathResource("data/ancillary/ONT_Genexis.csv").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/AncillaryEquipment/start?sync=true")
                        .file(ancillaries)
                        .param("preactivated", "false")
                        .param("warehouseId", "1")
                        .param("recyclable", "true")
                        .param("modelName", "test")
                        .param("status", "AVAILABLE")
                        .param("configuration", "ONT_Genexis")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"importStatus\":\"COMPLETED\",\"fileName\":\"ONT_Genexis\",\"errors\":[]}\n"));

        Mockito.verify(ancillaryEquipmentONTGenexisImporter, Mockito.times(3)).onParseLine(any(GenericEquipementCsvLines.AncillaryONTGenexisCsvLine.class));
        Mockito.verify(ancillaryEquipmentDefaultImporter, times(0)).onParseLine(any());

        //Assert mapping after import
        Optional<AncillaryEquipment> ancillaryEquipment = ancillaryRepository.findBySerialNumber("GNXS050580F8");
        Assertions.assertTrue(ancillaryEquipment.isPresent());

        Optional<AncillaryEquipment> ancillaryEquipment2 = ancillaryRepository.findBySerialNumber("GNXS050581CE");
        Assertions.assertTrue(ancillaryEquipment2.isPresent());

        assertAll(
                () -> Assertions.assertEquals("58:00:32:05:81:CE", ancillaryEquipment2.get().getMacAddress()),
                () -> Assertions.assertEquals("test", ancillaryEquipment2.get().getModel().getName()),
                () -> Assertions.assertEquals("E-5.3.0-R", ancillaryEquipment2.get().getSfpVersion()),
                () -> Assertions.assertEquals("GNXS050581CE", ancillaryEquipment2.get().getSerialNumber()),
                () -> Assertions.assertEquals("107476", ancillaryEquipment2.get().getBatchNumber()),
                () -> Assertions.assertEquals(Status.AVAILABLE, ancillaryEquipment2.get().getStatus()),
                () -> Assertions.assertEquals(AccessType.DOCSIS, ancillaryEquipment2.get().getAccessType()),
                () -> Assertions.assertEquals(EquipmentName.ONT, ancillaryEquipment.get().getEquipmentName()),
                () -> Assertions.assertFalse(ancillaryEquipment2.get().getPreactivated()));
    }

    @Test
    void importer_modelName_mandatory() throws Exception {
        MockMultipartFile ancillaries = new MockMultipartFile("file", "ONT_Genexis",
                "text/plain", new ClassPathResource("data/ancillary/ONT_Genexis.csv").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/AncillaryEquipment/start?sync=true")
                        .file(ancillaries)
                        .param("preactivated", "false")
                        .param("warehouseId", "1")
                        .param("recyclable", "true")
                        .param("status", "AVAILABLE")
                        .param("configuration", "ONT_Genexis")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"importStatus\":\"COMPLETED\",\"fileName\":\"ONT_Genexis\",\"errors\":[{\"id\":1,\"line\":2,\"error\":\"mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException Missing modelName in request\",\"group\":\"sheet1\"},{\"id\":2,\"line\":3,\"error\":\"mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException Missing modelName in request\",\"group\":\"sheet1\"},{\"id\":3,\"line\":4,\"error\":\"mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException Missing modelName in request\",\"group\":\"sheet1\"}]}\n"));
    }
}
