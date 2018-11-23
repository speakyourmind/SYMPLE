/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.music;

import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import org.symfound.comm.file.PathReader;
import org.symfound.controls.AppableControl;
import org.symfound.main.settings.SettingsController;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public abstract class MusicButton extends AppableControl {

    private static final String NAME = MusicButton.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);
    private MusicControl control;

    public MusicButton(String CSSClass, String key, String title, String index, MusicControl control) {
        super(CSSClass, key, title, index);
        this.control = control;
        initialize();
    }

    private void initialize() {
        configureAlbumArt();
        SettingsController.updatedProperty().addListener((observeableValue, oldValue, newValue) -> {
            if (newValue) {
                configureAlbumArt();
            }
        });

    }

    public void configureAlbumArt() {
        if (showAlbumArt()) {
            addToPane(getAlbumArt());
            getAlbumArt().toBack();
            getPrimaryControl().getStyleClass().clear();
            setCSS("transparent-" + getMusicControl().toString().toLowerCase(), getPrimaryControl());
        } else {
            getAlbumArt().removeFromParent();
            setCSS("media-" + getMusicControl().toString().toLowerCase(), getPrimaryControl());
        }
    }

    private static MusicPlayer musicView;

    public static MusicPlayer getMusicView() {
        if (musicView == null) {
            musicView = new MusicPlayer();
        }
        return musicView;
    }

    private AlbumArt albumArt;

    public AlbumArt getAlbumArt() {
        if (albumArt == null) {
            albumArt = new AlbumArt();
        }
        return albumArt;
    }

    private ListProperty<String> contents;

    /**
     *
     * @param value
     */
    public void setContents(List<String> value) {
        ObservableList<String> list = FXCollections.observableArrayList(value);
        LOGGER.info("Contents are " + list);
        contentProperty().setValue(list);
    }

    /**
     *
     * @param folder
     */
    public void setContents(String folder) {
        PathReader pathReader = new PathReader(folder);
        List<String> folderFilePaths = pathReader.getFolderFilePaths();
        setContents(folderFilePaths);
    }

    /**
     *
     * @return
     */
    public List<String> getContents() {
        return contentProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ListProperty<String> contentProperty() {
        if (contents == null) {
            contents = new SimpleListProperty<>();
        }
        return contents;
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
        getMusicView().getPreferences().put("folder", value);
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
        if (folderPath == null) {
            String homeFolder = getUser().getContent().getHomeFolder();
            String path = homeFolder + "/music/";
            folderPath = new SimpleStringProperty(getMusicView().getPreferences().get("folder", path));
        }
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
        if (shuffle == null) {
            shuffle = new SimpleBooleanProperty(Boolean.valueOf(getPreferences().get("shuffle", "true")));
        }
        return shuffle;
    }

    private BooleanProperty showAlbumArt;

    /**
     *
     * @param value
     */
    public void setShowAlbumArt(Boolean value) {
        showAlbumArtProperty().set(value);
        getPreferences().put("showAlbumArt", value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean showAlbumArt() {
        return showAlbumArtProperty().get();
    }

    /**
     *
     * @return
     */
    public BooleanProperty showAlbumArtProperty() {
        if (showAlbumArt == null) {
            showAlbumArt = new SimpleBooleanProperty(Boolean.valueOf(getPreferences().get("showAlbumArt", "false")));
        }
        return showAlbumArt;
    }

    private BooleanProperty showProgress;

    /**
     *
     * @param value
     */
    public void setShowProgress(Boolean value) {
        showProgressProperty().set(value);
        getPreferences().put("showProgress", value.toString());
    }

    /**
     *
     * @return
     */
    public Boolean showProgress() {
        return showProgressProperty().get();
    }

    /**
     *
     * @return
     */
    public BooleanProperty showProgressProperty() {
        if (showProgress == null) {
            showProgress = new SimpleBooleanProperty(Boolean.valueOf(getPreferences().get("showProgress", "true")));
        }
        return showProgress;
    }

    public static final MusicControl DEFAULT_MUSIC_CONTROL = MusicControl.NEXT;
    private ObjectProperty<MusicControl> musicControl;

    /**
     *
     * @param value
     */
    public void setMusicControl(MusicControl value) {
        musicControlProperty().setValue(value);
        getPreferences().put("musicControl", value.toString());
    }

    /**
     *
     * @return
     */
    public MusicControl getMusicControl() {
        return musicControlProperty().getValue();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<MusicControl> musicControlProperty() {
        if (musicControl == null) {
            musicControl = new SimpleObjectProperty(MusicControl.valueOf(getPreferences().get("musicControl", control.toString())));
        }
        return musicControl;
    }

}
