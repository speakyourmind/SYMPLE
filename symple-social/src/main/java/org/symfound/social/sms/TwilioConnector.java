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
   // SECURITY TO DO: Hide SID and Token

    /**
     *
     */
    public static final String ACCOUNT_SID = "AC6a37be7aaf5843480abe36821ed002c1";

    /**
     *
     */
    public static final String AUTH_TOKEN = "c9b94f75df200722e4ff2eb607763554";

    /**
     *
     */
    public void connect() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

}
