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
package org.symfound.builder.user.feature;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author Javed Gangjee
 */
public class Eye extends Feature {

    /**
     *
     */
    public Eye() {
        super(Feature.EYE_FEATURE);
    }

    /**
     *
     * @return
     */
    public Boolean isVisible() {
        return !((getPosX() == 0) && (getPosY() == 0) && (getPosZ() == 0));
    }

    /**
     *
     */
    public static final Double MAX_PUPIL_SIZE = 50.0;
    private static final Double DEFAULT_PUPIL_SIZE = 0.0;
    private DoubleProperty size;

    /**
     *
     * @param value
     */
    public void setSize(Double value) {
        sizeProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Double getSize() {
        return sizeProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty sizeProperty() {
        if (size == null) {
            size = new SimpleDoubleProperty(DEFAULT_PUPIL_SIZE);
        }
        return size;
    }

    private static final Double DEFAULT_GAZE_POS = 0.0;
    private DoubleProperty gazeX;
    private DoubleProperty gazeY;
    private DoubleProperty gazeZ;

    /**
     *
     * @param value
     */
    public void setGazeX(Double value) {
        gazeXProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Double getGazeX() {
        return gazeXProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty gazeXProperty() {
        if (gazeX == null) {
            gazeX = new SimpleDoubleProperty(DEFAULT_GAZE_POS);
        }
        return gazeX;
    }

    /**
     *
     * @param value
     */
    public void setGazeY(Double value) {
        gazeYProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Double getGazeY() {
        return gazeYProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty gazeYProperty() {
        if (gazeY == null) {
            gazeY = new SimpleDoubleProperty(DEFAULT_GAZE_POS);
        }
        return gazeY;
    }

    /**
     *
     * @param value
     */
    public void setGazeZ(Double value) {
        gazeZProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Double getGazeZ() {
        return gazeZProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty gazeZProperty() {
        if (gazeZ == null) {
            gazeZ = new SimpleDoubleProperty(DEFAULT_GAZE_POS);
        }
        return gazeZ;
    }
}
