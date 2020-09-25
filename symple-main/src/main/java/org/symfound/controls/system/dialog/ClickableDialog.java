/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system.dialog;

/**
 *
 * @author Javed Gangjee
 */
public abstract class ClickableDialog extends ScreenDialog {

    /**
     *
     * @param titleText
     * @param captionText
     */
    public ClickableDialog(String titleText, String captionText) {
        super(titleText, captionText);
    }

    /**
     *
     */
    public abstract void onClick();
}
