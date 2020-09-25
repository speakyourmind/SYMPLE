/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.hardware.characteristic;

import java.util.Properties;
import java.util.prefs.Preferences;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.symfound.builder.characteristic.Characteristic;

/**
 *
 * @author Javed Gangjee
 */
public class HardwareInfo extends Characteristic {

    /**
     *
     * @param preferences
     * @param properties
     */
    public HardwareInfo(Preferences preferences, Properties properties) {
        super(preferences, properties);
    }

    private static final String URL_KEY = "info.url";
    private StringProperty url;

    /**
     *
     * @param value
     */
    public void setURL(String value) {
        urlProperty().setValue(value);
        getPreferences().put(URL_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getURL() {
        return urlProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty urlProperty() {
        if (url == null) {
            String initValue = getPreference(URL_KEY);
            url = new SimpleStringProperty(initValue);
        }
        return url;
    }

    private static final String DESCRIPTION_KEY = "info.description";
    private StringProperty description;

    /**
     *
     * @param value
     */
    public void setDescription(String value) {
        descriptionProperty().setValue(value);
        getPreferences().put(DESCRIPTION_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return descriptionProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty descriptionProperty() {
        if (description == null) {
            String initValue = getPreference(DESCRIPTION_KEY);
            description = new SimpleStringProperty(initValue);
        }
        return description;
    }

    private static final String MANUFACTURER_KEY = "info.manufacturer";
    private StringProperty manufacturer;

    /**
     *
     * @param value
     */
    public void setManufacturer(String value) {
        manufacturerProperty().setValue(value);
        getPreferences().put(MANUFACTURER_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getManufacturer() {
        return manufacturerProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty manufacturerProperty() {
        if (manufacturer == null) {
            String initValue = getPreference(MANUFACTURER_KEY);
            manufacturer = new SimpleStringProperty(initValue);
        }
        return manufacturer;
    }

}
