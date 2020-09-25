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
package org.symfound.device.selection;

import java.util.Arrays;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Javed Gangjee
 */
public class SelectionEventType {

    // Selection

    /**
     *
     */
    public static final String PRESS_SELECT = "On Pressed";

    /**
     *
     */
    public static final String RELEASE_SELECT = "On Released";

    /**
     *
     */
    public static final List<String> EVENT_TYPES = Arrays.asList(
            PRESS_SELECT, RELEASE_SELECT);
    private StringProperty value;
    private final String initValue;

    /**
     *
     * @param initValue
     */
    public SelectionEventType(String initValue) {
        this.initValue = initValue;
    }

    /**
     *
     * @param value
     * @return
     */
    public static SelectionEventType valueOf(String value) {
        if (value == null) {
            throw new NullPointerException("A selection type must be specified");
        }
        return new SelectionEventType(value);
    }

    /**
     *
     * @param value
     */
    public void setValue(String value) {
        // Validate value. Throw exception.
        valueProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getValue() {
        return valueProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty valueProperty() {
        if (value == null) {
            value = new SimpleStringProperty(initValue);
        }
        return value;
    }
}
