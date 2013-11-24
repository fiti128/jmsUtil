package ru.sberbank.jms.util.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.sberbank.jms.util.domain.JmsConfiguration;

import javax.jms.*;


/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevsky-SA
 * Date: 22.11.13
 * Time: 9:34
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ManagingSendMessagesService {
    public static  JmsConfiguration defaultConfig;

   static {
        defaultConfig = new JmsConfiguration();
        defaultConfig.setUrl("vm://localhost:61616");
        defaultConfig.setQueueName("jms.topic.JmsUtil");
    }

    public boolean sendMessage(String xmlString, JmsConfiguration jmsConfiguration) {
        boolean result = true;
        jmsConfiguration = jmsConfiguration == null ? defaultConfig : jmsConfiguration;
        ConnectionFactory cf = new ActiveMQConnectionFactory(jmsConfiguration.getUrl());
        Connection conn = null;
        Session session = null;
        try {
            conn = cf.createConnection();
            session = conn.createSession(false,Session.AUTO_ACKNOWLEDGE);
            JmsQueue jmsQueue = JmsQueue.QUEUE;
            Destination destination = DestinationFactory.getDestination(jmsQueue, jmsConfiguration.getQueueName());
            MessageProducer producer = session.createProducer(destination);
            TextMessage message = session.createTextMessage();
            message.setText(xmlString);
            producer.send(message);
        }  catch (JMSException e) {
            Logger.getLogger(this.getClass()).error("Error on sending message",e);
            e.printStackTrace();
            result = false;
        }   finally {
            try {
                if (session!= null) {
                    session.close();
                }

            }  catch (JMSException ex) {

            }
            try {
                if (conn!=null) {
                    conn.close();
                }
            }  catch (JMSException ex) {

            }
        }

        return result;
    }
}
