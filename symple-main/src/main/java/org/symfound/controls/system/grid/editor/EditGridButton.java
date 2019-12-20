/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system.grid.editor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import org.apache.log4j.Logger;
import static org.symfound.builder.user.characteristic.Ability.MAX_LEVEL;
import static org.symfound.builder.user.characteristic.Navigation.BUTTON_DELIMITER;
import static org.symfound.builder.user.characteristic.Navigation.KEY_DELIMITER;
import org.symfound.builder.user.selection.SelectionMethod;
import org.symfound.controls.AppableControl;
import org.symfound.controls.RunnableControl;
import org.symfound.controls.ScreenControl;
import org.symfound.controls.system.OnOffButton;
import org.symfound.controls.system.SettingsRow;
import org.symfound.controls.system.UIExporter;
import org.symfound.controls.system.dialog.EditDialog;
import static org.symfound.controls.system.dialog.EditDialog.LOGGER;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.SettingsButtonBase;
import org.symfound.controls.user.ConfigurableGrid;
import org.symfound.controls.user.FillableGrid.FillDirection;
import org.symfound.controls.user.FillableGrid.FillMethod;
import static org.symfound.controls.user.UserSettingsButton.DEFAULT_TITLE;
import static org.symfound.main.FullSession.getSettingsFileName;
import org.symfound.main.HomeController;
import org.symfound.main.Main;
import org.symfound.tools.iteration.ParallelList;
import org.symfound.tools.timing.clock.Clock;
import org.symfound.tools.ui.SnapshotExporter;

/**
 *
 * @author Javed Gangjee
 */
public class EditGridButton extends SettingsButtonBase {

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
    public final ConfigurableGrid grid;

    /**
     *
     */
    public static final String KEY = "Edit Grid";

    public EditGridButton(ConfigurableGrid grid) {
        super("setting-button", KEY, DEFAULT_TITLE);
        this.grid = grid;
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
    public List<SettingsRow> sizeSettings = new ArrayList<>();

    public List<SettingsRow> aboutSettings = new ArrayList<>();

    /**
     *
     * @return
     */
    @Override
    public EditDialog configureEditDialog() {
        EditDialog editDialog = new EditDialog("Edit Grid") {
            private TextArea buttonOrderField;
            private ChoiceBox<FillMethod> fillMethodChoices;
            private ChoiceBox<FillDirection> fillDirectionChoices;
            private Slider hGapSlider;
            private Slider vGapSlider;
            private Slider marginSlider;
            private TextField maxDifficultyField;//TODO:Change to slider;
            private TextField minDifficultyField;//TODO:Change to slider;
            private Slider overrideRowSlider;
            private OnOffButton overrideRowButton;
            private Slider overrideColumnSlider;
            private OnOffButton overrideColumnButton;
            private Slider widthSlider;
            private OnOffButton widthButton;
            private Slider heightSlider;
            private OnOffButton heightButton;
            private TextArea overrideStyleField;
            private OnOffButton editDescriptionButton;
            private TextArea descriptionArea;
            private ChoiceBox<SelectionMethod> selectionMethodChoices;

            private OnOffButton paginationButton;

            private RunnableControl exportButton;

            @Override
            public Node addSettingControls() {

                SettingsRow buttonOrderRow = createSettingRow("Button Order", "Placeholder method to change app order");
                buttonOrderField = new TextArea();
                buttonOrderField.setStyle("-fx-font-size:1.6em;");
                buttonOrderField.setWrapText(true);
                buttonOrderField.setText(grid.getOrder().asString());
                grid.orderProperty().addListener((observable, oldValue, newValue) -> {
                    buttonOrderField.setText(grid.getOrder().asString());

                });
                buttonOrderField.maxHeight(80.0);
                buttonOrderField.maxWidth(360.0);
                buttonOrderField.getStyleClass().add("settings-text-area");

                buttonOrderRow.add(buttonOrderField, 1, 0, 2, 1);

                SettingsRow paginationRow = createSettingRow("Pagination", "Enable automated splitting into multiple pages");

                paginationButton = new OnOffButton("YES", "NO");
                paginationButton.setValue(grid.isPaginationEnabled());
                paginationButton.setMaxSize(180.0, 60.0);
                GridPane.setHalignment(paginationButton, HPos.LEFT);
                GridPane.setValignment(paginationButton, VPos.CENTER);
                paginationRow.add(paginationButton, 1, 0, 1, 1);

                SettingsRow fillMethodRow = createSettingRow("Fill Method", "How the grid is populated");

                fillMethodChoices = new ChoiceBox<>(FXCollections.observableArrayList(Arrays.asList(
                        FillMethod.ROW_WISE,
                        FillMethod.COLUMN_WISE
                )));
                fillMethodChoices.setValue(grid.getFillMethod());

                fillMethodChoices.maxHeight(80.0);
                fillMethodChoices.maxWidth(360.0);
                fillMethodChoices.getStyleClass().add("settings-text-area");
                fillMethodRow.add(fillMethodChoices, 1, 0, 2, 1);

                SettingsRow fillDirectionRow = createSettingRow("Fill Direction", "Fill order forward or in reverse");

                fillDirectionChoices = new ChoiceBox<>(FXCollections.observableArrayList(Arrays.asList(
                        FillDirection.FORWARD,
                        FillDirection.REVERSE
                )));
                fillDirectionChoices.setValue(grid.getFillDirection());
                fillDirectionChoices.maxHeight(80.0);
                fillDirectionChoices.maxWidth(360.0);
                fillDirectionChoices.getStyleClass().add("settings-text-area");
                fillDirectionRow.add(fillDirectionChoices, 1, 0, 2, 1);

                SettingsRow hGapRow = createSettingRow("Horizontal gap", "Adjust horizontal gaps between cells");
                hGapSlider = new Slider(0.0, 200.0, grid.getCustomHGap());
                hGapSlider.setMajorTickUnit(20);
                hGapSlider.setMinorTickCount(3);
                hGapSlider.setShowTickLabels(true);
                hGapSlider.setShowTickMarks(true);
                hGapSlider.setSnapToTicks(true);
                hGapRow.add(hGapSlider, 1, 0, 2, 1);

                SettingsRow vGapRow = createSettingRow("Vertical gap", "Adjust vertical gaps between cells");
                vGapSlider = new Slider(0.0, 200.0, grid.getCustomVGap());
                vGapSlider.setMajorTickUnit(20);
                vGapSlider.setMinorTickCount(3);
                vGapSlider.setShowTickLabels(true);
                vGapSlider.setShowTickMarks(true);
                vGapSlider.setSnapToTicks(true);
                vGapRow.add(vGapSlider, 1, 0, 2, 1);

                SettingsRow marginRow = createSettingRow("Margin", "Outer gap");
                marginSlider = new Slider(0.0, 200.0, grid.getCustomMargin());
                marginSlider.setMajorTickUnit(20);
                marginSlider.setMinorTickCount(3);
                marginSlider.setShowTickLabels(true);
                marginSlider.setShowTickMarks(true);
                marginSlider.setSnapToTicks(true);
                marginRow.add(marginSlider, 1, 0, 2, 1);

                SettingsRow difficultyRow = createSettingRow("Difficulty", "Controls size of grid");

                maxDifficultyField = new TextField();
                maxDifficultyField.setText(String.valueOf(grid.getMaxDifficulty()));
                maxDifficultyField.maxHeight(80.0);
                maxDifficultyField.maxWidth(360.0);
                maxDifficultyField.getStyleClass().add("settings-text-area");
                difficultyRow.add(maxDifficultyField, 2, 0, 1, 1);

                minDifficultyField = new TextField();
                minDifficultyField.setText(String.valueOf(grid.getMinDifficulty()));
                minDifficultyField.maxHeight(80.0);
                minDifficultyField.maxWidth(360.0);
                minDifficultyField.getStyleClass().add("settings-text-area");
                difficultyRow.add(minDifficultyField, 1, 0, 1, 1);

                SettingsRow overrideRowRow = createSettingRow("Row Size", "Row size overrides automation");

                overrideRowSlider = new Slider(1.0, MAX_LEVEL, grid.getOverrideRow());
                overrideRowSlider.setMajorTickUnit(1);
                overrideRowSlider.setMinorTickCount(0);
                overrideRowSlider.setShowTickLabels(true);
                overrideRowSlider.setShowTickMarks(true);
                overrideRowSlider.setSnapToTicks(true);

                overrideRowRow.add(overrideRowSlider, 2, 0, 1, 1);

                overrideRowButton = new OnOffButton("AUTO", "MANUAL");
                overrideRowButton.setValue(grid.getOverrideRow() == 0);
                overrideRowSlider.visibleProperty().bind(Bindings.not(overrideRowButton.valueProperty()));
                overrideRowButton.setMaxSize(180.0, 60.0);
                GridPane.setHalignment(overrideRowButton, HPos.LEFT);
                GridPane.setValignment(overrideRowButton, VPos.CENTER);
                overrideRowRow.add(overrideRowButton, 1, 0, 1, 1);

                SettingsRow overrideColumnRow = createSettingRow("Column Size", "Column size overrides automation");

                overrideColumnSlider = new Slider(1.0, MAX_LEVEL, grid.getOverrideColumn());
                overrideColumnSlider.setMajorTickUnit(1);
                overrideColumnSlider.setMinorTickCount(0);
                overrideColumnSlider.setShowTickLabels(true);
                overrideColumnSlider.setShowTickMarks(true);
                overrideColumnSlider.setSnapToTicks(true);

                overrideColumnRow.add(overrideColumnSlider, 2, 0, 1, 1);

                overrideColumnButton = new OnOffButton("AUTO", "MANUAL");
                overrideColumnButton.setValue(grid.getOverrideColumn() == 0);
                overrideColumnSlider.visibleProperty().bind(Bindings.not(overrideColumnButton.valueProperty()));
                overrideColumnButton.setMaxSize(180.0, 60.0);
                GridPane.setHalignment(overrideColumnButton, HPos.LEFT);
                GridPane.setValignment(overrideColumnButton, VPos.CENTER);
                overrideColumnRow.add(overrideColumnButton, 1, 0, 1, 1);

                SettingsRow widthRow = createSettingRow("Width", "Override the automated grid width");

                widthSlider = new Slider(500.0, 5000.0, grid.getOverrideWidth());
                widthSlider.setMajorTickUnit(500.0);
                widthSlider.setMinorTickCount(1);
                widthSlider.setShowTickLabels(true);
                widthSlider.setShowTickMarks(true);
                widthSlider.setSnapToTicks(true);

                widthRow.add(widthSlider, 2, 0, 1, 1);

                widthButton = new OnOffButton("AUTO", "MANUAL");
                widthButton.setValue(grid.isFitToWidth());
                widthSlider.visibleProperty().bind(Bindings.not(widthButton.valueProperty()));
                widthButton.setMaxSize(180.0, 60.0);
                GridPane.setHalignment(widthButton, HPos.LEFT);
                GridPane.setValignment(widthButton, VPos.CENTER);
                widthRow.add(widthButton, 1, 0, 1, 1);

                SettingsRow heightRow = createSettingRow("Height", "Override the automated grid height");

                heightSlider = new Slider(500.0, 5000.0, grid.getOverrideHeight());
                heightSlider.setMajorTickUnit(1000.0);
                heightSlider.setMinorTickCount(0);
                heightSlider.setShowTickLabels(true);
                heightSlider.setShowTickMarks(true);
                heightSlider.setSnapToTicks(true);

                heightRow.add(heightSlider, 2, 0, 1, 1);

                heightButton = new OnOffButton("AUTO", "MANUAL");
                heightButton.setValue(grid.isFitToHeight());
                heightSlider.visibleProperty().bind(Bindings.not(heightButton.valueProperty()));
                heightButton.setMaxSize(180.0, 60.0);
                GridPane.setHalignment(heightButton, HPos.LEFT);
                GridPane.setValignment(heightButton, VPos.CENTER);
                heightRow.add(heightButton, 1, 0, 1, 1);

                SettingsRow styleRow = createSettingRow("Style", "CSS Style code");

                overrideStyleField = new TextArea();
                overrideStyleField.setStyle("-fx-font-size:1.6em;");
                overrideStyleField.setText(grid.getOverrideStyle());
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
                selectionMethodChoices.setValue(grid.getSelectionMethod());
                selectionMethodChoices.setMaxSize(180.0, 60.0);
                selectionMethodChoices.getStyleClass().add("settings-text-area");
                selectionMethodRow.add(selectionMethodChoices, 1, 0, 2, 1);

                SettingsRow exportRow = createSettingRow("Export this Screen", "Limited ability to export this screen");

                exportButton = new RunnableControl("settings-button") {
                    @Override
                    public void run() {
                        String folder = getFolderSelection();
                        exportChildrenNodes(grid, folder);
                    }

                    public void exportChildrenNodes(ConfigurableGrid subGrid, String folder) {
                        String fileName = getSettingsFileName(grid.getIndex().replaceAll("\\/", "-").concat("-grid"));

                        //TO DO: UIExporter may be replaced with PReferencesExporter
                        UIExporter gridExporter = new UIExporter(folder, fileName, "/org/symfound/controls/user/subgrid/" + grid.getIndex());;
                        getExecutor().execute(gridExporter);

                        grid.getChildren().forEach((node) -> {
                            if (node instanceof AppableControl) {
                                AppableControl control = (AppableControl) node;
                                String nodeFile = getSettingsFileName(control.getIndex().replaceAll("\\/", "-"));
                                LOGGER.debug("Exporting preferences as " + nodeFile + " in " + folder);
                                UIExporter controlExporter = new UIExporter(folder, nodeFile, control.getPreferences().absolutePath());
                                getExecutor().execute(controlExporter);
                                /*   if (node instanceof ScreenControl) {
                                    String screenFile = getSettingsFileName("screen-" + control.getIndex().replaceAll("\\/", "-"));
                                    LOGGER.debug("Exporting preferences as " + screenFile + " in " + folder);
                                   UIExporter screenExporter = new UIExporter(folder, screenFile, "/org/symfound/controls/user/subgrid/" + control.getIndex());
                                    getExecutor().execute(screenExporter);
                                }*/
                            }
                        });
                    }

                    private ThreadPoolExecutor exportExecutor;

                    public ThreadPoolExecutor getExecutor() {
                        if (exportExecutor == null) {
                            exportExecutor = new ThreadPoolExecutor(3, 8, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
                        }
                        return exportExecutor;
                    }
                };
                exportButton.setControlType(ControlType.SETTING_CONTROL);
                exportButton.setText("EXPORT");
                exportButton.setMaxSize(180.0, 60.0);
                GridPane.setHalignment(exportButton, HPos.LEFT);
                GridPane.setValignment(exportButton, VPos.CENTER);
                exportRow.add(exportButton, 1, 0, 1, 1);

                SettingsRow descriptionRow = createSettingRow("Description", "About this board.");
                editDescriptionButton = new OnOffButton("DONE", "EDIT");
                editDescriptionButton.setMaxSize(180.0, 60.0);
                editDescriptionButton.setValue(Boolean.FALSE);
                GridPane.setHalignment(editDescriptionButton, HPos.LEFT);
                GridPane.setValignment(editDescriptionButton, VPos.CENTER);
                descriptionRow.add(editDescriptionButton, 1, 0, 1, 1);

                descriptionArea = new TextArea();
                descriptionArea.setText(grid.getDescription());
                descriptionArea.disableProperty().bind(Bindings.not(editDescriptionButton.valueProperty()));

                descriptionArea.setStyle("-fx-font-size:1.6em;");
                GridPane.setMargin(descriptionArea, new Insets(10.0));
                descriptionArea.prefHeight(80.0);
                descriptionArea.prefWidth(360.0);
                descriptionArea.getStyleClass().add("settings-text-area");
                descriptionRow.add(descriptionArea, 2, 0, 1, 1);

                fillSettings.add(fillMethodRow);
                fillSettings.add(fillDirectionRow);
                Tab fillTab = buildTab("Fill", fillSettings);

                sizeSettings.add(overrideRowRow);
                sizeSettings.add(overrideColumnRow);
                sizeSettings.add(widthRow);
                sizeSettings.add(heightRow);
                sizeSettings.add(paginationRow);
                Tab sizeTab = buildTab("Size", sizeSettings);

                lookSettings.add(styleRow);
                lookSettings.add(hGapRow);
                lookSettings.add(vGapRow);
                lookSettings.add(marginRow);
                Tab lookTab = buildTab("Look", lookSettings);

                selectionSettings.add(selectionMethodRow);
                Tab selectionTab = buildTab("Selection", selectionSettings);

                advancedSettings.add(buttonOrderRow);
                advancedSettings.add(difficultyRow);
                advancedSettings.add(exportRow);
                Tab advancedTab = buildTab("Advanced", advancedSettings);

                aboutSettings.add(descriptionRow);
                Tab aboutTab = buildTab("About", aboutSettings);

                TabPane tabPane = new TabPane();
                tabPane.setPadding(new Insets(0, 0, 5, 5));
                tabPane.setSide(Side.LEFT);
                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                tabPane.getTabs().add(sizeTab);
                tabPane.getTabs().add(fillTab);
                tabPane.getTabs().add(lookTab);
                tabPane.getTabs().add(selectionTab);
                tabPane.getTabs().add(advancedTab);
                tabPane.getTabs().add(aboutTab);
                return tabPane;
            }

            @Override
            public void setSettings() {

                HomeController.setUpdated(false);
                ParallelList<String, String> parallelList = new ParallelList<>();
                if (buttonOrderField.getText().replaceAll(" ", "").isEmpty()) {
                    buttonOrderField.setText("Replace Key=default,");
                }
                // TO DO: Test that string is valid or switch to xml
                String[] pairs = buttonOrderField.getText().split(BUTTON_DELIMITER);

                for (String pair : pairs) {
                    String[] keyValue = pair.split(KEY_DELIMITER);
                    parallelList.add(keyValue[0], keyValue[1]);
                }

                grid.setOrder(parallelList);
                grid.setFillMethod(fillMethodChoices.getValue());
                grid.setFillDirection(fillDirectionChoices.getValue());
                grid.setCustomHGap(hGapSlider.getValue());
                grid.setCustomVGap(vGapSlider.getValue());
                grid.setCustomMargin(marginSlider.getValue());
                grid.setMaxDifficulty(Double.valueOf(maxDifficultyField.getText()));
                grid.setMinDifficulty(Double.valueOf(minDifficultyField.getText()));

                if (overrideRowButton.getValue()) {
                    grid.setOverrideRow(0.0);

                } else {
                    grid.setOverrideRow(overrideRowSlider.getValue());
                }

                if (overrideColumnButton.getValue()) {
                    grid.setOverrideColumn(0.0);
                } else {
                    grid.setOverrideColumn(overrideColumnSlider.getValue());
                }

                grid.setFitToWidth(widthButton.getValue());
                if (widthButton.getValue()) {
                    HomeController.getSubGrid().setMaxWidth(Double.POSITIVE_INFINITY);
                } else {
                    grid.setOverrideWidth(widthSlider.getValue());
                }
                grid.setFitToHeight(heightButton.getValue());
                if (heightButton.getValue()) {
                    HomeController.getSubGrid().setMaxHeight(Double.POSITIVE_INFINITY);
                } else {
                    grid.setOverrideHeight(heightSlider.getValue());
                }

                grid.setOverrideStyle(overrideStyleField.getText());
                grid.setSelectionMethod(selectionMethodChoices.getValue());
                grid.enablePagination(paginationButton.getValue());
                grid.setDescription(descriptionArea.getText());
                HomeController.setUpdated(true);

            }

            @Override
            public void resetSettings() {
                buttonOrderField.setText(grid.getOrder().asString());
                fillMethodChoices.setValue(grid.getFillMethod());
                fillDirectionChoices.setValue(grid.getFillDirection());
                hGapSlider.setValue(grid.getCustomHGap());
                vGapSlider.setValue(grid.getCustomVGap());
                marginSlider.setValue(grid.getCustomMargin());
                maxDifficultyField.setText(String.valueOf(grid.getMaxDifficulty()));
                minDifficultyField.setText(String.valueOf(grid.getMinDifficulty()));
                overrideRowSlider.setValue(grid.getOverrideRow());
                overrideRowButton.setValue(grid.getOverrideRow() == 0);
                overrideColumnSlider.setValue(grid.getOverrideColumn());
                overrideColumnButton.setValue(grid.getOverrideColumn() == 0);
                overrideStyleField.setText(grid.getOverrideStyle());
                selectionMethodChoices.setValue(grid.getSelectionMethod());
                paginationButton.setValue(grid.isPaginationEnabled());
                descriptionArea.setText(grid.getDescription());
                HomeController.setUpdated(false);
            }
        };
        return editDialog;
    }

    @Override
    public void run() {
        LOGGER.info("Edit Grid button clicked");

    }

    private String getFolderSelection() {
        String folder = null;
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Export Settings Destination");
        final String homeFolder = getUser().getContent().getHomeFolder();

        final File file = new File(homeFolder);
        directoryChooser.setInitialDirectory(file);
        File directory = directoryChooser.showDialog(getPrimaryControl().getParentUI());
        if (directory != null) {
            folder = directory.getAbsolutePath();
        } else {
            throw new NullPointerException("Folder cannot be null");
        }
        return folder;
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
