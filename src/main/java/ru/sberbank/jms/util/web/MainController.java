package ru.sberbank.jms.util.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jmx.support.MBeanServerConnectionFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.sberbank.jms.util.domain.JmsConfiguration;
import ru.sberbank.jms.util.domain.MqConfig;
import ru.sberbank.jms.util.domain.XmlMessage;

import javax.annotation.Resource;
import javax.management.*;

/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevsky-SA
 * Date: 16.11.13
 * Time: 18:01
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class MainController {
    @Autowired
    private transient JmsTemplate jmsTopicTemplate;

//    @Autowired
    @Resource(name = "mBeanServerClient")
    MBeanServerConnection mBeanServerConnection;

    @RequestMapping(value="/test", method = RequestMethod.GET, produces = "text/html")
    public String showPage(Model uiModel) {
        List<JmsConfiguration> list = JmsConfiguration.findJmsConfigurationEntries(0,10);
        System.out.println(list.size());
        List<String> stringList = new ArrayList<String>();
        for (JmsConfiguration config : list) {
        	stringList.add(config.getUrl());
        }
        for (String s : stringList) {
            System.out.println(s);
        }
        return "redirect:/";
    }
    @RequestMapping(value="/" ,method = RequestMethod.GET)
    public String showMainPage(Model uiModel)  {
        List<JmsConfiguration> list = JmsConfiguration.findJmsConfigurationEntries(0,10);
        List<String> configurationList = new ArrayList<String>();
        for (JmsConfiguration config : list) {
            configurationList.add(config.getConfigurationName());
        }

        try {
            System.out.println(mBeanServerConnection.getAttribute(
                    new ObjectName("legres:name=DataSource"),"Url"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        jmsTopicTemplate.convertAndSend("Hi jms");
        JmsConfiguration jmsConfiguration = new JmsConfiguration();
        uiModel.addAttribute("jmsConfig",jmsConfiguration);
        uiModel.addAttribute("configurationList",configurationList);
        return "index";
    }

    @RequestMapping(value = "/getConfigurationList", method = RequestMethod.POST)
    public @ResponseBody
    JmsConfiguration getJmsConfigurationList(@RequestParam(required = false) String name) {

        JmsConfiguration jmsConfiguration = JmsConfiguration.findJmsConfiguration(name);
        System.out.println(jmsConfiguration.getConfigurationName());
//        List<JmsConfiguration> jmsConfigurationList = JmsConfiguration.findAllOptions();
//        for (JmsConfiguration config : jmsConfigurationList) {
//              if (config.getConfigurationName().equals(name)) {
//                  jmsConfiguration = config;
//              }
//        }


        return jmsConfiguration;
    }



}
