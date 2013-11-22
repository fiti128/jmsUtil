package ru.sberbank.jms.util.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sberbank.jms.util.domain.JmsConfiguration;
import ru.sberbank.jms.util.domain.JmsMessage;
import ru.sberbank.jms.util.jmx.ManagingReceiveMessagesService;

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

    @RequestMapping( method = RequestMethod.POST)
    public @ResponseBody
    List<JmsMessage> getJmsConfigurationList(@RequestParam(required = false) String receiveConfigurationId) {

        JmsConfiguration jmsConfiguration = JmsConfiguration.findJmsConfiguration(receiveConfigurationId);
        List<JmsMessage> list = JmsMessage.findAllJmsMessages();
        managingReceiveMessagesService.updateJmsMessages(jmsConfiguration);
        List<JmsMessage> newList = JmsMessage.findAllJmsMessages();
        if (newList.size()!=list.size()){
            for (JmsMessage jmsMessage : list) {
                jmsMessage.remove();
            }
        }
        list = JmsMessage.findAllJmsMessages();
//
//        JmsMessage jmsMessage = managingReceiveMessagesService.getAvailableMessages();
//        jmsMessage.setMessageBody("Received message");
//        JmsMessage newJmsMessage = new JmsMessage();
//        newJmsMessage.setMessageBody("Additional message");
//        List<JmsMessage> list = new ArrayList<JmsMessage>();
//        list.add(jmsMessage);
//        list.add(newJmsMessage);


        return list;
    }
}
