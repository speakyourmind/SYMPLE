/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.social.sms;

import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Message;
import org.joda.time.DateTime;
import org.symfound.social.SocialMediaReader;

public class TwilioReader extends SocialMediaReader {
final String accountSID;
    final String authToken;

    public TwilioReader(String accountSID, String authToken) {
        this.accountSID = accountSID;
        this.authToken = authToken;
    }

    //public static void main(String[] args) {
        
 //       TwilioPoster.getConnector().connect("AC6a37be7aaf5843480abe36821ed002c1", "c9b94f75df200722e4ff2eb607763554");
  
        
   // }

    public void read(DateTime since,String fromNumber) {
        TwilioConnector.connect(accountSID, authToken);
         ResourceSet<Message> messages = Message.reader()
                .setDateSent(new DateTime(2019, 8, 10, 0, 0))
                .setFrom(new com.twilio.type.PhoneNumber(fromNumber))
                .limit(20)
                .read();
        for (Message record : messages) {
            System.out.println(record.getSid());
            Message message = Message.fetcher(record.getSid())
                    .fetch();

            System.out.println(message.getTo() + " " + message.getBody());
        }
    }
}
