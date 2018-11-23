/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.user.characteristic;

import java.util.Properties;
import java.util.prefs.Preferences;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.symfound.builder.characteristic.Characteristic;
import org.symfound.comm.mail.MailAccount;

/**
 *
 * @author Javed Gangjee
 */
public class Social extends Characteristic {

    /**
     *
     * @param preferences
     * @param properties
     */
    public Social(Preferences preferences, Properties properties) {
        super(preferences, properties);
    }

    private static final String MAIL_USERNAME_KEY = "app.social.mail.username";
    private static final String MAIL_PASSWORD_KEY = "app.social.mail.password";
    private static final String MAIL_INCOMING = "app.social.mail.incoming";
    private static final String MAIL_OUTGOING = "app.social.mail.outgoing";
    private static final String MAIL_PORT_KEY = "app.social.mail.port";
    private ObjectProperty<MailAccount> mailAccount;

    /**
     *
     * @param value
     */
    public void setMailAccount(MailAccount value) {
        mailAccountProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public MailAccount getMailAccount() {
        return mailAccountProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<MailAccount> mailAccountProperty() {
        if (mailAccount == null) {
            String initUsername = getPreference(MAIL_USERNAME_KEY);
            String initPassword = getPreference(MAIL_PASSWORD_KEY);
            String initIncoming = getPreference(MAIL_INCOMING);
            String initOutgoing = getPreference(MAIL_OUTGOING);
            Integer initPort = Integer.valueOf(getPreference(MAIL_PORT_KEY));
            MailAccount initValue = new MailAccount(initUsername, initPassword, initIncoming, initOutgoing, initPort);
            mailAccount = new SimpleObjectProperty<>(initValue);
        }
        return mailAccount;
    }
}
