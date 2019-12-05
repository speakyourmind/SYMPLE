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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import org.apache.log4j.Logger;
import org.symfound.builder.user.selection.SelectionMethod;
import org.symfound.device.hardware.Hardware;
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
    public static final String PREVIOUS_SCREEN_INDEX = "-Previous";

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

        setDescription(getGridManager().getDescription());
        descriptionProperty().addListener((observable, oldValue, newValue) -> {
            getGridManager().setDescription(newValue);
        });

        setCustomHGap(getGridManager().getCustomHGap());
        customHGapProperty().addListener((observable, oldValue, newValue) -> {
            getGridManager().setCustomHGap(newValue.doubleValue());
        });

        setCustomVGap(getGridManager().getCustomVGap());
        customVGapProperty().addListener((observable, oldValue, newValue) -> {
            getGridManager().setCustomVGap(newValue.doubleValue());
        });

        setCustomMargin(getGridManager().getCustomMargin());
        customMarginProperty().addListener((observable, oldValue, newValue) -> {
            getGridManager().setCustomMargin(newValue.doubleValue());
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
        enablePagination(getGridManager().isPaginationEnabled());
        enablePaginationProperty().addListener((observable, oldValue, newValue) -> {
            getGridManager().enablePagination(newValue);
        });
        triggerReloadProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                reload();
                setTriggerReload(Boolean.FALSE);
            }
        });

        indexProperty().addListener((observable1, oldValue1, newValue1) -> {
            LOGGER.info("User navigated from " + oldValue1 + " to " + newValue1
                    + " order is " + getGridManager().getOrder().asString()
                    + "difficulty is " + getGridManager().getMaxDifficulty());
            getUser().getNavigation().setPreviousIndex(oldValue1);
            getUser().getNavigation().setCurrentIndex(newValue1);

            setOrder(getGridManager().getOrder());
            setCustomHGap(getGridManager().getCustomHGap());
            setCustomMargin(getGridManager().getCustomMargin());
            final Double newMargin = getCustomMargin();
            this.setAnchors(this, newMargin, newMargin, newMargin, newMargin);

            setCustomVGap(getGridManager().getCustomVGap());
            setFillMethod(getGridManager().getFillMethod());
            setFillDirection(getGridManager().getFillDirection());
            setMaxDifficulty(getGridManager().getMaxDifficulty());
            setMinDifficulty(getGridManager().getMinDifficulty());
            setOverrideRow(getGridManager().getOverrideRow());
            setOverrideColumn(getGridManager().getOverrideColumn());
            setOverrideStyle(getGridManager().getOverrideStyle());
            setSelectionMethod(getGridManager().getSelectionMethod());
            setPaused(getGridManager().isPaused());
            enablePagination(getGridManager().isPaginationEnabled());
            setDescription(getGridManager().getDescription());
            triggerReload();
        });


        triggerReload();
        
    }

    /**
     *
     */
    public void reload() {

        if (this.getParent() instanceof SubGrid) {
            final ObservableList<Node> children = this.getChildren();
            for (int i = 0; i < children.size(); i++) {
                Node node = children.get(i);
                if (node instanceof SubGrid) {
                    SubGrid control = (SubGrid) node;
                    editModeProperty().removeListener(control.getConfigButtonListener());
                }
            }
        }
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
    public static BooleanProperty editMode;

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

 

}
