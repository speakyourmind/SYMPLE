/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.social.sms;

import com.twilio.Twilio;
import org.apache.log4j.Logger;
import org.symfound.social.SocialMediaConnector;

/**
 *
 * @author Javed
 */
public class TwilioConnector extends SocialMediaConnector {
    
    private static final String NAME = TwilioConnector.class.getName();
    public static final Logger LOGGER = Logger.getLogger(NAME);
    
    private static Boolean connected = false;

    public static void connect(String accountSID, String authToken) {
        if (!connected) {
            Twilio.init(accountSID, authToken);
            connected = true;
            LOGGER.info("Successfully connected to Twilio");
        }
    }

}
