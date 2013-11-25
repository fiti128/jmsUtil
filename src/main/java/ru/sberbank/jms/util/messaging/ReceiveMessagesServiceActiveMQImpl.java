package ru.sberbank.jms.util.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sberbank.jms.util.domain.JmsConfiguration;
import ru.sberbank.jms.util.domain.JmsMessage;
import ru.sberbank.jms.util.domain.MqConfig;

import javax.jms.*;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevsky-SA
 * Date: 21.11.13
 * Time: 9:21
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ReceiveMessagesServiceActiveMQImpl implements ReceiveMessageService {
    public static  JmsConfiguration defaultConfig;

    static {
        defaultConfig = new JmsConfiguration();
        defaultConfig.setUrl("vm://localhost:61616");
        defaultConfig.setQueueNameReceive("jms.topic.JmsUtil");
    }

    @Autowired
    MessageListener messageListener;

    public void updateJmsMessages(JmsConfiguration jmsConfiguration) {
        jmsConfiguration = jmsConfiguration == null ? defaultConfig : jmsConfiguration;
        ConnectionFactory factory = new ActiveMQConnectionFactory(jmsConfiguration.getUrl());
        Connection conn = null;
        Session session = null;
        try{
            conn = factory.createConnection();
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = new ActiveMQQueue(jmsConfiguration.getQueueNameReceive());
            MessageConsumer consumer = session.createConsumer(destination);
//            TextMessage message = (TextMessage)consumer.receive(1000);
            consumer.setMessageListener(messageListener);

            conn.start();
            TimeUnit.SECONDS.sleep(2);
            conn.stop();
        }   catch (JMSException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
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
    }

    public boolean startConnection(MqConfig mqconfig) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void stopConnection() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}
