package ru.sberbank.jms.util.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sberbank.jms.util.domain.JmsMessage;
import ru.sberbank.jms.util.domain.MqConfig;
import ru.sberbank.jms.util.messaging.ReceiveMessageService;
import ru.sberbank.jms.util.messaging.ReceiveMessagesServiceWebsphereMQImpl;
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

    @RequestMapping(value = "/start",method = RequestMethod.POST)
    public @ResponseBody
    JmsMessage getJmsConfigurationList(@RequestParam(required = true) String factoryName,
                                             @RequestParam(required = true) String correlationId,
                                             @RequestParam(required = true) long timeout,
                                             @RequestParam(required = true) String queueName) {

        MqConfig mqConfig = new MqConfig();
        mqConfig.setConnectionFactoryName(factoryName);
        mqConfig.setTimeout(timeout);
        mqConfig.setQueueName(queueName);
        mqConfig.setCorrelationId(correlationId);

        ReceiveMessagesServiceWebsphereMQImpl.DEFAULT_MQ_CONFIG = mqConfig;

        return receiveMessageService.getMessages(mqConfig);
    }

}
