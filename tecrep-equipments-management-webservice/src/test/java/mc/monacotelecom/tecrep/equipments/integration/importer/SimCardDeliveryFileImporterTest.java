package mc.monacotelecom.tecrep.equipments.integration.importer;

import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sql/clean.sql", "/sql/simcard_data.sql"})
class SimCardDeliveryFileImporterTest extends BaseIntegrationTest {

    @Test
    void import_success() throws Exception {

        final String testSerialNumber = "893771033000000007";
        final String testSerialNumberSecond = "893771033000000008";

        MockMultipartFile file = new MockMultipartFile("file", "deliveryFile_eir.csv", "text/plain",
                new ClassPathResource("data/simCard/deliveryFile_eir.csv").getInputStream());

        mockMvc.perform(multipart("/private/auth/import/DeliveryFile/start?sync=true")
                        .file(file))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.importStatus").value("COMPLETED"))
                .andExpect(jsonPath("$.fileName").value("deliveryFile_eir.csv"))
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors[0].line").value(3))
                .andExpect(jsonPath("$.errors[0].error").value("mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException Simcard with ICCID '893530305243254377' could not be found"))
                .andExpect(jsonPath("$.errors[1].line").value(4))
                .andExpect(jsonPath("$.errors[1].error").value("mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException Simcard with ICCID '893530305243411070' could not be found"))
                .andExpect(jsonPath("$.errors[2].line").value(5))
                .andExpect(jsonPath("$.errors[2].error").value("mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException Simcard with ICCID '893530305243411071' could not be found"));


        final SimCard simCard = simCardRepository.findBySerialNumberAndCategory(testSerialNumber, EquipmentCategory.SIMCARD).get();
        final SimCard simCard1 = simCardRepository.findBySerialNumberAndCategory(testSerialNumberSecond, EquipmentCategory.SIMCARD).get();

        Assertions.assertEquals("7600506", simCard.getOrderId());
        Assertions.assertEquals("1SIMIONFT73601297", simCard.getPackId());

        Assertions.assertEquals("7600507", simCard1.getOrderId());
        Assertions.assertEquals("1SIMIONFT7360130", simCard1.getPackId());
    }
}
