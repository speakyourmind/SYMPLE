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
import javafx.event.ActionEvent;
import org.apache.log4j.Logger;
import org.symfound.builder.user.User;
import org.symfound.builder.user.characteristic.Navigation;
import org.symfound.builder.user.selection.SelectionMethod;
import org.symfound.controls.user.ConfigurableGrid;
import org.symfound.controls.user.SubGrid;
import org.symfound.main.HomeController;
import org.symfound.selection.Selector;
import org.symfound.selection.Curtain;

/**
 *
 * @author Javed Gangjee
 */
public class Scanner extends Selector {

    private static final String NAME = Scanner.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     * @param grid
     * @param user
     */
    public Scanner(SubGrid grid, User user) {
        super(grid, SelectionMethod.SCAN, user);
    }

    /**
     *
     */
    @Override
    public void configure() {
        startStop();
        loadStartStopListener();
    }
    /**
     *
     */
    @Override
    public void start() {
        LOGGER.info("Beginning Scan");
        reset();
        Double scanTime = getUser().getTiming().getScanTime();
        getLoopedEvent().setup(scanTime, (ActionEvent e) -> {
            loopEvent();
        });
        getLoopedEvent().play();
    }

    /**
     *
     */
    public void loopEvent() {
        getScourer().clearHighlight();
        getScourer().resetPosition();
        final Navigation navigation = getUser().getNavigation();
        getScourer().highlightNext(navigation);
    }

    /**
     *
     */
    @Override
    public void reset() {
        getLoopedEvent().end();
        setInProcess(false);
        getScourer().set(-1,-1);
        getScourer().clearHighlight();
        setInProcess(true);
    }

    /**
     *
     * @param currentGrid
     */
    @Override
    public void invokeSubGrid(SubGrid currentGrid) {
        Scanner scanner = currentGrid.getScanner();
        Curtain button = scanner.getCurtain();
        addCurtain(HomeController.getSubGrid(), button);
        scanner.start();
        button.executeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                configure();
            }
        });
    }

    /**
     *
     */
    @Override
    public void onSelected(){
        getScourer().resetPosition();
        super.onSelected();
    }
    /**
     *
     */
    public BooleanProperty autoStart;

    /**
     *
     * @param value
     */
    public void setAutoStart(Boolean value) {
        autoStartProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isAutoStart() {
        return autoStartProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty autoStartProperty() {
        if (autoStart == null) {
            autoStart = new SimpleBooleanProperty(false);
        }
        return autoStart;
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
}
