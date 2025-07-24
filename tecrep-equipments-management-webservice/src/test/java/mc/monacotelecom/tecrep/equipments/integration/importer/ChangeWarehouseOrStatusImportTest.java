package mc.monacotelecom.tecrep.equipments.integration.importer;

import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sql/clean.sql", "/sql/cpe_data.sql"})
class ChangeWarehouseOrStatusImportTest extends BaseIntegrationTest {

    @Test
    void import_withSerialNumber_success() throws Exception {
        mockMvc.perform(multipart("/private/auth/import/ChangeWarehouseOrStatus/start?selection=SERIAL_NUMBER&category=CPE&startRange=8937710330000000008&finalRange=8937710330000000009&event=BOOK&name=TATA&sync=true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"importStatus\":\"COMPLETED\",\"fileName\":\"ChangeWarehouseOrStatus\",\"errors\":[{\"id\":1,\"line\":0,\"error\":\"Cannot apply event 'book' on Ancillary Equipment with status 'ASSIGNED' and Serial Number '8937710330000000009'\",\"group\":null}]}\n"));

        var cpe1 = cpeRepository.findBySerialNumber("8937710330000000007").get();
        assertEquals(Status.BOOKED, cpe1.getStatus(), "CPE 8937710330000000007 should have been left as BOOKED");
        assertEquals("TATA", cpe1.getWarehouse().getName(), "CPE 8937710330000000007 should have been left with warehouse TATA");

        var cpe2 = cpeRepository.findBySerialNumber("8937710330000000008").get();
        assertEquals(Status.BOOKED, cpe2.getStatus(), "CPE 8937710330000000008 should now be BOOKED");
        assertEquals("TATA", cpe2.getWarehouse().getName(), "CPE 8937710330000000008 should now have warehouse TATA");

        var cpe3 = cpeRepository.findBySerialNumber("8937710330000000009").get();
        assertEquals(Status.ASSIGNED, cpe3.getStatus(), "CPE 8937710330000000009 should have been left as DEACTIVATED");
        assertEquals("TOTO", cpe3.getWarehouse().getName(), "CPE 8937710330000000009 should have been left with warehouse TOTO");

        var cpe4 = cpeRepository.findBySerialNumber("8937710330000000010").get();
        assertEquals(Status.DEPRECATED, cpe4.getStatus(), "CPE 8937710330000000010 should have been left as BOOKED");
        assertEquals("TOTO", cpe4.getWarehouse().getName(), "CPE 8937710330000000010 should have been left with warehouse TOTO");

        assertTrue(allImportsCompleted());
    }

    @Test
    void import_withSerialNumberAndEventUppercase_success() throws Exception {
        mockMvc.perform(multipart("/private/auth/import/ChangeWarehouseOrStatus/start?selection=SERIAL_NUMBER&category=CPE&startRange=8937710330000000008&finalRange=8937710330000000009&event=BOOK&name=TATA&sync=true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"importStatus\":\"COMPLETED\",\"fileName\":\"ChangeWarehouseOrStatus\",\"errors\":[{\"id\":1,\"line\":0,\"error\":\"Cannot apply event 'book' on Ancillary Equipment with status 'ASSIGNED' and Serial Number '8937710330000000009'\",\"group\":null}]}\n"));

        var cpe1 = cpeRepository.findBySerialNumber("8937710330000000007").get();
        assertEquals(Status.BOOKED, cpe1.getStatus(), "CPE 8937710330000000007 should have been left as BOOKED");
        assertEquals("TATA", cpe1.getWarehouse().getName(), "CPE 8937710330000000007 should have been left with warehouse TATA");

        var cpe2 = cpeRepository.findBySerialNumber("8937710330000000008").get();
        assertEquals(Status.BOOKED, cpe2.getStatus(), "CPE 8937710330000000008 should now be BOOKED");
        assertEquals("TATA", cpe2.getWarehouse().getName(), "CPE 8937710330000000008 should now have warehouse TATA");

        var cpe3 = cpeRepository.findBySerialNumber("8937710330000000009").get();
        assertEquals(Status.ASSIGNED, cpe3.getStatus(), "CPE 8937710330000000009 should have been left as DEACTIVATED");
        assertEquals("TOTO", cpe3.getWarehouse().getName(), "CPE 8937710330000000009 should have been left with warehouse TOTO");

        var cpe4 = cpeRepository.findBySerialNumber("8937710330000000010").get();
        assertEquals(Status.DEPRECATED, cpe4.getStatus(), "CPE 8937710330000000010 should have been left as BOOKED");
        assertEquals("TOTO", cpe4.getWarehouse().getName(), "CPE 8937710330000000010 should have been left with warehouse TOTO");

        assertTrue(allImportsCompleted());
    }

    @Test
    void import_withFile_success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "ChangeWarehouseOrStatus.csv", "text/plain",
                new ClassPathResource("data/changeWarehouseOrStatus/ChangeWarehouseOrStatus.csv").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/ChangeWarehouseOrStatus/start?selection=FILE&category=CPE&event=book&name=TATA&sync=true")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"importStatus\":\"COMPLETED\",\"fileName\":\"ChangeWarehouseOrStatus.csv\",\"errors\":[{\"id\":1,\"line\":0,\"error\":\"Cannot apply event 'book' on Ancillary Equipment with status 'ASSIGNED' and Serial Number '8937710330000000009'\",\"group\":null}]}\n"));

        var cpe1 = cpeRepository.findBySerialNumber("8937710330000000007").get();
        assertEquals(Status.BOOKED, cpe1.getStatus(), "CPE 8937710330000000007 should have been left as BOOKED");
        assertEquals("TATA", cpe1.getWarehouse().getName(), "CPE 8937710330000000007 should have been left with warehouse TATA");

        var cpe2 = cpeRepository.findBySerialNumber("8937710330000000008").get();
        assertEquals(Status.BOOKED, cpe2.getStatus(), "CPE 8937710330000000008 should now be BOOKED");
        assertEquals("TATA", cpe2.getWarehouse().getName(), "CPE 8937710330000000008 should now have warehouse TATA");

        var cpe3 = cpeRepository.findBySerialNumber("8937710330000000009").get();
        assertEquals(Status.ASSIGNED, cpe3.getStatus(), "CPE 8937710330000000009 should have been left as DEACTIVATED");
        assertEquals("TOTO", cpe3.getWarehouse().getName(), "CPE 8937710330000000009 should have been left with warehouse TOTO");

        var cpe4 = cpeRepository.findBySerialNumber("8937710330000000010").get();
        assertEquals(Status.DEPRECATED, cpe4.getStatus(), "CPE 8937710330000000010 should have been left as BOOKED");
        assertEquals("TOTO", cpe4.getWarehouse().getName(), "CPE 8937710330000000010 should have been left with warehouse TOTO");

        assertTrue(allImportsCompleted());
    }

    @Test
    void import_withFile_withStatusError_success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "ChangeWarehouseOrStatus_withError.csv", "text/plain",
                new ClassPathResource("data/changeWarehouseOrStatus/ChangeWarehouseOrStatus_withError.csv").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/ChangeWarehouseOrStatus/start?selection=FILE&category=CPE&event=book&name=TATA&sync=true")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"importStatus\":\"COMPLETED\",\"fileName\":\"ChangeWarehouseOrStatus_withError.csv\",\"errors\":[{\"id\":1,\"line\":0,\"error\":\"Cannot apply event 'book' on Ancillary Equipment with status 'DEPRECATED' and Serial Number '8937710330000000010'\",\"group\":null}]}\n"));

        var cpe1 = cpeRepository.findBySerialNumber("8937710330000000007").get();
        assertEquals(Status.BOOKED, cpe1.getStatus(), "CPE 8937710330000000007 should have been left as BOOKED");
        assertEquals("TATA", cpe1.getWarehouse().getName(), "CPE 8937710330000000007 should have been left with warehouse TATA");

        var cpe2 = cpeRepository.findBySerialNumber("8937710330000000010").get();
        assertEquals(Status.DEPRECATED, cpe2.getStatus(), "CPE 8937710330000000010 should have been left as DEPRECATED");
        assertEquals("TOTO", cpe2.getWarehouse().getName(), "CPE 8937710330000000010 should have been left with warehouse TOTO");
    }
}
