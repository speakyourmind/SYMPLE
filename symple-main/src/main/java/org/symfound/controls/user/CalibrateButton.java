/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import org.symfound.controls.RunnableControl;

/**
 *
 * @author Javed Gangjee
 */
public class CalibrateButton extends RunnableControl {

    /**
     *
     */
    public CalibrateButton() {
        super("home-button");
    }

    @Override
    public void run() {
        getPrimaryControl().getParentUI().close();
        //    uiCalibration.open();
        getSession().setPlaying(false);
    }

}
