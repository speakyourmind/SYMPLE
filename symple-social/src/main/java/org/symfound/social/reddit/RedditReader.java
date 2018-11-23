/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.social.reddit;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.symfound.comm.file.ExtensionAnalyzer;
import org.symfound.comm.rss.RSSReader;
import org.symfound.social.SocialMediaReader;

/**
 *
 * @author Javed Gangjee
 */
public class RedditReader extends SocialMediaReader{

    /**
     *
     * @param url
     * @return
     * @throws IOException
     * @throws FeedException
     */
    public static Map<String, String> readURL(String url) throws IOException, FeedException {
        SyndFeed feed = RSSReader.readFeed(url);
        Map<String, String> links = buildLinkMap(feed);
        return links;
    }

    /**
     *
     * @param feed
     * @return
     */
    public static Map<String, String> buildLinkMap(SyndFeed feed) {
        HashMap<String, String> map = new HashMap<>();
        List<SyndEntry> entries = feed.getEntries();
        entries.forEach((SyndEntry entry) -> {
            String value = entry.getContents().get(0).getValue();
            Document doc = Jsoup.parse(value);
            Elements links = doc.select("a[href]");
            Element link = links.get(2);
            String attr = link.attr("abs:href");
            ExtensionAnalyzer ea = new ExtensionAnalyzer(attr);

            if (attr.contains("youtube.com")) {
                String videoID = attr.replace("https://www.youtube.com/watch?v=", "");
                attr = buildEmbeddedURL(videoID);
                map.put(attr, entry.getTitle());
            } else if (attr.contains("youtu.be")) {
                String substring = attr.replace("https://youtu.be/", "");
                attr = buildEmbeddedURL(substring);
                map.put(attr, entry.getTitle());
            } else if (/*ea.isExtension("gifv") ||*/ea.isPictureFile()) {
                map.put(attr, entry.getTitle());
            }

        });
        Map<String, String> links = Collections.unmodifiableMap(map);

        return links;
    }

    /**
     *
     * @param substring
     * @return
     */
    public static String buildEmbeddedURL(String substring) {
        String attr;
        attr = "https://www.youtube.com/embed/" + substring
                + "?rel=0&"
                + "autoplay=1&"
                + "controls=0&"
                + "showinfo=0&"
                + "modestbranding=1"
                + "&iv_load_policy=3";
        return attr;
    }

}
