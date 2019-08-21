/*
 * Copyright (C) 2015 SpeakYourMind Foundation
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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;
import org.symfound.controls.RunnableControl;

/**
 *@deprecated 
 * @author Javed Gangjee
 */
public final class TierLaunchButton extends RunnableControl {

    private BooleanProperty launch;
    private StringProperty tier;

    /**
     *
     */
    public Pane pane;

    /**
     *
     */
    public Pane root;

    /**
     *
     */
    public TierLaunchButton() {
        super();
        root = (Pane) getParent();
        pane = new Pane();
        pane.visibleProperty().bindBidirectional(root.disableProperty());

        launchedProperty().addListener((observable, oldValue, newValue) -> {
            if (!getPane().isEmpty()) {
                // Or it can lookup another pane in the scene.
                pane = (Pane) getScene().lookup(getPane());
            }
            // Hide the top tier
            pane.setVisible(!pane.isVisible());
        });
    }

    @Override
    public void run() {
        launch();
    }

    /**
     * Configures the name of the tier in the scene to be paused.
     *
     * @param value tier fx:id
     */
    public void setPane(String value) {
        tierProperty().setValue(value);
    }

    /**
     * Gets the requested id of the tier that is to be paused in the scene.
     *
     * @return tier fx:id
     */
    public String getPane() {
        return tierProperty().getValue();
    }

    /**
     * Represents the name of the tier to lookup within the scene whose children
     * are to be paused/disabled.
     *
     * @return tier
     */
    public StringProperty tierProperty() {
        if (tier == null) {
            tier = new SimpleStringProperty("");
        }
        return tier;
    }

    /**
     * Toggles the paused boolean property.
     */
    public void launch() {
        launchedProperty().setValue(!isLaunched());
    }

    /**
     * Finds out whether the pause button is active.
     *
     * @return paused if true, false otherwise
     */
    public Boolean isLaunched() {
        return launchedProperty().getValue();
    }

    /**
     * Represents the pause function of the button.
     *
     * @return paused
     */
    public BooleanProperty launchedProperty() {
        if (launch == null) {
            launch = new SimpleBooleanProperty(false);
        }
        return launch;
    }

}
