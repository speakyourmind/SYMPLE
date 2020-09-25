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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.symfound.builder.characteristic.Characteristic;

/**
 *
 * @author Javed Gangjee
 */
public class Movability extends Characteristic {

    /**
     *
     * @param preferences
     * @param properties
     */
    public Movability(Preferences preferences, Properties properties) {
        super(preferences, properties);
    }

    /**
     *
     */
    public static final String ENABLED_KEY = "movability.enabled";
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
            Boolean initValue = Boolean.parseBoolean(getDefaultConfiguration().getProperty(ENABLED_KEY));
            enabled = new SimpleBooleanProperty(initValue);
        }
        return enabled;
    }

    /**
     *
     */
    public static final String FILTER_KEY = "movability.filter";
    private StringProperty filter;

    /**
     *
     * @param value
     */
    public void setFilter(String value) {
        filterProperty().setValue(value);
        getPreferences().put(FILTER_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getFilter() {
        return filterProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty filterProperty() {
        if (filter == null) {
            String initValue = getPreference(FILTER_KEY);
            filter = new SimpleStringProperty(initValue);
        }
        return filter;
    }

    /**
     *
     */
    public static final String SMOOTHING_FACTOR_KEY = "movability.smoothingFactor";
    private DoubleProperty smoothingFactor;

    /**
     *
     * @param value
     */
    public void setSmoothingFactor(Double value) {
        smoothingFactorProperty().setValue(value);
        getPreferences().put(SMOOTHING_FACTOR_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Double getSmoothingFactor() {
        return smoothingFactorProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty smoothingFactorProperty() {
        if (smoothingFactor == null) {
            Double initValue = Double.valueOf(getPreference(SMOOTHING_FACTOR_KEY));
            smoothingFactor = new SimpleDoubleProperty(initValue);
        }
        return smoothingFactor;
    }

    /**
     *
     */
    public static final String SAMPLE_SIZE_KEY = "movability.sampleSize";
    private IntegerProperty sampleSize;

    /**
     *
     * @param value
     */
    public void setSampleSize(Integer value) {
        sampleSizeProperty().setValue(value);
        getPreferences().put(SAMPLE_SIZE_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Integer getSampleSize() {
        return sampleSizeProperty().getValue();
    }

    /**
     *
     * @return
     */
    public IntegerProperty sampleSizeProperty() {
        if (sampleSize == null) {
            Integer initValue = Integer.parseInt(getPreference(SAMPLE_SIZE_KEY));
            sampleSize = new SimpleIntegerProperty(initValue);
        }
        return sampleSize;
    }

}
