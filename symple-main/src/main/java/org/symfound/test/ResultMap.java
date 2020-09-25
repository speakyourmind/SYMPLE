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

import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;
import org.symfound.controls.user.BuildableGrid;

/**
 *
 * @author Javed Gangjee
 */
public class ResultMap extends BuildableGrid {

    private static final String NAME = ResultMap.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final double EXTERIOR_GAP = 20.0;

    /**
     *
     */
    public static final Double DEFAULT_BASE_RADIUS = 25.0;

    /**
     *
     */
    public double baseRadius;

    /**
     *
     */
    public ResultMap() {
        this.baseRadius = DEFAULT_BASE_RADIUS;
    }

    /**
     *
     * @param baseRadius
     */
    public ResultMap(Double baseRadius) {
        this.baseRadius = baseRadius;// Auto calculate based on cell width
    }

    /**
     *
     * @param size
     * @param nodes
     */
    public void build(Integer size, ObservableList<Node> nodes) {
        List<Target> targets = new ArrayList<>();
        for (Node node : nodes) {
            if (node instanceof Target) {
                Target target = (Target) node;
                targets.add(target);
            }
        }
        build(size, targets);
    }

    private void build(Integer size, List<Target> targets) {
        setSpecRows(size);
        setSpecColumns(size);
        build();
        for (Target target : targets) {
            if (target.isComplete()) {
                Integer rowIndex = target.rowIndex;
                Integer columnIndex = target.columnIndex;
                LOGGER.info(" Target at (" + rowIndex + "," + columnIndex + ") is a " + target.getResult().get());
                addIndicator(target.getResult(), rowIndex, columnIndex, size);
            }
        }
        AnchorPane.setTopAnchor(this, EXTERIOR_GAP);
        AnchorPane.setBottomAnchor(this, EXTERIOR_GAP);
        AnchorPane.setLeftAnchor(this, EXTERIOR_GAP);
        AnchorPane.setRightAnchor(this, EXTERIOR_GAP);
        toFront();
    }

    private void addIndicator(Result result, Integer row, Integer column, Integer size) {
        ResultIndicator indicator = new ResultIndicator(baseRadius - size, result);
        indicator.setGridLocation(row, column);
        getChildren().add(indicator);
        GridPane.setHalignment(indicator, HPos.CENTER);
        GridPane.setValignment(indicator, VPos.CENTER);
    }

}
