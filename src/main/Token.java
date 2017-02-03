package main;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Eduard Schaf
 * @since 29.01.17
 */
public class Token {
    private Map<String, String> attributes;
    private List<Slash> slashList;

    public Token() {
        this.attributes = new LinkedHashMap<>();
        this.slashList = new ArrayList<>();
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public List<Slash> getSlashList() {
        return slashList;
    }

    /**
     * Get the attribute of the token using a key.
     *
     * @param key the attribute key
     * @return Optional.of(value) if the attribute exists,
     * Optional.empty() otherwise
     */
    public Optional<String> getAttribute(final String key) {
        return Optional.ofNullable(attributes.get(key));
    }

    /**
     * Add an attribute to the token using a key and a value.
     *
     * @param key the attribute key
     * @param value the attribute value
     * @return an instance of this token with the added attribute
     */
    public Token addAttribute(final String key, final String value) {
        attributes.put(key, value);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Token token = (Token) o;

        if (attributes != null ? !attributes.equals(token.attributes) : token.attributes != null) {
            return false;
        }
        return slashList != null ? slashList.equals(token.slashList) : token.slashList == null;
    }

    @Override
    public int hashCode() {
        int result = attributes != null ? attributes.hashCode() : 0;
        result = 31 * result + (slashList != null ? slashList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final String atrs = attributes
                .entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=\"" + entry.getValue() + "\"")
                .collect(Collectors.joining(" "));

        String result = "<token " + atrs + ">";

        if(!slashList.isEmpty()){
            String slashListString = slashList
                    .stream()
                    .map(Slash::toString)
                    .collect(Collectors.joining(System.lineSeparator()));

            result = result + System.lineSeparator() + slashListString;

        }

        result += "</token>";

        return result;
    }
}
