/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.emulation.reaction;

import org.symfound.builder.user.selection.SelectionMethod;
import org.symfound.device.selection.SelectionSource;

/**
 *
 * @author Javed Gangjee
 */
public abstract class Reaction {

    /**
     *
     */
    public SelectionSource source;

    /**
     *
     */
    public SelectionMethod method; 

    /**
     *
     * @param source
     * @param method
     */
    public Reaction(SelectionSource source, SelectionMethod method) {
        this.source = source;
        this.method = method;
    }

}
