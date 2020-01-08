/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.util.prefs.Preferences;
import org.symfound.controls.AppableControl;
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
        initialize();
    }

    private void initialize() {
        setSpeakText("Updating SYMPLE.The program will download and automatically install the new version.");
    }

    @Override
    public void run() {

        getSession().shutdown(Boolean.TRUE);

        getVersionManager().update();
        this.getParentPane().getChildren().add(getUpdaterPopup());

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
            updaterDialog.buildDialog();
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
