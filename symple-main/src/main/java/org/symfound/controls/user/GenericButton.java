/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.util.prefs.Preferences;
import org.symfound.controls.AppableControl;

/**
 *
 * @author Javed Gangjee
 */
public class GenericButton extends AppableControl {

    /**
     *
     */
    public static final String KEY = "None";

    /**
     *
     * @param index
     */
    public GenericButton(String index) {
        super("button", KEY, index, index);
        initialize();
    }

    private void initialize() {
        configureStyle();
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends GenericButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

}
