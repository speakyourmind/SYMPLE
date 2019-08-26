package org.symfound.controls.system;

import org.symfound.builder.settings.PreferencesExporter;
import java.io.File;
import javafx.stage.DirectoryChooser;
import org.apache.log4j.Logger;
import org.symfound.controls.user.AnimatedButton;
import static org.symfound.main.FullSession.getSettingsFileName;

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
        String fileName = getSettingsFileName("All");
        PreferencesExporter settingsExporter = new PreferencesExporter(folder, fileName, "/org/symfound");
        Thread thread = new Thread(settingsExporter);
        try {
            thread.start();
            thread.join();
        } catch (InterruptedException ex) {
            LOGGER.warn("Unable to backup settings file to " + folder, ex);
        }
    }

    private String getFolderSelection() {
        String folder = null;
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Export Settings Destination");
        final String homeFolder = getUser().getContent().getHomeFolder();
        final File file = new File(homeFolder);
        directoryChooser.setInitialDirectory(file);
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
        primary = new AnimatedButton("");
        primary.setWrapText(true);
        load(primary);
        setCSS(cssClass, primary);
    }

}
