/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.media;

/**
 *
 * @author Javed Gangjee
 */
public interface Playlistable {

    /**
     *
     */
    public void stop();

    /**
     *
     * @param autoNext
     */
    public void play(Boolean autoNext);

    /**
     *
     */
    public void pause();

    /**
     *
     * @param autoNext
     */
    public void next(Boolean autoNext);

    /**
     *
     * @param autoNext
     */
    public void previous(Boolean autoNext);
}
