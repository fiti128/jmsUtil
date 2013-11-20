package ru.sberbank.jms.util.jmx;

import ru.sberbank.jms.util.domain.JmsConfiguration;

import javax.management.*;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevsky-SA
 * Date: 20.11.13
 * Time: 16:01
 * To change this template use File | Settings | File Templates.
 */
public interface UpdateConfigurationService {
    void updateConfiguration(JmsConfiguration jmsConfiguration);
}
