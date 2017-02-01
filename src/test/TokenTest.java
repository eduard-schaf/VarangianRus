package test;

import main.Token;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * @author Eduard Schaf
 * @since 30.01.17
 */
public class TokenTest {
    @Test
    public void addAndGetAttributeSuccess() throws Exception {
        Token token = new Token().addAttribute("attr1", "value1");

        Optional expected = Optional.of("value1");

        Optional result = token.getAttribute("attr1");

        assertEquals(
                "Should store and retrieve an attribute of the token",
                expected,
                result
        );
    }

    @Test
    public void addAndGetAttributeFailure() throws Exception {
        Token token = new Token().addAttribute("attr1", "value1");

        Optional expected = Optional.empty();

        Optional result = token.getAttribute("unknown-attribute");

        assertEquals(
                "Should store but fail to retrieve an attribute of the token," +
                        "as none of the attributes stored do match the attribute requested",
                expected,
                result
        );
    }

    @Test
    public void testToString() throws Exception {
        Token token = new Token()
                .addAttribute("attr1", "value1")
                .addAttribute("attr2", "value2");

        String expected = "<token attr1=\"value1\" attr2=\"value2\"></token>";

        String result = token.toString();

        assertEquals(
                "Should display the string representation of the token",
                expected,
                result
        );
    }

}