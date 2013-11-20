package ru.sberbank.jms.util.jmx;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.sberbank.jms.util.domain.JmsConfiguration;

import javax.annotation.Resource;
import javax.management.*;
import java.io.IOException;


/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevsky-SA
 * Date: 20.11.13
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */
//@Service("updateConfigurationService")
public class UpdateConfigurationServiceImpl implements UpdateConfigurationService {
    Logger logger = Logger.getLogger(this.getClass());

//    @Resource(name = "mBeanServerClient")
    MBeanServerConnection mBeanServerConnection;

    public void updateConfiguration(JmsConfiguration jmsConfiguration) {
        try{
      mBeanServerConnection.setAttribute(
              new ObjectName("legres:name=Factory"),
              new Attribute("BrokerURL",jmsConfiguration.getUrl())
      );
      mBeanServerConnection.setAttribute(
              new ObjectName("legres:name=receiveTopic"),
              new Attribute("PhysicalName",jmsConfiguration.getQueueNameReceive())
      );
       mBeanServerConnection.setAttribute(
              new ObjectName("legres:name=sendTopic"),
              new Attribute("PhysicalName",jmsConfiguration.getQueueName())
      );
        } catch (Exception e) {
          logger.error("Got error when changing jmb with jmx",e);
            throw new RuntimeException(e);
        }

    }

    public MBeanServerConnection getmBeanServerConnection() {
        return mBeanServerConnection;
    }

    public void setmBeanServerConnection(MBeanServerConnection mBeanServerConnection) {
        this.mBeanServerConnection = mBeanServerConnection;
    }
}
