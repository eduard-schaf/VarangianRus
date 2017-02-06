package test;

import main.*;
import org.junit.Before;
import org.junit.Test;

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
    public void getChronicleEntryList() {
        int expected = 3;

        int result = text.getChronicleEntryList().size();

        assertEquals(
                "Should have 3 chronicle entries",
                expected,
                result
        );
    }

    @Test
    public void getChronicleEntryIds() {
        String expected =
                "chronicle-entry-1\n" +
                "chronicle-entry-2\n" +
                "chronicle-entry-3";

        final String result = text
                .getChronicleEntryList()
                .stream()
                .map(ChronicleEntry::getId)
                .collect(Collectors.joining("\n"));

        assertEquals(
                "Should have matching chronicle entry ids",
                expected,
                result
        );
    }

    @Test
    public void getChronicleEntrySentenceListSizes() {
        List<Integer> expected = Arrays.asList(
                3,
                1,
                51
        );

        final List<Integer> result = text
                .getChronicleEntryList()
                .stream()
                .map(chronicleEntry -> chronicleEntry.getSentenceList().size())
                .collect(Collectors.toList());

        assertEquals(
                "Should have matching number of sentences for each chronicle entry",
                expected,
                result
        );
    }

    @Test
    public void getChronicleEntrySentenceListFirstSentenceIds() {
        String expected =
                "123745\n" +
                "123753\n" +
                "123754";

        final String result = text
                .getChronicleEntryList()
                .stream()
                .map(chronicleEntry -> chronicleEntry.getSentenceList().get(0).getId())
                .collect(Collectors.joining("\n"));

        assertEquals(
                "Should have the matching first sentence id of each chronicle entry",
                expected,
                result
        );
    }

    @Test
    public void getChronicleEntrySentenceListFirstSentenceTokenListSizes() {
        List<Integer> expected = Arrays.asList(
                23,
                8,
                16
        );

        final List<Integer> result = text
                .getChronicleEntryList()
                .stream()
                .map(chronicleEntry -> chronicleEntry.getSentenceList().get(0).getTokenList().size())
                .collect(Collectors.toList());

        assertEquals(
                "Should have the matching first sentence token list sizes of each chronicle entry",
                expected,
                result
        );
    }

    @Test
    public void getFirstChronicleEntryFirstSentenceFirstToken() {
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
                .getChronicleEntryList()
                .get(0)
                .getSentenceList()
                .get(0)
                .getTokenList()
                .get(0);

        assertEquals(
                "Should have the matching token in the first chronicle entry, first sentence, first token",
                expected,
                result
        );
    }

    @Test
    public void getFirstChronicleEntryFirstSentenceLastToken() {
        Token expected = new Token()
                .addAttribute("id", "1922320")
                .addAttribute("empty-token-sort", "C")
                .addAttribute("head-id", "1711537")
                .addAttribute("relation", "atr");

        List<Token> tokenList = text
                .getChronicleEntryList()
                .get(0)
                .getSentenceList()
                .get(0)
                .getTokenList();

        Token result = tokenList.get(tokenList.size() - 1);

        assertEquals(
                "Should have the matching token in the first chronicle entry, first sentence, last token",
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
                .getChronicleEntryList().get(2)
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