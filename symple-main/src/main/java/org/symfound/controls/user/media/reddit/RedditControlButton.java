/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.reddit;

import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
public class RedditControlButton extends MediaControlButton<RedditControl> {

    private static final String NAME = RedditControlButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Reddit Control";

    private final RedditViewer viewer;

    /**
     *
     * @param index
     * @param viewer
     */
    public RedditControlButton(String index, RedditViewer viewer) {
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
        RedditManager redditManager = viewer.getRedditManager();
        switch (getControl()) {
            case NEXT:
                if (!viewer.getHold().isAdded()) {
                    LOGGER.info("Loading next url");
                    redditManager.getIterator().next();
                }
                break;
            case PREVIOUS:
                if (!viewer.getHold().isAdded()) {
                    LOGGER.info("Loading previous url");
                    redditManager.getIterator().previous();
                }
                break;

        }
    }

    private TextField subredditField;
    private ChoiceBox<String> sublist;
    private ChoiceBox<RedditControl> controlTypeBox;

    /**
     *
     */
    public OnOffButton shuffleButton;

    /**
     *
     */
    @Override
    public void setAppableSettings() {
        viewer.setSubreddit(subredditField.getText());
        viewer.setSubList(sublist.getValue());
        setControl(controlTypeBox.getValue());
        viewer.setShuffle(shuffleButton.getValue());
        super.setAppableSettings();
    }

    /**
     *
     */
    @Override
    public void resetAppableSettings() {
        subredditField.setText(viewer.getSubreddit());
        sublist.setValue(viewer.getSubList());
        controlTypeBox.setValue(getControl());
        shuffleButton.setValue(viewer.toShuffle());
        super.resetAppableSettings();
    }

    /**
     *
     * @return
     */
    @Override
    public List<Tab> addAppableSettings() {
        SettingsRow settingsRowA = createSettingRow("Subreddit", "URL");

        Label label = new Label("www.reddit.com/r/");
        subredditField = new TextField();
        subredditField.setText(viewer.getSubreddit());
        subredditField.prefHeight(80.0);
        subredditField.prefWidth(360.0);
        subredditField.getStyleClass().add("settings-text-area");

        Label label2 = new Label("/");

        sublist = new ChoiceBox<>(FXCollections.observableArrayList(Arrays.asList("", "top", "new", "rising", "controversial")));

        sublist.setPrefWidth(360.0);
        sublist.setMaxHeight(subredditField.heightProperty().getValue());
        sublist.setValue(viewer.getSubList());
        sublist.getStyleClass().add("choice-box");

        HBox subredditHBox = new HBox();
        subredditHBox.setPrefHeight(Double.POSITIVE_INFINITY);
        subredditHBox.setPrefWidth(Double.POSITIVE_INFINITY);
        subredditHBox.getChildren().add(label);
        subredditHBox.getChildren().add(subredditField);
        subredditHBox.getChildren().add(label2);
        subredditHBox.getChildren().add(sublist);

        subredditHBox.setAlignment(Pos.CENTER_LEFT);
        settingsRowA.add(subredditHBox, 1, 0, 2, 1);

        SettingsRow settingsRowC = createSettingRow("Control Type", "Next or Previous or Toggle");
        controlTypeBox = new ChoiceBox<>(FXCollections.observableArrayList(
                Arrays.asList(
                        RedditControl.NEXT,
                        RedditControl.PREVIOUS
                )));
        controlTypeBox.setValue(getControl());
        controlTypeBox.prefHeight(80.0);
        controlTypeBox.prefWidth(360.0);
        controlTypeBox.getStyleClass().add("settings-text-area");
        settingsRowC.add(controlTypeBox, 1, 0, 2, 1);

        SettingsRow settingsRowD = EditDialog.createSettingRow("Shuffle", "Randomize the order");

        shuffleButton = new OnOffButton("YES", "NO");
        shuffleButton.setMaxSize(180.0, 60.0);
        shuffleButton.setValue(viewer.toShuffle());
        GridPane.setHalignment(shuffleButton, HPos.LEFT);
        GridPane.setValignment(shuffleButton, VPos.CENTER);
        settingsRowD.add(shuffleButton, 1, 0, 1, 1);

        // settings = new ArrayList<>();   
        settings.add(settingsRowA);
        settings.add(settingsRowC);
        settings.add(settingsRowD);
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
            Class<? extends RedditControlButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

    private static final RedditControl DEFAULT_REDDIT_CONTROL = RedditControl.NEXT;

    /**
     *
     * @return
     */
    @Override
    public RedditControl getDefaultControlValue() {
        return RedditControl.valueOf(getPreferences().get("control", DEFAULT_REDDIT_CONTROL.toString()));
    }

}
