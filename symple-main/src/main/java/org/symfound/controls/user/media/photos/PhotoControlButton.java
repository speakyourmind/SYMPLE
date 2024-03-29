/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.photos;

import java.io.File;
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
import javafx.stage.DirectoryChooser;
import static org.symfound.controls.AppableControl.LOGGER;
import org.symfound.controls.RunnableControl;
import org.symfound.controls.ScreenControl;
import static org.symfound.controls.device.DeviceSettings.MAX_HEIGHT;
import static org.symfound.controls.device.DeviceSettings.MAX_WIDTH;
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
public class PhotoControlButton extends MediaControlButton<PhotoControl> {

    /**
     *
     */
    public static final String KEY = "Photo Control";

    private final PhotoViewer viewer;

    /**
     *
     * @param index
     * @param viewer
     */
    public PhotoControlButton(String index, PhotoViewer viewer) {
        super("button", KEY, "Photo Control", index);
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
        PhotoManager photoManager = viewer.getPhotoManager();
        switch (getControl()) {
            case NEXT:
                if (!viewer.getHold().isAdded()) {
                    LOGGER.info("Loading next image");
                    photoManager.getIterator().next();
                }
                break;
            case PREVIOUS:
                if (!viewer.getHold().isAdded()) {
                    LOGGER.info("Loading previous image");
                    photoManager.getIterator().previous();
                }
                break;

        }
    }

    private TextField folderPathField;
    private ChoiceBox<PhotoControl> controlTypeBox;

    /**
     *
     */
    public OnOffButton shuffleButton;

    /**
     *
     */
    @Override
    public void setAppableSettings() {
        viewer.setFolderPath(folderPathField.getText());
        setControl(controlTypeBox.getValue());
        viewer.setShuffle(shuffleButton.getValue());
        super.setAppableSettings();
    }

    /**
     *
     */
    @Override
    public void resetAppableSettings() {
        folderPathField.setText(viewer.getFolderPath());
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
        SettingsRow settingsRowA = createSettingRow("Folder", "Location of the files");

        folderPathField = new TextField();
        folderPathField.setText(viewer.getFolderPath());
        folderPathField.maxHeight(80.0);
        folderPathField.maxWidth(360.0);
        folderPathField.setMaxSize(360.0, 80.0);
        folderPathField.getStyleClass().add("settings-text-area");
        settingsRowA.add(folderPathField, 2, 0);

        RunnableControl readFileChooser = new RunnableControl() {
            @Override
            public void run() {
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Select videos folder");
                File selectedFile = chooser.showDialog(getPrimaryControl().getParentUI());
                folderPathField.setText(selectedFile.getAbsolutePath());
            }
        };
        readFileChooser.setControlType(ScreenControl.ControlType.SETTING_CONTROL);
        readFileChooser.setSymStyle("settings-button");
        readFileChooser.setText("LOAD");
        readFileChooser.setMaxSize(MAX_WIDTH, MAX_HEIGHT);
        //  readFilePathGrid.add(readFileChooser, 0, 0);
        settingsRowA.add(readFileChooser, 1, 0);

        SettingsRow settingsRowB = createSettingRow("Control Type", "Next or Previous or Toggle");
        controlTypeBox = new ChoiceBox<>(FXCollections.observableArrayList(
                Arrays.asList(
                        PhotoControl.NEXT,
                        PhotoControl.PREVIOUS
                )));
        controlTypeBox.setValue(getControl());
        controlTypeBox.maxHeight(80.0);
        controlTypeBox.maxWidth(360.0);
        controlTypeBox.getStyleClass().add("settings-text-area");
        settingsRowB.add(controlTypeBox, 1, 0, 2, 1);

        SettingsRow settingsRowC = EditDialog.createSettingRow("Shuffle", "Randomize the order");

        shuffleButton = new OnOffButton("YES", "NO");
        shuffleButton.setMaxSize(180.0, 60.0);
        shuffleButton.setValue(viewer.toShuffle());
        GridPane.setHalignment(shuffleButton, HPos.LEFT);
        GridPane.setValignment(shuffleButton, VPos.CENTER);
        settingsRowC.add(shuffleButton, 1, 0, 1, 1);

        generalSettings.add(settingsRowA);
        generalSettings.add(settingsRowB);
        generalSettings.add(settingsRowC);
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
            EditDialog editDialog = new EditDialog("Edit Photo Control") {

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
            Class<? extends PhotoControlButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

    private static final PhotoControl DEFAULT_PHOTO_CONTROL = PhotoControl.NEXT;

    /**
     *
     * @return
     */
    @Override
    public PhotoControl getDefaultControlValue() {
        return PhotoControl.valueOf(getPreferences().get("control", DEFAULT_PHOTO_CONTROL.toString()));
    }
}
