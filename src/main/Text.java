package main;

/**
 * @author Eduard Schaf
 * @since 29.01.17
 */
public class Text {
    private String title;
    private Paragraph paragraph;

    public Text(String title) {
        this.title = title;
        this.paragraph = new Paragraph();
    }

    public void setParagraph(Paragraph paragraph) {
        this.paragraph = paragraph;
    }

    public String getTitle() {
        return title;
    }

    public Paragraph getParagraph() {
        return paragraph;
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
        return paragraph != null ? paragraph.equals(text.paragraph) : text.paragraph == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (paragraph != null ? paragraph.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Text{" +
                "title='" + title + '\'' +
                ", paragraph=" + paragraph +
                '}';
    }
}
