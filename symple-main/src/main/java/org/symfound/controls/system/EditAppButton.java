/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system;

import org.apache.log4j.Logger;
import org.symfound.controls.ConfirmableControl;
import org.symfound.controls.system.dialog.EditDialog;
import org.symfound.controls.system.dialog.OKCancelDialog;
import org.symfound.controls.user.AnimatedButton;

/**
 *
 * @author Javed Gangjee
 */
public class EditAppButton extends ConfirmableControl {

    private static final String NAME = EditAppButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Edit App";

    /**
     *
     */
    public EditDialog dialog;

    /**
     *
     * @param dialog
     */
    public EditAppButton(EditDialog dialog) {
        super("toolbar-edit-app");
        this.dialog = dialog;
        initialize();
    }

    private void initialize() {
        setControlType(ControlType.SETTING_CONTROL);
        primary = new AnimatedButton("");
        primary.setWrapText(true);
        load(primary);
        setCSS(cssClass, primary);
        setSelection(primary);
    }

    @Override
    public void run() {
        LOGGER.info("Editing app");
    }

    /**
     *
     * @return
     */
    @Override
    public OKCancelDialog getDialog() {
        if (settingsDialog == null) {
            settingsDialog = dialog;    
            settingsDialog.buildDialog();    
        }
        return settingsDialog;
    }

}
