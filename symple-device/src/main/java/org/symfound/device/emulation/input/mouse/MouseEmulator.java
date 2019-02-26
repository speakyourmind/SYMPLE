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
package org.symfound.device.emulation.input.mouse;

import java.awt.AWTException;
import java.awt.Point;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import org.apache.log4j.Logger;
import org.jnativehook.mouse.NativeMouseEvent;
import org.symfound.builder.user.User;
import org.symfound.device.emulation.input.InputEmulator;
import org.symfound.device.hardware.Hardware;
import org.symfound.device.hardware.characteristic.Selectability;
import org.symfound.tools.timing.DelayedEvent;

/**
 *
 * @author Javed Gangjee
 */
public class MouseEmulator extends InputEmulator<MouseAutomator, NativeMouseListener> {

    private static final String NAME = MouseEmulator.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    private final Hardware hardware;
    private final User user;

    /**
     *
     * @param hardware
     * @param user
     */
    public MouseEmulator(Hardware hardware, User user) {
        this.hardware = hardware;
        this.user = user;
        try {
            automator = new MouseAutomator();
        } catch (AWTException ex) {
            LOGGER.fatal(ex);
        }
        initialize();
    }

    private void initialize() {
        requestedPositionProperty().addListener((observable, oldValue, newValue) -> {
            try {
                final Boolean needsMouse = user.getInteraction().needsMouseControl();
                if (needsMouse) {
                    // Navigate to the requested position

                    //TODO: Test
                    /*    if (((int) newValue.getX()) == 0 && ((int) newValue.getY()) == 0) {
                        LOGGER.warn("Zero mouse position requested. skipping emulation");
                    } else{
                    LOGGER.debug("Requesting mouse to position: " + newValue);
                    getAutomator().navigate(newValue);
                    
                    }*/
                    LOGGER.debug("Requesting mouse to position: " + newValue);
                    getAutomator().navigate(newValue);
                }
            } catch (NullPointerException ex) {
                LOGGER.fatal("Failed to set updated mouse position", ex);
            }
        });

        requestedSelectionProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                try {
                    final Boolean userRequires = user.getInteraction().needsSelectionControl();
                    if (userRequires) {
                        Selectability selectability = hardware.getSelectability();
                        Double scale = selectability.getZoomScale();
                        Integer size = selectability.getZoomSize();
                        getAutomator().runClickSelect(getRequestedPosition(), scale, size);
                        LOGGER.info("Requesting mouse to click:" + getAutomator().getSelectionType());
                        DelayedEvent postSelectionHold = new DelayedEvent();
                        final Double postSelectTime = selectability.getPostSelectTime();
                        postSelectionHold.setup(postSelectTime, (ActionEvent e) -> {
                            requestSelection(false);
                        });
                        postSelectionHold.play();
                    }
                } catch (AWTException ex) {
                    LOGGER.fatal("Failed to execute click", ex);
                }
            }
        });

    }

    /**
     *
     * @return
     */
    @Override
    public NativeMouseListener getListener() {
        if (listener == null) {
            listener = new NativeMouseListener() {
                @Override
                public void nativeMousePressed(NativeMouseEvent e) {
                    Platform.runLater(() -> {
                        try {
                            Selectability selectability = hardware.getSelectability();
                            Double scale = selectability.getZoomScale();
                            Integer size = selectability.getZoomSize();
                            getAutomator().runClickSelect(listener.getActualPosition(), scale, size);
                        } catch (AWTException ex) {
                            LOGGER.fatal("Failed to read click", ex);
                        }
                    });
                }

                @Override
                public void nativeMouseMoved(NativeMouseEvent e) {
                    listener.setActualPosition(new Point(e.getX(), e.getY()));
                }
            };
        }

        return listener;
    }

    /**
     *
     */
    public static final Point DEFAULT_POSITION = new Point(0, 0);

    /**
     *
     */
    public ObjectProperty<Point> requestedPosition;

    /**
     *
     * @param value
     */
    public void requestPosition(Point value) {
        requestedPositionProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Point getRequestedPosition() {
        return requestedPositionProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<Point> requestedPositionProperty() {
        if (requestedPosition == null) {
            requestedPosition = new SimpleObjectProperty<>(DEFAULT_POSITION);
        }
        return requestedPosition;
    }

    /**
     *
     */
    public static final Boolean DEFAULT_SELECTION = Boolean.FALSE;

    /**
     *
     */
    public BooleanProperty requestedSelection;

    /**
     *
     * @param value
     */
    public void requestSelection(Boolean value) {
        requestedSelectionProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isSelectionRequested() {
        return requestedSelectionProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty requestedSelectionProperty() {
        if (requestedSelection == null) {
            requestedSelection = new SimpleBooleanProperty(DEFAULT_SELECTION);
        }
        return requestedSelection;
    }

}
