/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.calendar;

import com.google.api.client.util.DateTime;
import javafx.scene.layout.GridPane;
import org.symfound.controls.RunnableControl;
import org.symfound.controls.user.AnimatedLabel;
import org.symfound.controls.user.BuildableGrid;

/**
 *
 * @author Javed
 */
public class CalendarEntry extends RunnableControl {

    String backgroundColour;
    DateTime startTime;
    DateTime endTime;
    String text;

    /**
     *
     * @param backgroundColour
     * @param startTime
     * @param endTime
     * @param text
     */
    public CalendarEntry(String backgroundColour,
            DateTime startTime,
            DateTime endTime,
            String text) {
        initialize();
    }
    

    private void initialize() {
        BuildableGrid grid = new BuildableGrid();
        //  GridPane.setValignment(grid, VPos.BOTTOM);
        //GridPane.setHalignment(grid, hpos);
        grid.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.setMaxHeight(Double.POSITIVE_INFINITY);
        grid.setHgap(BuildableGrid.DEFAULT_GRID_GAP);
        grid.setVgap(BuildableGrid.DEFAULT_GRID_GAP);
        grid.setSpecRows(1);
        grid.setSpecColumns(2);
        grid.build();

        AnimatedLabel label = new AnimatedLabel(startTime.toString());
        GridPane.setConstraints(label, 0, 0);
        grid.getChildren().add(label);
        AnimatedLabel label2 = new AnimatedLabel(text);
        label.setStyle("-fx-background-color:" + backgroundColour);
        GridPane.setConstraints(label2, 0, 1);
        grid.getChildren().add(label2);
        addToPane(grid);

    }
}
