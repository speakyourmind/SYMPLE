package org.symfound.controls.user;

import org.symfound.controls.RunnableControl;
import org.symfound.main.FullSession;
import static org.symfound.main.FullSession.getMainUI;

/**
 *
 * @author Javed Gangjee
 */
public final class DiagnosticTestButton extends RunnableControl {

    /**
     *
     */
    public DiagnosticTestButton() {
        super("diagnostic-app-button");
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
}
