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
     * @param fileName the name of the xml file
     * @return the {@link Text} object with all information in the xml file.
     * @throws ParserConfigurationException fired when a configuration error
     * occurred
     * @throws IOException fired when the file could not be found
     * @throws SAXException fired when a SAX error or warning occurred
     */
    public static Text createTextFromXml(String fileName) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Load the input XML document, parse it and return an instance of the
        // Document class.
        Document document = builder.parse(new File("src/data/" + fileName));

        Element documentElement = document.getDocumentElement();

        NodeList chronicleEntryNodes = documentElement.getElementsByTagName("div");

        String textTitle = documentElement
                .getElementsByTagName("title")
                .item(0)
                .getChildNodes()
                .item(0)
                .getNodeValue();

        Text text = new Text(textTitle);

        addChronicleEntriesToText(chronicleEntryNodes, text);
        
        return text;
    }

    /**
     * Add all chronicle entry related information from the xml file to the
     * {@link Text} Object.
     *
     * @param chronicleEntryNodes the nodes with the tag name "chronicle-entry"
     * @param text the {@link Text} Object
     */
    private static void addChronicleEntriesToText(NodeList chronicleEntryNodes, Text text) {
        for (int i = 0; i < chronicleEntryNodes.getLength(); i++) {
            Node chronicleEntryNode = chronicleEntryNodes.item(i);
            if (chronicleEntryNode.getNodeType() == Node.ELEMENT_NODE) {
                Element chronicleEntryElement = (Element) chronicleEntryNode;

                String chronicleEntryId = "chronicle-entry-" + String.format("%d", i+1);

                NodeList sentenceNodes = chronicleEntryElement.getElementsByTagName("sentence");

                ChronicleEntry chronicleEntry = new ChronicleEntry();
                chronicleEntry.setId(chronicleEntryId);

                addSentencesToChronicleEntry(sentenceNodes, chronicleEntry);

                text.getChronicleEntryList().add(chronicleEntry);
            }
        }
    }

    /**
     * Add all sentence related information from the xml file to the
     * {@link ChronicleEntry} Object.
     *
     * @param sentenceNodes the nodes with the tag name "sentence"
     * @param chronicleEntry the {@link ChronicleEntry} object
     */
    private static void addSentencesToChronicleEntry(NodeList sentenceNodes, ChronicleEntry chronicleEntry) {
        for (int j = 0; j < sentenceNodes.getLength(); j++) {
            Node sentenceNode = sentenceNodes.item(j);
            if (sentenceNode.getNodeType() == Node.ELEMENT_NODE) {
                Element sentenceElement = (Element) sentenceNode;

                NodeList tokenNodes = sentenceElement.getElementsByTagName("token");

                Sentence sentence = new Sentence();
                sentence.setId(sentenceElement.getAttribute("id"));

                addTokensToSentence(tokenNodes, sentence);

                chronicleEntry.getSentenceList().add(sentence);
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
