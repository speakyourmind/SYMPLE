/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.main;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import org.apache.log4j.Logger;

/**
 *
 * @author Javed Gangjee
 */
public class FontLoader {

    private static final String NAME = FontLoader.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);
    private static final int DEFAULT_SIZE = 16;
    private final String fontLocation;

    /**
     *
     */
    public static List<Font> fontList = new ArrayList<>();

    /**
     *
     */
    public static Label label;

    /**
     *
     * @param fontLocation
     */
    public FontLoader(String fontLocation) {
        this.fontLocation = fontLocation;
    }

    /**
     *
     * @param fontNames
     */
    public void load(List<String> fontNames) {
        LOGGER.info("Loading fonts...");
        LOGGER.info("Available Fonts : " + fontNames.toString());
        for (String fontName : fontNames) {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(fontLocation + fontName);
            Font font = Font.loadFont(inputStream, DEFAULT_SIZE);
            fontList.add(font);
        }

    }

}
