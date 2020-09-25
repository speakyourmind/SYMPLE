/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.emulation.input.switcher;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.symfound.device.emulation.input.InputListener;
import org.symfound.device.hardware.Hardware;
import org.symfound.device.hardware.characteristic.Selectability;

/**
 *
 * @author Javed Gangjee
 */
public class SwitchListener extends InputListener {

    /**
     *
     */
    public final Hardware hardware;

    /**
     *
     * @param hardware
     */
    public SwitchListener(Hardware hardware) {
        this.hardware = hardware;
        initialize();
    }

    private void initialize() {
        getZScore().magnitudeProperty().addListener((observable, oldValue, newValue) -> {
            Selectability selectability = hardware.getSelectability();
            final Double threshold = selectability.getSwitchability().getThreshold();
            final Double hysteresis = selectability.getSwitchability().getHysteresis();
            final double magnitudeVal = newValue.doubleValue();
            if (magnitudeVal > threshold * (1 + hysteresis)) {
                setSwitched(true);
                LOGGER.debug("Switch activated at magnitude:" + magnitudeVal);
            }
            if (magnitudeVal < threshold * (1 - hysteresis)) {
                setSwitched(false);
                LOGGER.debug("Switch deactivated at magnitude:" + magnitudeVal);
            }
        });

    }

    /**
     *
     */
    @Override
    public void start() {
        // Use Mouse Listener instead.
    }

    private BooleanProperty switched;

    /**
     *
     * @param value
     */
    public void setSwitched(Boolean value) {
        switchedProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isSwitched() {
        return switchedProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty switchedProperty() {
        if (switched == null) {
            switched = new SimpleBooleanProperty(false);
        }
        return switched;
    }

    private ObjectProperty<ZScore> zScore;

    /**
     *
     * @return
     */
    public ZScore getZScore() {
        return zScoreProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<ZScore> zScoreProperty() {
        if (zScore == null) {
            zScore = new SimpleObjectProperty<>(new ZScore());
        }
        return zScore;
    }

}
