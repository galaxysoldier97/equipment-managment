package mc.monacotelecom.tecrep.equipments.integration;

import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sql/clean.sql", "/sql/simcard_generation_configuration_data.sql"})
class SimCardGenerationConfigurationIntegrationV2Tests extends BaseIntegrationTest {
    final String baseUrl = "/api/v2/private/auth/simGenerationConfigurations";

    @Test
    void search_simCardGenerationConfigurationCRUD_withNameReturned() throws Exception {
        mockMvc.perform(get(baseUrl + "?name=EIRCOM_FCS_MOB_LANDLINE_UNPAIR"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name").value("EIRCOM_FCS_MOB_LANDLINE_UNPAIR"))
                .andExpect(jsonPath("$.content[0].exportFileConfiguration.name").value("default_export"))
                .andExpect(jsonPath("$.content[0].exportFileConfiguration.prefix").value("MMC"))
                .andExpect(jsonPath("$.content[0].exportFileConfiguration.suffix").value(".inp"))
                .andExpect(jsonPath("$.content[0].exportFileConfiguration.headerFormat").value("<div>${CONTENT3}</div>"))
                .andExpect(jsonPath("$.content[0].exportFileConfiguration.recordFormat").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].importFileConfiguration.name").value("default_import"))
                .andExpect(jsonPath("$.content[0].importFileConfiguration.prefix").value("MMC"))
                .andExpect(jsonPath("$.content[0].importFileConfiguration.suffix").value(".out"))
                .andExpect(jsonPath("$.content[0].importFileConfiguration.headerFormat").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].importFileConfiguration.recordFormat").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].transportKey").value("6"))
                .andExpect(jsonPath("$.content[0].artwork").value("EIR_SME_REP"))
                .andExpect(jsonPath("$.content[0].simReference").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].algorithmVersion").value(1))
                .andExpect(jsonPath("$.content[0].plmn.id").value(1))
                .andExpect(jsonPath("$.content[0].plmn.code").value("27203"))
                .andExpect(jsonPath("$.content[0].plmn.networkName").value("Meteor Mobile Telecommunications Limited"))
                .andExpect(jsonPath("$.content[0].plmn.tadigCode").value("IRLME"))
                .andExpect(jsonPath("$.content[0].plmn.countryIsoCode").value("IRL"))
                .andExpect(jsonPath("$.content[0].plmn.countryName").value("Ireland"))
                .andExpect(jsonPath("$.content[0].plmn.rangesPrefix").value("3368900890401"))
                .andExpect(jsonPath("$.content[0].msinSequence").value("DEFAULT"))
                .andExpect(jsonPath("$.content[0].iccidSequence").value("DEFAULT"))
                .andExpect(jsonPath("$.content[0].notify").value("test@test.test"));
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
                .andExpect(jsonPath("$.name").value("EIRCOM_FCS_MOB_LANDLINE_UNPAIR"))
                .andExpect(jsonPath("$.exportFileConfiguration.name").value("default_export"))
                .andExpect(jsonPath("$.exportFileConfiguration.prefix").value("MMC"))
                .andExpect(jsonPath("$.exportFileConfiguration.suffix").value(".inp"))
                .andExpect(jsonPath("$.exportFileConfiguration.headerFormat").value("<div>${CONTENT3}</div>"))
                .andExpect(jsonPath("$.exportFileConfiguration.recordFormat").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.importFileConfiguration.name").value("default_import"))
                .andExpect(jsonPath("$.importFileConfiguration.prefix").value("MMC"))
                .andExpect(jsonPath("$.importFileConfiguration.suffix").value(".out"))
                .andExpect(jsonPath("$.importFileConfiguration.headerFormat").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.importFileConfiguration.recordFormat").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.transportKey").value("6"))
                .andExpect(jsonPath("$.artwork").value("EIR_SME_REP"))
                .andExpect(jsonPath("$.simReference").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.algorithmVersion").value(1))
                .andExpect(jsonPath("$.plmn.id").value(1))
                .andExpect(jsonPath("$.plmn.code").value("27203"))
                .andExpect(jsonPath("$.plmn.networkName").value("Meteor Mobile Telecommunications Limited"))
                .andExpect(jsonPath("$.plmn.tadigCode").value("IRLME"))
                .andExpect(jsonPath("$.plmn.countryIsoCode").value("IRL"))
                .andExpect(jsonPath("$.plmn.countryName").value("Ireland"))
                .andExpect(jsonPath("$.plmn.rangesPrefix").value("3368900890401"))
                .andExpect(jsonPath("$.msinSequence").value("DEFAULT"))
                .andExpect(jsonPath("$.iccidSequence").value("DEFAULT"))
                .andExpect(jsonPath("$.notify").value("test@test.test"));
    }

    @Test
    void getByName_missing() throws Exception {
        mockMvc.perform(get(baseUrl + "/MISSING"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"SIM Card generation configuration with name 'MISSING' not found\"}"));
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
                .andExpect(content().json("{\"errorMessage\":\"SIM Card generation configuration with name 'MISSING' not found\"}"));
    }

    @Test
    void delete_attachedToBatch() throws Exception {
        mockMvc.perform(delete(baseUrl + "/ANOTHER"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"SIM Card generation configuration with name 'ANOTHER' cannot be deleted because it is referenced by batch '6'\"}"));
    }

    @Test
    void create_success() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"new_scgc\", \"exportFileConfigurationName\":\"default_export\", \"importFileConfigurationName\":\"default_import\", \"transportKey\":\"1\",\"algorithmVersion\":4,\"plmnCode\": \"27203\",\"msinSequence\":\"DEFAULT\",\"iccidSequence\":\"SOMETHING\",\"artwork\":\"EIR_SME_REP\",\"simReference\":\"MET_PP001_LTE\", \"type\": \"UNIVERSAL\", \"fixedPrefix\": \"04563217\", \"sequencePrefix\": \"05478\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("new_scgc"))
                .andExpect(jsonPath("$.exportFileConfiguration.name").value("default_export"))
                .andExpect(jsonPath("$.exportFileConfiguration.prefix").value("MMC"))
                .andExpect(jsonPath("$.exportFileConfiguration.suffix").value(".inp"))
                .andExpect(jsonPath("$.exportFileConfiguration.headerFormat").value("<div>${CONTENT3}</div>"))
                .andExpect(jsonPath("$.exportFileConfiguration.recordFormat").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.importFileConfiguration.name").value("default_import"))
                .andExpect(jsonPath("$.importFileConfiguration.prefix").value("MMC"))
                .andExpect(jsonPath("$.importFileConfiguration.suffix").value(".out"))
                .andExpect(jsonPath("$.importFileConfiguration.headerFormat").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.importFileConfiguration.recordFormat").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.transportKey").value("1"))
                .andExpect(jsonPath("$.artwork").value("EIR_SME_REP"))
                .andExpect(jsonPath("$.simReference").value("MET_PP001_LTE"))
                .andExpect(jsonPath("$.type").value("UNIVERSAL"))
                .andExpect(jsonPath("$.fixedPrefix").value("04563217"))
                .andExpect(jsonPath("$.sequencePrefix").value("05478"))
                .andExpect(jsonPath("$.algorithmVersion").value(4))
                .andExpect(jsonPath("$.plmn.id").value(1))
                .andExpect(jsonPath("$.plmn.code").value("27203"))
                .andExpect(jsonPath("$.plmn.networkName").value("Meteor Mobile Telecommunications Limited"))
                .andExpect(jsonPath("$.plmn.tadigCode").value("IRLME"))
                .andExpect(jsonPath("$.plmn.countryIsoCode").value("IRL"))
                .andExpect(jsonPath("$.plmn.countryName").value("Ireland"))
                .andExpect(jsonPath("$.plmn.rangesPrefix").value("3368900890401"))
                .andExpect(jsonPath("$.msinSequence").value("DEFAULT"))
                .andExpect(jsonPath("$.iccidSequence").value("SOMETHING"))
                .andExpect(jsonPath("$.notify").value("test@test.test"));
    }

    @Test
    void create_successNoPrefixes() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"new_scgc\", \"exportFileConfigurationName\":\"default_export\", \"importFileConfigurationName\":\"default_import\", \"transportKey\":\"1\",\"algorithmVersion\":4,\"plmnCode\": \"27203\",\"msinSequence\":\"DEFAULT\",\"iccidSequence\":\"SOMETHING\",\"artwork\":\"EIR_SME_REP\",\"simReference\":\"MET_PP001_LTE\", \"type\": \"UNIVERSAL\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("new_scgc"))
                .andExpect(jsonPath("$.exportFileConfiguration.name").value("default_export"))
                .andExpect(jsonPath("$.exportFileConfiguration.prefix").value("MMC"))
                .andExpect(jsonPath("$.exportFileConfiguration.suffix").value(".inp"))
                .andExpect(jsonPath("$.exportFileConfiguration.headerFormat").value("<div>${CONTENT3}</div>"))
                .andExpect(jsonPath("$.exportFileConfiguration.recordFormat").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.importFileConfiguration.name").value("default_import"))
                .andExpect(jsonPath("$.importFileConfiguration.prefix").value("MMC"))
                .andExpect(jsonPath("$.importFileConfiguration.suffix").value(".out"))
                .andExpect(jsonPath("$.importFileConfiguration.headerFormat").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.importFileConfiguration.recordFormat").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.transportKey").value("1"))
                .andExpect(jsonPath("$.artwork").value("EIR_SME_REP"))
                .andExpect(jsonPath("$.simReference").value("MET_PP001_LTE"))
                .andExpect(jsonPath("$.type").value("UNIVERSAL"))
                .andExpect(jsonPath("$.fixedPrefix").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.sequencePrefix").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.algorithmVersion").value(4))
                .andExpect(jsonPath("$.plmn.id").value(1))
                .andExpect(jsonPath("$.plmn.code").value("27203"))
                .andExpect(jsonPath("$.plmn.networkName").value("Meteor Mobile Telecommunications Limited"))
                .andExpect(jsonPath("$.plmn.tadigCode").value("IRLME"))
                .andExpect(jsonPath("$.plmn.countryIsoCode").value("IRL"))
                .andExpect(jsonPath("$.plmn.countryName").value("Ireland"))
                .andExpect(jsonPath("$.plmn.rangesPrefix").value("3368900890401"))
                .andExpect(jsonPath("$.msinSequence").value("DEFAULT"))
                .andExpect(jsonPath("$.iccidSequence").value("SOMETHING"))
                .andExpect(jsonPath("$.notify").value("test@test.test"));
    }

    @Test
    void create_failureIfFixedPrefixNotNumeric() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"new_scgc\", \"exportFileConfigurationName\":\"default_export\", \"importFileConfigurationName\":\"default_import\", \"transportKey\":\"1\",\"algorithmVersion\":4,\"plmn\":{ \"code\": \"27203\"},\"msinSequence\":\"DEFAULT\",\"iccidSequence\":\"SOMETHING\",\"artwork\":\"EIR_SME_REP\",\"simReference\":\"MET_PP001_LTE\", \"type\": \"UNIVERSAL\", \"fixedPrefix\": \"ABC\", \"sequencePrefix\": \"5478\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Fixed prefix 'ABC' must be numeric\"}"));
    }

    @Test
    void create_failureIfSequencePrefixNotNumeric() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"new_scgc\", \"exportFileConfigurationName\":\"default_export\", \"importFileConfigurationName\":\"default_import\", \"transportKey\":\"1\",\"algorithmVersion\":4,\"plmn\":{ \"code\": \"27203\"},\"msinSequence\":\"DEFAULT\",\"iccidSequence\":\"SOMETHING\",\"artwork\":\"EIR_SME_REP\",\"simReference\":\"MET_PP001_LTE\", \"type\": \"UNIVERSAL\", \"fixedPrefix\": \"4563217\", \"sequencePrefix\": \"ABC\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Sequence prefix 'ABC' must be numeric\"}"));
    }

    @Test
    void create_successNoPlmn() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"new_scgc\", \"exportFileConfigurationName\":\"default_export\", \"importFileConfigurationName\":\"default_import\", \"transportKey\":\"1\",\"algorithmVersion\":4,\"msinSequence\":\"DEFAULT\",\"iccidSequence\":\"SOMETHING\", \"type\": \"UNIVERSAL\", \"fixedPrefix\": \"4563217\", \"sequencePrefix\": \"5478\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("new_scgc"))
                .andExpect(jsonPath("$.exportFileConfiguration.name").value("default_export"))
                .andExpect(jsonPath("$.exportFileConfiguration.prefix").value("MMC"))
                .andExpect(jsonPath("$.exportFileConfiguration.suffix").value(".inp"))
                .andExpect(jsonPath("$.exportFileConfiguration.headerFormat").value("<div>${CONTENT3}</div>"))
                .andExpect(jsonPath("$.exportFileConfiguration.recordFormat").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.importFileConfiguration.name").value("default_import"))
                .andExpect(jsonPath("$.importFileConfiguration.prefix").value("MMC"))
                .andExpect(jsonPath("$.importFileConfiguration.suffix").value(".out"))
                .andExpect(jsonPath("$.importFileConfiguration.headerFormat").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.importFileConfiguration.recordFormat").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.transportKey").value("1"))
                .andExpect(jsonPath("$.artwork").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.simReference").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.type").value("UNIVERSAL"))
                .andExpect(jsonPath("$.fixedPrefix").value("4563217"))
                .andExpect(jsonPath("$.sequencePrefix").value("5478"))
                .andExpect(jsonPath("$.algorithmVersion").value(4))
                .andExpect(jsonPath("$.plmn").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.msinSequence").value("DEFAULT"))
                .andExpect(jsonPath("$.iccidSequence").value("SOMETHING"))
                .andExpect(jsonPath("$.notify").value("test@test.test"));
    }

    @Test
    void create_missingImportFileConfig() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"new_scgc\", \"exportFileConfigurationName\":\"default_export\", \"importFileConfigurationName\":\"missing\", \"type\": \"UNIVERSAL\", \"fixedPrefix\": \"4563217\", \"sequencePrefix\": \"5478\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"File configuration with name 'missing' could not be found\"}"));
    }

    @Test
    void create_missingExportFileConfig() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"new_scgc\", \"exportFileConfigurationName\":\"missing\", \"importFileConfigurationName\":\"default_import\", \"type\": \"UNIVERSAL\", \"fixedPrefix\": \"4563217\", \"sequencePrefix\": \"5478\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"File configuration with name 'missing' could not be found\"}"));
    }

    @Test
    void create_noExportFileConfig() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"new_scgc\", \"importFileConfigurationName\":\"default_import\", \"type\": \"UNIVERSAL\", \"fixedPrefix\": \"4563217\", \"sequencePrefix\": \"5478\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Rejected value 'null' for field 'exportFileConfigurationName'\"}"));
    }

    @Test
    void create_missingPlmn() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"new_scgc\", \"exportFileConfigurationName\":\"default_export\", \"importFileConfigurationName\":\"default_import\", \"transportKey\":\"1\",\"algorithmVersion\":4,\"plmnCode\": \"MISSING\",\"msinSequence\":\"DEFAULT\",\"msinSequence\":\"SOMETHING\", \"type\": \"UNIVERSAL\", \"fixedPrefix\": \"4563217\", \"sequencePrefix\": \"5478\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"PLMN with code 'MISSING' could not be found\"}"));
    }

    @Test
    void create_nameAlreadyExists() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"EIRCOM_FCS_MOB_LANDLINE_UNPAIR\", \"exportFileConfigurationName\":\"default_export\", \"importFileConfigurationName\":\"default_import\", \"transportKey\":\"1\",\"algorithmVersion\":4,\"plmn\":{ \"code\": \"27203\"},\"msinSequence\":\"DEFAULT\",\"msinSequence\":\"SOMETHING\"}"))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().json("{\"errorMessage\":\"SIM Card generation configuration with name 'EIRCOM_FCS_MOB_LANDLINE_UNPAIR' already exists\"}"));
    }

    @Test
    void update_success() throws Exception {
        mockMvc.perform(patch(baseUrl + "/EIRCOM_FCS_MOB_LANDLINE_UNPAIR")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"something_else\", \"exportFileConfigurationName\":\"default_import\", \"importFileConfigurationName\":\"default_export\", \"transportKey\":\"1\",\"algorithmVersion\":5,\"plmnCode\": \"27204\",\"msinSequence\":\"ANOTHER\",\"iccidSequence\":\"ELSE\",\"artwork\":\"something\",\"simReference\":\"else\", \"type\": \"UNIVERSAL\", \"fixedPrefix\": \"04563217\", \"sequencePrefix\": \"05478\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\":\"something_else\",\"exportFileConfiguration\":{\"name\":\"default_import\",\"prefix\":\"MMC\",\"suffix\":\".out\",\"headerFormat\":null,\"recordFormat\":null},\"importFileConfiguration\":{\"name\":\"default_export\",\"prefix\":\"MMC\",\"suffix\":\".inp\",\"headerFormat\":\"<div>${CONTENT3}</div>\",\"recordFormat\":null},\"transportKey\":\"1\",\"artwork\":\"something\",\"simReference\":\"else\",\"type\":\"UNIVERSAL\",\"fixedPrefix\":\"04563217\",\"sequencePrefix\":\"05478\",\"algorithmVersion\":5,\"plmn\":{\"id\":2,\"code\":\"27204\",\"networkName\":\"Meteor Mobile Telecommunications Limited 2\",\"tadigCode\":\"IRLME2\",\"countryIsoCode\":\"IRL\",\"countryName\":\"Ireland\",\"rangesPrefix\":\"3368900890402\"},\"msinSequence\":\"ANOTHER\",\"iccidSequence\":\"ELSE\",\"notify\":\"test@test.test\"}"));
    }

    @Test
    void update_missing() throws Exception {
        mockMvc.perform(patch(baseUrl + "/MISSING")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"something_else\", \"exportFileConfigurationName\":\"default_import\", \"importFileConfigurationName\":\"default_export\", \"transportKey\":\"1\",\"algorithmVersion\":5,\"plmnCode\": \"27203\",\"msinSequence\":\"ANOTHER\",\"iccidSequence\":\"ELSE\"}"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"SIM Card generation configuration with name 'MISSING' not found\"}"));
    }

    @Test
    void update_nameAlreadyExists() throws Exception {
        mockMvc.perform(patch(baseUrl + "/EIRCOM_FCS_MOB_LANDLINE_UNPAIR")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"ANOTHER\", \"exportFileConfigurationName\":\"default_import\", \"importFileConfigurationName\":\"default_export\", \"transportKey\":\"1\",\"algorithmVersion\":5,\"plmnCode\":\"27203\",\"msinSequence\":\"ANOTHER\",\"iccidSequence\":\"ELSE\"}"))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().json("{\"errorMessage\":\"SIM Card generation configuration with name 'ANOTHER' already exists\"}"));
    }

    @Test
    void update_sameName() throws Exception {
        mockMvc.perform(patch(baseUrl + "/EIRCOM_FCS_MOB_LANDLINE_UNPAIR")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"EIRCOM_FCS_MOB_LANDLINE_UNPAIR\", \"exportFileConfigurationName\":\"default_import\", \"importFileConfigurationName\":\"default_export\", \"transportKey\":\"1\",\"algorithmVersion\":5,\"plmnCode\": \"27204\",\"msinSequence\":\"ANOTHER\",\"iccidSequence\":\"ELSE\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("EIRCOM_FCS_MOB_LANDLINE_UNPAIR"))
                .andExpect(jsonPath("$.exportFileConfiguration.name").value("default_import"))
                .andExpect(jsonPath("$.exportFileConfiguration.prefix").value("MMC"))
                .andExpect(jsonPath("$.exportFileConfiguration.suffix").value(".out"))
                .andExpect(jsonPath("$.exportFileConfiguration.headerFormat").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.exportFileConfiguration.recordFormat").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.importFileConfiguration.name").value("default_export"))
                .andExpect(jsonPath("$.importFileConfiguration.prefix").value("MMC"))
                .andExpect(jsonPath("$.importFileConfiguration.suffix").value(".inp"))
                .andExpect(jsonPath("$.importFileConfiguration.headerFormat").value("<div>${CONTENT3}</div>"))
                .andExpect(jsonPath("$.importFileConfiguration.recordFormat").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.transportKey").value("1"))
                .andExpect(jsonPath("$.artwork").value("EIR_SME_REP"))
                .andExpect(jsonPath("$.simReference").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.algorithmVersion").value(5))
                .andExpect(jsonPath("$.plmn.id").value(2))
                .andExpect(jsonPath("$.plmn.code").value("27204"))
                .andExpect(jsonPath("$.plmn.networkName").value("Meteor Mobile Telecommunications Limited 2"))
                .andExpect(jsonPath("$.plmn.tadigCode").value("IRLME2"))
                .andExpect(jsonPath("$.plmn.countryIsoCode").value("IRL"))
                .andExpect(jsonPath("$.plmn.countryName").value("Ireland"))
                .andExpect(jsonPath("$.plmn.rangesPrefix").value("3368900890402"))
                .andExpect(jsonPath("$.msinSequence").value("ANOTHER"))
                .andExpect(jsonPath("$.notify").value("test@test.test"));
    }

    @Test
    void update_missingImportFileConfig() throws Exception {
        mockMvc.perform(patch(baseUrl + "/EIRCOM_FCS_MOB_LANDLINE_UNPAIR")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"EIRCOM_FCS_MOB_LANDLINE_UNPAIR\", \"exportFileConfigurationName\":\"default_import\", \"importFileConfigurationName\":\"missing\", \"transportKey\":\"1\",\"algorithmVersion\":5,\"plmnCode\":\"27203\",\"msinSequence\":\"ANOTHER\",\"iccidSequence\":\"ELSE\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"File configuration with name 'missing' could not be found\"}"));
    }
}
