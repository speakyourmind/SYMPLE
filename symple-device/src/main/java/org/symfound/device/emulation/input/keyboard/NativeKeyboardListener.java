package org.symfound.device.emulation.input.keyboard;

import org.apache.log4j.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.symfound.device.emulation.input.InputListener;

/**
 *
 * @author Javed Gangjee
 */
public class NativeKeyboardListener extends InputListener implements NativeKeyListener {

    private static final String NAME = NativeKeyboardListener.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     * @param e
     */
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        LOGGER.info("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            unregisterHook();
        }
    }

    /**
     *
     * @param e
     */
    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        LOGGER.info("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    /**
     *
     * @param e
     */
    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        LOGGER.info("Key Typed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
    }

    /**
     *
     */
    @Override
    public void start() {
        GlobalScreen.addNativeKeyListener(new NativeKeyboardListener());
    }

}
