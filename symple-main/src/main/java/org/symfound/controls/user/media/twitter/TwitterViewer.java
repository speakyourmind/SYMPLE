/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.twitter;

import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import org.apache.log4j.Logger;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.BuildableGrid;
import static org.symfound.controls.user.RootPane.DEFAULT_PREF_HEIGHT;
import static org.symfound.controls.user.RootPane.DEFAULT_PREF_WIDTH;
import org.symfound.controls.user.ScreenStatus;
import org.symfound.controls.user.media.web.WebViewer;
import org.symfound.social.twitter.Tweet;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public class TwitterViewer extends WebViewer {

    private static final String NAME = TwitterViewer.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Twitter Viewer";

    /**
     *
     * @param index
     */
    public TwitterViewer(String index) {
        super(KEY, index);
    }

    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton("");
        BuildableGrid grid = new BuildableGrid();
        grid.setHgap(BuildableGrid.DEFAULT_GRID_GAP);
        grid.setVgap(BuildableGrid.DEFAULT_GRID_GAP);
        grid.setSpecRows(3);
        grid.setSpecColumns(3);
        grid.build();
        grid.setStyle("-fx-background-color:white; "
                + "    -fx-border-color: -fx-dark;\n"
                + "    -fx-border-insets: -3;\n"
                + "    -fx-border-width: 5;");
        GridPane.setConstraints(getWebView(), 0, 1, 3, 2);
        grid.getChildren().add(getWebView());
        GridPane.setConstraints(getLinkTitle(), 1, 0, 2, 1);
        grid.getChildren().add(getLinkTitle());
        GridPane.setConstraints(getProfilePicView(), 0, 0, 1, 1);
        grid.getChildren().add(getProfilePicView());
        addToPane(grid);
    }

    private TextArea tweetText;

    private TextArea getLinkTitle() {
        if (tweetText == null) {
            tweetText = new TextArea();
            //linkTitle.setAlignment(Pos.BOTTOM_CENTER);
            
            tweetText.setStyle("-fx-font-size:24pt;-fx-background-color:white;");
            tweetText.setWrapText(Boolean.TRUE);
        }
        return tweetText;
    }

    /**
     *
     */
    public WebView profilePicView;

    /**
     *
     * @return
     */
    public WebView getProfilePicView() {
        if (profilePicView == null) {
            profilePicView = new WebView();
            profilePicView.setPrefSize(DEFAULT_PREF_WIDTH, DEFAULT_PREF_HEIGHT);
            profilePicView.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

        }
        return profilePicView;
    }

    /**
     *
     */
    @Override
    public void configure() {

        if (getIndex().contains("next")) {
            reload();
        }
        indexProperty().addListener((observable1, oldValue1, newValue1) -> {
            if (newValue1.contains("next")) {
                reload();
            }
        });

        getTwitterManager().getIterator().modeProperty().addListener((observable, oldValue, newValue) -> {
            play();
        });

    }

    /**
     *
     */
    @Override
    public void play() {
        Platform.runLater(() -> {
            setStatus(ScreenStatus.LOADING);
            Tweet tweet = getTwitterManager().getIterator().get();
            getProfilePicView().getEngine().load(tweet.getProfileImageURL());

            if (!tweet.getMediaURL().isEmpty()) {
                getWebView().getEngine().load(tweet.getMediaURL());

            } else {
                LOGGER.info("No media found on this tweet");
                if (!tweet.getURL().isEmpty()) {
                    getWebView().getEngine().load(tweet.getURL());
                } else {
                    // getWebView().getEngine().load(null);
                }
            }
            getLinkTitle().setText(tweet.getText());
            getLinkTitle().toFront();
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
        getTwitterManager().setShuffle(toShuffle());
        Thread thread = new Thread(getTwitterManager());
        thread.start();
    }

    private TwitterManager manager;

    /**
     *
     * @return
     */
    public TwitterManager getTwitterManager() {
        if (manager == null) {
            manager = new TwitterManager();
        }

        return manager;
    }

    @Override
    public Preferences getPreferences() {
        // if (preferences == null) {
        String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
        Class<? extends TwitterViewer> aClass = this.getClass();
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
