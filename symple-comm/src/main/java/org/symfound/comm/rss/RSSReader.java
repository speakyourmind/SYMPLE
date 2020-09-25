package org.symfound.comm.rss;

import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.io.IOException;
import java.net.URL;

// TO DO

/**
 *
 * @author Javed Gangjee
 */
public class RSSReader {

    /**
     *
     * @param URL
     * @return
     * @throws IOException
     * @throws FeedException
     */
    public static SyndFeed readFeed(String URL) throws IOException, FeedException {
        URL feedSource = new URL(URL);
        SyndFeedInput input = new SyndFeedInput();
        return input.build(new XmlReader(feedSource));
    }
}
