/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.social.twitter;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Javed
 */
public class Tweet {

    private StringProperty name;

    /**
     *
     * @param value
     */
    public void setName(String value) {
        nameProperty().set(value);
    }

    /**
     *
     * @return
     */
    public String getName() {
        return nameProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty nameProperty() {
        if (name == null) {
            name = new SimpleStringProperty("");
        }
        return name;
    }
    private StringProperty text;

    /**
     *
     * @param value
     */
    public void setText(String value) {
        textProperty().set(value);
    }

    /**
     *
     * @return
     */
    public String getText() {
        return textProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty textProperty() {
        if (text == null) {
            text = new SimpleStringProperty("");
        }
        return text;
    }

    private StringProperty mediaURL;

    /**
     *
     * @param value
     */
    public void setMediaURL(String value) {
        mediaURLProperty().set(value);
    }

    /**
     *
     * @return
     */
    public String getMediaURL() {
        return mediaURLProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty mediaURLProperty() {
        if (mediaURL == null) {
            mediaURL = new SimpleStringProperty("");
        }
        return mediaURL;
    }
   
    private StringProperty profileImageURL;

    /**
     *
     * @param value
     */
    public void setProfileImageURL(String value) {
        profileImageURLProperty().set(value);
    }

    /**
     *
     * @return
     */
    public String getProfileImageURL() {
        return profileImageURLProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty profileImageURLProperty() {
        if (profileImageURL == null) {
            profileImageURL = new SimpleStringProperty("");
        }
        return profileImageURL;
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
    public String getURL() {
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
   
}
