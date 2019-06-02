/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.selection;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import org.apache.log4j.Logger;
import org.symfound.builder.user.User;
import org.symfound.builder.user.selection.Chooser;
import org.symfound.builder.user.selection.SelectionMethod;
import org.symfound.controls.RunnableControl;
import org.symfound.controls.user.ConfigurableGrid;
import static org.symfound.controls.user.ConfigurableGrid.editModeProperty;
import static org.symfound.controls.user.ConfigurableGrid.inEditMode;
import org.symfound.controls.user.SubGrid;
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
    public final ConfigurableGrid gridToScour;
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
        this.gridToScour = grid;
        this.method = method;
        this.user = user;
    }

    /**
     *
     */
    public void configure() {
        startStop();
        Bindings.concat(getUser().getInteraction().assistedModeProperty().asString(),
                getUser().getInteraction().selectionMethodProperty().asString(),
                gridToScour.selectionMethodProperty().asString(),
                editModeProperty().asString(),
                gridToScour.indexProperty()).addListener((observable, oldValue, newValue) -> {
            startStop();
        });
    }

    /**
     *
     */
    public void startStop() {
        SelectionMethod userMethod = getUser().getInteraction().getSelectionMethod();
        SelectionMethod gridMethod = gridToScour.getSelectionMethod();

        Boolean overrideSelectionMethod = getUser().getInteraction().isInAssistedMode();
        SelectionMethod deducedMethod = (overrideSelectionMethod) ? gridMethod : userMethod;

        final int size = gridToScour.getGridManager().getOrder().getFirstList().size();
        if (deducedMethod.equals(getSelectionMethod())
                && !inEditMode()
                && gridToScour.isRootGrid()
                && size > 1) {
            if (!inProcess()) {
                removeSelectorButton();
                LOGGER.info("Required method is " + deducedMethod.toString()
                        + ". Starting selector:" + getSelectionMethod());
                addSelectorButton(gridToScour, getSelectorButton());
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
        //  getScourer().resetPosition();
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
            LOGGER.info("Adding Selector button to Sub grid " + gridToScour.getIndex());
            subGrid.addToPane(selectorButton);
        } else {
            LOGGER.fatal("Scanner node's parent is neither a grid nor a subgrid");
        }
    }

    /**
     *
     */
    public void removeSelectorButton() {
        LOGGER.info("Removing selector button from grid: " + gridToScour.getIndex());
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
                SubGrid nestedGrid = (SubGrid) current;
                LOGGER.info("Nested grid selected: " + nestedGrid.getConfigurableGrid().getIndex());
                invokeSubGrid(nestedGrid);
                setExecuted(true);
                stop();
            } else {
                LOGGER.info("Executing selection ");
                current.buttonHandler();
                current.execute();
                setExecuted(true);
                stop();
                if (gridToScour.isRootGrid()) {
                    LOGGER.info("Restarting root grid: " + gridToScour.getIndex());
                    configure();
                }
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
            scourer = new Scourer(gridToScour);
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
