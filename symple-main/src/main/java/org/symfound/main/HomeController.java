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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;
import org.symfound.app.GridController;
import static org.symfound.controls.ScreenControl.CSS_PATH;
import static org.symfound.controls.ScreenControl.setSizeMax;
import org.symfound.controls.user.AnimatedLabel;
import org.symfound.controls.user.BuildableGrid;
import org.symfound.controls.user.ConfigurableGrid;
import org.symfound.controls.user.SubGrid;

/**
 *
 * @author Javed Gangjee
 */
public class HomeController extends GridController {

    private static final String NAME = HomeController.class.getName();

    /**
     *
     */
    public static final String KEY = "Screen";

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    private static BooleanProperty updated = new SimpleBooleanProperty(true);

    /**
     *
     * @param value
     */
    public static void setUpdated(Boolean value) {
        updatedProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public static boolean isUpdated() {
        return updatedProperty().getValue();
    }

    /**
     *
     * @return
     */
    public static BooleanProperty updatedProperty() {
        if (updated == null) {
            updated = new SimpleBooleanProperty(false);
        }
        return updated;
    }

    @FXML
    private BuildableGrid gpWizard;
    @FXML
    private AnimatedLabel lblCongratulations;
    @FXML
    private AnimatedLabel lblReady;
    @FXML
    private GridPane gpMain;
    @FXML
    private AnchorPane apMain;

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

                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setPadding(new Insets(0));
                scrollPane.setFitToHeight(Boolean.TRUE);
                scrollPane.setFitToWidth(Boolean.TRUE);
                setSizeMax(scrollPane);
                scrollPane.setContent(getGrid());
                scrollPane.getStylesheets().add(CSS_PATH);
                GridPane.setRowIndex(scrollPane, 1);

                gpMain.getChildren().add(scrollPane);

                //getGrid().maxHeightProperty().bind(Bindings.multiply(0.96, gpMain.heightProperty()));
            }

        });
    }

    private static SubGrid grid;

    /**
     *
     * @return
     */
    public static SubGrid getGrid() {
        if (grid == null) {
            grid = new SubGrid("home");
            grid.getConfigurableGrid().setRootGrid(true);
            updatedProperty().addListener((observable2, oldValue2, newValue2) -> {
                if (newValue2) {
                    grid.getConfigurableGrid().triggerReload();
                    HomeController.setUpdated(false);
                }
            });
            setSizeMax(grid);
        }
        return grid;
    }

}
