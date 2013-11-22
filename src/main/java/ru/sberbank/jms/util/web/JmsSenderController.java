package ru.sberbank.jms.util.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sberbank.jms.util.domain.JmsConfiguration;
import ru.sberbank.jms.util.domain.JmsMessage;
import ru.sberbank.jms.util.messaging.ManagingSendMessagesService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevsky-SA
 * Date: 22.11.13
 * Time: 9:26
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping(value = "/send")
@Controller
public class JmsSenderController {
    @Autowired
    ManagingSendMessagesService sendMessageService;

    @RequestMapping( method = RequestMethod.POST)
    public @ResponseBody
    JmsMessage getJmsConfigurationList(@RequestParam(required = true) String sendConfigurationId,@RequestParam(required = true) String xmlString) {

//        JmsConfiguration jmsConfiguration = JmsConfiguration.findJmsConfiguration(sendConfigurationId);
        if (xmlString == null || xmlString.trim().length() < 1) {
            xmlString = "Привет чувак!";
        }
        JmsConfiguration jmsConfiguration = null;
        boolean success = sendMessageService.sendMessage(xmlString,jmsConfiguration);
        String result = success ? "Сообщение послано успешно!" : "Возникла ошибка при отправке сообщения";
        JmsMessage jmsMessage = new JmsMessage();
        jmsMessage.setMessageBody(result);
        return jmsMessage;
    }
}
