/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.youtube;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import org.symfound.media.MediaManager;
import static org.symfound.controls.user.media.youtube.YouTubePlaylistLoader.load;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public class YouTubeManager extends MediaManager<String> {

    private static final String NAME = YouTubeManager.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public YouTubeManager() {
        super(Arrays.asList(""));
    }


    @Override
    public void run() {
        try {
            List<String> videos = load(getPlaylistLoader().getPlaylistID());
            LOGGER.info(videos.size() + " videos retrieved");
            setItems(videos, 100);
        } catch (IOException ex) {
            LOGGER.fatal(ex);
        }
    }

    private YouTubePlaylistLoader playlistLoader;

    /**
     *
     * @return
     */
    public YouTubePlaylistLoader getPlaylistLoader() {
        if (playlistLoader == null) {
            playlistLoader = new YouTubePlaylistLoader();
        }
        return playlistLoader;
    }
}
