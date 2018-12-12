/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.symfound.builder.characteristic.PreferencesManager;
import org.symfound.main.Main;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public class SettingsExporter extends SettingsTransporter {

    /**
     *
     */
    public static final String NAME = SettingsExporter.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);
    private final String folder;
    private String fileName;

    public SettingsExporter(String folder, String fileName) {
        this.fileName = fileName;
        this.folder = folder;
    }

    public SettingsExporter(String folder) {
        this.folder = folder;
    }

    @Override
    public void run() {
        DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        Preferences allprefs = Preferences.userRoot().node("/org/symfound");
        if (fileName == null) {
            fileName = "/" + Main.getSession().getUser().getProfile().getFullName() + " Settings " + dateFormat.format(date) + ".xml";
        }
        try {
            LOGGER.debug("Exporting preferences as " + fileName + " in " + folder);
            final String destination = folder + fileName;
            PreferencesManager.exportTo(destination, allprefs);
        } catch (BackingStoreException | IOException ex) {
            LOGGER.fatal("Error exporting preferences xml file " + fileName + " to folder " + folder, ex);
        }
    }

}
