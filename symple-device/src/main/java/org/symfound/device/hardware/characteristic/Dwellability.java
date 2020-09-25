/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.hardware.characteristic;

import java.util.Properties;
import java.util.prefs.Preferences;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.symfound.builder.characteristic.Characteristic;

/**
 *
 * @author Javed Gangjee
 */
public class Dwellability extends Characteristic {

    /**
     *
     * @param preferences
     * @param properties
     */
    public Dwellability(Preferences preferences, Properties properties) {
        super(preferences, properties);
    }

    /**
     *
     */
    public static final String DWELLABLE_KEY = "selectability.dwell";
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
            Boolean initValue = Boolean.parseBoolean(getDefaultConfiguration().getProperty(DWELLABLE_KEY));
            enabled = new SimpleBooleanProperty(initValue);
        }
        return enabled;
    }

}
