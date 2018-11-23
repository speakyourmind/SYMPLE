/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.emulation.input;

import org.apache.log4j.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

/**
 *
 * @author Javed Gangjee
 */
public abstract class InputListener {

    private static final String NAME = InputListener.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public abstract void start();

    /**
     *
     */
    public static void registerHook() {
        try {
            if (!GlobalScreen.isNativeHookRegistered()) {
                GlobalScreen.registerNativeHook();
                LOGGER.info("Native hook registered");
            } else {
                LOGGER.warn("Native hook already registered");
            }
        } catch (NativeHookException | IllegalMonitorStateException ex) {
            LOGGER.fatal("Unable to register native hook", ex);
        }
    }

    /**
     *
     */
    public static void stop() {
        unregisterHook();
    }

    /**
     *
     */
    public static void unregisterHook() {
        try {
            if (!GlobalScreen.isNativeHookRegistered()) {
                GlobalScreen.unregisterNativeHook();
                LOGGER.info("Native hook unregistered");
            } else{
                LOGGER.warn("Native hook not registered. Unregisteration not attempted.");        
            }
        } catch (NativeHookException ex) {
            LOGGER.fatal("Unable to unregister native hook", ex);
        }
    }
}
