/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.prefs.Preferences;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;
import org.symfound.builder.user.characteristic.Statistics;
import org.symfound.controls.RunnableControl;
import org.symfound.controls.ScreenControl;
import org.symfound.controls.SystemControl;
import org.symfound.controls.system.OnOffButton;
import org.symfound.controls.system.SettingsRow;
import org.symfound.controls.system.dialog.EditDialog;
import org.symfound.controls.system.dialog.OKCancelDialog;
import org.symfound.main.HomeController;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
@Deprecated
public class StatsButton extends SystemControl {

    /**
     *
     */
    public static final String NAME = StatsButton.class.getName();

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
    public static final String KEY = "Stats";

    public StatsButton() {
        super("toolbar-stats", KEY, "", "default");
    }

    @Override
    public void defineButton() {
        setEditable(Boolean.FALSE);
        setControlType(ScreenControl.ControlType.SETTING_CONTROL);
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
    public List<SettingsRow> gridStats = new ArrayList<>();

    /**
     *
     */
    public List<SettingsRow> sessionStats = new ArrayList<>();

    /**
     *
     */
    public List<SettingsRow> globalStats = new ArrayList<>();

    /**
     *
     * @return
     */
    public EditDialog configureEditDialog() {
        EditDialog editDialog = new EditDialog("Usage Stats") {

            AnimatedLabel sessionSelectionsLabel;
            AnimatedLabel sessionWordsSpokenLabel;
            AnimatedLabel sessionTimeInUseLabel;
            AnimatedLabel sessionStartTimeLabel;
            RunnableControl resetSessionUsageButton;

            AnimatedLabel totalSelectionsLabel;
            AnimatedLabel totalWordsSpokenLabel;
            AnimatedLabel totalSessionCountLabel;
            AnimatedLabel totalTimeInUseLabel;
            AnimatedLabel firstUsedLabel;
            AnimatedLabel lastUsedLabel;

            RunnableControl resetUsageButton;
            OnOffButton recordButton;

            @Override
            public Node addSettingControls() {
                final Statistics statistics = getSession().getUser().getStatistics();
                SettingsRow sessionSelectionsRow = EditDialog.createSettingRow("Selections", "Button selections in this session");
                sessionSelectionsLabel = new AnimatedLabel();
                sessionSelectionsLabel.setStyle("-fx-font-size:3em;");
                sessionSelectionsLabel.textProperty().bind(statistics.sessionSelectionsProperty().asString());
                sessionSelectionsRow.add(sessionSelectionsLabel, 1, 0, 1, 1);

                SettingsRow sessionWordsSpokenRow = EditDialog.createSettingRow("Words Spoken", "Number of words spoken in this session");
                sessionWordsSpokenLabel = new AnimatedLabel();
                sessionWordsSpokenLabel.setStyle("-fx-font-size:3em;");
                sessionWordsSpokenLabel.textProperty().bind(statistics.sessionSpokenWordCountProperty().asString());
                sessionWordsSpokenRow.add(sessionWordsSpokenLabel, 1, 0, 1, 1);

                SettingsRow sessionTimeInUseRow = EditDialog.createSettingRow("Time In Use", "Session time in use");
                sessionTimeInUseLabel = new AnimatedLabel();
                sessionTimeInUseLabel.setStyle("-fx-font-size:3em;");
                statistics.sessionTimeInUseProperty().addListener((observable, oldValue, newValue) -> {

                    String timeString = splitIntoHMS(newValue);
                    sessionTimeInUseLabel.setText(timeString);
                });
                //  sessionTimeInUseLabel.textProperty().bind(Bindings.concat(getSession().getUser().getStatistics().sessionTimeInUseProperty().asString(), " seconds"));
                sessionTimeInUseRow.add(sessionTimeInUseLabel, 1, 0, 2, 1);

                SettingsRow sessionStartTimeRow = EditDialog.createSettingRow("Start Time", "Time this session began");
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

                sessionStats.add(sessionSelectionsRow);
                sessionStats.add(sessionWordsSpokenRow);
                sessionStats.add(sessionTimeInUseRow);
                sessionStats.add(sessionStartTimeRow);
                sessionStats.add(resetSessionStatsRow);
                Tab sessionTab = buildTab("SESSION", sessionStats);

                globalStats.add(totalSelectionsRow);
                globalStats.add(totalWordsSpokenRow);
                globalStats.add(totalSessionCountRow);
                globalStats.add(totalTimeInUseRow);
                globalStats.add(lastUsedRow);
                globalStats.add(resetAllStatsRow);
                globalStats.add(recordingRow);
                Tab globalTab = buildTab("ALL", globalStats);

                TabPane tabPane = new TabPane();
                tabPane.setPadding(new Insets(5));
                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                tabPane.getTabs().add(sessionTab);
                tabPane.getTabs().add(globalTab);
                return tabPane;
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

            @Override
            public void setSettings() {

                HomeController.setUpdated(true);

            }

            @Override
            public void resetSettings() {

                HomeController.setUpdated(false);
            }
        };
        return editDialog;
    }

    @Override
    public void run() {
        LOGGER.info("Stats button clicked");
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(this.getClass()).node(KEY.toLowerCase());
        }
        return preferences;
    }
}
