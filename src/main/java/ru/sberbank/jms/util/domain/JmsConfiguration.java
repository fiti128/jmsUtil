package ru.sberbank.jms.util.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.plural.RooPlural;
import org.springframework.roo.addon.tostring.RooToString;

import ru.sberbank.jms.util.messaging.JmsQueue;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooPlural("Options")
public class JmsConfiguration {

    /**
     */

    private String url;

    /**
     */

    private String configurationName;

    /**
     */

    private String queueName;

    /**
     */

    private int delay;

    /**
     */
   private String queueNameReceive;

    /**
     */

    private JmsQueue queueType;
}
