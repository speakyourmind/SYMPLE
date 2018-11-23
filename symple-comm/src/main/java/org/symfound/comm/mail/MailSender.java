package org.symfound.comm.mail;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Javed Gangjee
 */
public class MailSender {

    private static final String NAME = MailSender.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public String strSubject = "";

    /**
     *
     */
    public String strBody = "";

    /**
     *
     */
    public String strRecipientEmail = "";

    /**
     *
     */
    public String strSenderEmail = "";

    private final MailAccount mailAccount;

    /**
     *
     * @param mailAccount
     */
    public MailSender(MailAccount mailAccount) {
        this.mailAccount = mailAccount;
    }

    /**
     *
     * @param toAddress
     * @param subject
     * @param strEmailBody
     */
    public void send(String toAddress, String subject, String strEmailBody) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", mailAccount.getHost());
        props.put("mail.smtp.port", mailAccount.getPort().toString());
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailAccount.getUsername(), mailAccount.getPassword());
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailAccount.getUsername()));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toAddress));
            message.setSubject(subject);
            message.setText(strEmailBody);

            Transport.send(message);

            LOGGER.info("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
