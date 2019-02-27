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
package org.symfound.text.prediction.local.trie;

import java.util.HashMap;

/**
 *
 * @author Javed Gangjee
 */
public class TrieNode {

    //Character associated with this node

    /**
     *
     */
    public final char character;

    /*Root should have no relevant character value*/
    private boolean _isRoot;

    /*Keeps track of parent in order to concatenate 
     * characters into string all the way up to the root.*/
    private TrieNode _parent;

    //Each node keeps track of its direct children in Hash 
    private final HashMap<Character, TrieNode> _children;

    //Whether or not a word ends at this node
    private boolean _hasWordFlag;

    /**
     *
     * @param val
     */
    public TrieNode(char val) {
        this.character = val;
        _hasWordFlag = false;
        _isRoot = false;

        _children = new HashMap<>();

        _parent = null;
    }

    /**
     *
     * @return
     */
    public HashMap<Character, TrieNode> children() {
        return _children;
    }

    /**
     *
     * @return
     */
    public boolean hasChildren() {
        return _children.size() > 0;
    }

    /**
     *
     */
    public void setRoot() {
        _isRoot = true;
    }

    /**
     *
     * @return
     */
    public boolean isRoot() {
        return _isRoot;
    }

    /**
     *
     * @param marksWord
     */
    public void flagWord(boolean marksWord) {
        _hasWordFlag = marksWord;
    }

    /**
     *
     * @return
     */
    public boolean isFlagged() {
        return _hasWordFlag;
    }

    /**
     *
     * @param parent
     */
    public void setParent(TrieNode parent) {
        _parent = parent;
    }

    /**
     * Returns string which concatenates characters of parents up until root
     * node. Add this node's char.
     *
     * @return
     */
    public String getString() {
        if (isRoot()) {
            return "";
        }
        return _parent.getString() + character;
    }
}
