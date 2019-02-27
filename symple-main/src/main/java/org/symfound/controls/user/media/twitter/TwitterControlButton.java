/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.twitter;

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
import org.apache.log4j.Logger;
import org.symfound.controls.system.EditAppButton;
import org.symfound.controls.system.OnOffButton;
import org.symfound.controls.system.SettingsRow;
import org.symfound.controls.system.dialog.EditDialog;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.controls.user.ScreenStatus;
import org.symfound.controls.user.media.MediaControlButton;
import org.symfound.social.twitter.TwitterReader;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public class TwitterControlButton extends MediaControlButton<TwitterControl> {

    private static final String NAME = TwitterControlButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Twitter Control";

    private final TwitterViewer viewer;

    /**
     *
     * @param index
     * @param viewer
     */
    public TwitterControlButton(String index, TwitterViewer viewer) {
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
        TwitterManager twitterManager = viewer.getTwitterManager();     
        viewer.getWebView().getEngine().load(null);
        switch (getControl()) {
            case NEXT:
                if (!viewer.getHold().isAdded()) {
                    LOGGER.info("Loading next tweet");
                    twitterManager.getIterator().next();
                }
                break;
            case PREVIOUS:
                if (!viewer.getHold().isAdded()) {
                    LOGGER.info("Loading previous tweet");
                    twitterManager.getIterator().previous();
                }
                break;

        }
        
    }

    private TextField pinField;
    private ChoiceBox<TwitterControl> controlTypeBox;

    /**
     *
     */
    public OnOffButton shuffleButton;

    /**
     *
     */
    @Override
    public void setAppableSettings() {
        final TwitterReader twitterReader = viewer.getTwitterManager().getTwitterReader();
        if (twitterReader.getConnector().getToken().isEmpty()
                || twitterReader.getConnector().getTokenSecret().isEmpty()) {

            viewer.getTwitterManager().getTwitterReader().getConnector().setPin(pinField.getText());
        }
        setControl(controlTypeBox.getValue());
        viewer.setShuffle(shuffleButton.getValue());
        super.setAppableSettings();
    }

    /**
     *
     */
    @Override
    public void resetAppableSettings() {
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
        final TwitterReader twitterReader = viewer.getTwitterManager().getTwitterReader();
        if (twitterReader.getConnector().getToken().isEmpty()
                || twitterReader.getConnector().getTokenSecret().isEmpty()) {
            SettingsRow settingsRowA = createSettingRow("Pin", "Enter Pin shown in browser");
            pinField = new TextField();
            pinField.setText(getNavigateIndex());
            pinField.maxHeight(80.0);
            pinField.maxWidth(60.0);
            pinField.getStyleClass().add("settings-text-area");
            settingsRowA.add(pinField, 1, 0, 1, 1);
            settings.add(settingsRowA);
        }

        SettingsRow settingsRowC = createSettingRow("Control Type", "Next or Previous or Toggle");
        controlTypeBox = new ChoiceBox<>(FXCollections.observableArrayList(
                Arrays.asList(
                        TwitterControl.NEXT,
                        TwitterControl.PREVIOUS
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
            Class<? extends TwitterControlButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

    private static final TwitterControl DEFAULT_TWITTER_CONTROL = TwitterControl.NEXT;

    /**
     *
     * @return
     */
    @Override
    public TwitterControl getDefaultControlValue() {
        return TwitterControl.valueOf(getPreferences().get("control", DEFAULT_TWITTER_CONTROL.toString()));
    }

}
