/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user.media.pageflip;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import org.symfound.comm.file.PathReader;
import org.symfound.comm.file.PathWriter;
import org.symfound.media.MediaManager;
import org.symfound.main.Main;

/**
 *
 * @author Javed Gangjee <jgangjee@gmail.com>
 */
public class PageFlipManager extends MediaManager<String> {

    private static final String NAME = PageFlipManager.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public PageFlipManager() {
        super(Arrays.asList(""));
    }

    @Override
    public void run() {
        setItems(getURLs(), 100);
    }

    private static final List<String> DEFAULT_SITES_LIST
            = Arrays.asList("http://speakyourmindfoundation.org/",
                    "https://www.braingate.org/",
                    "https://www.brown.edu/");

    /**
     *
     */
    public String saveLocation;
    private List<String> urls = new ArrayList<>();

    /**
     *
     * @param value
     */
    public void setURLs(List<String> value) {
        urls = new ArrayList<>(value);
        saveLocation = Main.getSession().getUser().getContent().getHomeFolder() + "/Documents/SYMPLE/Apps/" + "PageFlip/urls.txt";

        PathWriter pathWriter = new PathWriter(saveLocation);
        try {
            pathWriter.writeToFile(value.toString().replaceAll("[\\[\\]]", "").replaceAll(" ",""), false, true);
        } catch (IOException ex) {
            LOGGER.fatal(ex);
        }
    }

    /**
     *
     * @return
     */
    public final List<String> getURLs() {

        saveLocation = Main.getSession().getUser().getContent().getHomeFolder() + "/Documents/SYMPLE/Apps/" + "PageFlip/urls.txt";
        File file = new File(saveLocation);
        if (!file.isDirectory()) {
            if (!file.exists()) {
                try {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                    setURLs(DEFAULT_SITES_LIST);
                } catch (IOException ex) {
                    LOGGER.fatal(ex);
                }
            }
        }

        try {
            PathReader pathReader = new PathReader(saveLocation);

            String text = pathReader.getFileText();
            if (!text.isEmpty()) {
                urls = new ArrayList<>(Arrays.asList(text.split(",")));
            }
        } catch (IOException ex) {
            LOGGER.fatal(ex);
        }

        return urls;
    }

}
