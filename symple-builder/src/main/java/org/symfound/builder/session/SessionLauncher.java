/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.session;

/**
 *
 * @author Javed Gangjee
 * @param <T>
 */
public class SessionLauncher<T extends Session> {

    private T t;

    /**
     *
     * @param t
     */
    public SessionLauncher(T t) {
        this.t = t;
    }

    /**
     *
     */
    public void launch() {
        t.start();
    }

    /**
     *
     * @return
     */
    public T getSession() {
        return t;
    }
}
