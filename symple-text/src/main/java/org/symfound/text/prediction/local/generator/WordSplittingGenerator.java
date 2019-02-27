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
package org.symfound.text.prediction.local.generator;

import java.util.ArrayList;
import java.util.List;
import org.symfound.text.prediction.local.trie.Trie;

/**
 *
 * @author Javed Gangjee
 */
public class WordSplittingGenerator implements Generator {

    private final Trie _trie;

    /**
     *
     * @param trie
     */
    public WordSplittingGenerator(Trie trie) {
        _trie = trie;
    }

    /**
     *
     * @param input
     * @return
     */
    @Override
    public List<String> generateSuggestions(String input) {
        List<String> suggs = new ArrayList<>();
        char[] chars = input.toCharArray();
        //Check starting at first word
        for (int i = 1; i < chars.length; i++) {
            //Second word, substring until end of input string
            String sbstrng = input.substring(i);
            //Check if trie contains word formed by second substring, from index to end of string
            if (_trie.containsWord(sbstrng)) {
                //Check first word, substring from start to index of index i
                String prestrng = input.substring(0, i);
                if (_trie.containsWord(prestrng)) {

                    String splitWord = prestrng + " " + sbstrng;
                    if (!suggs.contains(splitWord)) {
                        suggs.add(splitWord);
                    }
                }
            }
        }
        return suggs;
    }
}
