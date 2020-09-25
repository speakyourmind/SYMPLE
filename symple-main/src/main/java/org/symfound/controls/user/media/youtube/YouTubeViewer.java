/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.youtube;

import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;
import org.symfound.controls.user.ScreenStatus;
import org.symfound.controls.user.media.web.WebViewer;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public class YouTubeViewer extends WebViewer {

    private static final String NAME = YouTubeViewer.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "YouTube Viewer";

    /**
     *
     * @param index
     */
    public YouTubeViewer(String index) {
        super(KEY, index);
    }

    /**
     *
     */
    @Override
    public void configure() {
        getYouTubeManager().getIterator().modeProperty().addListener((observable, oldValue, newValue) -> {
            play();
        });

        if (getIndex().contains("next")) {
            reload();
        }
        indexProperty().addListener((observable1, oldValue1, newValue1) -> {
            //System.out.println("Index has changed to " + newValue1);
            if (getIndex().contains("next")) {
                reload();
            }
        });

    }

    /**
     *
     */
    @Override
    public void play() {
        String videoId = getYouTubeManager().getIterator().get();
        Platform.runLater(() -> {
            if (!videoId.isEmpty()& !getYouTubeManager().loading) {
                setStatus(ScreenStatus.LOADING);
                String url = "https://www.youtube.com/embed/" + videoId
                        + "?"
                        + "autoplay=1&"
                        + "controls=0&"
                        + "showinfo=0&"
                        + "modestbranding=1"
                        + "&iv_load_policy=3";
                LOGGER.info("Opening URL: " + url);
                getWebView().getEngine().load(url);
                setStatus(ScreenStatus.PLAYING);
                addHold();
                this.toBack();
            } else {
                LOGGER.warn("Attempting to start video without an ID");
            }
        });
    }

    /**
     *
     */
    @Override
    public void end() {
        setStatus(ScreenStatus.ENDING);
        getWebView().getEngine().load(null);
        removeFromParent();
        setStatus(ScreenStatus.CLOSED);
    }

    /**
     *
     */
    @Override
    public void reload() {
        setStatus(ScreenStatus.READY);
        getYouTubeManager().getPlaylistLoader().setPlaylistID(getPlaylistId());
        getYouTubeManager().setShuffle(toShuffle());
        setStatus(ScreenStatus.CLOSED);
        Thread thread = new Thread(getYouTubeManager());
        thread.start();
    }

    @Override
    public Preferences getPreferences() {
        //    if (preferences == null) {
        String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
        Class<? extends YouTubeViewer> aClass = this.getClass();
        preferences = Preferences.userNodeForPackage(aClass).node(name);
        //  }
        return preferences;
    }

    private YouTubeManager manager;

    /**
     *
     * @return
     */
    public YouTubeManager getYouTubeManager() {
        if (manager == null) {
            manager = new YouTubeManager();
        }

        return manager;
    }

    private StringProperty playlistId;

    /**
     *
     * @param value
     */
    public void setPlaylistId(String value) {
        playlistIdProperty().set(value);
        getPreferences().put("playlistId", value);
    }

    /**
     *
     * @return
     */
    public String getPlaylistId() {
        return playlistIdProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty playlistIdProperty() {
        //   if (playlistId == null) {
        playlistId = new SimpleStringProperty(getPreferences().get("playlistId", "PLeyJPHbRnGabI8FmV3mJAGI8CpqYGj0Pf"));
        // }
        return playlistId;
    }

    /**
     *
     */
    public BooleanProperty shuffle;

    /**
     *
     * @param value
     */
    public void setShuffle(Boolean value) {
        shuffleProperty().setValue(value);
        getPreferences().put("shuffle", value.toString());
        LOGGER.info("Shuffle set to: " + value);
    }

    /**
     *
     * @return
     */
    public Boolean toShuffle() {
        return shuffleProperty().getValue();
    }

    /**
     *
     * @return
     */
    public BooleanProperty shuffleProperty() {
        //  if (shuffle == null) {
        shuffle = new SimpleBooleanProperty(Boolean.valueOf(getPreferences().get("shuffle", "false")));
        //}
        return shuffle;
    }

}
