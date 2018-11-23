/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import org.apache.log4j.Logger;
import org.symfound.text.prediction.local.Predictor;

/**
 *
 * @author Javed Gangjee
 */
public class PredictionButton extends ScriptButton {

    private static final String NAME = PredictionButton.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     * @param predictionWord
     */
    public PredictionButton(String predictionWord) {
        setText(predictionWord);
    }

    @Override
    public void run() {
        final String text = getPrimaryControl().getText();
        LOGGER.info("Prediction Button clicked:" + text);

        if (text.length() > 1) {
            if (getTextArea().getPredictor().getAutoCompleteTimer().isPlaying()) {
                getTextArea().getPredictor().stopAutoCompleteTimer();
                getTextArea().type("");
            }

            String pasteWord;
            if (getTextArea().getText().endsWith(" ") || getTextArea().getText().isEmpty()) {
                pasteWord = text;
            } else {
                pasteWord = Predictor.getRemainingWord(text, getTextArea().getText());
            }

            LOGGER.info("Word to be pasted: " + pasteWord);
            getTextArea().get().appendText(pasteWord);
            ActiveTextArea.bSpaceMode = 0;
        }
    }

}
