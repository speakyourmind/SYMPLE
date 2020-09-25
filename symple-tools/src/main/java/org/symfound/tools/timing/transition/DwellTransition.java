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
package org.symfound.tools.timing.transition;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import org.symfound.tools.timing.TimingBase;

/**
 *
 * @author Javed Gangjee
 */
public class DwellTransition extends TimingBase {

    private ObjectProperty<Paint> color;
    private DoubleProperty scale;

    /**
     * Setup a timeline that grows or shrinks a button in a specified amount of
     * time.
     *
     * @param time amount of time to dwell
     * @param toColor change the color
     * @param toScale how much the button grows
     */
    public void setupGrow(Double time, Paint toColor, Double toScale) {
        //Build Dwell Animation keys
        KeyValue startKey = new KeyValue(playingProperty(), true);
        KeyValue colorKey = new KeyValue(colorProperty(), toColor);
        KeyValue scaleKey = new KeyValue(scaleProperty(), toScale);
        //Build Dwell Animation Frame
        KeyFrame startFrame = new KeyFrame(Duration.seconds(DEFAULT_START_SECS), startKey);
        KeyFrame dwellFrame = new KeyFrame(Duration.seconds(time), colorKey, scaleKey);

        //Add to timeline
        timeline.getKeyFrames().add(dwellFrame);
    }

    /**
     *
     * @return
     */
    public ObjectProperty<Paint> colorProperty() {
        if (color == null) {
            color = new SimpleObjectProperty<>();
        }
        return color;
    }

    /**
     *
     * @return
     */
    public DoubleProperty scaleProperty() {
        if (scale == null) {
            scale = new SimpleDoubleProperty();
        }
        return scale;
    }

}
