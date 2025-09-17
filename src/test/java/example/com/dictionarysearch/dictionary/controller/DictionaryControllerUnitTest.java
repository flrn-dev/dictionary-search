package example.com.dictionarysearch.dictionary.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import example.com.dictionarysearch.dictionary.model.WordEntry;
import example.com.dictionarysearch.dictionary.service.DictionaryService;

class DictionaryControllerUnitTest {

    private DictionaryService dictionaryService;
    private DictionaryController controller;

    @BeforeEach
    void setUp() {
        dictionaryService = mock(DictionaryService.class);
        controller = new DictionaryController(dictionaryService);
    }

    @SuppressWarnings("null")
    @Test
    void shouldReturnResultsFromService() {
        WordEntry entry = new WordEntry();
        entry.setLemma("Haus");

        when(dictionaryService.search(".*", null, 0L, 10L, "alphabetical_asc"))
                .thenReturn(List.of(entry));

        ResponseEntity<Map<String, Object>> response = controller.search(".*", null, 0L, 10L, "alphabetical_asc");

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Map<String, Object> body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.get("page")).isEqualTo(0L);
        assertThat(body.get("pageSize")).isEqualTo(10L);
        assertThat(body.get("sortBy")).isEqualTo("alphabetical_asc");
        assertThat((List<?>) body.get("results")).hasSize(1);
    }

    @SuppressWarnings("null")
    @Test
    void shouldReturnEmptyResultsWhenServiceReturnsEmptyList() {
        when(dictionaryService.search(null, null, 0L, 10L, "alphabetical_asc"))
                .thenReturn(List.of());

        ResponseEntity<Map<String, Object>> response = controller.search(null, null, 0L, 10L, "alphabetical_asc");

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat((List<?>) response.getBody().get("results")).isEmpty();
    }
}
