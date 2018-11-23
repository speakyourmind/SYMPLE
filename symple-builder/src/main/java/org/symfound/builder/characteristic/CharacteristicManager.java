/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.characteristic;

import java.util.Properties;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;

/**
 *
 * @author Javed Gangjee
 */
public abstract class CharacteristicManager {

    private static final String NAME = CharacteristicManager.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public Preferences preferences;

    /**
     *
     */
    public Properties properties;

    /**
     *
     * @return
     */
    public abstract Preferences getPreferences();

    /**
     *
     * @return
     */
    public abstract Properties getDefaultConfiguration();

    /**
     *
     * @param key
     * @return
     */
    public String getPreference(String key) {
        return getPreferences().get(key, getDefaultConfiguration().getProperty(key));
    }

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getPreference(String key, String defaultValue) {
        return getPreferences().get(key, getDefaultConfiguration().getProperty(key, defaultValue));
    }

}
