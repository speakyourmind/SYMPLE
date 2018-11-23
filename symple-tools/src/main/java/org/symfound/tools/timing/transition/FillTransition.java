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
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import org.symfound.tools.timing.TimingBase;

/**
 *
 * @author Javed Gangjee
 */
public class FillTransition extends TimingBase {

    private ObjectProperty<Paint> color;

    /**
     *
     * @param time
     * @param fromColor
     * @param toColor
     * @return
     */
    public Timeline setup(Double time, Color fromColor, Color toColor) {
        //Build Dwell Animation keys
        KeyValue startKey = new KeyValue(playingProperty(), true);
        KeyValue initKey = new KeyValue(colorProperty(), fromColor);
        KeyValue finalKey = new KeyValue(colorProperty(), toColor);
        //Build Dwell Animation Frame
        KeyFrame startFrame = new KeyFrame(Duration.seconds(DEFAULT_START_SECS), startKey, initKey);
        KeyFrame endFrame = new KeyFrame(Duration.seconds(time), finalKey);

        //Add to timeline
        timeline.getKeyFrames().addAll(startFrame, endFrame);

        return timeline;
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

}
