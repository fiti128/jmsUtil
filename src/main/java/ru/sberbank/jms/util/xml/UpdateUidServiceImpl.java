package ru.sberbank.jms.util.xml;



import org.apache.commons.codec.digest.DigestUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevsky-SA
 * Date: 20.11.13
 * Time: 17:13
 * To change this template use File | Settings | File Templates.
 */

@Service("updateUidService")
public class UpdateUidServiceImpl implements UpdateUidService {

    public static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String ENCODING = "UTF-8";
    public static final String ERROR_STRING = "Произошла ошибка при обновлении UID. Вывод старой версии\n";

    public String updateUid(String xml) {
        try {
        SAXBuilder builder = new SAXBuilder();

        Document doc = (Document) builder.build(new ByteArrayInputStream(xml.getBytes(Charset.forName(ENCODING))));
        Element rootNode = doc.getRootElement();

        List<Element> elementList = rootNode.getChildren();

        Element depoAccInfoRq = elementList.get(0);
            List<Element> uidList = depoAccInfoRq.getChildren();
            uidList.get(0).setText(getUniqueHash());
            uidList.get(1).setText(new SimpleDateFormat(DATE_PATTERN).format(new Date()));
            uidList.get(2).setText(getUniqueHash());


        XMLOutputter xmlOutput = new XMLOutputter();

        // display nice nice      -
        xmlOutput.setFormat(Format.getPrettyFormat());
        xml = xmlOutput.outputString(doc);
        } catch (Exception e) {
            xml = ERROR_STRING + xml;
            e.printStackTrace();
        }
        return xml;
          //To change body of implemented methods use File | Settings | File Templates.
    }

    private String getUniqueHash() {
        return DigestUtils.md5Hex(String.valueOf(System.nanoTime()));
    }


}
