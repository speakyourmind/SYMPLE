/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.hardware.eyetracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public static final String EXE_LOCATION = "\\etc\\tobii\\";

    public static final String DATA_STREAM_EXE_FILE = "MinimalGazeDataStream.exe"; // TO DO: Make a setting. TO DO: Change Name
    public static final String LAUNCH_FILE = "Launch.lnk"; // TO DO: Make a setting. TO DO: Change Name

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
        {
            try {
                Process p = Runtime.getRuntime().exec("sc query \"Tobii Service\"");

                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

                String line = reader.readLine();
                while (line != null) {
                    if (line.trim().startsWith("STATE")) {
                        if (line.trim().substring(line.trim().indexOf(":") + 1, line.trim().indexOf(":") + 4).trim().equals("1")) {
                            LOGGER.info("Eye Tracker Service is Stopped");
                            String TEST = "\"C:\\Program Files\\SYMPLE\\app\\etc\\tobii\\Launch.lnk\"";
                            ProcessBuilder pb = new ProcessBuilder("cmd", "/c",
                                    TEST);
                            Process trackerProcess;
                            try {
                                trackerProcess = pb.start();
                                trackerProcess.waitFor();
                            } catch (IOException | InterruptedException ex) {
                                LOGGER.fatal(ex);
                            }

                        } else if (line.trim().substring(line.trim().indexOf(":") + 1, line.trim().indexOf(":") + 4).trim().equals("2")) {
                            System.out.println("Eye Tracker Service is Starting....");
                        } else if (line.trim().substring(line.trim().indexOf(":") + 1, line.trim().indexOf(":") + 4).trim().equals("3")) {
                            System.out.println("Eye Tracker Service is Stopping....");
                        } else if (line.trim().substring(line.trim().indexOf(":") + 1, line.trim().indexOf(":") + 4).trim().equals("4")) {
                            System.out.println("Eye Tracker Service is Running");
                        }

                    }
                    line = reader.readLine();
                }

            } catch (IOException e1) {
            }

        }

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
            LOGGER.info("Launching Eye Tracker processing from: " + command + " and reading its data from " + readFile);
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
