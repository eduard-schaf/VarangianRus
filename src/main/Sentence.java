package main;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eduard Schaf
 * @since 29.01.17
 */
public class Sentence {
    private String id;
    private List<Token> tokenList;

    public Sentence() {
        this.id = "";
        this.tokenList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Token> getTokenList() {
        return tokenList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Sentence sentence = (Sentence) o;

        if (id != null ? !id.equals(sentence.id) : sentence.id != null) {
            return false;
        }
        return tokenList != null ? tokenList.equals(sentence.tokenList) : sentence.tokenList == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tokenList != null ? tokenList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "id='" + id + '\'' +
                ", tokenList=" + tokenList +
                '}';
    }
}
