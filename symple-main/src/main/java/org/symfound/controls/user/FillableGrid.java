/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import static org.symfound.builder.user.characteristic.Ability.MAX_LEVEL;
import org.symfound.controls.AppableControl;
import org.symfound.controls.RunnableControl;

/**
 *
 * @author Javed Gangjee
 */
public class FillableGrid extends BuildableGrid {

    /**
     *
     */
    public static enum FillMethod {

        /**
         *
         */
        ROW_WISE,
        /**
         *
         */
        COLUMN_WISE
    }

    /**
     *
     */
    public static enum FillDirection {

        /**
         *
         */
        FORWARD,
        /**
         *
         */
        REVERSE
    }

    /**
     *
     */
    public static final int DEFAULT_COLUMN_SPAN = 1;

    /**
     *
     */
    public static final int DEFAULT_ROW_SPAN = 1;

    /**
     *
     */
    public final void loadFXMLChildren() {
        getSession().builtProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                ObservableList<Node> children = getChildren();
                for (Node node : children) {
                    if (node instanceof AppableControl) {
                        getControlsQueue().add((AppableControl) node);
                    }
                }

            }
        });
    }

    /**
     *
     * @param screenControls
     * @param method
     * @param direction
     */
    public void configure(List<AppableControl> screenControls, FillMethod method, FillDirection direction) {
        if (direction.equals(FillDirection.REVERSE)) {
            Collections.reverse(screenControls);
        }
        this.controlsQueue = screenControls;
        Runnable runnable = () -> {
            populate(method);
            spread(method);
            disableAll(isPaused());
            setStatus(ScreenStatus.PLAYING);
        };
        Platform.runLater(runnable);
    }

    /**
     *
     * @param value
     */
    public void disableAll(Boolean value) {
        getChildren().forEach((child) -> {
            if (child instanceof AppableControl) {
                AppableControl control = (AppableControl) child;
                if (!(control instanceof LockButton)) {
                    //   System.out.println(value.toString());
                    control.setDisable(value);
                }
            }
        });
    }

    private BooleanProperty paused;

    /**
     * Toggles the paused boolean property.
     */
    public void togglePause() {
        setPaused(!isPaused());
    }

    /**
     *
     * @param value
     */
    public void setPaused(Boolean value) {
        pausedProperty().setValue(value);
    }

    /**
     * Finds out whether the pause button is active.
     *
     * @return paused if true, false otherwise
     */
    public Boolean isPaused() {
        return pausedProperty().getValue();
    }

    /**
     * Represents the pause function of the button.
     *
     * @return paused
     */
    public BooleanProperty pausedProperty() {
        if (paused == null) {
            paused = new SimpleBooleanProperty();
        }
        return paused;
    }

    /**
     *
     * @param dimension
     * @throws IllegalArgumentException
     */
    public void expand(String dimension) throws IllegalArgumentException {
        //Originally by row
        String dimension1;
        String dimension2;
        Integer spec1;
        Integer spec2;
        switch (dimension) {
            case "row":
                dimension1 = "row";
                dimension2 = "column";
                spec1 = getSpecRows();
                spec2 = getSpecColumns();
                break;
            case "column":
                dimension1 = "column";
                dimension2 = "row";
                spec1 = getSpecColumns();
                spec2 = getSpecRows();
                break;
            default:
                throw new IllegalArgumentException(dimension + " parameter not recognized");
        }
        for (int i = 0; i < spec2; i++) {
            for (int j = 0; j < spec1; j++) {
                RunnableControl node = this.get(i, j);
                if (node != null) {
                    if (node instanceof AppableControl) {
                        AppableControl appableControl = (AppableControl) node;

                        Integer span1 = getDimensionSpan(appableControl, dimension1);
                        Integer index1 = getDimensionIndex(appableControl, dimension1);

                        Integer span2 = getDimensionSpan(appableControl, dimension2);
                        Integer index2 = getDimensionIndex(appableControl, dimension2);

                        Integer expandedSpan1 = span1 + appableControl.getExpandByDimension(dimension1);
                        if (expandedSpan1 + index1 > spec1) {
                            expandedSpan1 = spec1 - index1;
                            appableControl.setExpandByDimension(spec1 - index1, dimension1);
                        }
                        setDimensionSpan(appableControl, expandedSpan1, dimension1);
                        appableControl.toFront();

                        if (appableControl.getExpandByDimension(dimension1) > 0) {
                            LOGGER.info("Checking for overlap for appableControl " + appableControl.getText());
                            for (int k = 1; k < expandedSpan1; k++) {
                                for (int l = 0; l < span2; l++) {
                                    Integer nextIndex2 = getDimensionIndex(appableControl, dimension2) + l;
                                    Integer nextIndex1 = getDimensionIndex(appableControl, dimension1) + k;

                                    RunnableControl spanCheckNode;
                                    if (dimension.equals("row")) {
                                        spanCheckNode = get(nextIndex1, nextIndex2);
                                    } else {
                                        spanCheckNode = get(nextIndex2, nextIndex1);
                                    }
                                    if (spanCheckNode != null) {
                                        LOGGER.info("Found overlapping appableControl " + spanCheckNode.getText()
                                                + " at " + nextIndex2 + "," + nextIndex1);
                                        LOGGER.info("Cascading remaining cells from "
                                                + nextIndex2 + ", " + nextIndex1 + " to "
                                                + spec2 + ", " + spec1);
                                        spanCheckNode.removeFromParent();
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }

    }

    /**
     *
     * @param method
     */
    public void populate(FillMethod method) {
        // Add the populated list of buttons to the grid
        //System.out.println(" Adding controls to grid of size (" + getSpecRows() + "," + getSpecColumns() + ")");
        int k = 0;

        if (method.equals(FillMethod.ROW_WISE)) {
            for (int i = 0; i < getSpecRows(); i++) {
                for (int j = 0; j < getSpecColumns(); j++) {
                    if (k < getControlsQueue().size()) {
                        RunnableControl screenControl = getControlsQueue().get(k);
                        test(screenControl);
                        //  screenControl.setGridLocation(k);
                        //System.out.println("Adding " + screenControl.getText() + " to column " + j + " to row " + i);
                        add(screenControl, j, i, DEFAULT_COLUMN_SPAN, DEFAULT_ROW_SPAN);
                        k++;
                    }
                }
            }
        }
        if (method.equals(FillMethod.COLUMN_WISE)) {
            for (int i = 0; i < getSpecColumns(); i++) {
                for (int j = 0; j < getSpecRows(); j++) {
                    if (k < getControlsQueue().size()) {
                        RunnableControl screenControl = getControlsQueue().get(k);
                        //System.out.println("Adding " + screenControl.getText() + " to column " + j + " to row " + i);
                        test(screenControl);
                        add(screenControl, i, j, DEFAULT_COLUMN_SPAN, DEFAULT_ROW_SPAN);
                        k++;
                    }
                }
            }
        }
    }

    private void test(RunnableControl screenControl) {
        GridPane.setHgrow(screenControl, Priority.NEVER);
        GridPane.setFillWidth(screenControl, Boolean.TRUE);
        GridPane.setVgrow(screenControl, Priority.NEVER);
        GridPane.setFillHeight(screenControl, Boolean.TRUE);
    }

    /**
     *
     * @param method
     */
    public void spread(FillMethod method) {
        setEmptyCells(getNumOfEmptyCells());
        if (hasEmptyCells()) {
            if (method.equals(FillMethod.COLUMN_WISE)) {
                cascade("row");
                span("column");
            } else {
                cascade("column");
                span("row");
            }
        }
        expand("row");
        expand("column");
    }

    /**
     *
     * @param dimension
     */
    public void cascade(String dimension) {
        //Originally by row
        String dimension1;
        String dimension2;
        Integer spec1;
        Integer spec2;
        switch (dimension) {
            case "row":
                dimension1 = "row";
                dimension2 = "column";
                spec1 = getSpecRows();
                spec2 = getSpecColumns();
                break;
            case "column":
                dimension1 = "column";
                dimension2 = "row";
                spec1 = getSpecColumns();
                spec2 = getSpecRows();
                break;
            default:
                throw new IllegalArgumentException(dimension + " parameter not recognized");
        }
        for (int i = 0; i < getControlsQueue().size(); i++) {
            RunnableControl currentNode = getControlsQueue().get(i);
            // Only change the span if there are empty spots available.
            if (hasEmptyCells()) {
                // Gets the row and column index of each button
                Integer index1 = getDimensionIndex(currentNode, dimension1);
                Integer index2 = getDimensionIndex(currentNode, dimension2);
                // Span the row by 1 for each button that is not in the last
                // row of the grid.
                if (index1 < spec1 - 1 && index2 < spec2) {
                    // Get the current span
                    Integer span1 = getDimensionSpan(currentNode, dimension1);
                    // If this is not the last button in the list...
                    if (i < getControlsQueue().size() - 1) {
                        // Increment it by one

                        span1 += 1;
                        //System.out.println("Span " + dimension + " for " + currentNode.getText() + " at " + index1 + index2 + " set to " + span1);

                    } else {
                        // Or span all the way to the last row
                        span1 = spec1 - index1;
                        //System.out.println("Span " + dimension + " for last button " + currentNode.getText() + " at " + index1 + index2 + " set to " + span1);

                    }

                    if (span1 <= spec1) {
                        // Cascade the remaining buttons down by 1 without modifying span
                        for (int j = i + 1; j < getControlsQueue().size(); j++) {
                            RunnableControl nextNode = getControlsQueue().get(j);
                            // Get the current row and column index of the next button
                            Integer nextIndex1 = getDimensionIndex(nextNode, dimension1);
                            Integer nextIndex2 = getDimensionIndex(nextNode, dimension2);

                            // Increment the row index
                            nextIndex1 += 1;
                            //System.out.println(dimension1 + " for " + nextNode.getText() + " set to " + nextIndex1);
                            // If the incremented index is beyond the last row...
                            if (nextIndex1 > spec1 - 1) {
                                // Move the button over to the first row in the next column
                                nextIndex1 = 0;
                                nextIndex2 += 1;
                                /* if (nextIndex2 > spec2) {
                             nextNode.removeFromParent();
                                     }*/
                                //System.out.println("Moving " + nextNode.getText() + " to next " + dimension2 + " " + nextIndex2);
                            }

                            // Set the newly determined row and column index
                            setCell(nextNode, dimension1, nextIndex1, dimension2, nextIndex2);
                        }
                        setDimensionSpan(currentNode, span1, dimension1);
                        setEmptyCells(getEmptyCells() - 1);
                    }

                }
            }
        }
    }

    /**
     *
     * @param dimension
     */
    public void span(String dimension) {
        //Originally by column
        String dimension1;
        String dimension2;
        Integer spec1;
        Integer spec2;
        switch (dimension) {
            case "row":
                dimension1 = "row";
                dimension2 = "column";
                spec1 = getSpecRows();
                spec2 = getSpecColumns();
                break;
            case "column":
                dimension1 = "column";
                dimension2 = "row";
                spec1 = getSpecColumns();
                spec2 = getSpecRows();
                break;
            default:
                throw new IllegalArgumentException(dimension + " parameter not recognized");
        }
        // DIMENSION SPANNING
        Integer counter1 = 0;
        Integer spanCounter = 2;
        // Only span the column if there is more than one full column available
        while (getEmptyCells() >= spec2 && counter1 < spec1) {
            for (Node node : getControlsQueue()) {
                Integer index1 = getDimensionIndex(node, dimension1);
                if (Objects.equals(index1, counter1)) {
                    Integer span1 = getDimensionSpan(node, dimension1);
                    span1++;
                    setDimensionSpan(node, span1, dimension1);
                    Integer span2 = getDimensionSpan(node, dimension2);
                    for (int i = 0; i < span2; i++) {
                        decrementEmptyCells();
                    }
                }
            }

            for (Node node : getControlsQueue()) {
                Integer nextIndex1 = getDimensionIndex(node, dimension1);
                if (nextIndex1 > counter1) {
                    nextIndex1++;
                    setDimensionIndex(node, nextIndex1, dimension);
                }
            }
            counter1 += spanCounter;

            if (counter1 >= spec1) {
                spanCounter++;
                counter1 = 0;
            }
        }
    }

    /**
     *
     * @param size
     * @return
     */
    public int getRowSize(Double size) {
        Integer rowSize;
        if (getOverrideRow() <= 0.0) {
            Double roundedSize = Math.floor(size);
            Double horizontalSize = (roundedSize < MAX_LEVEL) ? roundedSize : MAX_LEVEL;
            rowSize = horizontalSize.intValue();
        } else {
            rowSize = getOverrideRow().intValue();
        }
        return rowSize;
    }

    /**
     *
     * @param size
     * @return
     */
    public int getColumnSize(Double size) {
        Integer columnSize;
        if (getOverrideColumn() <= 0.0) {
            Double roundedSize = Math.ceil(size);
            Double verticalSize = (roundedSize < MAX_LEVEL) ? roundedSize : MAX_LEVEL;
            columnSize = verticalSize.intValue();
        } else {
            columnSize = getOverrideColumn().intValue();
        }
        return columnSize;
    }

    /**
     *
     */
    public DoubleProperty overrideRow;

    /**
     *
     * @param value
     */
    public void setOverrideRow(Double value) {
        overrideRowProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Double getOverrideRow() {
        return overrideRowProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty overrideRowProperty() {
        if (overrideRow == null) {
            overrideRow = new SimpleDoubleProperty();
        }
        return overrideRow;
    }

    /**
     *
     */
    public DoubleProperty overrideColumn;

    /**
     *
     * @param value
     */
    public void setOverrideColumn(Double value) {
        overrideColumnProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Double getOverrideColumn() {
        return overrideColumnProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty overrideColumnProperty() {
        if (overrideColumn == null) {
            overrideColumn = new SimpleDoubleProperty();
        }
        return overrideColumn;
    }

    /**
     *
     */
    public List<AppableControl> controlsQueue;

    /**
     *
     * @return
     */
    public List<AppableControl> getControlsQueue() {
        if (controlsQueue == null) {
            controlsQueue = new ArrayList<>();
        }
        return controlsQueue;
    }

    /**
     *
     */
    public void resetControlsQueue() {
        getControlsQueue().clear();
    }

    private IntegerProperty emptyCells;

    /**
     *
     */
    public void decrementEmptyCells() {
        setEmptyCells(getEmptyCells() - 1);
    }

    /**
     *
     * @param value
     */
    public void setEmptyCells(Integer value) {
        emptyCellsProperty().setValue(value);
    }

    private boolean hasEmptyCells() {
        return getEmptyCells() > 0;
    }

    /**
     *
     * @return
     */
    public Integer getNumOfEmptyCells() {
        return getSpecRows() * getSpecColumns() - getControlsQueue().size();
    }

    /**
     *
     * @return
     */
    public Integer getEmptyCells() {
        return emptyCellsProperty().getValue();
    }

    /**
     *
     * @return
     */
    public IntegerProperty emptyCellsProperty() {
        if (emptyCells == null) {
            emptyCells = new SimpleIntegerProperty(0);
        }
        return emptyCells;
    }

    private IntegerProperty overlappingCells;

    /**
     *
     */
    public void decrementOverlappingCells() {
        setOverlappingCells(getOverlappingCells() - 1);
    }

    /**
     *
     * @param value
     */
    public void setOverlappingCells(Integer value) {
        overlappingCellsProperty().setValue(value);
    }

    private boolean hasOverlappingCells() {
        return getOverlappingCells() > 0;
    }

    /**
     *
     * @return
     */
    public Integer getOverlappingCells() {
        return overlappingCellsProperty().getValue();
    }

    /**
     *
     * @return
     */
    public IntegerProperty overlappingCellsProperty() {
        if (overlappingCells == null) {
            overlappingCells = new SimpleIntegerProperty(0);
        }
        return overlappingCells;
    }

    private ObjectProperty<ScreenStatus> status;

    /**
     *
     * @param value
     */
    public void setStatus(ScreenStatus value) {
        statusProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public ScreenStatus getStatus() {
        return statusProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<ScreenStatus> statusProperty() {
        if (status == null) {
            status = new SimpleObjectProperty<>(ScreenStatus.CLOSED);
        }
        return status;
    }

}
