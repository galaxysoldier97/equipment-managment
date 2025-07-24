package mc.monacotelecom.tecrep.equipments.integration.importer;

import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.entity.CPE;
import mc.monacotelecom.tecrep.equipments.enums.*;
import mc.monacotelecom.tecrep.equipments.importer.data.GenericEquipementCsvLines;
import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory.ANCILLARY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Sql({"/sql/clean.sql", "/sql/cpe_data.sql"})
class CPEImportTests extends BaseIntegrationTest {

    @Nested
    @DisplayName("FTTH_Sagemcom")
    class FTTH_Sagemcom {
        @DisplayName("Import without Ancillary on-the-fly creation")
        @Test
        void import_cpe_default_ok_withCSV_noAncillary() throws Exception {
            MockMultipartFile cpesFile = new MockMultipartFile("file", "CPE_FTTH_Sagemcom.csv",
                    "text/plain", new ClassPathResource("data/cpe/CPE_FTTH_Sagemcom.csv").getInputStream());

            mockMvc.perform(multipart("/private/auth/import/CPE/start?sync=true")
                            .file(cpesFile)
                            .param("preactivated", "false")
                            .param("warehouseId", "1")
                            .param("modelName", "Toti")
                            .param("withAncillary", "false")
                            .param("configuration", "FTTH_Sagemcom")
                            .param("recyclable", "false")
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"importStatus\":\"COMPLETED\",\"fileName\":\"CPE_FTTH_Sagemcom.csv\",\"errors\":[]}"));

            Mockito.verify(cpeDefaultImporter, times(1)).onParseLine(any(GenericEquipementCsvLines.CPEFTTHCsvLine.class));
            Mockito.verify(cpeDOCSISImporter, times(0)).onParseLine(any());

            List<CPE> cpes = cpeRepository.findAll();
            assertEquals(6, cpes.size());

            var cpe = cpes.stream().filter(x -> "GFAB20500158".equals(x.getSerialNumber())).findAny().get();

            //Assert mapping after import
            assertAll(
                    () -> assertTrue(Objects.nonNull(cpe.getEquipmentId())),
                    () -> assertEquals(Status.INSTORE, cpe.getStatus()),
                    () -> assertEquals(EquipmentNature.MAIN, cpe.getNature()),
                    () -> assertFalse(cpe.getRecyclable()),
                    () -> assertFalse(cpe.getPreactivated()),
                    () -> assertEquals("GFAB20500158", cpe.getSerialNumber()),
                    () -> assertEquals("Toti", cpe.getModel().getName()),
                    () -> assertEquals("6C:99:61:1C:2C:31", cpe.getMacAddressCpe()),
                    () -> assertEquals("6C:99:61:1C:2C:37", cpe.getMacAddressLan()),
                    () -> assertEquals("6C:99:61:1C:2C:33", cpe.getMacAddressVoip()),
                    () -> assertEquals("01.0", cpe.getHwVersion()),
                    () -> assertEquals("TOTO", cpe.getWarehouse().getName()),
                    () -> assertEquals("Toto", cpe.getModel().getProvider().getName()),
                    () -> assertEquals(EquipmentCategory.CPE, cpe.getCategory()),
                    () -> assertEquals(AccessType.FTTH, cpe.getAccessType())
            );

            final Optional<AncillaryEquipment> ancillary = ancillaryRepository.findBySerialNumber("GFAB20500158");
            assertFalse(ancillary.isPresent());
        }

        @DisplayName("Import with Ancillary on-the-fly creation, failure when ancillary model not found")
        @Test
        void import_cpe_default_withAncillary_missingAncillaryModel() throws Exception {
            MockMultipartFile cpesFile = new MockMultipartFile("file", "CPE_FTTH_Sagemcom.csv",
                    "text/plain", new ClassPathResource("data/cpe/CPE_FTTH_Sagemcom.csv").getInputStream());

            mockMvc.perform(multipart("/private/auth/import/CPE/start?sync=true")
                            .file(cpesFile)
                            .param("preactivated", "false")
                            .param("warehouseId", "1")
                            .param("modelName", "Toto")
                            .param("withAncillary", "true")
                    )
                    .andDo(print())
                    .andExpect(jsonPath("$.importStatus").value("COMPLETED"))
                    .andExpect(jsonPath("$.fileName").value("CPE_FTTH_Sagemcom.csv"))
                    .andExpect(jsonPath("$.errors", hasSize(1)))
                    .andExpect(jsonPath("$.errors[0].line").value(2))
                    .andExpect(jsonPath("$.errors[0].error").value("mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException Could not find an Equipment Model with name 'Toto' for category 'ANCILLARY'"));
        }

        @DisplayName("Import with Ancillary on-the-fly creation")
        @Test
        void import_cpe_default_ok_withCSV_withAncillary() throws Exception {
            MockMultipartFile cpesFile = new MockMultipartFile("file", "CPE_FTTH_Sagemcom_withAncillary.csv",
                    "text/plain", new ClassPathResource("data/cpe/CPE_FTTH_Sagemcom_withAncillary.csv").getInputStream());

            mockMvc.perform(multipart("/private/auth/import/CPE/start?sync=true")
                            .file(cpesFile)
                            .param("preactivated", "false")
                            .param("warehouseId", "1")
                            .param("modelName", "Toti")
                            .param("configuration", "FTTH_Sagemcom")
                            .param("recyclable", "false")
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"importStatus\":\"COMPLETED\",\"fileName\":\"CPE_FTTH_Sagemcom_withAncillary.csv\",\"errors\":[]}"));

            Mockito.verify(cpeDefaultImporter, times(1)).onParseLine(any(GenericEquipementCsvLines.CPEFTTHCsvLine.class));
            Mockito.verify(cpeDOCSISImporter, times(0)).onParseLine(any());

            List<CPE> cpes = cpeRepository.findAll();
            assertEquals(6, cpes.size());

            var cpe = cpes.stream().filter(x -> "GFAB20500158".equals(x.getSerialNumber())).findAny().get();

            //Assert mapping after import
            assertAll(
                    () -> assertTrue(Objects.nonNull(cpe.getEquipmentId())),
                    () -> assertEquals(Status.INSTORE, cpe.getStatus()),
                    () -> assertEquals(EquipmentNature.MAIN, cpe.getNature()),
                    () -> assertFalse(cpe.getRecyclable()),
                    () -> assertFalse(cpe.getPreactivated()),
                    () -> assertEquals("GFAB20500158", cpe.getSerialNumber()),
                    () -> assertEquals("Toti", cpe.getModel().getName()),
                    () -> assertEquals("6C:99:61:1C:2C:31", cpe.getMacAddressCpe()),
                    () -> assertEquals("6C:99:61:1C:2C:37", cpe.getMacAddressLan()),
                    () -> assertEquals("6C:99:61:1C:2C:33", cpe.getMacAddressVoip()),
                    () -> assertEquals("01.0", cpe.getHwVersion()),
                    () -> assertEquals("TOTO", cpe.getWarehouse().getName()),
                    () -> assertEquals("Toto", cpe.getModel().getProvider().getName()),
                    () -> assertEquals(EquipmentCategory.CPE, cpe.getCategory()),
                    () -> assertEquals(AccessType.FTTH, cpe.getAccessType())
            );

            final Optional<AncillaryEquipment> ancillary = ancillaryRepository.findBySerialNumber("GFAB20500158");
            assertTrue(ancillary.isPresent());
            assertEquals("H3WY4W7WYF9C", ancillary.get().getPassword());
            assertThat(ancillary.get().getModel().getCategory()).isEqualTo(ANCILLARY);
            assertThat(ancillary.get().getModel().getName()).isEqualTo("Toti");
            assertThat(ancillary.get().getRecyclable()).isFalse();
            assertThat(ancillary.get().getEquipmentName()).isEqualTo(EquipmentName.ONT);
        }

        @DisplayName("Import with Ancillary on-the-fly creation, with Excel file")
        @Test
        void import_cpe_default_ok_withExcel() throws Exception {
            MockMultipartFile cpesFile = new MockMultipartFile("file", "CPE_DOCSIS_Sagemcom_2.xls",
                    "text/plain", new ClassPathResource("data/cpe/CPE_DOCSIS_Sagemcom_2.xls").getInputStream());

            mockMvc.perform(multipart("/private/auth/import/CPE/start?sync=true")
                            .file(cpesFile)
                            .param("preactivated", "false")
                            .param("warehouseId", "1")
                            .param("modelName", "Toti")
                            .param("configuration", "FTTH_Sagemcom")
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"importStatus\":\"COMPLETED\",\"fileName\":\"CPE_DOCSIS_Sagemcom_2.xls\",\"errors\":[]}"));

            Mockito.verify(cpeDefaultImporter, times(2)).onParseLine(any(GenericEquipementCsvLines.CPEFTTHCsvLine.class));
            Mockito.verify(cpeDOCSISImporter, times(0)).onParseLine(any());

            List<CPE> cpes = cpeRepository.findAll();
            assertEquals(7, cpes.size());

            var cpe = cpes.stream().filter(x -> "GFAB12600944".equals(x.getSerialNumber())).findAny().get();

            //Assert mapping after import
            assertTrue(Objects.nonNull(cpe.getEquipmentId()));
            assertEquals(Status.INSTORE, cpe.getStatus());
            assertEquals(EquipmentNature.MAIN, cpe.getNature());
            assertTrue(cpe.getRecyclable());
            assertFalse(cpe.getPreactivated());
            assertEquals("GFAB12600944", cpe.getSerialNumber());
            assertEquals("Toti", cpe.getModel().getName());
            assertEquals("6C:99:61:12:CB:C1", cpe.getMacAddressCpe());
            assertEquals("6C:99:61:12:CB:C7", cpe.getMacAddressLan());
            assertEquals("6C:99:61:12:CB:C3", cpe.getMacAddressVoip());
            assertEquals("01.0", cpe.getHwVersion());
            assertEquals("TOTO", cpe.getWarehouse().getName());
            assertEquals("Toto", cpe.getModel().getProvider().getName());
            assertEquals(EquipmentCategory.CPE, cpe.getCategory());
            assertEquals(AccessType.FTTH, cpe.getAccessType());
            assertEquals(false, cpe.getPreactivated());

            final Optional<AncillaryEquipment> ancillary = ancillaryRepository.findBySerialNumber("GFAB12600944");
            assertTrue(ancillary.isPresent());
            assertEquals("HKCHMFFKCQJF", ancillary.get().getPassword());
            assertThat(ancillary.get().getModel().getCategory()).isEqualTo(ANCILLARY);
            assertThat(ancillary.get().getModel().getName()).isEqualTo("Toti");
            assertThat(ancillary.get().getRecyclable()).isTrue();
        }
    }

    @Nested
    @DisplayName("Docsis_Sagemcom")
    class Docsis_Sagemcom {

        @DisplayName("Import without Ancillary on-the-fly creation")
        @Test
        void import_cpe_DOCSIS_without_ancillary_ok() throws Exception {
            MockMultipartFile cpes = new MockMultipartFile("file", "CPE_DOCSIS_Sagemcom.csv",
                    "text/plain", new ClassPathResource("data/cpe/CPE_DOCSIS_Sagemcom.csv").getInputStream());

            mockMvc.perform(multipart("/private/auth/import/CPE/start?sync=true")
                            .file(cpes)
                            .param("preactivated", "false")
                            .param("accessType", "DOCSIS")
                            .param("providerId", "1")
                            .param("warehouseId", "1")
                            .param("modelName", "Toto")
                            .param("configuration", "Docsis_Sagemcom")
                            .param("equipmentName", "HDD")
                            .param("withAncillary", "false")
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"importStatus\":\"COMPLETED\",\"fileName\":\"CPE_DOCSIS_Sagemcom.csv\",\"errors\":[]}"));

            Mockito.verify(cpeDOCSISImporter, Mockito.atLeast(1)).onParseLine(any(GenericEquipementCsvLines.CpeDOCSISCsvLine.class));
            Mockito.verify(cpeDefaultImporter, times(0)).onParseLine(any());

            Optional<CPE> cpeOpt = cpeRepository.findByCategoryAndSerialNumber(EquipmentCategory.CPE, "201912017305102620");
            assertTrue(cpeOpt.isPresent());
            var cpe = cpeOpt.get();

            //Assert mapping after import
            assertTrue(Objects.nonNull(cpe.getEquipmentId()));
            assertEquals(Status.INSTORE, cpe.getStatus());
            assertEquals(EquipmentNature.MAIN, cpe.getNature());
            assertTrue(cpe.getRecyclable());
            assertFalse(cpe.getPreactivated());
            assertEquals("201912017305102620", cpe.getSerialNumber());
            assertEquals("Toto", cpe.getModel().getName());
            assertEquals("B4:2A:0E:68:DA:04", cpe.getMacAddressCpe());
            assertEquals("B4:2A:0E:68:DA:06", cpe.getMacAddressRouter());
            assertEquals("B4:2A:0E:68:DA:05", cpe.getMacAddressVoip());
            assertEquals("CGA2121", cpe.getHwVersion());
            assertEquals("TOTO", cpe.getWarehouse().getName());
            assertEquals("Tata", cpe.getModel().getProvider().getName());
            assertEquals(EquipmentCategory.CPE, cpe.getCategory());
            assertEquals(AccessType.DOCSIS, cpe.getAccessType());
            assertEquals(false, cpe.getPreactivated());
            assertEquals("Toto", cpe.getModel().getName());

            final Optional<AncillaryEquipment> ancillary = ancillaryRepository.findBySerialNumber("201912017305102620");
            assertFalse(ancillary.isPresent());
        }

        @DisplayName("Import with Ancillary on-the-fly creation")
        @SuppressWarnings("java:S5961")
        @Test
        void import_cpe_DOCSIS_with_ancillary_ok() throws Exception {
            MockMultipartFile cpes = new MockMultipartFile("file", "CPE_DOCSIS_Sagemcom.csv",
                    "text/plain", new ClassPathResource("data/cpe/CPE_DOCSIS_Sagemcom.csv").getInputStream());

            mockMvc.perform(multipart("/private/auth/import/CPE/start?sync=true")
                            .file(cpes)
                            .param("preactivated", "false")
                            .param("withAncillary", "true")
                            .param("accessType", "DOCSIS")
                            .param("providerId", "1")
                            .param("warehouseId", "1")
                            .param("modelName", "sagem")
                            .param("configuration", "Docsis_Sagemcom")
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"importStatus\":\"COMPLETED\",\"fileName\":\"CPE_DOCSIS_Sagemcom.csv\",\"errors\":[]}"));

            Mockito.verify(cpeDOCSISImporter, Mockito.atLeast(2)).onParseLine(any(GenericEquipementCsvLines.CpeDOCSISCsvLine.class));
            Mockito.verify(cpeDefaultImporter, times(0)).onParseLine(any());

            Optional<CPE> cpeOpt = cpeRepository.findByCategoryAndSerialNumber(EquipmentCategory.CPE, "201912017305102620");
            assertTrue(cpeOpt.isPresent());
            var cpe = cpeOpt.get();

            assertAll(
                    () -> assertTrue(Objects.nonNull(cpe.getEquipmentId())),
                    () -> assertEquals(Status.INSTORE, cpe.getStatus()),
                    () -> assertEquals(EquipmentNature.MAIN, cpe.getNature()),
                    () -> assertTrue(cpe.getRecyclable()),
                    () -> assertFalse(cpe.getPreactivated()),
                    () -> assertEquals("201912017305102620", cpe.getSerialNumber()),
                    () -> assertEquals("sagem", cpe.getModel().getName()),
                    () -> assertEquals("B4:2A:0E:68:DA:04", cpe.getMacAddressCpe()),
                    () -> assertEquals("B4:2A:0E:68:DA:06", cpe.getMacAddressRouter()),
                    () -> assertEquals("B4:2A:0E:68:DA:05", cpe.getMacAddressVoip()),
                    () -> assertEquals("CGA2121", cpe.getHwVersion()),
                    () -> assertEquals("TOTO", cpe.getWarehouse().getName()),
                    () -> assertEquals("Toto", cpe.getModel().getProvider().getName()),
                    () -> assertEquals(EquipmentCategory.CPE, cpe.getCategory()),
                    () -> assertEquals(AccessType.DOCSIS, cpe.getAccessType()),
                    () -> assertEquals(false, cpe.getPreactivated()),
                    () -> assertEquals("sagem", cpe.getModel().getName())
            );

            Optional<AncillaryEquipment> ancillaryEquipmentOpt = ancillaryRepository.findBySerialNumber("484849");
            assertTrue(ancillaryEquipmentOpt.isPresent());
            var ancillaryEquipment = ancillaryEquipmentOpt.get();

            assertAll(
                    () -> assertEquals("484849", ancillaryEquipment.getSerialNumber()),
                    () -> assertEquals(EquipmentNature.MAIN, ancillaryEquipment.getNature()),
                    () -> assertTrue(ancillaryEquipment.getRecyclable()),
                    () -> assertFalse(ancillaryEquipment.getIndependent()),
                    () -> assertEquals("sagem", ancillaryEquipment.getModel().getName()),
                    () -> assertEquals("STB", ancillaryEquipment.getEquipmentName().toString()),
                    () -> assertEquals(AccessType.DOCSIS, ancillaryEquipment.getAccessType()),
                    () -> assertEquals(Status.INSTORE, ancillaryEquipment.getStatus()),
                    () -> assertEquals(EquipmentCategory.ANCILLARY.toString(), ancillaryEquipment.getCategory().toString()),
                    () -> assertThat(ancillaryEquipment.getModel().getCategory()).isEqualTo(ANCILLARY),
                    () -> assertThat(ancillaryEquipment.getModel().getName()).isEqualTo("sagem"),
                    () -> assertThat(ancillaryEquipment.getEquipmentName()).isEqualTo(EquipmentName.STB)
            );
        }

        @DisplayName("Import with Ancillary on-the-fly creation, failure for missing Ancillary model")
        @Test
        void import_cpe_DOCSIS_with_ancillary_missingAncillaryModel() throws Exception {
            MockMultipartFile cpes = new MockMultipartFile("file", "CPE_DOCSIS_Sagemcom.csv",
                    "text/plain", new ClassPathResource("data/cpe/CPE_DOCSIS_Sagemcom.csv").getInputStream());

            mockMvc.perform(multipart("/private/auth/import/CPE/start?sync=true")
                            .file(cpes)
                            .param("preactivated", "false")
                            .param("withAncillary", "true")
                            .param("accessType", "DOCSIS")
                            .param("providerId", "1")
                            .param("warehouseId", "1")
                            .param("modelName", "Toto")
                            .param("equipmentName", "ONT")
                            .param("configuration", "Docsis_Sagemcom")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.importStatus").value("COMPLETED"))
                    .andExpect(jsonPath("$.fileName").value("CPE_DOCSIS_Sagemcom.csv"))
                    .andExpect(jsonPath("$.errors", hasSize(2)))
                    .andExpect(jsonPath("$.errors[0].line").value(2))
                    .andExpect(jsonPath("$.errors[0].error").value("mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException Could not find an Equipment Model with name 'Toto' for category 'ANCILLARY'"))
                    .andExpect(jsonPath("$.errors[1].line").value(3))
                    .andExpect(jsonPath("$.errors[1].error").value("mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException Could not find an Equipment Model with name 'Toto' for category 'ANCILLARY'"));
        }
    }

    @DisplayName("Configuration unknown for CPE importer")
    @Test
    void import_cpe_unknown_configuration() throws Exception {
        MockMultipartFile cpes = new MockMultipartFile("file", "CPE_DOCSIS_Sagemcom.csv",
                "text/plain", new ClassPathResource("data/cpe/CPE_DOCSIS_Sagemcom.csv").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/CPE/start?sync=true")
                        .file(cpes)
                        .param("preactivated", "false")
                        .param("providerId", "1")
                        .param("warehouseId", "1")
                        .param("modelName", "sagem")
                        .param("configuration", "Unknown"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Could not find an importer able to process your request\"}"));

        Mockito.verify(cpeDOCSISImporter, times(0)).onParseLine(any());
        Mockito.verify(cpeDefaultImporter, times(0)).onParseLine(any());
    }
}
