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

import org.symfound.controls.user.BuildableGrid;

/**
 *
 * @author Javed Gangjee
 */
public abstract class TestGrid extends BuildableGrid {

    /**
     *
     */
    public Integer size;

    /**
     *
     * @return
     */
    public Integer getSize() {
        return size;
    }

    /**
     *
     * @param size
     */
    public void setup(Integer size) {
        this.size = size;
        clear();
        setSpecRows(size);
        setSpecColumns(size);
        build();
        populate();
        refreshNodes();
    }

    // TO DO: Add parameter and clone object
    // CalibrationGrid DiagnosticGrid can then be replaced.

    /**
     *
     */
    public abstract void populate();

    /**
     *
     */
    public void done() {
        setVisibleAll(false);
        setDisableAll(true);
    }
}
