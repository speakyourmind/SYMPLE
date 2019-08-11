/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import org.symfound.controls.system.SettingsRow;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;

/**
 *
 * @author Javed
 */
public class VolumeGridButton extends VolumeControl {

    /**
     *
     */
    public enum VolumeControls {

        /**
         *
         */
        UP,

        /**
         *
         */
        DOWN,

        /**
         *
         */
        MUTE
    }

    /**
     *
     */
    public static final String KEY = "Volume";

    /**
     *
     * @param index
     */
    public VolumeGridButton(String index) {
        super("music-volume-up", KEY, "Volume", index);
        initialize();
    }

    private void initialize() {
        setCSS("music-volume-" + getVolumeControl().toString().toLowerCase().replaceAll(" ", ""), getPrimaryControl());

        volumeControlProperty().addListener((observeableValue, oldValue, newValue) -> {
            setCSS("music-volume-" + newValue.toString().toLowerCase().replaceAll(" ", ""), getPrimaryControl());
        });
        volumeProperty().bindBidirectional(getUser().getInteraction().volumeProperty());

    }

    @Override
    public void run() {
        switch (getVolumeControl()) {
            case UP:
                setVolume(getVolume() + STEP_VOLUME);
                if (getVolume() > MAX_VOLUME) {
                    setVolume(MAX_VOLUME);
                }
                break;
            case DOWN:
                setVolume(getVolume() - STEP_VOLUME);
                if (getVolume() > MIN_VOLUME) {
                    setVolume(getVolume());
                } else {
                    setVolume(0.0);
                }
                break;

            case MUTE:
                if (getVolume() == 0.0) {
                    setVolume(MIN_VOLUME);
                } else {
                    setVolume(0.0);
                }
                break;
        }
        setVolume(getVolume());
        LOGGER.info("Volume set to " + getVolume());

    }

    private ChoiceBox<VolumeControls> volumeControlType;

    /**
     *
     */
    @Override
    public void setAppableSettings() {
        setVolumeControl(volumeControlType.getValue());
        super.setAppableSettings();
    }

    /**
     *
     */
    @Override
    public void resetAppableSettings() {
        volumeControlType.setValue(getVolumeControl());
        super.resetAppableSettings();
    }

    /**
     *
     * @return
     */
    @Override
    public List<Tab> addAppableSettings() {
        SettingsRow settingsRowA = createSettingRow("Control Type", "Up, Down or Mute");
        volumeControlType = new ChoiceBox<>(FXCollections.observableArrayList(Arrays.asList(
                VolumeControls.UP,
                VolumeControls.DOWN,
                VolumeControls.MUTE
        )));
        volumeControlType.setValue(getVolumeControl());
        volumeControlType.maxHeight(80.0);
        volumeControlType.maxWidth(360.0);
        volumeControlType.getStyleClass().add("settings-text-area");
        settingsRowA.add(volumeControlType, 1, 0, 2, 1);
        
        actionSettings.add(settingsRowA);
        List<Tab> tabs = super.addAppableSettings();

        return tabs;
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends VolumeGridButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

    private static final VolumeControls DEFAULT_MUSIC_CONTROL = VolumeControls.UP;
    private ObjectProperty<VolumeControls> volumeControl;

    /**
     *
     * @param value
     */
    public void setVolumeControl(VolumeControls value) {
        volumeControlProperty().setValue(value);
        getPreferences().put("volumeControl", value.toString());
    }

    /**
     *
     * @return
     */
    public VolumeControls getVolumeControl() {
        return volumeControlProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<VolumeControls> volumeControlProperty() {
        if (volumeControl == null) {
            volumeControl = new SimpleObjectProperty(VolumeControls.valueOf(getPreferences().get("volumeControl", DEFAULT_MUSIC_CONTROL.toString())));
        }
        return volumeControl;
    }
}
