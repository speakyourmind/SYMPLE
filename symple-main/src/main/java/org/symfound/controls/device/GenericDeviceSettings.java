/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.device;

import java.util.Arrays;
import java.util.List;
import org.symfound.controls.system.SettingsRow;
import org.symfound.controls.system.SettingsTab;
import org.symfound.device.Device;
import org.symfound.device.hardware.generic.Generic;

/**
 *
 * @author Javed Gangjee
 */
public class GenericDeviceSettings extends DeviceSettings<Generic> {

    /**
     *
     */
    public GenericDeviceSettings() {
        super();
        initialize();
    }

    private void initialize() {
        populate();
    }

    /**
     *
     * @return
     */
    @Override
    public SettingsTab buildGeneralTab() {
        List<SettingsRow> rows = Arrays.asList();
        SettingsTab generalTab = new SettingsTab(GENERAL_TAB_TITLE, rows);
        return generalTab;
    }

    /**
     *
     * @return
     */
    @Override
    public Device<Generic> getDevice() {
        if (device == null) {
            device = getSession().getDeviceManager().generic;
        }
        return device;
    }
}
