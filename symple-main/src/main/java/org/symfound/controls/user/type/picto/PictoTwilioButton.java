/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.type.picto;

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
public class PictoTwilioButton extends TextCommunicatorButton {

    private static final String NAME = PictoTwilioButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Picto Twilio";

    /**
     *
     */
    public PictoTwilioButton() {
        super("speak-field-button", KEY, "", "default");
        initialize();
    }

    /**
     *
     * @param index
     */
    public PictoTwilioButton(String index) {
        super("speak-field-button", KEY, "Send SMS", index);
        initialize();
    }

    private void initialize() {
        bindToText();
        configureFont();
        configureStyle();
    }

    /**
     *
     */
    @Override
    public void bindToText() {
  //      setCommText(getUser().getTyping().getActiveText());
    //    commTextProperty().bindBidirectional(getUser().getTyping().activeTextProperty());
   //     setCommText(getPictoArea().getPictoText());
     //   commTextProperty().bindBidirectional(getPictoArea().pictoTextProperty());
    }

    @Override
    public void run() {
                getPictoArea().updatePictoText();
        final Social social = getUser().getSocial();
        LOGGER.info("Sending text message from " + social.getTwilioFromNumber() + " to " + getToNumber() + ": " + getPictoArea().getPictoText());
        getTwilioPoster(social.getTwilioAccountSID(), social.getTwilioAuthToken()).textMessage(getToNumber(), social.getTwilioFromNumber(), getPictoArea().getPictoText());
    }

    /**
     *
     */
    public TextField toNumberField;

    /**
     *
     */
    @Override
    public void setAppableSettings() {
        setToNumber(toNumberField.getText());
        super.setAppableSettings();
    }

    /**
     *
     */
    @Override
    public void resetAppableSettings() {
        toNumberField.setText(getToNumber());
        super.resetAppableSettings();
    }

    /**
     *
     * @return
     */
    @Override
    public List<Tab> addAppableSettings() {

        SettingsRow settingsTo = createSettingRow("To", "Phone number of the person you are sending this to");

        toNumberField = new TextField();
        toNumberField.setText(getToNumber());
        toNumberField.prefHeight(80.0);
        toNumberField.prefWidth(360.0);
        toNumberField.getStyleClass().add("settings-text-area");
        settingsTo.add(toNumberField, 1, 0, 2, 1);

        actionSettings.add(settingsTo);
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
            Class<? extends PictoTwilioButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }
}
