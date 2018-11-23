/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.voice;

import java.util.prefs.Preferences;
import org.symfound.controls.user.SubGrid;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public class SpeakGrid extends SubGrid {

    /**
     *
     */
    public static final String KEY = "Speak Grid";

    public SpeakGrid(String index) {
        super(KEY,index);
    }
    
    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends SpeakGrid> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }
}
