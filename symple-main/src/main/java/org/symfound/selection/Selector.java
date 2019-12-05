/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.selection;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;
import org.symfound.builder.user.User;
import org.symfound.builder.user.selection.Chooser;
import org.symfound.builder.user.selection.SelectionMethod;
import org.symfound.controls.RunnableControl;
import static org.symfound.controls.user.ConfigurableGrid.editModeProperty;
import static org.symfound.controls.user.ConfigurableGrid.inEditMode;
import org.symfound.controls.user.SubGrid;
import org.symfound.main.HomeController;
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
    public final SubGrid gridToScour;
    private final User user;

    /**
     *
     * @return
     */
    public User getUser() {
        return user;
    }

    public Selector(SubGrid subGrid, SelectionMethod method, User user) {
        this.gridToScour = subGrid;
        this.method = method;
        this.user = user;
        
    }


    /**
     *
     */
    public void startStop() {
        SelectionMethod userMethod = getUser().getInteraction().getSelectionMethod();
        SelectionMethod gridMethod = gridToScour.getConfigurableGrid().getSelectionMethod();

        Boolean overrideSelectionMethod = getUser().getInteraction().isInAssistedMode();
        SelectionMethod deducedMethod = (overrideSelectionMethod) ? gridMethod : userMethod;

        final int size = gridToScour.getConfigurableGrid().getGridManager().getOrder().getFirstList().size();
        if (deducedMethod.equals(getSelectionMethod())
                && !inEditMode()
                && gridToScour.getConfigurableGrid().isRootGrid()
                && size > 1) {
            if (!inProcess()) {
                removeCurtain();
                LOGGER.info("Required method is " + deducedMethod.toString()
                        + ". Starting selector:" + getSelectionMethod());

                addCurtain(HomeController.getSubGrid(), getCurtain());
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
    public abstract void configure();

    /**
     *
     */
    public void stop() {
        LOGGER.info("Stopping selector " + this.getSelectionMethod());
        removeCurtain();
        getLoopedEvent().end();
        setInProcess(false);
        //  getScourer().resetPosition();
        getScourer().clearHighlight();
    }

    /**
     *
     */
    public abstract void reset();

    public Curtain curtain;

    /**
     *
     * @return
     */
    public Curtain getCurtain() {
        if (curtain == null) {
            curtain = new Curtain(this, "Selector=default");
            GridPane.setRowIndex(curtain, 1);
            GridPane.setColumnSpan(curtain, 2);
        }
        return curtain;
    }

    /**
     *
     */
    public void onSelected() {
        runSelection();
    }

    public void addCurtain(SubGrid subGrid, Curtain curtain) {
        LOGGER.info("Adding curtain to grid "
                + subGrid.getConfigurableGrid().getIndex()
                + " with order " + curtain.getButtonOrder()
                + " for method " + this.getSelectionMethod().toString());
        HomeController.getScreenGrid().getChildren().add(curtain);
        curtain.toFront();
    }

    /**
     *
     */
    public void removeCurtain() {
        LOGGER.info("Removing curtain from grid: " + gridToScour.getConfigurableGrid().getIndex());
        HomeController.getScreenGrid().getChildren().remove(getCurtain());
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
                if (gridToScour.getConfigurableGrid().isRootGrid()) {
                    LOGGER.info("Restarting root grid: " + gridToScour.getConfigurableGrid().getIndex());
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
