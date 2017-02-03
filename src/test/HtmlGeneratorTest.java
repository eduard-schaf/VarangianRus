package test;

import main.DomParser;
import main.HtmlGenerator;
import main.Text;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

/**
 * @author Eduard Schaf
 * @since 31.01.17
 */
public class HtmlGeneratorTest {
    private Document doc;

    @Before
    public void setup() throws IOException, SAXException, ParserConfigurationException {
        Text text = DomParser.createText();

        HtmlGenerator htmlGenerator = new HtmlGenerator(text);

        String html = htmlGenerator.convertTextToHtml();

        doc = Jsoup.parse(html);
    }

    @Test
    public void getTitle() {
        String expected = "The Varangians are called, The Primary Chronicle, Codex Laurentianus";

        String result = doc.select("#text-title").text();

        assertEquals(
                "Should match the title",
                expected,
                result
        );
    }

    @Test
    public void getParagraph() {
        int expected = 1;

        int result = doc.select("paragraph").size();

        assertEquals(
                "Should have one paragraph",
                expected,
                result
        );
    }

    @Test
    public void getParagraphSentenceListSize() {
        int expected = 55;

        final int result = doc
                .select("paragraph")
                .select("sentence").size();

        assertEquals(
                "Should have matching number of sentences of the paragraph",
                expected,
                result
        );
    }

    @Test
    public void getParagraphSentenceListFirstSentenceId() {
        String expected = "123745";

        final String result = doc
                .select("paragraph")
                .select("sentence").first().attr("id");

        assertEquals(
                "Should have the matching first sentence id of the paragraph",
                expected,
                result
        );
    }

    @Test
    public void getParagraphSentenceListFirstSentenceTokenListSize() {
        int expected = 23;

        final int result = doc
                .select("paragraph")
                .select("sentence").first().select("token").size();

        assertEquals(
                "Should have the matching first sentence token list size of the paragraph",
                expected,
                result
        );
    }

    @Test
    public void getFirstParagraphFirstSentenceFirstToken() {
        Element expected = new Element("token")
                .attr("id", "1711536")
                .attr("data-lemma", "въ")
                .attr("data-part-of-speech", "R-")
                .attr("data-morphology", "---------n")
                .attr("data-head-id", "1711542")
                .attr("data-relation", "adv")
                .text("въ");

        Element result = doc
                .select("paragraph")
                .first()
                .select("sentence")
                .first()
                .select("token")
                .first();

        assertEquals(
                "Should have the matching element in the first paragraph, first sentence, first token",
                expected.toString(),
                result.toString()
        );
    }

    @Test
    public void getFirstParagraphFirstSentenceLastToken() {
        Element expected = new Element("token")
                .attr("id", "1922320")
                .attr("data-empty-token-sort", "C")
                .attr("data-head-id", "1711537")
                .attr("data-relation", "atr");

        Element result = doc
                .select("paragraph")
                .first()
                .select("sentence")
                .first()
                .select("token")
                .last();

        assertEquals(
                "Should have the matching element in the first paragraph, first sentence, last token",
                expected.toString(),
                result.toString()
        );
    }

    @Test
    public void getLastParagraphLastSentenceLastTokenAfterTag() {
        String expected = "·:· ";

        String result = doc
                .select("paragraph")
                .last()
                .select("sentence")
                .last()
                .select("token")
                .last()
                .nextSibling()
                .toString();

        assertEquals(
                "Should have the matching string after the tag in the last paragraph, last sentence, last token",
                expected,
                result
        );
    }

    @Test
    public void slashesInToken() {
        String slashTag = "slash";

        Element slash1 = new Element(slashTag)
                .attr("data-target-id", "1711631")
                .attr("data-relation", "sub");

        Element slash2 = new Element(slashTag)
                .attr("data-target-id", "1711632")
                .attr("data-relation", "aux");

        String expected =
                slash1.toString() + "\n" +
                slash2.toString();

        final List<Element> tokenListHavingTokenWithSlashes = doc
                .select("paragraph")
                .select("sentence")
                .stream()
                .filter(sentence -> "123761".equals(sentence.attr("id")))
                .map(sentence -> sentence.select("token"))
                .collect(Collectors.toList())
                .get(0);

        final String result = tokenListHavingTokenWithSlashes
                .stream()
                .filter(token -> !token.select(slashTag).isEmpty())
                .map(token -> token.select(slashTag))
                .collect(Collectors.toList())
                .get(0)
                .stream()
                .map(Element::toString)
                .collect(Collectors.joining("\n"));

        assertEquals(
                "Should have the matching slash list for this token",
                expected,
                result
        );
    }

    @Test
    public void writeHtmlToFile() throws IOException {

        String htmlFolder = "src/serverClientInteraction/texts/";

        String fileEnding = ".html";
        String expected = htmlFolder + "The-Varangians-are-called-The-Primary-Chronicle-Codex-Laurentianus" + fileEnding;

        String result;

        Path start = Paths.get(htmlFolder);
        int maxDepth = 5;
        try (Stream<Path> stream = Files.find(start, maxDepth, (path, attr) ->
                String.valueOf(path).endsWith(fileEnding))) {
            result = stream
                    .sorted()
                    .map(String::valueOf)
                    .collect(Collectors.joining("\n"));
        }

        assertEquals(
                "Should have created the html file in the expected location",
                expected,
                result
        );
    }
}