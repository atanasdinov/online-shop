package com.scalefocus.shop.service.email;

import com.scalefocus.shop.model.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;


/**
 * <b>Custom event used for indicating a completion of user registration process.</b>
 */
public class EmailSenderEvent extends ApplicationEvent {

    private static final Logger logger = LoggerFactory.getLogger(EmailSenderEvent.class);

    private String appUrl;
    private Locale locale;
    private User user;

    public EmailSenderEvent(User user, Locale locale, String appUrl) {
        super(user);
        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
