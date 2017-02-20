package test;

import main.DomParser;
import main.HtmlGenerator;
import main.Text;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    public void setup() throws Exception {
        Text text = DomParser.createTextFromXml("varangians.xml");

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
    public void getChronicleEntries() {
        int expected = 3;

        int result = doc.select("chronicle-entry").size();

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

        final String result = doc
                .select("chronicle-entry")
                .stream()
                .map(element -> element.attr("id"))
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

        final List<Integer> result = doc
                .select("chronicle-entry")
                .stream()
                .map(chronicleEntry -> chronicleEntry.select("sentence").size())
                .collect(Collectors.toList());

        assertEquals(
                "Should have matching number of sentences of each chronicle entry",
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

        final String result = doc
                .select("chronicle-entry")
                .stream()
                .map(chronicleEntry -> chronicleEntry.select("sentence").first().attr("id"))
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

        final List<Integer> result = doc
                .select("chronicle-entry")
                .stream()
                .map(chronicleEntry -> chronicleEntry.select("sentence").first().select("token").size())
                .collect(Collectors.toList());

        assertEquals(
                "Should have the matching first sentence token list sizes of each chronicle entry",
                expected,
                result
        );
    }

    @Test
    public void tokenWithoutDistractors() {
        Element expected = new Element("token")
                .attr("id", "1711536")
                .attr("data-form", "въ")
                .attr("data-lemma", "въ")
                .attr("data-part-of-speech", "R-")
                .attr("data-morphology", "---------n")
                .attr("data-head-id", "1711542")
                .attr("data-relation", "adv")
                .attr("data-answers", "въ")
                .text("въ");

        Element result = doc
                .select("#1711536")
                .first();

        assertEquals(
                "Should have the matching element without distractors",
                expected.toString(),
                result.toString()
        );
    }

    @Test
    public void tokenWithDistractorsAndAlternatives() {
        Element expected = new Element("token")
                .attr("id", "1711871")
                .attr("data-form", "варѧги")
                .attr("data-lemma", "варягъ")
                .attr("data-part-of-speech", "Nb")
                .attr("data-morphology", "-p---ma--i")
                .attr("data-head-id", "1711872")
                .attr("data-relation", "obj")
                .attr("data-distractors", "варѧзи;варѧга;варꙗга;варѧгъ;варѧгомъ")
                .attr("data-answers", "варѧги;вариги;варѧгы;вѧрѧгы;варяги;варягы;вярягы")
                .text("варѧги");

        Element result = doc
                .select("#1711871")
                .first();

        assertEquals(
                "Should have the matching element with distractors",
                expected.toString(),
                result.toString()
        );
    }

    @Test
    public void tokenWithEmptyTokenSort() {
        Element expected = new Element("token")
                .attr("id", "1922320")
                .attr("data-empty-token-sort", "C")
                .attr("data-head-id", "1711537")
                .attr("data-relation", "atr");

        Element result = doc
                .select("#1922320")
                .first();

        assertEquals(
                "Should have the matching element with the empty token sort attribute",
                expected.toString(),
                result.toString()
        );
    }

    @Test
    public void textAfterTag() {
        String expected = "·:· ";

        String result = doc
                .select("#1711882")
                .first()
                .nextSibling()
                .toString();

        assertEquals(
                "Should have the matching string after the tag",
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
                slash1.toString() + System.lineSeparator() +
                slash2.toString();

        final String result = doc
                .select("#1711636")
                .first()
                .select(slashTag)
                .stream()
                .map(Element::toString)
                .collect(Collectors.joining(System.lineSeparator()));

        assertEquals(
                "Should have the matching slash list for this token",
                expected,
                result
        );
    }

    @Test
    public void writeHtmlToFile() throws Exception {
        Path dataFolder = Paths.get("src/data/");

        String htmlFolder = "src/serverClientInteraction/texts/";

        String fileEnding = ".html";

        Set<String> expectedSet = new HashSet<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dataFolder, "*.{xml}")) {
            for (Path entry: stream) {
                Text text = DomParser.createTextFromXml(entry.getFileName().toString());

                HtmlGenerator htmlGenerator = new HtmlGenerator(text);

                htmlGenerator.convertTextToHtml();

                String htmlFilePath = htmlFolder.concat(htmlGenerator.getFileName()).concat(fileEnding);

                expectedSet.add(htmlFilePath);
            }
        }

        Set<String> resultSet;

        Path start = Paths.get(htmlFolder);
        int maxDepth = 5;
        try (Stream<Path> stream = Files.find(start, maxDepth, (path, attr) ->
                String.valueOf(path).endsWith(fileEnding))) {
            resultSet = stream
                    .map(String::valueOf)
                    .collect(Collectors.toSet());
        }

        assertEquals(
                "Should have created the html files in the expected location",
                expectedSet,
                resultSet
        );
    }
}