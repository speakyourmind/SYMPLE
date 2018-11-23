package org.symfound.controls.user.voice;

import java.util.prefs.Preferences;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;
import org.symfound.controls.user.PictoArea;

/**
 *
 * @author Javed Gangjee
 */
public class SpeakPictoButton extends SpeakButton {

    private static final String NAME = SpeakPictoButton.class.getName();

    public static final Logger LOGGER = Logger.getLogger(NAME);
    public static final String KEY = "Speak Picto";

    public SpeakPictoButton(String index) {
        super("", KEY, "", index);
    }

    @Override
    public void bindToText() {
        //      speakTextProperty().bindBidirectional(getPictoArea().pictoTextProperty());
    }

    @Override
    public void run() {
        switch (getSpeakControl()) {
            case SPEAK:
                getPictoArea().updatePictoText();
                speak(getPictoArea().getPictoText());
                break;
            case STOP:
                stopSpeak();
                break;
        }
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends SpeakPictoButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }
    public PictoArea picto;

    /**
     *
     * @return
     */
    public final PictoArea getPictoArea() {
        if (picto == null) {
            if (!getPictoID().isEmpty()) {
                final String pictoHash = "#" + getPictoID();
                // Or it can lookup another pane in the scene.
                picto = (PictoArea) getScene().lookup(pictoHash);
            }
        }
        return picto;
    }
    private StringProperty pictoID;

    /**
     *
     * @param value
     */
    public void setPictoID(String value) {
        pictoIDProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getPictoID() {
        return pictoIDProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty pictoIDProperty() {
        if (pictoID == null) {
            pictoID = new SimpleStringProperty("");
        }
        return pictoID;
    }
}
