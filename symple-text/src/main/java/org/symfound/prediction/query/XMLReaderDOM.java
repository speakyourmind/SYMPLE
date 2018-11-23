package org.symfound.prediction.query;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Javed Gangjee
 */
public class XMLReaderDOM {

    /**
     *
     */
    public static final String NAME = XMLReaderDOM.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        String filePath = "config/punctuation.xml";
        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("Scenarios");
            //now XML is loaded as Document in memory, lets convert it to Object List
            List<QueryScenario> empList = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                empList.add(getScenario(nodeList.item(i)));
            }
            //lets print Scenario list information
            empList.forEach((emp) -> {
                LOGGER.info(emp.toString());
            });
        } catch (SAXException | ParserConfigurationException | IOException ex) {
            LOGGER.fatal(ex);
        }

    }

    private static QueryScenario getScenario(Node node) {
        //XMLReaderDOM domReader = new XMLReaderDOM();
        QueryScenario emp = new QueryScenario();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            emp.setName(getTagValue("name", element));
            emp.setDeterminer(getTagValue("determiner", element));
            emp.setType(getTagValue("type", element));
        }

        return emp;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

}
