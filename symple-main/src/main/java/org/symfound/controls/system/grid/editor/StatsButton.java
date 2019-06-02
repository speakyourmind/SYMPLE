/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.system.grid.editor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
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
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;
import static org.symfound.builder.user.characteristic.Navigation.BUTTON_DELIMITER;
import static org.symfound.builder.user.characteristic.Navigation.KEY_DELIMITER;
import org.symfound.builder.user.selection.SelectionMethod;
import org.symfound.controls.RunnableControl;
import org.symfound.controls.ScreenControl;
import org.symfound.controls.SystemControl;
import org.symfound.controls.system.OnOffButton;
import org.symfound.controls.system.SettingsRow;
import org.symfound.controls.system.dialog.EditDialog;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.controls.system.dialog.OKCancelDialog;
import org.symfound.controls.user.AnimatedLabel;
import org.symfound.controls.user.BuildableGrid;
import org.symfound.controls.user.ButtonGrid;
import static org.symfound.controls.user.ExitButton.KEY;
import org.symfound.controls.user.FillableGrid;
import org.symfound.main.Main;
import org.symfound.main.settings.SettingsController;
import org.symfound.tools.iteration.ParallelList;

/**
 *
 * @author Javed Gangjee <javed@speakyourmindfoundation.org>
 */
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
            AnimatedLabel sessionTimeUsedLabel;
            AnimatedLabel sessionStartTimeLabel;

            AnimatedLabel totalSelectionsLabel;
            AnimatedLabel totalWordsSpokenLabel;
            AnimatedLabel totalSessionCountLabel;
            AnimatedLabel totalTimeUsedLabel;
            AnimatedLabel lastUsedLabel;

            @Override
            public Node addSettingControls() {
                SettingsRow sessionSelectionsRow = EditDialog.createSettingRow("Selections", "Button selections in this session");
                sessionSelectionsLabel = new AnimatedLabel();
                sessionSelectionsLabel.setStyle("-fx-font-size:3em;");
                sessionSelectionsLabel.textProperty().bind(getSession().getUser().getStatistics().sessionSelectionsProperty().asString());
                sessionSelectionsRow.add(sessionSelectionsLabel, 1, 0, 1, 1);

                SettingsRow sessionWordsSpokenRow = EditDialog.createSettingRow("Words Spoken", "Total of all words spoken");
                sessionWordsSpokenLabel = new AnimatedLabel();
                sessionWordsSpokenLabel.setStyle("-fx-font-size:3em;");
                sessionWordsSpokenLabel.textProperty().bind(getSession().getUser().getStatistics().sessionSpokenWordCountProperty().asString());
                sessionWordsSpokenRow.add(sessionWordsSpokenLabel, 1, 0, 1, 1);

                SettingsRow sessionStartTimeRow = EditDialog.createSettingRow("Start Time", "Time this session began");
                sessionStartTimeLabel = new AnimatedLabel();
                sessionStartTimeLabel.setStyle("-fx-font-size:3em;");
                Date date = new Date(getSession().getUser().getStatistics().getSessionStartTime());
                DateFormat formatter = new SimpleDateFormat("d MMM yyyy HH:mm:ss aaa");
                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                String startTime = formatter.format(date);
                sessionStartTimeLabel.setText(startTime);
                sessionStartTimeRow.add(sessionStartTimeLabel, 1, 0, 2, 1);

                SettingsRow totalSelectionsRow = EditDialog.createSettingRow("Total Selections", "Total of all button clicks");
                totalSelectionsLabel = new AnimatedLabel();
                totalSelectionsLabel.setStyle("-fx-font-size:3em;");
                totalSelectionsLabel.textProperty().bind(getSession().getUser().getStatistics().totalSelectionCountProperty().asString());
                totalSelectionsRow.add(totalSelectionsLabel, 1, 0, 1, 1);

                SettingsRow totalWordsSpokenRow = EditDialog.createSettingRow("Total Words Spoken", "Total of all words spoken");
                totalWordsSpokenLabel = new AnimatedLabel();
                totalWordsSpokenLabel.setStyle("-fx-font-size:3em;");
                totalWordsSpokenLabel.textProperty().bind(getSession().getUser().getStatistics().totalSpokenWordsCountProperty().asString());
                totalWordsSpokenRow.add(totalWordsSpokenLabel, 1, 0, 1, 1);

                SettingsRow totalSessionCountRow = EditDialog.createSettingRow("Total Sessions", "Number of times the program is launched");
                totalSessionCountLabel = new AnimatedLabel();
                totalSessionCountLabel.setStyle("-fx-font-size:3em;");
                totalSessionCountLabel.textProperty().bind(getSession().getUser().getStatistics().totalSessionCountProperty().asString());
                totalSessionCountRow.add(totalSessionCountLabel, 1, 0, 1, 1);
                
                
                SettingsRow lastUsedRow = EditDialog.createSettingRow("Previous Session", "Last time the program was run");
                lastUsedLabel = new AnimatedLabel();
                lastUsedLabel.setStyle("-fx-font-size:3em;");
                Date lastUsedDate = new Date(getSession().getUser().getStatistics().getLastUsed());
                DateFormat lastUsedFormatter = new SimpleDateFormat("d MMM yyyy HH:mm:ss aaa");
                lastUsedFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                String lastUsedTime = lastUsedFormatter.format(lastUsedDate);
                lastUsedLabel.setText(lastUsedTime);
                lastUsedRow.add(lastUsedLabel, 1, 0, 2, 1);

                // gridStats.add(fillMethodRow);
                // Tab gridTab = buildTab("GRID", gridStats);
                //sessionStats.add(overrideRowRow);
                sessionStats.add(sessionSelectionsRow);
                sessionStats.add(sessionWordsSpokenRow);
                sessionStats.add(sessionStartTimeRow);
                Tab sessionTab = buildTab("SESSION", sessionStats);

                globalStats.add(totalSelectionsRow);
                globalStats.add(totalWordsSpokenRow);
                globalStats.add(totalSessionCountRow);
                globalStats.add(lastUsedRow);

                Tab globalTab = buildTab("ALL", globalStats);

                TabPane tabPane = new TabPane();
                tabPane.setPadding(new Insets(5));
                tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                //  tabPane.getTabs().add(gridTab);
                tabPane.getTabs().add(sessionTab);
                tabPane.getTabs().add(globalTab);
                return tabPane;
            }

            @Override
            public void setSettings() {

                SettingsController.setUpdated(true);

            }

            @Override
            public void resetSettings() {

                SettingsController.setUpdated(false);
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
