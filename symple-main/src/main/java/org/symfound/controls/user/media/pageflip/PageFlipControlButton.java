/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.pageflip;

import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
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
public class PageFlipControlButton extends MediaControlButton<PageFlipControl> {

    private static final String NAME = PageFlipControlButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "PageFlip Control";

    private final PageFlipViewer viewer;

    /**
     *
     * @param index
     * @param viewer
     */
    public PageFlipControlButton(String index, PageFlipViewer viewer) {
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
        PageFlipManager pageflipManager = viewer.getPageFlipManager();
        switch (getControl()) {
            case NEXT:
                if (!viewer.getHold().isAdded()) {
                    LOGGER.info("Loading next url");
                    pageflipManager.getIterator().next();
                }
                break;
            case PREVIOUS:
                if (!viewer.getHold().isAdded()) {
                    LOGGER.info("Loading previous url");
                    pageflipManager.getIterator().previous();
                }
                break;

        }
    }

    private TextArea sitesField;
    private ChoiceBox<PageFlipControl> controlTypeBox;

    /**
     *
     */
    public OnOffButton shuffleButton;

    /**
     *
     */
    @Override
    public void setAppableSettings() {
        viewer.getPageFlipManager().setURLs(Arrays.asList(sitesField.getText().split(",")));
        setControl(controlTypeBox.getValue());
        viewer.setShuffle(shuffleButton.getValue());
        super.setAppableSettings();
    }

    /**
     *
     */
    @Override
    public void resetAppableSettings() {
        sitesField.setText(viewer.getPageFlipManager().getURLs().toString()
                .replaceAll("[\\[\\]]", "").replaceAll(" ",""));
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

        SettingsRow sitesRow = EditDialog.createSettingRow("Sites", "Sites to be displayed on this app");

        sitesField = new TextArea();
        sitesField.setText(viewer.getPageFlipManager().getURLs().toString()
                .replaceAll("[\\[\\]]", "").replaceAll(" ",""));
        GridPane.setMargin(sitesField, new Insets(10.0));
        sitesField.prefHeight(80.0);
        sitesField.prefWidth(360.0);
        sitesField.getStyleClass().add("settings-text-area");
        sitesRow.add(sitesField, 1, 0, 2, 1);

        SettingsRow typeRow = createSettingRow("Control Type", "Next or Previous or Toggle");
        controlTypeBox = new ChoiceBox<>(FXCollections.observableArrayList(
                Arrays.asList(
                        PageFlipControl.NEXT,
                        PageFlipControl.PREVIOUS
                )));
        controlTypeBox.setValue(getControl());
        controlTypeBox.prefHeight(80.0);
        controlTypeBox.prefWidth(360.0);
        controlTypeBox.getStyleClass().add("settings-text-area");
        typeRow.add(controlTypeBox, 1, 0, 2, 1);

        SettingsRow shuffleRow = EditDialog.createSettingRow("Shuffle", "Randomize the order");

        shuffleButton = new OnOffButton("YES", "NO");
        shuffleButton.setMaxSize(180.0, 60.0);
        shuffleButton.setValue(viewer.toShuffle());
        GridPane.setHalignment(shuffleButton, HPos.LEFT);
        GridPane.setValignment(shuffleButton, VPos.CENTER);
        shuffleRow.add(shuffleButton, 1, 0, 1, 1);

        generalSettings.add(sitesRow);
        generalSettings.add(typeRow);
        generalSettings.add(shuffleRow);
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
            EditDialog editDialog = new EditDialog("Edit Page Flip Button") {

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
            Class<? extends PageFlipControlButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

    private static final PageFlipControl DEFAULT_REDDIT_CONTROL = PageFlipControl.NEXT;

    /**
     *
     * @return
     */
    @Override
    public PageFlipControl getDefaultControlValue() {
        return PageFlipControl.valueOf(getPreferences().get("control", DEFAULT_REDDIT_CONTROL.toString()));
    }

}
