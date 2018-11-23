package org.symfound.social.sms;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.symfound.social.SocialMediaPoster;

public class TwilioPoster extends SocialMediaPoster {

    public void textMessage(String to, String from, String message) {
        getConnector().connect();

        Message sms = Message.creator(new PhoneNumber(to),
                new PhoneNumber(from),
                message).create();

        System.out.println(sms.getSid());
    }

    private TwilioConnector connector;

    public TwilioConnector getConnector() {
        if (connector == null) {
            connector = new TwilioConnector();
        }
        return connector;
    }


}
