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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 *
 * @author Javed Gangjee
 */
public class TextAnalyzer {

    private static final String NAME = TextAnalyzer.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);
    String text;

    /**
     *
     */
    public TextAnalyzer() {
    }

    /**
     *
     * @param text
     */
    public TextAnalyzer(String text) {
        this.text = text;
    }



    private static boolean isSentence = false;

    /**
     *
     */
    public static HashMap<String, Integer> unigramMap = new HashMap<>();

    /**
     *
     */
    public static HashMap<String, HashMap<String, Integer>> bigramMap = new HashMap<>();
    private static String previousWord;

    /**
     * Parses each line of text in a <code>BufferedReader</code>. Differs from
     * <code>parseString</code>, which parses text into words of a
     * <code>String</code>, not a <code>BufferedReader</code>.
     *
     * @param br BufferedReader used to parse this text into words
     * @param mapWeight weight of frequency mapping for each given occurrence
     * (e.g. higher in user dictionary)
     * @return list of words in buffer of BufferedReader
     * @throws java.io.IOException
     */
    public List<String> parseText(BufferedReader br, int mapWeight) throws IOException {
        List<String> wordlist = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().length() == 0) {
                continue;
            }
            wordlist.addAll(parseString(line, mapWeight));
        }
        return wordlist;
    }

    /**
     * Splits a single string or line into List of words without punctuation or
     * spaces, all lower case (according to manipulations by
     * <code>processWord()</code>. <br>
     *
     * @param line of text to parse into words
     * @param mapWeight weight of frequency mapping for each given occurrence
     * (e.g. higher in user dictionary)
     * @return List of words in specified <code> String line </code>
     */
    public List<String> parseString(String line, int mapWeight) {
        List<String> wordlist = new ArrayList<>();
        String[] arr = line.split(" ");
        for (int i = 0; i < arr.length; i++) {
            String word = processWord(arr[i]);
            String[] addWord = word.split(" ");
            for (String newWord : addWord) {
                if (newWord != null) {
                    wordlist.add(newWord);
                    updateMaps(newWord, mapWeight);
                }
                if (isSentence) {
                    resetPrevWord();
                    isSentence = false;
                }
            }
        }
        return wordlist;
    }

    /**
     * Updates unigramMap and bigramMap with specified newWord, and gives the
     * frequency weight according to specified int weight. Updates the
     * <code>_prevWord</code> instance variable for bigramMap.
     *
     * @param newWord <code>String</code> word to add or update in maps
     * @param weight number of frequency to give map for each occurrence
     */
    public void updateMaps(String newWord, int weight) {
        if (newWord != null) {
            if (unigramMap.containsKey(newWord)) {
                unigramMap.put(newWord, unigramMap.get(newWord) + weight);
            } else {
                unigramMap.put(newWord, weight);
            }
            if (previousWord != null) {
                if (bigramMap.containsKey(previousWord)) {
                    HashMap<String, Integer> map = bigramMap.get(previousWord);
                    if (map != null) {
                        if (map.containsKey(newWord)) {
                            Integer freq = map.get(newWord);
                            if (freq != null) {
                                map.put(newWord, freq + weight);
                            }
                        } else {
                            map.put(newWord, weight);
                        }
                    } else {
                        LOGGER.info("map is null");
                    }
                } else {
                    HashMap<String, Integer> map = new HashMap<>();
                    map.put(newWord, weight);
                    bigramMap.put(previousWord, map);
                }
            }
            previousWord = newWord;
        }
    }

    /**
     * Takes word and strips punctuation and spaces, and makes all lower case.
     *
     */
    private String processWord(String word) {
        String regex = "[^\\w]";
        if (word.contains(".") || word.contains("!") || word.contains("?")) {
            isSentence = true;
        }
        Pattern pattern = Pattern.compile(regex + "+");
        word = word.replaceAll(pattern.pattern(), " ");
        return word.trim().toLowerCase();
    }

    /**
     * Sets <code>_prevWord</code> instance variable back to null, to avoid
     * updating bigramMap with an erroneous pair. <br>
     * Can be called at the end of each dictionary, and/or sentence or line
     * break, depending on context.
     */
    public void resetPrevWord() {
        previousWord = null;
    }

    /**
     * @param filename path of file to parse
     * @param mappingWeight weight of frequency mapping for each given
     * occurrence (e.g. higher in user dictionary)
     * @return list of words from file mapped to frequency of occurrence in
     * file.
     *
     */
    public List<String> parseFile(String filename, int mappingWeight) {
        List<String> wordlist = new ArrayList<>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream fileStream = loader.getResourceAsStream(filename);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(fileStream));
            wordlist = parseText(br, mappingWeight);
        } catch (FileNotFoundException ex) {
            LOGGER.fatal(null, ex);
        } catch (IOException ex) {
            LOGGER.fatal(null, ex);
        }

        return wordlist;
    }
    
    
    /**
     * Test if the entered text is a punctuation that ends a sentence.
     *
     * @return
     */
    public Boolean isSentence() {
        // Remove whitespaces and change to lower case to get the raw string
        String textRaw = text.toLowerCase().trim();
        // Test to see if text contains a punctuation that ends a sentence
        Boolean isEnd = textRaw.endsWith(".") || textRaw.endsWith("!")
                || textRaw.endsWith("?");
        return isEnd;
    }

    /**
     * Method returns whether or not a String ends with a sentence punctation
     * mark, with no text following that punctuation. Sentence-ending
     * punctuation includes <code>. || ! || ?</code>. Differs from the method
     * <code>isSentence</code> in that this method only returns true if the last
     * sentence in the specified <code>String txt</code> is ended with a
     * punctuation mark, whereas <code>isSentence</code> returns true if the
     * text area contains any sentence, not necessarily at the end.
     *
     * @return true if <code>txt</code> ends with sentence else false
     */
    public  Boolean endsInSentence() {
        // CASE: Does not contain a sentence (i.e. no sentence punctuation '.||?||!')
        if (!isSentence()) {
            return false;
            // CASE: Contains at least one sentence-ending punctuation mark
        } else {
            // Get last punctuation, either . || ! || ?
            int lastSentenceEndIndex = Math.max(Math.max(text.lastIndexOf('.'),
                    text.lastIndexOf('!')), text.lastIndexOf('?'));
            String substr = text.substring(lastSentenceEndIndex);
            // If there are non-space characters after lastSentenceEndIndex, 
            // txt does NOT end in sentence
            return !(substr.trim().length() > 1);
        }
    }

}
