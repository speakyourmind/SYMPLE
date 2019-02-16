package org.symfound.controls.user;

import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.symfound.controls.AppableControl;

/**
 * This button disables the children of the pane, so they are no longer
 * selectable.
 * <p>
 * By default the parent of the button is assumed to be the one that is to be
 * disabled. However, the user can configure the <code>paneProperty</code> to
 * lookup a pane of that name within the button's scene.
 *
 * @author Javed Gangjee
 */
public final class LockButton extends AppableControl {

    /**
     *
     */
    public static final String NAME = LockButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Lock";

    /**
     *
     */
    public static final String DEFAULT_TITLE = "Lock";

    private ButtonGrid lockGrid;

    public LockButton(ButtonGrid lockGrid) {
        super("type-button", KEY, DEFAULT_TITLE, "default");
        this.lockGrid = lockGrid;
        configureStyle();
        
    }

    /**
     *
     */
    @Override
    public void run() {
        LOGGER.info("Lock button pressed");
        lockGrid.togglePause();
       
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(this.getClass()).node(KEY.toLowerCase());
        }
        return preferences;
    }
}
