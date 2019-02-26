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
package org.symfound.tools.timing.clock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javafx.event.ActionEvent;

/**
 *
 * @author Javed Gangjee
 */
public class Clock extends ClockBase {

    /**
     *
     */
    public String format; //format for example: "EEE, MMM d h:mm:ss a"

    /**
     *
     * @param format
     */
    public Clock(String format) {
        this.format = format;
        initialize();
    }

    private void initialize() {
        setup();
    }

    /**
     *
     * @param format
     */
    public void setFormat(String format) {
        this.format = format;
        this.end();
        setup();
        play();
    }

    @Override
    public void setup() {
        setup(CYCLE_TIME, (ActionEvent actionEvent) -> {
            setText(getTimestamp());
        });
    }

    /**
     *
     * @return
     */
    public String getTimestamp() {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat(format);
        String timestamp = dateFormat.format(calendar.getTime());
        return timestamp;
    }

}
