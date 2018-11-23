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
package org.symfound.text.prediction.local.util;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Javed Gangjee
 */
public class ProbabilityModel {

    /**
     * Maps occurrences of single words in the corpus. Only needs to be
     * calculated once to populate a Map when corpus is originally filled from
     * file.
     *
     * @param wordlist
     * @return
     */
    public static HashMap<String, Integer> unigram(List<String> wordlist) {
        HashMap<String, Integer> probs = new HashMap<>();
        wordlist.forEach((string) -> {
            if (probs.containsKey(string)) {
                probs.put(string, probs.get(string) + 1);
            } else {
                probs.put(string, 1);
            }
        });
        return probs;
    }

    /**
     * Returns number of times a given word follows a prev_word in the corpus,
     * passed as wordlist.
     *
     * @param prev_word
     * @param curr_word
     * @param wordlist
     * @return
     */
    public static int bigram(String prev_word, String curr_word,
            List<String> wordlist) {
        int bigram = 0;
        for (int i = 1; i < wordlist.size(); i++) {
            String word = wordlist.get(i);
            if (word.equals(curr_word)) {
                if (wordlist.get(i - 1).equals(prev_word)) {
                    bigram += 1;
                }
            }
        }
        return bigram;
    }
}
