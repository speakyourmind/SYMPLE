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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
    
    // TO DO: Important - To Encrypt
    
    private static final String FROM_NUMBER_KEY = "app.social.twilio.fromNumber";
    private StringProperty twilioFromNumber;

    /**
     *
     * @param value
     */
    public void setTwilioFromNumber(String value) {
        twilioFromNumberProperty().setValue(value);
        getPreferences().put(FROM_NUMBER_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getTwilioFromNumber() {
        return twilioFromNumberProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty twilioFromNumberProperty() {
        if (twilioFromNumber == null) {
            String initValue = getPreference(FROM_NUMBER_KEY);
            twilioFromNumber = new SimpleStringProperty(initValue);
        }
        return twilioFromNumber;
    }
    
    
    
    // TO DO: Important - To Encrypt
    
    private static final String ACCOUNT_SID_KEY = "app.social.twilio.accountSID";
    private StringProperty twilioAccountSID;

    /**
     *
     * @param value
     */
    public void setTwilioAccountSID(String value) {
        twilioAccountSIDProperty().setValue(value);
        getPreferences().put(ACCOUNT_SID_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getTwilioAccountSID() {
        return twilioAccountSIDProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty twilioAccountSIDProperty() {
        if (twilioAccountSID == null) {
            String initValue = getPreference(ACCOUNT_SID_KEY);
            twilioAccountSID = new SimpleStringProperty(initValue);
        }
        return twilioAccountSID;
    }
    
     private static final String AUTH_TOKEN_KEY = "app.social.twilio.authToken";
    private StringProperty twilioAuthToken;

    /**
     *
     * @param value
     */
    public void setTwilioAuthToken(String value) {
        twilioAuthTokenProperty().setValue(value);
        getPreferences().put(AUTH_TOKEN_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getTwilioAuthToken() {
        return twilioAuthTokenProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty twilioAuthTokenProperty() {
        if (twilioAuthToken == null) {
            String initValue = getPreference(AUTH_TOKEN_KEY);
            twilioAuthToken = new SimpleStringProperty(initValue);
        }
        return twilioAuthToken;
    }
}
