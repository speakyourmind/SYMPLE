/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.log4j.Logger;
import org.comtel2000.keyboard.control.KeyButton;
import org.comtel2000.keyboard.event.KeyButtonEvent;
import org.symfound.app.DesktopController;
import org.symfound.builder.user.characteristic.Typing;
import org.symfound.controls.ScreenControl;
import org.symfound.device.emulation.input.keyboard.ActionKeyCode;
import org.symfound.device.emulation.input.keyboard.KeyboardAutomator;
import org.symfound.device.emulation.input.keyboard.KeyboardEmulator;
import org.symfound.device.processing.Processor;

/**
 *
 * @author Javed Gangjee
 */
public class ExternalKeyButton extends ScreenControl<KeyButton> {

    private static final String NAME = ExternalKeyButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public ExternalKeyButton() {
        super();
        initialize();
    }

    private void initialize() {
        Processor processor = getSession().getDeviceManager().getCurrent().getProcessor();
        final KeyboardEmulator keyboard = processor.getEmulationManager().getKeyboard();
        KeyboardAutomator keyboardAutomator = keyboard.getAutomator();
        getPrimaryControl().setOnShortPressed((KeyButtonEvent e) -> {
            if (getActionKey() > 0) {
                Typing typing = getUser().getTyping();
                typing.setActiveText(typing.getActiveText().concat(getText()));
            }
            DesktopController.getDesktopExecutor().execute(() -> {
                keyboardAutomator.handle(e);
            });
        });
    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {
        primary = new KeyButton("", 200); // Delay only constructor not available.
        load(primary);
        setCSS("type-button-small", primary);
        primary.setKeyCode(getActionKey());
        actionKeyProperty().addListener((observable, oldValue, newValue) -> {
            primary.setKeyCode(newValue.intValue());
        });

    }

    /**
     *
     */
    public static final int DEFAULT_KEY_CODE = ActionKeyCode.UNASSIGNED;

    /**
     *
     */
    public IntegerProperty actionKey;

    /**
     *
     * @param value
     */
    public void setActionKey(int value) {
        actionKeyProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public int getActionKey() {
        return actionKeyProperty().getValue();
    }

    /**
     *
     * @return
     */
    public IntegerProperty actionKeyProperty() {
        if (actionKey == null) {
            actionKey = new SimpleIntegerProperty(DEFAULT_KEY_CODE);
        }
        return actionKey;
    }

}
