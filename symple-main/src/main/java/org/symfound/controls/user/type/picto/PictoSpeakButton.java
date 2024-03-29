package org.symfound.controls.user.type.picto;

import java.util.prefs.Preferences;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;
import org.symfound.controls.user.voice.SpeakButton;
import org.symfound.tools.iteration.ParallelList;

/**
 *
 * @author Javed Gangjee
 */
public class PictoSpeakButton extends SpeakButton {

    private static final String NAME = PictoSpeakButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Picto Speak";

    /**
     *
     * @param index
     */
    public PictoSpeakButton(String index) {
        super("", KEY, "Speak", index);
    }

    /**
     *
     */
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
                Boolean clearAfter = true;
                if (clearAfter) {
                    final ParallelList<String, String> order = new ParallelList<>();
                    order.getFirstList().add("Replace Key");
                    order.getSecondList().add("default");

                    getPictoArea().getConfigurableGrid().setOrder(order);
                    getPictoArea().getConfigurableGrid().reload();
                    getPictoArea().setPictoText("");
                }
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
            Class<? extends PictoSpeakButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

}
