package test;

import main.*;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * @author Eduard Schaf
 * @since 30.01.17
 */
public class DomParserTest {
    private Text text;

    @Before
    public void setup() throws IOException, SAXException, ParserConfigurationException {
        text = DomParser.createText();
    }

    @Test
    public void getTitle() {
        String expected = "The Primary Chronicle, Codex Laurentianus";

        String result = text.getTitle();

        assertEquals(
                "Should match the title",
                expected,
                result
        );
    }

    @Test
    public void getParagraphList() {
        int expected = 3;

        int result = text.getParagraphList().size();

        assertEquals(
                "Should have 3 paragraphs",
                expected,
                result
        );
    }

    @Test
    public void getParagraphIds() {
        String expected =
                "paragraph-1\n" +
                "paragraph-2\n" +
                "paragraph-3";

        final String result = text
                .getParagraphList()
                .stream()
                .map(Paragraph::getId)
                .collect(Collectors.joining("\n"));

        assertEquals(
                "Should have matching paragraph ids",
                expected,
                result
        );
    }

    @Test
    public void getParagraphTitles() {
        String expected =
                "6367: Tribute to the Varangians\n" +
                "6368\n" +
                "6369–6370: The Varangians come to rule the Land of the Rus";

        final String result = text
                .getParagraphList()
                .stream()
                .map(Paragraph::getTitle)
                .collect(Collectors.joining("\n"));

        assertEquals(
                "Should have matching paragraph titles",
                expected,
                result
        );
    }

    @Test
    public void getParagraphSentenceListSizes() {
        List<Integer> expected = Arrays.asList(
                3,
                1,
                51
        );

        final List<Integer> result = text
                .getParagraphList()
                .stream()
                .map(paragraph -> paragraph.getSentenceList().size())
                .collect(Collectors.toList());

        assertEquals(
                "Should have matching number of sentences of each paragraph",
                expected,
                result
        );
    }

    @Test
    public void getParagraphSentenceListFirstSentenceIds() {
        String expected =
                "123745\n" +
                "123753\n" +
                "123754";

        final String result = text
                .getParagraphList()
                .stream()
                .map(paragraph -> paragraph.getSentenceList().get(0).getId())
                .collect(Collectors.joining("\n"));

        assertEquals(
                "Should have the matching first sentence id of each paragraph",
                expected,
                result
        );
    }

    @Test
    public void getParagraphSentenceListFirstSentenceTokenListSizes() {
        List<Integer> expected = Arrays.asList(
                23,
                8,
                16
        );

        final List<Integer> result = text
                .getParagraphList()
                .stream()
                .map(paragraph -> paragraph.getSentenceList().get(0).getTokenList().size())
                .collect(Collectors.toList());

        assertEquals(
                "Should have the matching first sentence token list sizes of each paragraph",
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
                .getParagraphList()
                .get(0)
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
                .getParagraphList()
                .get(0)
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
                .getParagraphList().get(2)
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