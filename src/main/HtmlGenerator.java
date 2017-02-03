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

        String titleHtml = "<title id='text-title'>" + title + "</title>";

        Document doc = Jsoup.parseBodyFragment(titleHtml);

        doc.outputSettings(doc.outputSettings().prettyPrint(false));

        Element body = doc.body();

        addParagraph(body);

        String generatedHtml = body.html();

        writeHtmlToFile(title, generatedHtml);

        return generatedHtml;
    }

    /**
     * Add the paragraph to the html using all information stored
     * inside the {@link Paragraph} Object.
     *
     * @param body the element with the tag name "body"
     */
    private void addParagraph(Element body) {
        Element paragraphElement = body.appendElement("paragraph");

        addSentences(paragraphElement, text.getParagraph().getSentenceList());
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
                String key = "data-" + tokenEntry.getKey();
                key = key.replace("data-id", "id");

                tokenElement.attr(key, tokenEntry.getValue());
            }

            tokenElement.text(tokenElement.attr("data-form"));
            tokenElement.removeAttr("data-form");

            sentenceElment.append(tokenElement.attr("data-presentation-after"));
            tokenElement.removeAttr("data-presentation-after");

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
                    .attr("data-target-id", slash.getTargetId())
                    .attr("data-relation", slash.getRelation());
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

        String fileLocation = "src/serverClientInteraction/texts/" + adjustedTitle + ".html";

        Path path = Paths.get(fileLocation);
        try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("utf-8"))) {
            writer.write(html);
        }
    }
}
