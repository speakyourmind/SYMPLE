/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.device;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import org.symfound.controls.system.OnOffButton;
import org.symfound.controls.system.SettingsRow;
import org.symfound.controls.system.SettingsTab;
import org.symfound.controls.user.AnimatedLabel;
import org.symfound.controls.user.BuildableGrid;
import org.symfound.device.Device;
import org.symfound.device.hardware.swifty.Swifty;

/**
 *
 * @author Javed Gangjee
 */
public class SwiftySettings extends DeviceSettings<Swifty> {

    /**
     *
     */
    public static final int DEFAULT_COLUMNS = 1;

    /**
     *
     */
    public static final int DEFAULT_ROWS = 3;
    private static final int DIP_SIZE = 4;

    /**
     *
     */
    public static final int DEFAULT_CONFIG_COLUMNS = DIP_SIZE;

    /**
     *
     */
    public static final int DEFAULT_CONFIG_ROWS = 2;
    private BuildableGrid configGrid;
    private List<OnOffButton> dipButtons;

    /**
     *
     */
    public SwiftySettings() {
        super();
        initialize();
    }

    private void initialize() {
        populate();
    }

    /**
     *
     * @return
     */
    @Override
    public SettingsTab buildGeneralTab() {
        SettingsRow dipRow = buildSettingsRow("DIP", "Match the buttons to switch positions");
        configGrid = buildConfigGrid();
        dipRow.add(configGrid, 1, 0, 2, 1);
        List<SettingsRow> rows = Arrays.asList(dipRow);
        SettingsTab generalTab = new SettingsTab(GENERAL_TAB_TITLE, rows);
        return generalTab;
    }

    private BuildableGrid buildConfigGrid() {
        BuildableGrid grid = new BuildableGrid();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setMaxWidth(Double.POSITIVE_INFINITY);
        grid.setMaxHeight(Double.POSITIVE_INFINITY);
        grid.setHgap(5.0);
        grid.setVgap(5.0);

        grid.setSpecRows(DEFAULT_CONFIG_ROWS);
        List<Double> rowPercentages = Arrays.asList(20.0, 80.0);
        grid.buildRowsByPerc(rowPercentages);

        grid.setSpecColumns(DEFAULT_CONFIG_COLUMNS);
        grid.buildColumns();

        for (int i = 0; i < DIP_SIZE; i++) {
            AnimatedLabel label = new AnimatedLabel();
            label.setText(String.valueOf(i));
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
            setCSS("settings-label", label);
            grid.add(label, i, 0);
        }

        dipButtons = buildDIPButtons(grid);
        for (int i = 0; i < DIP_SIZE; i++) {
            grid.add(dipButtons.get(i), i, 1);
        }
        return grid;
    }

    private List<OnOffButton> buildDIPButtons(BuildableGrid grid) {
        List<OnOffButton> buttons = new ArrayList<>();
        for (int i = 0; i < DIP_SIZE; i++) {
            OnOffButton onOff = new OnOffButton("","");
            buttons.add(onOff);
        }
        return buttons;
    }

    /*  @Override
    public void set() {
        super.set();
        String dipValue = "";
        for (OnOffButton dipButton : dipButtons) {
            final String stringValue = dipButton.getValue().toString();
            Integer intValue = (stringValue.equals(Boolean.toString(true))) ? 1 : 0;
            dipValue = dipValue.concat(intValue.toString());
        }
        getHardware().setDIP(dipValue);

    }

    @Override
    public void reset() {
        super.reset();
        List<String> dipList = Arrays.asList(getHardware().getDIP().split(""));
        for (int i = 0; i < dipList.size(); i++) {
            dipButtons.get(i).setValue("1".equals(dipList.get(i)));
        }
    }*/

    /**
     *
     * @return
     */

    @Override
    public Device<Swifty> getDevice() {

        if (device == null) {
            device = getSession().getDeviceManager().swifty;
        }
        return device;
    }

}
