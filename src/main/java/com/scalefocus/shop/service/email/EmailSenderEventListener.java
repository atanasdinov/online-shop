package com.scalefocus.shop.service.email;

import com.scalefocus.shop.configuration.MailConfig;
import com.scalefocus.shop.model.entity.User;
import com.scalefocus.shop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.UUID;


/**
 * <b>User registration event listener.</b>
 */
@Component
public class EmailSenderEventListener implements ApplicationListener<EmailSenderEvent> {

    private static final Logger logger = LoggerFactory.getLogger(EmailSenderEventListener.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MailConfig mailConfig;

    @Override
    public void onApplicationEvent(EmailSenderEvent event) {
        this.sendActivationLink(event);
    }

    /**
     * This method is used to send email to the user containing account activation link.
     *
     * @param event
     */
    private void sendActivationLink(EmailSenderEvent event) {
        String email = mailConfig.getMailProperties().getProperty("mail.source");
        String password = mailConfig.getMailProperties().getProperty("mail.password");

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        };

        Session session = Session.getInstance(mailConfig.getMailProperties(), auth);

        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/user/registrationConfirm?token=" + token;

        try {
            MimeMessage mimeMessage = new MimeMessage(session);

            mimeMessage.setFrom(new InternetAddress(email));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddress));
            mimeMessage.setSubject(subject);
            mimeMessage.setText("Activate your account here:\n " + "http://localhost:8080" + confirmationUrl);

            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("Error with mail confirmation!", e);
        }
    }

}
