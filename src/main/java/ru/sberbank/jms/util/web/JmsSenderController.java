package ru.sberbank.jms.util.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sberbank.jms.util.domain.JmsConfiguration;
import ru.sberbank.jms.util.domain.JmsMessage;
import ru.sberbank.jms.util.domain.MqConfig;
import ru.sberbank.jms.util.messaging.SendMessagesService;
import ru.sberbank.jms.util.messaging.SendMessagesServiceActiveMqImpl;
import ru.sberbank.jms.util.messaging.SendMessagesServiceWebsphereMqImpl;


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
    @Qualifier(value = "wmqSender")
    SendMessagesService sendMessageService;

    @RequestMapping( method = RequestMethod.POST)
    public @ResponseBody
    JmsMessage getJmsConfigurationList(@RequestParam(required = true) String host,
                                       @RequestParam(required = true) int port,
                                       @RequestParam(required = true) String channel,
                                       @RequestParam(required = true) String managerName,
                                       @RequestParam(required = true) String destinationName,
                                       @RequestParam(required = true) String correlationId,
                                       @RequestParam(required = true) boolean isTopic,
                                       @RequestParam(required = true) String xmlString) {

        MqConfig mqConfig = new MqConfig();
        mqConfig.setHost(host);
        mqConfig.setPort(port);
        mqConfig.setChannel(channel);
        mqConfig.setQueueManagerName(managerName);
        mqConfig.setDestinationName(destinationName);
        mqConfig.setIS_TOPIC(isTopic);
        mqConfig.setCorrelationId(correlationId);

        System.out.println(mqConfig);
        SendMessagesServiceWebsphereMqImpl.DEFAULT_MQ_CONFIG = mqConfig;

        if (xmlString == null || xmlString.trim().length() < 1) {
            xmlString = "Привет работяга!";
        }


        JmsConfiguration jmsConfiguration = null;
        boolean success = sendMessageService.sendMessage(xmlString,null);
        String result = success ? "Сообщение послано успешно!" : "Возникла ошибка при отправке сообщения";
        JmsMessage jmsMessage = new JmsMessage();
        jmsMessage.setMessageBody(result);
        return jmsMessage;
    }
}
