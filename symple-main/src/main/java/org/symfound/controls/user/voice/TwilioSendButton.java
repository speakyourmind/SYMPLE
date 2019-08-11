/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.voice;

import java.util.List;
import java.util.prefs.Preferences;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;
import org.symfound.builder.user.characteristic.Social;
import org.symfound.controls.system.SettingsRow;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.social.sms.TwilioPoster;

/**
 *
 * @author Javed
 */
public class TwilioSendButton extends TextCommunicatorButton {

    private static final String NAME = TwilioSendButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Twilio Send";

    /**
     *
     */
    public TwilioSendButton() {
        super("speak-field-button", KEY, "", "default");
        initialize();
    }

    /**
     *
     * @param index
     */
    public TwilioSendButton(String index) {
        super("speak-field-button", KEY, "", index);
        initialize();
    }

    private void initialize() {
        bindToText();
        configureFont();
    }

    /**
     *
     */
    @Override
    public void bindToText() {
        setCommText(getUser().getTyping().getActiveText());
        commTextProperty().bindBidirectional(getUser().getTyping().activeTextProperty());
    }

    @Override
    public void run() {
        LOGGER.info("Sending text message from " + getFromNumber() + " to " + getToNumber() + ": " + getCommText());
        final Social social = getUser().getSocial();
        getTwilioPoster(social.getTwilioAccountSID(), social.getTwilioAuthToken()).textMessage(getToNumber(), social.getTwilioFromNumber(), getCommText());
    }

    /**
     *
     */
    public TextField fromNumberField;

    /**
     *
     */
    public TextField toNumberField;

    /**
     *
     */
    @Override
    public void setAppableSettings() {
        setFromNumber(fromNumberField.getText());
        setToNumber(toNumberField.getText());
        super.setAppableSettings();
    }

    /**
     *
     */
    @Override
    public void resetAppableSettings() {
        fromNumberField.setText(getFromNumber());
        toNumberField.setText(getToNumber());
        super.resetAppableSettings();
    }

    /**
     *
     * @return
     */
    @Override
    public List<Tab> addAppableSettings() {

        SettingsRow settingsFrom = createSettingRow("From", "What's your phone number?");

        fromNumberField = new TextField();
        fromNumberField.setText(getFromNumber());
        fromNumberField.prefHeight(80.0);
        fromNumberField.prefWidth(360.0);
        fromNumberField.getStyleClass().add("settings-text-area");
        settingsFrom.add(fromNumberField, 1, 0, 2, 1);

        SettingsRow settingsTo = createSettingRow("To", "Phone number of the person you are sending this to");

        toNumberField = new TextField();
        toNumberField.setText(getToNumber());
        toNumberField.prefHeight(80.0);
        toNumberField.prefWidth(360.0);
        toNumberField.getStyleClass().add("settings-text-area");
        settingsTo.add(toNumberField, 1, 0, 2, 1);

        actionSettings.add(settingsTo);
        actionSettings.add(settingsFrom);
        List<Tab> tabs = super.addAppableSettings();
        return tabs;
    }
    private static final String DEFAULT_TEXT_TO_KEY = "text.to";
    private StringProperty toNumber;

    /**
     *
     * @param value
     */
    public void setToNumber(String value) {
        toNumberProperty().set(value);
        getPreferences().put(DEFAULT_TEXT_TO_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getToNumber() {
        return toNumberProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty toNumberProperty() {
        if (toNumber == null) {
            toNumber = new SimpleStringProperty(getPreferences().get(DEFAULT_TEXT_TO_KEY, "+15555555555"));
        }
        return toNumber;
    }

    private static final String DEFAULT_TEXT_FROM_KEY = "text.from";
    private StringProperty fromNumber;

    /**
     *
     * @param value
     */
    public void setFromNumber(String value) {
        fromNumberProperty().set(value);
        getPreferences().put(DEFAULT_TEXT_FROM_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getFromNumber() {
        return fromNumberProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty fromNumberProperty() {
        if (fromNumber == null) {
            fromNumber = new SimpleStringProperty(getPreferences().get(DEFAULT_TEXT_FROM_KEY, "+15555555555"));
        }
        return fromNumber;
    }
    private TwilioPoster poster;

    public TwilioPoster getTwilioPoster(String accountSID, String authToken) {
        if (poster == null) {
            poster = new TwilioPoster(accountSID, authToken);
        }
        return poster;
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends TwilioSendButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }
}
