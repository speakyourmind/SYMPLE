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
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;
import static org.symfound.builder.user.characteristic.Navigation.BUTTON_DELIMITER;
import static org.symfound.builder.user.characteristic.Navigation.KEY_DELIMITER;
import org.symfound.builder.user.selection.SelectionMethod;
import org.symfound.controls.SystemControl;
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
        super("toolbar-edit", KEY, "", "default");
        this.buttonGrid = buttonGrid;
        initialize();
    }
    
    private void initialize() {
        setConfirmable(Boolean.TRUE);
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
    public List<SettingsRow> orderSettings = new ArrayList<>();

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
        EditDialog editDialog = new EditDialog() {
            TextArea buttonOrderField;
            BuildableGrid buttonOrderGrid;
            ChoiceBox<FillMethod> fillMethodChoices;
            ChoiceBox<FillDirection> fillDirectionChoices;
            TextField gapField;//TODO:Change to slider;
            TextField maxDifficultyField;//TODO:Change to slider;
            TextField minDifficultyField;//TODO:Change to slider;
            TextField overrideRowField;//TODO:Change to slider;
            TextField overrideColumnField;//TODO:Change to slider;
            private TextArea overrideStyleField;
            private ChoiceBox<SelectionMethod> selectionMethodChoices;
            
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

                /*  SettingsRow buttonOrderTestRow2 = createSettingRow("Button Order Test", "Placeholder method to change app order");
                buttonOrderGrid = new BuildableGrid();
                List<String> firstList2 = buttonGrid.getOrder().getFirstList();
                buttonOrderGrid.setSpecRows(firstList2.size());
                buttonOrderGrid.setSpecColumns(2);
                buttonOrderGrid.build();
                for (int i = 0; i < firstList2.size(); i++) {
                    ChoiceBox field = new ChoiceBox<>(FXCollections.observableArrayList(KEY_CATALOGUE));
                    field.setValue(firstList2.get(i));
                    field.getStyleClass().add("settings-text-area");
                    field.minWidth(360.0);
                    GridPane.setRowIndex(field, i);
                    GridPane.setColumnIndex(field, 0);
                    GridPane.setHalignment(field, HPos.CENTER);
                    buttonOrderGrid.getChildren().add(field);
                }

                List<String> secondList2 = buttonGrid.getOrder().getSecondList();
                for (int i = 0; i < secondList2.size(); i++) {
                    TextField field = new TextField(secondList2.get(i));
                    field.getStyleClass().add("settings-text-area");
                    GridPane.setRowIndex(field, i);
                    GridPane.setColumnIndex(field, 1);
                    buttonOrderGrid.getChildren().add(field);
                }
                buttonOrderTestRow2.add(buttonOrderGrid, 1, 0, 2, 1);*/
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
                
                SettingsRow gridGapRow = createSettingRow("Grid gap", "Adjust gaps between cells");
                
                gapField = new TextField();
                gapField.setText(String.valueOf(buttonGrid.getGap()));
                gapField.maxHeight(80.0);
                gapField.maxWidth(360.0);
                gapField.getStyleClass().add("settings-text-area");
                gridGapRow.add(gapField, 1, 0, 2, 1);
                
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
                
                SettingsRow overrideSizeRow = createSettingRow("Grid Size", "Row x Column size overrides automation");
                
                overrideRowField = new TextField();
                
                overrideRowField.setText(buttonGrid.getOverrideRow().toString());
                overrideRowField.maxHeight(80.0);
                overrideRowField.maxWidth(360.0);
                overrideRowField.getStyleClass().add("settings-text-area");
                overrideSizeRow.add(overrideRowField, 1, 0, 1, 1);
                
                overrideColumnField = new TextField();
                overrideColumnField.setText(buttonGrid.getOverrideColumn().toString());
                overrideColumnField.maxHeight(80.0);
                overrideColumnField.maxWidth(360.0);
                overrideColumnField.getStyleClass().add("settings-text-area");
                overrideSizeRow.add(overrideColumnField, 2, 0, 1, 1);
                
                SettingsRow styleRow = createSettingRow("Style", "CSS Style code");
                
                overrideStyleField = new TextArea();
                overrideStyleField.setStyle("-fx-font-size:1.6em;");
                overrideStyleField.setText(buttonGrid.getOverrideStyle());
                overrideStyleField.maxHeight(80.0);
                overrideStyleField.maxWidth(360.0);
                overrideStyleField.getStyleClass().add("settings-text-area");
                styleRow.add(overrideStyleField, 1, 0, 2, 1);
                
                SettingsRow selectionMethodRow = createSettingRow("Selection Method", "Only available in caregiver mode");
                selectionMethodChoices = new ChoiceBox<>(FXCollections.observableArrayList(
                        Arrays.asList(
                                SelectionMethod.CLICK,
                                SelectionMethod.DWELL,
                                SelectionMethod.SCAN,
                                SelectionMethod.STEP
                        )));
                selectionMethodChoices.disableProperty().bind(Main.getSession().getUser().getInteraction().overrideSelectionMethodProperty().not());
                selectionMethodChoices.setValue(buttonGrid.getSelectionMethod());
                selectionMethodChoices.maxHeight(80.0);
                selectionMethodChoices.maxWidth(360.0);
                selectionMethodChoices.getStyleClass().add("settings-text-area");
                selectionMethodRow.add(selectionMethodChoices, 1, 0, 2, 1);
                
                orderSettings.add(buttonOrderRow);
                //  orderSettings.add(buttonOrderTestRow2);
                Tab orderTab = buildTab("ORDER", orderSettings);
                
                fillSettings.add(fillMethodRow);
                fillSettings.add(fillDirectionRow);
                Tab fillTab = buildTab("FILL", fillSettings);
                
                gridSettings.add(difficultyRow);
                gridSettings.add(overrideSizeRow);
                Tab gridTab = buildTab("SIZE", gridSettings);
                
                lookSettings.add(styleRow);
                lookSettings.add(gridGapRow);
                Tab lookTab = buildTab("LOOK", lookSettings);

                selectionSettings.add(selectionMethodRow);
                Tab selectionTab = buildTab("SELECTION", selectionSettings);
                
                TabPane tabPane = new TabPane();
                tabPane.setPadding(new Insets(5));
                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                tabPane.getTabs().add(orderTab);
                tabPane.getTabs().add(fillTab);
                tabPane.getTabs().add(gridTab);
                tabPane.getTabs().add(lookTab);
                tabPane.getTabs().add(selectionTab);
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
                buttonGrid.setGap(Double.valueOf(gapField.getText()));
                buttonGrid.setMaxDifficulty(Double.valueOf(maxDifficultyField.getText()));
                buttonGrid.setMinDifficulty(Double.valueOf(minDifficultyField.getText()));
                
                buttonGrid.setOverrideRow(Double.valueOf(overrideRowField.getText()));
                buttonGrid.setOverrideColumn(Double.valueOf(overrideColumnField.getText()));
                buttonGrid.setOverrideStyle(overrideStyleField.getText());
                buttonGrid.setSelectionMethod(selectionMethodChoices.getValue());
                SettingsController.setUpdated(true);
                
            }
            
            @Override
            public void resetSettings() {
                buttonOrderField.setText(buttonGrid.getOrder().asString());
                fillMethodChoices.setValue(buttonGrid.getFillMethod());
                fillDirectionChoices.setValue(buttonGrid.getFillDirection());
                gapField.setText(String.valueOf(buttonGrid.getGap()));
                maxDifficultyField.setText(String.valueOf(buttonGrid.getMaxDifficulty()));
                minDifficultyField.setText(String.valueOf(buttonGrid.getMinDifficulty()));
                overrideRowField.setText(String.valueOf(buttonGrid.getOverrideRow()));
                overrideColumnField.setText(String.valueOf(buttonGrid.getOverrideColumn()));
                overrideStyleField.setText(buttonGrid.getOverrideStyle());
                selectionMethodChoices.setValue(buttonGrid.getSelectionMethod());
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
