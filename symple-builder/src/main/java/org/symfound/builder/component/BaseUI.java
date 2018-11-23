/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

/**
 *
 * @author Javed Gangjee
 */
public class BaseUI extends Stage {

    /**
     *
     */
    public static final String NAME = BaseUI.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String APP_TITLE = "SYMPLE by SpeakYourMind Foundation";

    /**
     *
     */
    public Boolean fullscreen = true;

    /**
     *
     */
    public Boolean resizeable = false;

    /**
     *
     */
    public Boolean maximized = false;

    /**
     *
     */
    public void open() {
        //  setCurrentProperties();
        show();
        toFront();
        setMaximized(true);
        setFullScreen(true);
    }

    /**
     *
     */
    public void supressFullScreenHint() {
        // Disables the hint when the Apps goes into fullscreen
        setFullScreenExitHint("");
        setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    }

    /**
     *
     */
    public void setCurrentProperties() {
        setResizable(resizeable);
        setFullScreen(fullscreen);
    }

    private BooleanProperty built;

    /**
     *
     * @return
     */
    public Boolean isBuilt() {
        return builtProperty().getValue();
    }

    /**
     *
     * @param value
     */
    public void setBuilt(Boolean value) {
        builtProperty().setValue(value);
    }

    /**
     *
     * @return built
     */
    public BooleanProperty builtProperty() {
        if (built == null) {
            built = new SimpleBooleanProperty(Boolean.FALSE);
        }
        return built;
    }
}
