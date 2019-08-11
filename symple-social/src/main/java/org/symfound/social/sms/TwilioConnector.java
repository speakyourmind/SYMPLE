/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.social.sms;

import com.twilio.Twilio;
import org.symfound.social.SocialMediaConnector;

/**
 *
 * @author Javed
 */
public class TwilioConnector extends SocialMediaConnector {
    // Find your Account Sid and Token at twilio.com/user/account

    private static Boolean connected;

    public static void connect(String accountSID, String authToken) {
        if (!connected) {
            Twilio.init(accountSID, authToken);
            connected = true;
        }
    }

}
