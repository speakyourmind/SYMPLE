/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.emulation.reaction;

import javafx.scene.Node;
import org.symfound.builder.user.selection.SelectionMethod;
import org.symfound.device.selection.SelectionSource;

/**
 *
 * @author Javed Gangjee
 */
public abstract class PhysicalReaction extends Reaction {

    /**
     *
     * @param method
     */
    public PhysicalReaction(SelectionMethod method) {
        super(SelectionSource.PHYSICAL, method);
    }

    /**
     *
     * @param node
     */
    public abstract void configure(Node node);

}
