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
import java.util.Arrays;
import java.util.List;
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
import org.symfound.controls.ScreenControl;
import static org.symfound.controls.ScreenControl.CSS_PATH;
import static org.symfound.controls.ScreenControl.setSizeMax;
import static org.symfound.controls.device.SwiftySettings.DEFAULT_CONFIG_COLUMNS;
import static org.symfound.controls.device.SwiftySettings.DEFAULT_CONFIG_ROWS;
import org.symfound.controls.system.Toolbar;
import org.symfound.controls.user.ActiveTextArea;
import static org.symfound.controls.user.ActiveTextArea.getPredictor;
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
    private AnchorPane apMain;

    /**
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getSession().builtProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                AnchorPane.setTopAnchor(getScreenGrid(), 0.0);
                AnchorPane.setLeftAnchor(getScreenGrid(), 0.0);
                AnchorPane.setRightAnchor(getScreenGrid(), 0.0);
                AnchorPane.setBottomAnchor(getScreenGrid(), 0.0);
                apMain.getChildren().add(getScreenGrid());
                ConfigurableGrid.editModeProperty().bindBidirectional(FullSession.getMainUI().editModeProperty());
                getScreenGrid().getChildren().add(getToolbar());
                getScreenGrid().getChildren().add(getScrollPane());

                //getGrid().maxHeightProperty().bind(Bindings.multiply(0.96, gpMain.heightProperty()));
            }

        });
    }

    private static Toolbar toolbar;

    public Toolbar getToolbar() {
        if (toolbar == null) {
            toolbar = new Toolbar();
            toolbar.setButtonOrder("Update=default,Snapshot=default,Edit=default,User Settings=default,Minimize=default,Script=toolbar/home,Exit=default");
            GridPane.setColumnSpan(toolbar, 2);
        }
        return toolbar;
    }

    private static ScrollPane scrollPane;

    public static ScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new ScrollPane();
            scrollPane.setPadding(new Insets(0));
            setSizeMax(scrollPane);
            scrollPane.setContent(getSubGrid());
            scrollPane.getStylesheets().add(CSS_PATH);
            GridPane.setRowIndex(scrollPane, 1);
            GridPane.setColumnSpan(scrollPane, 2);
        }
        return scrollPane;
    }

    private static BuildableGrid screenGrid;

    public static GridPane getScreenGrid() {
        if (screenGrid == null) {
            screenGrid = new BuildableGrid();
            //   screenGrid.setPadding(new Insets(0,0,0,0));
            ScreenControl.setSizeMax(screenGrid);
            screenGrid.setHgap(5.0);
            screenGrid.setVgap(5.0);
            screenGrid.setSpecRows(2);
            List<Double> rowPercentages = Arrays.asList(5.0, 95.0);
            screenGrid.buildRowsByPerc(rowPercentages);

            screenGrid.setSpecColumns(2);
            List<Double> columnPercentages = Arrays.asList(10.0, 90.0);
            screenGrid.buildColumnsByPerc(columnPercentages);
            
            screenGrid.setPrefWidth(10000.0);
            screenGrid.setMaxWidth(10000.0);
            

        }
        return screenGrid;
    }

    private static SubGrid grid;

    /**
     *
     * @return
     */
    public static SubGrid getSubGrid() {
        if (grid == null) {
            grid = new SubGrid("home");
            grid.getConfigurableGrid().setRootGrid(true);
            updatedProperty().addListener((observable2, oldValue2, newValue2) -> {
                if (newValue2) {
                    grid.getConfigurableGrid().triggerReload();
                    setUpdated(false);
                }
            });
          
            setSizeMax(grid);
        }
        return grid;
    }

}
