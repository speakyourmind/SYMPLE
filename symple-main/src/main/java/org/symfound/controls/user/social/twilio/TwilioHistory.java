package org.symfound.controls.user.social.twilio;

import com.google.common.collect.Range;
import com.twilio.rest.api.v2010.account.Message;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.prefs.Preferences;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.symfound.builder.user.characteristic.Social;
import org.symfound.controls.AppableControl;
import org.symfound.controls.user.AnimatedLabel;
import static org.symfound.controls.ScreenControl.setSizeMax;
import org.symfound.controls.system.SettingsRow;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.main.Main;
import org.symfound.social.sms.TwilioReader;

/**
 *
 * @author Javed Gangjee
 */
public final class TwilioHistory extends AppableControl {

    /**
     *
     */
    public static final String NAME = TwilioHistory.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    private static final boolean BACKUP_ON_EXIT = Boolean.TRUE;

    /**
     *
     */
    public static final String KEY = "Twilio History";

    /**
     *
     */
    public static final String DEFAULT_TITLE = "";

    public TwilioHistory(String index) {
        super("transparent", KEY, DEFAULT_TITLE, index);
        initialize();
    }

    TwilioHistoryGrid smsHistoryGrid;

    private void initialize() {
        setDisabled(true);
        reloadHistoryGrid();
        fromNumberProperty().addListener((observable, oldValue, newValue) -> {
            reloadHistoryGrid();
        });
    }

    public void reloadHistoryGrid() {
        if (smsHistoryGrid != null) {
            this.getChildren().remove(smsHistoryGrid);
        }
        List<Message> smsHistory = retrieveMessages();
        final int numOfMessages = smsHistory.size();
        smsHistoryGrid = new TwilioHistoryGrid(numOfMessages, 160.0);
        for (int i = 0; i < numOfMessages; i++) {
            final Message message = smsHistory.get(i);
            AnchorPane pane = new AnchorPane();
            VBox vBox = loadMessageView(message);
            pane.getChildren().add(vBox);
            smsHistoryGrid.grid.add(pane, 0, i);
        }
        addToPane(smsHistoryGrid);
    }

    public VBox loadMessageView(final Message message) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setMaxWidth(600.0);
        vBox.setMaxHeight(200.0);

        final TextArea smsArea = new TextArea(message.getBody());
        smsArea.getStylesheets().add(CSS_PATH);
        smsArea.setWrapText(true);
        vBox.getChildren().add(smsArea);

        final String myNumber = getUser().getSocial().getTwilioFromNumber();
        final boolean isFromMe = message.getFrom().toString().equals(myNumber);
        if (isFromMe) {
            smsArea.setStyle("-fx-text-fill:-fx-light;\n"
                    + "    -fx-background-color: -fx-dark,-fx-blue;\n"
                    + "    -fx-background-insets: 0,5;");
            AnchorPane.setRightAnchor(vBox, 0.0);
        } else {
            smsArea.setStyle(
                    "    -fx-background-color: -fx-dark,-fx-light;\n"
                    + "    -fx-background-insets: 0,5;");
            AnchorPane.setLeftAnchor(vBox, 0.0);
        }

        AnimatedLabel timeSent = loadTimestampLabel(message);
        vBox.getChildren().add(timeSent);

        AnchorPane.setBottomAnchor(vBox, 0.0);
        AnchorPane.setTopAnchor(vBox, 0.0);

        return vBox;
    }

    public AnimatedLabel loadTimestampLabel(final Message message) {
        final DateTime dateSent = message.getDateSent();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("EEE, d MMM ''yy, hh:mm aaa");
        String formattedDateSent = fmt.print(dateSent);
        AnimatedLabel captionLabel = new AnimatedLabel(formattedDateSent);
        setCSS("settings-caption", captionLabel);
        captionLabel.setWrapText(true);
        captionLabel.setAlignment(Pos.CENTER_LEFT);
        setSizeMax(captionLabel);
        captionTextProperty().bindBidirectional(captionLabel.textProperty());
        return captionLabel;
    }

    @Override
    public void run() {

    }

    public List<Message> retrieveMessages() {
        Map<Long, Message> messages = new HashMap<>();
        Range range = Range.atLeast(DateTime.now().minusYears(1));
        final String contactNumber = getFromNumber();
        final String myNumber = Main.getSession().getUser().getSocial().getTwilioFromNumber();
        List<Message> readContactsSMS = getTwilioReader().readContactsSMS(range, contactNumber, myNumber);
        readContactsSMS.forEach((message) -> {
            messages.put(message.getDateSent().getMillis(), message);
        });
        List<Message> readMySMSToContact = getTwilioReader().readMySMSToContact(range, contactNumber, myNumber);
        readMySMSToContact.forEach((message) -> {
            messages.put(message.getDateSent().getMillis(), message);
        });

        SortedSet<Long> keys = new TreeSet<>(messages.keySet());
        List<Message> sortedMessages = new ArrayList<>();
        for (Long key : keys) {
            Message sortedMessage = messages.get(key);
            sortedMessages.add(sortedMessage);
        }

        return sortedMessages;
    }

    private TwilioReader twilioReader;

    private TwilioReader getTwilioReader() {
        if (twilioReader == null) {
            final Social social = Main.getSession().getUser().getSocial();
            twilioReader = new TwilioReader(social.getTwilioAccountSID(), social.getTwilioAuthToken());
        }
        return twilioReader;
    }

    private TextField fromNumberField;

    /**
     *
     */
    @Override
    public void setAppableSettings() {
        setFromNumber(fromNumberField.getText());
        super.setAppableSettings();
    }

    /**
     *
     */
    @Override
    public void resetAppableSettings() {
        fromNumberField.setText(getFromNumber());
        super.resetAppableSettings();
    }

    /**
     *
     * @return
     */
    @Override
    public List<Tab> addAppableSettings() {
        SettingsRow formatRow = createSettingRow("Contact Number", "Use +15555555555 format only");
        fromNumberField = new TextField();
        fromNumberField.setText(getFromNumber());
        fromNumberField.maxHeight(80.0);
        fromNumberField.maxWidth(60.0);
        fromNumberField.getStyleClass().add("settings-text-area");
        formatRow.add(fromNumberField, 1, 0, 1, 1);

        actionSettings.add(formatRow);
        List<Tab> tabs = super.addAppableSettings();

        return tabs;
    }

    private StringProperty fromNumber;

    /**
     *
     * @param value
     */
    public void setFromNumber(String value) {
        fromNumberProperty().setValue(value);
        getPreferences().put("fromNumber", value);
        LOGGER.info("fromNumber set to: " + value);

    }

    /**
     *
     * @return
     */
    public String getFromNumber() {
        return fromNumberProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty fromNumberProperty() {
        if (fromNumber == null) {
            fromNumber = new SimpleStringProperty(getPreferences().get("fromNumber", "+15089444913"));
        }
        return fromNumber;
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends TwilioHistory> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

}
