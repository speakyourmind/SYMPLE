/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import org.symfound.controls.ScreenControl;
import org.symfound.device.Device;
import org.symfound.device.emulation.EmulationManager;
import org.symfound.device.emulation.input.switcher.SwitchListener;

/**
 *
 * @author Javed Gangjee
 */
public class SwitchGrid extends ScreenControl<AnimatedButton> {

    /**
     *
     */
    public SwitchGrid() {
        super();
        initialize();
    }

    private void initialize() {
        final Device current = getSession().getDeviceManager().getCurrent();
        final EmulationManager emulationManager = current.getProcessor().getEmulationManager();
        final SwitchListener listener = emulationManager.getSwitch().getListener();
        listener.switchedProperty().addListener((observable, oldValue, newValue) -> {

        });
    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {

    }

}
