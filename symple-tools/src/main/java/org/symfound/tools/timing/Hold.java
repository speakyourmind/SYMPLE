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
package org.symfound.tools.timing;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;

/**
 *
 * @author Javed Gangjee
 */
public class Hold extends SimpleBooleanProperty {

    private static final Boolean DEFAULT_HOLD_VALUE = Boolean.FALSE;

    /**
     *
     */
    public Hold() {
        super(DEFAULT_HOLD_VALUE);
    }

    /**
     *
     * @param postSelectionTime
     */
    public void add(Double postSelectionTime) {
        setValue(true);
        DelayedEvent holdEvent = new DelayedEvent();
        holdEvent.setup(postSelectionTime, (ActionEvent e1) -> {
            remove();
        });
        holdEvent.play();

    }

    /**
     *
     */
    public void remove() {
        setValue(Boolean.FALSE);
    }

    /**
     *
     * @return
     */
    public Boolean isAdded() {
        return getValue();
    }

}
