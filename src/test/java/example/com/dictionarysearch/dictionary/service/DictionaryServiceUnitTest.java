package example.com.dictionarysearch.dictionary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import example.com.dictionarysearch.dictionary.model.WordEntry;
import example.com.dictionarysearch.dictionary.util.DictionaryLoader;

class DictionaryServiceUnitTest {

    private DictionaryLoader dictionaryLoader;
    private DictionaryService dictionaryService;

    @BeforeEach
    void setUp() {
        dictionaryLoader = mock(DictionaryLoader.class);
        List<WordEntry> entries = Arrays.asList(
                createEntry("Apfel"),
                createEntry("Erbse"),
                createEntry("Gurke"),
                createEntry("Lauch"),
                createEntry("Linse"),
                createEntry("Salat"),
                createEntry("Kraut"),
                createEntry("Banane"),
                createEntry("Birne"),
                createEntry("Apfelsine"),
                createEntry("Äpfel"),
                createEntry("Mango"),
                createEntry("Aus"),
                createEntry("Sau"));
        when(dictionaryLoader.getEntries()).thenReturn(entries);
        dictionaryService = new DictionaryService(dictionaryLoader);
    }

    private WordEntry createEntry(String lemma) {
        WordEntry entry = new WordEntry();
        entry.setLemma(lemma);
        return entry;
    }

    @Test
    void testSearchWithPartialMatch() {
        List<WordEntry> result = dictionaryService.search("___", "HAUS", 0, 10, "ALPHABETICAL_ASC");
        assertEquals(2, result.size());
        assertEquals("Aus", result.get(0).getLemma());
        assertEquals("Sau", result.get(1).getLemma());
    }

    @Test
    void testSearchValidTemplateAndLetters() {
        List<WordEntry> result = dictionaryService.search("Apfel", "Apfel", 0, 10, "ALPHABETICAL_ASC");
        assertEquals(1, result.size());
        assertEquals("Apfel", result.get(0).getLemma());
    }

    @Test
    void testSearchTemplateWithUnderscore() {
        List<WordEntry> result = dictionaryService.search("A____", "Apfel", 0, 10, "ALPHABETICAL_ASC");
        assertEquals(1, result.size());
        assertEquals("Apfel", result.get(0).getLemma());
    }

    @Test
    void testSearchLettersWithWildcard() {
        List<WordEntry> result = dictionaryService.search("Banane", "Banan?", 0, 10, "ALPHABETICAL_ASC");
        assertEquals(1, result.size());
        assertEquals("Banane", result.get(0).getLemma());
    }

    @Test
    void testSearchInvalidTemplateThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            dictionaryService.search("Apfel!", "Apfel", 0, 10, "ALPHABETICAL_ASC");
        });
    }

    @Test
    void testSearchInvalidLettersThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            dictionaryService.search("Apfel", "Apfel!", 0, 10, "ALPHABETICAL_ASC");
        });
    }

    @Test
    void testPaging() {
        List<WordEntry> result;
        result = dictionaryService.search("_____", "?????", 0, 3, "ALPHABETICAL_ASC");
        assertEquals(3, result.size());
        result = dictionaryService.search("_____", "?????", 1, 3, "ALPHABETICAL_ASC");
        assertEquals(3, result.size());
        result = dictionaryService.search("_____", "?????", 2, 3, "ALPHABETICAL_ASC");
        assertEquals(3, result.size());
        result = dictionaryService.search("_____", "?????", 3, 3, "ALPHABETICAL_ASC");
        assertEquals(1, result.size());
    }
}