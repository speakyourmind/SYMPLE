/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.symfound.controls.user;

import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import org.symfound.builder.user.characteristic.Typing;
import org.symfound.controls.RunnableControl;
import org.symfound.device.Device;
import org.symfound.device.emulation.input.keyboard.KeyboardAutomator;
import org.symfound.device.processing.Processor;
import org.symfound.text.TextOperator;
import org.symfound.text.prediction.local.Predictor;

/**
 *
 * @author Javed Gangjee
 */
public class ExternalPredictionButton extends RunnableControl {

    private final String predictionWord;

    /**
     *
     * @param predictionWord
     * @param cssClass
     */
    public ExternalPredictionButton(String predictionWord, String cssClass) {
        super(cssClass);
        this.predictionWord = predictionWord;
        this.setText(predictionWord);
    }

    @Override
    public void run() {
        String pasteText;
        final String activeText = getUser().getTyping().getActiveText();
        pasteText = Predictor.getRemainingWord(predictionWord, activeText);
        Typing typing = getUser().getTyping();
        typing.setActiveText(typing.getActiveText().concat(pasteText));
        sendWordToComponent(pasteText);
    }

    /**
     *
     * @param pasteText
     * @throws HeadlessException
     */
    public void sendWordToComponent(String pasteText) throws HeadlessException {
        TextOperator.setStringToClipboard(pasteText);
        pasteToComponent();
    }

    /**
     *
     */
    public void pasteToComponent() {
        final Device current = getSession().getDeviceManager().getCurrent();
        Processor processor = current.getProcessor();
        KeyboardAutomator keyboardAutomator = processor.getEmulationManager().getKeyboard().getAutomator();
        keyboardAutomator.sendToComponent((char) KeyEvent.VK_V, true);
    }
}
