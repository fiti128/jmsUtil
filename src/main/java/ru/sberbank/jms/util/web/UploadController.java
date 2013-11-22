package ru.sberbank.jms.util.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import ru.sberbank.jms.util.domain.JmsConfiguration;
import ru.sberbank.jms.util.domain.XmlMessage;
import ru.sberbank.jms.util.xml.UpdateUidService;

import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevsky-SA
 * Date: 18.11.13
 * Time: 12:16
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping(value = "/upload")
@Controller
public class UploadController {
    public static final String ERROR_UPLOAD_STRING_NOT_XML = "Для загрузки требуется xml файл!";
    public static final String UPLOAD_EXTENSION_PATTERN = ".*xml";
    public static final String DEFAULT_ENCODING = "Windows-1251";
    @Autowired
    private transient JmsTemplate jmsTopicTemplate;

    @Autowired
    UpdateUidService updateUidService;


    private XmlMessage xmlMessage;

    @RequestMapping( method = RequestMethod.POST)
    public @ResponseBody
    JmsConfiguration upload(MultipartHttpServletRequest request) throws IOException {

        JmsConfiguration jmsConfiguration = new JmsConfiguration();
        jmsConfiguration.setUrl(ERROR_UPLOAD_STRING_NOT_XML);
        //0. notice, we have used MultipartHttpServletRequest

        //1. get the files from the request object
        Iterator<String> itr =  request.getFileNames();

        MultipartFile mpf = request.getFile(itr.next());
        String fileName = mpf.getOriginalFilename();


        if (fileName.matches(UPLOAD_EXTENSION_PATTERN)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mpf.getInputStream(), getEncoding()));
            StringBuilder sb = new StringBuilder();
            String str;
            while((str =reader.readLine()) != null) {
               sb.append(str).append("\n\r");
            }
            str = sb.toString();
            str = updateUidService.updateUid(str);
            xmlMessage = new XmlMessage();
            xmlMessage.setXmlText(str);
            jmsConfiguration.setUrl(str);
        }
        return jmsConfiguration;

    }

    private String getEncoding() {
        return DEFAULT_ENCODING;
    }

    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public @ResponseBody
    XmlMessage sendMessage(MultipartHttpServletRequest request)  {

        JmsConfiguration jmsConfiguration = new JmsConfiguration();
        List<JmsConfiguration> jmsConfigurationList = JmsConfiguration.findAllOptions();
        for (JmsConfiguration config : jmsConfigurationList) {
            if (config.getConfigurationName().equals("name")) {
                jmsConfiguration = config;
            }
        }
        jmsTopicTemplate.convertAndSend(xmlMessage.getXmlText());

        return xmlMessage;
    }
}
