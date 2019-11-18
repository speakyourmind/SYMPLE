package org.symfound.controls.user;

import java.util.prefs.Preferences;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.text.FontWeight;
import org.apache.log4j.Logger;
import org.symfound.controls.AppableControl;
import static org.symfound.main.FullSession.getMainUI;
import org.symfound.main.builder.App;

/**
 * Launches an application along with the configured device.
 *
 */
public class AppButton extends AppableControl {

    /**
     *
     */
    public static final String NAME = AppButton.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    private static final Integer INITIAL_INDEX = 0;
    private final Integer initScreen;

    /**
     *
     * @param app
     * @param index
     * @param title
     * @param screen
     */
    public AppButton(String app, String index, String title, Integer screen) {
        super("", app, title, index);
        this.app.setValue(app);
        this.initScreen = screen;
        initialize();
    }

    /**
     *
     */
    public final void initialize() {
        configureStyle("Roboto", FontWeight.NORMAL);
     
    }

    /**
     *
     */
    @Override
    public void configureStyle(String fontFamily, FontWeight fw) {
        if (!getOverrideStyle().isEmpty()) {
            LOGGER.info("Setting style for " + getKey() + "." + getIndex() + " to " + getOverrideStyle());
            getPrimaryControl().setStyle(getOverrideStyle());
        } else {
            setCSS(app.getValue().toLowerCase().replaceAll(" ", "") + "-app-button", primary);
            appProperty().addListener((observeableValue, oldValue, newValue) -> {
                setCSS(newValue.toLowerCase().replaceAll(" ", "") + "-app-button", primary);
            });
        }
    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton("");
        primary.setWrapText(true);
        load(primary);
        setSelection(primary);

    }

    /**
     *
     * @param output
     */
    public void launchApp(String output) {
   //     getSession().getApp(output).launch(getScreen());
        getSession().setPlaying(true);

    }

    /**
     *
     */
    @Override
    public void run() {
        LOGGER.info("Launching App: " + getApp());
        launchApp(getApp());
        LOGGER.info("Closing Main UI");
        getMainUI().close();
    }

    private StringProperty app = new SimpleStringProperty(App.NONE);

    /**
     * Sets the name of the application that this button will point to.
     *
     * @param value application name
     */
    public void setApp(String value) {
        appProperty().setValue(value);
    }

    /**
     * Gets the name of the application that this button will point to.
     *
     * @return application name
     */
    public String getApp() {
        return appProperty().getValue();
    }

    /**
     * Represents the name of the application that this button will point to.
     *
     * @return application name property
     */
    public StringProperty appProperty() {
        if (app == null) {
            app = new SimpleStringProperty(App.NONE);
        }
        return app;
    }

    private IntegerProperty screen;

    /**
     * Sets the offset of the UI in the Application list that this button will
     * point to.
     *
     * @param value screen offset
     */
    public void setScreen(Integer value) {
        screenProperty().set(value);
    }

    /**
     * Gets the offset of the UI in the Application list that this button will
     * point to.
     *
     * @return screen offset
     */
    public Integer getScreen() {
        return screenProperty().get();
    }

    /**
     * Represents the offset of the UI in the Application list that this button
     * will point to
     *
     * @return screen offset property
     */
    public IntegerProperty screenProperty() {
        if (screen == null) {
            screen = new SimpleIntegerProperty(initScreen);
        }
        return screen;
    }

    @Override
    public Preferences getPreferences() {
        // if (preferences == null) {
        String appPreferences = getApp().toLowerCase() + "/" + getIndex().toLowerCase();
        preferences = Preferences.userNodeForPackage(this.getClass()).node(appPreferences);
        //}
        return preferences;
    }
}
