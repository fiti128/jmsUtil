package ru.sberbank.jms.util.messaging;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class JmsUtilTopicListener implements MessageListener {

    public void onMessage(Message message) {
        try {
            System.out.println("JMS message received: " + ((TextMessage)message).getText());
        } catch (JMSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
