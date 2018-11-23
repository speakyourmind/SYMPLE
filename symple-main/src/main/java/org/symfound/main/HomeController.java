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
package org.symfound.main;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import org.apache.log4j.Logger;
import org.symfound.app.GridController;
import org.symfound.controls.user.AnimatedLabel;
import org.symfound.controls.user.ConfigurableGrid;
import org.symfound.controls.user.BuildableGrid;
import org.symfound.controls.user.SubGrid;

/**
 *
 * @author Javed Gangjee
 */
public class HomeController extends GridController {

    private static final String NAME = HomeController.class.getName();
    public static final String KEY = "Screen";
    public static final Logger LOGGER = Logger.getLogger(NAME);

    @FXML
    private BuildableGrid gpWizard;
    @FXML
    private AnimatedLabel lblCongratulations;
    @FXML
    private AnimatedLabel lblReady;
    @FXML
    private GridPane gpMain;

    /**
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //  setIndex("home");
        getSession().builtProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
               ConfigurableGrid.editModeProperty().bindBidirectional(FullSession.getMainUI().editModeProperty());
                GridPane.setRowIndex(getGrid(), 1);
                getGrid().maxHeightProperty().bind(Bindings.multiply(0.96,gpMain.heightProperty()));
                gpMain.getChildren().add(getGrid());
                
             /*   FullSession.getMainUI().getStack().currentProperty().addListener((observable1, oldValue1, newValue1) -> {
                    if (newValue1.get().equalsIgnoreCase(FullSession.HOME)) {
                        if (getUser().getProfile().isFirstUse()) {
                            gpWizard.toFront();
                            // TO DO : Use Sequential Transition
                            DelayedEvent delayedEvent1 = new DelayedEvent();
                            delayedEvent1.setup(1.0, (ActionEvent e) -> {
                                lblCongratulations.animate().startFade(1.0, 0.0, 1.0);

                            });
                            DelayedEvent delayedEvent2 = new DelayedEvent();
                            delayedEvent2.setup(3.0, (ActionEvent e) -> {
                                lblReady.animate().startFade(1.0, 0.0, 1.0);

                            });
                            DelayedEvent delayedEvent3 = new DelayedEvent();
                            delayedEvent3.setup(5.0, (ActionEvent e) -> {
                                gpWizard.animate().startFade(1.0, 1.0, 0.0);
                                gpWizard.animate().setOnFadeFinished((ActionEvent f) -> {
                                    apMain.getChildren().remove(gpWizard);
                                    getUser().getProfile().setFirstUse(false);
                                });
                            });
                            delayedEvent1.play();
                            delayedEvent2.play();
                            delayedEvent3.play();
                        } else {
                            apMain.getChildren().remove(gpWizard);
                        }
                    }
                });*/
            }

        });
    }

    private static SubGrid grid;

    public static SubGrid getGrid() {
        if (grid == null) {
            grid = new SubGrid("home");
            grid.getConfigurableGrid().setRootGrid(true);
        }
        return grid;
    }


}
