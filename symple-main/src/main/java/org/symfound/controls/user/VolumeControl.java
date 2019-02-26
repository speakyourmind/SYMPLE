/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.symfound.controls.AppableControl;

/**
 *
 * @author Javed Gangjee
 */
public abstract class VolumeControl extends AppableControl {

    /**
     *
     */
    public static final Double CHANGE_RATE_VOLUME = 2.0;

    /**
     *
     */
    public static final double STEP_VOLUME = 0.2;

    /**
     *
     */
    public static final double MIN_VOLUME = 0.3;

    /**
     *
     */
    public static final double MAX_VOLUME = 1.0;

    /**
     *
     */
    public static final double DEFAULT_VOLUME = 0.8;

    /**
     *
     */
    public Double initVolume;
    private static DoubleProperty volume;

    /**
     *
     * @param CSSClass
     * @param key
     * @param title
     * @param index
     */
    public VolumeControl(String CSSClass, String key, String title, String index) {
        super(CSSClass, key, title, index);
    }

    /**
     *
     * @param volume
     */
    public void setVolume(double volume) {
        volumeProperty().set(volume);
    }

    /**
     *
     * @return
     */
    public double getVolume() {
        return volumeProperty().get();
    }

    /**
     *
     * @return
     */
    public DoubleProperty volumeProperty() {
        if (volume == null) {
            volume = new SimpleDoubleProperty(DEFAULT_VOLUME);
        }
        return volume;
    }

}
