/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.Twilio;

import java.util.Arrays;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.symfound.builder.user.characteristic.Social;
import org.symfound.main.Main;
import org.symfound.media.MediaManager;
import org.symfound.social.sms.TwilioReader;

/**
 *
 * @author Javed Gangjee <jgangjee@Twilio.com>
 */
public class TwilioManager extends MediaManager<String> {

    private static final String NAME = TwilioManager.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public TwilioManager() {
        super(Arrays.asList(""));
    }

    @Override
    public void run() {
        List<String> messages = retrieveMessages(new DateTime(2019, 8, 13, 0, 0), "+15197090119");
        LOGGER.info(messages.size() + " messages retrieved");
        getIterator().setTypes(messages);
    }

    /**
     *
     * @param since
     * @param fromNumber
     * @return
     */
    public List<String> retrieveMessages(DateTime since, String fromNumber) {
        List<String> messages = getTwilioReader().read(since, fromNumber);
        return messages;

    }

    private TwilioReader twilioReader;

    private TwilioReader getTwilioReader() {
        if (twilioReader == null) {
            final Social social = Main.getSession().getUser().getSocial();
            twilioReader = new TwilioReader(social.getTwilioAccountSID(), social.getTwilioAuthToken());
        }
        return twilioReader;
    }

    private StringProperty since;

    /**
     *
     * @param value
     */
    public void setSince(String value) {
        sinceProperty().set(value);
    }

    /**
     *
     * @return
     */
    public String getSince() {
        return sinceProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty sinceProperty() {
        if (since == null) {
            since = new SimpleStringProperty();
        }
        return since;
    }
}
