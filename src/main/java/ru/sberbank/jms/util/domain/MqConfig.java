package ru.sberbank.jms.util.domain;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevsky-SA
 * Date: 17.11.13
 * Time: 17:33
 * To change this template use File | Settings | File Templates.
 */
public class MqConfig implements Serializable {
    private String url;
    private String delay;

    public MqConfig() {
    }

    public String getUrl() {
        return url;
    }

    public String getDelay() {
        return delay;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }
}
