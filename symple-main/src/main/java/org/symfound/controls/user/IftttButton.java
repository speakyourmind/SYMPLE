package org.symfound.controls.user;

import java.io.IOException;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;
import org.symfound.comm.web.Webhook;
import org.symfound.controls.AppableControl;
import org.symfound.controls.system.SettingsRow;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.main.Main;

/**
 *
 * @author Javed Gangjee
 */
public final class IftttButton extends AppableControl {

    /**
     *
     */
    public static final String NAME = IftttButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "IFTTT";

    /**
     *
     * @param index
     */
    public IftttButton(String index) {
        super("word-1", KEY, index, index);
        initialize();
    }

    private void initialize() {
        configureStyle();
    }

    /**
     *
     */
    @Override
    public void run() {
        Webhook webhooks = new Webhook();
        try {
            
            String urlEnd = "https://maker.ifttt.com/trigger/" + getWebhookEventEnd() + "/with/key/" + getWebhookKey();
            webhooks.request(urlEnd);
            final Double postSelectTime = Main.getSession().getDeviceManager().getCurrent().getHardware().getSelectability().getPostSelectTime();
            
            final long name = (long)(postSelectTime*1000.0);
            //Thread.sleep(name);

            String urlStart = "https://maker.ifttt.com/trigger/" + getWebhookEventStart() + "/with/key/" + getWebhookKey();
            webhooks.request(urlStart);
            
            /*final long name = (long)(postSelectTime*1000.0);
            System.out.println(name);
            Thread.sleep(name);
            
            String urlEnd = "https://maker.ifttt.com/trigger/" + getWebhookEventEnd() + "/with/key/" + getWebhookKey();
            webhooks.request(urlEnd);
        */} catch (IOException ex) {
            LOGGER.warn(ex);
        }
            /*  } catch (InterruptedException ex) {
            LOGGER.warn(ex);
            }*/

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

    /**
     *
     */
    public TextField webhookKeyField;

    /**
     *
     */
    public TextField webhookEventStartField;

    /**
     *
     */
    public TextField webhookEventEndField;

    /**
     *
     */
    @Override
    public void setAppableSettings() {
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

        SettingsRow settingsRowA = createSettingRow("Key", "User key configured in webhook");
        webhookKeyField = new TextField();
        webhookKeyField.setText(getWebhookKey());
        webhookKeyField.prefHeight(80.0);
        webhookKeyField.prefWidth(360.0);
        webhookKeyField.getStyleClass().add("settings-text-area");
        settingsRowA.add(webhookKeyField, 1, 0, 2, 1);

        SettingsRow settingsRowStart = createSettingRow("Start Event", "Activity event name in webhook");

        webhookEventStartField = new TextField();
        webhookEventStartField.setText(getWebhookEventStart());
        webhookEventStartField.prefHeight(80.0);
        webhookEventStartField.prefWidth(360.0);
        webhookEventStartField.getStyleClass().add("settings-text-area");
        settingsRowStart.add(webhookEventStartField, 1, 0, 2, 1);

        SettingsRow settingsRowEnd = createSettingRow("End Event", "Activity event name in webhook");

        webhookEventEndField = new TextField();
        webhookEventEndField.setText(getWebhookEventEnd());
        webhookEventEndField.prefHeight(80.0);
        webhookEventEndField.prefWidth(360.0);
        webhookEventEndField.getStyleClass().add("settings-text-area");
        settingsRowEnd.add(webhookEventEndField, 1, 0, 2, 1);

        actionSettings.add(settingsRowA);
        actionSettings.add(settingsRowStart);
        actionSettings.add(settingsRowEnd);
        List<Tab> tabs = super.addAppableSettings();
        return tabs;
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends IftttButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
            
        }
        return preferences;
    }
}
