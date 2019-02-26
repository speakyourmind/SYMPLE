/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.ProgressBar;
import org.symfound.comm.file.PathWriter;
import org.symfound.controls.AppableControl;
import static org.symfound.controls.ScreenControl.CSS_PATH;
import org.symfound.controls.system.SettingsExporter;
import org.symfound.controls.system.dialog.OKDialog;
import org.symfound.controls.system.dialog.ScreenPopup;
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
    }

    @Override
    public void run() {
      
        String backupFolder = getUser().getContent().getHomeFolder() + "/Documents/SYMPLE/Settings/Backup";
        PathWriter backupPathWriter = new PathWriter(backupFolder);
        backupPathWriter.file.mkdirs();
        SettingsExporter backupSettingsExporter = new SettingsExporter(backupFolder);
        LOGGER.info("Backing up settings to folder: " +backupFolder);
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
    public ScreenPopup<OKDialog> getUpdaterPopup() {

        if (updaterPopup == null) {
            updaterPopup = new ScreenPopup<>(getUpdaterDialog());
        }
        return updaterPopup;
    }
    ProgressBar progressBar;

    /**
     *
     * @return
     */
    public OKDialog getUpdaterDialog() {
        if (updaterDialog == null) {
            updaterDialog = new OKDialog("Updating...", "SYMPLE will close when the download is complete", "ABORT") {
                @Override
                public void onOk() {
                    getVersionManager().msiDownloader.abort();
                }

                @Override
                public void buildDialog() {
                    getStylesheets().add(CSS_PATH);
                    List<Double> rowPercentages = Arrays.asList(60.0, 10.0, 30.0);
                    buildBaseGrid(3, 1, rowPercentages);
                    baseGrid.add(titledLabel, 0, 0);
                    progressBar = new ProgressBar();
                    progressBar.setMaxWidth(Double.POSITIVE_INFINITY);
                    progressBar.setPrefHeight(30.0);
                    progressBar.getStylesheets().add(CSS_PATH);
                    progressBar.setPadding(new Insets(0.0, 20.0, 0.0, 20.0));
                    progressBar.getStyleClass().clear();
                    progressBar.getStyleClass().add("progress-bar-dark");
                    progressBar.progressProperty().bindBidirectional(getVersionManager().msiDownloader.getTracker().progressProperty());
                    baseGrid.add(progressBar, 0, 1);
                    actionGrid = buildActionGrid(HPos.CENTER, 360.0, 60.0);
                    baseGrid.add(actionGrid, 0, 2);
                    addToStackPane(baseGrid);
                }
            };
            updaterDialog.titleTextProperty().bindBidirectional(titleTextProperty());
            updaterDialog.captionTextProperty().bindBidirectional(captionTextProperty());
            //  updaterDialog.okButton.textProperty().bindBidirectional(okTextProperty());
            //  updaterDialog.cancelButton.textProperty().bindBidirectional(cancelTextProperty());
        }
        return updaterDialog;
    }
    /**
     *
     */
    public OKDialog updaterDialog;

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(this.getClass()).node(KEY.toLowerCase());
        }
        return preferences;
    }
}
