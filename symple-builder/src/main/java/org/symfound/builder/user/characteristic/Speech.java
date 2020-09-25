/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.user.characteristic;

import java.util.Properties;
import java.util.prefs.Preferences;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.symfound.builder.characteristic.Characteristic;

/**
 *
 * @author Javed Gangjee
 */
public class Speech extends Characteristic {

    /**
     *
     * @param preferences
     * @param properties
     */
    public Speech(Preferences preferences, Properties properties) {
        super(preferences, properties);
    }

    private static final String SPEAKING_VOICE_KEY = "app.speaking.voice";
    private StringProperty speakingVoice;

    /**
     *
     * @param value
     */
    public void setSpeakingVoice(String value) {
        speakingVoiceProperty().setValue(value);
        getPreferences().put(SPEAKING_VOICE_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getSpeakingVoice() {
        return speakingVoiceProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty speakingVoiceProperty() {
        if (speakingVoice == null) {
            String initValue = getPreference(SPEAKING_VOICE_KEY);
            speakingVoice = new SimpleStringProperty(initValue);
        }
        return speakingVoice;
    }
    
    
    private static final String READING_VOICE_KEY = "app.speaking.readingVoice";
    private StringProperty readingVoice;

    /**
     *
     * @param value
     */
    public void setReadingVoice(String value) {
        readingVoiceProperty().setValue(value);
        getPreferences().put(READING_VOICE_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getReadingVoice() {
        return readingVoiceProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty readingVoiceProperty() {
        if (readingVoice == null) {
            String initValue = getPreference(READING_VOICE_KEY);
            readingVoice = new SimpleStringProperty(initValue);
        }
        return readingVoice;
    }
}
