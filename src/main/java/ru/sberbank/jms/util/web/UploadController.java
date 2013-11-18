package ru.sberbank.jms.util.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import ru.sberbank.jms.util.domain.JmsConfiguration;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevsky-SA
 * Date: 18.11.13
 * Time: 12:16
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping(value = "/upload")
@Controller
public class UploadController {

    @RequestMapping( method = RequestMethod.POST)
    public @ResponseBody
    JmsConfiguration upload(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException {

        JmsConfiguration jmsConfiguration = new JmsConfiguration();
        jmsConfiguration.setUrl("Для загрузки требуется xml файл!");
        //0. notice, we have used MultipartHttpServletRequest

        //1. get the files from the request object
        Iterator<String> itr =  request.getFileNames();

        MultipartFile mpf = request.getFile(itr.next());
        String fileName = mpf.getOriginalFilename();


        if (fileName.matches(".*xml")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mpf.getInputStream(),"UTF-8"));
            StringBuilder sb = new StringBuilder();
            String str =null;
            while((str =reader.readLine()) != null) {
               sb.append(str).append("\n\r");
            }
            str = sb.toString();
            System.out.println(str);
            jmsConfiguration.setUrl(str);
        }
        return jmsConfiguration;

    }
}
