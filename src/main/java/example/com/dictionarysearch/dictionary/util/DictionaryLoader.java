package example.com.dictionarysearch.dictionary.util;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import example.com.dictionarysearch.dictionary.model.WordEntry;

@Component
public class DictionaryLoader {

    private final List<WordEntry> entries;

    public DictionaryLoader() {
        this.entries = loadDictionary();
    }

    private List<WordEntry> loadDictionary() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ClassPathResource resource = new ClassPathResource("dictionary.json");
            return mapper.readValue(
                    resource.getInputStream(),
                    new TypeReference<List<WordEntry>>() {}
            );
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<WordEntry> getEntries() {
        return entries;
    }
}
