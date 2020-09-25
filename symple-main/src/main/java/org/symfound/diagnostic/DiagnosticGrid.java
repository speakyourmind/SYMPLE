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
package org.symfound.diagnostic;

import org.symfound.test.TestGrid;

/**
 * TO DO: REMOVE
 *
 * @author Javed Gangjee
 */
public class DiagnosticGrid extends TestGrid {

    /**
     *
     */
    public DiagnosticGrid(){
        super();
        this.setStyle("-fx-background-color:-fx-light;");
                
    }

    /**
     *
     */
    @Override
    public void populate() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                DiagnosticTarget target = new DiagnosticTarget();
                target.columnIndex = i;
                target.rowIndex = j;
                target.setVisible(false);
                target.setDisable(false);
                add(target, i, j);
            }
        }
    }

}
