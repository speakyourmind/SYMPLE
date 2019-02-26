/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.calendar;

import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.apache.log4j.Logger;
import org.symfound.controls.system.EditAppButton;
import org.symfound.controls.system.OnOffButton;
import org.symfound.controls.system.SettingsRow;
import org.symfound.controls.system.dialog.EditDialog;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.controls.user.ScreenStatus;
import org.symfound.controls.user.media.MediaControlButton;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public class CalendarControlButton extends MediaControlButton<CalendarControl> {

    private static final String NAME = CalendarControlButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Calendar Control";

    private CalendarViewer viewer;

    /**
     *
     * @param index
     * @param viewer
     */
    public CalendarControlButton(String index, CalendarViewer viewer) {
        super("button", KEY, KEY, index);
        this.viewer = viewer;
        initialize();
    }

    private void initialize() {
        getPrimaryControl().setDisable(!viewer.getStatus().equals(ScreenStatus.PLAYING));
        viewer.statusProperty().addListener((observable1, oldValue1, newValue1) -> {
            getPrimaryControl().setDisable(!newValue1.equals(ScreenStatus.PLAYING));
        });
    }

    @Override
    public void run() {
        CalendarManager CalendarManager = viewer.getCalendarManager();
        switch (getControl()) {
            case NEXT:
                if (!viewer.getHold().isAdded()) {
                    LOGGER.info("Loading next url");
              //      CalendarManager.getIterator().next();
                }
                break;
            case PREVIOUS:
                if (!viewer.getHold().isAdded()) {
                    LOGGER.info("Loading previous url");
                //    CalendarManager.getIterator().previous();
                }
                break;

        }
    }

    private ChoiceBox<CalendarControl> controlTypeBox;

    /**
     *
     */
    public OnOffButton shuffleButton;

    /**
     *
     */
    @Override
    public void setAppableSettings() {
        setControl(controlTypeBox.getValue());
        super.setAppableSettings();
    }

    /**
     *
     */
    @Override
    public void resetAppableSettings() {

        controlTypeBox.setValue(getControl());
        super.resetAppableSettings();
    }

    /**
     *
     * @return
     */
    @Override
    public List<Tab> addAppableSettings() {

        SettingsRow typeRow = createSettingRow("Control Type", "Next or Previous or Toggle");
        controlTypeBox = new ChoiceBox<>(FXCollections.observableArrayList(
                Arrays.asList(
                        CalendarControl.NEXT,
                        CalendarControl.PREVIOUS
                )));
        controlTypeBox.setValue(getControl());
        controlTypeBox.prefHeight(80.0);
        controlTypeBox.prefWidth(360.0);
        controlTypeBox.getStyleClass().add("settings-text-area");
        typeRow.add(controlTypeBox, 1, 0, 2, 1);

        settings.add(typeRow);
        List<Tab> tabs = super.addAppableSettings();

        return tabs;
    }

    /**
     *
     * @return
     */
    @Override
    public EditAppButton getEditAppButton() {
        if (editAppButton == null) {
            EditDialog editDialog = new EditDialog() {

                @Override
                public void onOk() {
                    super.onOk();
                    viewer.reload();
                }

                @Override
                public void setSettings() {
                    setAppableSettings();
                }

                @Override
                public void resetSettings() {
                    resetAppableSettings();
                }

                @Override
                public Node addSettingControls() {
                    TabPane tabPane = new TabPane();
                    tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
                    tabPane.getTabs().addAll(addAppableSettings());
                    return tabPane;
                }

            };

            editAppButton = new EditAppButton(editDialog);
            editAppButton.setPane("apMain");
        }
        return editAppButton;
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends CalendarControlButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

    private static final CalendarControl DEFAULT_REDDIT_CONTROL = CalendarControl.NEXT;

    /**
     *
     * @return
     */
    @Override
    public CalendarControl getDefaultControlValue() {
        return CalendarControl.valueOf(getPreferences().get("control", DEFAULT_REDDIT_CONTROL.toString()));
    }

}
