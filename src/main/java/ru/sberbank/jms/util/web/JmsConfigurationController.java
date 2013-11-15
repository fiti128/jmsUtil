package ru.sberbank.jms.util.web;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.sberbank.jms.util.domain.JmsConfiguration;

@RequestMapping("/jmsconfigurations")
@Controller
@RooWebScaffold(path = "jmsconfigurations", formBackingObject = JmsConfiguration.class)
public class JmsConfigurationController {
}
