package org.symfound.social.sms;

import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.symfound.social.SocialMediaPoster;

/**
 *
 * @author Administrator
 */
public class TwilioPoster extends SocialMediaPoster {

    String accountSID;
    String authToken;

    public TwilioPoster(String accountSID, String authToken) {
        this.accountSID = accountSID;
        this.authToken = authToken;
    }

    /**
     *
     * @param to
     * @param from
     * @param message
     */
    public void textMessage(String to, String from, String message) {
        TwilioConnector.connect(accountSID, authToken);
        Message.creator(new PhoneNumber(to),
                new PhoneNumber(from),
                message).create();

    }

    private static TwilioConnector connector;

    /**
     *
     * @return
     */
    public static TwilioConnector getConnector() {
        if (connector == null) {
            connector = new TwilioConnector();
        }
        return connector;
    }

}
