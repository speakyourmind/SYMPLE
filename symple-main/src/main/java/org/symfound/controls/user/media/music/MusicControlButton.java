/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.music;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.DirectoryChooser;
import org.apache.log4j.Logger;
import org.symfound.audio.music.song.Song;
import org.symfound.audio.music.song.SongFileAnalyzer;
import org.symfound.comm.file.PathReader;
import org.symfound.controls.RunnableControl;
import org.symfound.controls.ScreenControl;
import static org.symfound.controls.ScreenControl.CSS_PATH;
import org.symfound.controls.system.OnOffButton;
import org.symfound.controls.system.SettingsRow;
import static org.symfound.controls.system.dialog.EditDialog.createSettingRow;
import org.symfound.controls.system.dialog.SettingsDialog;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.main.HomeController;

/**
 *
 * @author Javed
 */
public class MusicControlButton extends MusicButton {

    private static final String NAME = MusicControlButton.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String KEY = "Music Control";

    /**
     *
     * @param index
     */
    public MusicControlButton(String index) {
        super("music-app-button", KEY, index, index, DEFAULT_MUSIC_CONTROL);
        initialize();
    }

    /**
     *
     * @param CSSClass
     * @param key
     * @param title
     * @param index
     * @param control
     */
    public MusicControlButton(String CSSClass, String key, String title, String index, MusicControl control) {
        super(CSSClass, key, title, index, control);
        initialize();
    }

    private void initialize() {
        configureMusicView();
        folderPathProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.info("Folder updated to " + newValue);
            if (getMusicView().getMediaPlayer() != null) {
                if (getMusicView().getMediaPlayer().getStatus().equals(Status.PLAYING)) {
                    getMusicView().stop();
                }
            }
            configureMusicView();
        });
        configureSongUpdate();
        getMusicView().getPlaylistManager().currentSongProperty().addListener((observable1, oldValue1, newValue1) -> {
            configureSongUpdate();
        });

        addToPane(getMusicView());
        configureTitle();

        addToPane(getProgressBar(), null, 0.0, 0.0, 0.0);
        getPrimaryControl().setStyle(getOverrideStyle());
        overrideStyleProperty().addListener((obversable, oldValue, newValue) -> {
            getPrimaryControl().setStyle(newValue);
        });
        setCSS("media-" + getMusicControl().toString().toLowerCase(), getPrimaryControl());
        musicControlProperty().addListener((observeableValue, oldValue, newValue) -> {
            setCSS("media-" + getMusicControl().toString().toLowerCase(), getPrimaryControl());
        });

        /*    getPrimaryControl().setStyle(getOverrideStyle());
        overrideStyleProperty().addListener((obversable, oldValue, newValue) -> {
            getPrimaryControl().setStyle(newValue);
        });*/
        getPrimaryControl().toFront();
    }

    /**
     *
     */
    public void configureSongUpdate() {
        if (getMusicView().getPlaylistManager().getCurrentSong() != null) {
            setSongInfo(getMusicView().getPlaylistManager().getCurrentSong());
            getPrimaryControl().toFront();
            getProgressBar().toFront();
        }
    }

    /**
     *
     */
    public void shuffleContents() {
        if (toShuffle()) {
            getMusicView().getPlaylistManager().shuffle();
        }
    }

    /**
     *
     */
    public void configureMusicView() {
        PathReader pathReader = new PathReader(getFolderPath());
        List<String> folderFilePaths = pathReader.getFolderFilePaths();
        Boolean validFolder = Boolean.FALSE;
        for (String path : folderFilePaths) {
            SongFileAnalyzer analyze = new SongFileAnalyzer(path);
            if (analyze.isPlayable()) {
                LOGGER.info("At least one playable file found: " + path);
                validFolder = Boolean.TRUE;
                break;
            }
        }
        if (validFolder) {
            setContents(folderFilePaths);
            getMusicView().getPlaylistManager().setPlaylist(getContents());
            shuffleContents();
        } else {
            String message = "Uh-oh! Items in the folder " 
                    + getFolderPath() + "are not playable music files.";
            LOGGER.fatal(message);
            
            generateFixableError(message);
        }

    }

    @Override
    public void run() {
        MusicPlayer view = getMusicView();
        switch (getMusicControl()) {
            case NEXT:
                LOGGER.info("Playing next song");
                view.next(Boolean.FALSE);
                break;
            case PREVIOUS:
                LOGGER.info("Playing previous song");
                view.previous(Boolean.FALSE);
                break;
            case TOGGLE:
                if (view.getMediaPlayer() != null) {
                    if (view.getMediaPlayer().getStatus().equals(Status.PLAYING)) {
                        view.stop();
                    } else {
                        view.next(Boolean.FALSE);
                    }
                } else {
                    view.next(Boolean.FALSE);
                }
                break;
            case STOP:
                if (view.getMediaPlayer() != null) {
                    if (view.getMediaPlayer().getStatus().equals(Status.PLAYING)) {
                        view.stop();
                    }
                }
                break;
            case INFO:
                LOGGER.info("Playing next song");
                view.next(Boolean.FALSE);
                break;
            default:
                break;
        }
    }

    private TextField folderName;
    private ChoiceBox<MusicControl> musicControlType;
    private OnOffButton showProgressButton;
    private OnOffButton showAlbumArtButton;
    private OnOffButton shuffleButton;

    /**
     *
     */
    @Override
    public void setAppableSettings() {
        setFolderPath(folderName.getText());
        setMusicControl(musicControlType.getValue());
        setShuffle(shuffleButton.getValue());
        setShowProgress(showProgressButton.getValue());
        setShowAlbumArt(showAlbumArtButton.getValue());
        super.setAppableSettings();
    }

    /**
     *
     */
    @Override
    public void resetAppableSettings() {
        folderName.setText(getFolderPath());
        musicControlType.setValue(getMusicControl());
        shuffleButton.setValue(toShuffle());
        showProgressButton.setValue(showProgress());
        showAlbumArtButton.setValue(showAlbumArt());
        super.resetAppableSettings();
    }

    /**
     *
     * @return
     */
    @Override
    public List<Tab> addAppableSettings() {

        SettingsRow settingsRowA = createSettingRow("Folder", "Location of the files");
        folderName = new TextField();
        folderName.setText(getFolderPath());
        folderName.maxHeight(80.0);
        folderName.maxWidth(360.0);
        folderName.setMaxSize(360.0, 80.0);
        folderName.getStyleClass().add("settings-text-area");
        settingsRowA.add(folderName, 2, 0);

        RunnableControl readFileChooser = new RunnableControl() {
            @Override
            public void run() {
                DirectoryChooser fileChooser = new DirectoryChooser();
                fileChooser.setTitle("Import Settings Source");
                // fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON files", "*.json"));
                File selectedFile = fileChooser.showDialog(getPrimaryControl().getParentUI());
                folderName.setText(selectedFile.getAbsolutePath());
            }
        };
        readFileChooser.setControlType(ScreenControl.ControlType.SETTING_CONTROL);
        readFileChooser.setSymStyle("settings-button");
        readFileChooser.setText("LOAD");
        readFileChooser.setMaxSize(SettingsDialog.MAX_WIDTH, SettingsDialog.MAX_HEIGHT);
        //  readFilePathGrid.add(readFileChooser, 0, 0);
        settingsRowA.add(readFileChooser, 1, 0);

        SettingsRow settingsRowB = createSettingRow("Control Type", "Next or Previous or Toggle");
        musicControlType = new ChoiceBox<>(FXCollections.observableArrayList(Arrays.asList(
                MusicControl.NEXT,
                MusicControl.PREVIOUS,
                MusicControl.TOGGLE,
                MusicControl.STOP
        )));
        musicControlType.setValue(getMusicControl());
        musicControlType.maxHeight(80.0);
        musicControlType.maxWidth(360.0);
        musicControlType.getStyleClass().add("settings-text-area");
        settingsRowB.add(musicControlType, 1, 0, 2, 1);

        SettingsRow settingsRowC = createSettingRow("Shuffle", "Shuffle order of playlist");

        shuffleButton = new OnOffButton("ENABLED", "DISABLED");
        shuffleButton.setMaxSize(180.0, 60.0);
        shuffleButton.setValue(toShuffle());
        GridPane.setHalignment(shuffleButton, HPos.LEFT);
        GridPane.setValignment(shuffleButton, VPos.CENTER);
        settingsRowC.add(shuffleButton, 1, 0, 1, 1);

        SettingsRow settingsRowD = createSettingRow("Show Progress", "Show or hide the progress bar on this button");

        showProgressButton = new OnOffButton("SHOW", "HIDE");
        showProgressButton.setMaxSize(180.0, 60.0);
        showProgressButton.setValue(showProgress());
        GridPane.setHalignment(showProgressButton, HPos.LEFT);
        GridPane.setValignment(showProgressButton, VPos.CENTER);
        settingsRowD.add(showProgressButton, 1, 0, 1, 1);

        SettingsRow settingsRowE = createSettingRow("Show Album Art", "Show or hide the album art on this button");

        showAlbumArtButton = new OnOffButton("SHOW", "HIDE");
        showAlbumArtButton.setMaxSize(180.0, 60.0);
        showAlbumArtButton.setValue(showAlbumArt());
        GridPane.setHalignment(showAlbumArtButton, HPos.LEFT);
        GridPane.setValignment(showAlbumArtButton, VPos.CENTER);
        settingsRowE.add(showAlbumArtButton, 1, 0, 1, 1);

        actionSettings.add(settingsRowA);
        actionSettings.add(settingsRowB);
        actionSettings.add(settingsRowC);
        actionSettings.add(settingsRowD);
        actionSettings.add(settingsRowE);
        List<Tab> tabs = super.addAppableSettings();

        return tabs;
    }

    private ProgressBar progressBar;

    /**
     *
     * @return
     */
    public ProgressBar getProgressBar() {
        if (progressBar == null) {
            progressBar = new ProgressBar();

            progressBar.getStylesheets().add(CSS_PATH);
            progressBar.getStyleClass().add("music-progress");

            progressBar.setMinWidth(getPrimaryControl().getWidth());

            getMusicView().mediaPlayerProperty().addListener((observable2, oldValue2, newValue2) -> {
                newValue2.currentTimeProperty().addListener((observable3, oldValue3, newValue3) -> {
                    Double currentTime = newValue2.getCurrentTime().toMillis();
                    Double totalTime = newValue2.getTotalDuration().toMillis();
                    progressBar.setProgress(currentTime / totalTime);
                });
            });
            progressBar.setVisible(showProgress());
            showProgressProperty().addListener((observable4, oldValue4, newValue4) -> {
                progressBar.setVisible(newValue4);
            });

        }
        return progressBar;
    }

    /**
     *
     * @param song
     */
    public void setSongInfo(Song song) {
        if (song != null) {
            final String title = song.getTitle();
            final String artist = song.getArtist();
            LOGGER.info("Updating song information and album art: " + title + "-" + artist);
            setTitle(title + "-" + artist);
            if (getAlbumArt() != null) {
                getAlbumArt().set(song);
            }
        } else {
            LOGGER.warn("Song is null");
        }
    }

    @Override
    public Preferences getPreferences() {
        if (preferences == null) {
            String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
            Class<? extends MusicControlButton> aClass = this.getClass();
            preferences = Preferences.userNodeForPackage(aClass).node(name);
        }
        return preferences;
    }

    /**
     *
     */
    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton("") {
            @Override
            public void revert(String originalText, String originalStyle) {
                //Original Size X
                setScaleX(1);
                //Original Size Y
                setScaleY(1);
                if (originalText != null) {
                    setSongInfo(getMusicView().getPlaylistManager().getCurrentSong());
                }
                if (originalStyle != null) {
                    //Original Style
                    setStyle(originalStyle);
                } else {
                    setStyle("");
                }
                getSession().setMutex(false);
            }

        };
        primary.setWrapText(true);
        load(primary);
        setSelection(primary);
    }

}
