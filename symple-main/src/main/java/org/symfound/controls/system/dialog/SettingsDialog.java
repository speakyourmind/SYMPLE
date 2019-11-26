package org.symfound.controls.system.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import org.symfound.builder.session.Display;
import static org.symfound.controls.ScreenControl.setSize;
import static org.symfound.controls.device.SwiftySettings.DEFAULT_COLUMNS;
import org.symfound.controls.system.SettingsGrid;
import org.symfound.controls.system.SettingsRow;
import org.symfound.controls.system.SettingsTab;
import org.symfound.controls.system.Toolbar;
import static org.symfound.controls.system.dialog.ScreenDialog.MIN_HEIGHT;
import static org.symfound.controls.system.dialog.ScreenDialog.MIN_WIDTH;
import org.symfound.controls.user.AnimatedLabel;
import org.symfound.controls.user.BuildableGrid;

/**
 *
 * @author Javed Gangjee
 */
public abstract class SettingsDialog extends OKDialog {

    /**
     *
     */
    public static final double MAX_HEIGHT = 60.0;

    /**
     *
     */
    public static final double MAX_WIDTH = 180.0;

    /**
     *
     */
    public static final String TEXT_FIELD_STYLE = "settings-text-area";

    /**
     *
     */
    public List<SettingsTab> tabs = new ArrayList<>();

    /**
     *
     */
    public SettingsDialog() {
        super("", "", "DONE");
    }

    /**
     *
     * @param title
     */
    public final void buildBase(String title) {
        List<Double> rowPercentages = Arrays.asList(65.0, 35.0);
        buildBaseGrid(3, 1, rowPercentages);
        Toolbar toolbar = new Toolbar();
        toolbar.setButtonOrder("Exit=default");
        toolbar.setTitleText(title);
        baseGrid.add(toolbar, 0, 0);
        TabPane tabPane = buildTabPane(getTabList());
        baseGrid.add(tabPane, 0, 1);
        actionGrid = buildActionGrid(HPos.CENTER, 360.0, 60.0);
        baseGrid.add(actionGrid, 0, 2);
    }

    public abstract List<SettingsTab> getTabList();

    /**
     *
     * @param rows
     * @param columns
     * @param rowPercentages
     */
    @Override
    public void buildBaseGrid(Integer rows, Integer columns, List<Double> rowPercentages) {
        // Build base grid
        baseGrid = new BuildableGrid();
        baseGrid.getStyleClass().add("main");
        baseGrid.setAlignment(Pos.CENTER);
        final Display display = getSession().getDisplay();

        Double width = 1.0 * display.getScreenWidth();
        baseGrid.setMaxWidth(width);
        baseGrid.setMinWidth(MIN_WIDTH);
        Double height = 1.0 * display.getScreenHeight();
        baseGrid.setMaxHeight(height);
        baseGrid.setMinHeight(MIN_HEIGHT);
        baseGrid.setHgap(0.0);
        baseGrid.setVgap(0.0);

        // Build base Grid rows
        baseGrid.setSpecRows(rows);
        List<Double> rowPercentage = Arrays.asList(8.0, 82.0, 10.0);
        baseGrid.buildRowsByPerc(rowPercentage);

        // Build base Grid columns
        baseGrid.setSpecColumns(DEFAULT_COLUMNS);
        baseGrid.buildColumns();

    }

    // TO DO: Replace with Toolbar
    /**
     *
     * @param title
     * @return
     */
    public final AnimatedLabel buildTitle(String title) {
        AnimatedLabel label = new AnimatedLabel();
        setSizeMax(label);
        label.setText(title);
        label.setStyle("-fx-background-color:-fx-dark;");// TO DO: Add this to settings-title
        setCSS(Toolbar.TITLE_STYLE, label);
        return label;
    }

    /**
     *
     * @param tabs
     * @return
     */
    public static TabPane buildTabPane(List<SettingsTab> tabs) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.stream().forEach((tab) -> {
            tabPane.getTabs().add(tab);
        });
        tabPane.setSide(Side.LEFT);
        return tabPane;
    }

    /**
     *
     * @param title
     * @param helpText
     * @return
     */
    public final SettingsRow buildSettingsRow(String title, String helpText) {
        SettingsRow settingsRow = new SettingsRow();
        settingsRow.setPadding(new Insets(0, 40, 0, 40));
        settingsRow.setMaxHeight(SettingsGrid.HEIGHT_PER_ROW);
        settingsRow.setTitleText(title);
        settingsRow.setHelpText(helpText);
        return settingsRow;
    }

    /**
     *
     * @param title
     * @param caption
     * @param slider
     * @return
     */
    public SettingsRow buildSliderFieldSetting(String title, String caption, Slider slider) {
        SettingsRow row = buildSettingsRow(title, caption);

        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setSnapToTicks(true);
        row.add(slider, 2, 0);

        TextField valueField = new TextField();
        setSize(valueField, MAX_WIDTH, MAX_HEIGHT);
        valueField.getStyleClass().add(TEXT_FIELD_STYLE);
        valueField.setText(String.valueOf(slider.getValue()));
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            valueField.setText(String.valueOf(newValue));
        });
        row.add(valueField, 1, 0);

        return row;
    }

    /**
     *
     */
    @Override
    public void onOk() {

    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {
        //UNUSED
    }
}
