/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.reddit;

import java.util.Map;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
public class RedditViewer extends WebViewer {

    private static final String NAME = RedditViewer.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Reddit Viewer";

    /**
     *
     * @param index
     */
    public RedditViewer(String index) {
        super(KEY, index);
    }

    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton();

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
        getRedditManager().getIterator().modeProperty().addListener((observable, oldValue, newValue) -> {
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
            String url = getRedditManager().getIterator().get();
            final Map<String, String> links = getRedditManager().getLinks();
            if (links != null) {
                String title = links.get(url);
                /* if (webView.getParent() instanceof Pane) {
                Pane parent = (Pane) webView.getParent();
                parent.getChildren().remove(webView);
                webView = null;
                }
                addToPane(getWebView());*/
                LOGGER.info("Opening URL: " + url + " - " + title);
                getWebView().getEngine().load(url);
                getLinkTitle().setText(title);
                getLinkTitle().toFront();
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
        String url = "https://www.reddit.com/r/" + getSubreddit() + "/" + getSubList() + "/.rss?limit=1000";
        setStatus(ScreenStatus.REQUESTED);
        getRedditManager().setURL(url);
        getRedditManager().setShuffle(toShuffle());
        Thread thread = new Thread(getRedditManager());
        thread.start();
    }

    private RedditManager manager;

    /**
     *
     * @return
     */
    public RedditManager getRedditManager() {
        if (manager == null) {
            manager = new RedditManager();
        }

        return manager;
    }

    @Override
    public Preferences getPreferences() {
        // if (preferences == null) {
        String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
        Class<? extends RedditViewer> aClass = this.getClass();
        preferences = Preferences.userNodeForPackage(aClass).node(name);
        //}
        return preferences;
    }

    private static final String SUBREDDIT_KEY = "subreddit";
    private static final String DEFAULT_SUBREDDIT = "funny";
    private StringProperty subreddit;

    /**
     *
     * @param value
     */
    public void setSubreddit(String value) {
        subredditProperty().setValue(value);
        getPreferences().put("subreddit", value);
        LOGGER.info("Subreddit set to: " + value);

    }

    /**
     *
     * @return
     */
    public String getSubreddit() {
        return subredditProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty subredditProperty() {
        //   if (subreddit == null) {
        subreddit = new SimpleStringProperty(getPreferences().get(SUBREDDIT_KEY, DEFAULT_SUBREDDIT));
        // }
        return subreddit;
    }

    private StringProperty subList;

    /**
     *
     * @param value
     */
    public void setSubList(String value) {
        subListProperty().setValue(value);
        getPreferences().put("subList", value);
        LOGGER.info("SubList set to: " + value);

    }

    /**
     *
     * @return
     */
    public String getSubList() {
        return subListProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty subListProperty() {
        //   if (subList == null) {
        subList = new SimpleStringProperty(getPreferences().get("subList", "top"));
        // }
        return subList;
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
