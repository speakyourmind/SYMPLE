/*
 * Copyright (C) 2014 SpeakYourMind Foundation
 * Visit us at http://www.speakyourmindfoundation.org/

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.symfound.voice.player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;
import static javax.sound.sampled.AudioSystem.getLine;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import static javazoom.jl.player.FactoryRegistry.systemRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import org.apache.log4j.Logger;
import static org.apache.log4j.Logger.getLogger;

/**
 *
 * @author Javed Gangjee
 */
public class AudioPlayer {

    private static final String NAME = AudioPlayer.class.getName();
    private static final Logger LOGGER = getLogger(NAME);
    

    /**
     * Gets the format of the AudioInputStream and creates a clip which is
     * played.
     *
     * @param audioStream stream to be played
     * @throws LineUnavailableException
     * @throws IOException
     */
    public void playAudioInputStream(AudioInputStream audioStream)
            throws LineUnavailableException, IOException {
        AudioFormat format = audioStream.getFormat();
        Line.Info linfo = new DataLine.Info(Clip.class, format);
        Clip clip = (Clip) getLine(linfo);
        clip.open(audioStream);
        clip.start();
    }

    /**
     * Checks if the file is valid, then converts it into a stream and plays the
     * sound.
     *
     * @param audioFile file to be played
     * @throws javax.sound.sampled.LineUnavailableException
     * @throws java.io.IOException
     * @throws javax.sound.sampled.UnsupportedAudioFileException
     */
    public void playAudioFile(File audioFile)
            throws LineUnavailableException, IOException,
            UnsupportedAudioFileException {
        // Check if the provided file is available
        if (audioFile.isFile() == true) {
            // Get the file as a stream
            AudioInputStream stream = getAudioInputStream(audioFile);
            // Play the stream
            playAudioInputStream(stream);
        }
    }
    
    
  
    public AdvancedPlayer playInputStream(InputStream speechStream) throws JavaLayerException {
        FactoryRegistry systemRegistry = systemRegistry();
        AudioDevice audioDevice = systemRegistry.createAudioDevice();
        //create an MP3 player
        AdvancedPlayer player = new AdvancedPlayer(speechStream, audioDevice);

        player.setPlayBackListener(new PlaybackListener() {
            @Override
            public void playbackStarted(PlaybackEvent evt) {
                LOGGER.info("Playback started");
            }

            @Override
            public void playbackFinished(PlaybackEvent evt) {
                LOGGER.info("Playback finished");
            }
        });
        return player;
    }
    
    
    
}
