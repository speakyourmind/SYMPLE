package org.symfound.controls.user;

import java.util.prefs.Preferences;
import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;
import org.symfound.controls.AppableControl;
import org.symfound.controls.system.dialog.ScreenPopup;
import static org.symfound.main.FullSession.getMainUI;
import static org.symfound.main.Main.getVersionManager;

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

        initialize();
    }

    private void initialize() {

        if (getVersionManager().needsUpdate()) {
            if (getUser().getProfile().autoUpdate()) {
                setTitleText("Update & Exit");;
                setCaptionText("Are you sure you want to close the program?\n"
                        + "Program will close after downloading an update.");
            } else {

                setTitleText("Exit Without Updating");;
                setCaptionText("Are you sure you want to close the program?\n"
                        + "An update is available and can be downloaded manually.");
            }
        } else {
            setTitleText("Exit");
            setCaptionText("Are you sure you want to close the program?");
        }
        getUser().getProfile().autoUpdateProperty().addListener((oldValue, newValue, observable) -> {
            if (getVersionManager().needsUpdate()) {
                if (getUser().getProfile().autoUpdate()) {
                    setTitleText("Update & Exit");;
                    setCaptionText("Are you sure you want to close the program?\n"
                            + "Program will close after downloading an update.");
                } else {

                    setTitleText("Exit Without Updating");;
                    setCaptionText("Are you sure you want to close the program?\n"
                            + "An update is available and can be downloaded manually. \n"
                            + "Note:See Settings > Profile > Update on Exit");
                }
            } else {
                setTitleText("Exit");
                setCaptionText("Are you sure you want to close the program?");
            }
        });

        setSpeakText(DEFAULT_TITLE);
    }

    private boolean triggerUpdate() {
        return getVersionManager().needsUpdate() && getUser().getProfile().autoUpdate();
    }

    @Override
    public void run() {
        getSession().shutdown(Boolean.TRUE);
        if (triggerUpdate()) {
            getVersionManager().update();
            this.getParentPane().getChildren().add(getUpdaterPopup());
        } else {
            LOGGER.info("Exiting the program");
            getSession().close();
        }
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
            updaterDialog = new UpdaterDialog() {
                @Override
                public void onOk() {
                    super.onOk();
                    LOGGER.info("Exiting the program");
                    getSession().exit(BACKUP_ON_EXIT);
                }
            };
            updaterDialog.buildDialog();
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
