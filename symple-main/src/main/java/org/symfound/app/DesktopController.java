/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.app;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.apache.log4j.Logger;
import org.symfound.device.Device;
import org.symfound.device.emulation.input.mouse.ZoomUI;
import org.symfound.device.processing.Processor;
import org.symfound.main.builder.UI;

/**
 *
 * @author Javed Gangjee
 */
public class DesktopController extends CommonController {

    private static final String NAME = DesktopController.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Desktop";
    @FXML
    private AnchorPane apMain;

    private static ThreadPoolExecutor executor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getSession().builtProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                UI ui = getParentUI(apMain);
                ui.getScene().setFill(ZoomUI.TRANSLUCENT_COLOUR);
                ui.showingProperty().addListener((observeableValue1, oldValue1, newValue1) -> {
                    Device current = getSession().getDeviceManager().getCurrent();
                    Processor processor = current.getProcessor();
                    processor.getEmulationManager().listen(newValue1);
                });
            }
        });

    }

    /**
     *
     * @return
     */
    public static ThreadPoolExecutor getDesktopExecutor() {
        if (executor == null) {
            executor = new ThreadPoolExecutor(3, 8, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        }
        return executor;
    }

    private static final Boolean DEFAULT_SHIFT_MODE = Boolean.FALSE;
    private static BooleanProperty shiftMode;

    /**
     *
     * @param value
     */
    public static void setShiftMode(Boolean value) {
        shiftModeProperty().setValue(value);
    }

    /**
     *
     */
    public static void toggleShiftMode() {
        setShiftMode(!shiftMode());
    }

    /**
     *
     * @return
     */
    public static Boolean shiftMode() {
        return shiftModeProperty().getValue();
    }

    /**
     *
     * @return
     */
    public static BooleanProperty shiftModeProperty() {
        if (shiftMode == null) {
            shiftMode = new SimpleBooleanProperty(DEFAULT_SHIFT_MODE);
        }
        return shiftMode;
    }
}
