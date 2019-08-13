/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.symfound.builder.user.characteristic.Social;
import org.symfound.controls.system.OnOffButton;
import org.symfound.controls.system.SettingsRow;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import static org.symfound.controls.user.media.twilio.TwilioSendButton.LOGGER;
import org.symfound.device.emulation.input.keyboard.ActionKeyCode;
import org.symfound.main.builder.UI;
import org.symfound.social.sms.TwilioPoster;

/**
 *
 * @author Javed Gangjee
 */
public class ScriptButton extends TypingControl {

    /**
     *
     */
    public static final String KEY = "Script";
    public static final String DESCRIPTION = "Talk or Type Button";

    /**
     *
     */
    public ScriptButton() {
        super("type-button", KEY, "-1");
    }

    /**
     *
     * @param index
     */
    public ScriptButton(String index) {
        super("type-button", KEY, index);
        initialize();
    }

    private void initialize() {
        configureStyle();

        setActionKey(getKeyCodeConfig());
        keyCodeConfigProperty().addListener((obversable, oldValue, newValue) -> {
            setActionKey(getKeyCodeConfig());
        });

    }

    @Override
    public void run() {
        super.run();
        if (isTypable()) {
            type(getPrimaryControl().getText());
        }

        if (isSMSEnabled()) {
            sendTwilioText();
        }

        UI ui = (UI) getScene().getWindow();
        if (ui.inEditMode()) {
            LOGGER.info("Exiting edit mode before navigating");
            ui.setEditMode(Boolean.FALSE);
        }
    }

    public void sendTwilioText() {
        final Social social = getUser().getSocial();
        final String twilioAuthToken = social.getTwilioAuthToken();
        final String twilioAccountSID = social.getTwilioAccountSID();
        LOGGER.info("Sending text message from " + social.getTwilioFromNumber()
                + " to " + getToNumber() + ": " + getSpeakText());
        getTwilioPoster(twilioAccountSID, twilioAuthToken)
                .textMessage(getToNumber(), social.getTwilioFromNumber(), getSpeakText());
    }

    /**
     *
     * @param text
     */
    public void type(String text) {
        if (getTextArea() != null) {
            getTextArea().handle(getActionKey(), text);
        }
        if (getPictoArea() != null) {
            getPictoArea().add(this);
        }
    }

    private TextField toNumberField;
    private TextField keyCodeField;
    private OnOffButton typableButton;
    private OnOffButton smsEnabledButton;
    public List<SettingsRow> socialSettings = new ArrayList<>();

    public TextField webhookKeyField;
    public TextField webhookEventStartField;
    public TextField webhookEventEndField;
    public List<SettingsRow> environmentSettings = new ArrayList<>();

    /**
     *
     */
    @Override
    public void setAppableSettings() {
        setKeyCodeConfig(Integer.valueOf(keyCodeField.getText()));
        setTypable(typableButton.getValue());
        setSMSEnabled(smsEnabledButton.getValue());
        setToNumber(toNumberField.getText());

        setWebhookKey(webhookKeyField.getText());
        setWebhookEventStart(webhookEventStartField.getText());
        setWebhookEventEnd(webhookEventEndField.getText());
        super.setAppableSettings();
    }

    /**
     *
     */
    @Override
    public void resetAppableSettings() {
        keyCodeField.setText(getKeyCodeConfig().toString());
        typableButton.setValue(isTypable());
        smsEnabledButton.setValue(isSMSEnabled());
        toNumberField.setText(getToNumber());

        webhookKeyField.setText(getWebhookKey());
        webhookEventStartField.setText(getWebhookEventStart());
        webhookEventEndField.setText(getWebhookEventEnd());

        super.resetAppableSettings();
    }

    /**
     *
     * @return
     */
    @Override
    public List<Tab> addAppableSettings() {

        SettingsRow typableRow = createSettingRow("Typable", "Type the phrase on the button");
        typableButton = new OnOffButton("YES", "NO");
        typableButton.setMaxSize(180.0, 60.0);
        typableButton.setValue(isTypable());
        GridPane.setHalignment(typableButton, HPos.LEFT);
        GridPane.setValignment(typableButton, VPos.CENTER);

        typableRow.add(typableButton, 1, 0, 1, 1);

        keyCodeField = new TextField();
        keyCodeField.setText(getKeyCodeConfig().toString());
        keyCodeField.setMaxSize(180.0, 60.0);
        keyCodeField.getStyleClass().add("settings-text-area");
        typableRow.add(keyCodeField, 2, 0, 1, 1);

        SettingsRow settingsTo = createSettingRow("SMS", "Send button text to a number");
        smsEnabledButton = new OnOffButton("YES", "NO");
        smsEnabledButton.setMaxSize(180.0, 60.0);
        smsEnabledButton.setValue(isSMSEnabled());
        GridPane.setHalignment(smsEnabledButton, HPos.LEFT);
        GridPane.setValignment(smsEnabledButton, VPos.CENTER);
        settingsTo.add(smsEnabledButton, 1, 0, 1, 1);

        toNumberField = new TextField();
        toNumberField.setText(getToNumber());
        toNumberField.prefHeight(80.0);
        toNumberField.prefWidth(360.0);
        toNumberField.getStyleClass().add("settings-text-area");
        settingsTo.add(toNumberField, 2, 0, 1, 1);

        socialSettings = new ArrayList<>();
        socialSettings.add(settingsTo);
        Tab socialTab = buildTab("SOCIAL", socialSettings);

        SettingsRow webhookKeyRow = createSettingRow("Key", "User key configured in webhook");
        webhookKeyField = new TextField();
        webhookKeyField.setText(getWebhookKey());
        webhookKeyField.prefHeight(80.0);
        webhookKeyField.prefWidth(360.0);
        webhookKeyField.getStyleClass().add("settings-text-area");
        webhookKeyRow.add(webhookKeyField, 1, 0, 2, 1);

        SettingsRow webhookStartRow = createSettingRow("Start Event", "Activity event name in webhook");

        webhookEventStartField = new TextField();
        webhookEventStartField.setText(getWebhookEventStart());
        webhookEventStartField.prefHeight(80.0);
        webhookEventStartField.prefWidth(360.0);
        webhookEventStartField.getStyleClass().add("settings-text-area");
        webhookStartRow.add(webhookEventStartField, 1, 0, 2, 1);

        SettingsRow webhookEndRow = createSettingRow("End Event", "Activity event name in webhook");

        webhookEventEndField = new TextField();
        webhookEventEndField.setText(getWebhookEventEnd());
        webhookEventEndField.prefHeight(80.0);
        webhookEventEndField.prefWidth(360.0);
        webhookEventEndField.getStyleClass().add("settings-text-area");
        webhookEndRow.add(webhookEventEndField, 1, 0, 2, 1);

        environmentSettings = new ArrayList<>();
        environmentSettings.add(webhookKeyRow);
        environmentSettings.add(webhookStartRow);
        environmentSettings.add(webhookEndRow);
        Tab environmentTab = buildTab("HOME", environmentSettings);

        actionSettings.add(typableRow);
        List<Tab> tabs = super.addAppableSettings();

        tabs.add(socialTab);
        tabs.add(environmentTab);
        return tabs;
    }

    /**
     *
     */
    public static final String DEFAULT_KEY_CODE_CONFIG = String.valueOf(ActionKeyCode.UNASSIGNED);

    /**
     *
     */
    public IntegerProperty keyCodeConfig;

    /**
     *
     * @param value
     */
    public void setKeyCodeConfig(Integer value) {
        keyCodeConfigProperty().setValue(value);
        getPreferences().put("keyCodeConfig", value.toString());

    }

    /**
     *
     * @return
     */
    public Integer getKeyCodeConfig() {
        return keyCodeConfigProperty().getValue();
    }

    /**
     *
     * @return
     */
    public IntegerProperty keyCodeConfigProperty() {
        if (keyCodeConfig == null) {
            keyCodeConfig = new SimpleIntegerProperty(Integer.valueOf(getPreferences().get("keyCodeConfig", DEFAULT_KEY_CODE_CONFIG)));
        }
        return keyCodeConfig;
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends ScriptButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

    private BooleanProperty typable;

    /**
     *
     * @param value
     */
    public void setTypable(Boolean value) {
        typableProperty().set(value);
        getPreferences().put("typable", value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean isTypable() {
        return typableProperty().get();
    }

    /**
     *
     * @return
     */
    public BooleanProperty typableProperty() {
        if (typable == null) {
            typable = new SimpleBooleanProperty(Boolean.valueOf(getPreferences().get("typable", "false")));
        }
        return typable;
    }

    private BooleanProperty smsEnabled;

    /**
     *
     * @param value
     */
    public void setSMSEnabled(Boolean value) {
        smsEnabledProperty().set(value);
        getPreferences().put("smsEnabled", value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean isSMSEnabled() {
        return smsEnabledProperty().get();
    }

    /**
     *
     * @return
     */
    public BooleanProperty smsEnabledProperty() {
        if (smsEnabled == null) {
            smsEnabled = new SimpleBooleanProperty(Boolean.valueOf(getPreferences().get("smsEnabled", "false")));
        }
        return smsEnabled;
    }

    private TwilioPoster poster;

    public TwilioPoster getTwilioPoster(String accountSID, String authToken) {
        if (poster == null) {
            poster = new TwilioPoster(accountSID, authToken);
        }
        return poster;
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

    private StringProperty webhookKey;

    /**
     *
     * @param value
     */
    public void setWebhookKey(String value) {
        webhookKeyProperty().set(value);
        getPreferences().put("webhook.key", value);
    }

    /**
     *
     * @return
     */
    public String getWebhookKey() {
        return webhookKeyProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty webhookKeyProperty() {
        if (webhookKey == null) {
            webhookKey = new SimpleStringProperty(getPreferences().get("webhook.key", "dOqKgMizDNXLj8dKIEy4oH"));
        }
        return webhookKey;
    }

    private StringProperty webhookEventStart;

    /**
     *
     * @param value
     */
    public void setWebhookEventStart(String value) {
        webhookEventStartProperty().set(value);
        getPreferences().put("webhook.event.start", value);
    }

    /**
     *
     * @return
     */
    public String getWebhookEventStart() {
        return webhookEventStartProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty webhookEventStartProperty() {
        if (webhookEventStart == null) {
            webhookEventStart = new SimpleStringProperty(getPreferences().get("webhook.event.start", "test"));
        }
        return webhookEventStart;
    }

    private StringProperty webhookEventEnd;

    /**
     *
     * @param value
     */
    public void setWebhookEventEnd(String value) {
        webhookEventEndProperty().set(value);
        getPreferences().put("webhook.event.end", value);
    }

    /**
     *
     * @return
     */
    public String getWebhookEventEnd() {
        return webhookEventEndProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty webhookEventEndProperty() {
        if (webhookEventEnd == null) {
            webhookEventEnd = new SimpleStringProperty(getPreferences().get("webhook.event.end", "test"));
        }
        return webhookEventEnd;
    }

}
