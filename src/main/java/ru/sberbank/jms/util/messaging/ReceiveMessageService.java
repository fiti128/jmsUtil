package ru.sberbank.jms.util.messaging;

import ru.sberbank.jms.util.domain.JmsConfiguration;

/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevskiy-SA
 * Date: 24.11.13
 * Time: 17:48
 * To change this template use File | Settings | File Templates.
 */
public interface ReceiveMessageService {
    void updateJmsMessages(JmsConfiguration jmsConfiguration);
}