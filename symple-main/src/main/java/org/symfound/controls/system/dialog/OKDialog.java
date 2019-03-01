/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system.dialog;

import java.util.Arrays;
import java.util.List;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.BuildableGrid;

/**
 *
 * @author Javed Gangjee
 */
public abstract class OKDialog extends ScreenDialog {

    /**
     *
     */
    public BuildableGrid actionGrid;

    /**
     *
     */
    public AnimatedButton okButton;

    /**
     *
     */
    public String okText;

    /**
     *
     * @param titleText
     * @param captionText
     * @param okText
     */
    public OKDialog(String titleText, String captionText, String okText) {
        super(titleText, captionText);
        this.okText = okText;
        initialize();
    }

    private void initialize() {
        buildDialog();
    }

    /**
     *
     */
    public void buildDialog() {
        getStylesheets().add(CSS_PATH);
        List<Double> rowPercentages = Arrays.asList(65.0, 35.0);
        buildBaseGrid(2, 1, rowPercentages);
        baseGrid.add(titledLabel, 0, 0);
        actionGrid = buildActionGrid(HPos.CENTER, 360.0, 60.0);
        baseGrid.add(actionGrid, 0, 1);
        addToStackPane(baseGrid);
    }

    /**
     *
     * @param rows
     * @param columns
     * @param rowPercentages
     */
    public void buildBaseGrid(Integer rows, Integer columns, List<Double> rowPercentages) {
        baseGrid.setSpecRows(rows);
        baseGrid.buildRowByPerc(rowPercentages);
        baseGrid.setSpecColumns(columns);
        baseGrid.buildColumns();
    }

    /**
     *
     * @param hpos
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public BuildableGrid buildActionGrid(HPos hpos, Double maxWidth, Double maxHeight) {
        BuildableGrid grid = new BuildableGrid();
        //  GridPane.setValignment(grid, VPos.BOTTOM);
        GridPane.setHalignment(grid, hpos);
        grid.setMaxWidth(maxWidth);
        grid.setMaxHeight(maxHeight);
        grid.setHgap(BuildableGrid.DEFAULT_GRID_GAP);
        grid.setVgap(BuildableGrid.DEFAULT_GRID_GAP);
        grid.setSpecRows(1);
        grid.setSpecColumns(1);
        grid.build();

        addOKButton(grid);
        configureActions();
        return grid;
    }

    private void addOKButton(BuildableGrid grid) {
        okButton = new AnimatedButton("");
        okButton.getStyleClass().add("settings-button");
        okButton.setMaxWidth(Double.POSITIVE_INFINITY);
        okButton.setMaxHeight(Double.POSITIVE_INFINITY);
        GridPane.setValignment(okButton, VPos.BOTTOM);
        GridPane.setHalignment(okButton, HPos.RIGHT);
        okButton.setText(okText);
        grid.add(okButton, 0, 0);
    }

    /**
     *
     */
    public void configureActions() {
        okButton.setOnMouseClicked((MouseEvent e) -> {
            onOk();
            setDone(true);
        });
    }

    /**
     *
     */
    public abstract void onOk();

}
