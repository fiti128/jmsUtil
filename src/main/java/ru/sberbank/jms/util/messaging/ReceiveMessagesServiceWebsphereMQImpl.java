package ru.sberbank.jms.util.messaging;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.log4j.Logger;
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
    private static String FORMAT_FILTER = "JMSCorrelationID = '%s'";
    public static  JmsConfiguration defaultConfig;
    public static String HOST = "10.71.0.34";
    public static int PORT = 1415;
    public static String CHANNEL = "SYSTEM.DEF.SVRCONN";
    public static String QUEUE_MANAGER_NAME = "JMS01.DEMO";
    public static String DESTINATION_NAME = "JMS.LQ";
    public static String CORRELATION_ID= "SBERBANK.MINSK";
    public static boolean IS_TOPIC = false;
    public static MqConfig DEFAULT_MQ_CONFIG;
    private volatile boolean connected = false;
    private Connection connection = null;
    private Session session = null;
    private MessageConsumer consumer = null;

    static {


        DEFAULT_MQ_CONFIG = new MqConfig();
        DEFAULT_MQ_CONFIG.setHost(HOST);
        DEFAULT_MQ_CONFIG.setPort(PORT);
        DEFAULT_MQ_CONFIG.setChannel(CHANNEL);
        DEFAULT_MQ_CONFIG.setQueueManagerName(QUEUE_MANAGER_NAME);
        DEFAULT_MQ_CONFIG.setDestinationName(DESTINATION_NAME);
        DEFAULT_MQ_CONFIG.setIS_TOPIC(IS_TOPIC);
        DEFAULT_MQ_CONFIG.setCorrelationId(CORRELATION_ID);


    }


    @Autowired
    MessageListener messageListener;

    public void updateJmsMessages(JmsConfiguration jmsConfiguration) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public synchronized boolean startConnection(MqConfig mqConfig) {
       if (connected) return true;
       mqConfig = DEFAULT_MQ_CONFIG;
       String host = mqConfig.getHost();
       int port = mqConfig.getPort();
       String channel = CHANNEL;
       String queueManagerName = mqConfig.getQueueManagerName();
       String destinationName = mqConfig.getDestinationName();
       boolean isTopic = mqConfig.isIS_TOPIC();
       String filter = String.format(FORMAT_FILTER,mqConfig.getCorrelationId());
       Destination destination = null;

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
           connected = true;
           session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
           if (isTopic) {
               destination = session.createTopic(destinationName);
           }
           else {
               destination = session.createQueue(destinationName);
           }
           consumer = session.createConsumer(destination,filter);

           // Start the connection
           connection.start();
           // And, receive the message
           consumer.setMessageListener(messageListener);


       }
       catch (JMSException jmsex) {
           connected = false;
           Logger.getLogger(this.getClass()).error("Error while creating jms connection",jmsex);
       }
       return connected;
   }

    public synchronized void stopConnection() {

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
        connected = false;
    }



}
