/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.music;

import java.util.prefs.Preferences;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.log4j.Logger;
import org.symfound.audio.music.PlaylistManager;
import org.symfound.audio.music.song.Song;
import org.symfound.builder.user.characteristic.Interaction;
import org.symfound.controls.user.AnimatedMediaView;
import org.symfound.main.Main;

/**
 *
 * @author Javed Gangjee
 */
public class MusicPlayer extends AnimatedMediaView {

    private static final String NAME = MusicPlayer.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public MusicPlayer() {
        super();
    }

    /**
     *
     */
    @Override
    public void stop() {
        if (getMediaPlayer() != null) {
            LOGGER.info("Stopping Music Player");
            getMediaPlayer().stop();

        }
    }

    /**
     *
     * @param autoNext
     */
    @Override
    public void next(Boolean autoNext) {
        if (!getHold().isAdded()) {
            stop();
            Song song = getPlaylistManager().getNextSong();
            LOGGER.info("Stopping Music Player");
            play(song, autoNext);
        }
    }

    /**
     *
     * @param autoNext
     */
    @Override
    public void previous(Boolean autoNext) {
        if (!getHold().isAdded()) {
            stop();
            Song song = getPlaylistManager().getPreviousSong();
            play(song, autoNext);
        }

    }

    /**
     *
     * @param song
     * @param autoNext
     */
    public void play(Song song, Boolean autoNext) {
        getPlaylistManager().setCurrentSong(song);
        // log(song);
        play(autoNext);
    }

    /**
     *
     * @param autoNext
     */
    @Override
    public void play(Boolean autoNext) {
        addHold();
        String currentSong = getPlaylistManager().getCurrentSong().getFile().toURI().toString();
        LOGGER.info("Loading current song file: " + currentSong);
        Media media = new Media(currentSong);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        setMediaPlayer(mediaPlayer);
        Interaction interaction = Main.getSession().getUser().getInteraction();
        getMediaPlayer().setVolume(interaction.getVolume());
        getMediaPlayer().volumeProperty().bindBidirectional(interaction.volumeProperty());
        getMediaPlayer().setOnReady(() -> {
            LOGGER.info("Playing song file: " + currentSong);
            getMediaPlayer().play();
        });

        setVisible(true);
        if (autoNext) {
            getMediaPlayer().setOnEndOfMedia(() -> {
                next(autoNext);
                toBack();
            });
        }

    }
    
    private PlaylistManager playlistManager;

    /**
     *
     * @return
     */
    public PlaylistManager getPlaylistManager() {
        if (playlistManager == null) {
            playlistManager = new PlaylistManager();
        }
        return playlistManager;
    }

    private Preferences preferences;

    /**
     *
     * @return
     */
    public final Preferences getPreferences() {
        if (preferences == null) {
            preferences = Preferences.userNodeForPackage(this.getClass());
        }
        return preferences;
    }

}
