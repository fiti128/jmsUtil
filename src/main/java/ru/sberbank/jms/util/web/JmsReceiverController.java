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
import ru.sberbank.jms.util.messaging.ReceiveMessageService;
import ru.sberbank.jms.util.messaging.ReceiveMessagesServiceWebsphereMQImpl;
import ru.sberbank.jms.util.messaging.SendMessagesServiceWebsphereMqImpl;
import ru.sberbank.jms.util.services.MessageStorageService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevsky-SA
 * Date: 21.11.13
 * Time: 16:50
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping(value = "/receive")
@Controller
public class JmsReceiverController {

    @Autowired
    @Qualifier(value = "websphereMq")
    private transient ReceiveMessageService receiveMessageService;

    @Autowired
    private transient MessageStorageService messageStorageService;

    @RequestMapping(value = "/start",method = RequestMethod.POST)
    public @ResponseBody
    JmsMessage getJmsConfigurationList(@RequestParam(required = true) String host,
                                             @RequestParam(required = true) int port,
                                             @RequestParam(required = true) String channel,
                                             @RequestParam(required = true) String managerName,
                                             @RequestParam(required = true) String destinationName,
                                             @RequestParam(required = true) String correlationId,
                                             @RequestParam(required = true) boolean isTopic) {

        MqConfig mqConfig = new MqConfig();
        mqConfig.setHost(host);
        mqConfig.setPort(port);
        mqConfig.setChannel(channel);
        mqConfig.setQueueManagerName(managerName);
        mqConfig.setDestinationName(destinationName);
        mqConfig.setIS_TOPIC(isTopic);
        mqConfig.setCorrelationId(correlationId);

        ReceiveMessagesServiceWebsphereMQImpl.DEFAULT_MQ_CONFIG = mqConfig;

        receiveMessageService.startConnection(mqConfig);

        return new JmsMessage();
    }

    @RequestMapping(value = "/stop",method = RequestMethod.POST)
    public @ResponseBody
    JmsMessage stopConnection() {
        receiveMessageService.stopConnection();
                return new JmsMessage();
    }

    @RequestMapping(value = "/get",method = RequestMethod.POST)
    public @ResponseBody
    List<JmsMessage> getMessages() {
        List<JmsMessage> messageList = new ArrayList<JmsMessage>();
        JmsMessage jmsMessage = new JmsMessage();
        jmsMessage.setMessageBody(messageStorageService.getMessagesFromStorage());
        messageStorageService.clearStorage();
        messageList.add(jmsMessage);
        return messageList;
    }
}
