/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.settings;

import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public class PreferencesExporter extends PreferencesTransporter {

    /**
     *
     */
    public static final String NAME = PreferencesExporter.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);
    private final String folder;
    private final String fileName;
    private final String node;

    /**
     *
     * @param folder
     * @param fileName
     */
    public PreferencesExporter(String folder, String fileName, String node) {
        this.fileName = fileName;
        this.folder = folder;
        this.node = node;
    }

    /**
     *
     * @param folder
     */
    /*  public SettingsExporter(String folder) {
        this.folder = folder;
    }*/
    @Override
    public void run() {
        export(folder, fileName, node);
    }

    public static void export(String folder, String fileName, String node) {
        Preferences prefs = Preferences.userRoot().node(node);
        try {
            LOGGER.debug("Exporting preferences as " + fileName + " in " + folder);
            final String destination = folder + fileName;
            PreferencesManager.exportTo(destination, prefs);
            
        } catch (BackingStoreException | IOException ex) {
            LOGGER.fatal("Error exporting preferences xml file " + fileName + " to folder " + folder, ex);
        }
    }

}
