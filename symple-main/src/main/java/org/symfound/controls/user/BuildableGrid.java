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
package org.symfound.controls.user;

import java.util.List;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import org.apache.log4j.Logger;

/**
 *
 * @author Javed Gangjee
 */
public class BuildableGrid extends AnimatedGrid {

    private static final String NAME = BuildableGrid.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final double LAUNCH_TIME_PER_CONTROL = 0.03;

    /**
     *
     */
    public static final double LAUNCH_TIME_INITIAL = 0.5;


    /**
     *
     */
    public void build() {
        buildRows();
        buildColumns();
    }

    /**
     *
     */
    public void buildRows() {
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(100.0 / getSpecRows());
        for (int i = 0; i < getSpecRows(); i++) {
            addRow(row);
        }
    }

    /**
     *
     * @param percentages
     */
    public void buildRowsByPerc(List<Double> percentages) {
        for (Double percentage : percentages) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(percentage);
            addRow(row);
        }
    }

    private void addRow(RowConstraints row) {
        getRowConstraints().add(row);
    }

    /**
     *
     */
    public void buildColumns() {
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(100.0 / getSpecColumns());
        for (int i = 0; i < getSpecColumns(); i++) {
            addColumn(column);
        }
    }

    /**
     *
     * @param columnPercentages
     */
    public void buildColumnsByPerc(List<Double> columnPercentages) {
        for (Double columnPercentage : columnPercentages) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(columnPercentage);
            addColumn(column);
        }
    }

    private void addColumn(ColumnConstraints column) {
        getColumnConstraints().add(column);
    }

}
