package ru.sberbank.jms.util.messaging;

import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevsky-SA
 * Date: 20.11.13
 * Time: 10:22
 * To change this template use File | Settings | File Templates.
 */
@Component("messageReceiveService")
public class MesssageReceiveServiceImpl implements MessageReceiveService {
    public void processMessage(Object message) {
        System.out.println("Processed message: " + message);
    }
}
