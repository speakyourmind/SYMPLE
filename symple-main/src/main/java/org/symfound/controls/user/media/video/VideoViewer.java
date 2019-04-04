/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.video;

import java.io.File;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.apache.log4j.Logger;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.ScreenStatus;
import org.symfound.controls.user.media.MediaViewer;
import org.symfound.main.Main;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public class VideoViewer extends MediaViewer {

    private static final String NAME = VideoViewer.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Video Viewer";

    /**
     *
     * @param index
     */
    public VideoViewer(String index) {
        super("transparent", KEY, "", index);
        initialize();
    }

    private void initialize() {
        configure();
    }

    /**
     *
     */
    public void configure() {

        getVideoManager().getIterator().modeProperty().addListener((observable, oldValue, newValue) -> {
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

    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton("");
        addToPane(getBorderPane());
    }

    /**
     *
     */
    @Override
    public void addConfigButtons() {
        /*addToPane(getKeyRemoveButton(), 20.0, null, null, 0.0);
        getKeyRemoveButton().toFront();*/
        addToPane(getEditAppButton(), null, null, getHeight() / 2, getWidth() / 2);
        getEditAppButton().toFront();
        getPrimaryControl().setDisable(true);
    }

    @Override
    public Preferences getPreferences() {
        String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
        Class<? extends VideoViewer> aClass = this.getClass();
        preferences = Preferences.userNodeForPackage(aClass).node(name);
        return preferences;
    }

    /**
     *
     */
    @Override
    public void play() {
        Platform.runLater(() -> {
            final MediaPlayer oldPlayer = getMediaView().getMediaPlayer();
            if (oldPlayer != null) {
                LOGGER.info("Stopping media player");
                oldPlayer.stop();
                oldPlayer.dispose();
                getMediaView().setMediaPlayer(null);
            }
            setStatus(ScreenStatus.LOADING);
            String location = getVideoManager().getIterator().get();
            LOGGER.info("Current file location is" + location);
            File file = new File(location);
            Media media = new Media(file.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);

            mediaPlayer.volumeProperty().bindBidirectional(Main.getSession().getUser().getInteraction().volumeProperty());

            Double startTime = 0.0;
            /*     if (randomizeStartTime()) {
                Double mediaDuration = media.durationProperty().getValue().toSeconds();
                Random r = new Random();
                startTime = DEFAULT_START_TIME + (mediaDuration - 0.0) * r.nextDouble();
            }*/
            mediaPlayer.setStartTime(Duration.seconds(startTime));
            /*   getMediaView().setTranslateX(0.0);
            if (getParent() instanceof Pane) {
                Pane parent = (Pane) getParent();
                double move = (parent.getWidth() - media.getWidth()) / 4;
                getMediaView().setTranslateX(move);
            }
            /*  if (autoNext) {
                    mediaPlayer.setOnEndOfMedia(() -> {
                        next(autoNext);
                    });
                }*/
            //

            getMediaView().setPreserveRatio(true);
            mediaPlayer.setOnReady(() -> {

                mediaPlayer.play();
                setStatus(ScreenStatus.PLAYING);
            });

            getMediaView().setMediaPlayer(mediaPlayer);
            LOGGER.info("Playing " + media.getSource());
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
        if (getMediaView().getMediaPlayer() != null) {
            getMediaView().getMediaPlayer().stop();
            getMediaView().setMediaPlayer(null);
        }
        removeFromParent();
        setStatus(ScreenStatus.CLOSED);
    }

    /**
     *
     */
    @Override
    public void reload() {
        setStatus(ScreenStatus.READY);
        getVideoManager().setFolder(getFolderPath());
        getVideoManager().setShuffle(toShuffle());
        setStatus(ScreenStatus.REQUESTED);
        Thread thread = new Thread(getVideoManager());
        thread.start();
    }

    private BorderPane borderPane;

    /**
     *
     * @return
     */
    public BorderPane getBorderPane() {
        if (borderPane == null) {
            borderPane = new BorderPane();
            borderPane.setStyle("-fx-background-color:-fx-dark;");
            borderPane.setCenter(getMediaView());
        }
        return borderPane;
    }

    private MediaView mediaView;

    /**
     *
     * @return
     */
    public MediaView getMediaView() {
        if (mediaView == null) {
            mediaView = new MediaView();
            mediaView.fitWidthProperty().bind(widthProperty());
            mediaView.fitHeightProperty().bind(heightProperty());
            mediaView.setMediaPlayer(null);
        }
        return mediaView;
    }

    private VideoManager manager;

    /**
     *
     * @return
     */
    public VideoManager getVideoManager() {
        if (manager == null) {
            manager = new VideoManager();
        }

        return manager;
    }

    /**
     *
     */
    public StringProperty folderPath;

    /**
     *
     * @param value
     */
    public void setFolderPath(String value) {
        folderPathProperty().setValue(value);
        getPreferences().put("folder", value);
        LOGGER.info("Folder set to: " + value);
    }

    /**
     *
     * @return
     */
    public String getFolderPath() {
        return folderPathProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty folderPathProperty() {
        //  if (folderPath == null) {
        folderPath = new SimpleStringProperty(getPreferences().get("folder", getUser().getContent().getHomeFolder() + "/videos/"));
        //}
        return folderPath;
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
