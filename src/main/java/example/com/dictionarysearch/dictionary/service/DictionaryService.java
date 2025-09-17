package example.com.dictionarysearch.dictionary.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import example.com.dictionarysearch.dictionary.model.WordEntry;
import example.com.dictionarysearch.dictionary.util.DictionaryLoader;

@Service
public class DictionaryService {
    private final DictionaryLoader dictionaryLoader;

    public DictionaryService(DictionaryLoader dictionaryLoader) {
        this.dictionaryLoader = dictionaryLoader;
    }

    public List<WordEntry> search(String template, String letters, long page, long pageSize, String sortBy) {
        if (template != null && !isValidTemplate(template))
            throw new IllegalArgumentException("found invalid characters in template string [" + template
                    + "], expected A-Z, a-z, ä, ö, ü, Ä, Ö, Ü or '_' as a wildcard symbol!");

        if (letters != null && !isValidLetters(letters))
            throw new IllegalArgumentException("found invalid characters in letters string [" + letters
                    + "], expected A-Z, a-z, ä, ö, ü, Ä, Ö, Ü or '?' as a joker!");

        Comparator<WordEntry> comparator = switch (sortBy.toLowerCase()) {
            case "alphabetical_asc" -> SortFunction.ALPHABETICAL_ASC.getComparator();
            case "alphabetical_desc" -> SortFunction.ALPHABETICAL_DESC.getComparator();
            case "length_asc" -> SortFunction.LENGTH_ASC.getComparator();
            case "length_desc" -> SortFunction.LENGTH_DESC.getComparator();
            default -> throw new IllegalArgumentException("invalid sort order [" + sortBy
                    + "], expected one of (alphabetical_asc, alphabetical_desc, length_asc, length_desc)!");
        };

        return dictionaryLoader.getEntries().stream()
                .filter(el -> filterByTemplate(template, el.getLemma()))
                .filter(el -> filterByLetters(letters, el.getLemma()))
                .sorted(comparator)
                .skip(page * pageSize)
                .limit(pageSize)
                .toList();
    }

    private static boolean isValidTemplate(String template) {
        return template.matches("[a-zA-ZäöüÄÖÜ_]*");
    }

    private static boolean filterByTemplate(String template, String lemma) {
        if (template == null)
            return true;
        if (template.length() != lemma.length())
            return false;

        for (int i = 0; i < template.length(); i++) {
            char tc = Character.toLowerCase(template.charAt(i));
            char lc = Character.toLowerCase(lemma.charAt(i));

            if (tc == '_' || tc == lc)
                continue;

            return false;
        }

        return true;
    }

    private static boolean isValidLetters(String letters) {
        return letters.matches("[a-zA-ZäöüÄÖÜ?]*");
    }

    private static boolean filterByLetters(String letters, String lemma) {
        if (letters == null)
            return true;

        if (letters.length() < lemma.length())
            return false;

        Map<Character, Integer> freq = new HashMap<>();

        for (char c : letters.toLowerCase().toCharArray())
            freq.put(c, freq.getOrDefault(c, 0) + 1);

        for (char c : lemma.toLowerCase().toCharArray()) {
            if (freq.containsKey(c)) {
                freq.put(c, freq.get(c) - 1);
                if (freq.get(c) == 0)
                    freq.remove(c);
            } else if (freq.containsKey('?')) {
                freq.put('?', freq.get('?') - 1);
                if (freq.get('?') == 0)
                    freq.remove('?');
            } else {
                return false;
            }
        }

        return true;
    }
}
