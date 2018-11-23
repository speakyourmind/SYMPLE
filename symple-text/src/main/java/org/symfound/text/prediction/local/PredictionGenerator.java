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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import org.symfound.text.TextAnalyzer;
import org.symfound.text.prediction.local.generator.EditDistanceGenerator;
import org.symfound.text.prediction.local.generator.Generator;
import org.symfound.text.prediction.local.generator.PrefixMatchingGenerator;
import org.symfound.text.prediction.local.generator.WordSplittingGenerator;
import org.symfound.text.prediction.local.trie.Trie;

/**
 * This class is a delegate between all autocorrect functionality and the rest
 * of SYMPlatform and the common folder. <br>
 * PredictionGenerator.initialize() must be called when the program is
 * initialized, before generating suggestions. Then other methods can be called
 * for word suggestions, such as PredictionGenerator.generate.
 *
 * @author michaelweinstein
 */
public class PredictionGenerator {

    private static final String NAME = PredictionGenerator.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);
    // ======== Vars ======= //
    // Settings
    private Boolean led = true;
    private int led_dist = 3;
    private Boolean prefix = true;
    private Boolean whitespace = true;
    private Boolean smart_rank = false;

    // Dictionaries used
    private String[] dicts = {"SYMPLE/pred/sherlock.txt", "SYMPLE/pred/dictionary.txt"};
    private static String userDict = "user.txt";    // Static, because should be used for all generators once set
    private static int userDictWeight = 50;
    TextAnalyzer textAnalyzer = new TextAnalyzer();
    // Instance vars

    /**
     *
     */
    public static PredictionEngine engine;
    private String[] defaultSuggestions = {
        "YES", "NO", "THANK YOU", "PLEASE", "STOP"
    };

    /**
     * Basic functions of constructor:
     * <ul>
     * <li> Populates the <code>Trie</code> with words from dictionaries in
     * <code>dicts</code> </li>
     * <li> Creates generators </li>
     * <li> Instantiates the Engine with populated <code>Trie</code> </li>
     * </ul>
     *
     * @param dicts array of file paths pointing to dictionary txt files
     * @param editDistanceGenerator boolean to toggle edit distance words
     * @param wordsplitGenerator boolean to toggle word-splitting
     * @param prefixGenerator boolean to toggle prefix-matching
     * @param editDistance int edit distance range for words returned by
     * edit_dist_generator
     * @param userDictionary String path to the custom user dictionary
     * @param userDictionaryWeight int weight of words in user dictionary i.e.
     * multiplier of unigram/bigram frequency
     */
    public PredictionGenerator(String[] dicts, Boolean editDistanceGenerator,
            Boolean wordsplitGenerator, Boolean prefixGenerator,
            int editDistance, String userDictionary, int userDictionaryWeight) {
        this.dicts = dicts;
        this.led = editDistanceGenerator;
        this.whitespace = wordsplitGenerator;
        this.prefix = prefixGenerator;
        this.led_dist = editDistance;
        userDict = userDictionary;
        userDictWeight = userDictionaryWeight;
        initialize();
    }

    /**
     *
     */
    public PredictionGenerator() {

    }

    // ======== Methods ======= //
    /**
     * Called by constructor after instance vars have been set to args. Creates
     * the Trie data structure using words from specified dictionaries and
     * instantiates all required Generators. Then instantiates Engine class with
     * the populated Trie and list of Generators.
     */
    private void initialize() {
        /* Initialize trie with dictionaries from dict[] */
        // create empty trie to populate with words in dictionaries
        Trie trie = new Trie();
        // populate trie with dicts
        populateTrie(trie, dicts);

        /* Initialize generators specified by settings vars */
        List<Generator> generators = new ArrayList<>();
        //activate Levenshtein Edit Distance suggestions
        if (led) {
            generators.add(new EditDistanceGenerator(trie, led_dist));
        }
        //activate prefix matching suggestions
        if (prefix) {
            generators.add(new PrefixMatchingGenerator(trie));
        }
        //active word splitting suggestions
        if (whitespace) {
            generators.add(new WordSplittingGenerator(trie));
        }

        /* Instantiate Engine (after populateTrie has created ngram maps */
        engine = new PredictionEngine(trie, generators);
        // defaults to false
        engine.activateSmartRanker(smart_rank);
    }

    // TODO: Comment
    /**
     *
     * @param input
     * @param numWords
     * @param toCase
     * @return
     */
    public List<String> generateRaw(String input, int numWords, Boolean toCase) {
        //Quit on an empty line
        List<String> suggestions;
        if (!input.isEmpty() && input.charAt(input.length() - 1) == ' ') {
            // Maps words in bigram/unigram maps as they are parsed; no data persistence
            List<String> parsedInput = textAnalyzer.parseString(input, 1);
            String last_word = parsedInput.get(parsedInput.size() - 1);
            suggestions = Arrays.asList(engine.bigramSuggest(last_word, numWords, toCase));
            // CASE: Partial word has been entered
        } else {
            PredictionEngine.updateUserMap(input);	//for smart rank
            // Maps words in bigram/unigram maps as they are parsed; no data persistence
            List<String> parsedInput = textAnalyzer.parseString(input, 1);
            int size = parsedInput.size();
            String lastWord = parsedInput.get(size - 1);
            String prevInput = "";
            if (size > 1) {
                prevInput = parsedInput.get(size - 2);
            }

            // gets suggestions for last word in input
            suggestions = Arrays.asList(engine.suggest(prevInput, lastWord, numWords));

            // Case: If toCase==1, UPPER CASE. Else lower case
            String str;
            for (int i = 0; i < suggestions.size(); i++) {
                str = suggestions.get(i);
                String suggestion = (toCase) ? str.toUpperCase() : str.toLowerCase();
                suggestions.set(i, suggestion);
            }
        }
        return suggestions;
    }
/**
     *
     * @param input
     * @param numWords
     * @param toCase
     * @return
     */
    public List<String> generate(String input, Integer numWords, Boolean toCase) {
        List<String> suggestions = generateRaw(input, numWords, toCase);
        if (suggestions.size() < 1) {
            suggestions = (Arrays.asList(defaultSuggestions)).subList(0, numWords);
        }
        return suggestions;
    }

    /**
     * Sets the list of words that show up when the input has a trailing space.
     *
     * @param suggestions default suggestions ~5
     */
    public void setDefaultSuggestions(String[] suggestions) {
        defaultSuggestions = suggestions;
    }

    /**
     * Takes in a list of filenames specifying corpus of words, and a Trie, then
     * populates Trie with strings parsed from files. If there are multiple
     * corpora, this method uses words from all of them to populate one Trie.
     */
    private void populateTrie(Trie trie, String[] filenames) {
        // Add all non-custom dictionaries specified in dicts
        for (String file : filenames) {
            // Reset previous word pointer at start of each dictionary
            textAnalyzer.resetPrevWord();
            textAnalyzer.parseFile(file, 1).stream().forEach((word) -> {
                trie.addString(word);
            });
        }
        // Add custom user dictionary separately
        textAnalyzer.resetPrevWord();
        List<String> dict = textAnalyzer.parseFile(userDict, userDictWeight);
        textAnalyzer.resetPrevWord();
        dict.stream().forEach((word) -> {
            trie.addString(word);
        });
    }

    /**
     * Sets custom dictionary specific to a given user, which is updated
     * automatically as user types. Allows <code>userDict</code> to be changed
     * without re-instantiating <code>PredictionGenerator</code>.
     *
     * @param filepath to user dictionary to be stored in <code>userDict</code>
     * (i.e. src/ui/username/user.txt)
     */
    public static void setUserDictionary(String filepath) {
        userDict = filepath;
    }

    /**
     * Writes <code>String textToAdd</code> to the user's custom dictionary,
     * stored at <code>userDict</code> and set using static mutator. <br>
     * Appends a newline character before writing text to fill, then writes
     * sentence to end of file.
     *
     * @param textToAdd String to add to user's custom dictionary
     * @throws java.net.URISyntaxException
     */
    public void saveToUserDictionary(String textToAdd) throws URISyntaxException {
        // printline update
        LOGGER.info("SAVING to user dictionary: Adding line '"
                + textToAdd + "' to userDict '" + userDict + "'");
        File file = new File(userDict);

        // try-with-resources creates BufferedWriter that appends to end of file (FileWriter set to true)
        try (BufferedWriter br = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true))) {
            br.newLine();
            br.write(textToAdd);
        } catch (FileNotFoundException e) {
            LOGGER.fatal("Unable to find user dictionary file: " + userDict);
        } catch (IOException e) {
            LOGGER.fatal("Cannot read user dictionary: " + userDict);
        }
    }

}
