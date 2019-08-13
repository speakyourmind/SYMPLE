/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.Twilio;

import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import org.symfound.controls.AppableControl;
import org.symfound.controls.system.SettingsRow;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.tools.iteration.ModeIterator;

/**
 *
 * @author Javed Gangjee <jgangjee@Twilio.com>
 */
public class TwilioControlButton extends AppableControl {

    /**
     *
     */
    public static final String KEY = "Twilio Control";

    /**
     *
     * @param index
     */
    public TwilioControlButton(String index) {
        super("button", KEY, "Twilio Control", index);
        initialize();
    }

    private void initialize() {
        style();
        overrideStyleProperty().addListener((obversable, oldValue, newValue) -> {
            style();
        });
        controlProperty().addListener((observeableValue, oldValue, newValue) -> {
            style();
        });
    }

    /**
     *
     */
    public void style() {
        if (!getOverrideStyle().isEmpty()) {
            getPrimaryControl().setStyle(getOverrideStyle());
        } else {
            getPrimaryControl().setStyle("");
            setCSS("media-" + getControl().toString().toLowerCase(), getPrimaryControl());
        }
    }

    @Override
    public void run() {
        final ModeIterator<String> iterator = TwilioViewer.getTwilioManager().getIterator();
        switch (getControl()) {
            case NEXT:
                LOGGER.info("Loading next message");
                iterator.next();
                break;
            case PREVIOUS:
                LOGGER.info("Loading previous message");
                iterator.previous();
                break;

        }
        String message = iterator.get(); 
        LOGGER.info("Current message length:" + message.length() + " with text:" + message);
        String cleanedMessage = message.trim().replaceAll(" ", "").replaceAll("\n","").replaceAll("\r","");
        LOGGER.info("Current message cleaned up :" + cleanedMessage.length() + " with text:" + message);
        if (cleanedMessage.isEmpty())  {
            run();
        }
    }

    private ChoiceBox<TwilioControl> controlType;

    /**
     *
     */
    @Override
    public void setAppableSettings() {
        setControl(controlType.getValue());
        super.setAppableSettings();
    }

    /**
     *
     */
    @Override
    public void resetAppableSettings() {
        controlType.setValue(getControl());
        super.resetAppableSettings();
    }

    /**
     *
     * @return
     */
    @Override
    public List<Tab> addAppableSettings() {
        SettingsRow settingsRowA = createSettingRow("Control Type", "Next or Previous or Toggle");
        controlType = new ChoiceBox<>(FXCollections.observableArrayList(
                Arrays.asList(
                        TwilioControl.NEXT,
                        TwilioControl.PREVIOUS
                )));
        controlType.setValue(getControl());
        controlType.maxHeight(80.0);
        controlType.maxWidth(360.0);
        controlType.getStyleClass().add("settings-text-area");
        settingsRowA.add(controlType, 1, 0, 2, 1);

        actionSettings.add(settingsRowA);
        List<Tab> tabs = super.addAppableSettings();

        return tabs;
    }

    private static final TwilioControl DEFAULT_CONTROL = TwilioControl.NEXT;
    private ObjectProperty<TwilioControl> twilioControl;

    /**
     *
     * @param value
     */
    public void setControl(TwilioControl value) {
        controlProperty().setValue(value);
        getPreferences().put("TwilioControl", value.toString());
    }

    /**
     *
     * @return
     */
    public TwilioControl getControl() {
        return controlProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<TwilioControl> controlProperty() {
        if (twilioControl == null) {
            twilioControl = new SimpleObjectProperty(TwilioControl.valueOf(getPreferences().get("TwilioControl", DEFAULT_CONTROL.toString())));
        }
        return twilioControl;
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends TwilioControlButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

}
