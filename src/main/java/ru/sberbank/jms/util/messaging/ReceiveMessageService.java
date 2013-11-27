package ru.sberbank.jms.util.messaging;

import ru.sberbank.jms.util.domain.JmsMessage;
import ru.sberbank.jms.util.domain.MqConfig;

/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevskiy-SA
 * Date: 24.11.13
 * Time: 17:48
 * To change this template use File | Settings | File Templates.
 */
public interface ReceiveMessageService {

    String CORRELATION_ID= "SBERBANK.MINSK";
    String CONNECTION_FACTORY_NAME="jms/erib/way4u/InputQCF";
    String QUEUE_NAME="jms/erib/way4u/OutputQueue";

    JmsMessage getMessages(MqConfig mqConfig);
    boolean startConnection(MqConfig mqconfig);
    void stopConnection();
}
