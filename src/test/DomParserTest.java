package test;

import main.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Eduard Schaf
 * @since 30.01.17
 */
public class DomParserTest {
    private Text text;

    @Before
    public void setup() throws Exception {
        text = DomParser.createTextFromXml("varangians.xml");
    }

    @Test
    public void getTitle() {
        String expected = "The Varangians are called, The Primary Chronicle, Codex Laurentianus";

        String result = text.getTitle();

        assertEquals(
                "Should match the title",
                expected,
                result
        );
    }

    @Test
    public void getParagraph() {
        assertTrue(
                "Should have a paragraph, as the sentence list is not empty",
                !text.getParagraph().getSentenceList().isEmpty()
        );
    }

    @Test
    public void getParagraphSentenceListSize() {
        int expected = 55;

        final int result = text
                .getParagraph()
                .getSentenceList()
                .size();

        assertEquals(
                "Should have matching number of sentences of the paragraph",
                expected,
                result
        );
    }

    @Test
    public void getParagraphSentenceListFirstSentenceId() {
        String expected = "123745";

        final String result = text
                .getParagraph()
                .getSentenceList()
                .get(0)
                .getId();

        assertEquals(
                "Should have the matching first sentence id of the paragraph",
                expected,
                result
        );
    }

    @Test
    public void getParagraphSentenceListFirstSentenceTokenListSize() {
        int expected = 23;

        final int result = text
                .getParagraph()
                .getSentenceList()
                .get(0)
                .getTokenList()
                .size();

        assertEquals(
                "Should have the matching first sentence token list size of the paragraph",
                expected,
                result
        );
    }

    @Test
    public void getFirstParagraphFirstSentenceFirstToken() {
        Token expected = new Token()
                .addAttribute("id", "1711536")
                .addAttribute("form", "въ")
                .addAttribute("lemma", "въ")
                .addAttribute("part-of-speech", "R-")
                .addAttribute("morphology", "---------n")
                .addAttribute("head-id", "1711542")
                .addAttribute("relation", "adv")
                .addAttribute("presentation-after", " ");

        Token result = text
                .getParagraph()
                .getSentenceList()
                .get(0)
                .getTokenList()
                .get(0);

        assertEquals(
                "Should have the matching token in the first paragraph, first sentence, first token",
                expected,
                result
        );
    }

    @Test
    public void getFirstParagraphFirstSentenceLastToken() {
        Token expected = new Token()
                .addAttribute("id", "1922320")
                .addAttribute("empty-token-sort", "C")
                .addAttribute("head-id", "1711537")
                .addAttribute("relation", "atr");

        List<Token> tokenList = text
                .getParagraph()
                .getSentenceList()
                .get(0)
                .getTokenList();

        Token result = tokenList.get(tokenList.size() - 1);

        assertEquals(
                "Should have the matching token in the first paragraph, first sentence, last token",
                expected,
                result
        );
    }

    @Test
    public void slashesInToken() {
        Slash slash1 = new Slash("1711631", "sub");
        Slash slash2 = new Slash("1711632", "aux");

        List<Slash> expected = Arrays.asList(
                slash1,
                slash2
        );

        final List<Token> tokenListHavingTokenWithSlashes = text
                .getParagraph()
                .getSentenceList()
                .stream()
                .filter(sentence -> "123761".equals(sentence.getId()))
                .map(Sentence::getTokenList)
                .collect(Collectors.toList())
                .get(0);

        final List<Slash> result = tokenListHavingTokenWithSlashes
                .stream()
                .filter(token -> !token.getSlashList().isEmpty())
                .map(Token::getSlashList)
                .collect(Collectors.toList())
                .get(0);

        assertEquals(
                "Should have the matching slash list for this token",
                expected,
                result
        );
    }
}