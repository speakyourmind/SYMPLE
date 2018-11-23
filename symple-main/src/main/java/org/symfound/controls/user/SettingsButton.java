package org.symfound.controls.user;

import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.symfound.controls.AppableControl;
import org.symfound.main.FullSession;
import static org.symfound.main.FullSession.getMainUI;
import static org.symfound.main.Main.getVersionManager;
import org.symfound.main.settings.SettingsController;

/**
 *
 * @author Javed Gangjee
 */
public final class SettingsButton extends AppableControl {

    /**
     *
     */
    public static final String NAME = SettingsButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Settings";

    /**
     *
     */
    public static final String DEFAULT_TITLE = "Launch Settings";

    /**
     *
     */
    public SettingsButton() {
        super("setting-button", KEY, DEFAULT_TITLE,"default");
    }

    /**
     *
     */
    @Override
    public void run() {
        LOGGER.info("Does program need update? " + getVersionManager().needsUpdate());
        getMainUI().getStack().load(FullSession.MAIN_SETTINGS);
        if (!getMainUI().isShowing()) {
            getPrimaryControl().getScene().getWindow().hide();
            LOGGER.info("Launching Settings screen");
            getMainUI().open();
        }
        SettingsController.setUpdated(false);
        getSession().setPlaying(false);
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(this.getClass()).node(KEY.toLowerCase());
        }
        return preferences;
    }
}
