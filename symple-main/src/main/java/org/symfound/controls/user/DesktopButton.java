/*
 * Copyright (C) 2014 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.symfound.controls.user;

import images.Images;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.apache.log4j.Logger;
import org.symfound.controls.RunnableControl;
import org.symfound.device.Device;
import org.symfound.device.emulation.EmulationManager;
import org.symfound.device.processing.Processor;
import org.symfound.main.builder.UI;

/**
 *
 * @author Javed Gangjee
 */
public final class DesktopButton extends RunnableControl {

    private static final String NAME = DesktopButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public Circle circle = new Circle();

    /**
     *
     */
    public DesktopButton() {
        super("settings-button");

        clickTypeProperty().addListener((observable, oldValue, newValue) -> {

            String imgPath = "mouse_" + getClickType().toLowerCase() + ".png";
            String image = Images.class.getResource(imgPath).toExternalForm();
            getPrimaryControl().setStyle("-fx-background-image:url('" + image + "'); "
                    + "-fx-background-size: 128 128; \n"
                    + "-fx-background-repeat: no-repeat; \n"
                    + "-fx-background-position: center;");
            configureIndicator();
        });

    }

    /**
     *
     */
    public void configureIndicator() {
        circle.setFill(Paint.valueOf("2980b9"));
        circle.setRadius(20.0);
        circle.setVisible(false);
        circle.setTranslateX(20.0);
        circle.setTranslateY(20.0);

        final Device currentDevice = getSession().getDeviceManager().getCurrent();
        final Processor processor = currentDevice.getProcessor();
        final EmulationManager input = processor.getEmulationManager();
        circle.visibleProperty().bind(Bindings.equal(clickType.getValue(), input.getMouse().getAutomator().getSelectionTypeIterator().modeProperty()));
        addToPane(circle);
    }

    /**
     *
     */
    @Override
    public void run() {
        final Device currentDevice = getSession().getDeviceManager().getCurrent();
        final Processor processor = currentDevice.getProcessor();
        final EmulationManager input = processor.getEmulationManager();
        input.getMouse().getAutomator().setSelectionType(getClickType());
        circle.toFront();
        ((UI) circle.getScene().getWindow()).setIconified(true);
    }

    private StringProperty clickType;

    /**
     *
     * @param value
     */
    public void setClickType(String value) {
        clickTypeProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getClickType() {
        return clickTypeProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty clickTypeProperty() {
        if (clickType == null) {
            clickType = new SimpleStringProperty();
        }
        return clickType;
    }

}
