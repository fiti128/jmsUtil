package ru.sberbank.jms.util.messaging;

/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevsky-SA
 * Date: 20.11.13
 * Time: 10:21
 * To change this template use File | Settings | File Templates.
 */
public interface MessageReceiveService {
    void processMessage(Object message);
}
