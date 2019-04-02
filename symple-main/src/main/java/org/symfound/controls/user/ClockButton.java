package org.symfound.controls.user;

import java.util.List;
import java.util.prefs.Preferences;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import org.symfound.controls.AppableControl;
import org.symfound.controls.system.SettingsRow;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.tools.timing.clock.Clock;

/**
 *
 * @author Javed Gangjee
 */
public class ClockButton extends AppableControl {

    /**
     *
     */
    public static final String KEY = "Clock";

    /**
     *
     */
    public static final String DEFAULT_TITLE = "";
    private static final String DEFAULT_TIME_FORMAT = "hh:mm";
    private static final String DEFAULT_DATE_FORMAT = "d MMM yy";

    /**
     *
     * @param index
     */
    public ClockButton(String index) {
        super("time-button", KEY, DEFAULT_TITLE, index);

        initialize();
    }

    private TextField formatField;

    /**
     *
     */
    @Override
    public void setAppableSettings() {
        setDateFormat(formatField.getText());
        super.setAppableSettings();
    }

    /**
     *
     */
    @Override
    public void resetAppableSettings() {
        formatField.setText(getDateFormat());
        super.resetAppableSettings();
    }

    /**
     *
     * @return
     */
    @Override
    public List<Tab> addAppableSettings() {
        SettingsRow formatRow = createSettingRow("Date Format", "Ex: hh:mm, d MMM 'yy");
        formatField = new TextField();
        formatField.setText(getDateFormat());
        formatField.maxHeight(80.0);
        formatField.maxWidth(60.0);
        formatField.getStyleClass().add("settings-text-area");
        formatRow.add(formatField, 1, 0, 1, 1);

        settings.add(formatRow);
        List<Tab> tabs = super.addAppableSettings();

        return tabs;
    }

    private void initialize() {
        Clock date = new Clock(getDateFormat());
        date.textProperty().addListener((observable,oldValue,newValue)->{
            getPrimaryControl().setText(newValue);
        });
        date.play();

        dateFormatProperty().addListener((observable, oldValue, newValue) -> {
            date.setFormat(newValue);
        });
        configureStyle();
    }

    @Override
    public void run() {

    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends ClockButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

    private static final String DATEFORMAT_KEY = "dateformat";
    private static final String DEFAULT_DATEFORMAT = "d MMM yy";
    private StringProperty dateFormat;

    /**
     *
     * @param value
     */
    public void setDateFormat(String value) {
        dateFormatProperty().setValue(value);
        getPreferences().put("dateformat", value);
        LOGGER.info("DateFormat set to: " + value);

    }

    /**
     *
     * @return
     */
    public String getDateFormat() {
        return dateFormatProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty dateFormatProperty() {
        if (dateFormat == null) {
            dateFormat = new SimpleStringProperty(getPreferences().get(DATEFORMAT_KEY, DEFAULT_DATEFORMAT));
        }
        return dateFormat;
    }
}
