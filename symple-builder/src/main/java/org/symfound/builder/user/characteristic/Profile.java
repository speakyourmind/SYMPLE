/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.user.characteristic;

import java.util.Properties;
import java.util.prefs.Preferences;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.symfound.builder.characteristic.Characteristic;

/**
 *
 * @author Javed Gangjee
 */
public class Profile extends Characteristic {

    /**
     *
     * @param preferences
     * @param properties
     */
    public Profile(Preferences preferences, Properties properties) {
        super(preferences, properties);
    }

    /**
     *
     * @return
     */
    public String getFullName() {
        final String userLastName = getLastName();
        final String userFirstName = getFirstName();
        final String name = userFirstName + " " + userLastName;
        return name;
    }

    private BooleanProperty firstUse;

    /**
     *
     * @param value
     */
    public void setFirstUse(Boolean value) {
        firstUseProperty().setValue(value);
        getPreferences().put("profile.firstUse", value.toString());
    }

    /**
     * Returns if the user is using the program for the first time.
     *
     * @return true if first use, false otherwise
     */
    public Boolean isFirstUse() {
        return firstUseProperty().getValue();
    }

    /**
     * Represents if this user is using the program for the first time. Used to
     * determine if the wizard needs to be launched.
     *
     * @return first use
     */
    public BooleanProperty firstUseProperty() {
        if (firstUse == null) {
            Boolean initValue = Boolean.valueOf(getDefaultConfiguration().getProperty("profile.firstUse"));
            firstUse = new SimpleBooleanProperty(initValue);
        }
        return firstUse;
    }


    private static final String USERNAME_KEY = "profile.username";
    private StringProperty username;

    /**
     * Gets the username of this user.
     *
     * @return username
     */
    public String getUsername() {
        return usernameProperty().getValue();
    }

    /**
     * Represents the username of this user.
     * <b>NOTE:</b> This will eventually be tied into a user management system.
     * Currently only one default user exists.
     *
     * @return username
     */
    public StringProperty usernameProperty() {
        if (username == null) {
            String initValue = getPreference(USERNAME_KEY);
            username = new SimpleStringProperty(initValue);
        }
        return username;
    }

    private static final String FIRST_NAME_KEY = "profile.firstName";
    private StringProperty firstName;

    /**
     *
     * @param value
     */
    public void setFirstName(String value) {
        firstNameProperty().setValue(value);
        getPreferences().put(FIRST_NAME_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getFirstName() {
        return firstNameProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty firstNameProperty() {
        if (firstName == null) {
            String initValue = getPreference(FIRST_NAME_KEY);
            firstName = new SimpleStringProperty(initValue);
        }
        return firstName;
    }

    private static final String LAST_NAME_KEY = "profile.lastName";
    private StringProperty lastName;

    /**
     *
     * @param value
     */
    public void setLastName(String value) {
        lastNameProperty().setValue(value);
        getPreferences().put(LAST_NAME_KEY, value);

    }

    /**
     *
     * @return
     */
    public String getLastName() {
        return lastNameProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty lastNameProperty() {
        if (lastName == null) {
            String initValue = getPreference(LAST_NAME_KEY);
            lastName = new SimpleStringProperty(initValue);
        }
        return lastName;
    }

    
    private static final String USER_ID_KEY = "profile.id";
    private StringProperty userID;

    /**
     *
     * @param value
     */
    public void setUserID(String value) {
        userIDProperty().setValue(value);
        getPreferences().put(USER_ID_KEY, value);

    }

    /**
     *
     * @return
     */
    public String getUserID() {
        return userIDProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty userIDProperty() {
        if (userID == null) {
            String initValue = getPreference(USER_ID_KEY);
            userID = new SimpleStringProperty(initValue);
        }
        return userID;
    }

     private static final String AUTO_UPDATE_KEY = "profile.autoUpdate";
    private BooleanProperty autoUpdate;

    /**
     *
     * @param value
     */
    public void setAutoUpdate(Boolean value) {
        autoUpdateProperty().setValue(value);
        getPreferences().put(AUTO_UPDATE_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean autoUpdate() {
        return autoUpdateProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty autoUpdateProperty() {
        if (autoUpdate == null) {
            Boolean initValue = Boolean.valueOf(getPreference(AUTO_UPDATE_KEY));
            autoUpdate = new SimpleBooleanProperty(initValue);
        }
        return autoUpdate;
    }
    
}
