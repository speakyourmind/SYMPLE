/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.session;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;
import org.symfound.builder.Buildable;
import org.symfound.builder.Builder;
import org.symfound.builder.loader.UIPath;
import org.symfound.builder.user.User;

/**
 *
 * @author Javed Gangjee
 */
public abstract class Session implements Buildable {

    private static final String NAME = Session.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     * @param user
     */
    public Session(User user) {
        this.user = user;
    }
    //Main User

    /**
     *
     */
    public User user;

    /**
     *
     * @return
     */
    public User getUser() {
        return user;
    }

    private Builder builder;

    /**
     *
     * @return
     */
    public Builder getBuilder() {
        if (builder == null) {
            builder = new Builder();
        }
        return builder;
    }

    /**
     *
     */
    public void start() {
        build(getBuilder());
    }

    /**
     *
     */
    public abstract void checkCompletion();

    /**
     *
     */
    public void shutdown() {
        LOGGER.info("Shutting down session");
        Platform.exit();
        System.exit(0);
    }

    /**
     *
     */
    public static final Boolean DEFAULT_MUTEX_VALUE = false;
    private BooleanProperty mutex;

    /**
     *
     * @return
     */
    public Boolean isMutex() {
        return mutexProperty().getValue();
    }

    /**
     *
     * @param value
     */
    public void setMutex(Boolean value) {
        mutexProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public BooleanProperty mutexProperty() {
        if (mutex == null) {
            mutex = new SimpleBooleanProperty(DEFAULT_MUTEX_VALUE);
        }
        return mutex;
    }

    private BooleanProperty built = new SimpleBooleanProperty();

    /**
     *
     * @param value
     */
    public void setBuilt(Boolean value) {
        builtProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isBuilt() {
        return builtProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty builtProperty() {
        if (built == null) {
            built = new SimpleBooleanProperty(false);
        }
        return built;
    }

    // Build Properties
    private final BooleanProperty playing = new SimpleBooleanProperty(false);

    /**
     *
     * @param value
     */
    public void setPlaying(Boolean value) {
        playingProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isPlaying() {
        return playingProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty playingProperty() {
        return playing;
    }

    private Display display;

    /**
     *
     * @return
     */
    public Display getDisplay() {
        if (display == null) {
            display = new Display();
        }
        return display;
    }

    private static final String DEFAULT_PROPERTIES_FILE = "session.properties";
    private Properties properties;

    /**
     *
     * @return
     */
    public Properties getConfiguration() {
        if (properties == null) {
            String resourceName = DEFAULT_PROPERTIES_FILE;
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream resourceStream = loader.getResourceAsStream(resourceName);
            properties = new Properties();
            try {
                properties.load(resourceStream);
            } catch (IOException ex) {
                LOGGER.fatal("Unable to load " + DEFAULT_PROPERTIES_FILE, ex);
            }
        }
        return properties;
    }

    private static final String PROGRAM_VERSIONINFOURL_KEY = "program.versionInfoURL";
    private StringProperty versionInfoURL;

    /**
     *
     * @return
     */
    public String getVersionInfoURL() {
        return versionInfoURLProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty versionInfoURLProperty() {
        if (versionInfoURL == null) {
            String initValue = getConfiguration().getProperty(PROGRAM_VERSIONINFOURL_KEY);
            versionInfoURL = new SimpleStringProperty(initValue);
        }
        return versionInfoURL;
    }

    private static final String PROGRAM_VERSION_KEY = "program.version";
    private StringProperty version;

    /**
     *
     * @return
     */
    public String getVersion() {
        return versionProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty versionProperty() {
        if (version == null) {
            String initValue = getConfiguration().getProperty(PROGRAM_VERSION_KEY);
            version = new SimpleStringProperty(initValue);
        }
        return version;
    }

    private static final String PROGRAM_SEVERITY_KEY = "program.severity";
    private StringProperty severity;

    /**
     *
     * @return
     */
    public String getSeverity() {
        return severityProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty severityProperty() {
        if (severity == null) {
            String initValue = getConfiguration().getProperty(PROGRAM_SEVERITY_KEY);
            severity = new SimpleStringProperty(initValue);
        }
        return severity;
    }

    private DoubleProperty buildTimeout;
    private static final String BUILD_TIMEOUT_KEY = "build.timeout";

    /**
     *
     * @return
     */
    public Double getBuildTimeout() {
        return buildTimeoutProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty buildTimeoutProperty() {
        if (buildTimeout == null) {
            Double initValue = Double.valueOf(getConfiguration().getProperty(BUILD_TIMEOUT_KEY));
            buildTimeout = new SimpleDoubleProperty(initValue);
        }
        return buildTimeout;
    }

    private static List<UIPath> buildList;

    /**
     *
     * @return
     */
    public static List<UIPath> getBuildList() {
        if (buildList == null) {
            buildList = new ArrayList<>();
        }
        return buildList;
    }

}
