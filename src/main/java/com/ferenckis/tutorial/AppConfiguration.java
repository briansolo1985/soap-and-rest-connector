package com.ferenckis.tutorial;

import org.apache.hadoop.fs.FileSystem;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import java.io.IOException;


import static java.lang.String.format;
import static javax.xml.soap.MessageFactory.newInstance;
import static org.apache.hadoop.fs.FileSystem.get;
import static org.joda.time.DateTime.now;
import static org.joda.time.DateTimeZone.UTC;
import static org.joda.time.format.DateTimeFormat.forPattern;

@org.springframework.context.annotation.Configuration
@ComponentScan("com.ferenckis.tutorial")
@PropertySource({"classpath:default.properties", "classpath:credentials.properties"})
public class AppConfiguration {

    private static final String TIMESTAMP_PATTERN = "yyyy-MM-dd";
    private static final String PARTITION_TEMPLATE = "timestamp=%s";

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public MessageFactory messageFactory() throws SOAPException {
        return newInstance();
    }

    @Bean
    public SOAPConnectionFactory soapConnectionFactory() {
        try {
            return SOAPConnectionFactory.newInstance();
        } catch (SOAPException e) {
            throw new RuntimeException("Error initializing SOAPConnectionFactory", e);
        }
    }

    @Bean
    public FileSystem fileSystem() throws IOException {
        return get(new org.apache.hadoop.conf.Configuration());
    }

    @Bean
    public String workdayMasterFilePartitionTag() {
        String timestamp = forPattern(TIMESTAMP_PATTERN).print(now(UTC));
        return format(PARTITION_TEMPLATE, timestamp);
    }

}
