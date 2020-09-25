/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.social.sms;

import com.google.common.collect.Range;
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

    public List<Message> readContactsSMS(Range<DateTime> range, String contactsNumber, String myNumber) {
        TwilioConnector.connect(accountSID, authToken);
        return getSMSs(range, contactsNumber);
    }

    public List<Message> readMySMSToContact(Range<DateTime> range, String contactsNumber, String myNumber) {
        TwilioConnector.connect(accountSID, authToken);
        List<Message> mySMSToContact = new ArrayList<>();
        List<Message> allMySMS = getSMSs(range, myNumber);
        allMySMS.forEach((message) -> {
            final String to = message.getTo();
            if (to.equals(contactsNumber)) {
                mySMSToContact.add(message);
            }
        });
        return mySMSToContact;
    }

    public List<Message> getSMSs(Range<DateTime> range, String myNumber) {
        ResourceSet<Message> mySMSSet = Message.reader()
                .setDateSent(range)
                .setFrom(new PhoneNumber(myNumber))
                .limit(20)
                .read();
        List<Message> smsList = new ArrayList<>();
        for (Message record : mySMSSet) {
            Message message = Message.fetcher(record.getSid())
                    .fetch();
            smsList.add(message);
        }
        return smsList;
    }
}
