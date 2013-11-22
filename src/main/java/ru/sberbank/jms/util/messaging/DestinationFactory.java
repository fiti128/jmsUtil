package ru.sberbank.jms.util.messaging;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.Destination;

/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevsky-SA
 * Date: 22.11.13
 * Time: 12:31
 * To change this template use File | Settings | File Templates.
 */
public class DestinationFactory {

    public static Destination getDestination(JmsQueue jmsQueue, String queueName) {
        return jmsQueue == JmsQueue.QUEUE ? new ActiveMQQueue(queueName) : new ActiveMQTopic(queueName);
    }
}
