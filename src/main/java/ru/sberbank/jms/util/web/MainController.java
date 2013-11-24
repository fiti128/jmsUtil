package ru.sberbank.jms.util.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.sberbank.jms.util.domain.JmsConfiguration;


/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevsky-SA
 * Date: 16.11.13
 * Time: 18:01
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class MainController {





    @RequestMapping(value="/" ,method = RequestMethod.GET)
    public String showMainPage(Model uiModel)  {
        List<JmsConfiguration> list = new ArrayList<JmsConfiguration>();
        List<String> configurationList = new ArrayList<String>();
        for (JmsConfiguration config : list) {
            configurationList.add(config.getConfigurationName());
        }

        JmsConfiguration jmsConfiguration = new JmsConfiguration();
        uiModel.addAttribute("jmsConfig",jmsConfiguration);
        uiModel.addAttribute("configurationList",configurationList);
        return "index";
    }

    @RequestMapping(value = "/getConfigurationList", method = RequestMethod.POST)
    public @ResponseBody
    JmsConfiguration getJmsConfigurationList(@RequestParam(required = false) String name) {

        JmsConfiguration jmsConfiguration = new JmsConfiguration();
//        List<JmsConfiguration> jmsConfigurationList = JmsConfiguration.findAllOptions();
//        for (JmsConfiguration config : jmsConfigurationList) {
//              if (config.getConfigurationName().equals(name)) {
//                  jmsConfiguration = config;
//              }
//        }


        return jmsConfiguration;
    }



}
