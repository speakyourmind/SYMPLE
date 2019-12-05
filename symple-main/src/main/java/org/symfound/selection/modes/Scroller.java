/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.selection.modes;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import org.symfound.builder.user.User;
import org.symfound.builder.user.characteristic.Navigation;
import org.symfound.builder.user.selection.SelectionMethod;
import org.symfound.controls.user.SubGrid;
import org.symfound.selection.Curtain;
import org.symfound.selection.Selector;
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
            curtain = new Curtain(this, "Scroll=left,Blank=default,Scroll=right");
        }
        return curtain;
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
    
}