package ru.sberbank.jms.util.messaging;

import ru.sberbank.jms.util.domain.MqConfig;

/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevskiy-SA
 * Date: 24.11.13
 * Time: 18:38
 * To change this template use File | Settings | File Templates.
 */
public interface SendMessagesService {

    String CORRELATION_ID= "SBERBANK.MINSK";
    String CONNECTION_FACTORY_NAME="jms/erib/way4u/InputQCF";
    String QUEUE_NAME="jms/erib/way4u/OutputQueue";


    boolean sendMessage(String xmlString, MqConfig config);
}
