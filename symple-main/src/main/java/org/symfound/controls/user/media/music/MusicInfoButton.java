/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.music;

import java.util.prefs.Preferences;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;
import org.symfound.audio.music.song.Song;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public class MusicInfoButton extends MusicControlButton {

    private static final String NAME = MusicInfoButton.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Music Info";

    /**
     *
     * @param index
     */
    public MusicInfoButton(String index) {
        super("transparent", KEY, "", index, MusicControl.INFO);
     }

    @Override
    public void loadPrimaryControl() {
        super.loadPrimaryControl();
        addToPane(getVBox(), null, 20.0, 20.0, 20.0);
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends MusicInfoButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

    private VBox vBox;

    /**
     *
     * @return
     */
    public VBox getVBox() {
        if (vBox == null) {
            vBox = new VBox();
            setCSS("transparent", vBox);
            vBox.getChildren().add(getSongNameLabel());
            vBox.getChildren().add(getArtistNameLabel());
            vBox.getChildren().add(getAlbumNameLabel());
        }
        return vBox;
    }

    /**
     *
     * @param song
     */
    @Override
    public void setSongInfo(Song song) {
        getSongNameLabel().setText(song.getTitle());
        getArtistNameLabel().setText(song.getArtist());
        getAlbumNameLabel().setText(song.getAlbum());
        LOGGER.info("Updating song information and album art: " + 
                song.getTitle() + "-" + song.getArtist());
       if (getAlbumArt() != null) {
            getAlbumArt().set(song);
        }
    }

    private Label songLabel;

    /**
     *
     * @return
     */
    public Label getSongNameLabel() {
        if (songLabel == null) {
            songLabel = new Label("Ready...");
            this.setCSS("music-song", songLabel);
         //   lblSong.setWrapText(true);
        }
        return songLabel;
    }
    private Label artistLabel;

    /**
     *
     * @return
     */
    public Label getArtistNameLabel() {
        if (artistLabel == null) {
            artistLabel = new Label("");
            this.setCSS("music-artist", artistLabel);
        }
        return artistLabel;
    }

    private Label albumLabel;

    /**
     *
     * @return
     */
    public Label getAlbumNameLabel() {
        if (albumLabel == null) {
            albumLabel = new Label("");
            this.setCSS("music-album", albumLabel);
          //  lblAlbum.setWrapText(true);
        }
        return albumLabel;
    }

}
