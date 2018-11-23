/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import images.Images;
import java.util.prefs.Preferences;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;
import org.symfound.comm.weather.WeatherService;
import org.symfound.controls.AppableControl;

/**
 *
 * @author Javed Gangjee
 */
@Deprecated
public final class WeatherButton extends AppableControl {

    private static final String NAME = WeatherButton.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Weather";

    /**
     *
     */
    public static final String DEFAULT_TITLE = "";

    /**
     *
     */
    public AnimatedLabel label;

    /**
     *
     */
    public static WeatherService weatherService;

    /**
     *
     */
    public Button imageButton;

    /**
     *
     */
    public WeatherButton() {
        super("time-button", KEY, DEFAULT_TITLE,"default");
        BuildableGrid grid = new BuildableGrid();
        grid.setSpecRows(1);
        grid.setSpecColumns(2);
        grid.build();

        label = new AnimatedLabel();
        GridPane.setValignment(label, VPos.CENTER);
        GridPane.setHalignment(label, HPos.CENTER);
        label.setWrapText(true);
        setCSS("app-button-label", label);
        grid.add(label, 1, 0);

        imageButton = new Button();
        load(imageButton);
        setCSS("transparent", imageButton);
        grid.add(imageButton, 0, 0);

        addToPane(grid);
        updateTemperature();
        getUser().getProfile().cityProperty().addListener((observable, oldValue, newValue) -> {
            weatherService = null; // TO DO: Find cleaner way to do this.
            updateTemperature();
        });

    }

    private void updateTemperature() {
        if (weatherService == null) {
            weatherService = new WeatherService();
            weatherService.load(getUser().getProfile().getCity(), getUser().getProfile().getRegion());
        }
        label.setText(weatherService.temp + " ⁰");
        getPrimaryControl().setText(getUser().getProfile().getCity() + ", " + getUser().getProfile().getCountry());
        updateIcon();
    }

    @Override
    public void run() {
        updateTemperature();
    }

    /**
     *
     */
    public void updateIcon() {
        String image;
        try {
            image = Images.class.getResource("weather_" + weatherService.text.toLowerCase().replaceAll(" ", "").trim() + ".png").toExternalForm();
        } catch (NullPointerException ex) {
            image = Images.class.getResource("weather_partlycloudy.png").toExternalForm();
            LOGGER.warn("Image not available for current weather " + weatherService.text, ex);
        }
        imageButton.setStyle("-fx-background-image: url('" + image + "'); "
                + "-fx-background-size: 100 100; "
                + "-fx-background-repeat: no-repeat; "
                + "-fx-background-position: center center;"
                + "-fx-padding: 0 40 0 0");
    }

    private StringProperty iconType;

    /**
     *
     * @return
     */
    public String getIconType() {
        return iconType.get();
    }

    /**
     *
     * @param value
     */
    public void setIconType(String value) {
        iconType.set(value);
    }

    /**
     *
     * @return
     */
    public StringProperty iconTypeProperty() {
        if (iconType == null) {
            iconType = new SimpleStringProperty();
        }
        return iconType;
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(this.getClass()).node(KEY.toLowerCase());
        }
        return preferences;
    }

}
