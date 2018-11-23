/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.reddit;

import com.rometools.rome.io.FeedException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;
import org.symfound.controls.user.media.MediaManager;
import org.symfound.social.reddit.RedditReader;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public class RedditManager extends MediaManager<String> {

    private static final String NAME = RedditManager.class.getName();
    public static final Logger LOGGER = Logger.getLogger(NAME);

    public RedditManager() {
        super(Arrays.asList(""));
    }

    @Override
    public void run() {
        try {
            LOGGER.info("Reading URL:"+getURL());
            //setLinks(null);
            setLinks(RedditReader.readURL(getURL()));
            List<String> keys = new ArrayList<>(getLinks().keySet());
            setItems(keys,100);
        } catch (IOException | FeedException ex) {
            LOGGER.warn(ex);
        }
    }
    private StringProperty url;

    /**
     *
     * @param value
     */
    public void setURL(String value) {
        urlProperty().set(value);
    }

    /**
     *
     * @return
     */
    public final String getURL() {
        return urlProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty urlProperty() {
        if (url == null) {
            url = new SimpleStringProperty("");
        }
        return url;
    }

    private ObjectProperty<Map<String, String>> links;

    public void setLinks(Map<String, String> value) {
        linksProperty().setValue(value);
    }

    public Map<String, String> getLinks() {
        return linksProperty().getValue();
    }

    public ObjectProperty<Map<String, String>> linksProperty() {
        if (links == null) {
            links = new SimpleObjectProperty<>();
        }
        return links;
    }

}
