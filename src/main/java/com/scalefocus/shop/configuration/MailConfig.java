package com.scalefocus.shop.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;


/**
 * <b>A configuration class for configuring the JavaMailSender used to send verification emails upon registration.</b>
 */
@Configuration
@ComponentScan
@PropertySource(value = {"classpath:mail.properties"})
public class MailConfig {

    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    private static final String MAIL_SMTP_PORT = "mail.smtp.port";
    private static final String MAIL_SOURCE = "mail.source";
    private static final String MAIL_PASSWORD = "mail.password";

    @Autowired
    private Environment env;

    /**
     * Configuration bean configuring a JavaMailSender.
     *
     * @return configured {@link JavaMailSender} object.
     */
    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setJavaMailProperties(getMailProperties());

        return javaMailSender;
    }

    /**
     * @return {@link Properties} object with configured mail account.
     */
    public Properties getMailProperties() {
        Properties properties = new Properties();

        properties.setProperty(MAIL_SMTP_HOST, env.getProperty(MAIL_SMTP_HOST));
        properties.setProperty(MAIL_SMTP_PORT, env.getProperty(MAIL_SMTP_PORT));
        properties.setProperty(MAIL_SOURCE, env.getProperty(MAIL_SOURCE));
        properties.setProperty(MAIL_PASSWORD, env.getProperty(MAIL_PASSWORD));
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        return properties;
    }

}