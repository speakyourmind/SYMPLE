/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.apache.log4j.Logger;
import org.symfound.controls.RunnableControl;
import org.symfound.device.Device;
import org.symfound.device.emulation.input.keyboard.NativeKeyboardListener;
import org.symfound.device.emulation.input.mouse.NativeMouseListener;
import org.symfound.device.processing.Processor;

/**
 *
 * @author Javed Gangjee
 */
public class MacroRecordButton extends RunnableControl {

    /**
     *
     */
    public static final String NAME = MacroRecordButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    private NativeKeyboardListener keyboardListener;
    private NativeMouseListener mouseListener;

    /**
     *
     */
    public MacroRecordButton() {
        super("record-button");
        initialize();
    }

    private void initialize() {
        recordProperty().addListener((observable, oldValue, newValue) -> {
            Device current = getSession().getDeviceManager().getCurrent();
            Processor processor = current.getProcessor();
            processor.getEmulationManager().listen(newValue);
        });
    }

    @Override
    public void run() {
        toggleRecord();
    }

    private static final Boolean DEFAULT_RECORD = Boolean.FALSE;
    private BooleanProperty record;

    /**
     *
     * @param value
     */
    public void setRecord(Boolean value) {
        recordProperty().setValue(value);
    }

    /**
     *
     */
    public void toggleRecord() {
        setRecord(!record());
    }

    /**
     *
     * @return
     */
    public Boolean record() {
        return recordProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty recordProperty() {
        if (record == null) {
            record = new SimpleBooleanProperty(DEFAULT_RECORD);
        }
        return record;
    }

}
