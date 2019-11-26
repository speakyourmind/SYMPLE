/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import org.symfound.controls.SystemControl;
import org.symfound.controls.system.dialog.EditDialog;
import org.symfound.controls.system.dialog.OKCancelDialog;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public abstract class SettingsButtonBase  extends SystemControl{

    public SettingsButtonBase(String CSSClass, String key, String title) {
        super(CSSClass, key, title, "default");
    }
    
    
     @Override
    public void defineButton() {
        setEditable(Boolean.FALSE);
        setControlType(ControlType.SETTING_CONTROL);
        setNavigatePostClick(Boolean.FALSE);

    }
    
    
    /**
     *
     * @return
     */
    @Override
    public OKCancelDialog getDialog() {
        if (settingsDialog == null) {
            settingsDialog = configureEditDialog();
        }
        return settingsDialog;
    }

    public abstract EditDialog configureEditDialog();
    
}
