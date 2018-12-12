package org.symfound.controls.user;

import java.io.IOException;
import java.util.logging.Level;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.symfound.builder.characteristic.PreferencesManager;
import org.symfound.controls.AppableControl;
import static org.symfound.controls.system.SettingsExportButton.LOGGER;

/**
 *
 * @author Javed Gangjee
 */
public final class ExitButton extends AppableControl {

    /**
     *
     */
    public static final String NAME = ExitButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);
    
    private static final boolean BACKUP_ON_EXIT = Boolean.TRUE;

    /**
     *
     */
    public static final String KEY = "Exit";

    /**
     *
     */
    public static final String DEFAULT_TITLE = "Exit";

    /**
     *
     */
    public ExitButton() {
        super("exit-button", KEY, DEFAULT_TITLE, "default");
        initTitleText = "Exit";
        initCaptionText = "Are you sure you want to close the program?";
    }

    @Override
    public void run() {
    /*    LOGGER.info("Backing up current settings");
        String folder = getUser().getContent().getHomeFolder() + "/Documents/SYMPLE/Settings/";
        Preferences allprefs = Preferences.userRoot().node("/org/symfound");
        final String fileName = "\\" + getUser().getProfile().getFullName() + " All Settings.xml";
        LOGGER.debug("Exporting preferences to " + fileName);
        final String destination = folder + fileName;
        try {
            PreferencesManager.exportTo(destination, allprefs);
        } catch (BackingStoreException ex) {
            java.util.logging.Logger.getLogger(ExitButton.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ExitButton.class.getName()).log(Level.SEVERE, null, ex);
        }*/

        LOGGER.info("Exiting the program");
        getSession() .shutdown(BACKUP_ON_EXIT);
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(this.getClass()).node(KEY.toLowerCase());
        }
        return preferences;
    }
}
