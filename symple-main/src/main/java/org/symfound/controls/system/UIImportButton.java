package org.symfound.controls.system;

import org.symfound.builder.settings.PreferencesImporter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.apache.log4j.Logger;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.main.builder.UI;

/**
 *
 * @author Javed Gangjee
 */
public final class UIImportButton extends SettingsManagerControl {

    private static final String NAME = UIImportButton.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public UIImportButton() {
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

        final List<String> fileSelection = getFileSelection();
        for (String file : fileSelection) {
            final Thread thread = new Thread(new PreferencesImporter(file));
            thread.start();
            getExecutor().execute(thread);
        }
        deleteMasterFile();
        getSession().exit(Boolean.FALSE);
    }

    private static ThreadPoolExecutor executor;

    public static ThreadPoolExecutor getExecutor() {
        if (executor == null) {
            executor = new ThreadPoolExecutor(3, 8, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        }
        return executor;
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
        primary = new AnimatedButton("");
        primary.setWrapText(true);
        load(primary);
        setCSS(cssClass, primary);
    }

}
