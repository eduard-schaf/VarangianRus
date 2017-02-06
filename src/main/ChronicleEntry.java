package main;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eduard Schaf
 * @since 29.01.17
 */
public class ChronicleEntry {
    private String id;
    private List<Sentence> sentenceList;

    public ChronicleEntry() {
        this.id = "";
        this.sentenceList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Sentence> getSentenceList() {
        return sentenceList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChronicleEntry chronicleEntry = (ChronicleEntry) o;

        if (id != null ? !id.equals(chronicleEntry.id) : chronicleEntry.id != null) {
            return false;
        }
        return sentenceList != null ? sentenceList.equals(chronicleEntry.sentenceList) : chronicleEntry.sentenceList == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sentenceList != null ? sentenceList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ChronicleEntry{" +
                "id='" + id + '\'' +
                ", sentenceList=" + sentenceList +
                '}';
    }
}
