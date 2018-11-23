/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.awt.MouseInfo;
import java.awt.Point;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.stage.Window;
import org.symfound.controls.RunnableControl;
import org.symfound.device.Device;
import org.symfound.device.emulation.input.mouse.MouseEmulator;

/**
 *
 * @author Javed Gangjee
 */
public final class MoveWindowButton extends RunnableControl {

    /**
     *
     */
    public MoveWindowButton() {
        super("move");
        this.setControlType(ControlType.SETTING_CONTROL);
        initialize();
    }

    private void initialize() {
        ChangeListener<? super Point> moveListener = (observable1, oldValue1, newValue1) -> {
            Platform.runLater(() -> {
                Window root = getPrimaryControl().getScene().getWindow();
                Point location = MouseInfo.getPointerInfo().getLocation();
                root.setX(location.getX() - (getLayoutX() + getWidth() / 2));
                root.setY(location.getY() - (getLayoutY() + getHeight() / 2));
            });
        };

        activeProperty().addListener((observable, oldValue, newValue) -> {
            Device current = getSession().getDeviceManager().getCurrent();
            MouseEmulator mouse = current.getProcessor().getEmulationManager().getMouse();
            ObjectProperty<Point> actualPositionProperty = mouse.getListener().actualPositionProperty();
            if (newValue) {
                actualPositionProperty.addListener(moveListener);
            } else {
                actualPositionProperty.removeListener(moveListener);
            }
        });
    }

    /*    @Override
     public void buttonHandler() {
     // Save the original text
     srcID = getPrimaryControl().getId();
     // Save the original text
     srcText = getPrimaryControl().getText();
     // Save the original style
     srcStyle = getPrimaryControl().getStyle();
     }*/
    @Override
    public void run() {
        toggleActive();
    }

    private BooleanProperty active;

    /**
     *
     */
    public void toggleActive() {
        activeProperty().setValue(!activeProperty().getValue());
    }

    /**
     *
     * @param value
     */
    public void setActive(Boolean value) {
        activeProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isActive() {
        return activeProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty activeProperty() {
        if (active == null) {
            active = new SimpleBooleanProperty(false);
        }
        return active;
    }
}
