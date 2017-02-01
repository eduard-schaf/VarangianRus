package main;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eduard Schaf
 * @since 29.01.17
 */
public class Paragraph {
    private String id;
    private String title;
    private List<Sentence> sentenceList;

    public Paragraph() {
        this.id = "";
        this.title = "";
        this.sentenceList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

        if (id != null ? !id.equals(paragraph.id) : paragraph.id != null) {
            return false;
        }
        if (title != null ? !title.equals(paragraph.title) : paragraph.title != null) {
            return false;
        }
        return sentenceList != null ? sentenceList.equals(paragraph.sentenceList) : paragraph.sentenceList == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (sentenceList != null ? sentenceList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Paragraph{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", sentenceList=" + sentenceList +
                '}';
    }
}
