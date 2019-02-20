/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public abstract class SystemControl extends AppableControl {

    public SystemControl(String CSSClass, String key, String title, String index) {
        super(CSSClass, key, title, index);
        initialize();
    }

    private void initialize() {
        setEditable(Boolean.FALSE);
        setControlType(ControlType.SETTING_CONTROL);
    }

}