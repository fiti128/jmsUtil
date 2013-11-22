package ru.sberbank.jms.util.jmx;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.AbstractMessageListenerContainer;
import org.springframework.stereotype.Service;
import ru.sberbank.jms.util.domain.JmsConfiguration;
import ru.sberbank.jms.util.domain.JmsMessage;

import javax.annotation.Resource;
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
public class ManagingReceiveMessagesService {
    @Resource(name = "jmsContainer")
    private AbstractMessageListenerContainer container;

    @Autowired
    MessageListener messageListener;
    public void receiveAttempt(){
                  nonSpringJMS();
//        springJms();
    }

    private void springJms() {
        System.out.println("Looking queeName: " + container.getDestinationName());

        container.start();


        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        container.stop();
    }

    private void nonSpringJMS() {
        ConnectionFactory factory = new ActiveMQConnectionFactory("vm://localhost:61616");
        Connection conn = null;
        Session session = null;
        try{
            conn = factory.createConnection();
            conn.start();
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = new ActiveMQQueue("jms.topic.JmsUtil");
            MessageConsumer consumer = session.createConsumer(destination);
            TextMessage message = (TextMessage)consumer.receive(1000);

            System.out.println("Non spring message: " +message.getText());
            conn.stop();
        }   catch (JMSException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }    finally {
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

    public JmsMessage getAvailableMessages() {
        receiveAttempt();
        return new JmsMessage();
    }

    public void updateJmsMessages(JmsConfiguration jmsConfiguration) {
        checkJmsConfiguration(jmsConfiguration);
        ConnectionFactory factory = new ActiveMQConnectionFactory("vm://localhost:61616");
        Connection conn = null;
        Session session = null;
        try{
            conn = factory.createConnection();
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = new ActiveMQQueue("jms.topic.JmsUtil");
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

    private void checkJmsConfiguration(JmsConfiguration jmsConfiguration) {

    }
}
