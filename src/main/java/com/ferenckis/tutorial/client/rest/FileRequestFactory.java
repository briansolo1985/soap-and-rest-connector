package com.ferenckis.tutorial.client.rest;


import com.ferenckis.tutorial.client.credential.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;

import static java.lang.String.format;

@Component
public class FileRequestFactory {

    @Autowired
    private Credentials workdayCredentials;

    @Value("${rest.master.file.service.base.url}")
    private String workdayBaseUrl;

    public URL newRequest(String workdayMasterFileId) {
        try {
            Authenticator.setDefault(new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(workdayCredentials.getWorkdayUserName(),
                            workdayCredentials.getWorkdayPassword().toCharArray());
                }
            });
            return new URL(workdayBaseUrl + workdayMasterFileId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
