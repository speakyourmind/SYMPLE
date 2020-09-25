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
package org.symfound.test;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.symfound.tools.timing.clock.Countdown;

/**
 *
 * @author Javed Gangjee
 */
public abstract class Test {

    /**
     *
     */
    public static final Integer COUNTDOWN_TIME = 10;
    private Countdown countdown;

    /**
     *
     */
    public static final Integer INITIAL_ATTEMPTS = 0;

    /**
     *
     */
    public static final Integer MAX_ATTEMPTS = 3;

    /**
     *
     */
    public IntegerProperty attempts;

    private BooleanProperty complete;

    /**
     *
     */
    public Result result;

    /**
     *
     * @param score
     * @return
     */
    public abstract String rating(Double score);

    /**
     *
     * @param countdown
     */
    public void setCountdown(Countdown countdown) {
        this.countdown = countdown;
    }

    /**
     *
     * @return
     */
    public Countdown getCountdown() {
        return countdown;
    }

    /**
     *
     * @return
     */
    public Result getResult() {
        if (result == null) {
            result = new Result();
        }
        return result;
    }

    /**
     *
     * @param value
     */
    public void setResult(String value) {
        getResult().set(value);
    }

    /**
     *
     * @param value
     */
    public void setAttempts(Integer value) {
        attemptsProperty().setValue(value);
    }

    /**
     *
     */
    public void incrementAttempts() {
        setAttempts(getAttempts() + 1);
    }

    /**
     *
     */
    public void resetAttempts() {
        setAttempts(INITIAL_ATTEMPTS);
    }

    /**
     *
     * @return
     */
    public Integer getAttempts() {
        return attemptsProperty().getValue();
    }

    /**
     *
     * @return
     */
    public IntegerProperty attemptsProperty() {
        if (attempts == null) {
            attempts = new SimpleIntegerProperty(INITIAL_ATTEMPTS);
        }
        return attempts;
    }

    /**
     *
     * @param value
     */
    public void setComplete(Boolean value) {
        completeProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isComplete() {
        return completeProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty completeProperty() {
        if (complete == null) {
            complete = new SimpleBooleanProperty(Boolean.FALSE);
        }
        return complete;
    }
}
