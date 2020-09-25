/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system.dialog;

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
public abstract class OKCancelDialog extends OKDialog {

    /**
     *
     */
    public AnimatedButton cancelButton;

    /**
     *
     */
    public String cancelText = "CANCEL";

    /**
     *
     * @param titleText
     * @param captionText
     * @param okText
     * @param cancelText
     */
    public OKCancelDialog(String titleText, String captionText, String okText, String cancelText) {
        super(titleText, captionText, okText);
        this.cancelText = cancelText;
    }

    /**
     *
     * @param hpos
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    @Override
    public BuildableGrid buildActionGrid(HPos hpos, Double maxWidth, Double maxHeight) {
        BuildableGrid grid = addActionGrid(hpos, maxWidth, maxHeight);
        addOKButton(grid);
        addCancelButton(grid);
        configureActions();
        return grid;
    }

    /**
     *
     * @param hpos
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public BuildableGrid addActionGrid(HPos hpos, Double maxWidth, Double maxHeight) {
        BuildableGrid grid = new BuildableGrid();
        //  GridPane.setValignment(grid, VPos.BOTTOM);
        GridPane.setHalignment(grid, hpos);
        grid.setMaxWidth(maxWidth);
        grid.setMaxHeight(maxHeight);
        grid.setHgap(BuildableGrid.DEFAULT_GRID_GAP);
        grid.setVgap(BuildableGrid.DEFAULT_GRID_GAP);
        grid.setSpecRows(1);
        grid.setSpecColumns(2);
        grid.build();
        return grid;
    }

    /**
     *
     * @param grid
     */
    public void addOKButton(BuildableGrid grid) {
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
     * @param grid
     */
    public void addCancelButton(BuildableGrid grid) {
        cancelButton = new AnimatedButton("");
        cancelButton.getStyleClass().add("settings-button");
        cancelButton.setMaxWidth(Double.POSITIVE_INFINITY);
        cancelButton.setMaxHeight(Double.POSITIVE_INFINITY);
        GridPane.setValignment(cancelButton, VPos.BOTTOM);
        GridPane.setHalignment(cancelButton, HPos.LEFT);
        cancelButton.setText("CANCEL");// TODO: cancelText
        grid.add(cancelButton, 1, 0);
    }

    /**
     *
     */
    @Override
    public void configureActions() {
        okButton.setOnMouseClicked((MouseEvent e) -> {
            onOk();
            setDone(true);
        });
        cancelButton.setOnMouseClicked((MouseEvent e) -> {
            onCancel();
            setDone(true);
        });
    }

    /**
     *
     */
    public abstract void onCancel();

}
