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
package org.symfound.device.emulation;

import java.awt.Point;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symfound.builder.user.User;
import org.symfound.builder.user.selection.SelectionMethod;
import org.symfound.device.emulation.input.InputListener;
import org.symfound.device.emulation.input.keyboard.KeyboardEmulator;
import org.symfound.device.emulation.input.mouse.MouseEmulator;
import org.symfound.device.emulation.input.mouse.MousePositionLog;
import org.symfound.device.emulation.input.mouse.NativeMouseListener;
import org.symfound.device.emulation.input.switcher.SwitchEmulator;
import org.symfound.device.hardware.Hardware;
import org.symfound.device.hardware.characteristic.Movability;
import org.symfound.tools.timing.DelayedEvent;

/**
 *
 * @author Javed Gangjee
 */
public final class EmulationManager {

    /**
     *
     */
    public static final String NAME = EmulationManager.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public double width;

    /**
     *
     */
    public double height;

    /**
     *
     */
    public Hardware hardware;

    /**
     *
     */
    public User user;

    /**
     *
     * @param user
     * @param hardware
     */
    public EmulationManager(User user, Hardware hardware) {
        this.hardware = hardware;
        this.user = user;

        requestProperty().addListener((observable, oldValue, newValue) -> {
            record(false);
            updateMousePosition(newValue);
            updateSelection(newValue);
        });

        recordProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Integer sampleSize = hardware.getMovability().getSampleSize();
                Point position = getRequest().getPosition();
                MousePositionLog positionLogger = getMouse().getListener().positionLog;

                positionLogger.log(position, sampleSize);
                LOGGER.debug("Mouse position log size: " + positionLogger.size() + ". Position recorded:" + position);
            }
        });
    }

    /**
     *
     * @param start
     */
    public void listen(Boolean start) {
        if (start) {
            InputListener.registerHook();
            getKeyboard().getListener().start();
            getMouse().getListener().start();
        } else {
            InputListener.stop();
        }
    }

    // Mouse
    private static MouseEmulator mouse;

    /**
     *
     * @return
     */
    public MouseEmulator getMouse() {
        if (mouse == null) {
            mouse = new MouseEmulator(hardware, user);
        }
        return mouse;
    }

    // Keyboard
    private static KeyboardEmulator keyboard;

    /**
     *
     * @return
     */
    public KeyboardEmulator getKeyboard() {
        if (keyboard == null) {
            keyboard = new KeyboardEmulator();
        }
        return keyboard;
    }
    // Switch
    private static SwitchEmulator switcher;

    /**
     *
     * @return
     */
    public SwitchEmulator getSwitch() {
        if (switcher == null) {
            switcher = new SwitchEmulator(hardware);
        }
        return switcher;
    }

    /**
     *
     */
    public Timeline dwellTimeline = new Timeline();

    /**
     *
     */
    public static Boolean clickMutex = false;

    /**
     *
     */
    public DelayedEvent resetTimeline = new DelayedEvent(); // TO DO: Replace with DelayedEvent

    /**
     *
     * @param request
     */
    public void updateMousePosition(EmulationRequest request) {
        if (shouldUpdatePosition(request)) {
            Movability movability = hardware.getMovability();
            String filterName = movability.getFilter();
            record(true);
            final NativeMouseListener listener = getMouse().getListener();
            switch (filterName) {
                case Filter.SMA:
                    Point smaPoint = listener.getSMAPoint();
                    getMouse().requestPosition(smaPoint);
                    break;
                case Filter.EMA:
                    Double smoothingFactor = movability.getSmoothingFactor();
                    Point emaPoint = listener.getEMAPoint(smoothingFactor);
                    getMouse().requestPosition(emaPoint);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid Filter name : " + filterName);

            }
        }
    }

    /**
     *
     * @param request
     * @return
     */
    public Boolean shouldUpdatePosition(EmulationRequest request) {
        final Point position = request.getPosition();
        final Boolean dataAvailable = position.getX() != 0 && position.getY() != 0;
        final Boolean hardwareAllows = hardware.getMovability().isEnabled();
        final Boolean shouldUpdate = dataAvailable && hardwareAllows;
        return shouldUpdate;
    }

    /**
     *
     * @return
     */
    public Timeline getDwellTimeline() {
        if (dwellTimeline == null) {
            Double delay = user.getTiming().getDwellTime();
            dwellTimeline = resetTimeline.setup(delay, (ActionEvent e) -> {
                getMouse().requestSelection(true);
                LOGGER.info("Requesting dwell selection");
            });
        }
        return dwellTimeline;
    }

    /**
     *
     * @param request
     */
    public void updateSelection(EmulationRequest request) {
        if (shouldUpdateSelection(request)) {
            SelectionMethod selectionMethod = user.getInteraction().getSelectionMethod();
            record(true);
            switch (selectionMethod) {
                // This means the user is able to select directly from the hardware
                // and the program does not need to click for them. For example: Swifty
                // break;
                case CLICK:
                    break;
                // This means the user is unable to select directly from the hardware
                // Program needs to do it for them. For example
                /*
            Integer dwellSensitivity = hardware.getSelectability().getSensitivity();
            if (getMouse().getListener().isDwellSelect(dwellSensitivity)) {
            if (!clickMutex) {
            clickMutex = true;
            getDwellTimeline().play();
            } else {
            clickMutex = false;
            }
            } else {
            getDwellTimeline().stop();
            clickMutex = false;
            }
            break;
                 */
                case DWELL:
                    break;
                case SWITCH:
                    // This means the user is unable to switch from the hardware.
                    // Program needs to do it for them. For example: ColourTracker
                    Point requestedPosition = request.getPosition();
                    Double tau = hardware.getSelectability().getSwitchability().getTau();
                    getSwitch().getListener().getZScore().updateMagnitude(tau, requestedPosition);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     *
     * @param request
     * @return
     */
    public boolean shouldUpdateSelection(EmulationRequest request) {
        final Point position = request.getPosition();
        final Boolean dataAvailable = position.getX() != 0 && position.getY() != 0;
        final Boolean hardwareAllows = hardware.getSelectability().canSelect();
        final boolean shouldUpdate = dataAvailable && hardwareAllows;
        return shouldUpdate;
    }

    private ObjectProperty<EmulationRequest> request;

    /**
     *
     * @param value
     */
    public void request(EmulationRequest value) {
        requestProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public EmulationRequest getRequest() {
        return requestProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<EmulationRequest> requestProperty() {
        if (request == null) {
            request = new SimpleObjectProperty<>();
        }
        return request;
    }

    /**
     *
     */
    public static final Boolean DEFAULT_RECORD = Boolean.FALSE;
    private BooleanProperty record;

    /**
     *
     * @param value
     */
    public void record(Boolean value) {
        recordProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isRecording() {
        return recordProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty recordProperty() {
        if (record == null) {
            record = new SimpleBooleanProperty(DEFAULT_RECORD);
        }
        return record;
    }
}
