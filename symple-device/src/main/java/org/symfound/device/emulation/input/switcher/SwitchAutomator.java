/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.emulation.input.switcher;

import java.awt.AWTException;
import org.symfound.device.emulation.input.mouse.MouseAutomator;

/**
 *
 * @author Javed Gangjee
 */
public class SwitchAutomator extends MouseAutomator {

    /**
     *
     * @throws AWTException
     */
    public SwitchAutomator() throws AWTException {
        super();
    }

    /**
     *
     */
    public void latch() {
        // Simulate mouse press for the given button type
        mousePress(LEFT_BUTTON);
        // Implement a short delay in case another click is started
        delay(SHORT_CLICK_DELAY);
    }

    /**
     *
     */
    public void unlatch() {
        // Simulate mouse press for the given button type
        mouseRelease(LEFT_BUTTON);
        // Implement a short delay in case another click is started
        delay(SHORT_CLICK_DELAY);
    }

}
