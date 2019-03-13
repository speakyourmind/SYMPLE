/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.util.Arrays;
import java.util.List;
import javafx.geometry.Pos;
import org.symfound.controls.ScreenControl;
import static org.symfound.controls.user.CommonGrid.DEFAULT_GRID_GAP;
import org.symfound.device.emulation.input.mouse.MouseSelectionType;

/**
 *
 * @author Javed Gangjee
 */
public class DesktopGrid extends BuildableGrid {

    /**
     *
     */
    public static final String OSK_KEY = "OSK";

    /**
     *
     */
    public static final List<String> DESKTOP_KEYS = Arrays.asList(
            MouseSelectionType.LEFT,
            MouseSelectionType.RIGHT,
            MouseSelectionType.DOUBLE,
            MouseSelectionType.SCROLL,
            MouseSelectionType.DRAG,
            MouseSelectionType.ZOOM,
            MouseSelectionType.OFF,
            SettingsButton.KEY,
            OSK_KEY);

    /**
     *
     */
    public BuildableGrid grid;

    /**
     *
     */
    public DesktopGrid() {
        this.getStylesheets().add(ScreenControl.CSS_PATH);
        this.getStyleClass().add("transparent");
        setSpecColumns(2);
        setSpecRows(4);
        setMaxWidth(Double.POSITIVE_INFINITY);
        setMaxHeight(Double.POSITIVE_INFINITY);
        setHgap(DEFAULT_GRID_GAP);
        setVgap(DEFAULT_GRID_GAP);
        setOpacity(1.0);
        build();

        addClickButton(MouseSelectionType.LEFT, "Left Click", 0, 0);
        addClickButton(MouseSelectionType.RIGHT, "Right Click", 1, 0);
        addClickButton(MouseSelectionType.DOUBLE, "Double Click", 0, 1);
        addClickButton(MouseSelectionType.SCROLL, "Scroll", 1, 1);
        addClickButton(MouseSelectionType.DRAG, "Drag", 0, 2);
        addClickButton(MouseSelectionType.ZOOM, "Zoom", 1, 2);

        //TODO: Allow opening a customized OSK    
        /*StandalonePopupButton osk = new StandalonePopupButton(SCREENS_FOLDER + "keyboard/onscreen");
        osk.setSymStyle("desktop-keyboard");
        osk.setAlignment(Pos.BOTTOM_CENTER);
        osk.setText("Keyboard");
        add(osk, 0, 3);*/
        //NavigateButton homeButton = new NavigateButton("default");
        ScriptButton homeButton = new ScriptButton("home");
        homeButton.setNavigateIndex("home");
        //homeButton.setSymStyle("home-button");
        //homeButton.setConfirmable(false);
        homeButton.setText("Home");
        //homeButton.setAlignment(Pos.BOTTOM_CENTER);
        add(homeButton, 0, 3, 2, 1);
    }

    private void addClickButton(String clickType, String text, Integer column, Integer row) {
        DesktopButton clickButton = new DesktopButton();
        clickButton.setClickType(clickType);
        clickButton.setAlignment(Pos.BOTTOM_CENTER);
        clickButton.setText(text);
        add(clickButton, column, row);
    }
}
