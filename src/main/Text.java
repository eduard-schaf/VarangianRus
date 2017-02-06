package main;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eduard Schaf
 * @since 29.01.17
 */
public class Text {
    private String title;
    private List<ChronicleEntry> chronicleEntryList;

    public Text(String title) {
        this.title = title;
        this.chronicleEntryList = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public List<ChronicleEntry> getChronicleEntryList() {
        return chronicleEntryList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Text text = (Text) o;

        if (title != null ? !title.equals(text.title) : text.title != null) {
            return false;
        }
        return chronicleEntryList != null ? chronicleEntryList.equals(text.chronicleEntryList) : text.chronicleEntryList == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (chronicleEntryList != null ? chronicleEntryList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Text{" +
                "title='" + title + '\'' +
                ", chronicleEntryList=" + chronicleEntryList +
                '}';
    }
}
