package com.ferenckis.tutorial;

import com.ferenckis.tutorial.client.rest.FileDownloader;
import com.ferenckis.tutorial.client.soap.SoapClientFacade;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

// TODO HIVE partition creation
@Component
public class Application {

    private static final Logger LOG = getLogger(Application.class);

    private static final String CONFIG_PATH = "classpath*:application-config.xml";

    @Autowired
    private SoapClientFacade workdaySoapClientFacade;

    @Autowired
    private FileDownloader workdayMasterFileDownloader;

    public static void main(String[] args) {
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext(CONFIG_PATH);
            context.getBean(Application.class).start(args);
        } catch (Exception e) {
            // TODO Assyst ticket creation
            LOG.error("Fatal error", e);
        }
    }

    public void start(String[] args) {
        parseArgs(args);
        download();
    }

    private void parseArgs(String[] args) {
        LOG.info("Parsing arguments");
    }

    private void download() {
        LOG.info("Fetching integrationId");
        String integrationId = workdaySoapClientFacade.callIntegrationIdService();
        LOG.info("Fetching documentId with integrationId={}", integrationId);
        String documentId = workdaySoapClientFacade.callDocumentIdService(integrationId);
        LOG.info("Downloading wd master file with documentId={}", documentId);
        workdayMasterFileDownloader.download(documentId);
        LOG.info("wd master file was downloaded successfully");
    }
}
