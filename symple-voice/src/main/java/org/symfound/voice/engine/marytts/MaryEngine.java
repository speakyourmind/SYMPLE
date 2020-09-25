/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.voice.engine.marytts;

import java.io.IOException;
import static java.util.Locale.UK;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import org.apache.log4j.Logger;
import static org.apache.log4j.Logger.getLogger;
import org.symfound.voice.builder.TTSEngine;
import static org.symfound.voice.engine.marytts.MaryVoiceManager.VOICE_MAP;

/**
 *
 * @author Javed Gangjee
 */
public class MaryEngine extends TTSEngine {

    private static final String NAME = MaryEngine.class.getName();
    private static final Logger LOGGER = getLogger(NAME);

    /**
     *
     */
    public static final String MARY_TTS = "Mary";

    /**
     *
     */
    public MaryInterface marytts;

    /**
     *
     */
    public MaryEngine() {
        super(MARY_TTS);
        load();
    }
    

    /**
     * MaryTTS package works by taking the text and loading it as an
     * AudioInputStream which is written to a timestamped wav file. The wav file
     * is then played.
     *
     * @param text
     * @param play
     * @param voiceName
     */
    // TO DO: Remove String text parameter. Tie activeText or button textProperty directly to this
    @Override
    public void run(String text, Boolean play, String voiceName) {

        marytts.setLocale(UK);
        marytts.setVoice(VOICE_MAP.get(voiceName));

        if (!text.isEmpty()) {
            AudioInputStream audio = null;
            try {
                audio = marytts.generateAudio(text);
                if (play) {
                    // Play the Audio
                    playAudioInputStream(audio);
                }
            } catch (SynthesisException | LineUnavailableException | IOException ex) {
                LOGGER.fatal(ex);
            } finally {
                try {
                    audio.close();
                } catch (IOException ex) {
                    LOGGER.fatal(ex);
                }
            }
        }

    }

    @Override
    public final void load() {
        try {
            LOGGER.fatal("Loading Mary TTS Interface");
            marytts = new LocalMaryInterface();
            LOGGER.info("Voices available for MaryTTS: " + marytts.getAvailableVoices());

        } catch (MaryConfigurationException ex) {
            LOGGER.fatal("Unable to start MaryTTS ", ex);
        }
    }

    private MaryVoiceManager voiceManager;

    /**
     *
     * @return
     */
    public MaryVoiceManager getVoiceManager() {
        if (voiceManager == null) {
            voiceManager = new MaryVoiceManager();
        }
        return voiceManager;
    }

  //  @Override

    /**
     *
     */
    public void stop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
