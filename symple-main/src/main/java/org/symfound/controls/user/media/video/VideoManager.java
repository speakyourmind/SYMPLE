/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.video;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;
import org.symfound.comm.file.ExtensionAnalyzer;
import org.symfound.comm.file.PathReader;
import org.symfound.controls.user.media.MediaManager;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public class VideoManager extends MediaManager<String> {

    private static final String NAME = VideoManager.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public VideoManager() {
        super(Arrays.asList(""));
    }

    @Override
    public void run() {
        PathReader pathReader = new PathReader(getFolder());
        List<String> folderFilePaths = pathReader.getFolderFilePaths();
        List<String> files = new ArrayList<>();
        folderFilePaths.forEach((location) -> {
            ExtensionAnalyzer locationAnalyzer = new ExtensionAnalyzer(location);
            if (locationAnalyzer.isVideoFile()) {
                files.add(location);
            }
        });
        setItems(files,100);

    }
    private StringProperty folder;

    /**
     *
     * @param value
     */
    public void setFolder(String value) {
        folderProperty().set(value);
    }

    /**
     *
     * @return
     */
    public final String getFolder() {
        return folderProperty().get();
    }

    /**
     *
     * @return
     */
    public StringProperty folderProperty() {
        if (folder == null) {
            folder = new SimpleStringProperty("");
        }
        return folder;
    }
}
