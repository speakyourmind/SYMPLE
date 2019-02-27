package org.symfound.controls.user;

import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.symfound.controls.AppableControl;

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
        initTitleText = "Exit";
        initCaptionText = "Are you sure you want to close the program?";
        setSpeakText(DEFAULT_TITLE);
    }

    @Override
    public void run() {
        LOGGER.info("Exiting the program");
        getSession().shutdown(BACKUP_ON_EXIT);
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(this.getClass()).node(KEY.toLowerCase());
        }
        return preferences;
    }
}
