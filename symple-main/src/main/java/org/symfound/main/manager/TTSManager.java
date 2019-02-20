/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.main.manager;

import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import org.symfound.builder.user.User;
import org.symfound.builder.user.characteristic.Speech;
import org.symfound.tools.iteration.ModeIterator;
import org.symfound.voice.main.TTSLauncher;

/**
 *
 * @author Javed Gangjee
 */
public class TTSManager implements Runnable {

    private static final String NAME = DeviceManager.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static TTSLauncher launcher;

    /**
     *
     * @param user
     */
    public TTSManager(User user) {
        this.user = user;
    }

    @Override
    public final void run() {
        Speech speech = getUser().getSpeech();
        getLauncher().setCurrentPlayer(speech.getSpeakingVoice());
        speech.speakingVoiceProperty().addListener((observable, oldValue, newValue) -> {
            getLauncher().setCurrentPlayer(speech.getSpeakingVoice());
        });
    }

    /**
     *
     * @return
     */
    public TTSLauncher getLauncher() {
        if (launcher == null) {
            LOGGER.info("Initializing TTS Launcher with build order " + getBuildOrder());
            launcher = new TTSLauncher(getBuildOrder());
        }
        return launcher;

    }

    private ObservableList<String> getBuildOrder() {
        return getUser().getNavigation().getTTSBuildOrder();
    }

    /**
     *
     * @return
     */
    public String getInitName() {
        return getUser().getSpeech().getSpeakingVoice();
    }

    /**
     *
     */
    private final User user;

    /**
     *
     * @return
     */
    public User getUser() {
        return user;
    }

    // Iterator Methods
    private ModeIterator<String> mode;

    /**
     *
     * @return
     */
    public ModeIterator<String> getIterator() {
        if (mode == null) {
            mode = new ModeIterator<>(getLauncher().getVoiceNames());
            mode.set(getInitName());
        }
        return mode;
    }
    // Iterator Methods
    private ModeIterator<String> readerVoiceModes;

    /**
     *
     * @return
     */
    public ModeIterator<String> getReaderIterator() {
        if (readerVoiceModes == null) {
            readerVoiceModes = new ModeIterator<>(getLauncher().getVoiceNames());
            readerVoiceModes.set(getInitName());
        }
        return readerVoiceModes;
    }
}
