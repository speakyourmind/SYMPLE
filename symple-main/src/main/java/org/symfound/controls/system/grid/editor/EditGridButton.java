/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system.grid.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;
import static org.symfound.builder.user.characteristic.Navigation.BUTTON_DELIMITER;
import static org.symfound.builder.user.characteristic.Navigation.KEY_DELIMITER;
import org.symfound.builder.user.selection.SelectionMethod;
import org.symfound.controls.SystemControl;
import org.symfound.controls.system.OnOffButton;
import org.symfound.controls.system.SettingsRow;
import org.symfound.controls.system.dialog.EditDialog;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.controls.system.dialog.OKCancelDialog;
import org.symfound.controls.user.BuildableGrid;
import org.symfound.controls.user.ButtonGrid;
import org.symfound.controls.user.FillableGrid.FillDirection;
import org.symfound.controls.user.FillableGrid.FillMethod;
import org.symfound.main.Main;
import org.symfound.main.settings.SettingsController;
import org.symfound.tools.iteration.ParallelList;

/**
 *
 * @author Javed Gangjee
 */
public class EditGridButton extends SystemControl {

    /**
     *
     */
    public static final String NAME = EditGridButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public ButtonGrid buttonGrid;

    /**
     *
     */
    public static final String KEY = "Edit Grid";

    /**
     *
     * @param buttonGrid
     */
    public EditGridButton(ButtonGrid buttonGrid) {
        super("toolbar-edit-grid", KEY, "", "default");
        this.buttonGrid = buttonGrid;
    }
    
    @Override
    public void defineButton() {
        setEditable(Boolean.FALSE);
        setControlType(ControlType.SETTING_CONTROL);
        setNavigatePostClick(Boolean.FALSE);
        
    }

    /**
     *
     * @return
     */
    @Override
    public OKCancelDialog getDialog() {
        if (settingsDialog == null) {
            settingsDialog = configureEditDialog();
        }
        return settingsDialog;
    }

    /**
     *
     */
    public List<SettingsRow> advancedSettings = new ArrayList<>();

    /**
     *
     */
    public List<SettingsRow> fillSettings = new ArrayList<>();

    /**
     *
     */
    public List<SettingsRow> lookSettings = new ArrayList<>();

    /**
     *
     */
    public List<SettingsRow> selectionSettings = new ArrayList<>();

    /**
     *
     */
    public List<SettingsRow> gridSettings = new ArrayList<>();

    /**
     *
     * @return
     */
    public EditDialog configureEditDialog() {
        EditDialog editDialog = new EditDialog("Edit Grid") {
            private TextArea buttonOrderField;
            //private BuildableGrid buttonOrderGrid;
            private ChoiceBox<FillMethod> fillMethodChoices;
            private ChoiceBox<FillDirection> fillDirectionChoices;
            private Slider hGapSlider;
            private Slider vGapSlider;
            private TextField maxDifficultyField;//TODO:Change to slider;
            private TextField minDifficultyField;//TODO:Change to slider;
            private Slider overrideRowSlider;
            private OnOffButton overrideRowButton;
            private Slider overrideColumnSlider;
            private OnOffButton overrideColumnButton;
            private TextArea overrideStyleField;
            private ChoiceBox<SelectionMethod> selectionMethodChoices;
            
            private OnOffButton paginationButton;
            
            @Override
            public Node addSettingControls() {
                SettingsRow buttonOrderRow = createSettingRow("Button Order", "Placeholder method to change app order");
                buttonOrderField = new TextArea();
                buttonOrderField.setStyle("-fx-font-size:1.6em;");
                buttonOrderField.setWrapText(true);
                buttonOrderField.setText(buttonGrid.getOrder().asString());
                buttonOrderField.maxHeight(80.0);
                buttonOrderField.maxWidth(360.0);
                buttonOrderField.getStyleClass().add("settings-text-area");
                buttonOrderRow.add(buttonOrderField, 1, 0, 2, 1);
                
                SettingsRow paginationRow = createSettingRow("Pagination", "Enable automated splitting into multiple pages");
                
                paginationButton = new OnOffButton("YES", "NO");
                paginationButton.setValue(buttonGrid.isPaginationEnabled());
                paginationButton.setMaxSize(180.0, 60.0);
                GridPane.setHalignment(paginationButton, HPos.LEFT);
                GridPane.setValignment(paginationButton, VPos.CENTER);
                paginationRow.add(paginationButton, 1, 0, 1, 1);
                
                SettingsRow fillMethodRow = createSettingRow("Fill Method", "How the grid is populated");
                
                fillMethodChoices = new ChoiceBox<>(FXCollections.observableArrayList(Arrays.asList(
                        FillMethod.ROW_WISE,
                        FillMethod.COLUMN_WISE
                )));
                fillMethodChoices.setValue(buttonGrid.getFillMethod());
                
                fillMethodChoices.maxHeight(80.0);
                fillMethodChoices.maxWidth(360.0);
                fillMethodChoices.getStyleClass().add("settings-text-area");
                fillMethodRow.add(fillMethodChoices, 1, 0, 2, 1);
                
                SettingsRow fillDirectionRow = createSettingRow("Fill Direction", "Fill order forward or in reverse");
                
                fillDirectionChoices = new ChoiceBox<>(FXCollections.observableArrayList(Arrays.asList(
                        FillDirection.FORWARD,
                        FillDirection.REVERSE
                )));
                fillDirectionChoices.setValue(buttonGrid.getFillDirection());
                fillDirectionChoices.maxHeight(80.0);
                fillDirectionChoices.maxWidth(360.0);
                fillDirectionChoices.getStyleClass().add("settings-text-area");
                fillDirectionRow.add(fillDirectionChoices, 1, 0, 2, 1);
                
                SettingsRow hGapRow = createSettingRow("Horizontal gap", "Adjust horizontal gaps between cells");
                hGapSlider = new Slider(0.0, 200.0, buttonGrid.getCustomHGap());
                hGapSlider.setMajorTickUnit(20);
                hGapSlider.setMinorTickCount(4);
                hGapSlider.setShowTickLabels(true);
                hGapSlider.setShowTickMarks(true);
                hGapRow.add(hGapSlider, 1, 0, 2, 1);
                
                SettingsRow vGapRow = createSettingRow("Vertical gap", "Adjust vertical gaps between cells");
                vGapSlider = new Slider(0.0, 200.0, buttonGrid.getCustomVGap());
                vGapSlider.setMajorTickUnit(20);
                vGapSlider.setMinorTickCount(4);
                vGapSlider.setShowTickLabels(true);
                vGapSlider.setShowTickMarks(true);
                vGapRow.add(vGapSlider, 1, 0, 2, 1);
                
                SettingsRow difficultyRow = createSettingRow("Difficulty", "Controls size of grid");
                
                maxDifficultyField = new TextField();
                maxDifficultyField.setText(String.valueOf(buttonGrid.getMaxDifficulty()));
                maxDifficultyField.maxHeight(80.0);
                maxDifficultyField.maxWidth(360.0);
                maxDifficultyField.getStyleClass().add("settings-text-area");
                difficultyRow.add(maxDifficultyField, 2, 0, 1, 1);
                
                minDifficultyField = new TextField();
                minDifficultyField.setText(String.valueOf(buttonGrid.getMinDifficulty()));
                minDifficultyField.maxHeight(80.0);
                minDifficultyField.maxWidth(360.0);
                minDifficultyField.getStyleClass().add("settings-text-area");
                difficultyRow.add(minDifficultyField, 1, 0, 1, 1);
                
                SettingsRow overrideRowRow = createSettingRow("Row Size", "Row size overrides automation");
                
                overrideRowSlider = new Slider(1.0, 10.0, buttonGrid.getOverrideRow());
                overrideRowSlider.setMajorTickUnit(1);
                overrideRowSlider.setMinorTickCount(0);
                overrideRowSlider.setShowTickLabels(true);
                overrideRowSlider.setShowTickMarks(true);
                overrideRowSlider.setSnapToTicks(true);
                
                overrideRowRow.add(overrideRowSlider, 2, 0, 1, 1);
                
                overrideRowButton = new OnOffButton("AUTO", "MANUAL");
                overrideRowButton.setValue(buttonGrid.getOverrideRow() == 0);
                overrideRowSlider.visibleProperty().bind(Bindings.not(overrideRowButton.valueProperty()));
                overrideRowButton.setMaxSize(180.0, 60.0);
                GridPane.setHalignment(overrideRowButton, HPos.LEFT);
                GridPane.setValignment(overrideRowButton, VPos.CENTER);
                overrideRowRow.add(overrideRowButton, 1, 0, 1, 1);
                
                SettingsRow overrideColumnRow = createSettingRow("Column Size", "Column size overrides automation");
                
                overrideColumnSlider = new Slider(1.0, 10.0, buttonGrid.getOverrideColumn());
                overrideColumnSlider.setMajorTickUnit(1);
                overrideColumnSlider.setMinorTickCount(0);
                overrideColumnSlider.setShowTickLabels(true);
                overrideColumnSlider.setShowTickMarks(true);
                overrideColumnSlider.setSnapToTicks(true);
                
                overrideColumnRow.add(overrideColumnSlider, 2, 0, 1, 1);
                
                overrideColumnButton = new OnOffButton("AUTO", "MANUAL");
                overrideColumnButton.setValue(buttonGrid.getOverrideColumn() == 0);
                overrideColumnSlider.visibleProperty().bind(Bindings.not(overrideColumnButton.valueProperty()));
                overrideColumnButton.setMaxSize(180.0, 60.0);
                GridPane.setHalignment(overrideColumnButton, HPos.LEFT);
                GridPane.setValignment(overrideColumnButton, VPos.CENTER);
                overrideColumnRow.add(overrideColumnButton, 1, 0, 1, 1);
                
                SettingsRow styleRow = createSettingRow("Style", "CSS Style code");
                
                overrideStyleField = new TextArea();
                overrideStyleField.setStyle("-fx-font-size:1.6em;");
                overrideStyleField.setText(buttonGrid.getOverrideStyle());
                overrideStyleField.maxHeight(80.0);
                overrideStyleField.maxWidth(360.0);
                overrideStyleField.getStyleClass().add("settings-text-area");
                styleRow.add(overrideStyleField, 1, 0, 2, 1);
                
                SettingsRow selectionMethodRow = createSettingRow("Selection Method", "Only available in assisted mode");
                selectionMethodChoices = new ChoiceBox<>(FXCollections.observableArrayList(
                        Arrays.asList(
                                SelectionMethod.CLICK,
                                SelectionMethod.DWELL,
                                SelectionMethod.SCAN,
                                SelectionMethod.STEP
                        )));
                selectionMethodChoices.disableProperty().bind(Main.getSession().getUser().getInteraction().assistedModeProperty().not());
                selectionMethodChoices.setValue(buttonGrid.getSelectionMethod());
                selectionMethodChoices.maxHeight(80.0);
                selectionMethodChoices.maxWidth(360.0);
                selectionMethodChoices.getStyleClass().add("settings-text-area");
                selectionMethodRow.add(selectionMethodChoices, 1, 0, 2, 1);
                
                fillSettings.add(fillMethodRow);
                fillSettings.add(fillDirectionRow);
                Tab fillTab = buildTab("FILL", fillSettings);
                
                gridSettings.add(overrideRowRow);
                gridSettings.add(overrideColumnRow);
                gridSettings.add(difficultyRow);
                gridSettings.add(paginationRow);
                Tab gridTab = buildTab("SIZE", gridSettings);
                
                lookSettings.add(styleRow);
                lookSettings.add(hGapRow);
                lookSettings.add(vGapRow);
                Tab lookTab = buildTab("LOOK", lookSettings);
                
                selectionSettings.add(selectionMethodRow);
                Tab selectionTab = buildTab("SELECTION", selectionSettings);
                
                advancedSettings.add(buttonOrderRow);
                Tab advancedTab = buildTab("ADVANCED", advancedSettings);
                
                TabPane tabPane = new TabPane();
                tabPane.setPadding(new Insets(5));
                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                tabPane.getTabs().add(gridTab);
                tabPane.getTabs().add(fillTab);
                tabPane.getTabs().add(lookTab);
                tabPane.getTabs().add(selectionTab);
                tabPane.getTabs().add(advancedTab);
                return tabPane;
            }
            
            @Override
            public void setSettings() {
                ParallelList<String, String> parallelList = new ParallelList<>();
                String[] pairs = buttonOrderField.getText().split(BUTTON_DELIMITER);
                for (String pair : pairs) {
                    String[] keyValue = pair.split(KEY_DELIMITER);
                    parallelList.add(keyValue[0], keyValue[1]);
                }
                buttonGrid.setOrder(parallelList);
                buttonGrid.setFillMethod(fillMethodChoices.getValue());
                buttonGrid.setFillDirection(fillDirectionChoices.getValue());
                buttonGrid.setCustomHGap(hGapSlider.getValue());
                buttonGrid.setCustomVGap(vGapSlider.getValue());
                buttonGrid.setMaxDifficulty(Double.valueOf(maxDifficultyField.getText()));
                buttonGrid.setMinDifficulty(Double.valueOf(minDifficultyField.getText()));
                
                if (overrideRowButton.getValue()) {
                    buttonGrid.setOverrideRow(0.0);
                    
                } else {
                    buttonGrid.setOverrideRow(overrideRowSlider.getValue());
                }
                
                if (overrideColumnButton.getValue()) {
                    buttonGrid.setOverrideColumn(0.0);
                } else {
                    buttonGrid.setOverrideColumn(overrideColumnSlider.getValue());
                }
                
                buttonGrid.setOverrideStyle(overrideStyleField.getText());
                buttonGrid.setSelectionMethod(selectionMethodChoices.getValue());
                buttonGrid.enablePagination(paginationButton.getValue());
                SettingsController.setUpdated(true);
                
            }
            
            @Override
            public void resetSettings() {
                buttonOrderField.setText(buttonGrid.getOrder().asString());
                fillMethodChoices.setValue(buttonGrid.getFillMethod());
                fillDirectionChoices.setValue(buttonGrid.getFillDirection());
                hGapSlider.setValue(buttonGrid.getCustomHGap());
                vGapSlider.setValue(buttonGrid.getCustomVGap());
                maxDifficultyField.setText(String.valueOf(buttonGrid.getMaxDifficulty()));
                minDifficultyField.setText(String.valueOf(buttonGrid.getMinDifficulty()));
                overrideRowSlider.setValue(buttonGrid.getOverrideRow());
                overrideRowButton.setValue(buttonGrid.getOverrideRow() == 0);
                overrideColumnSlider.setValue(buttonGrid.getOverrideColumn());
                overrideColumnButton.setValue(buttonGrid.getOverrideColumn() == 0);
                overrideStyleField.setText(buttonGrid.getOverrideStyle());
                selectionMethodChoices.setValue(buttonGrid.getSelectionMethod());
                paginationButton.setValue(buttonGrid.isPaginationEnabled());
                SettingsController.setUpdated(false);
            }
        };
        return editDialog;
    }
    
    @Override
    public void run() {
        LOGGER.info("Edit Grid button clicked");
    }
    
    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends EditGridButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }
}
