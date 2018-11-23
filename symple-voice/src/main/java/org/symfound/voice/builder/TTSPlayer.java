/*
 * Copyright (C) 2014 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.symfound.voice.builder;

import static java.lang.Boolean.TRUE;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;
import static org.apache.log4j.Logger.getLogger;

/**
 * Plays audio generated from a string by Text-To-Speech Engines.
 * <p>
 * <b>NOTE:</b> Currently only MaryTTS has been implemented
 *
 * @author Javed Gangjee
 * @param <T>
 */
public class TTSPlayer<T extends TTSEngine> {

    private static final String NAME = TTSPlayer.class.getName();
    private static final Logger LOGGER = getLogger(NAME);

    private final T t;

    /**
     *
     * @param t
     */
    public TTSPlayer(T t) {
        this.t = t;
    }

    /**
     *
     * @return
     */
    public T getEngine() {
        return t;
    }

    /**
     *
     * @param text
     * @param play
     * @param voice
     */
    public void play(String text, Boolean play, String voice) {
        Thread thread = new Thread(() -> {
            t.run(text, play, voice);
            
        });
        thread.start();

    }

    /**
     *
     * @param play
     * @param voice
     */
    public void play(Boolean play, String voice) {
        play(getText(), play, voice);
    }

    /**
     *
     * @param voice
     */
    public void play(String voice) {
        play(getText(), TRUE, voice);
    }

    /*   public void stop() {
        t.stop();
    }*/
    /**
     *
     */
    public static final String DEFAULT_TEXT = "";
    private StringProperty text;

    /**
     * Sets the text that will be spoken by the player
     *
     * @param value text to be spoken
     */
    public void setText(String value) {
        textProperty().setValue(value);
    }

    /**
     * Gets the current text that will be spoken by the player
     *
     * @return text
     */
    public String getText() {
        return textProperty().getValue();
    }

    /**
     * Represents the current text that will be spoken by the player
     *
     * @return text
     */
    public StringProperty textProperty() {
        if (text == null) {
            text = new SimpleStringProperty(DEFAULT_TEXT);
        }
        return text;
    }

}
