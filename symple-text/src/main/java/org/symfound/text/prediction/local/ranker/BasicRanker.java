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
package org.symfound.text.prediction.local.ranker;

import java.util.HashMap;
import org.symfound.text.prediction.local.PredictionEngine;
import org.symfound.text.prediction.local.trie.Trie;

/**
 * This class extends Comparator so it can be passed into Collections.sort
 * method. Compares only two strings at a time, according to the specifications
 * outlined in the assignment procedures (equality, bigram probability, unigram
 * probability, and alphabetical order). Has a _prevWord field which should be
 * set before compare() is called, used only in the bigram probability. If no
 * _prevWord was set, then the bigram probability ranking specification is
 * skipped. Ranker is not an interface because all four specifications are
 * compared in a single compare() method, so you only have to make a single
 * method call to Collections.sort, passing in a single Ranker object.
 */
public class BasicRanker implements Ranker {

//	private Trie _trie;
//	private boolean _bigramRank;
    private final String _prevWord;
    private final HashMap<String, Integer> _userMap;
    private boolean _smartRank;

    /**
     *
     * @param trie
     * @param prev
     * @param userMap
     */
    public BasicRanker(Trie trie, String prev, HashMap<String, Integer> userMap) {
//		_trie = trie;
//		_bigramRank = true;
        _prevWord = prev;
        _userMap = userMap;
        _smartRank = false;
    }

    /**
     *
     * @param smartRank
     */
    public void setSmartRank(boolean smartRank) {
        _smartRank = smartRank;
    }

    /**
     * Called to rank from compare() if smart ranking is activated.
     */
    private int smartRank(String str1, String str2) {
        if (_userMap.containsKey(str1) && _userMap.containsKey(str2)) {
            if (_userMap.get(str1) > _userMap.get(str2)) {
                return 1;
            } else if (_userMap.get(str1) < _userMap.get(str2)) {
                return -1;
            }
        } else if (_userMap.containsKey(str1) && !_userMap.containsKey(str2)) {
            return -1;
        } else if (!_userMap.containsKey(str1) && _userMap.containsKey(str2)) {
            return 1;
        }
        return 0;
    }

    /* Should maybe take in a Suggestion object, 
     * which stores previous words.
     * So in comparing two strings*/
    /**
     * Return values expected by Comparator's compare method:
     *
     * less than - Negative integer equals - Zero greater than - Positive
     * integer
     *
     * @param str1
     * @param str2
     * @return 
     */
    @Override
    public int compare(String str1, String str2) {

        // Check bigram/unigram on first word only, if pairs of words (according to assignment spec)
        String[] split1 = str1.split(" ");
        if (split1.length > 1) {
            str1 = split1[0];
        }
        String[] split2 = str2.split(" ");
        if (split2.length > 1) {
            str2 = split2[0];
        }

        //Smart Rank: user use frequency, activated first if user has used the word, skipped if tied
        if (_smartRank) {
            int smart = smartRank(str1, str2);
            if (smart != 0) {
                return smart;
            }
        }
        // TODO Remove block to BigramRanker?
        //2: bigram probability
        if (_prevWord != null && PredictionEngine.getBigramMap() != null) {
            HashMap<String, Integer> prevHash = PredictionEngine.getBigramMap().get(_prevWord);
            int str1Prob = 0;
            int str2Prob = 0;
            if (prevHash.containsKey(str1)) {
                str1Prob = prevHash.get(str1);
            }
            if (prevHash.containsKey(str2)) {
                str2Prob = prevHash.get(str2);
            }
            //check for one of the strings having a greater probability,
            if (str1Prob > str2Prob) {
                return 1;
            } else if (str1Prob < str2Prob) {
                return -1;
            }
            //but if there's a tie, continue to unigram probability
        }
        // TODO Remove block to UnigramRanker?
        //3: unigram probability (if bigram probability ties)
        //Get histogram (for unigram probabilities)
        if (PredictionEngine.getUnigramMap() != null) {
            HashMap<String, Integer> unigram = PredictionEngine.getUnigramMap();
            if (unigram.containsKey(str1) && unigram.containsKey(str2)) {
                if (unigram.get(str1) > unigram.get(str2)) {
                    return 1;
                } else if (unigram.get(str1) < unigram.get(str2)) {
                    return -1;
                }
            }
        }

        return str2.compareTo(str1);

    }
}
