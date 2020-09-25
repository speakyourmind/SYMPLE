/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.hardware.swifty;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.symfound.builder.user.feature.Feature;
import org.symfound.builder.user.feature.Unknown;
import org.symfound.device.hardware.Hardware;

/**
 *
 * @author Javed Gangjee
 */
public class Swifty extends Hardware<Feature> {

    /**
     *
     */
    public Swifty() {
        super(SWIFTY, new Unknown());
    }

    /**
     *
     * @return
     */
    @Override
    public Boolean connect() {
        return true;
    }

    /**
     *
     */
    @Override
    public void launch() {

    }

    /**
     *
     */
    @Override
    public void record() {
    }

    /**
     *
     */
    @Override
    public void bundle() {
    }

    /**
     *
     */
    @Override
    public void close() {

    }

    private static final String DIP_KEY = "custom.dip";
    private StringProperty dip;

    /**
     *
     * @param value
     */
    public void setDIP(String value) {
        dipProperty().setValue(value);
        getPreferences().put(DIP_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getDIP() {
        return dipProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty dipProperty() {
        if (dip == null) {
            String initValue = getPreference(DIP_KEY);
            dip = new SimpleStringProperty(initValue);
        }
        return dip;
    }

}
