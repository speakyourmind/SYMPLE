/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.audio.music.song;

import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import org.symfound.comm.file.ExtensionAnalyzer;

/**
 *
 * @author Javed Gangjee
 */
public class SongFileAnalyzer {
    private static final String NAME = SongFileAnalyzer.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    private final String path;

    /**
     *
     * @param path
     */
    public SongFileAnalyzer(String path) {
        this.path = path;
    }

    // Audio Extensions

    /**
     *
     */
    public static final String WAV_EXTENSION = ".wav";

    /**
     *
     */
    public static final String MP3_EXTENSION = ".mp3";

    /**
     *
     */
    public static final List<String> SONG_EXTENSIONS = Arrays.asList(WAV_EXTENSION, MP3_EXTENSION);

    /**
     *
     * @return
     */
    public Boolean isMP3() {
        return getExtensionAnalyzer().isExtension(MP3_EXTENSION);
    }

    /**
     *
     * @return
     */
    public Boolean isWAV() {
        return getExtensionAnalyzer().isExtension(WAV_EXTENSION);
    }

    /**
     *
     * @return
     */
    public Boolean isPlayable() {
        final Boolean extension = getExtensionAnalyzer().isExtension(SONG_EXTENSIONS);
        if (extension){
            LOGGER.info("File is a playable song: "+ path);
        } else {
            LOGGER.warn("File is NOT a playable song: "+path);
        }
        
        return extension;
    }
    
    
    private ExtensionAnalyzer extensionAnalyzer;

    /**
     *
     * @return
     */
    public ExtensionAnalyzer getExtensionAnalyzer() {
        if (extensionAnalyzer == null) {
            extensionAnalyzer = new ExtensionAnalyzer(path);
        }
        return extensionAnalyzer;
    }

}
