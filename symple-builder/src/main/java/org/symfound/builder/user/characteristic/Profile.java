/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.user.characteristic;

import java.util.Properties;
import java.util.prefs.Preferences;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
        final String lastName = getLastName();
        final String firstName = getFirstName();
        final String name = firstName + " " + lastName;
        return name;
    }

    private BooleanProperty firstUse;

    /**
     *
     * @param value
     */
    public void setFirstUse(Boolean value) {
        firstUseProperty().setValue(value);
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
            Boolean initValue = Boolean.valueOf(getDefaultConfiguration().getProperty("firstUse"));
            firstUse = new SimpleBooleanProperty(initValue);
        }
        return firstUse;
    }

    private static final String CITY_KEY = "profile.city";
    private StringProperty city;

    /**
     *
     * @param value
     */
    public void setCity(String value) {
        cityProperty().setValue(value);
        getPreferences().put(CITY_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getCity() {
        return cityProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty cityProperty() {
        if (city == null) {
            String initValue = getPreference(CITY_KEY);
            city = new SimpleStringProperty(initValue);
        }
        return city;
    }

    private static final String REGION_KEY = "profile.region";
    private StringProperty region;

    /**
     *
     * @param value
     */
    public void setRegion(String value) {
        regionProperty().setValue(value);
        getPreferences().put(REGION_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getRegion() {
        return regionProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty regionProperty() {
        if (region == null) {
            String initValue = getPreference(REGION_KEY);
            region = new SimpleStringProperty(initValue);
        }
        return region;
    }

    private static final String COUNTRY_KEY = "profile.country";
    private StringProperty country;

    /**
     *
     * @param value
     */
    public void setCountry(String value) {
        countryProperty().setValue(value);
        getPreferences().put(COUNTRY_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getCountry() {
        return countryProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty countryProperty() {
        if (country == null) {
            String initValue = getPreference(COUNTRY_KEY);
            country = new SimpleStringProperty(initValue);
        }
        return country;
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

    /**
     *
     */
    public static final String GENDER_MALE = "Male";

    /**
     *
     */
    public static final String GENDER_FEMALE = "Female";

    /**
     *
     */
    public static final String GENDER_NEUTRAL = "Neutral";
    private static final String GENDER_KEY = "profile.gender";
    private StringProperty gender;

    /**
     *
     * @param value
     */
    public void setGender(String value) {
        genderProperty().setValue(value);
        getPreferences().put(GENDER_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getGender() {
        return genderProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty genderProperty() {
        if (gender == null) {
            String initValue = getPreference(GENDER_NEUTRAL);
            gender = new SimpleStringProperty(initValue);
        }
        return gender;
    }

}
