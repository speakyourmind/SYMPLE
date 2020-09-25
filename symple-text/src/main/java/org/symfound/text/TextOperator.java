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
package org.symfound.text;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import org.apache.log4j.Logger;
import org.symfound.comm.file.PathWriter;

/**
 *
 * @author Javed Gangjee
 */
public class TextOperator {

    private static final String NAME = TextOperator.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);

    /**
     *
     */
    public static final String EOL = System.getProperty("line.separator");

    /**
     *
     */
    public TextArea textArea = new TextArea();

    /**
     *
     * @param textArea
     */
    public TextOperator(TextArea textArea) {
        this.textArea = textArea;
    }

    /**
     *
     */
    public TextOperator() {

    }

    /**
     * Write the text from the specified textArea into file.
     *
     * @param pathRead
     * @throws IOException
     */
    public void exportToFile(String pathRead)
            throws IOException {
        if (textArea != null) {
            PathWriter txtFileWriter = new PathWriter(pathRead);
            // Create the file if it does not exist
            txtFileWriter.create();
            if (textArea.getText() != null) {
                String writeText = textArea.getText();
                txtFileWriter.writeToFile(writeText, Boolean.FALSE, Boolean.TRUE);
            }
        }
    }

    /**
     *
     *
     * @param pathSave path to save file to
     * @param pathRead path of initial load file
     */
    public void autoSave(String pathSave, String pathRead) {
        if (textArea != null && pathSave != null && pathRead != null) {
            try {
                TextAnalyzer textAnalyzer = new TextAnalyzer(textArea.getText());
                // If the added text is a period or a phrase
                if (textAnalyzer.isSentence()) {
                    // Get timeStamp as String to use in filename
                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH mm").
                            format(Calendar.getInstance().getTime());

                    String fileName = pathSave + "Autosave " + timeStamp + ".txt";
                    // Write to both current file and autosaved file
                    exportToFile(fileName);
                }
                    exportToFile(pathRead);
                }catch (IOException ex) {
                LOGGER.fatal(null, ex);
            }

            }
        }
        /**
         * Gets the last sentence — marked by period, question mark or
         * exclamation mark — in the specified <code>TextArea textarea</code>.
         * Splits the text using a regular expression that selects for <code> . || ! || ?
         * </code> followed by one or more spaces. Then returns the last
         * sentence in the consequent array. This method is used when the user
         * ends a sentence to save only the most recent sentence in the custom
         * user dictionary automatically.
         *
         * @return the last sentence in <code>textarea</code>
         */
    public String getLastSentence() {
        String text = textArea.getText();
        // RegEx splits at . || ! || ? followed by one or more spaces
        String regex = "(?<=[.!?]) +";
        String[] sentences = text.split(regex);
        // CASE: No sentences found in TextArea
        if (sentences.length == 0) {
            LOGGER.fatal("No sentence in TextArea found; "
                    + "returning empty string.   (Pred.getLastSentence)");
            return "";
        }
        return sentences[sentences.length - 1];
    }

    /**
     *
     * @param string
     * @throws HeadlessException
     */
    public static void setStringToClipboard(String string) throws HeadlessException {
        StringSelection stringSelection = new StringSelection(string);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    /**
     *
     * @param addTxt
     * @param toCase
     */
    public void appendLetter(String addTxt, Boolean toCase) {
        Platform.runLater(() -> {
            textArea.replaceSelection("");
            final String addString = (toCase) ? addTxt.toUpperCase() : addTxt.toLowerCase();
            textArea.appendText(addString);
        });
    }

    /**
     * Split the text provided by the delimited into an array and then add that
     * to a list
     *
     * @param splitText text to be split
     * @param delim string to split the text
     * @return
     */
    public List<String> splitAsList(String splitText, String delim) {
        // Split the string as a string array
        String[] stringSplit = splitText.split(delim);
        // Convert the array to a list
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(stringSplit));

        return list;
    }

    /**
     *
     * @return
     */
    public TextArea getTextArea() {
        return textArea;
    }

}
