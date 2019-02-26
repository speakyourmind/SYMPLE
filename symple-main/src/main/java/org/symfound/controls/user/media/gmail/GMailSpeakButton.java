/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.gmail;

import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.symfound.controls.user.voice.SpeakButton;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public class GMailSpeakButton extends SpeakButton {

    private static final String NAME = GMailSpeakButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "GMail Speak";

    /**
     *
     * @param index
     */
    public GMailSpeakButton(String index) {
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
        setTrimmedSpeakText(GMailViewer.getGMailManager().getIterator().get());
        GMailViewer.getGMailManager().getIterator().modeProperty().addListener((obseravable, oldValue, newValue) -> {
            setTrimmedSpeakText(GMailViewer.getGMailManager().getIterator().get());
        });
    }


    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends GMailSpeakButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

}
