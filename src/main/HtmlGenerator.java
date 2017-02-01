package main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * @author Eduard Schaf
 * @since 29.01.17
 */
public class HtmlGenerator {
    private Text text;

    /**
     * The constructor of the {@link HtmlGenerator}.
     *
     * @param text the {@link Text} Object.
     */
    public HtmlGenerator(Text text) {
        this.text = text;
    }

    /**
     * Convert the {@link Text} Object into html format
     * and write it to a file.
     *
     * @return the generated html
     * @throws IOException fired when the file location does not exist
     */
    public String convertTextToHtml() throws IOException{
        String title = text.getTitle();

        String html =
                "<html>" +
                    "<head>" +
                        "<title>" +
                            title +
                        "</title>" +
                    "</head>" +
                    "<body>" +
                    "</body>" +
                "</html>";

        Document doc = Jsoup.parse(html);

        doc.outputSettings(doc.outputSettings().prettyPrint(false));

        addParagraphs(doc.select("body").first());

        String generatedHtml = doc.html();

        writeHtmlToFile(title, generatedHtml);

        return generatedHtml;
    }

    /**
     * Add paragraphs to the html using all information stored
     * inside the {@link Paragraph} Object.
     *
     * @param body the element with the tag name "body"
     */
    private void addParagraphs(Element body) {
        for(Paragraph paragraph: text.getParagraphList()){
            Element paragraphElement = body
                    .appendElement("paragraph")
                    .attr("id", paragraph.getId());

            paragraphElement
                    .appendElement("title")
                    .text(paragraph.getTitle());

            addSentences(paragraphElement, paragraph.getSentenceList());
        }
    }

    /**
     * Add sentences to the html using all information stored
     * inside the {@link Sentence} Object.
     *
     * @param paragraphElement the element with the tag name "paragraph"
     * @param sentenceList the sentences inside this paragraph
     */
    private void addSentences(Element paragraphElement, List<Sentence> sentenceList) {
        for(Sentence sentence: sentenceList){
            Element sentenceElment = paragraphElement
                    .appendElement("sentence")
                    .attr("id", sentence.getId());

            addTokens(sentenceElment, sentence.getTokenList());
        }
    }

    /**
     * Add Tokens to the html using all information stored
     * inside the {@link Token} Object.
     *
     * @param sentenceElment the element with the tag name "sentence"
     * @param tokenList the tokens inside this sentence
     */
    private void addTokens(Element sentenceElment, List<Token> tokenList) {
        for(Token token: tokenList){
            Element tokenElement = sentenceElment.appendElement("token");

            for(Map.Entry<String, String> tokenEntry: token.getAttributes().entrySet()){
                tokenElement.attr(tokenEntry.getKey(), tokenEntry.getValue());
            }

            tokenElement.text(tokenElement.attr("form"));
            tokenElement.removeAttr("form");

            sentenceElment.append(tokenElement.attr("presentation-after"));
            tokenElement.removeAttr("presentation-after");

            addSlashes(tokenElement, token.getSlashList());
        }
    }

    /**
     * Add slashes to the html using all information stored
     * inside the {@link Slash} Object.
     *
     * @param tokenElement the element with the tag name "token"
     * @param slashList the slashes inside this token
     */
    private void addSlashes(Element tokenElement, List<Slash> slashList) {
        for(Slash slash: slashList){
            tokenElement
                    .appendElement("slash")
                    .attr("target-id", slash.getTargetId())
                    .attr("relation", slash.getRelation());
        }
    }

    /**
     * Write the html to a file with the adjusted title as name.
     *
     * @param title the name of the file to write
     * @param html the html to write
     * @throws IOException fired when the file location does not exist
     */
    private void writeHtmlToFile(String title, String html) throws IOException {
        String adjustedTitle = title.replace(",", "").replace(" ", "-");

        String fileLocation = "src/html/" + adjustedTitle + ".html";

        Path path = Paths.get(fileLocation);
        try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("utf-8"))) {
            writer.write(html);
        }
    }
}
