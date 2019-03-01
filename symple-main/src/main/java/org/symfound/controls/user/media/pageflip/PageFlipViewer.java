/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.pageflip;

import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import org.apache.log4j.Logger;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.AnimatedLabel;
import org.symfound.controls.user.ScreenStatus;
import org.symfound.controls.user.media.web.WebViewer;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public class PageFlipViewer extends WebViewer {

    private static final String NAME = PageFlipViewer.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "PageFlip Viewer";

    /**
     *
     * @param index
     */
    public PageFlipViewer(String index) {
        super(KEY, index);
    }

    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton("");

        addToPane(getWebView());
        addToPane(getLinkTitle());
    }

    private AnimatedLabel linkTitle;

    private AnimatedLabel getLinkTitle() {
        if (linkTitle == null) {
            linkTitle = new AnimatedLabel();
            linkTitle.setAlignment(Pos.BOTTOM_CENTER);
            linkTitle.setStyle("-fx-font-size:38pt;");
        }
        return linkTitle;
    }

    /**
     *
     */
    @Override
    public void configure() {
        getPageFlipManager().getIterator().modeProperty().addListener((observable, oldValue, newValue) -> {
            play();
        });

        if (getIndex().contains("next")) {
            reload();
        }
        indexProperty().addListener((observable1, oldValue1, newValue1) -> {
            if (newValue1.contains("next")) {
                reload();
            }
        });
    }

    /**
     *
     */
    @Override
    public void play() {
        Platform.runLater(() -> {
            setStatus(ScreenStatus.LOADING);
            String url = getPageFlipManager().getIterator().get();
            if (url != null) {
                LOGGER.info("Opening URL: " + url);
                getWebView().getEngine().load(url);
            }
            setStatus(ScreenStatus.PLAYING);
            addHold();
            this.toBack();
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
        setStatus(ScreenStatus.REQUESTED);
        //getPageFlipManager().setURL();
        getPageFlipManager().setShuffle(toShuffle());
        Thread thread = new Thread(getPageFlipManager());
        thread.start();
    }

    private PageFlipManager manager;

    /**
     *
     * @return
     */
    public PageFlipManager getPageFlipManager() {
        if (manager == null) {
            manager = new PageFlipManager();
        }

        return manager;
    }

    @Override
    public Preferences getPreferences() {
        // if (preferences == null) {
        String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
        Class<? extends PageFlipViewer> aClass = this.getClass();
        preferences = Preferences.userNodeForPackage(aClass).node(name);
        //}
        return preferences;
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
