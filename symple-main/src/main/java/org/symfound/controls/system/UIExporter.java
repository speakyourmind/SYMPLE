/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system;

import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.symfound.builder.settings.PreferencesManager;
import org.symfound.builder.settings.PreferencesTransporter;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public class UIExporter extends PreferencesTransporter {

    /**
     *
     */
    public static final String NAME = UIExporter.class.getName();

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
    public UIExporter(String folder, String fileName, String node) {
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

    public void export(String folder, String fileName, String node) {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass()).node(node);
        try {
            LOGGER.info("Exporting preferences from " + prefs.absolutePath() + " as " + fileName + " in " + folder);
            final String destination = folder + fileName;
            PreferencesManager.exportTo(destination, prefs);
        } catch (BackingStoreException | IOException ex) {
            LOGGER.fatal("Error exporting preferences xml file " + fileName + " to folder " + folder, ex);
        }
    }

}
