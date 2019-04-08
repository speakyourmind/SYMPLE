/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.selection.modes;

import javafx.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symfound.builder.user.User;
import org.symfound.builder.user.characteristic.Navigation;
import org.symfound.builder.user.selection.SelectionMethod;
import org.symfound.controls.RunnableControl;
import org.symfound.controls.user.ConfigurableGrid;
import org.symfound.controls.user.SubGrid;
import org.symfound.selection.Selector;

/**
 *
 * @author Javed Gangjee
 */
public class Stepper extends Selector {

    private static final String NAME = Stepper.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     * @param grid
     * @param user
     */
    public Stepper(ConfigurableGrid grid, User user) {
        super(grid, SelectionMethod.STEP, user);
    }

    /**
     *
     */
    @Override
    public void start() {
        getScourer().set(-1, -1);
        // addSelectorButton();
        LOGGER.info("Attempting to start stepper");
        reset();
        final Navigation navigation = getUser().getNavigation();
        if (navigation.onFirstClick()) {
            getScourer().highlightNext(navigation);
            reset();
        }
    }

    private void setupLoop(Double stepTime) {
        LOGGER.info("Setting up looped event");
        getLoopedEvent().setup(stepTime, (ActionEvent e) -> {
            getScourer().resetPosition();
            runSelection();
        });
        LOGGER.debug("Starting looped event");
        getLoopedEvent().play();
    }

    /**
     *
     */
    @Override
    public void reset() {
        LOGGER.debug("Ending looped event");
        getLoopedEvent().end();
        setInProcess(false);
        LOGGER.info("Resetting stepper");
        Double stepTime = getUser().getTiming().getStepTime();
        setupLoop(stepTime);
        setInProcess(true);
    }

    /**
     *
     */
    @Override
    public void onSelected() {
        getScourer().clearHighlight();
        getScourer().resetPosition();
        final Navigation navigation = getUser().getNavigation();
        getScourer().highlightNext(navigation);
        reset();

    }

    /**
     *
     * @param nestedGrid
     */
    @Override
    public void invokeSubGrid(SubGrid nestedGrid) {
        
    //    stop();
      /*  Stepper nestedStepper = nestedGrid.getConfigurableGrid().getStepper();
        RunnableControl selector = nestedStepper.getSelectorButton();
        selector.setStyle("-fx-background-color:-fx-red;-fx-opacity:0.9;");
        addSelectorButton(grid, selector);
        LOGGER.info("Starting stepper in nested grid");
        nestedStepper.start();
*/
        /*      subStepper.executeProperty().addListener((observable, oldValue, newValue) -> {
            if (grid.isRootGrid()) {
                LOGGER.info("Restarting root grid");
                configureStartStop();
            }
        });*/
    }

}
