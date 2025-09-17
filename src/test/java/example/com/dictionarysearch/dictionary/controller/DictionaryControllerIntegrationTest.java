package example.com.dictionarysearch.dictionary.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import example.com.dictionarysearch.auth.service.ApiKeyService;
import example.com.dictionarysearch.dictionary.model.WordEntry;
import example.com.dictionarysearch.dictionary.service.DictionaryService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DictionaryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DictionaryService dictionaryService;

    @MockitoBean
    private ApiKeyService apiKeyService;  // Stub für gültigen Key

    @Test
    void shouldReturnResultsAsJsonWithValidApiKey() throws Exception {
        WordEntry entry = new WordEntry();
        entry.setLemma("Haus");

        // Stub Service
        when(dictionaryService.search(".*", null, 0L, 10L, "alphabetical_asc"))
                .thenReturn(List.of(entry));

        // Stub ApiKeyService
        when(apiKeyService.isValid("valid-key")).thenReturn(true);

        mockMvc.perform(get("/api/search")
                        .param("regex", ".*")
                        .param("page", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "alphabetical_asc")
                        .header("X-API-KEY", "valid-key")) // Auth-Key
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.sortBy").value("alphabetical_asc"))
                .andExpect(jsonPath("$.results[0].lemma").value("Haus"));
    }

    @Test
    void shouldReturnEmptyResultsAsJsonWithValidApiKey() throws Exception {
        // Stub Service
        when(dictionaryService.search(null, null, 0L, 10L, "alphabetical_asc"))
                .thenReturn(List.of());

        // Stub ApiKeyService
        when(apiKeyService.isValid("valid-key")).thenReturn(true);

        mockMvc.perform(get("/api/search")
                        .param("page", "0")
                        .param("pageSize", "10")
                        .param("sortBy", "alphabetical_asc")
                        .header("X-API-KEY", "valid-key"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").isEmpty());
    }

    @Test
    void shouldReturnUnauthorizedForMissingApiKey() throws Exception {
        mockMvc.perform(get("/api/search")
                        .param("page", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedForInvalidApiKey() throws Exception {
        when(apiKeyService.isValid("invalid-key")).thenReturn(false);

        mockMvc.perform(get("/api/search")
                        .param("page", "0")
                        .param("pageSize", "10")
                        .header("X-API-KEY", "invalid-key"))
                .andExpect(status().isUnauthorized());
    }
}
