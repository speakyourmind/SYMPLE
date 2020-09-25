/*
 * Copyright (C) 2015 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.symfound.device.hardware;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.prefs.Preferences;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.symfound.builder.characteristic.CharacteristicManager;
import org.symfound.builder.user.feature.Feature;
import org.symfound.device.hardware.characteristic.HardwareInfo;
import org.symfound.device.hardware.characteristic.Movability;
import org.symfound.device.hardware.characteristic.Processability;
import org.symfound.device.hardware.characteristic.Selectability;
import org.symfound.tools.timing.LoopedEvent;

/**
 *
 * @author Javed Gangjee
 * @param <T>
 */
public abstract class Hardware<T extends Feature> extends CharacteristicManager implements Processable {

    private static final String NAME = Hardware.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String HARDWARE_PACKAGE = "hardware";

    /**
     *
     */
    public static final String GENERIC = "Other";

    /**
     *
     */
    public static final String EYE_TRACKER = "Eye Tracker";

    /**
     *
     */
    public static final String COLOURTRACKER = "Colour Tracker";

    /**
     *
     */
    public static final String SWIFTY = "Swifty";

    /**
     *
     */
    public static final List<String> DEVICE_TYPES = Arrays.asList(EYE_TRACKER, SWIFTY, GENERIC);

    /**
     *
     */
    public T feature;

    /**
     *
     */
    public List<T> tList;

    /**
     *
     * @param initName
     * @param feature
     */
    public Hardware(String initName, T feature) {
        this.initName = initName;
        this.feature = feature;
        feature.trackedProperty().bindBidirectional(processedProperty());
      //  this.clearPreferences();
    }

    /**
     *
     * @param initName
     * @param featureList
     */
    public Hardware(String initName, List<T> featureList) {
        this.initName = initName;
        this.tList = featureList;
        featureList.stream().forEach((feature) -> {
            feature.trackedProperty().bindBidirectional(processedProperty());
        });
    }

    /**
     *
     */
    @Override
    public void process() {
        setProcessed(false);
        record();
        bundle();
        setProcessed(true);
    }

    /**
     *
     */
    public ObjectProperty<JSONObject> dataPackage;

    /**
     *
     * @param value
     */
    public void setDataPackage(JSONObject value) {
        dataPackageProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public JSONObject getDataPackage() {
        return dataPackageProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<JSONObject> dataPackageProperty() {
        if (dataPackage == null) {
            dataPackage = new SimpleObjectProperty<>();
        }
        return dataPackage;
    }

    // Characteristics

    /**
     *
     * @return
     */
    @Override
    public final Preferences getPreferences() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(this.getClass());
            
        }
        return preferences;
    }

    /**
     *
     * @return
     */
    @Override
    public Properties getDefaultConfiguration() {
        if (properties == null) {
            final String hardwareName = getName().toLowerCase().replaceAll(" ", "");
            String resourceName = HARDWARE_PACKAGE + "/" + hardwareName + ".properties";
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream resourceStream = loader.getResourceAsStream(resourceName);
            properties = new Properties();
            try {
                properties.load(resourceStream);
            } catch (IOException ex) {
                LOGGER.fatal("Unable to load properties from " + resourceName + " for " + hardwareName, ex);
            }
        }

        return properties;
    }
    
    /**
     *
     */
    public LoopedEvent loopedEvent;
   
    /**
     *
     * @return
     */
    public LoopedEvent createScanningEvent() {
        LoopedEvent scanningEvent = new LoopedEvent();
        scanningEvent.setup(getScanRate(), (ActionEvent) -> {
            process();
        });
        return scanningEvent;
    }

    /**
     *
     */
    public static final String SCAN_RATE_KEY = "processability.read.rate";
    private DoubleProperty scanRate;

    /**
     *
     * @param value
     */
    public void setScanRate(Double value) {
        scanRateProperty().setValue(value);
        getPreferences().put(SCAN_RATE_KEY, value.toString());
    }

    /**
     *
     * @return
     */
    public Double getScanRate() {
        return scanRateProperty().getValue();
    }

    /**
     *
     * @return
     */
    public DoubleProperty scanRateProperty() {
        if (scanRate == null) {
            Double initValue = Double.valueOf(getPreference(SCAN_RATE_KEY));
            scanRate = new SimpleDoubleProperty(initValue);
        }
        return scanRate;
    }

    private HardwareInfo info;

    /**
     *
     * @return
     */
    public HardwareInfo getInfo() {
        if (info == null) {
            info = new HardwareInfo(getPreferences(), getDefaultConfiguration());
        }
        return info;
    }

    private Processability processability;

    /**
     *
     * @return
     */
    public Processability getProcessability() {
        if (processability == null) {
            processability = new Processability(getPreferences(), getDefaultConfiguration());
        }
        return processability;
    }

    private Selectability selectability;

    /**
     *
     * @return
     */
    public Selectability getSelectability() {
        if (selectability == null) {
            selectability = new Selectability(getPreferences(), getDefaultConfiguration());
        }
        return selectability;
    }

    private Movability movability;

    /**
     *
     * @return
     */
    public Movability getMovability() {
        if (movability == null) {
            movability = new Movability(getPreferences(), getDefaultConfiguration());
        }
        return movability;
    }

    private String initName = GENERIC;
    private StringProperty name;

    /**
     *
     * @param value
     */
    public void setName(String value) {
        // Validate value. Throw exception.
        nameProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getName() {
        return nameProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty nameProperty() {
        if (name == null) {
            name = new SimpleStringProperty(initName);
        }
        return name;
    }

    private BooleanProperty enabled;

    /**
     *
     * @param value
     */
    public void setEnabled(Boolean value) {
        enabledProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isEnabled() {
        return enabledProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty enabledProperty() {
        if (enabled == null) {
            Boolean initValue = Boolean.parseBoolean(getDefaultConfiguration().getProperty("enabled"));
            enabled = new SimpleBooleanProperty(initValue);
        }
        return enabled;
    }

    // State Properties
    private static final Boolean DEFAULT_CONNECTED = Boolean.FALSE;
    private BooleanProperty connected;

    /**
     *
     * @param value
     */
    public void setConnected(Boolean value) {
        connectedProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isConnected() {
        return connectedProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty connectedProperty() {
        if (connected == null) {
            connected = new SimpleBooleanProperty(DEFAULT_CONNECTED);
        }
        return connected;
    }

    private static final Boolean DEFAULT_LAUNCHED = Boolean.FALSE;
    private BooleanProperty launched;

    /**
     *
     * @param value
     */
    public void setLaunched(Boolean value) {
        launchedProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isLaunched() {
        return launchedProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty launchedProperty() {
        if (launched == null) {
            launched = new SimpleBooleanProperty(DEFAULT_LAUNCHED);
        }
        return launched;
    }

    private static final Boolean DEFAULT_PROCESSED = Boolean.FALSE;
    private BooleanProperty processed;

    /**
     *
     * @param value
     */
    public void setProcessed(Boolean value) {
        processedProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isProcessed() {
        return processedProperty().getValue();
    }

    /**
     *
     * @return
     */
    public final BooleanProperty processedProperty() {
        if (processed == null) {
            processed = new SimpleBooleanProperty(DEFAULT_PROCESSED);
        }
        return processed;
    }

    private static final Boolean DEFAULT_BUNDLED = Boolean.FALSE;
    private BooleanProperty bundled;

    /**
     *
     * @param value
     */
    public void setBundled(Boolean value) {
        bundledProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Boolean isBundled() {
        return bundledProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty bundledProperty() {
        if (bundled == null) {
            bundled = new SimpleBooleanProperty(DEFAULT_BUNDLED);
        }
        return bundled;
    }

}
