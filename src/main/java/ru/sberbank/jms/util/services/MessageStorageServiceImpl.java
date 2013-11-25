package ru.sberbank.jms.util.services;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevskiy-SA
 * Date: 24.11.13
 * Time: 16:06
 * To change this template use File | Settings | File Templates.
 */
@Service(value = "messageStorageService")
public class MessageStorageServiceImpl implements MessageStorageService {
    private static String STORAGE_FILE = "storage.txt";
    private static String ENCODING= "Windows-1251";

    public void addMessageToStorage(String textMessage) {
         File file = new File(STORAGE_FILE);
        BufferedWriter bw = null;
        try {
           bw  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true),ENCODING));
            bw.newLine();
            bw.write("-----MESSAGE-----\n");
            bw.write(textMessage);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean clearStorage() {
       File file = new File(STORAGE_FILE);
        boolean result = true;
        if (file.exists()) {
        result = file.delete();

        }
        return result;
    }

    public String getMessagesFromStorage() {
        File file = new File(STORAGE_FILE);
        StringBuilder sb = new StringBuilder();
        if (file.exists())  {
            getMessages(file, sb);
        }
        return sb.toString();  //To change body of implemented methods use File | Settings | File Templates.
    }

    void getMessages(File file, StringBuilder sb) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),ENCODING));

            String str;
            while ((str=reader.readLine()) != null) {
            sb.append(str).append("\n");
            }


        } catch (Exception e) {
            Logger.getLogger(this.getClass()).error("Storage error",e);
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}
