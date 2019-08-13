/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.Twilio;

import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.symfound.controls.user.voice.SpeakButton;

/**
 *
 * @author Javed Gangjee <jgangjee@Twilio.com>
 */
public class TwilioSpeakButton extends SpeakButton {

    private static final String NAME = TwilioSpeakButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Twilio Speak";

    /**
     *
     * @param index
     */
    public TwilioSpeakButton(String index) {
        super("speak-field-button", KEY, "", index);
        initialize();
    }

    private void initialize() {

    }

    /**
     *
     */
    @Override
    public void bindToText() {
        setTrimmedSpeakText(TwilioViewer.getTwilioManager().getIterator().get());
        TwilioViewer.getTwilioManager().getIterator().modeProperty().addListener((obseravable, oldValue, newValue) -> {
            setTrimmedSpeakText(TwilioViewer.getTwilioManager().getIterator().get());
        });
    }


    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends TwilioSpeakButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

}
