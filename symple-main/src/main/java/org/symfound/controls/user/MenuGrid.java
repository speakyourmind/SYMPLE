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

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Node;
import static org.symfound.controls.ScreenControl.CSS_PATH;
import static org.symfound.controls.user.CommonGrid.DEFAULT_GRID_GAP;
import org.symfound.main.HomeController;
import org.symfound.main.settings.SettingsController;
import org.symfound.tools.iteration.ParallelList;

/**
 *
 * @author Javed Gangjee
 */
public final class MenuGrid extends ButtonGrid {

    /**
     *
     */
    public static final Integer MIN_SIZE = 2;

    /**
     *
     */
    public List<Node> menuList = new ArrayList<>();

    /**
     *
     */
    public MenuGrid() {
        super();
        getStylesheets().add(CSS_PATH);
        getStyleClass().add("main");
        initialize();
    }

    private void initialize() {
        getSession().builtProperty().addListener((observableList, oldValue, newValue) -> {
            if (newValue) {
                Double level = getUser().getAbility().getLevel();
                Double size = (level - 2 <= MIN_SIZE) ? MIN_SIZE : level - 2;
                reload(getValidatedKeyOrder(getUser().getNavigation().getMenuOrder()), FillMethod.ROW_WISE, FillDirection.FORWARD, size);
                HomeController.updatedProperty().addListener((observable2, oldValue2, newValue2) -> {
                    if (newValue2) {
                        reload();
                    }
                });
                getUser().getAbility().levelProperty().addListener((observable3, oldValue3, newValue3) -> {
                    reload();
                });

            }
        });
    }

    /**
     *
     */
    public void reload() {
        Double level = getUser().getAbility().getLevel();
        Double updatedSize = (level - 2 <= MIN_SIZE) ? MIN_SIZE : level - 2;
        ParallelList<String, String> order = getValidatedKeyOrder(getUser().getNavigation().getMenuOrder());
        reload(order, FillMethod.ROW_WISE, FillDirection.FORWARD, updatedSize);
    }

    /**
     *
     * @param buildOrder
     * @param method
     * @param direction
     * @param size
     */
    @Override
    public void build(ParallelList<String, String> buildOrder, FillMethod method, FillDirection direction, Double size) {
        clear();
        int rowSize = getRowSize(size);
        setSpecRows(rowSize);
        int columnSize = getColumnSize(size);
        setSpecColumns(columnSize);
        fill(buildOrder, rowSize * columnSize);

        build();
        configure(getControlsQueue(), method, direction);
        Insets insets = new Insets(DEFAULT_GRID_GAP);
        setPadding(insets);
        /*   if (!AppGrid.inEditMode()) {
            launchAnimation();
        }*/
        toBack();
    }
}
