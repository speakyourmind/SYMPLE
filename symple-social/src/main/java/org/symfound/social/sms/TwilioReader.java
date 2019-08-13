/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.social.sms;

import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.symfound.social.SocialMediaReader;

public class TwilioReader extends SocialMediaReader {

    private static final String NAME = TwilioReader.class.getName();

    public static final Logger LOGGER = Logger.getLogger(NAME);

    final String accountSID;
    final String authToken;

    public TwilioReader(String accountSID, String authToken) {
        this.accountSID = accountSID;
        this.authToken = authToken;
    }

    public List<String> read(DateTime since, String fromNumber) {
         TwilioConnector.connect(accountSID, authToken);
        List<String> messageText = new ArrayList<>();
        ResourceSet<Message> messages = Message.reader()
            //    .setDateSent(new DateTime(2019, 8, 10, 0, 0))
              .setFrom(new PhoneNumber(fromNumber))
                .limit(20)
                .read();

        for (Message record : messages) {
            LOGGER.info(record.getSid());
            Message message = Message.fetcher(record.getSid())
                    .fetch();
            messageText.add(message.getBody());
            LOGGER.info(message.getFrom() + " " + message.getBody());
        }
        return messageText;
    }
}
