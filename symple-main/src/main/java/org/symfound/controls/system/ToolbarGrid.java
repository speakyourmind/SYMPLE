/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system;

import java.util.ArrayList;
import java.util.List;
import org.symfound.controls.AppableControl;
import org.symfound.controls.ColourChoices;
import org.symfound.controls.RunnableControl;
import org.symfound.controls.ScreenControl.ControlType;
import org.symfound.controls.user.ButtonGrid;
import static org.symfound.controls.user.ButtonGrid.LOGGER;
import org.symfound.controls.user.ExecButton;
import org.symfound.controls.user.ExitButton;
import org.symfound.controls.user.NavigateButton;
import org.symfound.controls.user.MinimizeButton;
import org.symfound.controls.user.SettingsButton;
import org.symfound.controls.user.VersionUpdateButton;
import org.symfound.tools.selection.ParallelList;

/**
 *
 * @author Javed Gangjee
 */
public class ToolbarGrid extends ButtonGrid {

    private static final Integer DEFAULT_ROWS = 1;
    private static final double CONTROL_MAX_HEIGHT = 200.0;
    private static final double CONTROL_MAX_WIDTH = 75.0;

    /**
     *
     */
    public ToolbarGrid() {
        super();
    }

    /**
     *
     * @param buildOrder
     * @param method
     * @param direction
     */
    @Override
    public void build(ParallelList<String, String> buildOrder, FillMethod method, FillDirection direction, Double size) {
        clear();
        fill(buildOrder, null);
        setSpecRows(getRowSize(null));
        setSpecColumns(getColumnSize(null));
        configure(getControlsQueue(), method, direction);
    }

    /**
     *
     * @param rowSize
     * @return
     */
    @Override
    public int getRowSize(Double rowSize) {
        return DEFAULT_ROWS;
    }

    /**
     *
     * @param columnSize
     * @return
     */
    @Override
    public int getColumnSize(Double columnSize) {
        return getControlsQueue().size();
    }

    /**
     *
     * @param controlsQueue
     * @param method
     * @param direction
     */
    @Override
    public void configure(List<AppableControl> controlsQueue, FillMethod method, FillDirection direction) {
        build();
        setPrefHeight(CONTROL_MAX_HEIGHT);
        setMaxHeight(CONTROL_MAX_HEIGHT);
        for (int i = 0; i < getControlsQueue().size(); i++) {
            final RunnableControl control = getControlsQueue().get(i);
            control.setControlType(ControlType.SETTING_CONTROL);
            control.setPrefWidth(CONTROL_MAX_WIDTH);
            control.setPrefHeight(CONTROL_MAX_HEIGHT);
            control.setMaxWidth(CONTROL_MAX_WIDTH);
            control.setMaxHeight(CONTROL_MAX_HEIGHT);
            add(control, i, 0);
        }
    }

    @Override
    public void fill(ParallelList<String, String> buildOrder, Integer size) {
        List<AppableControl> requestedControls = new ArrayList<>();
        for (int i = 0; i < buildOrder.getFirstList().size(); i++) {
            String toBuild = buildOrder.getFirstList().get(i);
            switch (toBuild.trim()) {
                case EditButton.KEY:
                    EditButton edit = new EditButton();
                    edit.setControlType(ControlType.SETTING_CONTROL);
                    edit.setConfirmable(Boolean.TRUE);
                    edit.setPane("apMain");
                    edit.setOkText("CONFIRM");
                    edit.setCancelText("CANCEL");
                    requestedControls.add(edit);
                    break;
                case NavigateButton.KEY:
                    NavigateButton homeButton = new NavigateButton("home", "toolbar.home");
                    homeButton.setControlType(ControlType.SETTING_CONTROL);
                    homeButton.setEditable(Boolean.FALSE);
                    homeButton.getStyleClass().clear();
                    homeButton.getStyleClass().add("toolbar-home");
                    homeButton.setNavigateIndex("home");
                    homeButton.setSymStyle("toolbar-home");// TODO: Fix;
                    homeButton.setBackgroundColour(ColourChoices.DARK);
                    homeButton.setBackgroundURL("/images/home_small.png");
                    // homeButton.disablePrimaryProperty().bindBidirectional(ConfigurableGrid.editModeProperty());
                    homeButton.setOverrideStyle("-fx-background-size:20 20;-fx-background-position: center;-fx-background-repeat:no-repeat;");
                    requestedControls.add(homeButton);
                    break;
                case VersionUpdateButton.KEY:
                    VersionUpdateButton updateButton = new VersionUpdateButton();
                    updateButton.setControlType(ControlType.SETTING_CONTROL);
                    updateButton.setEditable(Boolean.FALSE);

                    //  updateButton.getStyleClass().remove("subgrid");
                    updateButton.setSymStyle("toolbar-update");
                    updateButton.setConfirmable(Boolean.TRUE);
                    updateButton.setPane("apMain");
                    updateButton.setOkText("CONFIRM");
                    updateButton.setCancelText("CANCEL");
                    requestedControls.add(updateButton);
                    break;
                case SettingsButton.KEY:
                    SettingsButton settingsButton = new SettingsButton();
                    settingsButton.setControlType(ControlType.SETTING_CONTROL);
                    settingsButton.setEditable(Boolean.FALSE);
                    // settingsButton.getStyleClass().remove("subgrid");
                    settingsButton.setSymStyle("toolbar-settings");
                    requestedControls.add(settingsButton);
                    break;
                case MinimizeButton.KEY:
                    MinimizeButton minimizeButton = new MinimizeButton();
                    minimizeButton.setControlType(ControlType.SETTING_CONTROL);
                    //  minimizeButton.getStyleClass().remove("subgrid");
                    minimizeButton.setEditable(Boolean.FALSE);

                    minimizeButton.setSymStyle("toolbar-minimized");
                    requestedControls.add(minimizeButton);
                    break;
                case ExitButton.KEY:
                    ExitButton exitButton = new ExitButton();
                    //    exitButton.getStyleClass().remove("subgrid");
                    exitButton.setControlType(ControlType.SETTING_CONTROL);
                    exitButton.setEditable(Boolean.FALSE);

                    exitButton.setSymStyle("toolbar-exit");
                    exitButton.setConfirmable(Boolean.TRUE);
                    exitButton.setPane("apMain");
                    exitButton.setOkText("CONFIRM");
                    exitButton.setCancelText("CANCEL");
                    requestedControls.add(exitButton);
                    break;
                case ExecButton.RESTART_KEY:
                    ExecButton execButton = new ExecButton("toolbar-power", ExecButton.RESTART_COMMAND);
                    execButton.setConfirmable(Boolean.TRUE);
                    execButton.setTitleText("Restart Your Computer");
                    execButton.setCaptionText("This will force a shutdown your computer and restart.");
                    execButton.setPane("apMain");
                    execButton.setOkText("CONFIRM");
                    execButton.setCancelText("CANCEL");
                    requestedControls.add(execButton);
                    break;

            }
        }

        resetControlsQueue();

        if (requestedControls.size() > 0) {
            getControlsQueue().addAll(requestedControls);
        } else {
            LOGGER.warn("No controls available!");
        }
    }

}
