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
    JmsMessage getJmsConfigurationList(@RequestParam(required = true) String factoryName,
                                       @RequestParam(required = true) String correlationId,
                                       @RequestParam(required = true) String queueName,
                                       @RequestParam(required = true) String xmlString) {

        MqConfig mqConfig = new MqConfig();
        mqConfig.setConnectionFactoryName(factoryName);
        mqConfig.setQueueName(queueName);
        mqConfig.setCorrelationId(correlationId);

        SendMessagesServiceWebsphereMqImpl.DEFAULT_MQ_CONFIG = mqConfig;

        if (xmlString == null || xmlString.trim().length() < 1) {
            xmlString = "Это послание Вы видете, т.к. пользователь послал пустое сообщение!";
        }


        JmsConfiguration jmsConfiguration = null;
        boolean success = sendMessageService.sendMessage(xmlString,null);
        String result = success ? "Сообщение послано успешно!" : "Возникла ошибка при отправке сообщения";
        JmsMessage jmsMessage = new JmsMessage();
        jmsMessage.setMessageBody(result);
        return jmsMessage;
    }
}
