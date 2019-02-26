package org.symfound.comm.rss;

import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.SyndFeedOutput;
import com.rometools.rome.io.XmlReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.symfound.comm.file.ExtensionAnalyzer;

// TO DO

/**
 *
 * @author Javed Gangjee
 */
public class RSSPublisher {

    /**
     *
     * @param args
     * @throws IOException
     * @throws FeedException
     */
    public static void main(String[] args) throws IOException, FeedException {
        //     SyndFeed feed = createFeed();
        //   addEntryToFeed(feed);
        //   publishFeed(feed);
        HashMap<String, String> map = new HashMap<>();
        SyndFeed feed = readFeed("https://www.reddit.com/r/funny/.rss");

        List<SyndEntry> entries = feed.getEntries();
        for (SyndEntry entry : entries) {
            String value = entry.getContents().get(0).getValue();
            Document doc = Jsoup.parse(value);
            Elements links = doc.select("a[href]");
            Element link = links.get(2);
            String attr = link.attr("abs:href");
            ExtensionAnalyzer ea = new ExtensionAnalyzer(attr);
            if (ea.isPictureFile()) {
                map.put(attr, entry.getTitle());
            }
        }
        Map<String, String> links = Collections.unmodifiableMap(map);


    }

    private static SyndFeed createFeed() {
        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_1.0");
        feed.setTitle("Test title");
        feed.setLink("http://www.reddit.com");
        feed.setDescription("Basic description");

        return feed;
    }

    private static void addEntryToFeed(SyndFeed feed) {
        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle("Entry title");
        entry.setLink("https://www.reddit.com/r/funny/.rss");

        addDescriptionToEntry(entry);
        addCategoryToEntry(entry);

        feed.setEntries(Arrays.asList(entry));
    }

    private static void addDescriptionToEntry(SyndEntry entry) {
        SyndContent description = new SyndContentImpl();
        description.setType("text/html");
        description.setValue("First entry");

        entry.setDescription(description);
    }

    private static void addCategoryToEntry(SyndEntry entry) {
        List<SyndCategory> categories = new ArrayList<>();
        SyndCategory category = new SyndCategoryImpl();
        category.setName("Sophisticated category");
        categories.add(category);

        entry.setCategories(categories);
    }

    private static void publishFeed(SyndFeed feed) throws IOException, FeedException {
        Writer writer = new FileWriter("xyz.txt");
        SyndFeedOutput syndFeedOutput = new SyndFeedOutput();
        syndFeedOutput.output(feed, writer);
        writer.close();
    }

    private static SyndFeed readFeed(String URL) throws IOException, FeedException {
        URL feedSource = new URL(URL);
        SyndFeedInput input = new SyndFeedInput();
        return input.build(new XmlReader(feedSource));
    }
}
