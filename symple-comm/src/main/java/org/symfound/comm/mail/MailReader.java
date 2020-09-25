package org.symfound.comm.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.mail.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Javed Gangjee
 */
public class MailReader {

    private static final String NAME = MailReader.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    private Folder inbox;
    private Integer messageCount;
    private Integer index = 0;

    private StringProperty from;
    private StringProperty subject;
    private StringProperty body;

    private final MailAccount mailAccount;

    /**
     *
     * @param mailAccount
     */
    public MailReader(MailAccount mailAccount) {
        this.mailAccount = mailAccount;
    }

    /**
     *
     * @throws NoSuchProviderException
     * @throws MessagingException
     */
    public void load() throws NoSuchProviderException, MessagingException {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        Session session = Session.getInstance(props, null);
        Store store = session.getStore();
        store.connect(mailAccount.getIncoming(), mailAccount.getUsername(), mailAccount.getPassword());
        inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);
        messageCount = inbox.getMessageCount();
        index = messageCount;
    }

    /**
     *
     * @throws MessagingException
     * @throws IOException
     */
    public void getNext() throws MessagingException, IOException {
        if (index <= 1) {
            index = messageCount;
        } else {
            index--;
        }
        Message message = inbox.getMessage(index);
        Address[] in = message.getFrom();
        String from = "";
        for (Address address : in) {
            LOGGER.info("FROM:" + address.toString());
            from += address.toString() + "; ";
        }

        setFrom(from);

        Multipart mp = (Multipart) message.getContent();
        BodyPart bp = mp.getBodyPart(0);

        LOGGER.info("SENT DATE:" + message.getSentDate());

        setSubject(message.getSubject());
        setBody(bp.getContent().toString());

        /*   try {
         List<InputStream> attachments = getAttachments(message);
         } catch (Exception ex) {
         Logger.getLogger(MailReader.class.getName()).fatal( null, ex);
         }*/
    }

    /**
     *
     * @param message
     * @return
     * @throws MessagingException
     * @throws Exception
     */
    public List<InputStream> getAttachments(Message message) throws MessagingException, Exception {
        Object content = message.getContent();
        if (content instanceof String) {
            return null;
        }

        if (content instanceof Multipart) {
            Multipart multipart = (Multipart) content;
            List<InputStream> result = new ArrayList<>();

            for (int i = 0; i < multipart.getCount(); i++) {
                result.addAll(getAttachments(multipart.getBodyPart(i)));
            }
            return result;

        }
        return null;
    }

    private List<InputStream> getAttachments(BodyPart part) throws Exception {
        List<InputStream> result = new ArrayList<>();
        Object content = part.getContent();
        if (content instanceof InputStream || content instanceof String) {
            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())
                    || !part.getFileName().isEmpty()) {
                result.add(part.getInputStream());
                return result;
            } else {
                return new ArrayList<>();
            }
        }

        if (content instanceof Multipart) {
            Multipart multipart = (Multipart) content;
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                result.addAll(getAttachments(bodyPart));
            }
        }
        return result;
    }

    /**
     *
     * @param value
     */
    public void setFrom(String value) {
        fromProperty().setValue(value);

    }

    /**
     *
     * @return
     */
    public String getFrom() {
        return fromProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty fromProperty() {
        if (from == null) {
            from = new SimpleStringProperty("");
        }
        return from;
    }

    /**
     *
     * @param value
     */
    public void setSubject(String value) {
        subjectProperty().setValue(value);

    }

    /**
     *
     * @return
     */
    public String getSubject() {
        return subjectProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty subjectProperty() {
        if (subject == null) {
            subject = new SimpleStringProperty("");
        }
        return subject;
    }

    /**
     *
     * @param value
     */
    public void setBody(String value) {
        bodyProperty().setValue(value);

    }

    /**
     *
     * @return
     */
    public String getBody() {
        return bodyProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty bodyProperty() {
        if (body == null) {
            body = new SimpleStringProperty("");
        }
        return body;
    }
}
