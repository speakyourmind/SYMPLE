/*
 * Copyright (C) 2015 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.symfound.audio.music.song;

import com.mpatric.mp3agic.ID3v1; //Give credit
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.IOException;
import java.io.RandomAccessFile;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.apache.log4j.Logger;
import org.symfound.comm.file.ExtensionAnalyzer;
import org.symfound.comm.file.PathReader;
import org.symfound.voice.player.AudioPlayer;

/**
 *
 * @author Javed Gangjee
 */
public final class Song extends PathReader {

    private static final Logger LOGGER = Logger.getLogger(Song.class.getName());

    private AudioPlayer audioPlayer;

    private String albumArtPath;
    private ID3v2 id3v2Tag;
    private ID3v1 id3v1Tag;

    /**
     *
     * @param path
     */
    public Song(String path) {
        super(path);
        initialize();
    }

    private void initialize() {
        if (getSongFileAnalyzer().isMP3()) {
            try {
                Mp3File mp3file = new Mp3File(getFile());

                if (mp3file.hasId3v2Tag()) {
                    id3v2Tag = mp3file.getId3v2Tag();

                } else if (mp3file.hasId3v1Tag()) {
                    id3v1Tag = mp3file.getId3v1Tag();
                }
            } catch (IOException | UnsupportedTagException | InvalidDataException ex) {
                LOGGER.fatal(null, ex);
            }
        } else if (getSongFileAnalyzer().isWAV()) {
            try {
                audioPlayer.playAudioFile(getFile());
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException ex) {
                LOGGER.fatal(null, ex);
            }
        }
    }

    private SongFileAnalyzer songFileAnalyzer;

    /**
     *
     * @return
     */
    public SongFileAnalyzer getSongFileAnalyzer() {
        if (songFileAnalyzer == null) {
            songFileAnalyzer = new SongFileAnalyzer(getPath());
        }
        return songFileAnalyzer;

    }
    private StringProperty title;

    /**
     *
     * @param value
     */
    public void setTitle(String value) {
        titleProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        if (id3v2Tag != null) {
            setTitle(id3v2Tag.getTitle());
        } else if (id3v1Tag != null) {
            setTitle(id3v1Tag.getTitle());
        }
        return titleProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty titleProperty() {
        if (title == null) {
            title = new SimpleStringProperty("");
        }
        return title;
    }

    private StringProperty artist;

    /**
     *
     * @param value
     */
    public void setArtist(String value) {
        artistProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getArtist() {
        if (id3v2Tag != null) {
            setArtist(id3v2Tag.getArtist());
        } else if (id3v1Tag != null) {
            setArtist(id3v1Tag.getArtist());
        }
        return artistProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty artistProperty() {
        if (artist == null) {
            artist = new SimpleStringProperty("");
        }
        return artist;
    }

    private StringProperty album;

    /**
     *
     * @param value
     */
    public void setAlbum(String value) {
        albumProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getAlbum() {
        if (id3v2Tag != null) {
            setAlbum(id3v2Tag.getAlbum());
        } else if (id3v1Tag != null) {
            setAlbum(id3v1Tag.getAlbum());
        }
        return albumProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty albumProperty() {
        if (album == null) {
            album = new SimpleStringProperty("");
        }
        return album;
    }

    /**
     *
     * @param repeat
     * @param position
     * @param size
     * @return
     * @throws IOException
     */
    public String getAlbumArtStyle(String repeat, String position, String size) throws IOException {
        String albumArtStyle;
        if (id3v2Tag != null) {
            byte[] imageData = id3v2Tag.getAlbumImage();

            if (imageData != null) {
                String mimeType = id3v2Tag.getAlbumImageMimeType();
                try (
                        // Write image to file - can determine appropriate file extension from the mime type
                        RandomAccessFile ra = new RandomAccessFile("album-artwork", "rw")) {
                    ra.write(imageData);
                }
                String playPath = getPath().subSequence(0, getPath().lastIndexOf("\\")).toString();

                PathReader songFolder = new PathReader(playPath);
                songFolder.getFolderFilePaths().stream().forEach((artCheck) -> {
                    ExtensionAnalyzer ea = new ExtensionAnalyzer(artCheck);
                    if (ea.isPictureFile()) {
                        artCheck = artCheck.replaceAll("\\\\", "/").replaceAll(" ", "%20");
                        albumArtPath = "file:/" + artCheck;
                    }
                });
            }

            albumArtStyle = "-fx-background color:-fx-dark;"
                    + "-fx-background-image: url(\"" + albumArtPath + "\");"
                    + "-fx-background-size: " + size + ";\n"
                    + "-fx-background-repeat: " + repeat + ";\n"
                    + "-fx-background-position: " + position + "";
            
        } else {
            LOGGER.info("No album art available");
            albumArtStyle = "-fx-background color:-fx-light;"
                    + "-fx-background-size: " + size + ";\n"
                    + "-fx-background-repeat: " + repeat + ";\n"
                    + "-fx-background-position: " + position + "";
        }
        return albumArtStyle;

    }
}
