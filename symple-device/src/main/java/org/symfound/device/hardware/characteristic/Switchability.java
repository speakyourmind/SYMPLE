/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.hardware.characteristic;

import java.util.Properties;
import java.util.prefs.Preferences;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.symfound.builder.characteristic.Characteristic;

/**
 *
 * @author Javed Gangjee
 */
public class Switchability extends Characteristic {

    /**
     *
     * @param preferences
     * @param properties
     */
    public Switchability(Preferences preferences, Properties properties) {
        super(preferences, properties);
    }
    
    /**
     *
     */
    public static final String ENABLE_KEY = "selectability.switch";
    private BooleanProperty enabled;

    /**
     *
     * @param value
     */
    public void setEnabled(Boolean value) {
        enabledProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isEnabled() {
        return enabledProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty enabledProperty() {
        if (enabled == null) {
            Boolean initValue = Boolean.parseBoolean(getDefaultConfiguration().getProperty(ENABLE_KEY));
            enabled = new SimpleBooleanProperty(initValue);
        }
        return enabled;
    }
    
    /**
     *
     */
    public static final String TAU_KEY = "selectability.switch.tau";
    private DoubleProperty tau;

    /**
     *
     * @param value
     */
    public void setTau(Double value) {
        tauProperty().setValue(value);
        getPreferences().put(TAU_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Double getTau() {
        return tauProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty tauProperty() {
        if (tau == null) {
            Double initValue = Double.valueOf(getPreference(TAU_KEY));
            tau = new SimpleDoubleProperty(initValue);
        }
        return tau;
    }

    /**
     *
     */
    public static final String THRESHOLD_KEY = "selectability.switch.threshold";
    private DoubleProperty threshold;

    /**
     *
     * @param value
     */
    public void setThreshold(Double value) {
        thresholdProperty().setValue(value);
        getPreferences().put(THRESHOLD_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Double getThreshold() {
        return thresholdProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty thresholdProperty() {
        if (threshold == null) {
            Double initValue = Double.valueOf(getPreference(THRESHOLD_KEY));
            threshold = new SimpleDoubleProperty(initValue);
        }
        return threshold;
    }

    /**
     *
     */
    public static final String HYSTERESIS_KEY = "selectability.switch.hysteresis";
    private DoubleProperty hysteresis;

    /**
     *
     * @param value
     */
    public void setHysteresis(Double value) {
        hysteresisProperty().setValue(value);
        getPreferences().put(HYSTERESIS_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Double getHysteresis() {
        return hysteresisProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty hysteresisProperty() {
        if (hysteresis == null) {
            Double initValue = Double.valueOf(getPreference(HYSTERESIS_KEY));
            hysteresis = new SimpleDoubleProperty(initValue);
        }
        return hysteresis;
    }
    
    /**
     *
     */
    public static final String ARC_SIZE_KEY = "selectability.switch.arc";
    private DoubleProperty arcSize;

    /**
     *
     * @param value
     */
    public void setArcSize(Double value) {
        arcSensitivityProperty().setValue(value);
        getPreferences().put(ARC_SIZE_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Double getArcSize() {
        return arcSensitivityProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty arcSensitivityProperty() {
        if (arcSize == null) {
            Double initValue = Double.valueOf(getPreference(ARC_SIZE_KEY));
            arcSize = new SimpleDoubleProperty(initValue);
        }
        return arcSize;
    }
    
}
