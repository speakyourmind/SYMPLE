/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.type.picto;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Javed
 */
public abstract class TextCommunicatorButton extends PictoControl {

    /**
     *
     * @param CSSClass
     * @param key
     * @param title
     * @param index
     */
    public TextCommunicatorButton(String CSSClass, String key, String title, String index) {
        super(CSSClass, key, title, index);

    }

    /**
     *
     */
    public abstract void bindToText();

    private static final String DEFAULT_COMM_TEXT = "";
    private StringProperty commText;

    /**
     *
     * @param value
     */
    public void setCommText(String value) {
        commTextProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getCommText() {
        return commTextProperty().getValue();
    }

    /**
     *
     * @return
     */
    public final StringProperty commTextProperty() {
        if (commText == null) {
            commText = new SimpleStringProperty(DEFAULT_COMM_TEXT);
        }
        return commText;
    }
}
