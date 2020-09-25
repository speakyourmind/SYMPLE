/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import org.symfound.media.Playlistable;
import javafx.scene.media.MediaView;
import org.symfound.builder.user.User;
import org.symfound.builder.user.selection.SelectionMethod;
import org.symfound.device.hardware.Hardware;
import org.symfound.main.FullSession;
import org.symfound.main.Main;
import org.symfound.tools.timing.Hold;

/**
 *
 * @author Javed Gangjee
 */
public abstract class AnimatedMediaView extends MediaView implements Playlistable {

    /**
     *
     */
    @Override
    public void pause() {
        if (getMediaPlayer() != null) {
            getMediaPlayer().pause();
        }
    }

    private Hold hold;

    /**
     *
     * @return
     */
    public Hold getHold() {
        if (hold == null) {
            hold = new Hold();
        }
        return hold;
    }

    /**
     *
     */
    public void addHold() {
        FullSession session = Main.getSession();
        User user = session.getUser();
        if (user.getInteraction().getSelectionMethod().equals(SelectionMethod.SWITCH)
                || user.getInteraction().getSelectionMethod().equals(SelectionMethod.SCAN)
                || user.getInteraction().getSelectionMethod().equals(SelectionMethod.STEP)) {
            Hardware hardware = session.getDeviceManager().getCurrent().getHardware();
            getHold().add(hardware.getSelectability().getPostSelectTime());
        }
    }

}
