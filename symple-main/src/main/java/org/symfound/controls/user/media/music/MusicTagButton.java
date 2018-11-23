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
public class MusicTagButton extends MusicControlButton {

    private static final String NAME = MusicTagButton.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    public static final String KEY = "Music Info";

    public MusicTagButton(String index) {
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
            Class<? extends MusicTagButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

    private VBox vBox;

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

    @Override
    public void setSongInfo(Song song) {
        getSongNameLabel().setText(song.getTitle());
        getArtistNameLabel().setText(song.getArtist());
        getAlbumNameLabel().setText(song.getAlbum());LOGGER.info("Updating song information and album art: " + song.getTitle() + "-" + song.getArtist());
       if (getAlbumArt() != null) {
            getAlbumArt().set(song);
        }
    }

    private Label lblSong;

    public Label getSongNameLabel() {
        if (lblSong == null) {
            lblSong = new Label("Ready...");
            this.setCSS("music-song", lblSong);
         //   lblSong.setWrapText(true);
        }
        return lblSong;
    }
    private Label lblArtist;

    public Label getArtistNameLabel() {
        if (lblArtist == null) {
            lblArtist = new Label("");
            this.setCSS("music-artist", lblArtist);
         //   lblArtist.setWrapText(true);
        }
        return lblArtist;
    }

    private Label lblAlbum;

    public Label getAlbumNameLabel() {
        if (lblAlbum == null) {
            lblAlbum = new Label("");
            this.setCSS("music-album", lblAlbum);
          //  lblAlbum.setWrapText(true);
        }
        return lblAlbum;
    }

}
