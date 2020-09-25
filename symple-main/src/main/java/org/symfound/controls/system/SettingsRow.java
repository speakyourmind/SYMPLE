/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system;

import java.util.Arrays;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import static org.symfound.controls.ScreenControl.CSS_PATH;
import org.symfound.controls.user.AnimatedPane;
import org.symfound.controls.user.BuildableGrid;

/**
 *
 * @author Javed Gangjee
 */
public class SettingsRow extends BuildableGrid {

    private TitledLabel titledLabel;

    private AnimatedPane background;

    /**
     *
     */
    public static final int DEFAULT_COLUMNS = 3;

    /**
     *
     */
    public static final int DEFAULT_ROWS = 1;

    /**
     *
     */
    public SettingsRow() {
        initialize();
    }

    private void initialize() {
        getStylesheets().add(CSS_PATH);
        getStyleClass().add("transparent");
        build();

        background = new AnimatedPane();
        background.getStyleClass().add("settings-pane");
        add(background, 0, 0, 3, 1);

        titledLabel = new TitledLabel();

        titledLabel.setMaxHeight(Double.POSITIVE_INFINITY);
        titledLabel.setMaxWidth(Double.POSITIVE_INFINITY);
        titleTextProperty().bindBidirectional(titledLabel.titleTextProperty());
        helpTextProperty().bindBidirectional(titledLabel.captionTextProperty());
        add(titledLabel, 0, 0);
    }

    /**
     *
     */
    public StringProperty titleText;

    /**
     *
     * @return
     */
    public String getTitleText() {
        return titleTextProperty().get();
    }

    /**
     *
     * @param value
     */
    public void setTitleText(String value) {
        titleTextProperty().set(value);
    }

    /**
     *
     * @return
     */
    public final StringProperty titleTextProperty() {
        if (titleText == null) {
            titleText = new SimpleStringProperty("");
        }
        return titleText;
    }

    /**
     *
     */
    public StringProperty helpText;

    /**
     *
     * @return
     */
    public String getHelpText() {
        return helpTextProperty().get();
    }

    /**
     *
     * @param value
     */
    public void setHelpText(String value) {
        helpTextProperty().set(value);
    }

    /**
     *
     * @return
     */
    public final StringProperty helpTextProperty() {
        if (helpText == null) {
            helpText = new SimpleStringProperty("");
        }
        return helpText;
    }

    @Override
    public final void build() {
        setHgap(BuildableGrid.DEFAULT_GRID_GAP);
        setVgap(40.0);

        setSpecRows(DEFAULT_ROWS);
        buildRows();

        setSpecColumns(DEFAULT_COLUMNS);
        List<Double> columnPercentages = Arrays.asList(30.0, 20.0, 50.0);
        buildColumnsByPerc(columnPercentages);
    }
}
