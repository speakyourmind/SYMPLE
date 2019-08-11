/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.youtube;

import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import static org.symfound.controls.AppableControl.LOGGER;
import org.symfound.controls.system.EditAppButton;
import org.symfound.controls.system.OnOffButton;
import org.symfound.controls.system.SettingsRow;
import org.symfound.controls.system.dialog.EditDialog;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.controls.user.ScreenStatus;
import org.symfound.controls.user.media.MediaControlButton;
import static org.symfound.controls.user.media.youtube.YouTubeControl.NEXT;
import static org.symfound.controls.user.media.youtube.YouTubeControl.PREVIOUS;
import static org.symfound.controls.user.media.youtube.YouTubeControl.STOP;

/**
 *
 * @author Javed Gangjee <jgangjee@youtube.com>
 */
public class YouTubeControlButton extends MediaControlButton<YouTubeControl> {

    /**
     *
     */
    public static final String KEY = "YouTube Control";

    private final YouTubeViewer viewer;

    /**
     *
     * @param index
     * @param viewer
     */
    public YouTubeControlButton(String index, YouTubeViewer viewer) {
        super("button", KEY, "YouTube Control", index);
        this.viewer = viewer;
        initialize();
    }

    private void initialize() {
        getPrimaryControl().setDisable(!viewer.getStatus().equals(ScreenStatus.PLAYING));
        viewer.statusProperty().addListener((observable1, oldValue1, newValue1) -> {
            getPrimaryControl().setDisable(!newValue1.equals(ScreenStatus.PLAYING));
        }
        );
    }

    @Override
    public void run() {
        switch (getControl()) {
            case NEXT:
                if (!viewer.getHold().isAdded()) {
                    LOGGER.info("Loading next message");
                    viewer.getYouTubeManager().getIterator().next();
                }
                break;

            case PREVIOUS:
                if (!viewer.getHold().isAdded()) {
                    LOGGER.info("Loading previous message");
                    viewer.getYouTubeManager().getIterator().previous();
                }
            case STOP:
                viewer.getWebView().getEngine().load(null);
                break;

        }
    }

    TextField playlistIDField;
    private ChoiceBox<YouTubeControl> controlTypeBox;

    /**
     *
     */
    public OnOffButton shuffleButton;

    /**
     *
     */
    @Override
    public void setAppableSettings() {
        viewer.setPlaylistId(playlistIDField.getText());
        setControl(controlTypeBox.getValue());
        viewer.setShuffle(shuffleButton.getValue());
        super.setAppableSettings();
    }

    /**
     *
     */
    @Override
    public void resetAppableSettings() {
        playlistIDField.setText(viewer.getPlaylistId());
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
        SettingsRow settingsRowA = createSettingRow("PlaylistID", "Use the tail end of playlist URL");
        playlistIDField = new TextField();
        playlistIDField.setText(viewer.getPlaylistId());
        playlistIDField.maxHeight(80.0);
        playlistIDField.maxWidth(360.0);
        playlistIDField.getStyleClass().add("settings-text-area");
        settingsRowA.add(playlistIDField, 1, 0, 2, 1);

        SettingsRow settingsRowB = createSettingRow("Control Type", "Next or Previous or Toggle");
        controlTypeBox = new ChoiceBox<>(FXCollections.observableArrayList(
                Arrays.asList(
                        YouTubeControl.NEXT,
                        YouTubeControl.PREVIOUS,
                        YouTubeControl.STOP
                )));
        controlTypeBox.setValue(getControl());
        controlTypeBox.maxHeight(80.0);
        controlTypeBox.maxWidth(360.0);
        controlTypeBox.getStyleClass().add("settings-text-area");
        settingsRowB.add(controlTypeBox, 1, 0, 2, 1);

        SettingsRow settingsRowD = EditDialog.createSettingRow("Shuffle", "Randomize the order");

        shuffleButton = new OnOffButton("YES", "NO");
        shuffleButton.setMaxSize(180.0, 60.0);
        shuffleButton.setValue(viewer.toShuffle());
        GridPane.setHalignment(shuffleButton, HPos.LEFT);
        GridPane.setValignment(shuffleButton, VPos.CENTER);
        settingsRowD.add(shuffleButton, 1, 0, 1, 1);
        
        actionSettings.add(settingsRowA);
        actionSettings.add(settingsRowB);
        actionSettings.add(settingsRowD);
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
            EditDialog editDialog = new EditDialog("Edit YouTube Control") {

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
            Class<? extends YouTubeControlButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

    private static final YouTubeControl DEFAULT_YOUTUBE_CONTROL = YouTubeControl.NEXT;

    /**
     *
     * @return
     */
    @Override
    public YouTubeControl getDefaultControlValue() {
        return YouTubeControl.valueOf(getPreferences().get("control", DEFAULT_YOUTUBE_CONTROL.toString()));
    }

}
