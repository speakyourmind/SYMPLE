package org.symfound.controls.user.social.twilio;

import com.google.common.collect.Range;
import com.twilio.rest.api.v2010.account.Message;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.prefs.Preferences;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.symfound.builder.user.characteristic.Social;
import org.symfound.controls.AppableControl;
import static org.symfound.controls.ScreenControl.CSS_PATH;
import static org.symfound.controls.ScreenControl.setSizeMax;
import org.symfound.controls.user.AnimatedLabel;
import org.symfound.controls.system.SettingsRow;
import org.symfound.controls.system.dialog.EditDialog;
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
        timeZoneProperty().addListener((observable, oldValue, newValue) -> {
            reloadHistoryGrid();
        });
    }

    public void reloadHistoryGrid() {
        if (smsHistoryGrid != null) {
            this.getChildren().remove(smsHistoryGrid);
        }
        List<Message> smsHistory = retrieveMessages();
        final int numOfMessages = smsHistory.size();

        VBox vBox = new VBox();
        vBox.setSpacing(20.0);
        for (int i = 0; i < numOfMessages; i++) {
            final Message message = smsHistory.get(i);
            VBox area = loadMessageView(message);
            AnchorPane pane = new AnchorPane();
            pane.getChildren().add(area);
            vBox.getChildren().add(pane);
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPadding(new Insets(5, 5, 5, 5));
        scrollPane.setFitToWidth(Boolean.TRUE);
        setSizeMax(scrollPane);
        scrollPane.setContent(vBox);
        scrollPane.setVvalue(1.0);
        scrollPane.getStylesheets().add(CSS_PATH);
        setCSS("main", scrollPane);
        addToPane(scrollPane);
    }

    public VBox loadMessageView(final Message message) {

        final TextArea smsArea = new TextArea(message.getBody());
        smsArea.getStylesheets().add(CSS_PATH);
        smsArea.setWrapText(true);
        smsArea.maxWidthProperty().bind(Bindings.multiply(this.widthProperty(), 0.75));
        VBox vBox = new VBox();
        vBox.getChildren().add(smsArea);
        AnimatedLabel timeSent = loadTimestampLabel(message);
        vBox.getChildren().add(timeSent);
        
        final String myNumber = getUser().getSocial().getTwilioFromNumber();
        final boolean isFromMe = message.getFrom().toString().equals(myNumber);
        if (isFromMe) {
            smsArea.setStyle("-fx-text-fill:-fx-light;\n"
                    + "    -fx-background-color: -fx-dark,-fx-blue;\n"
                    + "    -fx-background-insets: 0,5;");
            // vBox.setStyle("-fx-background-color:-fx-green;");
            AnchorPane.setRightAnchor(vBox, 0.0);
        } else {
            smsArea.setStyle(
                    "    -fx-background-color: -fx-dark,-fx-light;\n"
                    + "    -fx-background-insets: 0,5;");
            AnchorPane.setLeftAnchor(vBox, 0.0);
        }
        smsArea.setMaxHeight(TextUtils.computeTextWidth(smsArea.getFont(),
                    smsArea.getText(), 0.0D) * 0.5);
    
        AnchorPane.setBottomAnchor(vBox, 0.0);
        AnchorPane.setTopAnchor(vBox, 0.0);

        return vBox;
    }

    public AnimatedLabel loadTimestampLabel(final Message message) {
        final DateTime dateSent = message.getDateSent().toDateTime(DateTimeZone.forID(getTimeZone()));
        
        DateTimeFormatter fmt = DateTimeFormat.forPattern("EEE, d MMM ''yy, hh:mm aaa");
        String formattedDateSent = fmt.print(dateSent);
        AnimatedLabel captionLabel = new AnimatedLabel(formattedDateSent);
        setCSS("settings-caption", captionLabel);
        captionLabel.setWrapText(true);
        captionLabel.setAlignment(Pos.CENTER_LEFT);
        setSizeMax(captionLabel);
    //    captionTextProperty().bindBidirectional(captionLabel.textProperty());
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
    private ChoiceBox<String> timeZoneChoices;
    /**
     *
     */
    @Override
    public void setAppableSettings() {
        setFromNumber(fromNumberField.getText());
        setTimeZone(timeZoneChoices.getValue());
        super.setAppableSettings();
    }

    /**
     *
     */
    @Override
    public void resetAppableSettings() {
        fromNumberField.setText(getFromNumber());
        timeZoneChoices.setValue(getTimeZone());
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

        
        SettingsRow timeZoneChoicesRow = EditDialog.createSettingRow("Time Zone", "Time sent shown as per Twilio time zones");

        timeZoneChoices = new ChoiceBox<>(FXCollections.observableArrayList(new ArrayList<>(DateTimeZone.getAvailableIDs())));
        timeZoneChoices.setValue(getTimeZone());
        timeZoneChoices.setMaxSize(180.0, 60.0);

        timeZoneChoices.getStyleClass().add("settings-text-area");
        timeZoneChoicesRow.add(timeZoneChoices, 1, 0, 2, 1);

        
        actionSettings.add(formatRow);
        
        actionSettings.add(timeZoneChoicesRow);
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
            fromNumber = new SimpleStringProperty(getPreferences().get("fromNumber", ""));
        }
        return fromNumber;
    }

      private StringProperty timeZone;

    /**
     *
     * @param value
     */
    public void setTimeZone(String value) {
        timeZoneProperty().setValue(value);
        getPreferences().put("timeZone", value);
        LOGGER.info("timeZone set to: " + value);

    }

    /**
     *
     * @return
     */
    public String getTimeZone() {
        return timeZoneProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty timeZoneProperty() {
        if (timeZone == null) {
            timeZone = new SimpleStringProperty(getPreferences().get("timeZone", "America/Detroit"));
        }
        return timeZone;
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
