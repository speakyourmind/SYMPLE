package org.symfound.controls.user;

import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.symfound.controls.AppableControl;

/**
 *
 * @author Javed Gangjee
 */
public final class MinimizeButton extends AppableControl {

    /**
     *
     */
    public static final String NAME = MinimizeButton.class.getName();
    public static final Logger LOGGER = Logger.getLogger(NAME);
    public static final String KEY = "Minimize";
    public static final String DEFAULT_TITLE = "Minimize";

    /**
     *
     */
    public MinimizeButton() {
        super("minimize-button", KEY, DEFAULT_TITLE, "default");
        setText("_");
    }

    /**
     *
     */
    @Override
    public void run() {
        getPrimaryControl().getParentUI().setIconified(true);
        LOGGER.info("Window minimized");
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(this.getClass()).node(KEY.toLowerCase());
        }
        return preferences;
    }
}
