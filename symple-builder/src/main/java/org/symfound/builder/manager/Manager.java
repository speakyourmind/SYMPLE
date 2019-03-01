/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.manager;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author Javed Gangjee
 * @param <T>
 */
public abstract class Manager<T> {

    /**
     *
     */
    public ObjectProperty<T> current;

    /**
     *
     * @param value
     */
    public void setCurrent(T value) {
        currentProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public T getCurrent() {
        return currentProperty().get();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<T> currentProperty() {
        if (current == null) {
            current = new SimpleObjectProperty<>();
        }
        return current;
    }

}
