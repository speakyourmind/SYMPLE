/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.emulation.input.keyboard;

import org.symfound.device.emulation.input.InputEmulator;

/**
 *
 * @author Javed Gangjee
 */
public class KeyboardEmulator extends InputEmulator<KeyboardAutomator, NativeKeyboardListener> {

    /**
     *
     */
    public KeyboardEmulator() {
        automator = new KeyboardAutomator();
        listener = new NativeKeyboardListener();
    }

}
