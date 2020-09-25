package org.symfound.controls.system;

import org.symfound.builder.settings.PreferencesImporter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.apache.log4j.Logger;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.main.builder.UI;

/**
 *
 * @author Javed Gangjee
 */
public final class SettingsImportButton extends SettingsManagerControl {

    private static final String NAME = SettingsImportButton.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public SettingsImportButton() {
        super();
        setSelection(primary);
        initTitleText = "Import Settings";
        initCaptionText = "Warning! This will override all customization. "
                + "Application will shutdown after import.";
        setConfirmable(true);
    }

    /**
     *
     */
    @Override
    public void run() {
        final String fileSelection = getSelectedFile();
        if (!fileSelection.isEmpty()) {
            try {
                Preferences.userRoot().node("/org/symfound").removeNode();
            } catch (BackingStoreException ex) {
                LOGGER.fatal(ex);
            }
            PreferencesImporter settingsImporter = new PreferencesImporter(fileSelection);
            final Thread thread = new Thread(settingsImporter);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException ex) {
                LOGGER.fatal(ex);
            }
            deleteMasterFile();
            getSession().exit(Boolean.FALSE);
        } else {
            LOGGER.warn("No valid file selection made");
        }

    }

    private String getSelectedFile() {
        String importedFile = "";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Settings Source");
        final ExtensionFilter xmlFilter = new ExtensionFilter("XML Files", "*.xml");
        fileChooser.getExtensionFilters().addAll(xmlFilter);
        final UI root = getPrimaryControl().getParentUI();
        File selectedFile = fileChooser.showOpenDialog(root);
        if (selectedFile != null) {
            importedFile = selectedFile.getAbsolutePath();
        }
        return importedFile;
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
