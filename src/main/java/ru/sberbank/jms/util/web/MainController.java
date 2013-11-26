package ru.sberbank.jms.util.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import ru.sberbank.jms.util.domain.JmsConfiguration;
import ru.sberbank.jms.util.domain.XmlMessage;
import ru.sberbank.jms.util.messaging.ReceiveMessagesServiceWebsphereMQImpl;
import ru.sberbank.jms.util.messaging.SendMessagesServiceWebsphereMqImpl;
import ru.sberbank.jms.util.xml.UpdateUidService;



/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevsky-SA
 * Date: 16.11.13
 * Time: 18:01
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class MainController {
    public static final String ERROR_UPLOAD_STRING_NOT_XML = "Для загрузки требуется xml файл!";
    public static final String UPLOAD_EXTENSION_PATTERN = ".*xml";
    public static final String DEFAULT_ENCODING = "Windows-1251";

    private ServletFileUpload uploader;

    @Autowired
    UpdateUidService updateUidService;
    XmlMessage xmlMessage = new XmlMessage();

    @RequestMapping(value="/" ,method = RequestMethod.GET)
    public String showMainPage(Model uiModel)  {
        List<JmsConfiguration> list = new ArrayList<JmsConfiguration>();
        List<String> configurationList = new ArrayList<String>();
        for (JmsConfiguration config : list) {
            configurationList.add(config.getConfigurationName());
        }

        JmsConfiguration jmsConfiguration = new JmsConfiguration();
        uiModel.addAttribute("senderConfig", SendMessagesServiceWebsphereMqImpl.DEFAULT_MQ_CONFIG);
        uiModel.addAttribute("receiverConfig", ReceiveMessagesServiceWebsphereMQImpl.DEFAULT_MQ_CONFIG);
        uiModel.addAttribute("xml",xmlMessage);
        return "index";
    }
    @RequestMapping(value="/updateUid" ,method = RequestMethod.POST)
    @ResponseBody
    XmlMessage  updateUid(@RequestParam(required = true) String xml)  {
        String str = updateUidService.updateUid(xml);
        xmlMessage = new XmlMessage();
        xmlMessage.setXmlText(str);
        return xmlMessage;
    }

    @RequestMapping(value="/" ,method = RequestMethod.POST)
   public String upload(MultipartHttpServletRequest request,Model uiModel, @ModelAttribute("fileUpload") FileUpload fileUpload) throws IOException {

        MultipartFile mf = fileUpload.getMf();
        if(mf!=null) {
            System.out.println(mf.getOriginalFilename());
        }
        xmlMessage = new XmlMessage();
        xmlMessage.setXmlText(ERROR_UPLOAD_STRING_NOT_XML);

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
        }
            uiModel.addAttribute("result",xmlMessage);

        return "iframe";

    }

    @RequestMapping(value = "/getConfigurationList", method = RequestMethod.POST)
    public @ResponseBody
    JmsConfiguration getJmsConfigurationList(@RequestParam(required = false) String name) {

        JmsConfiguration jmsConfiguration = new JmsConfiguration();

        return jmsConfiguration;
    }

    private String getEncoding() {
        return DEFAULT_ENCODING;
    }

}
