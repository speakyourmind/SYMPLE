/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Region;
import org.symfound.controls.user.AnimatedPane;
import org.symfound.device.Device;
import org.symfound.device.hardware.Hardware;
import org.symfound.main.FullSession;
import org.symfound.controls.device.DeviceManager;

/**
 *
 * @author Javed Gangjee
 * @param <T>
 */
public abstract class ScreenControl<T extends Labeled> extends AnimatedPane {

    /**
     *
     */
    public static final String CSS_PATH = "/styles/default.css";

    /**
     *
     */
    public String cssClass;

    /**
     *
     */
    public ScreenControl() {
        this("");
    }

    /**
     *
     * @param CSSClass
     */
    public ScreenControl(String CSSClass) {
        this.cssClass = CSSClass;
        loadContents();
    }

    private void loadContents() {
        loadPrimaryControl();
    }

    // Sizing methods

    /**
     *
     * @param region
     */
    public static void setSizeMax(Region region) {
        setSize(region, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     *
     * @param region
     * @param width
     * @param height
     */
    public static void setSize(Region region, Double width, Double height) {
        Platform.runLater(() -> {
            region.setMaxWidth(width);
            region.setMaxHeight(height);
        });
    }

    // Adding methods
    /**
     *
     * @param node
     */
    public void addToPane(Node node) {
        addToPane(node, 0.0, 0.0, 0.0, 0.0);
    }

    /**
     *
     * @param node
     * @param top
     * @param left
     * @param bottom
     * @param right
     */
    public void addToPane(Node node, Double top, Double left, Double bottom, Double right) {
        setTopAnchor(node, top);
        setLeftAnchor(node, left);
        setRightAnchor(node, right);
        setBottomAnchor(node, bottom);
        getChildren().add(node);
    }

    /**
     *
     * @param control
     */
    public final void load(Control control) {
        setSizeMax(control);
        addToPane(control);
    }

    // CSS Methods

    /**
     *
     * @param CSSClass
     * @param parent
     */
    public void setCSS(String CSSClass, Parent parent) {
        parent.getStylesheets().add(CSS_PATH);
        parent.getStyleClass().add(CSSClass);
        symStyleProperty().addListener((observable, oldValue, newValue) -> {
            List<String> cssClasses = new ArrayList<>();
            if (newValue.contains(",")) {
                cssClasses = Arrays.asList(newValue.split(","));
            } else {
                cssClasses.add(newValue);
            }
            parent.getStyleClass().remove(CSSClass);
            cssClasses.stream().forEach((styleClass) -> {
                parent.getStyleClass().add(styleClass);
            });
        });
    }

    /**
     *
     * @param path
     */
    public void setImage(String path) {
        File file = new File(path);
        String image = file.toURI().toString();
        getPrimaryControl().setStyle("");
        getPrimaryControl().setStyle("-fx-background-image: url(\"" + image + "\");"
                + "-fx-background-size: cover;\n"
                + "-fx-background-color:-fx-light;\n"
                + "-fx-background-repeat: no-repeat;\n"
                + "-fx-background-position: center center;");

    }

    // Hardware getter methods

    /**
     *
     * @return
     */
    public Hardware getSelectedHardware() {
        final DeviceManager deviceManager = getSession().getDeviceManager();
        String deviceName = deviceManager.getIterator().get();
        Device device = deviceManager.get(deviceName);
        Hardware hardware = device.getHardware();
        return hardware;
    }

    /**
     *
     * @return
     */
    public Hardware getCurrentHardware() {
        final FullSession session = getSession();
        final DeviceManager deviceManager = session.getDeviceManager();
        return deviceManager.getCurrent().getHardware();
    }

    // Properties
    /**
     * Gets the text on the root <code>AnimatedButton</code>
     *
     * @return
     */
    public String getText() {
        return textProperty().get();
    }

    /**
     * Sets the text on the root <code>AnimatedButton</code>
     *
     * @param value
     */
    public void setText(String value) {
        textProperty().set(value);
    }

    /**
     * Represents the text on the root <code>AnimatedButton</code>
     *
     * @return <code>AnimatedButton</code> textProperty
     */
    public StringProperty textProperty() {
        return getPrimaryControl().textProperty();
    }

    /**
     *
     */
    public StringProperty symStyle;

    /**
     *
     * @return
     */
    public String getSymStyle() {
        return symStyleProperty().get();
    }

    /**
     *
     * @param value
     */
    public void setSymStyle(String value) {
        symStyleProperty().set(value);
    }

    // TO DO: Rename to something generic

    /**
     *
     * @return
     */
    public StringProperty symStyleProperty() {
        if (symStyle == null) {
            symStyle = new SimpleStringProperty("");
        }
        return symStyle;
    }

    /**
     *
     */
    public static enum ControlType {

        /**
         *
         */
        SETTING_CONTROL,

        /**
         *
         */
        STANDARD_CONTROL
    }

    /**
     *
     */
    public static final ControlType DEFAULT_CONTROL = ControlType.STANDARD_CONTROL;

    /**
     *
     */
    public ObjectProperty<ControlType> controlType;

    /**
     *
     * @return
     */
    public ControlType getControlType() {
        return controlTypeProperty().get();
    }

    /**
     *
     * @param value
     */
    public void setControlType(ControlType value) {
        controlTypeProperty().set(value);
    }

    /**
     *
     * @return
     */
    public ObjectProperty<ControlType> controlTypeProperty() {
        if (controlType == null) {
            controlType = new SimpleObjectProperty<>(DEFAULT_CONTROL);
        }
        return controlType;
    }

    /**
     *
     */
    public T primary;

    private ObjectProperty<T> primaryControl;

    /**
     *
     */
    public abstract void loadPrimaryControl();

    /**
     *
     * @param value
     */
    public void setPrimaryControl(T value) {
        primaryControlProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public T getPrimaryControl() {
        return primaryControlProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<T> primaryControlProperty() {
        if (primaryControl == null) {
            primaryControl = new SimpleObjectProperty(primary);
        }
        return primaryControl;
    }

}
