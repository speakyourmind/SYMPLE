/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system;

import java.io.File;
import org.apache.log4j.Logger;
import org.symfound.controls.ConfirmableControl;

/**
 *
 * @author Javed Gangjee
 */
public abstract class SettingsManagerControl extends ConfirmableControl {
    
    private static final String NAME = SettingsManagerControl.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public SettingsManagerControl() {
        super("settings-button");
    }
    
    public void deleteMasterFile() {
        String masterFile = getUser().getContent().getHomeFolder() + "/Documents/SYMPLE/Settings/Master.xml";
        File file = new File(masterFile);
        if (file.exists()) {
            if (file.delete()) {
                LOGGER.info("Master file " + masterFile + " deleted successfully");
            } else {
                LOGGER.fatal("Failed to delete master file " + masterFile);
            }
        } else {
            LOGGER.warn("Master file does not exist or has already been deleted");
        }
    }
    
}
