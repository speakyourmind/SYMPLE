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
package org.symfound.diagnostic;

import org.symfound.test.Result;
import org.symfound.test.Test;

/**
 *
 * @author Javed Gangjee
 */
public class DiagnosticTest extends Test {

    /**
     *
     */
    public static final Double SUCCESS_TRIES = 0.0;

    /**
     *
     */
    public static final Double WARNING_TRIES = 2.5;

    /**
     *
     */
    public static final Double CRITICAL_TRIES = 4.25;

    /**
     *
     */
    public DiagnosticTest() {
        super();
    }

    /**
     * Get a general consensus on Diag test based on predefined ranges for the
     * average number of tries for selecting the target button.
     *
     * @param tryAverage
     * @return
     */
    @Override
    public String rating(Double tryAverage) {
        String rating = "";
        if (tryAverage >= SUCCESS_TRIES && tryAverage <= WARNING_TRIES) {
            rating = Result.SUCCESS_TITLE;
        } else if (tryAverage > WARNING_TRIES && tryAverage <= CRITICAL_TRIES) {
            rating = Result.WARNING_TITLE;
        } else if (tryAverage > CRITICAL_TRIES) {
            rating = Result.CRITICAL_TITLE;
        }
        setResult(Result.SUCCESS_TITLE);
        return rating;
    }

}
