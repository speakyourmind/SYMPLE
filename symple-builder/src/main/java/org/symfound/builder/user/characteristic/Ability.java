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
public class Ability extends Characteristic {

    /**
     *
     * @param preferences
     * @param properties
     */
    public Ability(Preferences preferences, Properties properties) {
        super(preferences, properties);
    }
    // Level

    /**
     *
     */
    public static final String LEVEL_KEY = "app.navigation.level";

    /**
     *
     */
    public static final Double MIN_LEVEL = 1.0;

    /**
     *
     */
    public static final Double DEFAULT_LEVEL = 3.0;

    /**
     *
     */
    public static final Double MAX_LEVEL = 30.0;
    private DoubleProperty level;

    /**
     * Sets the ability level of the user to the specified value
     *
     * @param value ability level
     */
    public void setLevel(Double value) {
        levelProperty().setValue(value);
        getPreferences().put(LEVEL_KEY, value.toString());
    }

    /**
     * Increases the defined user ability by 1
     */
    public void incrementLevel() {
        setLevel(getLevel() + 1);
    }

    /**
     * Reduces the defined user ability by 1.
     */
    public void decrementLevel() {
        setLevel(getLevel() - 1);
    }

    /**
     * Sets the ability level to the lowest possible level defined in
     * <code>LOW_LEVEL</code>. By default, that is a single button.
     *
     */
    public void resetLevel() {
        setLevel(MIN_LEVEL);
    }

    /**
     * Gets the ability level of the user.
     *
     * @return level
     */
    public Double getLevel() {
        return levelProperty().getValue();
    }

    /**
     * Defines the ability of the user.
     * <p>
     * Programmatically, this is the maximum number of buttons the user can
     * select in each dimension. For example, If the user ability is determined
     * to be 4, it is implied that they can consistently use a 4x4 UI.
     *
     * @return ability level
     */
    public DoubleProperty levelProperty() {
        if (level == null) {
            Double initValue = Double.valueOf(getPreference(LEVEL_KEY));
            level = new SimpleDoubleProperty(initValue);
        }
        return level;
    }


}
