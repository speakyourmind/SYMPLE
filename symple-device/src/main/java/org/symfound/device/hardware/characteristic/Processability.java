/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.device.hardware.characteristic;

import java.io.File;
import java.util.Properties;
import java.util.prefs.Preferences;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.symfound.builder.characteristic.Characteristic;

/**
 *
 * @author Javed Gangjee
 */
public class Processability extends Characteristic {

    /**
     *
     * @param preferences
     * @param properties
     */
    public Processability(Preferences preferences, Properties properties) {
        super(preferences, properties);
    }

    private static final String WRITE_METHOD_KEY = "processability.write.method";
    private StringProperty writeMethod;

    /**
     *
     * @param value
     */
    public void setWriteMethod(String value) {
        writeMethodProperty().setValue(value);
        getPreferences().put(WRITE_METHOD_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getWriteMethod() {
        return writeMethodProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty writeMethodProperty() {
        if (writeMethod == null) {
            String initValue = getPreference(WRITE_METHOD_KEY);
            writeMethod = new SimpleStringProperty(initValue);
        }
        return writeMethod;
    }

    private static final String WRITE_PORT_KEY = "processability.write.port";
    private IntegerProperty writePort;

    /**
     *
     * @param value
     */
    public void setWritePort(Integer value) {
        writePortProperty().setValue(value);
        getPreferences().put(WRITE_PORT_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Integer getWritePort() {
        return writePortProperty().getValue();
    }

    /**
     *
     * @return
     */
    public IntegerProperty writePortProperty() {
        if (writePort == null) {
            Integer initValue = Integer.parseInt(getPreference(WRITE_PORT_KEY).trim());
            writePort = new SimpleIntegerProperty(initValue);
        }
        return writePort;
    }

    private static final String WRITE_FILE_KEY = "processability.write.file";
    private StringProperty writeFile;

    /**
     *
     * @param value
     */
    public void setWriteFile(String value) {
        writeFileProperty().setValue(value);
        getPreferences().put(WRITE_FILE_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getWriteFile() {
        return writeFileProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty writeFileProperty() {
        if (writeFile == null) {
            String deviceFolder = getDeviceFolder();
            final String defaultValue = deviceFolder + "request.json";
            String initValue = getPreferences().get(WRITE_FILE_KEY, defaultValue);
            writeFile = new SimpleStringProperty(initValue);
        }
        return writeFile;
    }

    private static final String READ_METHOD_KEY = "processability.read.method";
    private StringProperty readMethod;

    /**
     *
     * @param value
     */
    public void setReadMethod(String value) {
        readMethodProperty().setValue(value);
        getPreferences().put(READ_METHOD_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getReadMethod() {
        return readMethodProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty readMethodProperty() {
        if (readMethod == null) {
            String initValue = getPreference(READ_METHOD_KEY);
            readMethod = new SimpleStringProperty(initValue);
        }
        return readMethod;
    }

    private static final String READ_PORT_KEY = "processability.read.port";
    private IntegerProperty readPort;

    /**
     *
     * @param value
     */
    public void setReadPort(Integer value) {
        readPortProperty().setValue(value);
        getPreferences().put(READ_PORT_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Integer getReadPort() {
        return readPortProperty().getValue();
    }

    /**
     *
     * @return
     */
    public IntegerProperty readPortProperty() {
        if (readPort == null) {
            Integer initValue = Integer.parseInt(getPreference(READ_PORT_KEY).trim());
            readPort = new SimpleIntegerProperty(initValue);
        }
        return readPort;
    }

    private static final String READ_FILE_KEY = "processability.read.file";
    private StringProperty readFile; // To Do: Change to ObjectProperty<File>

    /**
     *
     * @param value
     */
    public void setReadFile(String value) {
        readFileProperty().setValue(value);
        getPreferences().put(READ_FILE_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getReadFile() {
        return readFileProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty readFileProperty() {
        if (readFile == null) {
            String deviceFolder = getDeviceFolder();
            final String defaultValue = deviceFolder + "request.json";
            String initValue = getPreferences().get(READ_FILE_KEY, defaultValue);
            readFile = new SimpleStringProperty(initValue);
        }
        return readFile;
    }

    private String getDeviceFolder() {
        final String property = System.getProperty("user.home");
        final String deviceFolder = property + "/Documents/SYMPLE/Device/";
        File folderDestination = new File(deviceFolder);
        folderDestination.mkdirs(); // Create the folder if it doesn't exist.
        return deviceFolder;
    }

    // JSON KEYS

    /**
     *
     */
    public static final String DEFAULT_TIMESTAMP_KEY_VALUE = "timestamp";

    private static final String TIMESTAMP_KEY = "processability.keys.timestamp";
    private StringProperty timestampKey;

    /**
     *
     * @param value
     */
    public void setTimestampKey(String value) {
        timestampKeyProperty().setValue(value);
        getPreferences().put(TIMESTAMP_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getTimestampKey() {
        return timestampKeyProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty timestampKeyProperty() {
        if (timestampKey == null) {
            String initValue = getPreference(TIMESTAMP_KEY, DEFAULT_TIMESTAMP_KEY_VALUE);
            timestampKey = new SimpleStringProperty(initValue);
        }
        return timestampKey;
    }

    /**
     *
     */
    public static final String DEFAULT_TRACKING_KEY_VALUE = "isTracking";
    private static final String TRACKING_KEY = "processability.keys.tracking";
    private StringProperty trackingKey;

    /**
     *
     * @param value
     */
    public void setTrackingKey(String value) {
        trackingKeyProperty().setValue(value);
        getPreferences().put(TRACKING_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getTrackingKey() {
        return trackingKeyProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty trackingKeyProperty() {
        if (trackingKey == null) {
            String initValue = getPreference(TRACKING_KEY, DEFAULT_TRACKING_KEY_VALUE);
            trackingKey = new SimpleStringProperty(initValue);
        }
        return trackingKey;
    }

    /**
     *
     */
    public static final String DEFAULT_CLICKED_KEY_VALUE = "clicked";
    private static final String CLICKED_KEY = "processability.keys.clicked";
    private StringProperty clickedKey;

    /**
     *
     * @param value
     */
    public void setClickedKey(String value) {
        clickedKeyProperty().setValue(value);
        getPreferences().put(CLICKED_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getClickedKey() {
        return clickedKeyProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty clickedKeyProperty() {
        if (clickedKey == null) {
            String initValue = getPreference(CLICKED_KEY, DEFAULT_CLICKED_KEY_VALUE);
            clickedKey = new SimpleStringProperty(initValue);
        }
        return clickedKey;
    }

    /**
     *
     */
    public static final String DEFAULT_RAW_X_KEY_VALUE = "rawX";
    private static final String RAW_X_KEY = "processability.keys.rawX";
    private StringProperty rawXKey;

    /**
     *
     * @param value
     */
    public void setRawXKey(String value) {
        rawXKeyProperty().setValue(value);
        getPreferences().put(RAW_X_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getRawXKey() {
        return rawXKeyProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty rawXKeyProperty() {
        if (rawXKey == null) {
            String initValue = getPreference(RAW_X_KEY, DEFAULT_RAW_X_KEY_VALUE);
            rawXKey = new SimpleStringProperty(initValue);
        }
        return rawXKey;
    }

    /**
     *
     */
    public static final String DEFAULT_RAW_Y_KEY_VALUE = "rawY";
    private static final String RAW_Y_KEY = "processability.keys.rawY";
    private StringProperty rawYKey;

    /**
     *
     * @param value
     */
    public void setRawYKey(String value) {
        rawYKeyProperty().setValue(value);
        getPreferences().put(RAW_Y_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getRawYKey() {
        return rawYKeyProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty rawYKeyProperty() {
        if (rawYKey == null) {
            String initValue = getPreference(RAW_Y_KEY, DEFAULT_RAW_Y_KEY_VALUE);
            rawYKey = new SimpleStringProperty(initValue);
        }
        return rawYKey;
    }

    
    /**
     *
     */
    public static final String DEFAULT_RAW_Z_KEY_VALUE = "rawZ";
    private static final String RAW_Z_KEY = "processability.keys.rawZ";
    private StringProperty rawZKey;

    /**
     *
     * @param value
     */
    public void setRawZKey(String value) {
        rawZKeyProperty().setValue(value);
        getPreferences().put(RAW_Z_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getRawZKey() {
        return rawZKeyProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty rawZKeyProperty() {
        if (rawZKey == null) {
            String initValue = getPreference(RAW_Z_KEY, DEFAULT_RAW_Z_KEY_VALUE);
            rawZKey = new SimpleStringProperty(initValue);
        }
        return rawZKey;
    }
    
    
    /**
     *
     */
    public static final String DEFAULT_SMOOTH_X_KEY_VALUE = "smoothX";
    private static final String SMOOTH_X_KEY = "processability.keys.smoothX";
    private StringProperty smoothXKey;

    /**
     *
     * @param value
     */
    public void setSmoothXKey(String value) {
        smoothXKeyProperty().setValue(value);
        getPreferences().put(SMOOTH_X_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getSmoothXKey() {
        return smoothXKeyProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty smoothXKeyProperty() {
        if (smoothXKey == null) {
            String initValue = getPreference(SMOOTH_X_KEY, DEFAULT_SMOOTH_X_KEY_VALUE);
            smoothXKey = new SimpleStringProperty(initValue);
        }
        return smoothXKey;
    }

    /**
     *
     */
    public static final String DEFAULT_SMOOTH_Y_KEY_VALUE = "smoothY";
    private static final String SMOOTH_Y_KEY = "processability.keys.smoothY";
    private StringProperty smoothYKey;

    /**
     *
     * @param value
     */
    public void setSmoothYKey(String value) {
        smoothYKeyProperty().setValue(value);
        getPreferences().put(SMOOTH_Y_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getSmoothYKey() {
        return smoothYKeyProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty smoothYKeyProperty() {
        if (smoothYKey == null) {
            String initValue = getPreference(SMOOTH_Y_KEY, DEFAULT_SMOOTH_Y_KEY_VALUE);
            smoothYKey = new SimpleStringProperty(initValue);
        }
        return smoothYKey;
    }
        /**
     *
     */
    public static final String DEFAULT_SMOOTH_Z_KEY_VALUE = "smoothZ";
    private static final String SMOOTH_Z_KEY = "processability.keys.smoothZ";
    private StringProperty smoothZKey;

    /**
     *
     * @param value
     */
    public void setSmoothZKey(String value) {
        smoothZKeyProperty().setValue(value);
        getPreferences().put(SMOOTH_Z_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getSmoothZKey() {
        return smoothZKeyProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty smoothZKeyProperty() {
        if (smoothZKey == null) {
            String initValue = getPreference(SMOOTH_Z_KEY, DEFAULT_SMOOTH_Z_KEY_VALUE);
            smoothZKey = new SimpleStringProperty(initValue);
        }
        return smoothZKey;
    }
}
