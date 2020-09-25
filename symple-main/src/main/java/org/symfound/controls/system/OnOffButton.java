package org.symfound.controls.system;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import org.symfound.controls.RunnableControl;
import org.symfound.controls.user.AnimatedButton;

/**
 *
 * @author Javed Gangjee
 */
public class OnOffButton extends RunnableControl {

    /**
     *
     */
    public static final String OFF_VALUE = "OFF";
    public final String offValue;

    /**
     *
     */
    public static final String ON_VALUE = "ON";
    public final String onValue;

    /**
     *
     * @param onValue
     * @param offValue
     */
    public OnOffButton(String onValue, String offValue) {
        super("settings-text-area");
        this.onValue = onValue;
        this.offValue = offValue;
        initialize();
    }

    public OnOffButton() {
        this(ON_VALUE, OFF_VALUE);
    }

    private void initialize() {
        primary = new AnimatedButton("");
        
        load(primary);
        setCSS("settings-text-area", primary);

        getPrimaryControl().setOnMousePressed((MouseEvent e) -> {
            run();
        });
        setControlType(ControlType.SETTING_CONTROL);
        toggleButton(getValue(), onValue, offValue);
        valueProperty().addListener((observable, oldValue, newValue) -> {
            toggleButton(newValue, onValue, offValue);
        });
    }

    /**
     *
     * @param newValue
     */
    public void toggleButton(Boolean newValue) {
        toggleButton(newValue, ON_VALUE, OFF_VALUE);
    }

    /**
     *
     * @param newValue
     * @param onValue
     * @param offValue
     */
    public void toggleButton(Boolean newValue, String onValue, String offValue) {
        if (newValue) {
            getPrimaryControl().setText(onValue);
          getPrimaryControl().getStyleClass().remove("settings-text-area");
           setCSS("settings-button",getPrimaryControl());
          //  getPrimaryControl().setStyle("-fx-background-color:-fx-blue; -fx-background-insets: 0,0; -fx-text-fill: -fx-light; -fx-font-weight:bold; ");
        } else {
            getPrimaryControl().setText(offValue);
          //  getPrimaryControl().setStyle("");
          getPrimaryControl().getStyleClass().remove("settings-button");
           setCSS("settings-text-area",getPrimaryControl());
           
        }
    }

    private BooleanProperty value;

    /**
     *
     */
    public void toggleValue() {
        setValue(!getValue());
    }

    /**
     *
     * @param value
     */
    public void setValue(Boolean value) {
        valueProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean getValue() {
        return valueProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty valueProperty() {
        if (value == null) {
            value = new SimpleBooleanProperty(Boolean.FALSE);
        }
        return value;
    }

    @Override
    public void run() {
        toggleValue();
    }

}
