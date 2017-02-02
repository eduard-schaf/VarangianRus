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
import java.util.Arrays;
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
        String expected = "The Primary Chronicle, Codex Laurentianus";

        String result = doc.select("#text-title").text();

        assertEquals(
                "Should match the title",
                expected,
                result
        );
    }

    @Test
    public void getParagraphs() {
        int expected = 3;

        int result = doc.select("paragraph").size();

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

        final String result = doc
                .select("paragraph")
                .stream()
                .map(element -> element.attr("id"))
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

        final String result = doc
                .select("paragraph title")
                .stream()
                .map(Element::text)
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

        final List<Integer> result = doc
                .select("paragraph")
                .stream()
                .map(paragraph -> paragraph.select("sentence").size())
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

        final String result = doc
                .select("paragraph")
                .stream()
                .map(paragraph -> paragraph.select("sentence").first().attr("id"))
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

        final List<Integer> result = doc
                .select("paragraph")
                .stream()
                .map(paragraph -> paragraph.select("sentence").first().select("token").size())
                .collect(Collectors.toList());

        assertEquals(
                "Should have the matching first sentence token list sizes of each paragraph",
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
                .get(2)
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

        String expected = htmlFolder + "The-Primary-Chronicle-Codex-Laurentianus" + fileEnding;

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