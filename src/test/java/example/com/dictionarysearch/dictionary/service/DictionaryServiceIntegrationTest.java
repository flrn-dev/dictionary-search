package example.com.dictionarysearch.dictionary.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import example.com.dictionarysearch.dictionary.model.WordEntry;
import example.com.dictionarysearch.dictionary.util.DictionaryLoader;

public class DictionaryServiceIntegrationTest {

    private DictionaryService dictionaryService;

    @BeforeEach
    void setUp() {
        DictionaryLoader loader = new DictionaryLoader();
        dictionaryService = new DictionaryService(loader);
    }

    @Test
    void testSearchFindsResults() {
        List<WordEntry> result = dictionaryService.search("A____", "Apfel", 0, 10, "ALPHABETICAL_ASC");
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(e -> e.getLemma().toLowerCase().startsWith("a")));
    }
}
