/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.voice.main;

import java.util.ArrayList;
import static java.util.Collections.unmodifiableMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.log4j.Logger;
import static org.apache.log4j.Logger.getLogger;
import org.symfound.voice.builder.TTSPlayer;
import org.symfound.voice.engine.awspolly.PollyEngine;
import static org.symfound.voice.engine.awspolly.PollyEngine.POLLY_TTS;
import org.symfound.voice.engine.marytts.MaryEngine;
import static org.symfound.voice.engine.marytts.MaryEngine.MARY_TTS;

/**
 *
 * @author Javed Gangjee
 */
public final class TTSLauncher {

    private static final String NAME = TTSLauncher.class.getName();
    private static final Logger LOGGER = getLogger(NAME);

    private static TTSPlayer<MaryEngine> maryPlayer;
    private static TTSPlayer<PollyEngine> pollyPlayer;
    private static List<String> buildOrder;

    /**
     *
     * @param buildOrder
     */
    public TTSLauncher(List<String> buildOrder) {
        this.buildOrder = buildOrder;
        getVoiceMap();
    }

    /**
     *
     * @return
     */
    public static TTSPlayer<MaryEngine> getMaryPlayer() {
        if (maryPlayer == null) {
            LOGGER.info("Initializing MaryTTS Engine...");
            MaryEngine maryEngine = new MaryEngine();
            maryPlayer = new TTSPlayer(maryEngine);
        }
        return maryPlayer;
    }

    /**
     *
     * @return
     */
    public static TTSPlayer<PollyEngine> getPollyPlayer() {
        if (pollyPlayer == null) {
            LOGGER.info("Initializing Amazon Polly Engine...");
            PollyEngine pollyEngine = new PollyEngine();
            pollyPlayer = new TTSPlayer(pollyEngine);
        }
        return pollyPlayer;
    }

    /**
     *
     * @return
     */
    public List<String> getVoiceNames() {
        List<String> voices = new ArrayList<>();
        buildOrder.forEach((String name) -> {
            if (name.equalsIgnoreCase(MARY_TTS)) {
                voices.addAll(getMaryPlayer().getEngine().getVoiceManager().getAvailableVoices());
            } else if (name.equals(POLLY_TTS)) {
                voices.addAll(getPollyPlayer().getEngine().getVoiceManager().getAvailableVoices());
            }
        });
        return voices;
    }

    /**
     *
     * @return
     */
    public static Map<String, String> buildVoiceMap() {
        LOGGER.info("TTS Engines to be built: " + buildOrder);
        HashMap<String, String> hashMap = new HashMap<>();
        buildOrder.forEach((String name) -> {
            if (name.equalsIgnoreCase(MARY_TTS)) {
                MaryEngine maryEngine = getMaryPlayer().getEngine();
                maryEngine.getVoiceManager().getAvailableVoices().stream().forEach((voice) -> {
                    LOGGER.info("Adding MaryTTS voice:" + voice);
                    hashMap.put(voice, maryEngine.getName());
                });
            } else if (name.equalsIgnoreCase(POLLY_TTS)) {
                PollyEngine pollyEngine = getPollyPlayer().getEngine();
                pollyEngine.getVoiceManager().getAvailableVoices().stream().forEach((voice) -> {
                    LOGGER.info("Adding PollyTTS voice:" + voice);
                    hashMap.put(voice, pollyEngine.getName());
                });
            }
        });
        LOGGER.info("TTS Voices Available: " + hashMap);
        return unmodifiableMap(hashMap);
    }

    private static Map<String, String> voiceMap;

    /**
     *
     * @return
     */
    public static Map<String, String> getVoiceMap() {
        if (voiceMap == null) {
            voiceMap = buildVoiceMap();
        }
        return voiceMap;
    }

    /**
     *
     * @return
     */
    public final Map<String, TTSPlayer> buildPlayerMap() {
        Map<String, TTSPlayer> buildMap;
        HashMap<String, TTSPlayer> hashMap = new HashMap<>();
        buildOrder.forEach((String name) -> {
            if (name.equalsIgnoreCase(MARY_TTS)) {
                hashMap.put(getMaryPlayer().getEngine().getName(), getMaryPlayer());
            } else if (name.equalsIgnoreCase(POLLY_TTS)) {
                hashMap.put(getPollyPlayer().getEngine().getName(), getPollyPlayer());
            }
        });
        buildMap = unmodifiableMap(hashMap);
        LOGGER.info("TTS Player Map: " + buildMap);
        return buildMap;
    }

    /**
     *
     */
    public Map<String, TTSPlayer> playerMap;

    /**
     *
     * @param name
     * @return
     */
    public TTSPlayer get(String name) {
        return getPlayerMap().get(name);
    }

    /**
     *
     * @return
     */
    public Map<String, TTSPlayer> getPlayerMap() {
        if (playerMap == null) {
            playerMap = buildPlayerMap();
        }
        return playerMap;
    }

    /**
     *
     */
    public ObjectProperty<TTSPlayer> currentPlayer;

    /**
     *
     * @param name
     */
    public void setCurrentPlayer(String name) {
        String engineName = getVoiceMap().get(name);
        TTSPlayer ttsPlayer = get(engineName);
        setCurrentPlayer(ttsPlayer);
    }

    /**
     *
     * @param value
     */
    public void setCurrentPlayer(TTSPlayer value) {
        currentPlayerProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public TTSPlayer getCurrentPlayer() {
        return currentPlayerProperty().get();
    }

    /**
     *
     * @return
     */
    public ObjectProperty<TTSPlayer> currentPlayerProperty() {
        if (currentPlayer == null) {
            currentPlayer = new SimpleObjectProperty<>();
        }
        return currentPlayer;
    }

}
