/*
 * Copyright (C) 2015 SpeakYourMind Foundation
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
package org.symfound.test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Label;

/**
 *
 * @author Javed Gangjee
 */
public final class ResultValue extends Label {

    /**
     *
     */
    public static Double DEFAULT_VALUE = 0.0;
    private DoubleProperty value;

    /**
     *
     * @param value
     * @param decimal
     * @param unit
     */
    public void set(Double value, Integer decimal, String unit) {
        BigDecimal bd = new BigDecimal(value).setScale(decimal, RoundingMode.HALF_EVEN);
        setValue(bd.doubleValue());
        setText(getValue().toString() + unit);
    }

    /**
     *
     * @param value
     */
    public void setValue(Double value) {
        valueProperty().set(value);
    }

    /**
     *
     * @return
     */
    public Double getValue() {
        return valueProperty().get();
    }

    /**
     *
     * @return
     */
    public DoubleProperty valueProperty() {
        if (value == null) {
            value = new SimpleDoubleProperty(DEFAULT_VALUE);
        }
        return value;
    }

}
