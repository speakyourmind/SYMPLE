/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.device;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;
import org.symfound.controls.RunnableControl;
import static org.symfound.controls.ScreenControl.setSize;
import static org.symfound.controls.ScreenControl.setSizeMax;
import org.symfound.controls.system.SettingsRow;
import org.symfound.controls.system.SettingsTab;
import org.symfound.controls.user.ButtonGrid;
import org.symfound.device.Device;
import org.symfound.device.hardware.eyetracker.GamingEyeTracker;

/**
 *
 * @author Javed Gangjee
 */
public class EyeTrackerSettings extends DeviceSettings<GamingEyeTracker> {

    private static final String NAME = EyeTrackerSettings.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public EyeTrackerSettings() {
        super();
        initialize();
    }

    private void initialize() {
       populate();
    }

    /**
     *
     * @return
     */
    @Override
    public GamingEyeTracker getHardware() {
        if (hardware == null) {
            hardware = getSession().getDeviceManager().eyeTracker.getHardware();
        }
        return hardware;
    }

    /**
     *
     * @return
     */
    @Override
    public SettingsTab buildGeneralTab() {
        SettingsRow consoleRow = buildSettingsRow("Console", "Output from external reader");
        TextArea console = new TextArea();
        console.setStyle("-fx-font-size: 1.1em;");
        console.setPromptText("Waiting for " + hardware.getName() + " data");
        GridPane.setMargin(console, new Insets(20.0));
        getHardware().getRuntimeExecutor().consoleTextProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                int length = newValue.length();
                if (length > 1000) {
                    console.setText(newValue.substring(length - 1000, length));
                    console.appendText("");
                    console.setScrollTop(Double.MAX_VALUE);
                }
            });//this will scroll to the bottom
        });
        console.setEditable(false);
        console.setWrapText(false);
        setSizeMax(console);
        consoleRow.add(console, 1, 0, 2, 1);

        SettingsRow calibrationRow = buildSettingsRow("Calibrate", "Run the manufacturer's calibration routine");
        RunnableControl calibrate = new RunnableControl() {
            @Override
            public void run() {
                getHardware().calibrate();
            }
        };

        calibrate.setSymStyle("settings-button");
        calibrate.setControlType(ControlType.SETTING_CONTROL);
        setSize(calibrate, MAX_WIDTH, MAX_HEIGHT);
        calibrate.setText("CALIBRATE");
        calibrate.setOnMouseClicked(null);
        calibrationRow.add(calibrate, 1, 0, 1, 1);

        SettingsRow troubleshootRow = buildSettingsRow("Troubleshoot", "Fix this tracker if you get an error");
        RunnableControl troubleshoot = new RunnableControl() {
            @Override
            public void run() {
                String TEST = "\"C:\\Program Files\\SYMPLE\\app\\etc\\tobii\\Launch.lnk\"";
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c",
                        TEST);
                Process p;
                try {
                    p = pb.start();
                    p.waitFor();
                } catch (IOException | InterruptedException ex) {
                    LOGGER.fatal(ex);
                }

            }
        };

        troubleshoot.setSymStyle("settings-button");
        troubleshoot.setControlType(ControlType.SETTING_CONTROL);
        setSize(troubleshoot, MAX_WIDTH, MAX_HEIGHT);
        troubleshoot.setText("FIX");
        troubleshoot.setOnMouseClicked(null);
        troubleshootRow.add(troubleshoot, 1, 0, 1, 1);

        List<SettingsRow> rows = Arrays.asList(consoleRow, calibrationRow, troubleshootRow);
        SettingsTab generalTab = new SettingsTab(GENERAL_TAB_TITLE, rows);
        return generalTab;
    }

    /**
     *
     * @return
     */
    @Override
    public Device<GamingEyeTracker> getDevice() {
        if (device == null) {
            device = getSession().getDeviceManager().eyeTracker;
        }
        return device;
    }

    @Override
    public Node addSettingControls() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSettings() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetSettings() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
