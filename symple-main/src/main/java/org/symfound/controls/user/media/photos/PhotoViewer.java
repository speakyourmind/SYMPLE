/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.photos;

import java.io.File;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.apache.log4j.Logger;
import org.symfound.controls.user.AnimatedButton;
import org.symfound.controls.user.ScreenStatus;
import org.symfound.controls.user.media.MediaViewer;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public class PhotoViewer extends MediaViewer {

    private static final String NAME = PhotoViewer.class.getName();

    public static final Logger LOGGER = Logger.getLogger(NAME);
    public static final String KEY = "Photo Viewer";

    public PhotoViewer(String index) {
        super("transparent", KEY, "", index);
        initialize();
    }

    private void initialize() {
        configure();
    }

    public void configure() {

        getPhotoManager().getIterator().modeProperty().addListener((observable, oldValue, newValue) -> {
            play();
        });

        if (getIndex().contains("next")) {
            System.out.println("Index has changed to " + getIndex());
            reload();
        }
        indexProperty().addListener((observable1, oldValue1, newValue1) -> {
            if (newValue1.contains("next")) {
                System.out.println("Index has changed to " + newValue1);
                reload();
            }
        });
    }

    @Override
    public void reload() {
        setStatus(ScreenStatus.READY);
        getPhotoManager().setFolder(getFolderPath());
        getPhotoManager().setShuffle(toShuffle());
        setStatus(ScreenStatus.REQUESTED);
        Thread thread = new Thread(getPhotoManager());
        thread.start();
    }

    @Override
    public void play() {
        String path = getPhotoManager().getIterator().get();
        if (!path.isEmpty()) {
            setStatus(ScreenStatus.LOADING);
            LOGGER.info("setting image to " + getPhotoManager().getIterator().get());
            File file = new File(path);
            String imageLocation = file.toURI().toString();
            Image image = new Image(imageLocation);
            Platform.runLater(() -> {
                getPhotoView().setImage(image);
                //   getPhotoView().setRotate(90.0);
                setStatus(ScreenStatus.PLAYING);
                /*   getPhotoView().setStyle("");
            // Set the look of the target
             getPhotoView().setStyle("-fx-background-image: url(\"" + image + "\");"
                    + "-fx-background-size: contain;\n"
                    + "-fx-background-repeat: no-repeat;\n"
                    + "-fx-background-position: center;");*/
                addHold();
                toBack();
            });
        } else {
            LOGGER.warn("Path is empty");
        }
    }

    @Override
    public void end() {
        setStatus(ScreenStatus.ENDING);
        removeFromParent();
        setStatus(ScreenStatus.CLOSED);
    }

    @Override
    public void loadPrimaryControl() {
        primary = new AnimatedButton();
        addToPane(getBorderPane());
    }

    @Override
    public void addConfigButtons() {
      /*  addToPane(getKeyRemoveButton(), 20.0, null, null, 0.0);
        getKeyRemoveButton().toFront();*/
        addToPane(getEditAppButton(), null, null, getHeight() / 2, getWidth() / 2);
        getEditAppButton().toFront();
        getPrimaryControl().setDisable(true);
    }
    private PhotoManager manager;

    public PhotoManager getPhotoManager() {
        if (manager == null) {
            manager = new PhotoManager();
        }

        return manager;
    }
    private BorderPane borderPane;

    public BorderPane getBorderPane() {
        if (borderPane == null) {
            borderPane = new BorderPane();
            borderPane.setStyle("-fx-background-color:-fx-dark;");
            borderPane.setCenter(getPhotoView());
        }
        return borderPane;
    }

    private ImageView photoView;

    public ImageView getPhotoView() {
        if (photoView == null) {
            photoView = new ImageView();
            photoView.fitWidthProperty().bind(widthProperty());
            photoView.fitHeightProperty().bind(heightProperty());
            photoView.setPreserveRatio(true);

        }
        return photoView;
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
        folderPath = new SimpleStringProperty(getPreferences().get("folder", getUser().getContent().getHomeFolder() + "/pictures/"));
        //}
        return folderPath;
    }

    @Override
    public Preferences getPreferences() {
        //     if (preferences == null) {
        String name = KEY.toLowerCase() + "/" + getIndex().toLowerCase();
        Class<? extends PhotoViewer> aClass = this.getClass();
        preferences = Preferences.userNodeForPackage(aClass).node(name);
        //   }
        return preferences;
    }
}
