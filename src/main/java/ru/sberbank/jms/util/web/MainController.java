package ru.sberbank.jms.util.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.sberbank.jms.util.domain.JmsConfiguration;
import ru.sberbank.jms.util.domain.MqConfig;

/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevsky-SA
 * Date: 16.11.13
 * Time: 18:01
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class MainController {
//	@Value("#{button_home}")
    String home="home";

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
    public String showMainPage(Model uiModel) {
        List<JmsConfiguration> list = JmsConfiguration.findJmsConfigurationEntries(0,10);
        System.out.println(list.size());
        List<String> configurationList = new ArrayList<String>();
        
        for (JmsConfiguration config : list) {
            configurationList.add(config.getUrl());
        }
        for (String s : configurationList) {
            System.out.println(s);
        }
        System.out.println(home);
        JmsConfiguration jmsConfiguration = new JmsConfiguration();
        uiModel.addAttribute("jmsConfig",jmsConfiguration);
        uiModel.addAttribute("configurationList",configurationList);
        return "index";
    }

    @RequestMapping(value = "/getConfigurationList", method = RequestMethod.POST)
    public @ResponseBody
    JmsConfiguration getJmsConfigurationList(@RequestParam(required = false) String name) {
        String result = "good Url";
        if(name!=null) {
            result = name;
        }
        JmsConfiguration jmsConfiguration = new JmsConfiguration();
        jmsConfiguration.setUrl(result);
         return jmsConfiguration;
    }
}
