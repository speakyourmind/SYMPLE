/*
 * Copyright (C) 2015 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.symfound.builder.user;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;
import org.symfound.builder.characteristic.CharacteristicManager;
import org.symfound.builder.characteristic.PreferencesManager;
import org.symfound.builder.user.characteristic.Ability;
import org.symfound.builder.user.characteristic.Content;
import org.symfound.builder.user.characteristic.Interaction;
import org.symfound.builder.user.characteristic.Navigation;
import org.symfound.builder.user.characteristic.Physical;
import org.symfound.builder.user.characteristic.Profile;
import org.symfound.builder.user.characteristic.Social;
import org.symfound.builder.user.characteristic.Speech;
import org.symfound.builder.user.characteristic.Timing;
import org.symfound.builder.user.characteristic.Typing;

/**
 * Defines the properties of the user that is interacting with the program.
 *
 * @author Javed Gangjee
 */
public class User extends CharacteristicManager {

    private static final String NAME = User.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     * @return
     */
    @Override
    public final Preferences getPreferences() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(this.getClass());
        }
        return preferences;
    }

    /**
     *
     * @return
     */
    @Override
    public final Properties getDefaultConfiguration() {
        if (properties == null) {
            String resourceName = "user.properties";
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream resourceStream = loader.getResourceAsStream(resourceName);
            properties = new Properties();
            try {
                properties.load(resourceStream);
            } catch (IOException ex) {
                LOGGER.fatal(null, ex);
            }
        }

        return properties;
    }

    private Profile profile;

    /**
     *
     * @return
     */
    public Profile getProfile() {
        if (profile == null) {
            profile = new Profile(getPreferences(), getDefaultConfiguration());
        }
        return profile;
    }

    private Physical physical;

    /**
     *
     * @return
     */
    public Physical getPhysical() {
        if (physical == null) {
            physical = new Physical(getPreferences(), getDefaultConfiguration());
        }
        return physical;
    }

    private Navigation navigation;

    /**
     *
     * @return
     */
    public Navigation getNavigation() {
        if (navigation == null) {
            navigation = new Navigation(getPreferences(), getDefaultConfiguration());
        }
        return navigation;
    }

    private Speech speech;

    /**
     *
     * @return
     */
    public Speech getSpeech() {
        if (speech == null) {
            speech = new Speech(getPreferences(), getDefaultConfiguration());
        }
        return speech;
    }
    private Typing typing;

    /**
     *
     * @return
     */
    public Typing getTyping() {
        if (typing == null) {
            typing = new Typing(getPreferences(), getDefaultConfiguration());
        }
        return typing;
    }

    private Social social;

    /**
     *
     * @return
     */
    public Social getSocial() {
        if (social == null) {
            social = new Social(getPreferences(), getDefaultConfiguration());
        }
        return social;
    }

    private Timing timing;

    /**
     *
     * @return
     */
    public Timing getTiming() {
        if (timing == null) {
            timing = new Timing(getPreferences(), getDefaultConfiguration());
        }
        return timing;
    }

    private Interaction interaction;

    /**
     *
     * @return
     */
    public Interaction getInteraction() {
        if (interaction == null) {
            interaction = new Interaction(getPreferences(), getDefaultConfiguration());
        }
        return interaction;
    }
    private Content content;

    /**
     *
     * @return
     */
    public Content getContent() {
        if (content == null) {
            content = new Content(getPreferences(), getDefaultConfiguration());
        }
        return content;
    }

    private Ability ability;

    /**
     *
     * @return
     */
    public Ability getAbility() {
        if (ability == null) {
            ability = new Ability(getPreferences(), getDefaultConfiguration());
        }
        return ability;
    }

    private StringProperty deviceName;

    /**
     * Sets the device being used by this user.
     *
     * @param value device name
     */
    public void setDeviceName(String value) {
        deviceNameProperty().setValue(value);
        LOGGER.info("Current device is being set to: " + value);
        getPreferences().put("device.name", value);
    }

    /**
     * Gets the device that is set to this user.
     *
     * @return device name
     */
    public String getDeviceName() {
        return deviceNameProperty().getValue();
    }

    /**
     * Represents the device being used by this user.
     *
     * @return device
     */
    //TO DO : Make Device abstract and turn this into a wildcard
    public StringProperty deviceNameProperty() {
        if (deviceName == null) {
            deviceName = new SimpleStringProperty(getPreference("device.name"));
        }
        return deviceName;
    }

    /**
     *
     * @param folder
     */
    public void exportPreferences(String folder) {
        LOGGER.debug("Exporting user settings. Selected folder: " + folder);
        try {
            final String fileName = "\\" + getProfile().getFullName() + " Settings.xml";
            LOGGER.debug("Exporting user settings. File name: " + fileName);
            final String destination = folder + fileName;
            PreferencesManager.exportTo(destination, getPreferences());
            LOGGER.info("Exporting user settings to " + destination);
        } catch (BackingStoreException | IOException ex) {
            LOGGER.fatal("Unable to export user settings");
        }

    }

}
