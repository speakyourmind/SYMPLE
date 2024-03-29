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
package org.symfound.tools.timing;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 *
 * @author Javed Gangjee
 */
public class LoopedEvent extends TimingBase {

    /**
     *
     */
    public static final Integer CYCLE_COUNT = Timeline.INDEFINITE;

    /**
     *
     */
    public Integer cycleCount = CYCLE_COUNT;

    /**
     *
     */
    public LoopedEvent() {
        super();
    }

    /**
     *
     * @param time
     * @param e
     * @return
     */
    public Timeline setup(Double time, EventHandler<ActionEvent> e) {
        KeyValue startKey = new KeyValue(playingProperty(), true);
        KeyFrame startFrame = new KeyFrame(Duration.seconds(DEFAULT_START_SECS), startKey);
        KeyFrame actionFrame = new KeyFrame(Duration.seconds(time), e);
        timeline = new Timeline(startFrame, actionFrame);
        timeline.setCycleCount(cycleCount);
        return timeline;
    }

}
