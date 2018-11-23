/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.symfound.controls.AppableControl;
import org.symfound.device.emulation.input.keyboard.ActionKeyCode;

/**
 *
 * @author Javed Gangjee
 */
public abstract class TypingControl extends AppableControl {

    /**

    /**
     *
     * @param cssClass
     * @param key
     * @param index
     */
    public TypingControl(String cssClass, String key, String index) {
        super(cssClass, key, index, index);
    }

    public ActiveTextArea textArea;
    /**
     *
     * @return
     */
    public final ActiveTextArea getTextArea() {
        if (textArea == null) {
            if (!getTextAreaID().isEmpty()) {
                final String textAreaHash = "#" + getTextAreaID();
                // Or it can lookup another pane in the scene.
                textArea = (ActiveTextArea) getScene().lookup(textAreaHash);
            }
        }
        return textArea;
    }
    private StringProperty textAreaID;

    /**
     *
     * @param value
     */
    public void setTextAreaID(String value) {
        textAreaIDProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getTextAreaID() {
        return textAreaIDProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty textAreaIDProperty() {
        if (textAreaID == null) {
            textAreaID = new SimpleStringProperty("");
        }
        return textAreaID;
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
    /**
     *
     */
    public static final int DEFAULT_KEY_CODE = ActionKeyCode.UNASSIGNED;

    /**
     *
     */
    public IntegerProperty actionKey;

    /**
     *
     * @param value
     */
    public void setActionKey(int value) {
        actionKeyProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public int getActionKey() {
        return actionKeyProperty().getValue();
    }

    /**
     *
     * @return
     */
    public IntegerProperty actionKeyProperty() {
        if (actionKey == null) {
            actionKey = new SimpleIntegerProperty(DEFAULT_KEY_CODE);
        }
        return actionKey;
    }
}
