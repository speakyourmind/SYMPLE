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
import org.symfound.text.prediction.local.trie.TrieNode;
import org.symfound.text.prediction.local.util.EditDistance;

/**
 *
 * @author Javed Gangjee
 */
public class EditDistanceGenerator implements Generator {

    private Trie _trie;
    private int _editDist;

    /**
     * Constructors, can either set editDist or editDist gets set to default of
     * 3.
     *
     * @param trie
     */
    public EditDistanceGenerator(Trie trie) {
        _trie = trie;
        _editDist = 3;
    }

    /**
     *
     * @param trie
     * @param edit_distance
     */
    public EditDistanceGenerator(Trie trie, int edit_distance) {
        _trie = trie;
        _editDist = edit_distance;
    }

    /**
     * A search function that assumes the previous row is already filled in.
     * Useful when using trie for editfind distance, because you don't need to
     * fill in the whole matrix at each row since the trie's structure extends
     * from shared prefixes. Uses the Hanov algorithm for a more efficient
     * search of the trie for edit distance, storing only two rows at a time
     * instead of entire cost matrix.
     */
    private void recursiveSearch(TrieNode node, String string, int[] prevRow, List<String> results) {
        //columns for each letter of string to match, plus empty space
        int col_num = string.length() + 1;
        //create next row from info with prevRow
        int[] currRow = new int[col_num];
        currRow[0] = prevRow[0] + 1;
        for (int c = 1; c < col_num; c++) {
            //insert ops
            int aCost = currRow[c - 1] + 1;
            //delete ops
            int bCost = prevRow[c] + 1;
            //to replace or not
            int replace;
            if (string.charAt(c - 1) != node.character) {
                replace = 1;
            } else {
                replace = 0;
            }
            int cCost = prevRow[c - 1] + replace;
            //minimum cost
            currRow[c] = EditDistance.minimum(aCost, bCost, cCost);
        }
        //if we're at end of word, compare to cost in last index of curr row
        if (node.isFlagged()) {
            int endCost = currRow[currRow.length - 1];
            if (endCost <= _editDist) {
                results.add(node.getString());
            }
        }
        //if word not complete, compare against minimum cost in curr row
//		else {
        //find minimum of row and check cost against max edit dist
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < currRow.length; i++) {
            if (currRow[i] < min) {
                min = currRow[i];
            }
        }
        if (min <= _editDist) {
            for (TrieNode child : node.children().values()) {
                recursiveSearch(child, string, currRow, results);
            }
        }
//		}
    }

    /**
     * Generates suggestions under a certain edit distance using the
     * recursiveSearch algorithm, which stores two rows of the string matrix for
     * edit distance at a time.
     * @return 
     */
    @Override
    public List<String> generateSuggestions(String input) {
        //populate the first row
        int[] currRow = new int[input.length() + 1];
        for (int i = 0; i < currRow.length; i++) {
            currRow[i] = i;
        }
        List<String> results = new ArrayList<String>();
        //recursively search and check edit distance, starting at root
        TrieNode node = _trie.getRoot();
        for (TrieNode child : node.children().values()) {
            recursiveSearch(child, input, currRow, results);
        }
        return results;
    }

    /**
     * Set edit distance for suggestions returned.
     * @return 
     */
    public int getMaxEditDistance() {
        return _editDist;
    }

    /**
     *
     * @param dist
     */
    public void setMaxEditDistance(int dist) {
        _editDist = dist;
    }

}
