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
package org.symfound.text.prediction.local;

import java.net.URISyntaxException;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.apache.log4j.Logger;
import org.symfound.text.TextAnalyzer;
import org.symfound.text.TextOperator;
import org.symfound.tools.timing.DelayedEvent;

/**
 *
 * @author Javed Gangjee
 */
public class Predictor {

    /**
     *
     */
    public static final String NAME = Predictor.class.getName();

    /**
     *
     */
    public static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public TextArea textArea;

    /**
     *
     */
    public TextOperator textOperator;
    private StringProperty text;

    /**
     *
     * @param textArea
     */
    public Predictor(TextArea textArea) {
        this.textArea = textArea;
        textOperator = new TextOperator(textArea);
    }

    /**
     * Get the top specified number of words from the prediction list and set it
     * to the specified button.
     *
     * @param predList list to get the words from
     * @param numWords number of words to return
     */
    public void getPredictionsAsList(List<String> predList, int numWords) {

        // If the number of predictions generated is greater than or equal
        // the number of words requested
        if (predList.size() >= numWords) {
            // Set the first word to initialize the list as a text string
            String predText = predList.get(0);
            // For number of predicted words requested
            for (int j = 1; j < numWords; j++) {
                // Build list of predicted words 
                predText = predText + System.getProperty("line.separator")
                        + predList.get(j);
                // Set the button with the list of predicted words
                setText(predText);
            }
        }
    }

    /**
     * Set the text to be used in the <code>PredictionGenerator</code> to the
     * full string in the <code>textArea</code> but do not include the selection
     * if autocomplete is set to true.
     *
     * @param autocomplete
     * @return
     */
    public String getPredInput(Boolean autocomplete) {
        // Initialize to the full text in the area
        String predInputText = textArea.getText();
        if (autocomplete) {
            // Get the autocomplete selection
            IndexRange selRng = textArea.getSelection();
            Integer range = selRng.getStart();
            if (range > textArea.getLength()) {
                range = textArea.getLength();
            }
            // Get substring of the text area upto the start of selected range
            predInputText = textArea.getText().substring(0, range);
        }
        return predInputText;
    }

    /**
     * <em>NOTE: Should take in a textArea without cursorChar, and this method
     * does not append the cursorChar itself.</em><br>
     * <em>CursorChar should be stripped before the method invocation, and added
     * back afterwards.</em>
     *
     * @param acGen autocomplete prediction generator
     * @param autoCompleteTime time in ms to reset the word
     * @param toCase upper case = 1, lower case = 2(or more)
     */
    public void setAutoComplete(PredictionGenerator acGen,
            Double autoCompleteTime, Boolean toCase) {
        String input = textArea.getText();
        // Populate with new autocorrect engine; settings set in initialize()
        List<String> predictionList = acGen.generateRaw(input, 1, toCase);
        //Continue if at least one word is found
        if (predictionList.size() > 0) {
            //   LOGGER.info("Prediction List Size:" + predList);
            // CASE: Space as last character indicates new word, prediction based only on bigram probability
            if (input.charAt(input.length() - 1) == ' ') {
                int i = 0;
                if (predictionList.get(i).length() > 0) {
                    textArea.appendText(predictionList.get(i));
                    textArea.selectPreviousWord();
                }
            } else { // CASE: Partial word has been entered          
                //Select the previous word in order to read the length
                textArea.selectPreviousWord();
                //Get the autocomplete predicted word & remove letters entered so far
                String compText = predictionList.get(0).substring(textArea.getSelection().getLength());
                //Save the current length of the text in the area
                int originalLength = textArea.getLength();
                // Append text without cursorChar
                textArea.appendText(compText);
                // Select all appended text
                textArea.selectRange(originalLength, textArea.getLength());
            }
            startAutoCompleteTimer(autoCompleteTime);
        } else {
            LOGGER.info("No prediction words found");
        }
    }

    /**
     *
     */
    public DelayedEvent timer;

    /**
     *
     * @param time
     */
    public void startAutoCompleteTimer(Double time) {
        getAutoCompleteTimer().end();
        getAutoCompleteTimer().setup(time, (ActionEvent e) -> {
            textArea.replaceSelection("");
        });
        getAutoCompleteTimer().playFromStart();
    }

    /**
     *
     * @return
     */
    public DelayedEvent getAutoCompleteTimer() {
        if (timer == null) {
            timer = new DelayedEvent();
        }
        return timer;
    }

    /**
     *
     */
    public void stopAutoCompleteTimer() {
        getAutoCompleteTimer().end();
    }

    /**
     * Saves the last sentence in the text area to the custom user dictionary.
     * Only executes if <code>mainText.getText()</code> ends in a sentence.
     *
     * @return true if user dictionary was updated else false
     */
    public Boolean updateUserDictionary() {
        PredictionGenerator predictionGenerator = new PredictionGenerator();
        Boolean dictUpdated = false;

        // Boolean cursorRemoved = textOperator.removeCursorChar();
        TextAnalyzer textAnalyzer = new TextAnalyzer(textArea.getText());

        // TODO: Remove selected text if it exists
        if (textAnalyzer.endsInSentence()) {
            try {
                // If last sentence completed, store it in user's custom dictionary
                predictionGenerator.saveToUserDictionary(textOperator.getLastSentence());
            } catch (URISyntaxException ex) {
                LOGGER.fatal(null, ex);
            }
            dictUpdated = true;
        }

        return dictUpdated;
    }

    /**
     *
     */
    public static final String SPACE = " ";

    /**
     *
     * @param word
     * @param target
     * @return
     */
    public static String getRemainingWord(String word, final String target) {
        String pasteText;
        if (target.isEmpty() || target.endsWith(SPACE)) {
            pasteText = word.concat(SPACE);
        } else {
            int lastSpace = target.lastIndexOf(SPACE);
            String wordSoFar = target.substring(lastSpace + 1);
            String remainingWord = word.substring(wordSoFar.length());
            pasteText = remainingWord.concat(SPACE);
        }
        return pasteText;
    }

    /**
     *
     * @param value
     */
    public void setText(String value) {
        textProperty().setValue(value);
    }

    /**
     *
     * @return
     */
    public String getText() {
        return textProperty().getValue();
    }

    /**
     *
     * @return
     */
    public StringProperty textProperty() {
        if (text == null) {
            text = new SimpleStringProperty("");
        }
        return text;
    }

}
