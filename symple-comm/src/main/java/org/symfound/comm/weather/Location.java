/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.comm.weather;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.simple.JSONObject;

/**
 *
 * @author Javed Gangjee
 */
public class Location {

    /**
     *
     * @param jsonObject
     */
    public void update(JSONObject jsonObject) {
        updateCity(jsonObject);
        updateCountry(jsonObject);
        updateRegion(jsonObject);
    }
    private StringProperty city;

    /**
     *
     * @param jsonObject
     */
    public void updateCity(JSONObject jsonObject) {
        setCity((String) (jsonObject.get("city")));
    }

    /**
     *
     * @param value
     */
    public void setCity(String value) {
        cityProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getCity() {
        return cityProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty cityProperty() {
        if (city == null) {
            city = new SimpleStringProperty("");
        }
        return city;
    }

    private StringProperty country;

    /**
     *
     * @param jsonObject
     */
    public void updateCountry(JSONObject jsonObject) {
        setCountry((String) (jsonObject.get("country")));
    }

    /**
     *
     * @param value
     */
    public void setCountry(String value) {
        countryProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getCountry() {
        return countryProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty countryProperty() {
        if (country == null) {
            country = new SimpleStringProperty("");
        }
        return country;
    }

    private StringProperty region;

    /**
     *
     * @param jsonObject
     */
    public void updateRegion(JSONObject jsonObject) {
        setRegion((String) (jsonObject.get("region")));
    }

    /**
     *
     * @param value
     */
    public void setRegion(String value) {
        regionProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getRegion() {
        return regionProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty regionProperty() {
        if (region == null) {
            region = new SimpleStringProperty("");
        }
        return region;
    }
}
