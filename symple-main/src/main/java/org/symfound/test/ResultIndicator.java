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

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 *
 * @author Javed Gangjee
 */
public class ResultIndicator extends StackPane {

    Circle circle = new Circle();

    /**
     *
     * @param radius
     * @param result
     */
    public ResultIndicator(Double radius, Result result) {
        super();
        circle.setFill(Paint.valueOf(result.getColour()));
        circle.setRadius(radius);
        StackPane.setAlignment(circle, Pos.CENTER);
        getChildren().add(circle);
    }

    /**
     *
     * @param rowIndex
     * @param columnIndex
     */
    public void setGridLocation(Integer rowIndex, Integer columnIndex) {
        GridPane.setRowIndex(this, rowIndex);
        GridPane.setColumnIndex(this, columnIndex);
    }
}
