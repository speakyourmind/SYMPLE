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
package org.symfound.tools.timing;

import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 *
 * @author Javed Gangjee
 */
public class TimingBase {

    /**
     *
     */
    public Timeline timeline = new Timeline();
    private BooleanProperty playing;

    /**
     *
     */
    public static final Double DEFAULT_START_SECS = 0.0;

    /**
     *
     */
    public void end() {
        // Clean timeline
        timeline.stop();
        // Empty frames from timeline
        timeline.getKeyFrames().clear();
        setPlaying(Boolean.FALSE);
    }

    /**
     *
     */
    public void play() {
       
        timeline.play();
        setPlaying(Boolean.TRUE);
    }
    
      /**
     *
     */
    public void playFromStart() {
       
        timeline.playFromStart();
        setPlaying(Boolean.TRUE);
    }

    /**
     *
     * @return
     */
    public Timeline get() {
        return timeline;
    }

    /**
     *
     * @param timeline
     */
    public void set(Timeline timeline) {
        this.timeline = timeline;
    }

    /**
     *
     * @param value
     */
    public void setPlaying(Boolean value) {
        playingProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isPlaying() {
        return playingProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty playingProperty() {
        if (playing == null) {
            playing = new SimpleBooleanProperty(Boolean.FALSE);
        }
        return playing;
    }

}
