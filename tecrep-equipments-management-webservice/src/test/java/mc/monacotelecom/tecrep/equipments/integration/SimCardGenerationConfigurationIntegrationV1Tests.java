package mc.monacotelecom.tecrep.equipments.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({"/sql/clean.sql", "/sql/simcard_generation_configuration_data.sql"})
class SimCardGenerationConfigurationIntegrationV1Tests extends BaseIntegrationTest {
    final String baseUrl = "/private/auth/simGenerationConfigurations";

    @Test
    void search_simCardGenerationConfigurationCRUD_withNameReturned() throws Exception {
        mockMvc.perform(get(baseUrl + "?name=EIRCOM_FCS_MOB_LANDLINE_UNPAIR"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[{\"name\":\"EIRCOM_FCS_MOB_LANDLINE_UNPAIR\",\"exportFileConfiguration\":{\"name\":\"default_export\",\"prefix\":\"MMC\",\"suffix\":\".inp\",\"headerFormat\":\"<div>${CONTENT3}</div>\",\"recordFormat\":null},\"importFileConfiguration\":{\"name\":\"default_import\",\"prefix\":\"MMC\",\"suffix\":\".out\",\"headerFormat\":null,\"recordFormat\":null},\"transportKey\":\"6\",\"artwork\":\"EIR_SME_REP\",\"simReference\":null,\"algorithmVersion\":1,\"plmn\":{\"plmnId\":1,\"code\":\"27203\",\"networkName\":\"Meteor Mobile Telecommunications Limited\",\"tadigCode\":\"IRLME\",\"countryIsoCode\":\"IRL\",\"countryName\":\"Ireland\",\"rangesPrefix\":\"3368900890401\"},\"msinSequence\":\"DEFAULT\",\"iccidSequence\":\"DEFAULT\",\"notify\":\"test@test.test\"}],\"pageable\":{\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"pageSize\":20,\"pageNumber\":0,\"offset\":0,\"paged\":true,\"unpaged\":false},\"totalPages\":1,\"totalElements\":1,\"last\":true,\"number\":0,\"size\":20,\"numberOfElements\":1,\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"first\":true,\"empty\":false}"));
    }

    @Test
    void search_simCardGenerationConfigurationCRUD_withNameNone() throws Exception {
        mockMvc.perform(get(baseUrl + "?name=MISSING"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"content\":[],\"pageable\":{\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"pageSize\":20,\"pageNumber\":0,\"offset\":0,\"paged\":true,\"unpaged\":false},\"totalPages\":0,\"totalElements\":0,\"last\":true,\"number\":0,\"size\":20,\"numberOfElements\":0,\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"first\":true,\"empty\":true}"));
    }

    @Test
    void getByName_success() throws Exception {
        mockMvc.perform(get(baseUrl + "/EIRCOM_FCS_MOB_LANDLINE_UNPAIR"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\":\"EIRCOM_FCS_MOB_LANDLINE_UNPAIR\",\"exportFileConfiguration\":{\"name\":\"default_export\",\"prefix\":\"MMC\",\"suffix\":\".inp\",\"headerFormat\":\"<div>${CONTENT3}</div>\",\"recordFormat\":null},\"importFileConfiguration\":{\"name\":\"default_import\",\"prefix\":\"MMC\",\"suffix\":\".out\",\"headerFormat\":null,\"recordFormat\":null},\"transportKey\":\"6\",\"artwork\":\"EIR_SME_REP\",\"simReference\":null,\"algorithmVersion\":1,\"plmn\":{\"plmnId\":1,\"code\":\"27203\",\"networkName\":\"Meteor Mobile Telecommunications Limited\",\"tadigCode\":\"IRLME\",\"countryIsoCode\":\"IRL\",\"countryName\":\"Ireland\",\"rangesPrefix\":\"3368900890401\"},\"msinSequence\":\"DEFAULT\",\"iccidSequence\":\"DEFAULT\",\"notify\":\"test@test.test\"}"));
    }

    @Test
    void getByName_missing() throws Exception {
        mockMvc.perform(get(baseUrl + "/MISSING"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"SIM Card generation configuration with name 'MISSING' not found\"}"));
    }

    @Test
    void delete_success() throws Exception {
        mockMvc.perform(delete(baseUrl + "/EIRCOM_FCS_MOB_LANDLINE_UNPAIR"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_missing() throws Exception {
        mockMvc.perform(delete(baseUrl + "/MISSING"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"SIM Card generation configuration with name 'MISSING' not found\"}"));
    }

    @Test
    void delete_attachedToBatch() throws Exception {
        mockMvc.perform(delete(baseUrl + "/ANOTHER"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"SIM Card generation configuration with name 'ANOTHER' cannot be deleted because it is referenced by batch '6'\"}"));
    }

    @Test
    void create_success() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"new_scgc\", \"exportFileConfiguration\":{\"name\":\"default_export\"}, \"importFileConfiguration\":{\"name\":\"default_import\"}, \"transportKey\":\"1\",\"algorithmVersion\":4,\"plmn\":{ \"code\": \"27203\"},\"msinSequence\":\"DEFAULT\",\"iccidSequence\":\"SOMETHING\",\"artwork\":\"EIR_SME_REP\",\"simReference\":\"MET_PP001_LTE\", \"type\": \"UNIVERSAL\", \"fixedPrefix\": \"04563217\", \"sequencePrefix\": \"05478\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"name\":\"new_scgc\",\"exportFileConfiguration\":{\"name\":\"default_export\",\"prefix\":\"MMC\",\"suffix\":\".inp\",\"headerFormat\":\"<div>${CONTENT3}</div>\",\"recordFormat\":null},\"importFileConfiguration\":{\"name\":\"default_import\",\"prefix\":\"MMC\",\"suffix\":\".out\",\"headerFormat\":null,\"recordFormat\":null},\"transportKey\":\"1\",\"artwork\":\"EIR_SME_REP\",\"simReference\":\"MET_PP001_LTE\",\"type\":\"UNIVERSAL\",\"fixedPrefix\":\"04563217\",\"sequencePrefix\":\"05478\",\"algorithmVersion\":4,\"plmn\":{\"plmnId\":1,\"code\":\"27203\",\"networkName\":\"Meteor Mobile Telecommunications Limited\",\"tadigCode\":\"IRLME\",\"countryIsoCode\":\"IRL\",\"countryName\":\"Ireland\",\"rangesPrefix\":\"3368900890401\",\"links\":[]},\"msinSequence\":\"DEFAULT\",\"iccidSequence\":\"SOMETHING\",\"notify\":\"test@test.test\"}"));
    }

    @Test
    void create_successNoPrefixes() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"new_scgc\", \"exportFileConfiguration\":{\"name\":\"default_export\"}, \"importFileConfiguration\":{\"name\":\"default_import\"}, \"transportKey\":\"1\",\"algorithmVersion\":4,\"plmn\":{ \"code\": \"27203\"},\"msinSequence\":\"DEFAULT\",\"iccidSequence\":\"SOMETHING\",\"artwork\":\"EIR_SME_REP\",\"simReference\":\"MET_PP001_LTE\", \"type\": \"UNIVERSAL\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"name\":\"new_scgc\",\"exportFileConfiguration\":{\"name\":\"default_export\",\"prefix\":\"MMC\",\"suffix\":\".inp\",\"headerFormat\":\"<div>${CONTENT3}</div>\",\"recordFormat\":null},\"importFileConfiguration\":{\"name\":\"default_import\",\"prefix\":\"MMC\",\"suffix\":\".out\",\"headerFormat\":null,\"recordFormat\":null},\"transportKey\":\"1\",\"artwork\":\"EIR_SME_REP\",\"simReference\":\"MET_PP001_LTE\",\"type\":\"UNIVERSAL\",\"fixedPrefix\":null,\"sequencePrefix\":null,\"algorithmVersion\":4,\"plmn\":{\"plmnId\":1,\"code\":\"27203\",\"networkName\":\"Meteor Mobile Telecommunications Limited\",\"tadigCode\":\"IRLME\",\"countryIsoCode\":\"IRL\",\"countryName\":\"Ireland\",\"rangesPrefix\":\"3368900890401\",\"links\":[]},\"msinSequence\":\"DEFAULT\",\"iccidSequence\":\"SOMETHING\",\"notify\":\"test@test.test\"}"));
    }

    @Test
    void create_failureIfFixedPrefixNotNumeric() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"new_scgc\", \"exportFileConfiguration\":{\"name\":\"default_export\"}, \"importFileConfiguration\":{\"name\":\"default_import\"}, \"transportKey\":\"1\",\"algorithmVersion\":4,\"plmn\":{ \"code\": \"27203\"},\"msinSequence\":\"DEFAULT\",\"iccidSequence\":\"SOMETHING\",\"artwork\":\"EIR_SME_REP\",\"simReference\":\"MET_PP001_LTE\", \"type\": \"UNIVERSAL\", \"fixedPrefix\": \"ABC\", \"sequencePrefix\": \"5478\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"Fixed prefix 'ABC' must be numeric\"}"));
    }

    @Test
    void create_failureIfSequencePrefixNotNumeric() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"new_scgc\", \"exportFileConfiguration\":{\"name\":\"default_export\"}, \"importFileConfiguration\":{\"name\":\"default_import\"}, \"transportKey\":\"1\",\"algorithmVersion\":4,\"plmn\":{ \"code\": \"27203\"},\"msinSequence\":\"DEFAULT\",\"iccidSequence\":\"SOMETHING\",\"artwork\":\"EIR_SME_REP\",\"simReference\":\"MET_PP001_LTE\", \"type\": \"UNIVERSAL\", \"fixedPrefix\": \"4563217\", \"sequencePrefix\": \"ABC\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"Sequence prefix 'ABC' must be numeric\"}"));
    }

    @Test
    void create_successNoPlmn() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"new_scgc\", \"exportFileConfiguration\":{\"name\":\"default_export\"}, \"importFileConfiguration\":{\"name\":\"default_import\"}, \"transportKey\":\"1\",\"algorithmVersion\":4,\"msinSequence\":\"DEFAULT\",\"iccidSequence\":\"SOMETHING\", \"type\": \"UNIVERSAL\", \"fixedPrefix\": \"4563217\", \"sequencePrefix\": \"5478\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"name\":\"new_scgc\",\"exportFileConfiguration\":{\"name\":\"default_export\",\"prefix\":\"MMC\",\"suffix\":\".inp\",\"headerFormat\":\"<div>${CONTENT3}</div>\",\"recordFormat\":null},\"importFileConfiguration\":{\"name\":\"default_import\",\"prefix\":\"MMC\",\"suffix\":\".out\",\"headerFormat\":null,\"recordFormat\":null},\"transportKey\":\"1\",\"artwork\":null,\"simReference\":null,\"type\":\"UNIVERSAL\",\"fixedPrefix\":\"4563217\",\"sequencePrefix\":\"5478\",\"algorithmVersion\":4,\"plmn\":null,\"msinSequence\":\"DEFAULT\",\"iccidSequence\":\"SOMETHING\",\"notify\":\"test@test.test\"}"));
    }

    @Test
    void create_missingImportFileConfig() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"new_scgc\", \"exportFileConfiguration\":{\"name\":\"default_export\"}, \"importFileConfiguration\":{\"name\":\"missing\"}, \"type\": \"UNIVERSAL\", \"fixedPrefix\": \"4563217\", \"sequencePrefix\": \"5478\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"File configuration with name 'missing' could not be found\"}"));
    }

    @Test
    void create_missingExportFileConfig() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"new_scgc\", \"exportFileConfiguration\":{\"name\":\"missing\"}, \"importFileConfiguration\":{\"name\":\"default_import\"}, \"type\": \"UNIVERSAL\", \"fixedPrefix\": \"4563217\", \"sequencePrefix\": \"5478\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"File configuration with name 'missing' could not be found\"}"));
    }

    @Test
    void create_noExportFileConfig() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"new_scgc\", \"importFileConfiguration\":{\"name\":\"default_import\"}, \"type\": \"UNIVERSAL\", \"fixedPrefix\": \"4563217\", \"sequencePrefix\": \"5478\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"Rejected value 'null' for field 'exportFileConfiguration'\"}"));
    }

    @Test
    void create_missingPlmn() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"new_scgc\", \"exportFileConfiguration\":{\"name\":\"default_export\"}, \"importFileConfiguration\":{\"name\":\"default_import\"}, \"transportKey\":\"1\",\"algorithmVersion\":4,\"plmn\":{ \"code\": \"MISSING\"},\"msinSequence\":\"DEFAULT\",\"msinSequence\":\"SOMETHING\", \"type\": \"UNIVERSAL\", \"fixedPrefix\": \"4563217\", \"sequencePrefix\": \"5478\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"PLMN with code 'MISSING' could not be found\"}"));
    }

    @Test
    void create_nameAlreadyExists() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"EIRCOM_FCS_MOB_LANDLINE_UNPAIR\", \"exportFileConfiguration\":{\"name\":\"default_export\"}, \"importFileConfiguration\":{\"name\":\"default_import\"}, \"transportKey\":\"1\",\"algorithmVersion\":4,\"plmn\":{ \"code\": \"27203\"},\"msinSequence\":\"DEFAULT\",\"msinSequence\":\"SOMETHING\"}"))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().json("{\"error\":\"SIM Card generation configuration with name 'EIRCOM_FCS_MOB_LANDLINE_UNPAIR' already exists\"}"));
    }

    @Test
    void update_success() throws Exception {
        mockMvc.perform(patch(baseUrl + "/EIRCOM_FCS_MOB_LANDLINE_UNPAIR")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"something_else\", \"exportFileConfiguration\":{\"name\":\"default_import\"}, \"importFileConfiguration\":{\"name\":\"default_export\"}, \"transportKey\":\"1\",\"algorithmVersion\":5,\"plmn\":{ \"code\": \"27204\"},\"msinSequence\":\"ANOTHER\",\"iccidSequence\":\"ELSE\",\"artwork\":\"something\",\"simReference\":\"else\", \"type\": \"UNIVERSAL\", \"fixedPrefix\": \"04563217\", \"sequencePrefix\": \"05478\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\":\"something_else\",\"exportFileConfiguration\":{\"name\":\"default_import\",\"prefix\":\"MMC\",\"suffix\":\".out\",\"headerFormat\":null,\"recordFormat\":null},\"importFileConfiguration\":{\"name\":\"default_export\",\"prefix\":\"MMC\",\"suffix\":\".inp\",\"headerFormat\":\"<div>${CONTENT3}</div>\",\"recordFormat\":null},\"transportKey\":\"1\",\"artwork\":\"something\",\"simReference\":\"else\",\"type\":\"UNIVERSAL\",\"fixedPrefix\":\"04563217\",\"sequencePrefix\":\"05478\",\"algorithmVersion\":5,\"plmn\":{\"plmnId\":2,\"code\":\"27204\",\"networkName\":\"Meteor Mobile Telecommunications Limited 2\",\"tadigCode\":\"IRLME2\",\"countryIsoCode\":\"IRL\",\"countryName\":\"Ireland\",\"rangesPrefix\":\"3368900890402\",\"links\":[]},\"msinSequence\":\"ANOTHER\",\"iccidSequence\":\"ELSE\",\"notify\":\"test@test.test\"}"));
    }

    @Test
    void update_missing() throws Exception {
        mockMvc.perform(patch(baseUrl + "/MISSING")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"something_else\", \"exportFileConfiguration\":{\"name\":\"default_import\"}, \"importFileConfiguration\":{\"name\":\"default_export\"}, \"transportKey\":\"1\",\"algorithmVersion\":5,\"plmn\":{ \"code\": \"27203\"},\"msinSequence\":\"ANOTHER\",\"iccidSequence\":\"ELSE\"}"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\":\"SIM Card generation configuration with name 'MISSING' not found\"}"));
    }

    @Test
    void update_nameAlreadyExists() throws Exception {
        mockMvc.perform(patch(baseUrl + "/EIRCOM_FCS_MOB_LANDLINE_UNPAIR")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"ANOTHER\", \"exportFileConfiguration\":{\"name\":\"default_import\"}, \"importFileConfiguration\":{\"name\":\"default_export\"}, \"transportKey\":\"1\",\"algorithmVersion\":5,\"plmn\":{ \"code\": \"27203\"},\"msinSequence\":\"ANOTHER\",\"iccidSequence\":\"ELSE\"}"))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().json("{\"error\":\"SIM Card generation configuration with name 'ANOTHER' already exists\"}"));
    }

    @Test
    void update_sameName() throws Exception {
        mockMvc.perform(patch(baseUrl + "/EIRCOM_FCS_MOB_LANDLINE_UNPAIR")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"EIRCOM_FCS_MOB_LANDLINE_UNPAIR\", \"exportFileConfiguration\":{\"name\":\"default_import\"}, \"importFileConfiguration\":{\"name\":\"default_export\"}, \"transportKey\":\"1\",\"algorithmVersion\":5,\"plmn\":{ \"code\": \"27204\"},\"msinSequence\":\"ANOTHER\",\"iccidSequence\":\"ELSE\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\":\"EIRCOM_FCS_MOB_LANDLINE_UNPAIR\",\"exportFileConfiguration\":{\"name\":\"default_import\",\"prefix\":\"MMC\",\"suffix\":\".out\",\"headerFormat\":null,\"recordFormat\":null},\"importFileConfiguration\":{\"name\":\"default_export\",\"prefix\":\"MMC\",\"suffix\":\".inp\",\"headerFormat\":\"<div>${CONTENT3}</div>\",\"recordFormat\":null},\"transportKey\":\"1\",\"artwork\":\"EIR_SME_REP\",\"simReference\":null,\"algorithmVersion\":5,\"plmn\":{\"plmnId\":2,\"code\":\"27204\",\"networkName\":\"Meteor Mobile Telecommunications Limited 2\",\"tadigCode\":\"IRLME2\",\"countryIsoCode\":\"IRL\",\"countryName\":\"Ireland\",\"rangesPrefix\":\"3368900890402\"},\"msinSequence\":\"ANOTHER\",\"notify\":\"test@test.test\"}"));
    }

    @Test
    void update_missingImportFileConfig() throws Exception {
        mockMvc.perform(patch(baseUrl + "/EIRCOM_FCS_MOB_LANDLINE_UNPAIR")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"EIRCOM_FCS_MOB_LANDLINE_UNPAIR\", \"exportFileConfiguration\":{\"name\":\"default_import\"}, \"importFileConfiguration\":{\"name\":\"missing\"}, \"transportKey\":\"1\",\"algorithmVersion\":5,\"plmn\":{ \"code\": \"27203\"},\"msinSequence\":\"ANOTHER\",\"iccidSequence\":\"ELSE\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"File configuration with name 'missing' could not be found\"}"));
    }
}
