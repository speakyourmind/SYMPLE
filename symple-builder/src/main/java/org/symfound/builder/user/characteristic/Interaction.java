/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.user.characteristic;

import org.symfound.builder.user.selection.SelectionMethod;
import java.util.Properties;
import java.util.prefs.Preferences;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.symfound.builder.characteristic.Characteristic;

/**
 *
 * @author Javed Gangjee
 */
public class Interaction extends Characteristic {

    /**
     *
     * @param preferences
     * @param properties
     */
    public Interaction(Preferences preferences, Properties properties) {
        super(preferences, properties);
    }

    private static final String SELECTION_TYPE_KEY = "app.selection.type";
    private ObjectProperty<SelectionMethod> selectionMethod;

    /**
     * Sets the user's selection preference
     *
     * @param value
     */
    public void setSelectionMethod(SelectionMethod value) {
        selectionMethodProperty().setValue(value);
        getPreferences().put(SELECTION_TYPE_KEY, value.toString());

    }

    /**
     * Get how the user wants to interact with the program
     *
     * @return selection mode
     */
    public SelectionMethod getSelectionMethod() {
        return selectionMethodProperty().getValue();
    }

    /**
     * Defines the way in which the user interacts with the program, that is,
     * the selection mode.
     *
     * @return selection
     */
    public ObjectProperty<SelectionMethod> selectionMethodProperty() {
        if (selectionMethod == null) {
            selectionMethod = new SimpleObjectProperty<>(SelectionMethod.valueOf(getPreference(SELECTION_TYPE_KEY)));
        }
        return selectionMethod;
    }

    private static final String VOLUME_KEY = "app.media.volume";
    private DoubleProperty volume;

    /**
     *
     * @param value
     */
    public void setVolume(Double value) {
        volumeProperty().setValue(value);
        getPreferences().put(VOLUME_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Double getVolume() {
        return volumeProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty volumeProperty() {
        if (volume == null) {
            Double initValue = Double.valueOf(getPreference(VOLUME_KEY));
            volume = new SimpleDoubleProperty(initValue);
        }
        return volume;
    }

    private static final String SELECTION_TIME_KEY = "app.selection.selectionTime";
    private DoubleProperty selectionTime;

    /**
     *
     * @param value
     */
    public void setSelectionTime(Double value) {
        selectionTimeProperty().setValue(value);
        getPreferences().put(SELECTION_TIME_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Double getSelectionTime() {
        return selectionTimeProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty selectionTimeProperty() {
        if (selectionTime == null) {
            Double initValue = Double.valueOf(getPreference(SELECTION_TIME_KEY));
            selectionTime = new SimpleDoubleProperty(initValue);
        }
        return selectionTime;
    }

    private static final String SELECTION_COLOUR_KEY = "app.selection.selectionColour";
    private StringProperty selectionColour;

    /**
     *
     * @param value
     */
    public void setSelectionColour(String value) {
        // TO DO: Check validity
        selectionColourProperty().setValue(value);
        getPreferences().put(SELECTION_COLOUR_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getSelectionColour() {
        return selectionColourProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty selectionColourProperty() {
        if (selectionColour == null) {
            String initValue = getPreference(SELECTION_COLOUR_KEY);
            selectionColour = new SimpleStringProperty(initValue);
        }
        return selectionColour;
    }

    private static final String MOUSE_CONTROL_KEY = "device.movement.mouseControl";
    private BooleanProperty mouseControl;

    /**
     *
     * @param value
     */
    public void setMouseControl(Boolean value) {
        mouseControlProperty().setValue(value);
        getPreferences().put(MOUSE_CONTROL_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean needsMouseControl() {
        return mouseControlProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty mouseControlProperty() {
        if (mouseControl == null) {
            Boolean initValue = Boolean.valueOf(getPreference(MOUSE_CONTROL_KEY));
            mouseControl = new SimpleBooleanProperty(initValue);
        }
        return mouseControl;
    }

    private BooleanProperty selectionControl;

    /**
     *
     * @param value
     */
    public void setSelectionControl(Boolean value) {
        selectionControlProperty().setValue(value);
        getPreferences().put("device.movement.selectionControl", value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean needsSelectionControl() {
        return selectionControlProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty selectionControlProperty() {
        if (selectionControl == null) {
            Boolean initValue = Boolean.valueOf(getPreference("device.movement.selectionControl"));
            selectionControl = new SimpleBooleanProperty(initValue);
        }
        return selectionControl;
    }
    
     private static final String OVERRIDE_METHOD_KEY = "app.selection.override";
    private BooleanProperty overrideSelectionMethod;

    /**
     *
     * @param value
     */
    public void setOverrideSelectionMethod(Boolean value) {
        overrideSelectionMethodProperty().setValue(value);
        getPreferences().put(OVERRIDE_METHOD_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean overrideSelectionMethod() {
        return overrideSelectionMethodProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty overrideSelectionMethodProperty() {
        if (overrideSelectionMethod == null) {
            Boolean initValue = Boolean.valueOf(getPreference(OVERRIDE_METHOD_KEY));
            overrideSelectionMethod = new SimpleBooleanProperty(initValue);
        }
        return overrideSelectionMethod;
    }

}
