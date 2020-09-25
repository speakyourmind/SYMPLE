/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.emulation.input.switcher;

import java.awt.AWTException;
import org.apache.log4j.Logger;
import org.symfound.device.emulation.input.InputEmulator;
import org.symfound.device.hardware.Hardware;

/**
 *
 * @author Javed Gangjee
 */
public class SwitchEmulator extends InputEmulator<SwitchAutomator, SwitchListener> {

    private static final String NAME = SwitchEmulator.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     * @param hardware
     */
    public SwitchEmulator(Hardware hardware) {
        try {
            automator = new SwitchAutomator();
        } catch (AWTException ex) {
            LOGGER.fatal(ex);
        }
        
        listener = new SwitchListener(hardware);
    }
}
