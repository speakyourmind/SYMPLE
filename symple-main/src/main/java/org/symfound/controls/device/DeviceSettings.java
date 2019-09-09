/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.device;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import org.apache.log4j.Logger;
import org.symfound.controls.RunnableControl;
import static org.symfound.controls.ScreenControl.setSize;
import static org.symfound.controls.ScreenControl.setSizeMax;
import org.symfound.controls.system.SettingsRow;
import org.symfound.controls.system.SettingsTab;
import org.symfound.controls.system.WebLaunchButton;
import org.symfound.controls.system.dialog.SettingsDialog;
import org.symfound.controls.user.AnimatedLabel;
import org.symfound.controls.user.BuildableGrid;
import org.symfound.controls.user.SwitchDirectionButton;
import org.symfound.device.Device;
import org.symfound.device.emulation.input.switcher.ZScore;
import org.symfound.device.hardware.Hardware;
import org.symfound.device.hardware.characteristic.Movability;
import org.symfound.device.hardware.characteristic.Processability;
import org.symfound.device.hardware.characteristic.Selectability;
import org.symfound.device.processing.ReadMethod;
import org.symfound.device.processing.WriteMethod;

/**
 *
 * @author Javed Gangjee
 * @param <T>
 */
public abstract class DeviceSettings<T extends Hardware> extends SettingsDialog {

    private static final String NAME = DeviceSettings.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public DeviceSettings() {
        super();
        setId("apConfig");
    }

    /**
     *
     */
    public void populate() {
      //  tabs = getTabList();
        buildBase(getHardware().getName());
        addToStackPane(baseGrid);
       
    }

    /**
     *
     * @return
     */
    @Override
    public List<SettingsTab> getTabList() {
        List<SettingsTab> tabList = new ArrayList<>();
        // ABOUT TAB
        SettingsTab aboutTab = buildAboutTab();
        tabList.add(aboutTab);
        // GENERAL TAB
        SettingsTab generalTab = buildGeneralTab();
        tabList.add(generalTab);
        // PROCESSING TAB
        SettingsTab processingTab = buildProcessingTab();
        tabList.add(processingTab);

        SettingsTab movementTab = buildMovementTab();
        tabList.add(movementTab);

        // SELECTION TAB
        if (getHardware().getSelectability().canSelect()) {
            SettingsTab selectionTab = buildSelectionTab();
            tabList.add(selectionTab);
        }
        return tabList;
    }

    /**
     *
     */
    public static final String GENERAL_TAB_TITLE = "GENERAL";

    /**
     *
     * @return
     */
    public abstract SettingsTab buildGeneralTab();

    //ABOUT
    /**
     *
     */
    public static final String DESCRIPTION_TITLE = "Description";

    /**
     *
     */
    public static final String DESCRIPTION_CAPTION = "More about this device";

    /**
     *
     */
    public static final String MANUFACTURER_TITLE = "Manufacturer";

    /**
     *
     */
    public static final String MANUFACTURER_CAPTION = "Maker of this device";

    /**
     *
     */
    public static final String ABOUT_TAB_TITLE = "ABOUT";

    /**
     *
     * @return
     */
    public SettingsTab buildAboutTab() {
        SettingsRow descriptionRow = buildSettingsRow(DESCRIPTION_TITLE, DESCRIPTION_CAPTION);
        
        AnimatedLabel descriptionLabel = new AnimatedLabel();
        descriptionLabel.setWrapText(true);
        descriptionLabel.setText(getHardware().getInfo().getDescription());
        setSizeMax(descriptionLabel);
        setCSS("settings-label", descriptionLabel);
        descriptionRow.add(descriptionLabel, 1, 0, 2, 1);
       

        SettingsRow manufacturerRow = buildSettingsRow(MANUFACTURER_TITLE, MANUFACTURER_CAPTION);
        WebLaunchButton webLaunchButton = new WebLaunchButton();
        webLaunchButton.setText("VISIT");
        webLaunchButton.setPane("apConfig");
        webLaunchButton.setURL(getHardware().getInfo().getURL());
        setSize(webLaunchButton, MAX_WIDTH, MAX_HEIGHT);
        manufacturerRow.add(webLaunchButton, 1, 0);
        AnimatedLabel manufacturerLabel = new AnimatedLabel();
        manufacturerLabel.setText(getHardware().getInfo().getManufacturer());
        setSizeMax(manufacturerLabel);
        setCSS("settings-label", manufacturerLabel);
        manufacturerRow.add(manufacturerLabel, 2, 0);

        List<SettingsRow> rows = Arrays.asList(descriptionRow, manufacturerRow);
        SettingsTab aboutTab = new SettingsTab(ABOUT_TAB_TITLE, rows);

        return aboutTab;
    }

    /**
     *
     */
    public static final String SAMPLE_SIZE_TITLE = "Sample Size";

    /**
     *
     */
    public static final String SAMPLE_SIZE_CAPTION = "Controls the level of accuracy of the mouse cursor";

    /**
     *
     */
    public static final double SAMPLE_SIZE_MIN = 0.0;

    /**
     *
     */
    public static final double SAMPLE_SIZE_MAX = 500.0;

    /**
     *
     */
    public static final double SAMPLE_SIZE_INC = 100.0;

    /**
     *
     */
    public static final String SMOOTHING_FACTOR_TITLE = "Smoothing Factor";

    /**
     *
     */
    public static final String SMOOTHING_FACTOR_CAPTION = "Controls the smoothness of the mouse cursor";

    /**
     *
     */
    public static final double SMOOTHING_MIN = 0.0005;

    /**
     *
     */
    public static final double SMOOTHING_MAX = 0.1;

    /**
     *
     */
    public static final double SMOOTHING_INC = 0.01;

    /**
     *
     */
    public static final String MOVEMENT_TAB_TITLE = "MOVEMENT";

    /**
     *
     * @return
     */
    public SettingsTab buildMovementTab() {
        Movability movability = getHardware().getMovability();
        Slider sampleSizeSlider = new Slider(SAMPLE_SIZE_MIN, SAMPLE_SIZE_MAX, movability.getSampleSize());
        sampleSizeSlider.setMajorTickUnit(SAMPLE_SIZE_INC);
        sampleSizeSlider.setMinorTickCount(100);
        sampleSizeSlider.setValue(movability.getSampleSize());
        sampleSizeSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            movability.setSampleSize(newValue.intValue());
        });
        SettingsRow sampleSizeRow = buildSliderFieldSetting(SAMPLE_SIZE_TITLE, SAMPLE_SIZE_CAPTION, sampleSizeSlider);

        Slider smoothingSlider = new Slider(SMOOTHING_MIN, SMOOTHING_MAX, movability.getSmoothingFactor());
        smoothingSlider.setMajorTickUnit(SMOOTHING_INC);
        smoothingSlider.setMinorTickCount(5);
        smoothingSlider.setSnapToTicks(Boolean.TRUE);
        smoothingSlider.setShowTickMarks(true);
        smoothingSlider.setValue(movability.getSmoothingFactor());
        smoothingSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            movability.setSmoothingFactor(newValue.doubleValue());
        });
        SettingsRow smoothingRow = buildSliderFieldSetting(SMOOTHING_FACTOR_TITLE, SMOOTHING_FACTOR_CAPTION, smoothingSlider);

        List<SettingsRow> rows = Arrays.asList(sampleSizeRow, smoothingRow);
        SettingsTab movementTab = new SettingsTab(MOVEMENT_TAB_TITLE, rows);

        return movementTab;
    }

    /**
     *
     */
    public static final String ZOOM_SCALE_TITLE = "Zoom Scale";

    /**
     *
     */
    public static final String ZOOM_SCALE_CAPTION = "Scale of the magnification window";

    /**
     *
     */
    public static final double ZOOM_SCALE_MIN = 1.0;

    /**
     *
     */
    public static final double ZOOM_SCALE_MAX = 5.0;

    /**
     *
     */
    public static final double ZOOM_SCALE_INC = 0.5;

    /**
     *
     */
    public static final String ZOOM_SIZE_TITLE = "Zoom Size";

    /**
     *
     */
    public static final String ZOOM_SIZE_CAPTION = "Size of the magnification window";

    /**
     *
     */
    public static final int ZOOM_SIZE_MIN = 60;

    /**
     *
     */
    public static final int ZOOM_SIZE_MAX = 360;

    /**
     *
     */
    public static final int ZOOM_SIZE_INC = 60;

    /**
     *
     */
    public static final String POST_SELECT_TITLE = "Post Click Time";

    /**
     *
     */
    public static final String POST_SELECT_CAPTION = "Lock time after a selection is made";

    /**
     *
     */
    public static final double POST_SELECT_MIN = 0.0;

    /**
     *
     */
    public static final double POST_SELECT_MAX = 5.0;

    /**
     *
     */
    public static final double POST_SELECT_INC = 0.5;

    /**
     *
     */
    public static final String SELECTION_TAB_TITLE = "SELECTION";

    /**
     *
     * @return
     */
    public SettingsTab buildSelectionTab() {
        Selectability selectability = getHardware().getSelectability();
        Slider zoomScaleSlider = new Slider(ZOOM_SCALE_MIN, ZOOM_SCALE_MAX, selectability.getZoomScale());
        zoomScaleSlider.setMajorTickUnit(ZOOM_SCALE_INC);
        zoomScaleSlider.setValue(selectability.getZoomScale());
        zoomScaleSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            selectability.setZoomScale(newValue.doubleValue());
        });
        SettingsRow zoomScaleRow = buildSliderFieldSetting(ZOOM_SCALE_TITLE, ZOOM_SCALE_CAPTION, zoomScaleSlider);

        Slider zoomSizeSlider = new Slider(ZOOM_SIZE_MIN, ZOOM_SIZE_MAX, selectability.getZoomSize());
        zoomSizeSlider.setMajorTickUnit(ZOOM_SIZE_INC);
        zoomSizeSlider.setValue(selectability.getZoomSize());
        zoomSizeSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            selectability.setZoomSize(newValue.intValue());
        });
        SettingsRow zoomSizeRow = buildSliderFieldSetting(ZOOM_SIZE_TITLE, ZOOM_SIZE_CAPTION, zoomSizeSlider);

        Slider postSelectSlider = new Slider(POST_SELECT_MIN, POST_SELECT_MAX, selectability.getPostSelectTime());
        postSelectSlider.setMajorTickUnit(POST_SELECT_INC);
        postSelectSlider.setValue(selectability.getPostSelectTime());
        postSelectSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            selectability.setPostSelectTime(newValue.doubleValue());
        });
        SettingsRow postClickRow = buildSliderFieldSetting(POST_SELECT_TITLE, POST_SELECT_CAPTION, postSelectSlider);

        SettingsRow switchRow = buildSettingsRow("Calibrate", "Tune the switch's direction detection");

        BuildableGrid resultGrid = buildSwitchGrid();

        SwitchDirectionButton switchDirection = new SwitchDirectionButton(getDevice());
        switchDirection.setSymStyle("settings-button");
        switchDirection.setControlType(ControlType.SETTING_CONTROL);
        setSize(switchDirection, SettingsDialog.MAX_WIDTH, SettingsDialog.MAX_HEIGHT);
        switchDirection.setOnMouseClicked(null);
        resultGrid.add(switchDirection, 0, 0);

        ZScore zScore = switchDirection.getDirector().getListener().getZScore();
        TextField magnitudeField = new TextField();
        setSize(magnitudeField, MAX_WIDTH, MAX_HEIGHT);
        magnitudeField.getStyleClass().add(TEXT_FIELD_STYLE);
        magnitudeField.setText(String.valueOf(zScore.getMagnitude().intValue()));
        zScore.magnitudeProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                magnitudeField.setText("r=" + String.format("%.2f", newValue.doubleValue()));
            });
        });
        resultGrid.add(magnitudeField, 0, 1);

        TextField angleField = new TextField();
        setSize(angleField, MAX_WIDTH, MAX_HEIGHT);
        angleField.getStyleClass().add(TEXT_FIELD_STYLE);
        angleField.setText(String.valueOf(zScore.getTheta().doubleValue()));
        zScore.thetaProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                angleField.setText("ɵ=" + String.format("%.2f", Math.toDegrees(newValue.doubleValue())).concat("°"));
            });
        });
        resultGrid.add(angleField, 0, 2);

        switchRow.add(resultGrid, 1, 0);

        BuildableGrid switchGrid = buildSwitchGrid();

        Slider tauSlider = new Slider(0, 100, selectability.getSwitchability().getTau());
        tauSlider.setMajorTickUnit(20);
        tauSlider.setShowTickLabels(true);
        tauSlider.setShowTickMarks(true);
        tauSlider.setSnapToTicks(true);
        tauSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            selectability.getSwitchability().setTau(newValue.doubleValue());
        });
        switchGrid.add(tauSlider, 0, 0);

        AnimatedLabel tauLabel = buildSliderLabel("TAU");
        switchGrid.add(tauLabel, 0, 0);

        Slider thresholdSlider = new Slider(0, 10, selectability.getSwitchability().getThreshold());
        thresholdSlider.setMajorTickUnit(2);
        thresholdSlider.setShowTickLabels(true);
        thresholdSlider.setShowTickMarks(true);
        thresholdSlider.setSnapToTicks(true);
        thresholdSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            selectability.getSwitchability().setThreshold(newValue.doubleValue());
        });
        switchGrid.add(thresholdSlider, 0, 1);
        AnimatedLabel thresholdLabel = buildSliderLabel("THRESHOLD");
        switchGrid.add(thresholdLabel, 0, 1);

        Slider hysteresisSlider = new Slider(0, 1, selectability.getSwitchability().getHysteresis());
        hysteresisSlider.setMajorTickUnit(0.2);
        hysteresisSlider.setShowTickLabels(true);
        hysteresisSlider.setShowTickMarks(true);
        hysteresisSlider.setSnapToTicks(true);
        hysteresisSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            selectability.getSwitchability().setHysteresis(newValue.doubleValue());
        });
        switchGrid.add(hysteresisSlider, 0, 2);
        AnimatedLabel hysteresisLabel = buildSliderLabel("HYSTERESIS");
        switchGrid.add(hysteresisLabel, 0, 2);

        Slider arcSensitivitySlider = new Slider(0, 90, 2 * Math.toDegrees(selectability.getSwitchability().getArcSize()));
        arcSensitivitySlider.setMajorTickUnit(10);
        arcSensitivitySlider.setShowTickLabels(true);
        arcSensitivitySlider.setShowTickMarks(true);
        arcSensitivitySlider.setSnapToTicks(true);
        arcSensitivitySlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            selectability.getSwitchability().setArcSize(Math.toRadians(newValue.doubleValue()) / 2);
        });
        switchGrid.add(arcSensitivitySlider, 0, 3);
        AnimatedLabel arcSensitivityLabel = buildSliderLabel("ARC SENSITIVITY (°)");
        switchGrid.add(arcSensitivityLabel, 0, 3);

        switchRow.add(switchGrid, 2, 0, 1, 1);

        List<SettingsRow> rows = Arrays.asList(zoomScaleRow, zoomSizeRow, postClickRow, switchRow);
        SettingsTab selectionTab = new SettingsTab(SELECTION_TAB_TITLE, rows);

        return selectionTab;
    }

    /**
     *
     * @return
     */
    public SettingsTab buildProcessingTab() {
        SettingsRow writeMethodRow = buildWriteMethodRow();
        SettingsRow readMethodRow = buildReadMethodRow();
        Slider scanRateSlider = new Slider(0.005, 0.1, getHardware().getScanRate());
        scanRateSlider.setMajorTickUnit(0.05);
        scanRateSlider.setValue(getHardware().getScanRate());
        scanRateSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            getHardware().setScanRate(newValue.doubleValue());
        });
        SettingsRow scanRateRow = buildSliderFieldSetting("Scan Rate", "Time period between scans", scanRateSlider);

        List<SettingsRow> rows = Arrays.asList(writeMethodRow, readMethodRow, scanRateRow);
        SettingsTab processTab = new SettingsTab("PROCESSING", rows);
        return processTab;
    }

    private SettingsRow buildWriteMethodRow() {
        //WRITE
        final Processability processability = getHardware().getProcessability();
        SettingsRow writeMethodRow = buildSettingsRow("Write Method", "Destination of device data");
        ChoiceBox<String> writeMethodChoiceBox = new ChoiceBox<>();
        writeMethodChoiceBox.setItems(FXCollections.observableArrayList(WriteMethod.TYPES));
        writeMethodChoiceBox.setValue(processability.getWriteMethod());
        writeMethodChoiceBox.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            processability.setWriteMethod(newValue);
        });
        setSize(writeMethodChoiceBox, SettingsDialog.MAX_WIDTH, SettingsDialog.MAX_HEIGHT);
        writeMethodChoiceBox.getStyleClass().add("settings-text-area");
        writeMethodRow.add(writeMethodChoiceBox, 1, 0);

        TextField writePortField = new TextField("0");
        writePortField.setPromptText("Port Number");
        writePortField.setText(processability.getWritePort().toString());
        processability.writePortProperty().addListener((observable, oldValue, newValue) -> {
            writePortField.setText(newValue.toString());
        });
        writePortField.visibleProperty().bind(Bindings.equal(WriteMethod.PORT, writeMethodChoiceBox.valueProperty()));
        setSize(writePortField, SettingsDialog.MAX_WIDTH, SettingsDialog.MAX_HEIGHT);
        writePortField.getStyleClass().add("settings-text-area");
        writeMethodRow.add(writePortField, 2, 0);

        BuildableGrid writeFilePathGrid = buildFileChooserGrid();
        writeFilePathGrid.visibleProperty().bind(Bindings.equal(WriteMethod.FILE, writeMethodChoiceBox.valueProperty()));
        TextField writeFileField = new TextField();
        writeFileField.setPromptText("JSON Request Location");
        writeFilePathGrid.visibleProperty().bind(Bindings.equal(WriteMethod.FILE, writeMethodChoiceBox.valueProperty()));
        writeFileField.setText(processability.getWriteFile());
        writeFileField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            processability.setWriteFile(newValue);
        });
        setSize(writeFileField, Double.POSITIVE_INFINITY, SettingsDialog.MAX_HEIGHT);
        writeFileField.getStyleClass().add("settings-text-area");
        writeFilePathGrid.add(writeFileField, 1, 0);

        RunnableControl writeFileChooser = new RunnableControl() {

            @Override
            public void run() {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Import Settings Source");
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON files", "*.json"));
                File selectedFile = fileChooser.showOpenDialog(getPrimaryControl().getParentUI());
                writeFileField.setText(selectedFile.getAbsolutePath());
            }
        };
        writeFileChooser.setControlType(ControlType.SETTING_CONTROL);
        writeFileChooser.setSymStyle("settings-button");
        writeFileChooser.setText("LOAD");
        setSize(writeFileChooser, SettingsDialog.MAX_WIDTH, SettingsDialog.MAX_HEIGHT);
        writeFilePathGrid.add(writeFileChooser, 0, 0);

        writeMethodRow.add(writeFilePathGrid, 2, 0);
        return writeMethodRow;
    }

    private SettingsRow buildReadMethodRow() {
        final Processability processability = getHardware().getProcessability();
        //READ
        SettingsRow readMethodRow = buildSettingsRow("Read Method", "Source of the device data");
        ChoiceBox<String> readMethodChoiceBox = new ChoiceBox<>();
        readMethodChoiceBox.setItems(FXCollections.observableArrayList(ReadMethod.TYPES));
        readMethodChoiceBox.setValue(processability.getReadMethod());
        readMethodChoiceBox.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            processability.setReadMethod(newValue);
        });
        setSize(readMethodChoiceBox, SettingsDialog.MAX_WIDTH, SettingsDialog.MAX_HEIGHT);
        readMethodChoiceBox.getStyleClass().add("settings-text-area");
        readMethodRow.add(readMethodChoiceBox, 1, 0);

        TextField readPortField = new TextField("0");
        readPortField.setPromptText("Port Number");
        readPortField.visibleProperty().bind(Bindings.equal(ReadMethod.PORT, readMethodChoiceBox.valueProperty()));
        readPortField.setText(processability.getReadPort().toString());
        processability.readPortProperty().addListener((observable, oldValue, newValue) -> {
            readPortField.setText(newValue.toString());
        });
        setSize(readPortField, SettingsDialog.MAX_WIDTH, SettingsDialog.MAX_HEIGHT);
        readPortField.getStyleClass().add("settings-text-area");
        readMethodRow.add(readPortField, 2, 0);

        BuildableGrid readFilePathGrid = buildFileChooserGrid();
        readFilePathGrid.visibleProperty().bind(Bindings.equal(WriteMethod.FILE, readMethodChoiceBox.valueProperty()));

        TextField readFileField = new TextField();
        readFileField.setPromptText("JSON Request Location");
        readFileField.visibleProperty().bind(Bindings.equal(ReadMethod.FILE, readMethodChoiceBox.valueProperty()));
        readFileField.setText(processability.getReadFile());
        readFileField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            processability.setReadFile(newValue);
        });
        setSize(readFileField, Double.POSITIVE_INFINITY, SettingsDialog.MAX_HEIGHT);
        readFileField.getStyleClass().add("settings-text-area");
        readFilePathGrid.add(readFileField, 1, 0);

        RunnableControl readFileChooser = new RunnableControl() {
            @Override
            public void run() {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Import Settings Source");
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON files", "*.json"));
                File selectedFile = fileChooser.showOpenDialog(getPrimaryControl().getParentUI());
                readFileField.setText(selectedFile.getAbsolutePath());
            }
        };
        readFileChooser.setControlType(ControlType.SETTING_CONTROL);
        readFileChooser.setSymStyle("settings-button");
        readFileChooser.setText("LOAD");
        setSize(readFileChooser, SettingsDialog.MAX_WIDTH, SettingsDialog.MAX_HEIGHT);
        readFilePathGrid.add(readFileChooser, 0, 0);
        readMethodRow.add(readFilePathGrid, 2, 0);
        return readMethodRow;
    }

    /**
     *
     * @return
     */
    public BuildableGrid buildFileChooserGrid() {
        BuildableGrid filePathGrid = new BuildableGrid();
        setSizeMax(filePathGrid);
        filePathGrid.setHgap(20.0);
        filePathGrid.setVgap(20.0);
        filePathGrid.setSpecRows(1);
        filePathGrid.buildRows();
        filePathGrid.setSpecColumns(2);
        List<Double> columnPercentages = Arrays.asList(25.0, 75.0);
        filePathGrid.buildColumnsByPerc(columnPercentages);
        return filePathGrid;
    }

    /**
     *
     * @return
     */
    public BuildableGrid buildSwitchGrid() {
        BuildableGrid filePathGrid = new BuildableGrid();
        setSizeMax(filePathGrid);
        filePathGrid.setHgap(20.0);
        filePathGrid.setVgap(20.0);
        filePathGrid.setSpecRows(4);
        filePathGrid.buildRows();
        filePathGrid.setSpecColumns(1);
        filePathGrid.buildColumns();
        return filePathGrid;
    }

    /**
     *
     * @param title
     * @return
     */
    public AnimatedLabel buildSliderLabel(String title) {
        AnimatedLabel label = new AnimatedLabel();
        label.setText(title);
        label.setAlignment(Pos.CENTER);
        label.getStyleClass().add("settings-slider-label");
        setSizeMax(label);
        GridPane.setHalignment(label, HPos.CENTER);
        GridPane.setValignment(label, VPos.CENTER);
        GridPane.setMargin(label, new Insets(40, 0, 0, 0));
        return label;
    }

    /**
     *
     */
    public T hardware;

    /**
     *
     * @return
     */
    public T getHardware() {
        if (hardware == null) {
            hardware = getDevice().getHardware();
        }
        return hardware;
    }

    /**
     *
     */
    public Device<T> device;

    /**
     *
     * @return
     */
    public abstract Device<T> getDevice();
}
