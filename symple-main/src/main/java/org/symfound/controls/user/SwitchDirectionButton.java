/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.symfound.controls.RunnableControl;
import org.symfound.device.Device;
import org.symfound.device.emulation.EmulationManager;
import org.symfound.device.emulation.input.switcher.SwitchDirection;
import org.symfound.device.emulation.input.switcher.SwitchDirector;
import org.symfound.device.emulation.input.switcher.SwitchListener;

/**
 *
 * @author Javed Gangjee
 */
public class SwitchDirectionButton extends RunnableControl {

    private final Device device;

    /**
     *
     * @param device
     */
    public SwitchDirectionButton(Device device) {
        this.device = device;
        initialize();
    }

    private void initialize() {
        getDirector().currentDirectionProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                if (!newValue.equals(SwitchDirector.NEUTRAL)
                        && !newValue.equals(SwitchDirector.OTHER)) {
                    setText(newValue.getName());
                    getPrimaryControl().setStyle("");
                    if (newValue.equals(SwitchDirector.UP)) {
                        getPrimaryControl().setStyle("-fx-background-color:-fx-red;");
                    } else if (newValue.equals(SwitchDirector.DOWN)) {
                        getPrimaryControl().setStyle("-fx-background-color:-fx-green;");
                    } else if (newValue.equals(SwitchDirector.LEFT)) {
                        getPrimaryControl().setStyle("-fx-background-color:-fx-purple;");
                    } else if (newValue.equals(SwitchDirector.RIGHT)) {
                        getPrimaryControl().setStyle("-fx-background-color:-fx-orange;");
                    }
                }
            });
        });
    }

    /**
     *
     */
    public ObjectProperty<SwitchDirector> director;

    /**
     *
     * @param value
     */
    public void setDirector(SwitchDirector value) {
        directorProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public SwitchDirector getDirector() {
        return directorProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<SwitchDirector> directorProperty() {
        if (director == null) {
            EmulationManager emulationManager = device.getProcessor().getEmulationManager();
            SwitchListener listener = emulationManager.getSwitch().getListener();
            director = new SimpleObjectProperty<>(new SwitchDirector(listener));

        }
        return director;
    }

    private ObjectProperty<SwitchDirection> currentDirection;

    /**
     *
     * @param value
     */
    public void setCurrentDirection(SwitchDirection value) {
        currentDirectionProperty().setValue(value);

    }

    /**
     *
     * @return
     */
    public SwitchDirection getCurrentDirection() {
        return currentDirectionProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<SwitchDirection> currentDirectionProperty() {
        if (currentDirection == null) {
            currentDirection = new SimpleObjectProperty<>();
        }
        return currentDirection;
    }

    @Override
    public void run() {

    }

}
