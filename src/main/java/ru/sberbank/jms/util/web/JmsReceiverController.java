package ru.sberbank.jms.util.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sberbank.jms.util.domain.JmsConfiguration;
import ru.sberbank.jms.util.domain.JmsMessage;
import ru.sberbank.jms.util.messaging.ManagingReceiveMessagesService;
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
    private transient ManagingReceiveMessagesService managingReceiveMessagesService;

    @Autowired
    private transient MessageStorageService messageStorageService;

    @RequestMapping( method = RequestMethod.POST)
    public @ResponseBody
    List<JmsMessage> getJmsConfigurationList(@RequestParam(required = false) String receiveConfigurationId) {

        JmsConfiguration jmsConfiguration =null;

        managingReceiveMessagesService.updateJmsMessages(jmsConfiguration);
        List<JmsMessage> messageList = new ArrayList<JmsMessage>();
        JmsMessage jmsMessage = new JmsMessage();
        jmsMessage.setMessageBody(messageStorageService.getMessagesFromStorage());
        messageStorageService.clearStorage();
        messageList.add(jmsMessage);

        return messageList;
    }
}
