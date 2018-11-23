/*
 * Copyright (C) 2015 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/
 *
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
package org.symfound.device.hardware.generic;

import java.awt.MouseInfo;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.symfound.builder.user.feature.Unknown;
import org.symfound.device.hardware.Hardware;
import org.symfound.device.hardware.characteristic.Processability;
import org.symfound.tools.timing.LoopedEvent;

/**
 *
 * @author Javed Gangjee
 */
public class Generic extends Hardware<Unknown> {

    private static final String NAME = Generic.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final Double SCAN_RATE = 0.02;

    /**
     *
     */
    public Double mouseX;

    /**
     *
     */
    public Double mouseY;

    private LoopedEvent loopedEvent;
    
    /**
     *
     */
    public Generic() {
        super(GENERIC, new Unknown());
    }

    /**
     *
     */
    @Override
    public void launch() {
        if (isEnabled()) {
            LOGGER.info("Launching Generic Device");
            getLoopedEvent().play();
        } else {
            LOGGER.warn( "Device is not Enabled");
        }
    }

    /**
     *
     * @return
     */
    public LoopedEvent getLoopedEvent() {
        if (loopedEvent == null) {
            loopedEvent = new LoopedEvent();
            loopedEvent.setup(SCAN_RATE, (ActionEvent) -> {
                process();
            });
        }
        return loopedEvent;
    }

    /**
     *
     */
    @Override
    public void record() {
        mouseX = MouseInfo.getPointerInfo().getLocation().getX();
        mouseY = MouseInfo.getPointerInfo().getLocation().getY();
        feature.setPosX(mouseX);
        feature.setPosY(mouseY);
    }

    /**
     *
     */
    @Override
    public void bundle() {
        setBundled(false);
        Map<String, Object> dataMap = new HashMap<>();
        Processability processability = getProcessability();
        Long time = (new Date()).getTime();
        dataMap.put(processability.getTimestampKey(), time.doubleValue());
        dataMap.put(processability.getTrackingKey(), true);
        dataMap.put(processability.getClickedKey(), false);
        dataMap.put(processability.getRawXKey(), mouseX.toString());
        dataMap.put(processability.getRawYKey(), mouseY.toString());
        dataMap.put(processability.getSmoothXKey(), feature.getPosX().toString());
        dataMap.put(processability.getSmoothYKey(), feature.getPosY().toString());
        setDataPackage(new JSONObject(dataMap));
        setBundled(true);
    }

    /**
     *
     * @return
     */
    @Override
    public Boolean connect() {
        return true;
    }

    /**
     *
     */
    @Override
    public void close() {
        getLoopedEvent().end();
    }

}
