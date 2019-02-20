/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.user.selection;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 *
 * @author Javed
 */
public class Chooser {

    /**
     *
     */
    public BooleanProperty selected;

    /**
     *
     * @param value
     */
    public void setSelected(Boolean value) {
        selectedProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isSelected() {
        return selectedProperty().getValue();

    }

    /**
     *
     * @return
     */
    public BooleanProperty selectedProperty() {
        if (selected == null) {
            selected = new SimpleBooleanProperty(false);
        }
        return selected;
    }
}
