package org.symfound.controls.system;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.symfound.controls.user.AnimatedButton;

/**
 *
 * @author Javed Gangjee
 */
public final class SettingsRestoreButton extends SettingsManagerControl {

    private static final String NAME = SettingsRestoreButton.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public SettingsRestoreButton() {
        super();
        setSelection(primary);
        initTitleText = "Restore All Settings";
        initCaptionText = "Warning! This will delete all customization. "
                + "Application will shutdown after restoration.";
        setConfirmable(true);
    }

    /**
     *
     */
    @Override
    public void run() {
      try {
            Preferences.userRoot().node("/org/symfound").removeNode();
        } catch (BackingStoreException ex) {
            LOGGER.fatal(ex);
        }
        deleteMasterFile();
        LOGGER.info("Exiting the program");
        getSession().exit(Boolean.FALSE);

    }


    /**
     *
     */
    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton("");
        primary.setWrapText(true);
        load(primary);
        setCSS(cssClass, primary);
    }

}
