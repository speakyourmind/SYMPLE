/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.selection.controls;

import java.util.prefs.Preferences;
import org.symfound.controls.AppableControl;
import org.symfound.selection.Selector;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public class SelectorButton extends SelectionControl {

    public static final String KEY = "Selector";

    private final Selector selector;

    public SelectorButton(Selector selector) {
        super("selector", KEY, "", "default");
        this.selector=selector;
    }

    public Selector getSelector(){
        return selector;
    }
    @Override
    public void run() {
        LOGGER.info("Selector button clicked");
        if (!getSelector().inProcess()) {
            getSelector().start();
        } else {
            getSelector().onSelected();
        }
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            Class<? extends SelectorButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(KEY);
        }
        return preferences;
    }

}
