package com.ferenckis.tutorial.client.credential;

import com.ferenckis.tutorial.util.CredentialHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Credentials {

    @Autowired
    private CredentialHandler credentialHandler;

    @Value("${user.name}")
    private String workdayUserName;

    @Value("${password}")
    private String workdayPassword;

    public String getWorkdayUserName() {
        return credentialHandler.decrypt(workdayUserName);
    }

    public String getWorkdayPassword() {
        return credentialHandler.decrypt(workdayPassword);
    }
}
