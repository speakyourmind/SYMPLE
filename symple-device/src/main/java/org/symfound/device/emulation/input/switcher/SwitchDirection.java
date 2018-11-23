/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.emulation.input.switcher;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Javed Gangjee
 */
public class SwitchDirection {

    /**
     *
     * @param name
     * @param angle
     */
    public SwitchDirection(String name, Double angle) {
        this.initName = name;
        this.initAngle = angle;
    }

    private StringProperty name;
    private final String initName;

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

    private DoubleProperty angle;
    private final Double initAngle;

    /**
     *
     * @return
     */
    public Double getAngle() {
        return angleProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty angleProperty() {
        if (angle == null) {
            angle = new SimpleDoubleProperty(initAngle);
        }
        return angle;
    }
    
}
