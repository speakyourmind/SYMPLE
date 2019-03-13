/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.hardware.eyetracker;

import org.apache.log4j.Logger;
import org.symfound.builder.loader.RuntimeExecutor;
import org.symfound.builder.user.feature.Eye;
import org.symfound.device.hardware.Calibratable;
import org.symfound.device.hardware.Hardware;
import org.symfound.tools.timing.LoopedEvent;

/**
 *
 * @author Javed Gangjee
 */
public class GamingEyeTracker extends EyeTracker implements Calibratable {

    private static final String NAME = GamingEyeTracker.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String EXE_LOCATION = "\\etc\\tobii\\";

    /**
     *
     */
    public static final String DATA_STREAM_EXE_FILE = "MinimalGazeDataStream.exe"; // TO DO: Make a setting. TO DO: Change Name
   
    private RuntimeExecutor runtimeExecutor;

    private LoopedEvent positionEvent;

    /**
     *
     * @param leftEye
     * @param rightEye
     */
    public GamingEyeTracker(Eye leftEye, Eye rightEye) {
        super(Hardware.EYE_TRACKER, leftEye, rightEye);
    }

    /**
     *
     * @return
     */
    @Override
    public Boolean connect() {
        setConnected(true);
        return isConnected();
    }

    /**
     *
     */
    @Override
    public void launch() {
        if (isEnabled()) {
            String userDirectory = System.getProperty("user.dir");
            String command = userDirectory + EXE_LOCATION + DATA_STREAM_EXE_FILE;
            String readFile = getProcessability().getReadFile();
            LOGGER.info("Launching Eye Tracker exe from: " + command + " and reading its data from " + readFile);
            getRuntimeExecutor().execute(command);
            setLaunched(true);

            loopedEvent = createScanningEvent();
            loopedEvent.play();

        } else {
            LOGGER.warn("Eye Tracker is not Enabled");
        }

        // Create another looped event
    }

    /**
     *
     */
    @Override
    public void record() {
        
        
    }

    /**
     *
     */
    @Override
    public void bundle() {
    }

    /**
     *
     */
    @Override
    public void close() {
        if (isLaunched()) {
            LOGGER.info("Closing Eye Tracker program: " + DATA_STREAM_EXE_FILE);
            loopedEvent.end();
            getRuntimeExecutor().destroy();
            setLaunched(false);
        }
    }

    /**
     *
     */
    @Override
    public void calibrate() {
        // close();
        /* String userDirectory = System.getProperty("user.dir");
        String command = userDirectory + EXE_LOCATION + "MinimalConfigurationTool.exe";
        LOGGER.info("Launching Eye Tracker calibration from: " + command);
        RuntimeExecutor executor = new RuntimeExecutor();
        executor.execute(command);*/

    }

    /**
     *
     * @return
     */
    public RuntimeExecutor getRuntimeExecutor() {
        if (runtimeExecutor == null) {
            runtimeExecutor = new RuntimeExecutor();
        }
        return runtimeExecutor;
    }
}
