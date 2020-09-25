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
package org.symfound.device.hardware.eyetracker;

import java.util.Arrays;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.symfound.builder.user.feature.Eye;
import org.symfound.device.hardware.Hardware;

/**
 *
 * @author Javed Gangjee
 */
public abstract class EyeTracker extends Hardware<Eye> {

    //TODO: Replace with enum
    /**
     *
     */
    public static final String SUCCESS_CALIBRATION = "GOOD";

    /**
     *
     */
    public static final String FAIL_CALIBRATION = "RUN";

    /**
     *
     */
    public static final List<String> CALIBRATION_STATES = Arrays.asList(
            FAIL_CALIBRATION, SUCCESS_CALIBRATION);

    /**
     *
     */

    /**
     *
     * @param initValue
     * @param leftEye
     * @param rightEye
     */
    public EyeTracker(String initValue, Eye leftEye, Eye rightEye) {
        super(initValue, leftEye);
        this.leftEye = leftEye;
        this.rightEye = rightEye;
    }
    private final Eye leftEye;

    /**
     *
     * @return
     */
    public Eye getLeftEye() {
        return leftEye;
    }
    
    private final Eye rightEye;

    /**
     *
     * @return
     */
    public Eye getRightEye() {
        return rightEye;
    }

    private BooleanProperty calibrated;
    /**
     *
     * @param value
     */
    public void setCalibrated(Boolean value) {
        calibratedProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isCalibrated() {
        return calibratedProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty calibratedProperty() {
        if (calibrated == null) {
            calibrated = new SimpleBooleanProperty(Boolean.FALSE);
        }
        return calibrated;
    }

}
