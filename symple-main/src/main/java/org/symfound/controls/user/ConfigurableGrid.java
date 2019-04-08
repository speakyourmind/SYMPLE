/*
 * Copyright (C) 2014 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/
 *
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
package org.symfound.controls.user;

import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symfound.builder.user.selection.SelectionMethod;
import org.symfound.device.hardware.Hardware;
import org.symfound.main.Main;
import org.symfound.main.settings.SettingsController;
import org.symfound.selection.modes.Scanner;
import org.symfound.selection.modes.Stepper;
import org.symfound.tools.iteration.ParallelList;
import org.symfound.tools.timing.DelayedEvent;

/**
 * Builds the home page grid of the program based on current user preferences
 * including ability level, device and selection mode.
 *
 * @author Javed Gangjee
 */
public class ConfigurableGrid extends ButtonGrid {

    private static final String NAME = ConfigurableGrid.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public ConfigurableGrid() {
        super();
    }

    /**
     *
     */
    public void configure() {

        setOrder(getGridManager().getOrder());
        orderProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.info("Setting order " + newValue.asString());
            getGridManager().setOrder(newValue);
        });

        setGap(getGridManager().getGap());
        gapProperty().addListener((observable, oldValue, newValue) -> {
            getGridManager().setGap(newValue.doubleValue());
        });

        setFillMethod(getGridManager().getFillMethod());
        fillMethodProperty().addListener((observable, oldValue, newValue) -> {
            getGridManager().setFillMethod(newValue);
        });

        setFillDirection(getGridManager().getFillDirection());
        fillDirectionProperty().addListener((observable, oldValue, newValue) -> {
            getGridManager().setFillDirection(newValue);
        });

        setMaxDifficulty(getGridManager().getMaxDifficulty());
        maxDifficultyProperty().addListener((observable, oldValue, newValue) -> {
            getGridManager().setMaxDifficulty(newValue.doubleValue());
        });

        setMinDifficulty(getGridManager().getMinDifficulty());
        minDifficultyProperty().addListener((observable, oldValue, newValue) -> {
            getGridManager().setMinDifficulty(newValue.doubleValue());
        });

        setOverrideRow(getGridManager().getOverrideRow());
        overrideRowProperty().addListener((observable, oldValue, newValue) -> {
            getGridManager().setOverrideRow(newValue.doubleValue());
        });

        setOverrideColumn(getGridManager().getOverrideColumn());
        overrideColumnProperty().addListener((observable, oldValue, newValue) -> {
            getGridManager().setOverrideColumn(newValue.doubleValue());
        });

        setOverrideStyle(getGridManager().getOverrideStyle());
        overrideStyleProperty().addListener((observable, oldValue, newValue) -> {
            getGridManager().setOverrideStyle(newValue);

        });

        setSelectionMethod(getGridManager().getSelectionMethod());
        selectionMethodProperty().addListener((observable, oldValue, newValue) -> {
            getGridManager().setSelectionMethod(newValue);
        });

        setPaused(getGridManager().isPaused());
        pausedProperty().addListener((observable, oldValue, newValue) -> {
            getGridManager().setPaused(newValue);
        });

        triggerReloadProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                reload();
                setTriggerReload(Boolean.FALSE);
            }
        });

        indexProperty().addListener((observable1, oldValue1, newValue1) -> {
            LOGGER.info("Index updated to " + newValue1
                    + " order is " + getGridManager().getOrder().asString()
                    + "difficulty is " + getGridManager().getMaxDifficulty());
            setOrder(getGridManager().getOrder());
            setGap(getGridManager().getGap());
            setFillMethod(getGridManager().getFillMethod());
            setFillDirection(getGridManager().getFillDirection());
            setMaxDifficulty(getGridManager().getMaxDifficulty());
            setMinDifficulty(getGridManager().getMinDifficulty());
            setOverrideRow(getGridManager().getOverrideRow());
            setOverrideColumn(getGridManager().getOverrideColumn());
            setOverrideStyle(getGridManager().getOverrideStyle());
            setSelectionMethod(getGridManager().getSelectionMethod());
            setPaused(getGridManager().isPaused());

            triggerReload();
        });

        BooleanProperty updatedSettings = SettingsController.updatedProperty();
        updatedSettings.addListener((observable2, oldValue2, newValue2) -> {
            if (newValue2) {
                /*   if (getOrder().getFirstList().contains(EditGridButton.KEY)) {
                    getOrder().remove(EditGridButton.KEY);
                }*/
                triggerReload();
                SettingsController.setUpdated(false);
            }
        });

        triggerReload();
        statusProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == ScreenStatus.PLAYING) {
                getScanner().configure();
                getStepper().configure();
            }
            getUser().getInteraction().selectionMethodProperty().addListener((observable1, oldValue1, newValue1) -> {
                LOGGER.info("User selection method changed from "
                        + oldValue1.toString() + " to " + newValue1.toString());
            });

        });
    }

    /**
     *
     */
    public void reload() {
        Double level = getUser().getAbility().getLevel();
        Double max = getMaxDifficulty();
        Double min = getMinDifficulty();
        Double size;
        if (min < level && level < max) {
            LOGGER.info("User's ability " + level
                    + " is less than the maximum difficulty of this grid: " + max
                    + " & greater than the minimum: " + min);
            size = level;
            LOGGER.info("Adjusted grid size to user's ability: " + size);
        } else if (level >= max) {
            LOGGER.info("User ability " + level + "  is more than the maximum difficulty of this grid: " + max);
            size = max;
            LOGGER.info("Setting grid size to this grid's difficulty: " + size);
        } else {
            LOGGER.warn("User ability " + level + "  is less than the minimum difficulty of this grid: " + max);
            size = min;
            LOGGER.warn("This screen is not well suited to the user");
        }
        reload(getValidatedKeyOrder(getOrder()), getFillMethod(), getFillDirection(), size);

    }

    /**
     *
     */
    public void stopSelector() {
        if (getScanner().inProcess()) {
            getScanner().stop();
        }
        if (getStepper().inProcess()) {
            getStepper().stop();
        }
    }

    private void startSelector(SelectionMethod newValue1) {
        if (this.getScene() != null) {
            if (!inEditMode() && getParentUI().isShowing()) {
                LOGGER.info("Grid is not in edit mode & the parent UI is showing");
                if (newValue1.equals(SelectionMethod.SCAN)) {
                    LOGGER.info("In SCAN mode");
                    if (!getScanner().inProcess()) {
                        getScanner().start();
                    } else {
                        LOGGER.warn("Attempting to start scanner while already in process");
                    }
                } else {
                    if (getScanner().inProcess()) {
                        LOGGER.info("Stopping scanner as user is not in SCAN mode");
                        getScanner().stop();
                    }
                }

                if (newValue1.equals(SelectionMethod.STEP)) {
                    LOGGER.info("In STEP mode");
                    if (!getStepper().inProcess()) {
                        getStepper().start();
                    } else {
                        LOGGER.warn("Attempting to start stepper while already in process");
                    }
                } else {
                    if (getStepper().inProcess()) {
                        LOGGER.info("Stopping stepper as user is not in STEP mode");
                        getStepper().stop();
                    }
                }
            }
        } else {
            LOGGER.warn("The scene of this grid is NULL");
        }
    }

    /**
     *
     * @param buildOrder
     * @param method
     * @param direction
     * @param size
     */
    @Override
    public void reload(ParallelList<String, String> buildOrder, FillMethod method, FillDirection direction, Double size) {
        Platform.runLater(() -> {
            if (!mutex) {
                mutex = true;
                LOGGER.info("Reloading App Grid with " + buildOrder.asString() + " and size " + size);
                build(buildOrder, method, direction, size);
                if (getUser().getInteraction().getSelectionMethod().equals(SelectionMethod.DWELL)) {
                    // setDisable(true);
                    setOpacity(1.0);
                    DelayedEvent delayedEvent = new DelayedEvent();
                    final Hardware hardware = getSession().getDeviceManager().getCurrent().getHardware();
                    delayedEvent.setup(hardware.getSelectability().getPostSelectTime(), (ActionEvent e) -> {
                        //   setDisable(false);
                        setOpacity(1.0);
                    });
                    delayedEvent.play();
                }
                mutex = false;
            }
        });
    }

    private StringProperty index;

    /**
     * Sets the offset of the UI in the Application list that this button will
     * point to.
     *
     * @param value index offset
     */
    public void setIndex(String value) {
        indexProperty().set(value);
    }

    /**
     * Gets the offset of the UI in the Application list that this button will
     * point to.
     *
     * @return index offset
     */
    public String getIndex() {
        return indexProperty().get();
    }

    /**
     * Represents the offset of the UI in the Application list that this button
     * will point to
     *
     * @return index offset property
     */
    public StringProperty indexProperty() {
        if (index == null) {
            index = new SimpleStringProperty("default");
        }
        return index;
    }

    /**
     *
     */
    public GridManager manager;

    /**
     *
     * @return
     */
    public GridManager getGridManager() {
        if (manager == null) {
            manager = new GridManager() {
                private Preferences preferences;

                @Override
                public Preferences getPreferences() {
                    preferences = getPrefs(getIndex());
                    return preferences;
                }

            };
        }
        return manager;
    }

    private static final Boolean DEFAULT_EDIT_MODE = Boolean.FALSE;
    private static BooleanProperty editMode;

    /**
     *
     * @param value
     */
    public static void setEditMode(Boolean value) {
        editModeProperty().setValue(value);
    }

    /**
     *
     */
    public static void toggleEditMode() {
        setEditMode(!inEditMode());
    }

    /**
     *
     * @return
     */
    public static Boolean inEditMode() {
        return editModeProperty().getValue();
    }

    /**
     *
     * @return
     */
    public static BooleanProperty editModeProperty() {
        if (editMode == null) {
            editMode = new SimpleBooleanProperty(DEFAULT_EDIT_MODE);
        }
        return editMode;
    }

    /**
     *
     */
    public Scanner scanner;

    /**
     *
     * @return
     */
    public Scanner getScanner() {
        if (scanner == null) {
            scanner = new Scanner(this, Main.getSession().getUser());
        }
        return scanner;
    }

    /**
     *
     */
    public Stepper stepper;

    /**
     *
     * @return
     */
    public Stepper getStepper() {
        if (stepper == null) {
            stepper = new Stepper(this, Main.getSession().getUser());
        }
        return stepper;
    }

}
