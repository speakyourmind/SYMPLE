/*
 * Copyright (C) 2014 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.symfound.device.processing;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.symfound.builder.user.User;
import org.symfound.comm.port.PortCaller;
import org.symfound.device.emulation.EmulationManager;
import org.symfound.device.emulation.EmulationRequest;
import org.symfound.device.emulation.input.mouse.MouseEmulator;
import org.symfound.device.hardware.Hardware;
import org.symfound.device.hardware.characteristic.Processability;

/**
 *
 * @author Javed Gangjee
 */
public final class Processor implements Runnable {

    private static final String NAME = Processor.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String HOSTNAME = "localhost";

    private final User user;
    private final Hardware hardware;

    private EmulationRequest request;

    /**
     *
     * @param user
     * @param hardware
     */
    public Processor(User user, Hardware hardware) {
        this.user = user;
        this.hardware = hardware;
        initialize();
    }

    private void initialize() {
        dataPackageProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                LOGGER.debug("Parsing data package:" + newValue);
                parseDataPackage(newValue);
            } else {
                LOGGER.debug("Skipped parsing. Data package is null");
            }
        });
    }

    /**
     *
     */
    @Override
    public void run() {
        MouseEmulator mouse = getEmulationManager().getMouse();
        final Integer sampleSize = mouse.getListener().positionLog.getSampleSize(); // TO DO: Remove
        if (sampleSize >= 0) {
            String readMethod = hardware.getProcessability().getReadMethod();
            switch (readMethod) {
                case ReadMethod.PORT:
                    try {
                        final Integer port = hardware.getProcessability().getWritePort();
                        PortCaller client = new PortCaller(HOSTNAME, port);
                        ExecutorService es = Executors.newSingleThreadExecutor();
                        Future<JSONObject> future = es.submit(client);
                        setDataPackage(future.get());
                        es.shutdown();
                    } catch (InterruptedException | ExecutionException ex) {
                        LOGGER.fatal(null, ex);
                    }
                    break;
                case ReadMethod.FILE:
                    String readFile = hardware.getProcessability().getReadFile();
                    JSONParser parser = new JSONParser();
                    FileReader fileReader;
                    try {
                        fileReader = new FileReader(readFile);
                        Object obj = parser.parse(fileReader);
                        JSONObject jsonObject = (JSONObject) obj;
                        setDataPackage(jsonObject);
                    } catch (FileNotFoundException ex) {
                        LOGGER.fatal("Unable to find JSON request file: " + readFile, ex);
                    } catch (IOException ex) {
                        LOGGER.fatal("Unable to read JSON request file: " + readFile, ex);
                    } catch (ParseException ex) {
                   //     LOGGER.fatal("Unable to parse JSON request file: " + readFile, ex);
                    }
                    break;
                case ReadMethod.LOCAL:
                    break;
                default:
                    throw new IllegalArgumentException("Invalid Read Method: " + readMethod);
            }
        }
    }

    private ObjectProperty<JSONObject> dataPackage;

    /**
     *
     * @param value
     */
    public void setDataPackage(JSONObject value) {
        dataPackageProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public JSONObject getDataPackage() {
        return dataPackageProperty().getValue();
    }

    private void parseDataPackage(JSONObject data) {
        Processability processability = hardware.getProcessability();
        // TO DO: validate data
        Double timestamp = (double) data.get(processability.getTimestampKey());
        Boolean isTracking = (Boolean) data.get(processability.getTrackingKey());
        Boolean clicked = (Boolean) data.get(processability.getClickedKey());
        Double rawX = Double.valueOf((String) data.get(processability.getRawXKey()));
        Double rawY = Double.valueOf((String) data.get(processability.getRawYKey()));
        Double smoothX = Double.valueOf((String) data.get(processability.getSmoothXKey()));
        Double smoothY = Double.valueOf((String) data.get(processability.getSmoothYKey()));
        Point smoothPoint = new Point(smoothX.intValue(), smoothY.intValue());
        Point rawPoint = new Point(rawX.intValue(), rawY.intValue());

        request = new EmulationRequest();
        request.setClick(clicked);
        request.setPosition(rawPoint);
        request.setKeyPressed("");
        getEmulationManager().request(request);

    }

    /**
     *
     * @return
     */
    public ObjectProperty<JSONObject> dataPackageProperty() {
        if (dataPackage == null) {
            dataPackage = new SimpleObjectProperty<>();
        }
        return dataPackage;
    }

    private EmulationManager emulationManager;

    /**
     *
     * @return
     */
    public EmulationManager getEmulationManager() {
        if (emulationManager == null) {
            emulationManager = new EmulationManager(user, hardware);
        }
        return emulationManager;
    }

}
