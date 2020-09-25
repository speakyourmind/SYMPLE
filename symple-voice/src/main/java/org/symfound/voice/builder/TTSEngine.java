/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.voice.builder;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.symfound.voice.player.AudioPlayer;

/**
 *
 * @author Javed Gangjee
 */
public abstract class TTSEngine extends AudioPlayer {

    /**
     *
     */
    public AdvancedPlayer player;
    private final String initName;

    /**
     *
     * @param initName
     */
    public TTSEngine(String initName) {
        this.initName = initName;
    }

    /**
     *
     */
    public abstract void load();

    /**
     *
     * @param text
     * @param play
     * @param voice
     */
    public abstract void run(String text, Boolean play, String voice);

    private StringProperty name;

    /**
     *
     * @param value
     */
    public void setName(String value) {
        // Validate value. Throw exception.
        nameProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getName() {
        return nameProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty nameProperty() {
        if (name == null) {
            name = new SimpleStringProperty(initName);
        }
        return name;
    }
    
    
    private static final Boolean DEFAULT_STOP_VALUE = Boolean.FALSE;
    private BooleanProperty stop;

    /**
     *
     * @param value
     */
    public void setStopped(Boolean value) {
        stoppedProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isStopped() {
        return stoppedProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty stoppedProperty() {
        if (stop == null) {
            stop = new SimpleBooleanProperty(DEFAULT_STOP_VALUE);
        }
        return stop;
    }
}
