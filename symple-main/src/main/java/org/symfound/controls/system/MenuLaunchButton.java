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
package org.symfound.controls.system;

import java.awt.Dimension;
import java.awt.Toolkit;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.apache.log4j.Logger;
import org.symfound.controls.RunnableControl;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.MenuGrid;

/**
 *
 * @author Javed Gangjee
 */
public final class MenuLaunchButton extends RunnableControl {

    private static final String NAME = MenuLaunchButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public MenuGrid menuGrid;

    /**
     *
     */
    public BooleanProperty menuVisible;

    /**
     *
     */
    public MenuLaunchButton() {
        menuGrid = new MenuGrid();
        menuGrid.visibleProperty().bindBidirectional(menuVisibleProperty());
        menuVisibleProperty().addListener((observable, oldValue, newValue) -> {
            Double selectionTime = getUser().getInteraction().getSelectionTime();
            if (newValue) {

                addToPane(menuGrid);
                Dimension dimScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Double menuHeight = (double) dimScreenSize.getHeight();

                TranslateTransition translateTransition = new TranslateTransition(
                        Duration.seconds(selectionTime), menuGrid);
                translateTransition.setFromY(-0.5 * menuHeight);
                translateTransition.setToY(0.0);
                translateTransition.setCycleCount(1);
                translateTransition.setInterpolator(Interpolator.EASE_IN);

                FadeTransition fadeTransition = new FadeTransition(
                        Duration.seconds(selectionTime * 3), menuGrid);
                fadeTransition.setFromValue(0.0);
                fadeTransition.setToValue(1.0);
                fadeTransition.setCycleCount(1);
                fadeTransition.setInterpolator(Interpolator.EASE_IN);

                ScaleTransition scaleTransition = new ScaleTransition(
                        Duration.seconds(selectionTime), menuGrid);
                scaleTransition.setFromX(0.0);
                scaleTransition.setToX(1.0);
                scaleTransition.setCycleCount(1);
                scaleTransition.setInterpolator(Interpolator.EASE_IN);

                ParallelTransition parallelTransition = new ParallelTransition();
                parallelTransition.getChildren().addAll(
                        translateTransition,
                        scaleTransition,
                        fadeTransition
                );
                parallelTransition.setCycleCount(1);
                parallelTransition.play();
            } else {
                Dimension dimScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Double menuHeight = (double) dimScreenSize.getHeight();
                TranslateTransition translateTransition = new TranslateTransition(
                        Duration.seconds(selectionTime), menuGrid);
                translateTransition.setFromY(0.0);
                translateTransition.setToY(-0.5 * menuHeight);
                translateTransition.setCycleCount(1);
                translateTransition.setInterpolator(Interpolator.EASE_IN);

                ScaleTransition scaleTransition = new ScaleTransition(
                        Duration.seconds(selectionTime), menuGrid);
                scaleTransition.setFromX(1.0);
                scaleTransition.setToX(0.0);
                scaleTransition.setCycleCount(1);
                scaleTransition.setInterpolator(Interpolator.EASE_IN);

                ParallelTransition parallelTransition = new ParallelTransition();
                parallelTransition.getChildren().addAll(
                        translateTransition,
                        scaleTransition
                );
                parallelTransition.setCycleCount(1);
                parallelTransition.play();
                parallelTransition.setOnFinished((ActionEvent e) -> {
                    getChildren().remove(menuGrid);
                });
            }

        });

        setOnMouseExited((MouseEvent e) -> {
            if (isMenuVisible()) {
                run();
            }
        });

        primary = new AnimatedButton();
        setCSS("main-menu-button", primary);
        setText("...");
        setSizeMax(primary);
        setSelection(primary);

        setLeftAnchor(primary, 0.0);
        setRightAnchor(primary, 0.0);
        getChildren().add(primary);

    }

    /**
     *
     */
    @Override
    public void run() {
        setMenuVisible(!isMenuVisible());

    }

    /**
     *
     * @return
     */
    public Boolean isMenuVisible() {
        return menuVisibleProperty().getValue();
    }

    /**
     *
     * @param value
     */
    public void setMenuVisible(Boolean value) {
        menuVisibleProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public BooleanProperty menuVisibleProperty() {
        if (menuVisible == null) {
            menuVisible = new SimpleBooleanProperty(Boolean.FALSE);
        }
        return menuVisible;
    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {

    }
}
