/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.reaction;

import org.symfound.tools.selection.SelectionMethod;
import org.symfound.tools.selection.SelectionSource;

/**
 *
 * @author Javed Gangjee
 */
public abstract class EmulatedReaction extends Reaction {

    /**
     *
     * @param method
     */
    public EmulatedReaction(SelectionMethod method) {
        super(SelectionSource.EMULATED, method);
    }

    /**
     *
     */
    public abstract void configure();
}
