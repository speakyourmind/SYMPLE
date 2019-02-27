/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.comm.weather;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.symfound.comm.port.JSONReader;

/**
 *
 * @author Javed Gangjee
 */
public class WeatherService {

    private static final String NAME = WeatherService.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);
    private final JSONReader jsonReader = new JSONReader();

    /**
     *
     */
    public String tempUnit;

    /**
     *
     */
    public String temp;

    /**
     *
     */
    public String text;

    /**
     *
     */
    public Location location = new Location();

    /**
     *
     * @param city
     * @param region
     */
    public void load(String city, String region) {
        try {

            jsonReader.loadPackage("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"
                    + city.toLowerCase() + "%2C%20" + region.toLowerCase() + "%22)%20and%20u%3D%27c%27&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");
            JSONObject query = (JSONObject) (jsonReader.getObject("query"));
            JSONObject results = (JSONObject) (query.get("results"));
            JSONObject channel = (JSONObject) (results.get("channel"));
            JSONObject units = (JSONObject) (channel.get("units"));
            tempUnit = (String) units.get("temperature");// Unit

            JSONObject item = (JSONObject) (channel.get("item"));
            JSONObject condition = (JSONObject) (item.get("condition"));
            temp = (String) (condition.get("temp"));
            text = (String) (condition.get("text"));

            location.update((JSONObject) (channel.get("location")));
        } catch (InterruptedException | MalformedURLException | ExecutionException | NullPointerException ex) {
            LOGGER.fatal("Could not get weather data for " + city + ", " + region, ex);
        }

    }

    /**
     *
     * @return
     */
    public Location getLocation() {
        return location;
    }

}
