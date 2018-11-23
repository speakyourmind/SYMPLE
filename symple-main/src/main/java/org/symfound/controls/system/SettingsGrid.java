/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import org.symfound.controls.ScreenControl;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.BuildableGrid;
import static org.symfound.controls.user.CommonGrid.DEFAULT_GRID_GAP;

/**
 *
 * @author Javed Gangjee
 */
public class SettingsGrid extends ScreenControl<AnimatedButton> {

    /**
     *
     */
    public static final double HEIGHT_PER_ROW = 200.0;

    /**
     *
     */
    public ScrollPane scrollPane;

    /**
     *
     */
    public BuildableGrid grid;

    /**
     *
     * @param numSettings
     */
    public SettingsGrid(Integer numSettings) {
        grid = new BuildableGrid();
        final Insets insets = new Insets(
                DEFAULT_GRID_GAP,
                DEFAULT_GRID_GAP * 2,
                DEFAULT_GRID_GAP,
                DEFAULT_GRID_GAP * 2);
        grid.setPadding(insets);
        grid.setPrefWidth(Double.POSITIVE_INFINITY);
        grid.setPrefHeight(numSettings * HEIGHT_PER_ROW);
        grid.setMaxWidth(grid.getPrefWidth());
        grid.setMaxHeight(grid.getPrefHeight());
        grid.setHgap(DEFAULT_GRID_GAP);
        grid.setVgap(DEFAULT_GRID_GAP);
        grid.setSpecRows(numSettings);
        grid.setSpecColumns(1);
        grid.build();
        setCSS("transparent", grid);

        scrollPane = new ScrollPane();
        scrollPane.setPadding(new Insets(5, 5, 5, 5));
        scrollPane.setFitToWidth(Boolean.TRUE);
        setSizeMax(scrollPane);
        scrollPane.setContent(grid);
        scrollPane.getStylesheets().add(CSS_PATH);
        setCSS("main", scrollPane);
        addToPane(scrollPane);
    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {
        // UNUSED
    }
}
