package ru.sberbank.jms.util.messaging;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sberbank.jms.util.domain.JmsMessage;
import ru.sberbank.jms.util.services.MessageStorageService;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class JmsUtilTopicListener implements MessageListener {
    @Autowired
    private transient MessageStorageService messageStorageService;

    public void onMessage(Message message) {
        try {
            String text = ((TextMessage)message).getText();
//            text = new String(text.getBytes("windows-1251"),"UTF-8");
            System.out.println("JMS message received: " + text);
            messageStorageService.addMessageToStorage(text);

        } catch (Exception e) {
            Logger.getLogger(this.getClass()).error("Error on saving message",e);
        }
    }
}
