package org.symfound.controls.user.social.twilio;

import java.util.prefs.Preferences;

import org.apache.log4j.Logger;


import org.symfound.controls.AppableControl;

/**
 *
 * @author Javed Gangjee
 */
public final class TwilioHistoryControlButton extends AppableControl {

    /**
     *
     */
    public static final String NAME = TwilioHistory.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    private static final boolean BACKUP_ON_EXIT = Boolean.TRUE;

    /**
     *
     */
    public static final String KEY = "Twilio Control";

    /**
     *
     */
    public static final String DEFAULT_TITLE = "";

    public TwilioHistoryControlButton(String index) {
        super("transparent", KEY, DEFAULT_TITLE, index);
        initialize();
    }

    private void initialize() {
    
    }

    @Override
    public void run() {

    }

    

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends TwilioHistoryControlButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

}
