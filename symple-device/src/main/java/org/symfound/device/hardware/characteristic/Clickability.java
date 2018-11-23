/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.hardware.characteristic;

import java.util.Properties;
import java.util.prefs.Preferences;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.symfound.builder.characteristic.Characteristic;
import org.symfound.tools.selection.SelectionEventType;

/**
 *
 * @author Javed Gangjee
 */
public class Clickability extends Characteristic {

    /**
     *
     * @param preferences
     * @param properties
     */
    public Clickability(Preferences preferences, Properties properties) {
        super(preferences, properties);
    }

    /**
     *
     */
    public static final String ENABLED_KEY = "selectability.click";
    private BooleanProperty enabled;

    /**
     *
     * @param value
     */
    public void setEnabled(Boolean value) {
        enabledProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isEnabled() {
        return enabledProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty enabledProperty() {
        if (enabled == null) {
            Boolean initValue = Boolean.parseBoolean(getDefaultConfiguration().getProperty(ENABLED_KEY));
            enabled = new SimpleBooleanProperty(initValue);
        }
        return enabled;
    }

    private static final String SELECTION_EVENT_KEY = "selectability.click.event";
    private ObjectProperty<SelectionEventType> eventType;

    /**
     * Sets the user's selection preference
     *
     * @param value
     */
    public void setEventType(SelectionEventType value) {
        eventTypeProperty().setValue(value);
        getPreferences().put(SELECTION_EVENT_KEY, value.getValue());

    }

    /**
     * Get how the user wants to interact with the program
     *
     * @return selection mode
     */
    public SelectionEventType getEventType() {
        return eventTypeProperty().getValue();
    }

    /**
     * Defines the way in which the user interacts with the program, that is,
     * the selection mode.
     *
     * @return selection
     */
    public ObjectProperty<SelectionEventType> eventTypeProperty() {
        if (eventType == null) {
            SelectionEventType initValue = new SelectionEventType(getPreference(SELECTION_EVENT_KEY));
            eventType = new SimpleObjectProperty<>(initValue);
        }
        return eventType;
    }

}
