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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import org.apache.log4j.Logger;
import org.symfound.text.TextAnalyzer;
import org.symfound.text.prediction.local.generator.Generator;
import org.symfound.text.prediction.local.ranker.BasicRanker;
import org.symfound.text.prediction.local.trie.Trie;

/**
 *
 * @author Javed Gangjee
 */
public class PredictionEngine {

    private static final String NAME = PredictionEngine.class.getName();
    private static final Logger LOGGER = Logger.getLogger(NAME);
    private final List<Generator> generators;
    private final Trie trie;
    private boolean smartRanker;

    private static HashMap<String, Integer> userMap;

    //Takes in all Generators specified by user-inputted arguments
    /* default */
    PredictionEngine(Trie trie, List<Generator> gs) {
        generators = gs;
        this.trie = trie;
        smartRanker = false;
        userMap = new HashMap<>();
    }

    /**
     * Keeps track of user input for frequency map.
     */
    /* default */ static void updateUserMap(String word) {
        if (userMap.containsKey(word)) {
            userMap.put(word, userMap.get(word) + 1);
        } else {
            userMap.put(word, 1);
        }
    }

    private static HashMap<String, Integer> unigramMap;

    /**
     * Store Unigram map when Engine is created, so it only needs to calculate
     * it once.
     *
     * @return
     */
    public static HashMap<String, Integer> getUnigramMap() {
        if (unigramMap == null) {
            if (TextAnalyzer.unigramMap != null) {
                unigramMap = TextAnalyzer.unigramMap;
            } else {
                LOGGER.info("TextParser.unigramMap is null. Need to parse text files first! (Engine.setUnigramMap)");
            }
        }
        return unigramMap;
    }

    private static HashMap<String, HashMap<String, Integer>> bigramMap;

    /**
     *
     * @return
     */
    public static HashMap<String, HashMap<String, Integer>> getBigramMap() {
        if (bigramMap == null) {
            if (TextAnalyzer.bigramMap != null) {
                bigramMap = TextAnalyzer.bigramMap;
            } else {
                LOGGER.info("bigramMap is null. Need to parse text files first!");
            }
        }
        return bigramMap;
    }

    /**
     * Turn on or off smartRanker specification in BasicRanker's compare method.
     */
    /* default */ void activateSmartRanker(boolean activate) {
        smartRanker = activate;
    }

    /**
     * Returns an array of top 5 suggestions given a current word and a previous
     * word (if there is one). Previous word can be null if there is not one,
     * because it will simply skip the bigram specification in the Ranking
     * (Comparator) sorting algorithm.
     */
    /* default */ String[] suggest(String previous, String current, Integer total) {
        current = current.toLowerCase();
        //First: compile one list of all suggestions from Engine Generators
        List<String> suggestions = new ArrayList<>();

        for (Generator g : generators) {
            List<String> genMatches = g.generateSuggestions(current);
            suggestions.addAll(genMatches);
        }

        //Second: Rank suggestions based on four prioritized specifications
        //rank suggestions based on ranking specifications in Ranker (extends Comparator)
        BasicRanker r = new BasicRanker(trie, previous, userMap);
//		r.setPreviousWord(prev_word);
        //smartRank is initialized to false in BasicRanker
        if (smartRanker) {
            r.setSmartRank(true);
        }

        Collections.sort(suggestions, r);
        //comparator requires reverse to get list sorted by relevance
        Collections.reverse(suggestions);

        //Third: Make array for up to 5 suggestions from rank-sorted list		
        int num = suggestions.size() >= total ? total : suggestions.size();
        //If no suggestions, return input
        /*    if (num == 0) {
         if (trie.contains(current)) {
         String[] words = {current};
         return words;
         }
         }*/
        String[] words = new String[num];

        int start = 0;
        // TODO If the first word is a valid curr_word
        // Adds first 0-2 words to word array
//		// First add exact input to words array if it is a valid word
//		if (_trie.containsWord(curr_word)) {
//			words[0] = curr_word;
//			start = 1;		
//			// Add next word to array, if non-exact suggestion stored in 0-index
//			if (!suggestions.isEmpty() && !suggestions.get(0).equals(curr_word)) {
//				words[1] = suggestions.get(0);
//				start = 2;
//			}
//		}        
        // Add up to first numWords suggestions to returned word array
        for (int i = start; i < num; i++) {
            words[i] = suggestions.get(i);
        }
        return words;
    }

    /**
     * Uses <code>String prev_word</code> as first word of bigram, and predicts
     * next word based on frequency of the bigrams (word pairs) logged in
     * <code>bigramMap</code>. Returns top <code>numSuggestions</code> second
     * words of bigram by frequency.
     *
     * @param previous first word in bigram is last word user entered
     * @param total size of array String[numSuggestions] returned
     * @param toCase
     * @return top <code>numSuggestions</code> words prioritized by bigram
     * frequency
     */
    public String[] bigramSuggest(String previous, int total, Boolean toCase) {
        String[] topHits;
        // If prev_word exists as a "First Word" in the bigramMap
        if (getBigramMap().containsKey(previous)) {
            // Get Value of Key = "First Word" as a Map in form <Second Word, Bigram Frequency>
            HashMap<String, Integer> valueMap = getBigramMap().get(previous);
            // Instantiate a PriorityQueue that prioritizes by Entries by Entry.value
            PriorityQueue<Map.Entry<String, Integer>> queue = new PriorityQueue<>(total,
                    (Map.Entry<String, Integer> e0, Map.Entry<String, Integer> e1) -> e1.getValue().compareTo(e0.getValue()));
            // Add each <Key, Value> pair (i.e. <Second Word, Frequency>) to PriorityQueue
            valueMap.entrySet().stream().forEach((valEntry) -> {
                queue.add(valEntry);
            });
            // Store top 5 results from PriorityQueue in arr		
            int num = queue.size() >= total ? total : queue.size();
            topHits = new String[num];
            for (int i = 0; i < total; i++) {
                Map.Entry<String, Integer> head = queue.poll();
                // CASE: Less than 'numSuggestions' bigrams, break to avoid NullPointerException
                if (head == null) {
                    break;
                }
                String key = head.getKey();
                // CASE: If toCase==1, UPPER CASE. Else lower case
                topHits[i] = (toCase) ? key.toUpperCase() : key.toLowerCase();
            }
        } else {
            LOGGER.info("Bigram Map does not contain word " + previous
                    + ". Returning empty string array");
            topHits = new String[0];
        }
        return topHits;
    }
}
