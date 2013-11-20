package ru.sberbank.jms.util.xml;


import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevsky-SA
 * Date: 20.11.13
 * Time: 17:13
 * To change this template use File | Settings | File Templates.
 */


public class UpdateUidServiceImpl implements UpdateUidService {
    public String updateUid(String xml) {
        try {
        SAXBuilder builder = new SAXBuilder();

        Document doc = (Document) builder.build(xml);
        Element rootNode = doc.getRootElement();

        // update staff id attribute
        Element staff = rootNode.getChild("ns2:DepoAccInfoRq").getChild("ns2:RqUID").setText("Yoho");
        staff.getAttribute("id").setValue("2");

        // add new age element
        Element age = new Element("age").setText("28");
        staff.addContent(age);

        // update salary value
        staff.getChild("salary").setText("7000");

        // remove firstname element
        staff.removeChild("firstname");

        XMLOutputter xmlOutput = new XMLOutputter();

        // display nice nice      -
        xmlOutput.setFormat(Format.getPrettyFormat());
        xml = xmlOutput.outputString(doc);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return xml;
          //To change body of implemented methods use File | Settings | File Templates.
    }
}
