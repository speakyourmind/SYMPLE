/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import org.apache.log4j.Logger;
import org.symfound.builder.user.User;
import static org.symfound.builder.user.characteristic.Ability.MAX_LEVEL;
import org.symfound.builder.user.characteristic.Interaction;
import org.symfound.builder.user.characteristic.Speech;
import org.symfound.builder.user.characteristic.Statistics;
import org.symfound.builder.user.selection.SelectionMethod;
import org.symfound.controls.RunnableControl;
import org.symfound.controls.device.DeviceManager;
import org.symfound.controls.device.EyeTrackerSettings;
import org.symfound.controls.device.GenericDeviceSettings;
import org.symfound.controls.device.SwiftySettings;
import org.symfound.controls.system.OnOffButton;
import org.symfound.controls.system.SettingsExportButton;
import org.symfound.controls.system.SettingsImportButton;
import org.symfound.controls.system.SettingsRestoreButton;
import org.symfound.controls.system.SettingsRow;
import org.symfound.controls.system.UIImportButton;
import org.symfound.controls.system.dialog.EditDialog;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.controls.system.dialog.OKCancelDialog;
import org.symfound.controls.user.voice.TTSManager;
import org.symfound.device.Device;
import org.symfound.device.hardware.Hardware;
import org.symfound.device.hardware.characteristic.Movability;
import org.symfound.device.hardware.characteristic.Selectability;
import org.symfound.device.selection.SelectionEventType;
import org.symfound.main.HomeController;
import org.symfound.main.Main;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
public class UserSettingsButton extends SettingsButtonBase {

    /**
     *
     */
    public static final String NAME = SettingsButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "User Settings";

    /**
     *
     */
    public static final String DEFAULT_TITLE = "Launch User Settings";

    public UserSettingsButton() {
        super("toolbar-settings", KEY, DEFAULT_TITLE);
        initialize();
    }

    private void initialize() {
        getDialog();
    }

    @Override
    public OKCancelDialog getDialog() {
        if (settingsDialog == null) {
            settingsDialog = configureEditDialog();
            settingsDialog.buildDialog();
        }
        return settingsDialog;
    }

    /**
     *
     */
    /**
     *
     * @return
     */
    @Override
    public EditDialog configureEditDialog() {
        EditDialog editDialog = new EditDialog("Settings") {
            private final List<SettingsRow> generalSettings = new ArrayList<>();
            private final List<SettingsRow> deviceSettings = new ArrayList<>();
            private final List<SettingsRow> selectionSettings = new ArrayList<>();
            private final List<SettingsRow> voiceSettings = new ArrayList<>();
            private final List<SettingsRow> typingSettings = new ArrayList<>();
            private final List<SettingsRow> smsSettings = new ArrayList<>();
            private final List<SettingsRow> statsSettings = new ArrayList<>();
            private final List<SettingsRow> aboutSettings = new ArrayList<>();
            private final List<SettingsRow> profileSettings = new ArrayList<>();

            private ChoiceBox<SelectionMethod> selectionChoiceBox;
            private DiagnosticTestButton diagnosticTest;
            private Slider dwellTimeSlider;
            private Slider scanTimeSlider;
            private Slider stepTimeSlider;
            private Slider scrollTimeSlider;
            private Slider levelSlider;
            private OnOffButton assistedModeButton;

            private ChoiceBox<String> deviceChoiceBox;
            private DeviceConfigButton configureDeviceButton;
            private OnOffButton mouseControlButton;
            private OnOffButton selectionControlButton;
            private Slider dwellSensitivitySlider;
            private ChoiceBox<String> selectionEventChoiceBox;
            private ChoiceBox<String> cbSwitchDirection;

            //Voice
            private ChoiceBox<String> voiceChoices;
            //SMS
            public TextField accountSIDField;
            public TextField authTokenField;
            public TextField fromNumberField;
            //Selection //TODO:Move to Selection Mode specific Settings
            private OnOffButton onFirstClickButton;
            private OnOffButton speakSelectionButton;
            private OnOffButton highlightBorderButton;
            private OnOffButton fullScreenButton;
            private OnOffButton playScourButton;
            public TextField playScourField;
            private OnOffButton playSelectButton;
            public TextField playSelectionField;
            private Slider scrollDistanceSlider;

            private ChoiceBox<Cursor> cursorChoiceBox;

            //Typing
            private Slider userDictWeightSlider;
            private OnOffButton autocompleteButton;
            private Slider autocompleteSlider;

            private TextField homeFolderField;
            private RunnableControl homeFolderButton;
            private Slider volumeSlider;

            private AnimatedLabel sessionSelectionsLabel;
            private AnimatedLabel sessionWordsSpokenLabel;
            private AnimatedLabel sessionTimeInUseLabel;
            private AnimatedLabel sessionStartTimeLabel;
            private RunnableControl resetSessionUsageButton;

            private AnimatedLabel totalSelectionsLabel;
            private AnimatedLabel totalWordsSpokenLabel;
            private AnimatedLabel totalSessionCountLabel;
            private AnimatedLabel totalTimeInUseLabel;
            private AnimatedLabel firstUsedLabel;
            private AnimatedLabel lastUsedLabel;

            RunnableControl resetUsageButton;
            OnOffButton recordButton;

            private OnOffButton autoUpdateButton;
            private SettingsExportButton exportButton;
            private SettingsRestoreButton resetButton;
            private SettingsImportButton importButton;
            private UIImportButton importUIButton;

            private AnimatedLabel versionLabel;
            private AnimatedLabel emailLabel;
            private AnimatedLabel disclaimerLabel;
            private VersionUpdateButton updateButton;

            @Override
            public Node addSettingControls() {
                selectionChoiceBox = new ChoiceBox<>();
                selectionChoiceBox.setItems(FXCollections.observableArrayList(SelectionMethod.values()));

                SettingsRow levelRow = EditDialog.createSettingRow("Level", "Coincides with user's ability to select number of buttons in each dimension");
                diagnosticTest = new DiagnosticTestButton();
                diagnosticTest.setText("TEST");
                diagnosticTest.setSymStyle("settings-button");
                diagnosticTest.setControlType(ControlType.SETTING_CONTROL);
                diagnosticTest.setMaxSize(180.0, 60.0);
                GridPane.setHalignment(diagnosticTest, HPos.LEFT);
                GridPane.setValignment(diagnosticTest, VPos.CENTER);
                levelSlider = new Slider(1, MAX_LEVEL, 4);
                levelSlider.setValue(getUser().getAbility().getLevel());
                diagnosticTest.visibleProperty().bind(Bindings.and(
                        levelSlider.valueProperty().isNotEqualTo(30), Bindings.or(
                        selectionChoiceBox.valueProperty().isEqualTo(SelectionMethod.CLICK),
                        selectionChoiceBox.valueProperty().isEqualTo(SelectionMethod.DWELL))));
                levelRow.add(diagnosticTest, 1, 0, 1, 1);
                levelSlider.setMajorTickUnit(5);
                levelSlider.setMinorTickCount(10);
                levelSlider.setShowTickLabels(true);
                levelSlider.setShowTickMarks(true);
                levelSlider.setSnapToTicks(true);

                levelRow.add(levelSlider, 2, 0, 1, 1);

                SettingsRow selectionModeRow = EditDialog.createSettingRow("Method", "Configure how your device makes selections");
                selectionChoiceBox.setMaxSize(180.0, 60.0);
                selectionModeRow.add(selectionChoiceBox, 1, 0, 1, 1);
                dwellTimeSlider = new Slider(1, 5, 1);
                dwellTimeSlider.setValue(getUser().getTiming().getDwellTime());
                dwellTimeSlider.visibleProperty().bind(Bindings.equal(selectionChoiceBox.valueProperty(), SelectionMethod.DWELL));

                dwellTimeSlider.setMajorTickUnit(1);
                dwellTimeSlider.setMinorTickCount(4);
                dwellTimeSlider.setShowTickLabels(true);
                dwellTimeSlider.setShowTickMarks(true);

                selectionModeRow.add(dwellTimeSlider, 2, 0, 1, 1);

                scanTimeSlider = new Slider(1, 10, 1);
                scanTimeSlider.setValue(getUser().getTiming().getScanTime());
                scanTimeSlider.visibleProperty().bind(Bindings.or(Bindings.equal(selectionChoiceBox.valueProperty(), SelectionMethod.SCAN), Bindings.equal(selectionChoiceBox.valueProperty(), SelectionMethod.CLICK)));
                scanTimeSlider.setMajorTickUnit(1);
                scanTimeSlider.setMinorTickCount(4);
                scanTimeSlider.setShowTickLabels(true);
                scanTimeSlider.setShowTickMarks(true);

                selectionModeRow.add(scanTimeSlider, 2, 0, 1, 1);

                scrollTimeSlider = new Slider(0.01, 1.0, 0.05);
                scrollTimeSlider.setValue(getUser().getTiming().getScrollTime());
                scrollTimeSlider.visibleProperty().bind((Bindings.equal(selectionChoiceBox.valueProperty(), SelectionMethod.SCROLL)));
                scrollTimeSlider.setMajorTickUnit(0.1);
                scrollTimeSlider.setMinorTickCount(1);
                scrollTimeSlider.setShowTickLabels(true);
                scrollTimeSlider.setShowTickMarks(true);

                selectionModeRow.add(scrollTimeSlider, 2, 0, 1, 1);

                stepTimeSlider = new Slider(1, 20, 1);
                stepTimeSlider.setValue(getUser().getTiming().getStepTime());
                stepTimeSlider.visibleProperty().bind(Bindings.equal(selectionChoiceBox.valueProperty(), SelectionMethod.STEP));
                stepTimeSlider.setMajorTickUnit(1);
                stepTimeSlider.setMinorTickCount(4);
                stepTimeSlider.setShowTickLabels(true);
                stepTimeSlider.setShowTickMarks(true);

                selectionModeRow.add(stepTimeSlider, 2, 0, 1, 1);
                selectionChoiceBox.setValue(getUser().getInteraction().getSelectionMethod());

                SettingsRow assistedModeRow = createSettingRow("Assisted Mode", "Enable when users need assistance in navigating menus");
                assistedModeButton = new OnOffButton("YES", "NO");
                assistedModeButton.setMaxSize(180.0, 60.0);
                assistedModeButton.setValue(getUser().getInteraction().isInAssistedMode());
                GridPane.setHalignment(assistedModeButton, HPos.LEFT);
                GridPane.setValignment(assistedModeButton, VPos.CENTER);
                assistedModeRow.add(assistedModeButton, 1, 0, 1, 1);

                SettingsRow mouseControlRow = createSettingRow("Mouse Control", "Determine how the device controls the mouse");
                mouseControlButton = new OnOffButton("YES", "NO");
                mouseControlButton.setMaxSize(180.0, 60.0);
                mouseControlButton.setValue(getUser().getInteraction().needsMouseControl());
                GridPane.setHalignment(mouseControlButton, HPos.LEFT);
                GridPane.setValignment(mouseControlButton, VPos.CENTER);
                mouseControlRow.add(mouseControlButton, 1, 0, 1, 1);

                SettingsRow selectionControlRow = createSettingRow("Selection Control", "Determine how the device registers a click");
                selectionControlButton = new OnOffButton("YES", "NO");
                selectionControlButton.setMaxSize(180.0, 60.0);
                selectionControlButton.setValue(getUser().getInteraction().needsSelectionControl());
                GridPane.setHalignment(selectionControlButton, HPos.LEFT);
                GridPane.setValignment(selectionControlButton, VPos.CENTER);
                selectionControlRow.add(selectionControlButton, 1, 0, 1, 1);

                dwellSensitivitySlider = new Slider(1, 60, 10);
                dwellSensitivitySlider.setValue(getUser().getTyping().getAutoCompleteTime());
                //    dwellSensitivitySlider.visibleProperty().bind(dwellSensitivityButton.valueProperty());
                dwellSensitivitySlider.setMajorTickUnit(10);
                dwellSensitivitySlider.setMinorTickCount(5);
                dwellSensitivitySlider.setShowTickLabels(true);
                dwellSensitivitySlider.setShowTickMarks(true);

                selectionControlRow.add(dwellSensitivitySlider, 2, 0, 1, 1);

                selectionEventChoiceBox = new ChoiceBox<>();
                selectionEventChoiceBox.setItems(FXCollections.observableArrayList(SelectionEventType.EVENT_TYPES));
                final String selectedDeviceName = getSession().getDeviceManager().getIterator().get();
                getUser().setDeviceName(selectedDeviceName);
                Hardware hardware = getSession().getDeviceManager().get(selectedDeviceName).getHardware();
                final Selectability selectability = hardware.getSelectability();
                selectionEventChoiceBox.setVisible(selectability.getClickability().isEnabled());
                selectionEventChoiceBox.setValue(selectability.getClickability().getEventType().getValue());
                selectionEventChoiceBox.setMaxSize(180.0, 60.0);
                selectionEventChoiceBox.getStyleClass().add("settings-text-area");
                selectionControlRow.add(selectionEventChoiceBox, 2, 0, 1, 1);

                SettingsRow deviceRow = EditDialog.createSettingRow("Device", "Select the device you are using");

                deviceChoiceBox = new ChoiceBox<>();
                deviceChoiceBox.setItems(FXCollections.observableArrayList(Hardware.DEVICE_TYPES));
                deviceChoiceBox.setMaxSize(180.0, 60.0);
                // deviceButton.setControlType(ControlType.SETTING_CONTROL);
                final String activeDevice = getUser().getDeviceName();
                getSession().getDeviceManager().getIterator().set(activeDevice);
                deviceChoiceBox.setValue(activeDevice);
                Hardware hardware1 = getSelectedHardware();
                resetMouseControl(hardware1);
                resetSelectionControl(hardware1);
                deviceChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                    deviceChoiceBox.setValue(newValue);
                    Hardware hardware2 = getSession().getDeviceManager().get(newValue).getHardware();
                    resetMouseControl(hardware2);
                    resetSelectionControl(hardware2);
                });

                deviceRow.add(deviceChoiceBox, 2, 0, 1, 1);
                configureDeviceButton = new DeviceConfigButton() {

                    @Override
                    public void execute() {
                        if (isConfirmable() && !(getUser().getDeviceName().equals(deviceChoiceBox.getValue()))) {
                            if (getSession().isBuilt()) {
                                stackPane.getChildren().add(getPopup(getDialog()));
                                final Double selectionTime = getSession().getUser().getInteraction().getSelectionTime();
                                getDialog().animate().startScale(selectionTime, 0.8, 1.0);
                            }
                        } else {
                            Platform.runLater(this);
                        }
                    }

                    @Override
                    public void run() {
                        final String selectedDeviceName = deviceChoiceBox.getValue();
                        getUser().setDeviceName(selectedDeviceName);
                        DeviceManager deviceManager = getSession().getDeviceManager();
                        switch (deviceChoiceBox.getValue()) {
                            case Hardware.SWIFTY:
                                SwiftySettings swiftyConfiguration = new SwiftySettings();
                                launchConfiguration(swiftyConfiguration);
                                break;
                            case Hardware.EYE_TRACKER:
                                EyeTrackerSettings tobiiConfiguration = new EyeTrackerSettings();
                                launchConfiguration(tobiiConfiguration);
                                break;
                            case Hardware.GENERIC:
                                GenericDeviceSettings genericConfiguration = new GenericDeviceSettings();
                                launchConfiguration(genericConfiguration);
                                break;
                        }
                    }

                };
                configureDeviceButton.setControlType(ControlType.SETTING_CONTROL);
                configureDeviceButton.setConfirmable(Boolean.TRUE);
                configureDeviceButton.setSymStyle("settings-button");
                configureDeviceButton.setMaxSize(180.0, 60.0);
                configureDeviceButton.setText("CONFIGURE");
                configureDeviceButton.setOkText("CONFIRM");
                configureDeviceButton.setCancelText("CANCEL");
                deviceRow.add(configureDeviceButton, 1, 0, 1, 1);

                SettingsRow voiceRow = createSettingRow("Voice", "Choose your speaking voice");
                TTSManager ttsPlayer = getSession().getTTSManager();
                voiceChoices = new ChoiceBox<>(FXCollections.observableArrayList(ttsPlayer.getLauncher().getVoiceNames()));
                final Speech speech = getUser().getSpeech();
                voiceChoices.setValue(speech.getSpeakingVoice());
                voiceChoices.setMaxSize(180.0, 60.0);
                voiceChoices.getStyleClass().add("settings-text-area");
                voiceRow.add(voiceChoices, 1, 0, 2, 1);

                SettingsRow autocompleteRow = EditDialog.createSettingRow("Autocomplete", "Length of time the autocompleted word will appear when typing");
                autocompleteButton = new OnOffButton("YES", "NO");
                autocompleteButton.setMaxSize(180.0, 60.0);
                autocompleteButton.setValue(getUser().getTyping().needsAutoComplete());
                GridPane.setHalignment(autocompleteButton, HPos.LEFT);
                GridPane.setValignment(autocompleteButton, VPos.CENTER);
                autocompleteRow.add(autocompleteButton, 1, 0, 1, 1);

                autocompleteSlider = new Slider(1, 60, 10);
                autocompleteSlider.setValue(getUser().getTyping().getAutoCompleteTime());
                autocompleteSlider.visibleProperty().bind(autocompleteButton.valueProperty());
                autocompleteSlider.setMajorTickUnit(10);
                autocompleteSlider.setMinorTickCount(5);
                autocompleteSlider.setShowTickLabels(true);
                autocompleteSlider.setShowTickMarks(true);

                autocompleteRow.add(autocompleteSlider, 2, 0, 1, 1);

                SettingsRow userDictWeightRow = EditDialog.createSettingRow("User Vocabulary", "Frequency of user's vocabulary in autocompleted words");
                userDictWeightSlider = new Slider(0, 100, 10);
                userDictWeightSlider.setValue(getUser().getTyping().getDictionaryWeight());
                userDictWeightSlider.setMajorTickUnit(10);
                userDictWeightSlider.setMinorTickCount(5);
                userDictWeightSlider.setShowTickLabels(true);
                userDictWeightSlider.setShowTickMarks(true);
                userDictWeightRow.add(userDictWeightSlider, 2, 0, 1, 1);

                SettingsRow twilioIDRow = EditDialog.createSettingRow("Twilio ID", "SID associated with your Twilio account");
                accountSIDField = new TextField();
                accountSIDField.setText(getUser().getSocial().getTwilioAccountSID());
                accountSIDField.setPrefSize(180.0, 60.0);
                accountSIDField.getStyleClass().add("settings-text-area");
                twilioIDRow.add(accountSIDField, 1, 0, 2, 1);

                SettingsRow twilioTokenRow = EditDialog.createSettingRow("Twilio Token", "Auth token associated with your Twilio account");
                authTokenField = new TextField();
                authTokenField.setText(getUser().getSocial().getTwilioAuthToken());
                authTokenField.setPrefSize(180.0, 60.0);
                authTokenField.getStyleClass().add("settings-text-area");
                twilioTokenRow.add(authTokenField, 1, 0, 2, 1);

                SettingsRow twilioNumberRow = EditDialog.createSettingRow("Twilio Number", "Number assigned to your Twilio account");
                fromNumberField = new TextField();
                fromNumberField.setText(getUser().getSocial().getTwilioFromNumber());
                fromNumberField.setPrefSize(180.0, 60.0);
                fromNumberField.getStyleClass().add("settings-text-area");
                twilioNumberRow.add(fromNumberField, 1, 0, 2, 1);

                SettingsRow onFirstClickRow = createSettingRow("Scan Right Away", "Start scanning with first click");
                onFirstClickButton = new OnOffButton("YES", "NO");
                onFirstClickButton.setMaxSize(180.0, 60.0);
                onFirstClickButton.setValue(getUser().getNavigation().onFirstClick());
                GridPane.setHalignment(onFirstClickButton, HPos.LEFT);
                GridPane.setValignment(onFirstClickButton, VPos.CENTER);
                onFirstClickRow.add(onFirstClickButton, 1, 0, 1, 1);

                SettingsRow speakSelectionRow = createSettingRow("Speak Selection", "Speak selection during scan");
                speakSelectionButton = new OnOffButton("YES", "NO");
                speakSelectionButton.setMaxSize(180.0, 60.0);
                speakSelectionButton.setValue(getUser().getNavigation().speakSelection());
                GridPane.setHalignment(speakSelectionButton, HPos.LEFT);
                GridPane.setValignment(speakSelectionButton, VPos.CENTER);
                speakSelectionRow.add(speakSelectionButton, 1, 0, 1, 1);

                SettingsRow highlightBorderRow = createSettingRow("Highlight Border", "Highlight border during tabbing");
                highlightBorderButton = new OnOffButton("YES", "NO");
                highlightBorderButton.setMaxSize(180.0, 60.0);
                highlightBorderButton.setValue(getUser().getNavigation().highlightBorder());
                GridPane.setHalignment(highlightBorderButton, HPos.LEFT);
                GridPane.setValignment(highlightBorderButton, VPos.CENTER);
                highlightBorderRow.add(highlightBorderButton, 1, 0, 1, 1);

                SettingsRow fullScreenRow = createSettingRow("Full Screen Scan", "Go full screen during scanning");
                fullScreenButton = new OnOffButton("YES", "NO");
                fullScreenButton.setMaxSize(180.0, 60.0);
                fullScreenButton.setValue(getUser().getNavigation().fullScreen());
                GridPane.setHalignment(fullScreenButton, HPos.LEFT);
                GridPane.setValignment(fullScreenButton, VPos.CENTER);
                fullScreenRow.add(fullScreenButton, 1, 0, 1, 1);

                SettingsRow playScourRow = createSettingRow("Scanning Sound", "Play sound while scanning");
                playScourButton = new OnOffButton("YES", "NO");
                playScourButton.setMaxSize(180.0, 60.0);
                playScourButton.setValue(getUser().getNavigation().playScourSound());
                GridPane.setHalignment(playScourButton, HPos.LEFT);
                GridPane.setValignment(playScourButton, VPos.CENTER);
                playScourRow.add(playScourButton, 1, 0, 1, 1);

                playScourField = new TextField();
                playScourField.setText(getUser().getNavigation().getScourSound());
                playScourField.setPrefSize(180.0, 60.0);
                playScourField.getStyleClass().add("settings-text-area");
                playScourRow.add(playScourField, 2, 0, 1, 1);

                SettingsRow playSelectionRow = createSettingRow("Selection Sound", "Play sound on selection");
                playSelectButton = new OnOffButton("YES", "NO");
                playSelectButton.setMaxSize(180.0, 60.0);
                playSelectButton.setValue(getUser().getNavigation().playSelectSound());
                GridPane.setHalignment(playSelectButton, HPos.LEFT);
                GridPane.setValignment(playSelectButton, VPos.CENTER);
                playSelectionRow.add(playSelectButton, 1, 0, 1, 1);

                playSelectionField = new TextField();
                playSelectionField.setText(getUser().getNavigation().getSelectionSound());
                playSelectionField.setPrefSize(180.0, 60.0);
                playSelectionField.getStyleClass().add("settings-text-area");
                playSelectionRow.add(playSelectionField, 2, 0, 1, 1);

                SettingsRow scrollDistanceRow = EditDialog.createSettingRow("Scroll Distance", "Amount to scroll the pane");
                scrollDistanceSlider = new Slider(0.01, 1.0, 0.05);
                scrollDistanceSlider.setValue(getUser().getNavigation().getScrollDistance());
                scrollDistanceSlider.visibleProperty().bind((Bindings.equal(selectionChoiceBox.valueProperty(), SelectionMethod.SCROLL)));
                scrollDistanceSlider.setMajorTickUnit(0.1);
                scrollDistanceSlider.setMinorTickCount(1);
                scrollDistanceSlider.setShowTickLabels(true);
                scrollDistanceSlider.setShowTickMarks(true);

                scrollDistanceRow.add(scrollDistanceSlider, 2, 0, 1, 1);

                SettingsRow cursorRow = createSettingRow("Cursor", "Choose the look of the cursor");
                cursorChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(Arrays.asList(Cursor.NONE, Cursor.DEFAULT, Cursor.HAND)));
                cursorChoiceBox.setValue(getUser().getNavigation().getCursor());
                cursorChoiceBox.setMaxSize(180.0, 60.0);
                cursorChoiceBox.getStyleClass().add("settings-text-area");
                cursorRow.add(cursorChoiceBox, 1, 0, 2, 1);

                SettingsRow homeFolderRow = createSettingRow("Home folder", "Folder to search for all the media files");
                homeFolderButton = new RunnableControl("settings-button") {
                    @Override
                    public void run() {

                        DirectoryChooser directoryChooser = new DirectoryChooser();

                        File selectedDirectory = directoryChooser.showDialog(this.getScene().getWindow());
                        if (selectedDirectory == null) {
                            homeFolderField.setText(getUser().getContent().getHomeFolder());
                        } else {
                            homeFolderField.setText(selectedDirectory.getAbsolutePath());
                        }
                    }
                };
                homeFolderButton.setText("SELECT");
                homeFolderButton.setControlType(ControlType.SETTING_CONTROL);
                homeFolderButton.setMaxSize(180.0, 60.0);
                GridPane.setHalignment(homeFolderButton, HPos.LEFT);
                GridPane.setValignment(homeFolderButton, VPos.CENTER);
                homeFolderRow.add(homeFolderButton, 1, 0, 1, 1);

                homeFolderField = new TextField();
                homeFolderField.setText(getUser().getContent().getHomeFolder());
                homeFolderField.setPrefSize(180.0, 60.0);
                homeFolderField.getStyleClass().add("settings-text-area");
                homeFolderRow.add(homeFolderField, 2, 0, 1, 1);

                SettingsRow volumeRow = EditDialog.createSettingRow("Volume", "Set the internal program volume");
                volumeSlider = new Slider(1, 60, 10);
                volumeSlider.setValue(getUser().getTyping().getDictionaryWeight());
                volumeSlider.setMajorTickUnit(10);
                volumeSlider.setMinorTickCount(5);
                volumeSlider.setShowTickLabels(true);
                volumeSlider.setShowTickMarks(true);
                volumeRow.add(volumeSlider, 2, 0, 1, 1);

                final Statistics statistics = getSession().getUser().getStatistics();
                SettingsRow sessionSelectionsRow = EditDialog.createSettingRow("Session Selections", "Button selections in this session");
                sessionSelectionsLabel = new AnimatedLabel();
                sessionSelectionsLabel.setStyle("-fx-font-size:3em;");
                sessionSelectionsLabel.textProperty().bind(statistics.sessionSelectionsProperty().asString());
                sessionSelectionsRow.add(sessionSelectionsLabel, 1, 0, 1, 1);

                SettingsRow sessionWordsSpokenRow = EditDialog.createSettingRow("Session Words Spoken", "Number of words spoken in this session");
                sessionWordsSpokenLabel = new AnimatedLabel();
                sessionWordsSpokenLabel.setStyle("-fx-font-size:3em;");
                sessionWordsSpokenLabel.textProperty().bind(statistics.sessionSpokenWordCountProperty().asString());
                sessionWordsSpokenRow.add(sessionWordsSpokenLabel, 1, 0, 1, 1);

                SettingsRow sessionTimeInUseRow = EditDialog.createSettingRow("Session Time In Use", "Session time in use");
                sessionTimeInUseLabel = new AnimatedLabel();
                sessionTimeInUseLabel.setStyle("-fx-font-size:3em;");
                statistics.sessionTimeInUseProperty().addListener((observable, oldValue, newValue) -> {

                    String timeString = splitIntoHMS(newValue);
                    sessionTimeInUseLabel.setText(timeString);
                });
                //  sessionTimeInUseLabel.textProperty().bind(Bindings.concat(getSession().getUser().getStatistics().sessionTimeInUseProperty().asString(), " seconds"));
                sessionTimeInUseRow.add(sessionTimeInUseLabel, 1, 0, 2, 1);

                SettingsRow sessionStartTimeRow = EditDialog.createSettingRow("Session Start", "Time this session began");
                sessionStartTimeLabel = new AnimatedLabel();
                sessionStartTimeLabel.setStyle("-fx-font-size:3em;");
                Date date = new Date(statistics.getSessionStartTime());
                DateFormat formatter = new SimpleDateFormat("d MMM yyyy, HH:mm:ss aaa");
                formatter.setTimeZone(TimeZone.getDefault());
                String startTime = formatter.format(date);
                sessionStartTimeLabel.setText(startTime);
                sessionStartTimeRow.add(sessionStartTimeLabel, 1, 0, 2, 1);

                SettingsRow resetSessionStatsRow = EditDialog.createSettingRow("Reset Session Stats", "Clear statistics data for this session");

                resetSessionUsageButton = new RunnableControl("settings-button") {
                    @Override
                    public void run() {
                        getUser().getStatistics().resetSessionStats();
                    }
                };
                resetSessionUsageButton.setControlType(ControlType.SETTING_CONTROL);
                resetSessionUsageButton.setText("RESET");
                resetSessionUsageButton.setMaxSize(180.0, 60.0);
                resetSessionStatsRow.add(resetSessionUsageButton, 1, 0, 1, 1);

                SettingsRow totalSelectionsRow = EditDialog.createSettingRow("Total Selections", "Total of all button clicks");
                totalSelectionsLabel = new AnimatedLabel();
                totalSelectionsLabel.setStyle("-fx-font-size:3em;");
                totalSelectionsLabel.textProperty().bind(statistics.totalSelectionCountProperty().asString());
                totalSelectionsRow.add(totalSelectionsLabel, 1, 0, 1, 1);

                SettingsRow totalTimeInUseRow = EditDialog.createSettingRow("Total Time In Use", "Total time in use");
                totalTimeInUseLabel = new AnimatedLabel();
                totalTimeInUseLabel.setStyle("-fx-font-size:3em;");

                statistics.totalTimeInUseProperty().addListener((observable, oldValue, newValue) -> {
                    String timeString = splitIntoHMS(newValue);
                    totalTimeInUseLabel.setText(timeString);
                });
                totalTimeInUseRow.add(totalTimeInUseLabel, 1, 0, 2, 1);

                SettingsRow totalWordsSpokenRow = EditDialog.createSettingRow("Total Words Spoken", "Total of all words spoken");
                totalWordsSpokenLabel = new AnimatedLabel();
                totalWordsSpokenLabel.setStyle("-fx-font-size:3em;");
                totalWordsSpokenLabel.textProperty().bind(statistics.totalSpokenWordsCountProperty().asString());
                totalWordsSpokenRow.add(totalWordsSpokenLabel, 1, 0, 1, 1);

                SettingsRow totalSessionCountRow = EditDialog.createSettingRow("Total Sessions", "Number of times the program is launched");
                totalSessionCountLabel = new AnimatedLabel();
                totalSessionCountLabel.setStyle("-fx-font-size:3em;");
                totalSessionCountLabel.textProperty().bind(statistics.totalSessionCountProperty().asString());
                totalSessionCountRow.add(totalSessionCountLabel, 1, 0, 1, 1);

                SettingsRow lastUsedRow = EditDialog.createSettingRow("Previous Session", "Last time the program was run");
                lastUsedLabel = new AnimatedLabel("");
                lastUsedLabel.setStyle("-fx-font-size:3em;");
                Date lastUsedDate = new Date(statistics.getLastUsed());
                DateFormat lastUsedFormatter = new SimpleDateFormat("d MMM yyyy, HH:mm:ss aaa");
                lastUsedFormatter.setTimeZone(TimeZone.getDefault());
                String lastUsedTime = lastUsedFormatter.format(lastUsedDate);
                // if (getLastUsed() != 0L) {
                lastUsedLabel.setText(lastUsedTime);

                lastUsedRow.add(lastUsedLabel, 1, 0, 2, 1);

                SettingsRow resetAllStatsRow = EditDialog.createSettingRow("Reset All Stats", "Clear all statistics data");

                resetUsageButton = new RunnableControl("settings-button") {
                    @Override
                    public void run() {
                        getUser().getStatistics().resetAllStats();
                    }
                };
                resetUsageButton.setControlType(ControlType.SETTING_CONTROL);
                resetUsageButton.setText("RESET");
                resetUsageButton.setMaxSize(180.0, 60.0);
                resetAllStatsRow.add(resetUsageButton, 1, 0, 1, 1);

                SettingsRow recordingRow = EditDialog.createSettingRow("Pause Recording", "");

                recordButton = new OnOffButton("RECORDING", "PAUSED");
                recordButton.setMaxSize(180.0, 60.0);
                recordButton.setValue(getUser().getStatistics().isRecording());
                recordButton.valueProperty().bindBidirectional(getUser().getStatistics().recordProperty());
                GridPane.setHalignment(recordButton, HPos.LEFT);
                GridPane.setValignment(recordButton, VPos.CENTER);
                recordingRow.add(recordButton, 1, 0, 1, 1);

                SettingsRow autoUpdateRow = createSettingRow("Auto Update", "Update the application on exit when required");
                autoUpdateButton = new OnOffButton("YES", "NO");
                autoUpdateButton.setMaxSize(180.0, 60.0);
                autoUpdateButton.setValue(getUser().getProfile().autoUpdate());
                GridPane.setHalignment(autoUpdateButton, HPos.LEFT);
                GridPane.setValignment(autoUpdateButton, VPos.CENTER);
                autoUpdateRow.add(autoUpdateButton, 1, 0, 1, 1);

                SettingsRow exportRow = EditDialog.createSettingRow("Save Settings", "Export all settings to an XML file");
                exportButton = new SettingsExportButton();
                exportButton.setControlType(ControlType.SETTING_CONTROL);
                exportButton.setConfirmable(Boolean.TRUE);
                exportButton.setSymStyle("settings-button");
                exportButton.setMaxSize(180.0, 60.0);
                exportButton.setText("EXPORT");
                exportButton.setOkText("CONFIRM");
                exportButton.setCancelText("CANCEL");
                exportRow.add(exportButton, 1, 0, 1, 1);

                SettingsRow resetRow = EditDialog.createSettingRow("Delete Everything", "Wipe all settings");
                resetButton = new SettingsRestoreButton();
                resetButton.setControlType(ControlType.SETTING_CONTROL);
                resetButton.setConfirmable(Boolean.TRUE);
                resetButton.setSymStyle("settings-button");
                resetButton.setMaxSize(180.0, 60.0);
                resetButton.setText("RESET");
                resetButton.setOkText("CONFIRM");
                resetButton.setCancelText("CANCEL");
                resetRow.add(resetButton, 1, 0, 1, 1);

                SettingsRow importRow = EditDialog.createSettingRow("Import Settings", "Import XMLs for profiles or individual boards");
                importButton = new SettingsImportButton();
                importButton.setControlType(ControlType.SETTING_CONTROL);
                importButton.setConfirmable(Boolean.TRUE);
                importButton.setSymStyle("settings-button");
                importButton.setMaxSize(180.0, 60.0);
                importButton.setText("PROFILE");
                importButton.setOkText("CONFIRM");
                importButton.setCancelText("CANCEL");
                importRow.add(importButton, 1, 0, 1, 1);
                importUIButton = new UIImportButton();
                importUIButton.setControlType(ControlType.SETTING_CONTROL);
                importUIButton.setConfirmable(Boolean.TRUE);
                importUIButton.setSymStyle("settings-button");
                importUIButton.setMaxSize(180.0, 60.0);
                importUIButton.setText("BOARDS");
                importUIButton.setOkText("CONFIRM");
                importUIButton.setCancelText("CANCEL");
                importRow.add(importUIButton, 2, 0, 1, 1);

                SettingsRow versionRow = EditDialog.createSettingRow("Version", "Current version of the software");

                versionLabel = new AnimatedLabel(Main.VERSION);
                versionLabel.getStyleClass().add("settings-label");
                versionRow.add(versionLabel, 2, 0, 1, 1);
                updateButton = new VersionUpdateButton();
                updateButton.setControlType(ControlType.SETTING_CONTROL);
                updateButton.setConfirmable(Boolean.TRUE);
                updateButton.setSymStyle("settings-button");
                updateButton.setMaxSize(180.0, 60.0);
                updateButton.setText("UPDATE");
                updateButton.setOkText("CONFIRM");
                updateButton.setCancelText("CANCEL");
                versionRow.add(updateButton, 1, 0, 1, 1);

                SettingsRow emailRow = EditDialog.createSettingRow("Contact", "Send us an email if you find an issue with the software");
                emailLabel = new AnimatedLabel("support@speakyourmindfoundation.org");
                emailLabel.getStyleClass().add("settings-label");
                emailRow.add(emailLabel, 1, 0, 2, 1);
                SettingsRow disclaimerRow = EditDialog.createSettingRow("Disclaimer", "");
                disclaimerLabel = new AnimatedLabel("Systems (hardware and software) provided by the SpeakYourMind Foundation are not medical devices and have not been approved for use by the FDA; therefore, these systems shall not be used or depended on to provide medical treatment or to make any form of healthcare decisions. Other means of communication must be used for any medical related purpose.");
                disclaimerLabel.getStyleClass().add("settings-caption");
                disclaimerLabel.setWrapText(Boolean.TRUE);
                disclaimerRow.add(disclaimerLabel, 1, 0, 2, 1);

                generalSettings.add(levelRow);
                generalSettings.add(selectionModeRow);
                generalSettings.add(assistedModeRow);
                generalSettings.add(homeFolderRow);
                Tab generalTab = buildTab("General", generalSettings);

                deviceSettings.add(deviceRow);
                deviceSettings.add(mouseControlRow);
                deviceSettings.add(selectionControlRow);
                Tab deviceTab = buildTab("Device", deviceSettings);

                statsSettings.add(sessionSelectionsRow);
                statsSettings.add(sessionWordsSpokenRow);
                statsSettings.add(sessionTimeInUseRow);
                statsSettings.add(sessionStartTimeRow);
                statsSettings.add(resetSessionStatsRow);
                statsSettings.add(totalSelectionsRow);
                statsSettings.add(totalWordsSpokenRow);
                statsSettings.add(totalSessionCountRow);
                statsSettings.add(totalTimeInUseRow);
                statsSettings.add(lastUsedRow);
                statsSettings.add(resetAllStatsRow);
                statsSettings.add(recordingRow);
                Tab statsTab = buildTab("Statistics", statsSettings);

                voiceSettings.add(voiceRow);
                voiceSettings.add(volumeRow);
                Tab voiceTab = buildTab("Voice", voiceSettings);

                typingSettings.add(autocompleteRow);
                typingSettings.add(userDictWeightRow);
                Tab typingTab = buildTab("Typing", typingSettings);

                smsSettings.add(twilioIDRow);
                smsSettings.add(twilioTokenRow);
                smsSettings.add(twilioNumberRow);
                Tab smsTab = buildTab("SMS", smsSettings);

                selectionSettings.add(onFirstClickRow);
                selectionSettings.add(speakSelectionRow);
                selectionSettings.add(highlightBorderRow);
                selectionSettings.add(fullScreenRow);
                selectionSettings.add(playScourRow);
                selectionSettings.add(playSelectionRow);
                selectionSettings.add(scrollDistanceRow);
                Tab selectionTab = buildTab("Selection", selectionSettings);

                profileSettings.add(resetRow);
                profileSettings.add(exportRow);
                profileSettings.add(importRow);
                profileSettings.add(autoUpdateRow);
                Tab profileTab = buildTab("Profile", profileSettings);

                aboutSettings.add(versionRow);
                aboutSettings.add(emailRow);
                aboutSettings.add(disclaimerRow);
                Tab aboutTab = buildTab("About", aboutSettings);

                TabPane tabPane = new TabPane();
                tabPane.setPadding(new Insets(0, 0, 5, 5));
                tabPane.setSide(Side.LEFT);
                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                tabPane.getTabs().add(generalTab);
                tabPane.getTabs().add(deviceTab);
                tabPane.getTabs().add(voiceTab);
                tabPane.getTabs().add(typingTab);
                tabPane.getTabs().add(smsTab);
                tabPane.getTabs().add(selectionTab);
                tabPane.getTabs().add(statsTab);
                tabPane.getTabs().add(aboutTab);
                tabPane.getTabs().add(profileTab);

                return tabPane;
            }

            @Override

            public void setSettings() {

                HomeController.setUpdated(false);
                final User user = getUser();
                //DEVICE
                final String selectedDeviceName = getSession().getDeviceManager().getIterator().get();
                user.setDeviceName(deviceChoiceBox.getValue());
                final Interaction interaction = user.getInteraction();
                interaction.setMouseControl(mouseControlButton.getValue());
                interaction.setSelectionControl(selectionControlButton.getValue());
                Hardware hardware = getSession().getDeviceManager().get(selectedDeviceName).getHardware();
                final Selectability selectability = hardware.getSelectability();
                selectability.setSensitivity((int) (dwellSensitivitySlider.getValue()));
                selectability.getClickability().setEventType(new SelectionEventType(selectionEventChoiceBox.getValue()));
                getUser().getInteraction().setSelectionMethod(selectionChoiceBox.getValue()); // TO DO: REMOVE

                //APPLICATION
                user.getTiming().setDwellTime(dwellTimeSlider.getValue());
                user.getTiming().setScanTime(scanTimeSlider.getValue());
                user.getTiming().setScrollTime(scrollTimeSlider.getValue());
                user.getTiming().setStepTime(stepTimeSlider.getValue());
                user.getAbility().setLevel(levelSlider.getValue());
                user.getInteraction().setAssistedMode(assistedModeButton.getValue());

                TTSManager ttsPlayer = getSession().getTTSManager();
                ttsPlayer.getIterator().set(voiceChoices.getValue());
                getUser().getSpeech().setSpeakingVoice(voiceChoices.getValue());
                LOGGER.info("Voice set to " + getUser().getSpeech().getSpeakingVoice());

                getUser().getSocial().setTwilioAccountSID(accountSIDField.getText());
                getUser().getSocial().setTwilioAuthToken(authTokenField.getText());
                getUser().getSocial().setTwilioFromNumber(fromNumberField.getText());

                getUser().getNavigation().setOnFirstClick(onFirstClickButton.getValue());
                getUser().getNavigation().setSpeakSelection(speakSelectionButton.getValue());
                getUser().getNavigation().setHighlightBorder(highlightBorderButton.getValue());
                getUser().getNavigation().setFullScreen(fullScreenButton.getValue());
                getUser().getNavigation().setPlayScourSound(playScourButton.getValue());
                getUser().getNavigation().setScourSound(playScourField.getText());
                getUser().getNavigation().setPlaySelectSound(playSelectButton.getValue());
                getUser().getNavigation().setSelectionSound(playSelectionField.getText());
                getUser().getNavigation().setScrollDistance(scrollDistanceSlider.getValue());

                // TYPING
                getUser().getTyping().setAutoComplete(autocompleteButton.getValue());
                getUser().getTyping().setAutoCompleteTime(autocompleteSlider.getValue());
                getUser().getTyping().setDictionaryWeight((int) userDictWeightSlider.getValue());
                getUser().getContent().setHomeFolder(homeFolderField.getText());
                getUser().getInteraction().setVolume(volumeSlider.getValue() * 0.1);

                getUser().getProfile().setAutoUpdate(autoUpdateButton.getValue());

                HomeController.setUpdated(true);

            }

            @Override
            public void resetSettings() {

                //DEVICE
                final String activeDevice = getUser().getDeviceName();
                //    getSession().getDeviceManager().getIterator().set(activeDevice);
                deviceChoiceBox.setValue(activeDevice);

                Hardware hardware = getSelectedHardware();
                resetMouseControl(hardware);
                resetSelectionControl(hardware);
                //  final User user = getUser();
                //APPLICATION 
                dwellTimeSlider.setValue(getUser().getTiming().getDwellTime());
                scrollTimeSlider.setValue(getUser().getTiming().getScrollTime());
                scanTimeSlider.setValue(getUser().getTiming().getScanTime());
                stepTimeSlider.setValue(getUser().getTiming().getStepTime());
                selectionChoiceBox.setValue(getUser().getInteraction().getSelectionMethod());
                levelSlider.setValue(getUser().getAbility().getLevel());
                assistedModeButton.setValue(getUser().getInteraction().isInAssistedMode());

                voiceChoices.setValue(getUser().getSpeech().getSpeakingVoice());

                accountSIDField.setText(getUser().getSocial().getTwilioAccountSID());
                authTokenField.setText(getUser().getSocial().getTwilioAuthToken());
                fromNumberField.setText(getUser().getSocial().getTwilioFromNumber());

                onFirstClickButton.setValue(getUser().getNavigation().onFirstClick());
                speakSelectionButton.setValue(getUser().getNavigation().speakSelection());
                highlightBorderButton.disableProperty().bind(fullScreenButton.valueProperty());
                highlightBorderButton.setValue(getUser().getNavigation().highlightBorder());
                fullScreenButton.setValue(getUser().getNavigation().fullScreen());
                playScourButton.setValue(getUser().getNavigation().playScourSound());
                playScourField.setText(getUser().getNavigation().getScourSound());
                playSelectButton.setValue(getUser().getNavigation().playSelectSound());
                playSelectionField.setText(getUser().getNavigation().getSelectionSound());
                scrollDistanceSlider.setValue(getUser().getNavigation().getScrollDistance());

                autocompleteButton.setValue(getUser().getTyping().needsAutoComplete());
                autocompleteSlider.setValue(getUser().getTyping().getAutoCompleteTime());
                userDictWeightSlider.setValue(getUser().getTyping().getDictionaryWeight());

                homeFolderField.setText(getUser().getContent().getHomeFolder());
                volumeSlider.setValue(getUser().getInteraction().getVolume() * 10.0);

                autoUpdateButton.setValue(getUser().getProfile().autoUpdate());
                HomeController.setUpdated(false);
            }

            private String splitIntoHMS(Number newValue) {
                Integer totalSecs = newValue.intValue();
                Integer hours = totalSecs / 3600;
                Integer minutes = (totalSecs % 3600) / 60;
                Integer seconds = totalSecs % 60;
                String timeString;
                if (hours > 0) {
                    timeString = String.format("%d hours %d minutes %d seconds", hours, minutes, seconds);
                } else if (minutes > 0) {
                    timeString = String.format("%d minutes %d seconds", minutes, seconds);
                } else {
                    timeString = String.format("%d seconds", seconds);
                }
                return timeString;
            }

            /**
             *
             * @param hardware
             */
            public void resetMouseControl(Hardware hardware) {
                final Movability movability = hardware.getMovability();
                //btnMouseControl.setDisable(!movability.isEnabled());
                mouseControlButton.setValue(getUser().getInteraction().needsMouseControl());
            }

            /**
             *
             * @param hardware
             */
            public void resetSelectionControl(Hardware hardware) {
                final Selectability selectability = hardware.getSelectability();
                selectionControlButton.setDisable(!selectability.canSelect());
                selectionControlButton.setValue(getUser().getInteraction().needsSelectionControl());
                //  dwellSensitivityField.setVisible(selectability.getDwellability().isEnabled() && selectionMethod.equals(SelectionMethod.DWELL));
                dwellSensitivitySlider.setVisible(selectability.getDwellability().isEnabled());
                dwellSensitivitySlider.setValue(selectability.getSensitivity());
                // cbSelectionEvent.setVisible(selectability.getClickability().isEnabled() && (selectionMethod.equals(SelectionMethod.SWITCH) || selectionMethod.equals(SelectionMethod.CLICK)));
                selectionEventChoiceBox.setVisible(selectability.getClickability().isEnabled());
                selectionEventChoiceBox.setValue(selectability.getClickability().getEventType().getValue());

            }

            @Override
            public Hardware getSelectedHardware() {
                String deviceName = deviceChoiceBox.getValue();
                final DeviceManager deviceManager = getSession().getDeviceManager();
                Device device = deviceManager.get(deviceName);
                Hardware hardware = device.getHardware();
                return hardware;
            }

        };
        return editDialog;
    }

    @Override
    public void run() {
        LOGGER.info("Edit User Settings button clicked");

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
            preferences = Preferences.userNodeForPackage(this.getClass()).node(KEY.toLowerCase());
        }
        return preferences;
    }

}
