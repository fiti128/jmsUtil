package ru.sberbank.jms.util.messaging;

import ru.sberbank.jms.util.domain.JmsMessage;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class JmsUtilTopicListener implements MessageListener {

    public void onMessage(Message message) {
        try {
            String text = ((TextMessage)message).getText();
            System.out.println("JMS message received: " + text);
            JmsMessage jmsMessage = new JmsMessage();
            jmsMessage.setMessageBody(text);

            jmsMessage.persist();
        } catch (JMSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
