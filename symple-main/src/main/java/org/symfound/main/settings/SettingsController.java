/*
 * Copyright (C) 2014 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/

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
package org.symfound.main.settings;

import images.Images;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.Logger;
import org.symfound.builder.user.User;
import org.symfound.builder.user.characteristic.Interaction;
import org.symfound.controls.system.OnOffButton;
import org.symfound.device.hardware.Hardware;
import org.symfound.device.hardware.characteristic.Movability;
import org.symfound.device.hardware.characteristic.Selectability;
import org.symfound.device.hardware.eyetracker.EyeTracker;
import org.symfound.device.selection.SelectionEventType;
import org.symfound.main.FullSession;
import static org.symfound.main.FullSession.*;
import org.symfound.main.HomeController;
import org.symfound.tools.iteration.ModeIterator;

/**
 *
 * @author Javed Gangjee
 */
public class SettingsController extends SettingsControllerBase {

    private static final String NAME = SettingsController.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    @FXML
    private Button btnDevice;
    @FXML
    private OnOffButton btnMouseControl;
    @FXML
    private OnOffButton btnSelectionControl;
    @FXML
    private Slider dwellSensitivityField;
    @FXML
    private ChoiceBox<String> cbSelectionEvent;
    @FXML
    private ChoiceBox<String> cbSwitchDirection;


    @FXML
    private void cancelSettings(MouseEvent e) {
        cancelSettings();
        getMainUI().getStack().load(HOME);
        getMainUI().open();
        HomeController.setUpdated(false);
    }

    @FXML
    private void prevDevice(MouseEvent e) {
        getSession().getDeviceManager().getIterator().previous();

    }

    @FXML
    private void nextDevice(MouseEvent e) {
        getSession().getDeviceManager().getIterator().next();
    }

    @FXML
    private void updateSettings(MouseEvent e) throws IOException {
        setSettings();
        getMainUI().getStack().load(HOME);
        getMainUI().open();
        HomeController.setUpdated(true);

    }

    @Override
    public void setSettings() {
        LOGGER.info("Setting Main Settings");
        final User user = getUser();
        //DEVICE
        final String selectedDeviceName = getSession().getDeviceManager().getIterator().get();
        user.setDeviceName(selectedDeviceName);
        final Interaction interaction = user.getInteraction();
        interaction.setMouseControl(btnMouseControl.getValue());
        interaction.setSelectionControl(btnSelectionControl.getValue());
        Hardware hardware = getSession().getDeviceManager().get(selectedDeviceName).getHardware();
        final Selectability selectability = hardware.getSelectability();
        selectability.setSensitivity((int) (dwellSensitivityField.getValue()));
        selectability.getClickability().setEventType(new SelectionEventType(cbSelectionEvent.getValue()));

    }

    @Override
    public void resetSettings() {
        LOGGER.info("Resetting Main Settings");

        //DEVICE
        final String activeDevice = getUser().getDeviceName();
        getSession().getDeviceManager().getIterator().set(activeDevice);
        btnDevice.setText(activeDevice);
        String image = Images.class.getResource("device_"
                + activeDevice.toLowerCase().replaceAll(" ", "").trim() + ".png").toExternalForm();
        btnDevice.setStyle("-fx-background-image: url('" + image + "'); "
                + "-fx-background-size: 80 80; "
                + "-fx-background-repeat: no-repeat; "
                + "-fx-background-position: center;");

        Hardware hardware = getSelectedHardware();
        resetMouseControl(hardware);
        resetSelectionControl(hardware);

        HomeController.setUpdated(false);
    }
    /**
     *
     * @param hardware
     */
    public void resetMouseControl(Hardware hardware) {
        final Movability movability = hardware.getMovability();
        //btnMouseControl.setDisable(!movability.isEnabled());
        btnMouseControl.setValue(getUser().getInteraction().needsMouseControl());
    }

    /**
     *
     * @param hardware
     */
    public void resetSelectionControl(Hardware hardware) {
        final Selectability selectability = hardware.getSelectability();
        btnSelectionControl.setDisable(!selectability.canSelect());
        btnSelectionControl.setValue(getUser().getInteraction().needsSelectionControl());
      //  dwellSensitivityField.setVisible(selectability.getDwellability().isEnabled() && selectionMethod.equals(SelectionMethod.DWELL));
        dwellSensitivityField.setVisible(selectability.getDwellability().isEnabled());
        dwellSensitivityField.setValue(selectability.getSensitivity());
        cbSelectionEvent.setItems(FXCollections.observableArrayList(SelectionEventType.EVENT_TYPES));
       // cbSelectionEvent.setVisible(selectability.getClickability().isEnabled() && (selectionMethod.equals(SelectionMethod.SWITCH) || selectionMethod.equals(SelectionMethod.CLICK)));
       cbSelectionEvent.setVisible(selectability.getClickability().isEnabled());
        cbSelectionEvent.setValue(selectability.getClickability().getEventType().getValue());

    }

    @Override
    public void initialize(URL location, ResourceBundle rb) {
     

        ObjectProperty<String> selectedDeviceMode = getSession().getDeviceManager().getIterator().modeProperty();
        selectedDeviceMode.addListener((observable, oldValue, newValue) -> {
            btnDevice.setText(newValue);
            String image = Images.class.getResource("device_"
                    + newValue.toLowerCase().replaceAll(" ", "").trim() + ".png").toExternalForm();
            btnDevice.setStyle("-fx-background-image: url('" + image + "'); "
                    + "-fx-background-size: 80 80; "
                    + "-fx-background-repeat: no-repeat; "
                    + "-fx-background-position: center;");
            Hardware hardware = getSession().getDeviceManager().get(newValue).getHardware();
            resetMouseControl(hardware);
            resetSelectionControl(hardware);
        });

        resetSettings();
        getSession().builtProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                FullSession.getMainUI().getStack().currentProperty().addListener((observable1, oldValue1, newValue1) -> {
                    if (newValue1.get().contains(FullSession.MAIN_SETTINGS)) {
                        resetSettings();
                    }
                });
            }

        });

    }


}
