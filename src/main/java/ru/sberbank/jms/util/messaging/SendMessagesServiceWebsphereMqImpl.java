package ru.sberbank.jms.util.messaging;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.sberbank.jms.util.domain.JmsConfiguration;
import ru.sberbank.jms.util.domain.MqConfig;

import javax.jms.*;


/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevsky-SA
 * Date: 22.11.13
 * Time: 9:34
 * To change this template use File | Settings | File Templates.
 */
@Service("wmqSender")
public class SendMessagesServiceWebsphereMqImpl implements SendMessagesService {
    public static  JmsConfiguration defaultConfig;
    public static String HOST = "10.71.0.34";
    public static int PORT = 1415;
    public static String CHANNEL = "SYSTEM.DEF.SVRCONN";
    public static String QUEUE_MANAGER_NAME = "JMS01.DEMO";
    public static String DESTINATION_NAME = "JMS.LQ";
    public static boolean IS_TOPIC = false;
    public static MqConfig DEFAULT_MQ_CONFIG;

    static {
        defaultConfig = new JmsConfiguration();
        defaultConfig.setUrl("vm://localhost:61616");
        defaultConfig.setQueueNameReceive("jms.topic.JmsUtil");

        DEFAULT_MQ_CONFIG = new MqConfig();
        DEFAULT_MQ_CONFIG.setHost(HOST);
        DEFAULT_MQ_CONFIG.setPort(PORT);
        DEFAULT_MQ_CONFIG.setChannel(CHANNEL);
        DEFAULT_MQ_CONFIG.setQueueManagerName(QUEUE_MANAGER_NAME);
        DEFAULT_MQ_CONFIG.setDestinationName(DESTINATION_NAME);
        DEFAULT_MQ_CONFIG.setIS_TOPIC(IS_TOPIC);
    }
    public boolean sendMessage(String xmlString, MqConfig mqConfig) {
        if (mqConfig==null) {
            mqConfig = DEFAULT_MQ_CONFIG;
        }

        String host = mqConfig.getHost();
        int port = mqConfig.getPort();
        String channel = CHANNEL;
        String queueManagerName = mqConfig.getQueueManagerName();
        String destinationName = mqConfig.getDestinationName();
        boolean isTopic = mqConfig.isIS_TOPIC();

        boolean result = true;
        // Variables
        Connection connection = null;
        Session session = null;
        Destination destination = null;
        MessageProducer producer = null;

        try {
            // Create a connection factory
            JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
            JmsConnectionFactory cf = ff.createConnectionFactory();

            // Set the properties
            cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, host);
            cf.setIntProperty(WMQConstants.WMQ_PORT, port);
            cf.setStringProperty(WMQConstants.WMQ_CHANNEL, channel);
            cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
            cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, queueManagerName);

            // Create JMS objects
            connection = cf.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            if (isTopic) {
                destination = session.createTopic(destinationName);
            }
            else {
                destination = session.createQueue(destinationName);
            }
            producer = session.createProducer(destination);

            TextMessage message = session.createTextMessage(xmlString);

            // Start the connection
            connection.start();

            // And, send the message
            producer.send(message);
            System.out.println("Sent message:\n" + message);

        }
        catch (JMSException jmsex) {
            result = false;
        }
        finally {
            if (producer != null) {
                try {
                    producer.close();
                }
                catch (JMSException jmsex) {
                    System.out.println("Producer could not be closed.");
                }
            }

            if (session != null) {
                try {
                    session.close();
                }
                catch (JMSException jmsex) {
                    System.out.println("Session could not be closed.");
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                }
                catch (JMSException jmsex) {
                    System.out.println("Connection could not be closed.");
                }
            }
        }


        return result;
    }
}
