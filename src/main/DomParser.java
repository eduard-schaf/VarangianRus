package main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * @author Eduard Schaf
 * @since 29.01.17
 */
public class DomParser {

    private DomParser() {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * Create a {@link Text} Object from the information inside the xml file.
     *
     * @return the {@link Text} object with all information in the xml file.
     * @throws ParserConfigurationException fired when a configuration error
     * occurred
     * @throws IOException fired when the file could not be found
     * @throws SAXException fired when a SAX error or warning occurred
     */
    public static Text createText() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Load the input XML document, parse it and return an instance of the
        // Document class.
        Document document = builder.parse(new File("src/data/varangians.xml"));

        Element documentElement = document.getDocumentElement();

        NodeList paragraphNodes = documentElement.getElementsByTagName("div");

        String textTitle = documentElement
                .getElementsByTagName("title")
                .item(0)
                .getChildNodes()
                .item(0)
                .getNodeValue();

        Text text = new Text(textTitle);

        addParagraphsToText(paragraphNodes, text);
        
        return text;
    }

    /**
     * Add all paragraph related information from the xml file to the
     * {@link Text} Object.
     *
     * @param paragraphNodes the nodes with the tag name "paragraph"
     * @param text the {@link Text} Object
     */
    private static void addParagraphsToText(NodeList paragraphNodes, Text text) {
        for (int i = 0; i < paragraphNodes.getLength(); i++) {
            Node paragraphNode = paragraphNodes.item(i);
            if (paragraphNode.getNodeType() == Node.ELEMENT_NODE) {
                Element paragraphElement = (Element) paragraphNode;

                String paragraphId = "paragraph-" + String.format("%d", i+1);
                String paragraphTitle = paragraphElement
                        .getElementsByTagName("title")
                        .item(0)
                        .getChildNodes()
                        .item(0)
                        .getNodeValue();

                NodeList sentenceNodes = paragraphElement.getElementsByTagName("sentence");

                Paragraph paragraph = new Paragraph();
                paragraph.setId(paragraphId);
                paragraph.setTitle(paragraphTitle);

                addSentencesToParagraph(sentenceNodes, paragraph);

                text.getParagraphList().add(paragraph);
            }
        }
    }

    /**
     * Add all sentence related information from the xml file to the
     * {@link Paragraph} Object.
     *
     * @param sentenceNodes the nodes with the tag name "sentence"
     * @param paragraph the {@link Paragraph} object
     */
    private static void addSentencesToParagraph(NodeList sentenceNodes, Paragraph paragraph) {
        for (int j = 0; j < sentenceNodes.getLength(); j++) {
            Node sentenceNode = sentenceNodes.item(j);
            if (sentenceNode.getNodeType() == Node.ELEMENT_NODE) {
                Element sentenceElement = (Element) sentenceNode;

                NodeList tokenNodes = sentenceElement.getElementsByTagName("token");

                Sentence sentence = new Sentence();
                sentence.setId(sentenceElement.getAttribute("id"));

                addTokensToSentence(tokenNodes, sentence);

                paragraph.getSentenceList().add(sentence);
            }
        }
    }

    /**
     * Add all token related information from the xml file to the
     * {@link Sentence} Object.
     *
     * @param tokenNodes the nodes with the tag name "sentence"
     * @param sentence the {@link Sentence} object
     */
    private static void addTokensToSentence(NodeList tokenNodes, Sentence sentence) {
        for (int k = 0; k < tokenNodes.getLength(); k++) {
            Node tokenNode = tokenNodes.item(k);
            if (tokenNode.getNodeType() == Node.ELEMENT_NODE) {
                Element tokenElement = (Element) tokenNode;

                Token token = new Token();

                addAttributeToToken("id", tokenElement, token);
                addAttributeToToken("empty-token-sort", tokenElement, token);
                addAttributeToToken("form", tokenElement, token);
                addAttributeToToken("lemma", tokenElement, token);
                addAttributeToToken("part-of-speech", tokenElement, token);
                addAttributeToToken("morphology", tokenElement, token);
                addAttributeToToken("head-id", tokenElement, token);
                addAttributeToToken("relation", tokenElement, token);
                addAttributeToToken("presentation-after", tokenElement, token);

                NodeList slashNodes = tokenElement.getElementsByTagName("slash");

                addSlashesToToken(slashNodes, token);

                sentence.getTokenList().add(token);
            }
        }
    }

    /**
     * Add the given attribute from the token element to the token.
     *
     * @param attribute the given attribute
     * @param tokenElement the token element the attribute is located in
     * @param token the token the attribute is add to
     */
    private static void addAttributeToToken(String attribute, Element tokenElement, Token token) {
        String attributeValue = tokenElement.getAttribute(attribute);
        if(!attributeValue.isEmpty()){
            token.addAttribute(attribute, attributeValue);
        }
    }

    /**
     * Add all slash related information from the xml file to the
     * {@link Token} Object.
     *
     * @param slashNodes the nodes with the tag name "slash"
     * @param token the {@link Token} object
     */
    private static void addSlashesToToken(NodeList slashNodes, Token token) {
        for (int j = 0; j < slashNodes.getLength(); j++) {
            Node slashNode = slashNodes.item(j);
            if (slashNode.getNodeType() == Node.ELEMENT_NODE) {
                Element slashElement = (Element) slashNode;

                Slash slash = new Slash(
                        slashElement.getAttribute("target-id"),
                        slashElement.getAttribute("relation")
                );

                token.getSlashList().add(slash);
            }
        }
    }
}
