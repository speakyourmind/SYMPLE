/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system;

import org.symfound.controls.ScreenControl;
import org.symfound.controls.user.AnimatedLabel;

/**
 *
 * @author Javed Gangjee
 */
public class VersionLabel extends ScreenControl<AnimatedLabel> {

    /**
     *
     */
    public VersionLabel() {
        super();
    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedLabel();
        setCSS("settings-label", primary);
        primary.textProperty().bindBidirectional(getSession().versionProperty());
        load(primary);
    }
}
