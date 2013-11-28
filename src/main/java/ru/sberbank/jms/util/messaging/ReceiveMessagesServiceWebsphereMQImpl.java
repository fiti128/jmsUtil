package ru.sberbank.jms.util.messaging;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sberbank.jms.util.domain.JmsConfiguration;
import ru.sberbank.jms.util.domain.JmsMessage;
import ru.sberbank.jms.util.domain.MqConfig;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

/**
* Created with IntelliJ IDEA.
* User: SBT-Yanushevsky-SA
* Date: 21.11.13
* Time: 9:21
* To change this template use File | Settings | File Templates.
*/
@Service ("websphereMq")
public class ReceiveMessagesServiceWebsphereMQImpl implements ReceiveMessageService {
    private transient Logger logger = Logger.getLogger(this.getClass());
    private static String FORMAT_FILTER = "JMSCorrelationID='%s'";
    public static  JmsConfiguration defaultConfig;
    public static MqConfig DEFAULT_MQ_CONFIG;
    private volatile boolean connected = false;
    private MessageConsumer consumer = null;
    private Connection connection = null;
    private Session session = null;

    static {
        DEFAULT_MQ_CONFIG = new MqConfig();
        DEFAULT_MQ_CONFIG.setQueueName(QUEUE_NAME);
        DEFAULT_MQ_CONFIG.setConnectionFactoryName(CONNECTION_FACTORY_NAME);
        DEFAULT_MQ_CONFIG.setCorrelationId(CORRELATION_ID);
        DEFAULT_MQ_CONFIG.setTimeout(5000);
    }


    @Autowired
    MessageListener messageListener;


    public JmsMessage getMessages(MqConfig mqConfig) {
        if (mqConfig == null) {
        mqConfig = DEFAULT_MQ_CONFIG;

        }
        String correlationId = mqConfig.getCorrelationId();
        JmsMessage jmsMessage = new JmsMessage();
        String filter = String.format(FORMAT_FILTER,correlationId);
        long timeout = mqConfig.getTimeout();
        String connectionFactoryName = mqConfig.getConnectionFactoryName();
        String queueName = mqConfig.getQueueName();

        try {
            Context ctx = new InitialContext(new Hashtable());
            System.out.println("creating factory " + connectionFactoryName);
            ConnectionFactory qcf = (ConnectionFactory) ctx
                    .lookup(connectionFactoryName);
            System.out.println("creating queue " +queueName);
            Queue q = (Queue) ctx.lookup(queueName);

            connection = qcf.createConnection();
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            System.out.println("creating consumer. Correlation ID is " + correlationId);
            byte[] correlationIdasBytes = new byte[24];
            correlationIdasBytes = correlationId.getBytes("UTF-8");
            String selector = "JMSCorrelationID='ID:" + getHexString(correlationIdasBytes) + "'";
            consumer = correlationId.length() > 0 ? session.createConsumer(q, selector):session.createConsumer(q);
            System.out.println("Trying selector " + selector);
            Message message = consumer.receive(timeout);
            if (message != null) {
                logger.debug("message received");
                logger.debug("id=" + message.getJMSMessageID());
                jmsMessage.setMessageBody(((TextMessage)message).getText());
            } else {
                logger.error("Message was not fount or received not a textMessage");
            }



        }
        catch (JMSException jmsex) {
            System.out.println("catch JMSException");
            connected = false;
            jmsex.printStackTrace();
            logger.error("Error while creating jms connection", jmsex);
            throw new RuntimeException(jmsex);
        } catch (NamingException e1) {
            System.out.println("catch Naming Exception");
            connected = false;
            e1.printStackTrace();
            logger.error("Error while creating jms connection", e1);
            throw new RuntimeException(e1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return jmsMessage;
    }

    public static String getHexString(byte[] b){
        StringBuilder sb = new StringBuilder();

        for (int i=0; i < b.length; i++) {
            sb.append(Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 ));
        }
        int length = 48 - sb.length();
         for (int i = 0; i < length; i++) {
            sb.append("0");
        }
        String result = sb.toString();
        System.out.println("hexString " +result);
        return result;
    }
    public synchronized boolean startConnection(MqConfig mqConfig) {
       if (connected) return true;
        mqConfig = DEFAULT_MQ_CONFIG;
        String correlationId = mqConfig.getCorrelationId();
        String filter = String.format(FORMAT_FILTER,correlationId);
        long timeout = mqConfig.getTimeout();
        String connectionFactoryName = mqConfig.getConnectionFactoryName();
        String queueName = mqConfig.getQueueName();

        try {
            Context ctx = new InitialContext(new Hashtable());
            ConnectionFactory qcf = (ConnectionFactory) ctx
                    .lookup(connectionFactoryName);
            System.out.println("getting queue " +queueName);
            Queue q = (Queue) ctx.lookup(queueName);

            connection = qcf.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            consumer = correlationId.length() > 0 ? session.createConsumer(q,filter):session.createConsumer(q);
            consumer.receive(timeout);

        }
       catch (JMSException jmsex) {
           System.out.println("catch JMSException");
           connected = false;
           jmsex.printStackTrace();
           Logger.getLogger(this.getClass()).error("Error while creating jms connection", jmsex);
           throw new RuntimeException(jmsex);
       } catch (NamingException e1) {
            connected = false;
            e1.printStackTrace();
            Logger.getLogger(this.getClass()).error("Error while creating jms connection", e1);
            throw new RuntimeException(e1);
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
