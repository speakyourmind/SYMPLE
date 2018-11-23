package org.symfound.voice.engine.awspolly;

import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.model.LanguageCode;
import static com.amazonaws.services.polly.model.LanguageCode.EnUS;
import com.amazonaws.services.polly.model.OutputFormat;
import static com.amazonaws.services.polly.model.OutputFormat.Mp3;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.Voice;
import java.io.IOException;
import java.io.InputStream;
import javazoom.jl.decoder.JavaLayerException;
import org.apache.log4j.Logger;
import static org.apache.log4j.Logger.getLogger;
import org.symfound.voice.builder.TTSEngine;

/**
 *
 * @author Javed Gangjee
 */
public final class PollyEngine extends TTSEngine {

    private static final String NAME = PollyEngine.class.getName();
    private static final Logger LOGGER = getLogger(NAME);

    /**
     *
     */
    public static final String POLLY_TTS = "Polly";

    /**
     *
     */
    public static final OutputFormat OUTPUT_FORMAT_MP3 = Mp3;

    /**
     *
     */
    public static final LanguageCode LANGUAGE_EN_US = EnUS;

    /**
     *
     */
    public AmazonPolly polly;

    /**
     *
     */
    public PollyVoiceManager voiceManager;

    /**
     *
     */
    public PollyEngine() {
        super(POLLY_TTS);
        load();
    }

    @Override
    public void load() {
        getVoiceManager().loadClient();
    }

    /**
     *
     * @return
     */
    public PollyVoiceManager getVoiceManager() {
        if (voiceManager == null) {
            voiceManager = new PollyVoiceManager();
        }
        return voiceManager;
    }

    /**
     *
     *
     * @param text
     * @param play
     * @param voiceName
     */
    @Override
    public void run(String text, Boolean play, String voiceName) {
        if (text != null && !text.isEmpty()) {
            if (play) {
                try {
                    LOGGER.info("Playing text with " + POLLY_TTS + ": " + text);
                    voiceManager.setVoice(voiceName);
                    InputStream speechStream = generateInputStream(text, voiceManager.getVoice());
                    player = playInputStream(speechStream);

                    stoppedProperty().addListener((observable, oldValue, newValue) -> {
                        LOGGER.info("Stopped value changed to " + newValue);
                        if (newValue) {
                            LOGGER.info("Closing player");
                            player.close();
                            setStopped(false);
                        }
                    });
                    player.play();
                } catch (IOException | JavaLayerException ex) {
                    LOGGER.fatal(null, ex);
                }
            }
        }else {
            LOGGER.warn("No text to play");
        }
    }

    /**
     *
     * @param text
     * @param voice
     * @return
     * @throws IOException
     */
    public InputStream generateInputStream(String text, Voice voice) throws IOException {
        SynthesizeSpeechRequest synthReq = new SynthesizeSpeechRequest();
        LOGGER.info("Synthesizing speech using Voice: " + voice.getName());
        synthReq.withText(text).withVoiceId(voice.getId());
        synthReq.withOutputFormat(OUTPUT_FORMAT_MP3);

        final AmazonPolly loadClient = getVoiceManager().loadClient();
        SynthesizeSpeechResult synthRes = loadClient.synthesizeSpeech(synthReq);
        InputStream audioStream = synthRes.getAudioStream();
        LOGGER.info("Audio stream generated for text: " + text);

        return audioStream;
    }

}
