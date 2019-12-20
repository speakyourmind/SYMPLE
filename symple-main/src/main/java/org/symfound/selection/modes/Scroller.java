/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.selection.modes;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import org.symfound.builder.user.User;
import org.symfound.builder.user.characteristic.Navigation;
import org.symfound.builder.user.selection.SelectionMethod;
import org.symfound.controls.user.ConfigurableGrid;
import static org.symfound.controls.user.ConfigurableGrid.inEditMode;
import org.symfound.controls.user.SubGrid;
import org.symfound.main.HomeController;
import static org.symfound.main.Main.getSession;
import org.symfound.selection.Curtain;
import org.symfound.selection.Selector;
import static org.symfound.selection.Selector.LOGGER;
import org.symfound.selection.controls.ScrollControl;
import org.symfound.selection.controls.ScrollControlButton;
import static org.symfound.selection.modes.Scanner.LOGGER;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public class Scroller extends Selector {

    public Scroller(SubGrid grid, User user) {
        super(grid, SelectionMethod.SCROLL, user);
    }

    @Override
    public void start() {
        LOGGER.info("Beginning Scroll");
        reset();
        Double scanTime = getUser().getTiming().getScanTime();
        getLoopedEvent().setup(scanTime, (ActionEvent e) -> {
            setRunScroll(Boolean.TRUE);
        });
        getLoopedEvent().play();
    }

    @Override
    public void reset() {
        LOGGER.info("Resetting Scroller");
        setInProcess(false);
        getLoopedEvent().end();
        setInProcess(true);
    }

    @Override
    public void invokeSubGrid(SubGrid currentGrid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Curtain getCurtain() {
        if (curtain == null) {
            curtain = new Curtain(this, "Scroll=left,Scroll Selector=default,Scroll=right") {
                @Override
                public void loadControls(String buttonOrder) {

                }
            };

        }
        return curtain;
    }

    ScrollControlButton leftScroll;
    ScrollControlButton rightScroll;

    @Override
    public void addCurtain(SubGrid subGrid, Curtain curtain) {

        LOGGER.info("Adding curtain to grid "
                + subGrid.getConfigurableGrid().getIndex()
                + " with order " + curtain.getButtonOrder()
                + " for method " + this.getSelectionMethod().toString());

        leftScroll = new ScrollControlButton(this,
                HomeController.getScrollPane(), ScrollControl.LEFT);
        leftScroll.setMaxWidth(getSession().getDisplay().getScreenWidth() / 3);
        GridPane.setRowIndex(leftScroll, 1);
        GridPane.setColumnSpan(leftScroll, 2);
        GridPane.setHalignment(leftScroll, HPos.LEFT);

        rightScroll = new ScrollControlButton(this,
                HomeController.getScrollPane(), ScrollControl.RIGHT);
        GridPane.setRowIndex(rightScroll, 1);
        GridPane.setColumnSpan(rightScroll, 2);
        GridPane.setHalignment(rightScroll, HPos.RIGHT);
        rightScroll.setMaxWidth(getSession().getDisplay().getScreenWidth() / 3);

        final GridPane screenGrid = HomeController.getScreenGrid();
        screenGrid.getChildren().add(leftScroll);
        screenGrid.getChildren().add(rightScroll);
        curtain.toFront();
    }

    /**
     *
     */
    @Override
    public void removeCurtain() {
        LOGGER.info("Removing curtain from grid: " + gridToScour.getConfigurableGrid().getIndex());
        final GridPane screenGrid = HomeController.getScreenGrid();
        final ObservableList<Node> children = screenGrid.getChildren();
        if (children.contains(leftScroll)) {
            children.remove(leftScroll);
        }
        if (children.contains(rightScroll)) {
            children.remove(rightScroll);
        }

    }
    /**
     *
     */
    public BooleanProperty runScroll;

    /**
     *
     * @param value
     */
    public void setRunScroll(Boolean value) {
        runScrollProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isRunScroll() {
        return runScrollProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty runScrollProperty() {
        if (runScroll == null) {
            runScroll = new SimpleBooleanProperty(false);
        }
        return runScroll;
    }

    private static ChangeListener<String> startStopListener;

    public void loadStartStopListener() {
        if (startStopListener == null) {
            startStopListener = (observable, oldValue, newValue) -> {
                startStop();
            };
            final StringExpression concatenatedBindings = Bindings.concat(
                    getUser().getInteraction().assistedModeProperty().asString(),
                    getUser().getInteraction().selectionMethodProperty().asString(),
                    gridToScour.getConfigurableGrid().selectionMethodProperty().asString(),
                    ConfigurableGrid.editModeProperty().asString());
            concatenatedBindings.addListener(startStopListener);

        }
    }

    /**
     *
     */
    @Override
    public void configure() {
        startStop();
        loadStartStopListener();
    }

    @Override
    public void startStop() {
        SelectionMethod userMethod = getUser().getInteraction().getSelectionMethod();
        SelectionMethod gridMethod = gridToScour.getConfigurableGrid().getSelectionMethod();

        Boolean overrideSelectionMethod = getUser().getInteraction().isInAssistedMode();
        SelectionMethod deducedMethod = (overrideSelectionMethod) ? gridMethod : userMethod;

        final GridPane screenGrid = HomeController.getScreenGrid();

        final int size = gridToScour.getConfigurableGrid().getGridManager().getOrder().getFirstList().size();
        if (deducedMethod.equals(getSelectionMethod())
                && !inEditMode()
                && gridToScour.getConfigurableGrid().isRootGrid()
                && size > 1
                && HomeController.getSubGrid().getWidth() > HomeController.getScreenGrid().getWidth()) {
            if (!inProcess()) {
                removeCurtain();
                LOGGER.info("Required method is " + deducedMethod.toString()
                        + ". Starting selector:" + getSelectionMethod());
                addCurtain(HomeController.getSubGrid(), getCurtain());
            }
        } else if (!deducedMethod.equals(getSelectionMethod())
                || inEditMode()
                || size <= 1
                || HomeController.getSubGrid().getWidth() <= HomeController.getScreenGrid().getWidth()) {
            LOGGER.info("Required method is " + deducedMethod.toString()
                    + ". Stopping selector." + getSelectionMethod());
            stop();
        }
    }
}
