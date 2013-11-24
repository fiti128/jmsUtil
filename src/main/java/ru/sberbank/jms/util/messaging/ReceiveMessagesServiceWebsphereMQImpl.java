package ru.sberbank.jms.util.messaging;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sberbank.jms.util.domain.JmsConfiguration;
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
@Service ("websphereMq")
public class ReceiveMessagesServiceWebsphereMQImpl implements ReceiveMessageService {
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


    @Autowired
    MessageListener messageListener;




    public void updateJmsMessages(JmsConfiguration jmsConfiguration) {
        jmsConfiguration = jmsConfiguration == null ? defaultConfig : jmsConfiguration;
        MqConfig mqConfig = DEFAULT_MQ_CONFIG;
        String host = mqConfig.getHost();
        int port = mqConfig.getPort();
        String channel = CHANNEL;
        String queueManagerName = mqConfig.getQueueManagerName();
        String destinationName = mqConfig.getDestinationName();
        boolean isTopic = mqConfig.isIS_TOPIC();




        Connection connection = null;
        Session session = null;
        Destination destination = null;
        MessageConsumer consumer = null;

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
            consumer = session.createConsumer(destination);

            // Start the connection
            connection.start();
            // And, receive the message
            consumer.setMessageListener(messageListener);
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }


        }
        catch (JMSException jmsex) {
        }
        finally {
            if (consumer != null) {
                try {
                    consumer.close();
                }
                catch (JMSException jmsex) {
                    System.out.println("Consumer could not be closed.");
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
    }


}
