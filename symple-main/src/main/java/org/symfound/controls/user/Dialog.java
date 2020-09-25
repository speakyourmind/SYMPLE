/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import org.symfound.controls.ScreenControl;
import static org.symfound.text.TextOperator.EOL;

/**
 *
 * @author Javed Gangjee
 */
public class Dialog extends ScreenControl<AnimatedButton> {

    private final StackPane stackPane = new StackPane();
    private final AnimatedLabel titleLabel;
    private final AnimatedLabel contentLabel;
    private final BuildableGrid grid;

    /**
     *
     */
    public Integer columns = 0;

    /**
     *
     */
    public static final int DEFAULT_COLUMNS = 1;

    /**
     *
     */
    public Integer rows = 0;

    /**
     *
     */
    public static final int DEFAULT_ROWS = 2;

    /**
     *
     */
    public String initContentText;

    /**
     *
     */
    public String initTrueText;

    /**
     *
     * @param message
     * @param trueText
     * @param falseText
     */
    public Dialog(String message, String trueText, String falseText) {
        this.initContentText = message;

        getStylesheets().add(CSS_PATH);

        grid = configureBaseGrid();

        titleLabel = new AnimatedLabel();
        titleLabel.textProperty().bindBidirectional(titleTextProperty());
        setSizeMax(titleLabel);
        titleLabel.setStyle("-fx-text-fill: -fx-dark;" + EOL
                + "    -fx-font-size:60px;"); // TO DO: REMOVE
        titleLabel.setAlignment(Pos.CENTER);

        contentLabel = new AnimatedLabel();
        contentLabel.textProperty().bindBidirectional(contentTextProperty());
        setSizeMax(contentLabel);
        contentLabel.setStyle("-fx-text-fill: -fx-dark;" + EOL
                + "    -fx-font-size:60px;"); // TO DO: REMOVE
        contentLabel.setAlignment(Pos.CENTER);
        grid.add(contentLabel, 0, 0, 1, 1);

        cssClass = "word-1";

        grid.add(primary, 0, 1, 1, 1);
        addToPane(grid, 60.0, 60.0, 60.0, 60.0);
        initialize();
    }

    private void initialize() {
        setTopAnchor(this, 0.0);
        setLeftAnchor(this, 0.0);
        setRightAnchor(this, 0.0);
        setBottomAnchor(this, 0.0);
    }

    private BuildableGrid configureBaseGrid() {
        BuildableGrid baseGrid = new BuildableGrid();
        baseGrid.setStyle("-fx-background-color: -fx-light;"); // TO DO: REMOVE
        baseGrid.setSpecRows(DEFAULT_ROWS);
        baseGrid.setSpecColumns(DEFAULT_COLUMNS);
        baseGrid.build();
        return baseGrid;
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
            titleText = new SimpleStringProperty(initContentText);
        }
        return titleText;
    }

    /**
     *
     */
    public StringProperty contentText;

    /**
     *
     * @return
     */
    public String getContentText() {
        return contentTextProperty().get();
    }

    /**
     *
     * @param value
     */
    public void setContentText(String value) {
        contentTextProperty().set(value);
    }

    /**
     *
     * @return
     */
    public final StringProperty contentTextProperty() {
        if (contentText == null) {
            contentText = new SimpleStringProperty(initContentText);
        }
        return contentText;
    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton("");
        primary.setText(initTrueText);
        setSizeMax(primary);
        setCSS(cssClass, primary);
    }

}
