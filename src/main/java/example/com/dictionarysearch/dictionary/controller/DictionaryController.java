package example.com.dictionarysearch.dictionary.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import example.com.dictionarysearch.dictionary.model.WordEntry;
import example.com.dictionarysearch.dictionary.service.DictionaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Words", description = "Durchsuchen des Wörterbuchs")
@RequestMapping("/api")
public class DictionaryController {

    private DictionaryService dictionaryService;

    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @Operation(summary = "Suche Wörter", description = "Durchsucht das Wörterbuch anhand von Regex und optionalem Buchstaben-Filter")
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> search(
            @Parameter(description = "Regex-Muster für das Lemma") @RequestParam(required = false) String regex,
            @Parameter(description = "Nur Buchstaben, die im Lemma vorkommen dürfen") @RequestParam(required = false) String letters,
            @Parameter(description = "Seitenzahl (beginnend bei 0)") @RequestParam(defaultValue = "0") Long page,
            @Parameter(description = "Seitengröße") @RequestParam(defaultValue = "10") Long pageSize,
            @Parameter(description = "Sortierung") @RequestParam(defaultValue = "alphabetical_asc") String sortBy) {

        List<WordEntry> results = dictionaryService.search(regex, letters, page, pageSize, sortBy);

        Map<String, Object> response = Map.of(
                "page", page,
                "pageSize", pageSize,
                "sortBy", sortBy,
                "results", results);

        return ResponseEntity.ok(response);
    }
}
