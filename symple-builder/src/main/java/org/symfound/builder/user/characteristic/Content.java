/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.builder.user.characteristic;

import java.util.Properties;
import java.util.prefs.Preferences;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.symfound.builder.characteristic.Characteristic;

/**
 *
 * @author Javed Gangjee
 */
public class Content extends Characteristic {

    /**
     *
     * @param preferences
     * @param properties
     */
    public Content(Preferences preferences, Properties properties) {
        super(preferences, properties);
    }

    private static final String HOME_FOLDER_KEY = "app.general.homeFolder";
    private StringProperty homeFolder;

    /**
     *
     * @param value
     */
    public void setHomeFolder(String value) {
        homeFolderProperty().setValue(value);
        getPreferences().put(HOME_FOLDER_KEY, value);
    }

    /**
     *
     * @return
     */
    public String getHomeFolder() {
        return homeFolderProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty homeFolderProperty() {
        if (homeFolder == null) {
            String initValue = getPreferences().get(HOME_FOLDER_KEY, System.getProperty("user.home"));
            homeFolder = new SimpleStringProperty(initValue);
        }
        return homeFolder;
    }
}
