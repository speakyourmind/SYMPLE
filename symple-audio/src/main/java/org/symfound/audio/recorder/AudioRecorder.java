package org.symfound.audio.recorder;

import java.io.*;
import javax.sound.sampled.*;
import org.apache.log4j.Logger;

/**
 * A sample program is to demonstrate how to record sound in Java author:
 * www.codejava.net
 */
// TO DO:Still not configurable
public class AudioRecorder {

    private static final String NAME = AudioRecorder.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);
    // record duration, in milliseconds

    static final long RECORD_TIME = 60000;  // 1 minute

    // path of the wav file
    File wavFile = new File("C:/Test/RecordAudio.wav");

    // format of audio file
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

    // the line from which audio data is captured
    TargetDataLine line;

    /**
     * Defines an audio format
     */
    AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
        return format;
    }

    /**
     * Captures the sound and record into a WAV file
     */
    public void start() {
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            // checks if system supports the data line
            if (!AudioSystem.isLineSupported(info)) {
                LOGGER.info("Line not supported");
                System.exit(0);
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();   // start capturing

            LOGGER.info("Start capturing...");

            AudioInputStream ais = new AudioInputStream(line);

            LOGGER.info("Start recording...");

            // start recording
            AudioSystem.write(ais, fileType, wavFile);

        } catch (LineUnavailableException | IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Closes the target data line to finish capturing and recording
     */
    public void finish() {
        line.stop();
        line.close();
        LOGGER.info("Finished");
    }

}
