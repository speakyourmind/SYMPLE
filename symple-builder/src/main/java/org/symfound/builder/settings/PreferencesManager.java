/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.settings;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;

/**
 *
 * @author Javed Gangjee
 */
public class PreferencesManager {

    private static final String NAME = PreferencesManager.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     * @param source
     */
    public static void importPreferences(String source) {
        try {
            LOGGER.info("Importing preferences from " + source);
            InputStream im = new FileInputStream(new File(source));
            Preferences.importPreferences(im);
           
        } catch (IOException | InvalidPreferencesFormatException ex) {
            LOGGER.fatal("Unable to load preferences from " + source, ex);
        }
    }

    
    /**
     *
     * @param sources
     */
    public static void importFrom(List<String> sources) {
        sources.stream().forEach((String source) -> {
            importPreferences(source);
        });
    }

    /**
     *
     * @param destination
     * @param preferences
     * @throws FileNotFoundException
     * @throws BackingStoreException
     * @throws IOException
     */
    public static void exportTo(String destination, Preferences preferences) throws FileNotFoundException, BackingStoreException, IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(destination);
        OutputStream osTree = new BufferedOutputStream(fileOutputStream);
        preferences.exportSubtree(osTree);
    }
}
