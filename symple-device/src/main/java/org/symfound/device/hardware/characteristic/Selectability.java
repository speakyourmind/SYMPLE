/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.hardware.characteristic;

import java.util.Properties;
import java.util.prefs.Preferences;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.symfound.builder.characteristic.Characteristic;

/**
 *
 * @author Javed Gangjee
 */
public class Selectability extends Characteristic {

    /**
     *
     * @param preferences
     * @param properties
     */
    public Selectability(Preferences preferences, Properties properties) {
        super(preferences, properties);
    }

    /**
     *
     * @return
     */
    public Boolean canSelect() {
        return getDwellability().isEnabled() || getClickability().isEnabled() || getSwitchability().isEnabled();
    }

    private static final String ZOOM_SCALE_KEY = "selectability.zoom.scale";
    private DoubleProperty zoomScale;

    /**
     *
     * @param value
     */
    public void setZoomScale(Double value) {
        zoomScaleProperty().setValue(value);
        getPreferences().put(ZOOM_SCALE_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Double getZoomScale() {
        return zoomScaleProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty zoomScaleProperty() {
        if (zoomScale == null) {
            Double initValue = Double.valueOf(getPreference(ZOOM_SCALE_KEY));
            zoomScale = new SimpleDoubleProperty(initValue);
        }
        return zoomScale;
    }

    private static final String ZOOM_SIZE_KEY = "selectability.zoom.size";
    private IntegerProperty zoomSize;

    /**
     *
     * @param value
     */
    public void setZoomSize(Integer value) {
        zoomSizeProperty().setValue(value);
        getPreferences().put(ZOOM_SIZE_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Integer getZoomSize() {
        return zoomSizeProperty().getValue();
    }

    /**
     *
     * @return
     */
    public IntegerProperty zoomSizeProperty() {
        if (zoomSize == null) {
            Integer initValue = Integer.parseInt(getPreference(ZOOM_SIZE_KEY));
            zoomSize = new SimpleIntegerProperty(initValue);
        }
        return zoomSize;
    }

    /**
     *
     */
    public static final String SELECTION_SENSITIVITY_KEY = "selectability.dwell.sensitivity";
    private IntegerProperty sensitivity;

    /**
     *
     * @param value
     */
    public void setSensitivity(Integer value) {
        sensitivityProperty().setValue(value);
        getPreferences().put(SELECTION_SENSITIVITY_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Integer getSensitivity() {
        return sensitivityProperty().getValue();
    }

    /**
     *
     * @return
     */
    public IntegerProperty sensitivityProperty() {
        if (sensitivity == null) {
            Integer initValue = Integer.parseInt(getDefaultConfiguration().getProperty(SELECTION_SENSITIVITY_KEY));
            sensitivity = new SimpleIntegerProperty(initValue);
        }
        return sensitivity;
    }

   

    private static final String POST_SELECT_TIME_KEY = "selectability.postSelectionTime";
    private DoubleProperty postSelectTime;

    /**
     * Set the amount of time the user dwells or hovers on an item before it
     * triggers a selection.
     *
     * @param value
     */
    public void setPostSelectTime(Double value) {
        postSelectTimeProperty().setValue(value);
        getPreferences().put(POST_SELECT_TIME_KEY, value.toString());
    }

    /**
     * Gets the amount of time the user needs to dwell or hover on an item
     * before it is selected.
     *
     * @return dwell time
     */
    public Double getPostSelectTime() {
        return postSelectTimeProperty().getValue();
    }

    /**
     * Defines the amount of time the user needs to dwell or hover on an item
     * before it is selected.
     *
     * @return
     */
    public DoubleProperty postSelectTimeProperty() {
        if (postSelectTime == null) {
            Double initValue = Double.valueOf(getPreference(POST_SELECT_TIME_KEY));
            postSelectTime = new SimpleDoubleProperty(initValue);
        }
        return postSelectTime;
    }

    private Clickability clickability;

    /**
     *
     * @return
     */
    public Clickability getClickability() {
        if (clickability == null) {
            clickability = new Clickability(getPreferences(), getDefaultConfiguration());
        }
        return clickability;
    }

    private Dwellability dwellability;

    /**
     *
     * @return
     */
    public Dwellability getDwellability() {
        if (dwellability == null) {
            dwellability = new Dwellability(getPreferences(), getDefaultConfiguration());
        }
        return dwellability;
    }
    private Switchability switchability;

    /**
     *
     * @return
     */
    public Switchability getSwitchability() {
        if (switchability == null) {
            switchability = new Switchability(getPreferences(), getDefaultConfiguration());
        }
        return switchability;
    }
}
