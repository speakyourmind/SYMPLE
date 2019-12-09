/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.symfound.controls.user.type.picto.PictoArea;
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

    public static final Map<Integer, String> KEY_CODE_MAP;

    static {
        HashMap<Integer, String> map = new HashMap<>();
        map.put(ActionKeyCode.UNASSIGNED, "Button Text");
        map.put(ActionKeyCode.BACK_SPACE, "Backspace");
        map.put(ActionKeyCode.SPACE, "Space");
        map.put(ActionKeyCode.CLEAR, "Clear");
        map.put(ActionKeyCode.ENTER, "Enter");
        map.put(ActionKeyCode.TAB, "Tab");
        map.put(ActionKeyCode.ARROW_DOWN, "Down Arrow");
        map.put(ActionKeyCode.ARROW_UP, "Up Arrow");
        map.put(ActionKeyCode.ARROW_LEFT, "Left Arrow");
        map.put(ActionKeyCode.ARROW_RIGHT, "Right Arrow");

        KEY_CODE_MAP = Collections.unmodifiableMap(map);
    }

    ;//TODO: Replace with BidiMap
   public static final Map<String, Integer> REV_KEY_CODE_MAP;

    static {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Button Text", ActionKeyCode.UNASSIGNED);
        map.put("Backspace", ActionKeyCode.BACK_SPACE);
        map.put("Space", ActionKeyCode.SPACE);
        map.put("Clear", ActionKeyCode.CLEAR);
        map.put("Enter", ActionKeyCode.ENTER);
        map.put("Tab", ActionKeyCode.TAB);
        map.put("Down Arrow", ActionKeyCode.ARROW_DOWN);
        map.put("Up Arrow", ActionKeyCode.ARROW_UP);
        map.put("Left Arrow", ActionKeyCode.ARROW_LEFT);
        map.put("Right Arrow", ActionKeyCode.ARROW_RIGHT);

        REV_KEY_CODE_MAP = Collections.unmodifiableMap(map);
    }

    ;

    /**
     *
     * /**
     *
     * @param cssClass
     * @param key
     * @param index
     */
    public TypingControl(String cssClass, String key, String index) {
        super(cssClass, key, index, index);
    }

    /**
     *
     */
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

    /**
     *
     */
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
