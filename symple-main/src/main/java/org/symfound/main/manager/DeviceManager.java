/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.main.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import org.symfound.builder.characteristic.PreferencesManager;
import org.symfound.builder.user.User;
import org.symfound.builder.user.characteristic.Physical;
import org.symfound.builder.user.feature.Eye;
import org.symfound.device.Device;
import org.symfound.device.hardware.Hardware;
import org.symfound.device.hardware.eyetracker.GamingEyeTracker;
import org.symfound.device.hardware.generic.Generic;
import org.symfound.device.hardware.swifty.Swifty;
import org.symfound.main.Main;

/**
 *
 * @author Javed Gangjee
 */
public class DeviceManager extends UsableManager<Device> implements Runnable {

    private static final String NAME = DeviceManager.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public List<Device> devices = new ArrayList<>();

    /**
     *
     * @param user
     */
    public DeviceManager(User user) {
        super(user);
        // initialize(user);
    }

    @Override
    public void run() {
        setCurrent(getUser().getDeviceName());
        getUser().deviceNameProperty().addListener((observable, oldValue, newValue) -> {
            setCurrent(newValue);
            launch(newValue);
        });
    }

    /**
     *
     * @param deviceName
     */
    public final void launch(String deviceName) {
        devices.stream().forEach((device) -> {
            Hardware hardware = device.getHardware();
            if (hardware.getName().equals(deviceName)) {
                LOGGER.info("Launching " + hardware.getName());
                Thread thread = new Thread(device);
                thread.start();
            } else {
                hardware.close();
            }
        });

    }

    /**
     *
     * @param user
     * @return
     */
    @Override
    public final Map<String, Device> buildMap(User user) {
        Map<String, Device> buildMap;
        HashMap<String, Device> hashMap = new HashMap<>();
        hashMap.put(Hardware.EYE_TRACKER, getTobii(user));
        hashMap.put(Hardware.SWIFTY, getSwifty(user));
        hashMap.put(Hardware.GENERIC, getGenericDevice(user));
        buildMap = Collections.unmodifiableMap(hashMap);
        devices = new ArrayList<>(buildMap.values());
        return buildMap;
    }

    /**
     *
     */
    public Device<Generic> generic;

    /**
     *
     * @param user
     * @return
     */
    public Device<Generic> getGenericDevice(User user) {
        if (generic == null) {
            LOGGER.info("Initializing Generic Device...");
            generic = new Device<>(new Generic(), user);
        }
        return generic;
    }

    /**
     *
     */
    public Device<Swifty> swifty;

    /**
     *
     * @param user
     * @return
     */
    public Device<Swifty> getSwifty(User user) {
        if (swifty == null) {
            LOGGER.info("Initializing Swifty...");
            swifty = new Device<>(new Swifty(), user);
        }
        return swifty;
    }

    /**
     *
     */
    public Device<GamingEyeTracker> tobii;

    /**
     *
     * @param user
     * @return
     */
    public Device<GamingEyeTracker> getTobii(User user) {
        if (tobii == null) {
            Physical physical = user.getPhysical();
            Eye leftEye = physical.getLeftEye();
            Eye rightEye = physical.getRightEye();
            LOGGER.info("Initializing Tobii...");
            tobii = new Device<>(new GamingEyeTracker(leftEye, rightEye), user);
        }
        return tobii;
    }

    /**
     *
     * @return
     */
    @Override
    public List<String> getNames() {
        return Hardware.DEVICE_TYPES;
    }

    /**
     *
     * @return
     */
    @Override
    public String getInitName() {
        return user.getDeviceName();
    }

    /**
     *
     * @return
     */
    public Boolean isSelectedCurrent() {
        final String currentHardware = getCurrent().getHardware().getName();
        final String selectedHardware = get(getIterator().get()).getHardware().getName();
        final boolean isSelectedCurrent = currentHardware.equalsIgnoreCase(selectedHardware);
        return isSelectedCurrent;
    }

    /**
     *
     */
    public void clearAllPreferences() {

        try {
            Preferences prefs = Preferences.userNodeForPackage(Main.class).parent();
            LOGGER.info("Clearing settings from node "+ prefs.absolutePath());
            prefs.removeNode();
        } catch (BackingStoreException ex) {
            LOGGER.fatal(ex);
        }

    }

    /**
     *
     * @param folder
     */
    public void exportAllPreferences(String folder) {
        File folderDestination = new File(folder);
        folderDestination.mkdirs(); // Create the folder if it doesn't exist.
        devices.stream().forEach((device) -> {
            Hardware hardware = device.getHardware();
            String hardwareName = hardware.getName();
            String hardwareFile = "\\" + getUser().getProfile().getFullName() + " " + hardwareName + ".xml";
            LOGGER.debug("Exporting " + hardwareName + "settings. File name: " + hardwareFile);
            String destination1 = folder + hardwareFile;
            LOGGER.info("Clearing " + hardwareName + " settings");
            try {
                PreferencesManager.exportTo(destination1, hardware.getPreferences());
            } catch (BackingStoreException | IOException ex) {
                LOGGER.fatal("Unable to export " + hardwareName + " settings");
            }
        });
    }

}
