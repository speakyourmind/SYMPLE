/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system.dialog;

import java.util.Arrays;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;
import org.symfound.builder.session.Display;
import static org.symfound.controls.ScreenControl.CSS_PATH;
import org.symfound.controls.system.SettingsRow;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.AnimatedLabel;
import org.symfound.controls.user.AnimatedPane;
import org.symfound.controls.user.BuildableGrid;
import org.symfound.main.settings.SetResetable;

/**
 *
 * @author Javed Gangjee
 */
public abstract class EditDialog extends OKCancelDialog implements SetResetable {

    private static final String NAME = EditDialog.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);
    private String defaultTitle;

    public EditDialog(String defaultTitle) {
        super("N/A", "This configuration is not available", "UPDATE", "CANCEL");
        this.defaultTitle = defaultTitle;
        initialize();

    }

    private void initialize() {
        setScreenFactor(0.98);
        final Display display = getSession().getDisplay();
        Double width = getScreenFactor() * display.getScreenWidth();
        baseGrid.setMaxWidth(width);
        Double height = getScreenFactor() * display.getScreenHeight();
        baseGrid.setMaxHeight(height);
        setTitle(defaultTitle);
    }

    /**
     *
     */
    @Override
    public void buildDialog() {
        getStylesheets().add(CSS_PATH);
        baseGrid.getStyleClass().add("border-background");
        List<Double> rowPercentages = Arrays.asList(8.0, 92.0);

        buildBaseGrid(2, 1, rowPercentages);
        baseGrid.add(addSettingControls(), 0, 1);
        baseGrid.setVgap(0);
        AnimatedPane actionPane = buildActionPane(HPos.CENTER, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        baseGrid.add(actionPane, 0, 0);

        // titleLabel.toFront();
        // baseGrid.add(titleLabel, 0, 0);
        addToStackPane(baseGrid);
    }

    /**
     *
     * @param settings
     * @return
     */
    public static BuildableGrid buildSettingsGrid(List<SettingsRow> settings) {
        Integer numRows = settings.size();
        BuildableGrid grid = new BuildableGrid();
        grid.getStyleClass().add("background");
        grid.setAlignment(Pos.CENTER);
        grid.setSpecRows(numRows);
        grid.buildRows();
        grid.setSpecColumns(1);
        grid.buildColumnsByPerc(Arrays.asList(100.0));
        grid.setHgap(20.0);
        grid.setPrefHeight(numRows * 200);
        GridPane.setMargin(grid, new Insets(0, 40, 0, 40));

        for (int i = 0; i < settings.size(); i++) {
            grid.add(settings.get(i), 0, i, 1, 1);
        }
        return grid;
    }

    /**
     *
     * @param title
     * @param caption
     * @return
     */
    public static SettingsRow createSettingRow(String title, String caption) {

        SettingsRow settingsRow = new SettingsRow();
        settingsRow.setMaxHeight(220.0);
        settingsRow.setTitleText(title);
        settingsRow.setHelpText(caption);
        return settingsRow;
    }

    /**
     *
     * @return
     */
    public abstract Node addSettingControls();

    /**
     *
     */
    @Override
    public void onOk() {
        LOGGER.info("OK Clicked on Edit Dialog. "
                + "Setting all settings to updated values");
        setSettings();
        // getParentUI().setEditMode(Boolean.FALSE);
    }

    /**
     *
     */
    @Override
    public void onCancel() {
        LOGGER.info("CANCEL Clicked on Edit Dialog"
                + "Resetting settings to default values");
        resetSettings();
        // getParentUI().setEditMode(Boolean.FALSE);
    }

    /**
     *
     * @param hpos
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public AnimatedPane buildActionPane(HPos hpos, Double maxWidth, Double maxHeight) {
        AnimatedPane pane = addActionPane(hpos, maxWidth, maxHeight);
        final AnimatedLabel titleLabel = new AnimatedLabel("Edit");

        titleLabel.textProperty().bind(this.titleProperty());
        titleLabel.setStyle(" -fx-text-fill: -fx-light;\n"
                + "    -fx-font-size: 3em;\n"
                + "    -fx-font-weight:bold;"
                + "    -fx-padding: 0 0 0 90;");
        titleLabel.setAlignment(Pos.CENTER_LEFT);
        setTopAnchor(titleLabel, 0.0);
        setLeftAnchor(titleLabel, 0.0);
        setBottomAnchor(titleLabel, 0.0);
        titleLabel.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        pane.getChildren().add(titleLabel);
        addOKButton(pane);
        addCancelButton(pane);
        configureActions();
        return pane;
    }

    /**
     *
     * @param hpos
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public AnimatedPane addActionPane(HPos hpos, Double maxWidth, Double maxHeight) {
        AnimatedPane pane = new AnimatedPane();
        pane.setStyle("-fx-background-color:-fx-dark;");
        pane.setMaxWidth(maxWidth);
        pane.setMaxHeight(maxHeight);

        return pane;
    }

    /**
     *
     * @param pane
     */
    public void addOKButton(AnimatedPane pane) {
        okButton = new AnimatedButton("");
        okButton.getStyleClass().add("toolbar-back");
        okButton.setMaxWidth(Double.POSITIVE_INFINITY);
        okButton.setMaxHeight(Double.POSITIVE_INFINITY);
        GridPane.setValignment(okButton, VPos.TOP);
        GridPane.setHalignment(okButton, HPos.LEFT);
        setTopAnchor(okButton, 0.0);
        setLeftAnchor(okButton, 0.0);
        setBottomAnchor(okButton, 0.0);
        pane.getChildren().add(okButton);
    }

    /**
     *
     * @param pane
     */
    public void addCancelButton(AnimatedPane pane) {
        cancelButton = new AnimatedButton("");
        cancelButton.getStyleClass().add("toolbar-exit");
        cancelButton.setMaxWidth(Double.POSITIVE_INFINITY);
        cancelButton.setMaxHeight(Double.POSITIVE_INFINITY);
        GridPane.setValignment(cancelButton, VPos.TOP);
        GridPane.setHalignment(cancelButton, HPos.RIGHT);
        setRightAnchor(cancelButton, 0.0);
        setTopAnchor(cancelButton, 0.0);
        setBottomAnchor(cancelButton, 0.0);

        pane.getChildren().add(cancelButton);
    }

    private StringProperty title;

    /**
     *
     * @param value
     */
    public void setTitle(String value) {
        titleProperty().set(value);
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return titleProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty titleProperty() {
        if (title == null) {
            title = new SimpleStringProperty(defaultTitle);

        }
        return title;
    }
}
