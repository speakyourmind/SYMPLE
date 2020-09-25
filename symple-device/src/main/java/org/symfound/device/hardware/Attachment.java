/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.hardware;

import org.symfound.builder.user.feature.Feature;

/**
 *
 * @author Javed Gangjee
 * @param <T>
 */
public abstract class Attachment<T extends Feature> extends Feature {

    /**
     *
     */
    public static final String ATTACHMENT_FEATURE = "Attachment";

    /**
     *
     */
    public T t;

    /**
     *
     * @param t
     */
    public Attachment(T t) {
        super(ATTACHMENT_FEATURE);
        this.t = t;
    }

    /**
     *
     * @return
     */
    public T getAssignedFeature() {
        return t;
    }
}
