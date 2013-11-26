package ru.sberbank.jms.util.web;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created with IntelliJ IDEA.
 * User: SBT-Yanushevskiy-SA
 * Date: 26.11.13
 * Time: 16:18
 * To change this template use File | Settings | File Templates.
 */
public class FileUpload {
    MultipartFile mf;

    public MultipartFile getMf() {
        return mf;
    }

    public void setMf(MultipartFile mf) {
        this.mf = mf;
    }
}
