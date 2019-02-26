/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.symfound.builder.characteristic.PreferencesManager;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public class SettingsImporter extends SettingsTransporter {

    /**
     *
     */
    public static final String NAME = SettingsImporter.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String ROOT_NODE = "/org/symfound";

    /**
     *
     */
    public String fileSelection;

    /**
     *
     * @param fileSelection
     */
    public SettingsImporter(String fileSelection) {
        this.fileSelection = fileSelection;

    }

    @Override
    public void run() {
        try {
         //   Preferences.userRoot().node(ROOT_NODE).removeNode();
           Preferences.userRoot().clear();
            PreferencesManager.importPreferences(fileSelection);
            Preferences.userRoot().node(ROOT_NODE).flush();

        } catch (BackingStoreException ex) {
            LOGGER.fatal(ex);
        }

    }
}
