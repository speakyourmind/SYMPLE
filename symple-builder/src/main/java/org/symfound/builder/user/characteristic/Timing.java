/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.user.characteristic;

import java.util.Properties;
import java.util.prefs.Preferences;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.symfound.builder.characteristic.Characteristic;

/**
 *
 * @author Javed Gangjee
 */
public class Timing extends Characteristic {

    /**
     *
     * @param preferences
     * @param properties
     */
    public Timing(Preferences preferences, Properties properties) {
        super(preferences, properties);
    }

    private static final String DWELL_TIME_KEY = "app.selection.dwellTime";
    private DoubleProperty dwellTime;

    /**
     * Set the amount of time the user dwells or hovers on an item before it
     * triggers a selection.
     *
     * @param value
     */
    public void setDwellTime(Double value) {
        dwellTimeProperty().setValue(value);
        getPreferences().put(DWELL_TIME_KEY, value.toString());
    }

    /**
     * Gets the amount of time the user needs to dwell or hover on an item
     * before it is selected.
     *
     * @return dwell time
     */
    public Double getDwellTime() {
        return dwellTimeProperty().getValue();
    }

    /**
     * Defines the amount of time the user needs to dwell or hover on an item
     * before it is selected.
     *
     * @return
     */
    public DoubleProperty dwellTimeProperty() {
        if (dwellTime == null) {
            Double initValue = Double.valueOf(getPreference(DWELL_TIME_KEY));
            dwellTime = new SimpleDoubleProperty(initValue);
        }
        return dwellTime;
    }

    private static final String SCROLL_TIME_KEY = "app.selection.scrollTime";
    private DoubleProperty scrollTime;

    /**
     *
     * @param value
     */
    public void setScrollTime(Double value) {
        scrollTimeProperty().setValue(value);
        getPreferences().put(SCROLL_TIME_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Double getScrollTime() {
        return scrollTimeProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty scrollTimeProperty() {
        if (scrollTime == null) {
            Double initValue = Double.valueOf(getPreference(SCROLL_TIME_KEY));
            scrollTime = new SimpleDoubleProperty(initValue);
        }
        return scrollTime;
    }
    
    private static final String SCAN_TIME_KEY = "app.selection.scanTime";
    private DoubleProperty scanTime;

    /**
     *
     * @param value
     */
    public void setScanTime(Double value) {
        scanTimeProperty().setValue(value);
        getPreferences().put(SCAN_TIME_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Double getScanTime() {
        return scanTimeProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty scanTimeProperty() {
        if (scanTime == null) {
            Double initValue = Double.valueOf(getPreference(SCAN_TIME_KEY));
            scanTime = new SimpleDoubleProperty(initValue);
        }
        return scanTime;
    }

    private static final String STEP_TIME_KEY = "app.selection.stepTime";
    private DoubleProperty stepTime;

    /**
     *
     * @param value
     */
    public void setStepTime(Double value) {
        stepTimeProperty().setValue(value);
        getPreferences().put(STEP_TIME_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Double getStepTime() {
        return stepTimeProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty stepTimeProperty() {
        if (stepTime == null) {
            Double initValue = Double.valueOf(getPreference(STEP_TIME_KEY));
            stepTime = new SimpleDoubleProperty(initValue);
        }
        return stepTime;
    }

}
