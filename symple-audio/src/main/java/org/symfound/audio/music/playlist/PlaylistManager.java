/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.audio.music.playlist;

import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.symfound.audio.music.song.Song;
import org.symfound.tools.iteration.ModeIterator;

/**
 *
 * @author Javed Gangjee
 */
public class PlaylistManager {

    /**
     *
     * @return
     */
    public Song getNextSong() {
        Song song;
        do {
            getPlaylist().next();
            String songLocation = getPlaylist().get();
            song = new Song(songLocation);
        } while (!song.getSongFileAnalyzer().isPlayable());
        return song;
    }

    /**
     *
     * @return
     */
    public Song getPreviousSong() {
        Song song;
        do {
            getPlaylist().previous();
            String songLocation = getPlaylist().get();
            song = new Song(songLocation);
        } while (!song.getSongFileAnalyzer().isPlayable());
        return song;
    }

    /**
     *
     */
    public void shuffle() {
        getPlaylist().shuffle();
    }

    private ObjectProperty<Song> currentSong;

    /**
     *
     * @param value
     */
    public void setCurrentSong(Song value) {
        currentSongProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public Song getCurrentSong() {
        return currentSongProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<Song> currentSongProperty() {
        if (currentSong == null) {
            currentSong = new SimpleObjectProperty<>();
        }

        return currentSong;
    }

    private ObjectProperty<ModeIterator<String>> playlist;

    /**
     *
     * @param contents
     */
    public void setPlaylist(List<String> contents) {
        setPlaylist(new ModeIterator<>(contents));
    }

    /**
     *
     * @param value
     */
    public void setPlaylist(ModeIterator<String> value) {
        playlistProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public ModeIterator<String> getPlaylist() {
        return playlistProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<ModeIterator<String>> playlistProperty() {
        if (playlist == null) {
            playlist = new SimpleObjectProperty<>();
        }

        return playlist;
    }
}
