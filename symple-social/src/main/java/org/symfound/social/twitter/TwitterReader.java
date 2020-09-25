/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.social.twitter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.symfound.social.SocialMediaReader;
import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.URLEntity;

/**
 *
 * @author Javed
 */
public class TwitterReader extends SocialMediaReader {

    /**
     *
     */
    public static final String NAME = TwitterReader.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     * @return
     * @throws IOException
     * @throws TwitterException
     * @throws URISyntaxException
     */
    public List<Tweet> loadHomeTimeline() throws IOException, TwitterException, URISyntaxException {
        List<Tweet> tweets = new ArrayList<>();
       getConnector().connect();

        final ResponseList<Status> homeTimeline = getConnector().getTwitterFactory().getHomeTimeline(new Paging(1, 100));
        homeTimeline.forEach((rawTweet) -> {
            //   System.out.println("User: " + rawTweet.getUser().getName() + " TWEET >>>> " + rawTweet.getText());

            Tweet tweet = new Tweet();
            tweet.setName(rawTweet.getUser().getName());
            tweet.setText(rawTweet.getText());
            tweet.setProfileImageURL(rawTweet.getUser().getOriginalProfileImageURL());

            final URLEntity[] urlEntities = rawTweet.getURLEntities();
            if (urlEntities.length > 0) {
                for (int i = 0; i < urlEntities.length; i++) {
                    final String expandedURL = urlEntities[i].getExpandedURL();
                    //   System.out.println(urlEntities[i].getURLURL());
                    //    if (!expandedURL.contains("twitter.com/i/web/status")) {
                    if (!expandedURL.contains("twitter.com")) {
                        tweet.setURL(expandedURL);// Expand to use a List
                    } else {
                        //LOGGER.info("Tweet link found: " + expandedURL);
                    }
                    //  }
                    // System.out.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM"+expandedURL);
                }
            }

            final MediaEntity[] mediaEntities = rawTweet.getMediaEntities();
            if (mediaEntities.length > 0) {
                for (int i = 0; i < mediaEntities.length; i++) {
                    //   System.out.println(mediaEntities[i].getMediaURL());
                    tweet.setMediaURL(mediaEntities[i].getMediaURL());// Expand to use a List
                }
            }
            tweets.add(tweet);
        });
        return tweets;
        //     Status status = twitter.updateStatus(args[0]);
        //   System.out.println("Successfully updated the status to [" + status.getText() + "].");
        // System.exit(0);
    }

    

    private TwitterConnector connector;

    /**
     *
     * @return
     */
    public TwitterConnector getConnector() {
        if (connector == null) {
            connector = new TwitterConnector();
        }
        return connector;
    }
   


}
