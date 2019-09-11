package org.symfound.controls.user;

import java.util.prefs.Preferences;
import org.symfound.controls.AppableControl;
import static org.symfound.controls.user.ExitButton.KEY;
import org.symfound.main.FullSession;
import static org.symfound.main.FullSession.getMainUI;
import static org.symfound.main.Main.getSession;

/**
 *
 * @author Javed Gangjee
 */
public final class DiagnosticTestButton extends AppableControl {

    /**
     *
     */
    public DiagnosticTestButton() {
        super("diagnostic-app-button","Test","Test","default");
    }

    /**
     *
     */
    @Override
    public void run() {
        getMainUI().getStack().load(FullSession.DIAGNOSTIC_APP);
        if (!getMainUI().isShowing()) {
            getPrimaryControl().getParentUI().close();
            getMainUI().open();
        }
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
