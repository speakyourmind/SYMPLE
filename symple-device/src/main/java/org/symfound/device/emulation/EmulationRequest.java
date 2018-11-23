/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.emulation;

import java.awt.Point;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Javed Gangjee
 */
public class EmulationRequest {

    private static final Point START_POSITION = new Point(0, 0);
    private ObjectProperty<Point> position;

    /**
     *
     * @param value
     */
    public void setPosition(Point value) {
        positionProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Point getPosition() {
        return positionProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<Point> positionProperty() {
        if (position == null) {
            position = new SimpleObjectProperty<>(START_POSITION);
        }
        return position;
    }

    private BooleanProperty click;

    /**
     *
     * @param value
     */
    public void setClick(Boolean value) {
        clickProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean getClick() {
        return clickProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty clickProperty() {
        if (click == null) {
            click = new SimpleBooleanProperty();
        }
        return click;
    }

    private StringProperty keyPressed;

    /**
     *
     * @param value
     */
    public void setKeyPressed(String value) {
        keyPressed().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getKeyPressed() {
        return keyPressed().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty keyPressed() {
        if (keyPressed == null) {
            keyPressed = new SimpleStringProperty();
        }
        return keyPressed;
    }
}
