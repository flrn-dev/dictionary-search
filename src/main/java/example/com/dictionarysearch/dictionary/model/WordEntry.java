package example.com.dictionarysearch.dictionary.model;

import java.util.List;

public class WordEntry {
    private String date;
    private List<String> other_lemmata;
    private String pos;
    private String type;
    private String lemma;
    private String url;
    private String freq;

    public WordEntry() {
    }

    public WordEntry(String date, List<String> other_lemmata, String pos, String type, String lemma, String url, String freq) {
        this.date = date;
        this.other_lemmata = other_lemmata;
        this.pos = pos;
        this.type = type;
        this.lemma = lemma;
        this.url = url;
        this.freq = freq;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getOther_lemmata() {
        return other_lemmata;
    }

    public void setOther_lemmata(List<String> other_lemmata) {
        this.other_lemmata = other_lemmata;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }
}
