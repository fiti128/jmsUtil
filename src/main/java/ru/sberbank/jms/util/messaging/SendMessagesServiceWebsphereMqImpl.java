package ru.sberbank.jms.util.messaging;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.sberbank.jms.util.domain.JmsConfiguration;
import ru.sberbank.jms.util.domain.MqConfig;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;


/**
* Created with IntelliJ IDEA.
* User: SBT-Yanushevsky-SA
* Date: 22.11.13
* Time: 9:34
* To change this template use File | Settings | File Templates.
*/
@Service("wmqSender")
public class SendMessagesServiceWebsphereMqImpl implements SendMessagesService {
    public static MqConfig DEFAULT_MQ_CONFIG;
    static {

        DEFAULT_MQ_CONFIG = new MqConfig();
        DEFAULT_MQ_CONFIG.setQueueName(QUEUE_NAME);
        DEFAULT_MQ_CONFIG.setConnectionFactoryName(CONNECTION_FACTORY_NAME);
        DEFAULT_MQ_CONFIG.setCorrelationId(CORRELATION_ID);
    }
    public boolean sendMessage(String xmlString, MqConfig mqConfig) {
        if (mqConfig==null) {
            mqConfig = DEFAULT_MQ_CONFIG;
        }
        String connectionFactoryName = mqConfig.getConnectionFactoryName();
        String queueName = mqConfig.getQueueName();
        String correlationId = mqConfig.getCorrelationId();

        boolean result = true;

        MessageProducer producer = null;
        Connection connection = null;
        Session session = null;
        try {
            System.out.println("getting context");
            Context ctx = new InitialContext(new Hashtable());
            System.out.println("getting factory" + connectionFactoryName);
            ConnectionFactory qcf = (ConnectionFactory) ctx
                    .lookup(connectionFactoryName);
            System.out.println("getting queue from jndi " + queueName);
            Queue q = (Queue) ctx.lookup(queueName);
            System.out.println("getting connection");
            connection = qcf.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer(q);
            TextMessage msg = session.createTextMessage();
            msg.setText(xmlString);
               if(correlationId.length() > 0 ) {
                   msg.setJMSCorrelationID(correlationId);
                   System.out.println("correlation Id set to " +correlationId);
               }
            producer.send(msg);
            System.out.println("Message sent:\nid=" + msg.getJMSMessageID());

        }
        catch (JMSException jmsex) {
            System.out.println("SEND: JMSException" + jmsex.getMessage());
            result = false;
            jmsex.printStackTrace();
            Logger.getLogger(this.getClass()).error("Error while creating jms connection",jmsex);
            throw new RuntimeException(jmsex);
        } catch (NamingException e) {
            System.out.println("SEND: NamingException" + e.getMessage());
            result = false;
            e.printStackTrace();
            Logger.getLogger(this.getClass()).error("Error while retrieving jndi ",e);
            throw new RuntimeException(e);
        } finally {
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
