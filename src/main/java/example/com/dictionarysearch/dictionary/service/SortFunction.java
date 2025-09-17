package example.com.dictionarysearch.dictionary.service;

import java.util.Comparator;

import example.com.dictionarysearch.dictionary.model.WordEntry;

public enum SortFunction {
    ALPHABETICAL_ASC(Comparator.comparing(WordEntry::getLemma)),
    ALPHABETICAL_DESC(Comparator.comparing(WordEntry::getLemma).reversed()),
    LENGTH_ASC(Comparator.comparingInt(e -> e.getLemma().length())),
    LENGTH_DESC(Comparator.comparingInt((WordEntry e) -> e.getLemma().length()).reversed());

    private final Comparator<WordEntry> comparator;

    SortFunction(Comparator<WordEntry> comparator) {
        this.comparator = comparator;
    }

    public Comparator<WordEntry> getComparator() {
        return comparator;
    }
}
