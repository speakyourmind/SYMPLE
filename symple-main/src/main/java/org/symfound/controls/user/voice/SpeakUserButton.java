package org.symfound.controls.user.voice;

import java.util.prefs.Preferences;
import org.apache.log4j.Logger;

/**
 *
 * @author Javed Gangjee
 */
public class SpeakUserButton extends SpeakButton {

    private static final String NAME = SpeakUserButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Speak Main";

    /**
     *
     */
    public SpeakUserButton() {
        super("speak-field-button", KEY, "", "default");
    }

    /**
     *
     * @param index
     */
    public SpeakUserButton(String index) {
        super("speak-field-button", KEY, "", index);
    }

    /**
     *
     */
    @Override
    public void bindToText() {
        commTextProperty().bindBidirectional(getUser().getTyping().activeTextProperty());
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends SpeakUserButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

}
