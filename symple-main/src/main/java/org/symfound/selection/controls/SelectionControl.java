/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.selection.controls;

import org.symfound.controls.AppableControl;
import org.symfound.selection.Selector;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public abstract class SelectionControl extends AppableControl {

    public SelectionControl(String CSSClass, String key, String title, String index) {
        super(CSSClass, key, title, index);
    }

}
