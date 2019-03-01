/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.twitter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import org.symfound.media.MediaManager;
import org.symfound.social.twitter.Tweet;
import org.symfound.social.twitter.TwitterReader;
import twitter4j.TwitterException;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public class TwitterManager extends MediaManager<Tweet> {

    private static final String NAME = TwitterManager.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public TwitterManager() {
        super(Arrays.asList(new Tweet()));
    }

    @Override
    public void run() {
        try {
            List<Tweet> tweets = getTwitterReader().loadHomeTimeline();
            List<Tweet> keys = new ArrayList<>(tweets);
            setItems(keys, 100);
        } catch (IOException | TwitterException | URISyntaxException ex) {
            LOGGER.fatal(ex);
        }

    }

    private TwitterReader reader;

    /**
     *
     * @return
     */
    public TwitterReader getTwitterReader() {
        if (reader == null) {
            reader = new TwitterReader();
        }
        return reader;
    }

}
