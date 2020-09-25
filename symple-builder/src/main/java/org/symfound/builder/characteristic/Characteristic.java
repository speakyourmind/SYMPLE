/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.characteristic;

import java.util.Properties;
import java.util.prefs.Preferences;

/**
 *
 * @author Javed Gangjee
 */
public class Characteristic extends CharacteristicManager {

    /**
     *
     * @param preferences
     * @param properties
     */
    public Characteristic(Preferences preferences, Properties properties) {
        this.preferences = preferences;
        this.properties = properties;
    }

    /**
     *
     * @return
     */
    @Override
    public Preferences getPreferences() {
        return preferences;
    }

    /**
     *
     * @return
     */
    @Override
    public Properties getDefaultConfiguration() {
        return properties;
    }

}
