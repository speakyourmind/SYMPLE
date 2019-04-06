/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.selection;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;
import org.symfound.builder.user.User;
import org.symfound.builder.user.selection.Chooser;
import org.symfound.builder.user.selection.SelectionMethod;
import org.symfound.controls.RunnableControl;
import org.symfound.controls.user.ConfigurableGrid;
import static org.symfound.controls.user.ConfigurableGrid.editModeProperty;
import static org.symfound.controls.user.ConfigurableGrid.inEditMode;
import org.symfound.controls.user.SubGrid;
import org.symfound.main.Main;
import org.symfound.tools.timing.LoopedEvent;

/**
 *
 * @author Javed Gangjee
 */
public abstract class Selector {

    private static final String NAME = Selector.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public final ConfigurableGrid grid;
    private final User user;

    /**
     *
     * @return
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @param grid
     * @param method
     * @param user
     */
    public Selector(ConfigurableGrid grid, SelectionMethod method, User user) {
        this.grid = grid;
        this.method = method;
        this.user = user;
    }

    /**
     *
     */
    public void configure() {
        Main.getSession().playingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                configureStartStop();
            }
        });

    }

    /**
     *
     */
    public void configureStartStop() {
        startStop();

        getUser().getInteraction().overrideSelectionMethodProperty().addListener((observable1, oldValue1, newValue1) -> {
            LOGGER.info("User selection method override changed from "
                    + oldValue1.toString() + " to " + newValue1.toString());
            startStop();
        });

        getUser().getInteraction().selectionMethodProperty().addListener((observable1, oldValue1, newValue1) -> {
            LOGGER.info("User selection method changed from "
                    + oldValue1.toString() + " to " + newValue1.toString());
            startStop();
        });

        grid.selectionMethodProperty().addListener((observable1, oldValue1, newValue1) -> {
            LOGGER.info("Grid selection method override changed from "
                    + oldValue1.toString() + " to " + newValue1.toString());
            startStop();
        });

        editModeProperty().addListener((observable, oldValue, newValue) -> {
            startStop();
        });

        grid.indexProperty().addListener((observable, oldValue, newValue) -> {
            startStop();
        });

    }

    /**
     *
     */
    public void startStop() {
        SelectionMethod userMethod = getUser().getInteraction().getSelectionMethod();
        SelectionMethod gridMethod = grid.getSelectionMethod();

        Boolean overrideSelectionMethod = getUser().getInteraction().overrideSelectionMethod();

        SelectionMethod deducedMethod = (overrideSelectionMethod) ? gridMethod : userMethod;

        final int size = grid.getGridManager().getOrder().getFirstList().size();
        if (deducedMethod.equals(getSelectionMethod())
                && !inEditMode()
                && grid.isRootGrid()
                && size > 1) {
            if (!inProcess()) {
                removeSelectorButton();
                LOGGER.info("Required method is " + deducedMethod.toString()
                        + ". Starting selector:" + getSelectionMethod());
                addSelectorButton(grid, getSelectorButton());
            }
        } else if (!deducedMethod.equals(getSelectionMethod())
                || inEditMode()
                || size <= 1) {
            LOGGER.info("Required method is " + deducedMethod.toString()
                    + ". Stopping selector." + getSelectionMethod());
            stop();
        }
    }

    private final SelectionMethod method;

    /**
     *
     * @return
     */
    public SelectionMethod getSelectionMethod() {
        return method;
    }

    /**
     *
     */
    public abstract void start();

    /**
     *
     */
    public void stop() {
        removeSelectorButton();
        getLoopedEvent().end();
        setInProcess(false);
        getScourer().resetPosition();
        getScourer().clearHighlight();
    }

    /**
     *
     */
    public abstract void reset();

    private RunnableControl selectorButton;

    /**
     *
     * @return
     */
    public RunnableControl getSelectorButton() {
        if (selectorButton == null) {
            selectorButton = new RunnableControl("selector") {
                @Override
                public void run() {
                    if (!inProcess()) {
                        start();
                    } else {
                        onSelected();
                    }
                }
            };
        }
        return selectorButton;
    }

    /**
     *
     */
    public void onSelected() {
        runSelection();
    }

    /**
     *
     * @param configGrid
     * @param selectorButton
     */
    public void addSelectorButton(ConfigurableGrid configGrid, RunnableControl selectorButton) {
        Parent parent = configGrid.getParent();
        if (parent instanceof SubGrid) {
            SubGrid subGrid = (SubGrid) parent;
            LOGGER.info("Adding Selector button to Sub grid " + grid.getIndex());
            subGrid.addToPane(selectorButton);
        } else if (parent instanceof GridPane) {
            ((GridPane) parent).add(selectorButton, 0, 1);
        } else {
            LOGGER.fatal("Scanner node's parent is neither a grid nor a subgrid");
        }
    }

    /**
     *
     */
    public void removeSelectorButton() {
        LOGGER.info("Removing selector button");
        getSelectorButton().removeFromParent();
    }

    /**
     *
     */
    public void runSelection() {
        Integer currentRow = getScourer().getCurrentRow();
        Integer currentColumn = getScourer().getCurrentColumn();
        if (currentRow >= 0 && currentColumn >= 0) {
            LOGGER.info("Selection made. Running current control at "
                    + currentRow + ", " + currentColumn);
            RunnableControl current = getScourer().getCurrent();
            if (current instanceof SubGrid) {
                LOGGER.info("Sub grid selected");
                stop();
                SubGrid currentGrid = (SubGrid) current;
                invokeSubGrid(currentGrid);
            } else {
                LOGGER.info("Executing selection ");
                current.buttonHandler();
                current.execute();
            }

            setExecuted(true);
            stop();

            if (grid.isRootGrid()) {
                LOGGER.info("Restarting root grid");
                configureStartStop();
            }
            setExecuted(false);
        } else {
            LOGGER.warn("User attempting to make selection before first cell is highlighted");
        }

    }

    /**
     *
     * @param currentGrid
     */
    public abstract void invokeSubGrid(SubGrid currentGrid);

    private Scourer scourer;

    /**
     *
     * @return
     */
    public Scourer getScourer() {
        if (scourer == null) {
            scourer = new Scourer(grid);
        }
        return scourer;
    }
    private Chooser chooser;

    /**
     *
     * @return
     */
    public Chooser getChooser() {
        if (chooser == null) {
            chooser = new Chooser();
        }
        return chooser;
    }
    /**
     *
     */
    private LoopedEvent loopedEvent;

    /**
     *
     * @return
     */
    public LoopedEvent getLoopedEvent() {
        if (loopedEvent == null) {
            loopedEvent = new LoopedEvent();
        }
        return loopedEvent;
    }

    /**
     *
     */
    public BooleanProperty inProcess;

    /**
     *
     * @param value
     */
    public void setInProcess(Boolean value) {
        inProcessProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean inProcess() {
        return inProcessProperty().getValue();

    }

    /**
     *
     * @return
     */
    public BooleanProperty inProcessProperty() {
        if (inProcess == null) {
            inProcess = new SimpleBooleanProperty(false);
        }
        return inProcess;
    }

    private BooleanProperty execute;

    /**
     *
     * @param value
     */
    public void setExecuted(Boolean value) {
        executeProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public BooleanProperty executeProperty() {
        if (execute == null) {
            execute = new SimpleBooleanProperty(Boolean.FALSE);
        }
        return execute;
    }
}
