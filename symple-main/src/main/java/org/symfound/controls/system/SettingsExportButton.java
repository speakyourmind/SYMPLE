package org.symfound.controls.system;

import java.io.File;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.stage.DirectoryChooser;
import org.apache.log4j.Logger;
import org.symfound.builder.characteristic.PreferencesManager;
import org.symfound.controls.user.AnimatedButton;

/**
 *
 * @author Javed Gangjee
 */
public final class SettingsExportButton extends SettingsManagerControl {

    /**
     *
     */
    public static final String NAME = SettingsExportButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public SettingsExportButton() {
        super();
        setSelection(primary);
        setConfirmable(false);
    }

    /**
     *
     */
    @Override
    public void run() {
        String folder = getFolderSelection();
        try {
            Preferences allprefs = Preferences.userRoot().node("/org/symfound");
            final String fileName = "/" + getUser().getProfile().getFullName() + " All Settings.xml";
            LOGGER.debug("Exporting preferences to " + fileName);
            final String destination = folder + fileName;
            PreferencesManager.exportTo(destination, allprefs);
            } catch (BackingStoreException | IOException ex) {
           LOGGER.fatal("Error exporting preferences xml file", ex);
            }
    }

    private String getFolderSelection() {
        String folder = null;
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Export Settings Destination");
        directoryChooser.setInitialDirectory(new File(getUser().getContent().getHomeFolder()));
        File directory = directoryChooser.showDialog(getPrimaryControl().getParentUI());
        if (directory != null) {
            folder = directory.getAbsolutePath();
        } else {
            throw new NullPointerException("Folder cannot be null");
        }
        return folder;
    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton();
        primary.setWrapText(true);
        load(primary);
        setCSS(cssClass, primary);
    }

}
