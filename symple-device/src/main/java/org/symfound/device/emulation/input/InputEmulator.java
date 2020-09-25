/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.emulation.input;

/**
 *
 * @author Javed Gangjee
 * @param <T>
 * @param <S>
 */
public abstract class InputEmulator<T extends InputAutomator, S extends InputListener> {

    /**
     *
     */
    public T automator;

    /**
     *
     * @return
     */
    public T getAutomator() {
        return automator;
    }

    /**
     *
     */
    public S listener;

    /**
     *
     * @return
     */
    public S getListener() {
        return listener;
    }
}
