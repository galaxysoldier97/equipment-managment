package mc.monacotelecom.tecrep.equipments.integration;


import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql({"/sql/clean.sql", "/sql/file_configuration_data.sql"})
class FileConfigurationIntegrationTests extends BaseIntegrationTest {

    final String baseUrl = "/private/auth/fileConfiguration";

    @Test
    void search_success() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("name", "default_export");
        mockMvc.perform(get(baseUrl + "?size=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(map))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name").value("default_export"))
                .andExpect(jsonPath("$.content[0].prefix").value("MMC"))
                .andExpect(jsonPath("$.content[0].suffix").value(".out"))
                .andExpect(jsonPath("$.content[0].headerFormat").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.content[0].recordFormat").value("<div>${CONTENT2}</div>"));
    }

    @Test
    void getByCode_success() throws Exception {
        mockMvc.perform(get(baseUrl + "/default_export"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("default_export"))
                .andExpect(jsonPath("$.prefix").value("MMC"))
                .andExpect(jsonPath("$.suffix").value(".out"))
                .andExpect(jsonPath("$.headerFormat").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.recordFormat").value("<div>${CONTENT2}</div>"));
    }

    @Test
    void getByCode_missing() throws Exception {
        mockMvc.perform(get(baseUrl + "/MISSING"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"File configuration with name 'MISSING' could not be found\"}"));
    }

    @Test
    void delete_success() throws Exception {
        mockMvc.perform(delete(baseUrl + "/test_export3"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_missing() throws Exception {
        mockMvc.perform(delete(baseUrl + "/MISSING"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"File configuration with name 'MISSING' could not be found\"}"));
    }

    @Test
    void delete_attachedToSimCardGenerationConfigurationImport() throws Exception {
        mockMvc.perform(delete(baseUrl + "/default_export"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"File configuration with name 'default_export' cannot be deleted because it is referenced as import by SIM Card generation configuration 'EIRCOM_FCS_MOB_LANDLINE_UNPAIR'\"}"));
    }

    @Test
    void delete_attachedToSimCardGenerationConfigurationExport() throws Exception {
        mockMvc.perform(delete(baseUrl + "/test_export2"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"File configuration with name 'test_export2' cannot be deleted because it is referenced as export by SIM Card generation configuration 'EIRCOM_FCS_MOB_LANDLINE_UNPAIR2'\"}"));
    }

    @Test
    void create_success() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"new_format\",\"prefix\":\"MMC\",\"suffix\":\".out\",\"headerFormat\":null,\"recordFormat\":\"<div>${CONTENT2}</div>\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("new_format"))
                .andExpect(jsonPath("$.prefix").value("MMC"))
                .andExpect(jsonPath("$.suffix").value(".out"))
                .andExpect(jsonPath("$.headerFormat").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.recordFormat").value("<div>${CONTENT2}</div>"));
    }

    @Test
    void create_nameAlreadyExists() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"default_export\",\"prefix\":\"MMC\",\"suffix\":\".out\",\"headerFormat\":null,\"recordFormat\":\"<div>${CONTENT2}</div>\"}"))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().json("{\"errorMessage\":\"File configuration with name 'default_export' already exists\"}"));
    }

    @Test
    void update_success() throws Exception {
        mockMvc.perform(patch(baseUrl + "/default_export")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"something_else\",\"prefix\":\"MMC2\",\"suffix\":\".out2\",\"headerFormat\":\"SOMETHING\",\"recordFormat\":\"<div>${CONTENT5}</div>\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("something_else"))
                .andExpect(jsonPath("$.prefix").value("MMC2"))
                .andExpect(jsonPath("$.suffix").value(".out2"))
                .andExpect(jsonPath("$.headerFormat").value("SOMETHING"))
                .andExpect(jsonPath("$.recordFormat").value("<div>${CONTENT5}</div>"));
    }

    @Test
    void update_missing() throws Exception {
        mockMvc.perform(patch(baseUrl + "/MISSING")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"new_format\",\"prefix\":\"MMC\",\"suffix\":\".out\",\"headerFormat\":null,\"recordFormat\":\"<div>${CONTENT2}</div>\"}"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errorMessage\":\"File configuration with name 'MISSING' could not be found\"}"));
    }

    @Test
    void update_nameAlreadyExists() throws Exception {
        mockMvc.perform(patch(baseUrl + "/default_export")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"test_export3\",\"prefix\":\"MMC2\",\"suffix\":\".out2\",\"headerFormat\":\"SOMETHING\",\"recordFormat\":\"<div>${CONTENT5}</div>\"}"))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().json("{\"errorMessage\":\"File configuration with name 'test_export3' already exists\"}"));
    }

    @Test
    void update_sameName() throws Exception {
        mockMvc.perform(patch(baseUrl + "/default_export")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"default_export\",\"prefix\":\"MMC2\",\"suffix\":\".out2\",\"headerFormat\":\"SOMETHING\",\"recordFormat\":\"<div>${CONTENT5}</div>\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("default_export"))
                .andExpect(jsonPath("$.prefix").value("MMC2"))
                .andExpect(jsonPath("$.suffix").value(".out2"))
                .andExpect(jsonPath("$.headerFormat").value("SOMETHING"))
                .andExpect(jsonPath("$.recordFormat").value("<div>${CONTENT5}</div>"));
    }
}
