/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import org.symfound.controls.ScreenControl;
import static org.symfound.controls.ScreenControl.CSS_PATH;
import org.symfound.controls.user.AnimatedLabel;

/**
 *
 * @author Javed Gangjee
 */
public class TitledLabel extends ScreenControl<AnimatedLabel> {

    private static final Boolean DEFAULT_WRAP_TEXT = Boolean.TRUE;

    /**
     *
     */
    public final AnimatedLabel captionLabel;

    /**
     *
     */
    public final VBox vBox;

    /**
     *
     */
    public TitledLabel() {
        super("settings-title");
        this.getPrimaryControl().setWrapText(Boolean.FALSE);
        /*  Font font=Font.font("Roboto", FontWeight.BOLD,18);
        this.getPrimaryControl().setFont(font);
         */
        vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(primary);
        captionLabel = new AnimatedLabel();
        setCSS("settings-caption", captionLabel);
        captionLabel.setWrapText(DEFAULT_WRAP_TEXT);
        captionLabel.setAlignment(Pos.CENTER_LEFT);
        setSizeMax(captionLabel);
        captionTextProperty().bindBidirectional(captionLabel.textProperty());
        vBox.getChildren().add(captionLabel);

        addToPane(vBox);

    }

    /**
     *
     * @param CSSClass
     * @param parent
     */
    @Override
    public final void setCSS(String CSSClass, Parent parent) {
        parent.getStylesheets().add(CSS_PATH);
        parent.getStyleClass().add(CSSClass);
        symStyleProperty().addListener((observable, oldValue, newValue) -> {
            parent.getStyleClass().add(newValue);
        });

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
            captionText = new SimpleStringProperty("");
        }
        return captionText;
    }

    /**
     *
     */
    public DoubleProperty spacing;

    /**
     *
     * @return
     */
    public Double getSpacing() {
        return spacingProperty().get();
    }

    /**
     *
     * @param value
     */
    public void setSpacing(Double value) {
        spacingProperty().set(value);
    }

    /**
     *
     * @return
     */
    public final DoubleProperty spacingProperty() {
        if (spacing == null) {
            spacing = new SimpleDoubleProperty(5.0);
            vBox.spacingProperty().bindBidirectional(spacingProperty());
        }
        return spacing;
    }

    private ObjectProperty<Pos> alignment;

    /**
     *
     * @param value
     */
    public final void setAlignment(Pos value) {
        alignmentProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public final Pos getAlignment() {
        return alignmentProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<Pos> alignmentProperty() {
        if (alignment == null) {
            alignment = new SimpleObjectProperty<>();
            alignment.bindBidirectional(vBox.alignmentProperty());
        }
        return alignment;
    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedLabel();
        setCSS("settings-label", primary);
        primary.setWrapText(DEFAULT_WRAP_TEXT);
        primary.setAlignment(Pos.CENTER_LEFT);
        setSizeMax(primary);
        titleTextProperty().bindBidirectional(primary.textProperty());
    }

}
