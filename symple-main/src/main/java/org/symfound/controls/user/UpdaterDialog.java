/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.util.Arrays;
import java.util.List;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.ProgressBar;
import static org.symfound.controls.ScreenControl.CSS_PATH;
import org.symfound.controls.system.dialog.OKDialog;
import static org.symfound.main.Main.getVersionManager;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public class UpdaterDialog extends OKDialog {

    ProgressBar progressBar;

    public UpdaterDialog() {
        super("Updating...", "SYMPLE will close when the download is complete", "ABORT");
    }

    @Override
    public void onOk() {
        getVersionManager().msiDownloader.abort();
    }

    @Override
    public void buildDialog() {
        getStylesheets().add(CSS_PATH);
        List<Double> rowPercentages = Arrays.asList(55.0, 10.0, 35.0);
        buildBaseGrid(3, 1, rowPercentages);
        baseGrid.add(titledLabel, 0, 0);
        progressBar = new ProgressBar();
        progressBar.setMaxWidth(Double.POSITIVE_INFINITY);
        progressBar.setPrefHeight(30.0);
        progressBar.getStylesheets().add(CSS_PATH);
        progressBar.setPadding(new Insets(0.0, 20.0, 0.0, 20.0));
        progressBar.getStyleClass().clear();
        progressBar.getStyleClass().add("progress-bar-dark");
        progressBar.progressProperty().bindBidirectional(getVersionManager().msiDownloader.getTracker().progressProperty());
        baseGrid.add(progressBar, 0, 1);
        actionGrid = buildActionGrid(HPos.CENTER, 180.0, 60.0);
        baseGrid.add(actionGrid, 0, 2);
        addToStackPane(baseGrid);
    }
}
