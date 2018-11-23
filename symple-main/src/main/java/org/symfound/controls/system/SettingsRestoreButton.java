package org.symfound.controls.system;

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
        LOGGER.info("Backing up current settings");
        String destination = getUser().getContent().getHomeFolder() + "/Documents/SYMPLE/Settings/";
        getSession().getDeviceManager().exportAllPreferences(destination); // Backup settings on update TO DO: Pick a better folder

        getSession().getDeviceManager().clearAllPreferences();

        LOGGER.info("Exiting the program");
        getSession().shutdown();

    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton();
        primary.setWrapText(true);
        load(primary);
        setCSS(cssClass, primary);
    }

}
