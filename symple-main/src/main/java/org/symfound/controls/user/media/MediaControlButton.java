/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media;

import org.symfound.media.MediaControl;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import org.symfound.builder.user.characteristic.Interaction;
import org.symfound.controls.AppableControl;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.ConfigurableGrid;
import org.symfound.tools.timing.DelayedEvent;

/**
 *
 * @author Administrator
 * @param <T>
 */
public abstract class MediaControlButton<T extends MediaControl> extends AppableControl {

    /**
     *
     * @param CSSClass
     * @param key
     * @param title
     * @param index
     */
    public MediaControlButton(String CSSClass, String key, String title, String index) {
        super(CSSClass, key, title, index);
        initialize();
    }

    private void initialize() {
        configureAutoStyling();
    }

    /**
     *
     */
    public void configureAutoStyling() {
        style();
        overrideStyleProperty().addListener((obversable, oldValue, newValue) -> {
            style();
        });
        controlProperty().addListener((observeableValue, oldValue, newValue) -> {
            style();
        });

       
    }

    /**
     *
     */
    public void style() {
        if (!getOverrideStyle().isEmpty()) {
            getPrimaryControl().setStyle(getOverrideStyle());
        } else {
            getPrimaryControl().setStyle("");
            setCSS("transparent-" + getControl().toString().toLowerCase(), getPrimaryControl());
        }
    }
    
    
    //TO DO: Bug- See AppableControl. 

    /**
     *
     */
     @Override
    public void configButtons() {
        boolean isSettingsControl = getControlType().equals(ControlType.SETTING_CONTROL);
        
        ConfigurableGrid.editModeProperty().addListener((observable1, oldValue1, newValue1) -> {
            if (newValue1 && !isSettingsControl && isEditable()) {
                addConfigButtons();
            } else {
                removeConfigButtons();
            }
        });
    }

    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton("") {

            @Override
            public DelayedEvent setOnFinished(String originalText, String originalStyle) {
                getSession().setMutex(true);
                Interaction interaction = getUser().getInteraction();
                DelayedEvent delayedEvent = new DelayedEvent();
                delayedEvent.setup(interaction.getSelectionTime(), (ActionEvent u) -> {
                    revert(originalText, originalStyle);
                });
                delayedEvent.play();
                return delayedEvent;
            }

            @Override
            public void revert(String originalText, String originalStyle) {
                //Original Size X
                setScaleX(1);
                //Original Size Y
                setScaleY(1);
                if (originalText != null) {
                    // to set title
                }
                if (originalStyle != null) {
                    //Original Style
                    setStyle(originalStyle);
                } else {
                    setStyle("");
                }
                getSession().setMutex(false);
            }

        };
        primary.setWrapText(true);
        load(primary);
        setSelection(primary);
    }
    private static final String DEFAULT_CONTROL = "NEXT";
    private ObjectProperty<T> control;

    /**
     *
     * @param value
     */
    public void setControl(T value) {
        controlProperty().setValue(value);
        getPreferences().put("control", value.toString());
    }

    /**
     *
     * @return
     */
    public T getControl() {
        return controlProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<T> controlProperty() {
        if (control == null) {
            control = new SimpleObjectProperty(getDefaultControlValue());
        }
        return control;
    }

    /**
     *
     * @return
     */
    public abstract T getDefaultControlValue();

}
