package org.symfound.controls.system;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.apache.log4j.Logger;
import org.symfound.builder.characteristic.PreferencesManager;
import org.symfound.controls.user.AnimatedButton;
import static org.symfound.main.Main.getSession;
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
        try {
            Preferences.userRoot().node("/org/symfound").removeNode();
        } catch (BackingStoreException ex) {
            java.util.logging.Logger.getLogger(SettingsImportButton.class.getName()).log(Level.SEVERE, null, ex);
        }
        final String fileSelection = getSelectedFile();
        SettingsImporter settingsImporter = new SettingsImporter(fileSelection);
        final Thread thread = new Thread(settingsImporter);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException ex) {
            LOGGER.fatal(ex);
        }
        String masterFile = getUser().getContent().getHomeFolder() + "/Documents/SYMPLE/Settings/Master.xml";
        File file = new File(masterFile);
        if (file.delete()) {
            LOGGER.info("Master file " + masterFile + " deleted successfully");
        } else {
            LOGGER.fatal("Failed to delete master file " + masterFile);
        }
        getSession().shutdown(Boolean.FALSE);

    }

    private String getSelectedFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Settings Source");
        final ExtensionFilter xmlFilter = new ExtensionFilter("XML Files", "*.xml");
        fileChooser.getExtensionFilters().addAll(xmlFilter);
        final UI root = getPrimaryControl().getParentUI();
        File selectedFile = fileChooser.showOpenDialog(root);
        String importedFile = selectedFile.getAbsolutePath();
        return importedFile;
    }

    private List<String> getFileSelection() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Settings Source");
        final ExtensionFilter xmlFilter = new ExtensionFilter("XML Files", "*.xml");
        fileChooser.getExtensionFilters().addAll(xmlFilter);

        final UI root = getPrimaryControl().getParentUI();

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(root);
        List<String> importFiles = new ArrayList<>();
        for (File selectedFile : selectedFiles) {
            if (selectedFile != null) {
                importFiles.add(selectedFile.getAbsolutePath());
            } else {
                throw new NullPointerException("File cannot be null");
            }
        }
        return importFiles;
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
