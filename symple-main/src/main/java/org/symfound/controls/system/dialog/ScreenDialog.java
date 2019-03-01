/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system.dialog;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.symfound.builder.session.Display;
import org.symfound.controls.ScreenControl;
import org.symfound.controls.system.TitledLabel;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.BuildableGrid;

/**
 *
 * @author Javed Gangjee
 */
public class ScreenDialog extends ScreenControl<AnimatedButton> {

    /**
     *
     */
    public static final Integer MIN_HEIGHT = 380;

    /**
     *
     */
    public static final Integer MIN_WIDTH = 520;

    /**
     *
     */
    public BuildableGrid baseGrid;

    /**
     *
     */
    public String initTitleText;

    /**
     *
     */
    public String initCaptionText;

    /**
     *
     */
    public TitledLabel titledLabel;

    /**
     *
     * @param titleText
     * @param captionText
     */
    public ScreenDialog(String titleText, String captionText) {
        getStylesheets().add(CSS_PATH);
        this.initTitleText = titleText;
        this.initCaptionText = captionText;

        initialize();

    }

    private void initialize() {
        baseGrid = buildBaseGrid();
        titledLabel = buildTitledLabel();
    }

    /**
     *
     * @return
     */
    public BuildableGrid buildBaseGrid() {
        BuildableGrid grid = new BuildableGrid();
        grid.getStyleClass().add("border-background");
        grid.setAlignment(Pos.CENTER);
        final Display display = getSession().getDisplay();

        Double width = getScreenFactor() * display.getScreenWidth();
        grid.setMaxWidth(width);
        grid.setMinWidth(MIN_WIDTH);

        Double height = getScreenFactor() * display.getScreenHeight();
        grid.setMaxHeight(height);
        grid.setMinHeight(MIN_HEIGHT);
        return grid;
    }

    public TitledLabel buildTitledLabel() {
        TitledLabel label = new TitledLabel();
        label.titleTextProperty().bindBidirectional(titleTextProperty());
        label.captionTextProperty().bindBidirectional(captionTextProperty());
        label.vBox.setSpacing(20.0);
        label.vBox.setAlignment(Pos.CENTER);
        label.getPrimaryControl().setAlignment(Pos.CENTER);
        label.getPrimaryControl().setStyle("-fx-text-fill:-fx-blue;");
        label.captionLabel.setAlignment(Pos.CENTER);
        label.captionLabel.setStyle("-fx-text-alignment:center;-fx-font-size:1.6em;");
        label.getStyleClass().add("dialog-label");
        GridPane.setHalignment(label, HPos.CENTER);
        GridPane.setValignment(label, VPos.CENTER);
        return label;
    }

    /**
     *
     * @param node
     */
    public void addToStackPane(Node node) {
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(node);
        addToPane(stackPane, 0.0, 0.0, 0.0, 0.0);
        setTopAnchor(this, 0.0);
        setLeftAnchor(this, 0.0);
        setRightAnchor(this, 0.0);
        setBottomAnchor(this, 0.0);
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
            titleText = new SimpleStringProperty(initTitleText);
        }
        return titleText;
    }

    /**
     *
     */
    public StringProperty captionText;

    /**
     *
     * @return
     */
    public String getCaptionText() {
        return captionTextProperty().get();
    }

    /**
     *
     * @param value
     */
    public void setCaptionText(String value) {
        captionTextProperty().set(value);
    }

    /**
     *
     * @return
     */
    public final StringProperty captionTextProperty() {
        if (captionText == null) {
            captionText = new SimpleStringProperty(initCaptionText);
        }
        return captionText;
    }

    /**
     *
     */
    public BooleanProperty done;

    /**
     *
     * @param value
     */
    public void setDone(Boolean value) {
        doneProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isDone() {
        return doneProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty doneProperty() {
        if (done == null) {
            done = new SimpleBooleanProperty(false);

        }
        return done;
    }

    private static final Double DEFAULT_SCREEN_FACTOR = 0.4;

    /**
     *
     */
    public Double screenFactor;

    /**
     *
     * @param value
     */
    public void setScreenFactor(Double value) {
        screenFactor = value;
    }

    /**
     *
     * @return
     */
    public Double getScreenFactor() {
        if (screenFactor == null) {
            screenFactor = DEFAULT_SCREEN_FACTOR;
        }
        return screenFactor;
    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {
        //UNUSED
    }
}
