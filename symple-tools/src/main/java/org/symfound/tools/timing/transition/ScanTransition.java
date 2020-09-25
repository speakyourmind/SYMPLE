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
package org.symfound.tools.timing.transition;

import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 *
 * @author Javed Gangjee
 */
public class ScanTransition extends DwellTransition {

    private StringProperty text;

    /**
     * Setup a timeline that scan through the button text list of letters, words
     * or phrases.
     *
     * @param list
     * @param background
     * @param color
     * @param time number of seconds between scans
     */
    public void setupScan(List<String> list, Color color, Color background, Double time) {
        // String that contains the text that needs to be scanned through
        list.add("");

        KeyValue startKey = new KeyValue(playingProperty(), true);
        KeyFrame startFrame = new KeyFrame(Duration.seconds(0), startKey);
        timeline.getKeyFrames().add(startFrame);

        //Build scan string frames
        for (int i = 0; i < list.size(); i++) {

            Duration frameTime = Duration.seconds((i + 2) * time);
            KeyValue scanFadeKey = new KeyValue(colorProperty(), background);
            KeyValue scanTextKey = new KeyValue(textProperty(), list.get(i));
            KeyValue scanColorKey = new KeyValue(colorProperty(), color);

            KeyFrame scanFrame = new KeyFrame(frameTime, scanTextKey, scanFadeKey);
            KeyFrame fadeOutFrame = new KeyFrame(frameTime.subtract(Duration.seconds(time / 2)), scanColorKey);
            timeline.getKeyFrames().addAll(scanFrame, fadeOutFrame);
        }
    }

    /**
     *
     * @param value
     */
    public void setText(String value) {
        textProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getText() {
        return textProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty textProperty() {
        if (text == null) {
            text = new SimpleStringProperty("");
        }
        return text;
    }

}
