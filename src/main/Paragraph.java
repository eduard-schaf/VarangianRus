package main;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eduard Schaf
 * @since 29.01.17
 */
public class Paragraph {
    private List<Sentence> sentenceList;

    public Paragraph() {
        this.sentenceList = new ArrayList<>();
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

        Paragraph paragraph = (Paragraph) o;

        return sentenceList != null ? sentenceList.equals(paragraph.sentenceList) : paragraph.sentenceList == null;
    }

    @Override
    public int hashCode() {
        return sentenceList != null ? sentenceList.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Paragraph{" +
                ", sentenceList=" + sentenceList +
                '}';
    }
}
