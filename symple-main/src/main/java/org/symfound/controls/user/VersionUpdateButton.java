/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.util.prefs.Preferences;
import org.symfound.comm.file.PathWriter;
import org.symfound.controls.AppableControl;
import org.symfound.builder.settings.PreferencesExporter;
import org.symfound.controls.system.dialog.OKDialog;
import org.symfound.controls.system.dialog.ScreenPopup;
import org.symfound.main.FullSession;
import static org.symfound.main.Main.getVersionManager;

/**
 *
 * @author Javed Gangjee
 */
public class VersionUpdateButton extends AppableControl {

    /**
     *
     */
    public static final String KEY = "Update";

    /**
     *
     */
    public static final String DEFAULT_TITLE = "Update";

    /**
     *
     */
    public VersionUpdateButton() {
        super("update-version-button", KEY, DEFAULT_TITLE, "default");
        getPrimaryControl().visibleProperty().bind(getVersionManager().needsUpdateProperty());
        initTitleText = "Update";
        initCaptionText = "The program will download and automatically install the new version.";
        initialize();
    }

    private void initialize() {
        setSpeakText("Updating SYMPLE.The program will download and automatically install the new version.");
    }

    @Override
    public void run() {

        String backupFolder = getUser().getContent().getHomeFolder() + "/Documents/SYMPLE/Settings/Backup";
        PathWriter backupPathWriter = new PathWriter(backupFolder);
        backupPathWriter.file.mkdirs();

        PreferencesExporter backupSettingsExporter = new PreferencesExporter(backupFolder, FullSession.getSettingsFileName("All"),"/org/symfound");
        LOGGER.info("Backing up settings to folder: " + backupFolder);
        Thread backupThread = new Thread(backupSettingsExporter);
        try {
            backupThread.start();
            backupThread.join();
        } catch (InterruptedException ex) {
            LOGGER.warn("Unable to backup settings file to " + backupFolder, ex);
        }

        getVersionManager().update();

        getParentPane().getChildren().add(getUpdaterPopup());
        final Double selectionTime = getSession().getUser().getInteraction().getSelectionTime();
        getUpdaterDialog().animate().startScale(selectionTime, 0.8, 1.0);

        getVersionManager().msiDownloader.getTracker().beginProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {

            }
        });
    }

    ScreenPopup updaterPopup;

    /**
     *
     * @return
     */
    public ScreenPopup<UpdaterDialog> getUpdaterPopup() {
        if (updaterPopup == null) {
            updaterPopup = new ScreenPopup<>(getUpdaterDialog());
        }
        return updaterPopup;
    }

    /**
     *
     */
    public UpdaterDialog updaterDialog;
    /**
     *
     * @return
     */
    public UpdaterDialog getUpdaterDialog() {
        if (updaterDialog == null) {
            updaterDialog = new UpdaterDialog();
            updaterDialog.titleTextProperty().bindBidirectional(titleTextProperty());
            updaterDialog.captionTextProperty().bindBidirectional(captionTextProperty());
        }
        return updaterDialog;
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(this.getClass()).node(KEY.toLowerCase());
        }
        return preferences;
    }
}
