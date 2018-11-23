/*
 * Copyright (C) 2015 
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
package org.symfound.tools.timing.clock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.event.ActionEvent;

/**
 *
 * @author
 */
public abstract class Countdown extends ClockBase {

    /**
     *
     */
    public Integer time;

    /**
     *
     * @param time
     */
    public Countdown(Integer time) {
        this.time = time;
    }

    @Override
    public void play() {
        setup();
        timeline.play();
        setPlaying(Boolean.TRUE);
    }

    @Override
    public void setup() {
        List<Integer> countdown = new ArrayList<>();
        for (int i = 0; i < time; i++) {
            countdown.add(time - i);
        }
        Iterator<Integer> iterator = countdown.iterator();

        setup(CYCLE_TIME, (ActionEvent e) -> {
            if (iterator.hasNext()) {
                setText(iterator.next().toString());
            } else {
                complete();
            }
        });
    }

    @Override
    public void end() {
        timeline.stop();
        // Empty frames from timeline
        timeline.getKeyFrames().clear();
        setPlaying(Boolean.FALSE);
        setText(time.toString());

    }

    /**
     *
     */
    public void complete() {
        end();
        run();
    }

    /**
     * This is the method that runs at the end of the countdown.
     */
    public abstract void run();
}
